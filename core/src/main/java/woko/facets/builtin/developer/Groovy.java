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

  public Resolution getResolution() {
    ActionBeanContext abc = getContext();
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
