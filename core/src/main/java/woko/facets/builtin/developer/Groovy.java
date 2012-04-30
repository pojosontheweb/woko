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
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONException;
import org.json.JSONObject;
import woko.facets.BaseResolutionFacet;

import java.io.PrintWriter;
import java.io.StringWriter;

@FacetKey(name="groovy",profileId="developer")
public class Groovy extends BaseResolutionFacet {

  private String code;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

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
    } catch(Throwable t) {
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
