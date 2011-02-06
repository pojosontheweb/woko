package woko.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import org.json.JSONObject
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name='groovy',profileId='developer')
class Groovy extends BaseResolutionFacet {

  String code

  Resolution getResolution(ActionBeanContext abc) {
    JSONObject result = new JSONObject()
    StringWriter sw = new StringWriter()
    PrintWriter out = new PrintWriter(sw)
    Binding b = new Binding([
            request: abc.request,
            woko: facetContext.woko,
            log: { msg ->
              out << "$msg\n"
            }
    ])
    GroovyShell shell = new GroovyShell(b);
    long time = System.currentTimeMillis();
    try {
      shell.evaluate(code);
    } catch(Throwable t) {
      out << "ERROR !\n"
      t.printStackTrace(out);
    } finally {
      out.flush()
      out.close()
    }
    long elapsed = System.currentTimeMillis() - time;

    String outStr = "Code executed, Took $elapsed ms.\nLog :\n" + sw.toString()
    result.put("log", outStr)
    return new StreamingResolution('text/json', result.toString())
  }


}
