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

class AppHttpClient {

    private final HttpClient httpClient = new DefaultHttpClient()
    private final String url

    private HttpContext ctx = null

    AppHttpClient(String url) {
        this.url = url
    }

    def doWithLogin(String username, String password, Closure c) {
        HttpRequest req = new HttpGet("$url/login?username=$username&password=$password&login=true")
        HttpResponse response = httpClient.execute(req)
        try {
            if (response.statusLine.statusCode==401) {
                throw new IllegalArgumentException("Authentication failed")
            }
        } finally {
            if (response) {
                EntityUtils.consume(response.entity)
            }
        }
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
        HttpPost post = new HttpPost("$url/relativeUrl")
        HttpParams params = new BasicHttpParams()
        HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params)
        paramsBean.setVersion(HttpVersion.HTTP_1_1)
        paramsBean.setContentCharset("UTF-8")
        parameters.each { k,v ->
            params.setParameter(k, v)
        }
        post.setParams(params)
        HttpResponse response = httpClient.execute(post)
        def sc = response.statusLine.statusCode
        if (sc==404) {
            throw new IllegalArgumentException("404 : $url$relativeUrl not found !")
        } else if (sc==500) {
            def ticket = getWokoErrorTicket(response)
            throw new IllegalArgumentException("Server-side error ! Check the logs (ticket=$ticket)")
        }
        String resultString = responseToString(response)
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
