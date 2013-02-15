/*
 * Copyright 2001-2012 Remi Vankeisbelck
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

package woko.facets.builtin.developer;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONException;
import org.json.JSONObject;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Execute arbitrary Groovy code on the server.
 *
 * Available only to <code>developer</code> users by default. Override for your role(s) in
 * order to make this available for your users.
 */
@FacetKey(name = "groovy", profileId = "developer")
public class Groovy<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType, UmType, UnsType, FdmType> {

    /**
     * The Groovy code to be executed
     */
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Execute the Groovy code in a <code>GroovyShell</code>.
     * Available binding variables are :
     * <ul>
     *     <li>request (HttpServletRequest) : the request</li>
     *     <li>woko (Woko) : the Woko instance for the app</li>
     *     <li>log (PrintWriter) : a logging buffer that will be returned after execution</li>
     * </ul>

     * @param abc the action bean context
     * @return a JSON representation of the exec (with the log or exception if any)
     */
    public Resolution getResolution(ActionBeanContext abc) {
        JSONObject result = new JSONObject();
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        Binding b = new Binding();
        b.setVariable("request", abc.getRequest());
        b.setVariable("woko", getFacetContext().getWoko());
        b.setVariable("log", out);
        GroovyShell shell = new GroovyShell(b);
        long time = System.currentTimeMillis();
        try {
            shell.evaluate(code);
        } catch (Throwable t) {
            out.write("ERROR !\n");
            t.printStackTrace(out);
        } finally {
            out.flush();
            out.close();
        }
        long elapsed = System.currentTimeMillis() - time;

        String outStr = "Code executed, Took " + elapsed + "ms.\nLog :\n" + sw.toString();
        try {
            result.put("log", outStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new StreamingResolution("text/json", result.toString());
    }


}
