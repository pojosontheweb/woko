/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.tooling.utils

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.protocol.HttpContext
import org.apache.http.client.methods.HttpGet
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.util.EntityUtils
import org.apache.http.client.methods.HttpPost
import org.apache.http.params.HttpParams
import org.apache.http.params.BasicHttpParams
import org.apache.http.HttpVersion
import org.apache.http.params.HttpProtocolParamBean
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.client.protocol.ClientContext
import org.apache.http.client.CookieStore
import org.apache.maven.model.Dependency
import org.apache.http.message.BasicNameValuePair
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.ResponseHandler
import org.apache.http.client.params.ClientPNames

class AppHttpClient {

    private final HttpClient httpClient = new DefaultHttpClient()
    private final String url

    private HttpContext ctx = null

    private final Logger logger
    private final PomHelper pomHelper

    AppHttpClient(Logger logger, String url, PomHelper pomHelper) {
        this.url = url
        this.logger = logger
        this.pomHelper = pomHelper
        httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true)
    }

    private boolean isBuiltInAuth() {
        for (Dependency d : pomHelper.model.dependencies) {
            if (d.artifactId=="woko-builtin-auth-web") {
                return true
            }
        }
        return false
    }

    def doWithLogin(String username, String password, Closure c) {
        HttpContext context = new BasicHttpContext()
        CookieStore cookieStore = new BasicCookieStore()
        context.setAttribute(ClientContext.COOKIE_STORE, cookieStore)
        def bia = isBuiltInAuth()
        String usernameParam = bia ? "username" : "j_username"
        String passwordParam = bia ? "password" : "j_password"
        String handler = bia ? "login" : "j_security_check"
        String additionalParam = bia ? "&login=true" : ""

        String loginUrl = "$url/$handler?$usernameParam=$username&$passwordParam=$password$additionalParam"
        HttpRequest req = new HttpGet(loginUrl)
        httpClient.execute(req, { HttpResponse response ->
            try {
                if (response.statusLine.statusCode==401) {
                    println "401 : auth failed !"
                    //throw new IllegalArgumentException("Authentication failed")
                }
            } finally {
                if (response) {
                    String resp = responseToString(response)
                    EntityUtils.consume(response.entity)
                }
            }
            ctx = context
            logger.indentedLog("Authenticated $username on $url")

        } as ResponseHandler, context)
        c()
    }

    private String responseToString(HttpResponse response) {
        StringWriter sw = new StringWriter()
        try {
            response.entity.content.withReader { r->
                sw << r
            }
            sw.flush()
        } finally {
            EntityUtils.consume(response.entity)
        }
        return sw.toString()
    }

    def post(String relativeUrl, Map<String,String> parameters, Closure c) {
        HttpPost post = new HttpPost("$url$relativeUrl")
        post.addHeader("content-type", "application/x-www-form-urlencoded");
        HttpParams params = new BasicHttpParams()
        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params)
        paramsBean.setVersion(HttpVersion.HTTP_1_1)
        paramsBean.setContentCharset("UTF-8")

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        parameters.each { k,v ->
            formparams.add(new BasicNameValuePair(k, v));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);
        post.setEntity(entity)
        String resultString = null
        httpClient.execute(post, { HttpResponse response ->
            def sc = response.statusLine.statusCode
            if (sc==404) {
                throw new IllegalArgumentException("404 : $url$relativeUrl not found !")
            } else if (sc==500) {
                def ticket = getWokoErrorTicket(response)
                throw new IllegalArgumentException("Server-side error ! Check the logs (ticket=$ticket)")
            }
            resultString = responseToString(response)
        } as ResponseHandler, ctx)
        c(resultString)
    }

    String getWokoErrorTicket(HttpResponse response) {
        def h = response.getFirstHeader("wokoErrorTicket")
        if (h) {
            return h.value
        }
        return null
    }

}
