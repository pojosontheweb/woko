package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import woko.persistence.DbExporter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FacetKey(name="export", profileId="developer")
public class ExportDatabase extends BaseResolutionFacet {

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        return new StreamingResolution("text/json") {
            @Override
            protected void stream(HttpServletResponse response) throws Exception {
                DbExporter e = new DbExporter();
                HttpServletRequest request = getFacetContext().getRequest();
                ServletContext ctx = request.getSession().getServletContext();
                e.export(Woko.getWoko(ctx), response.getOutputStream(), request);
            }
        }.setFilename("dump" + System.currentTimeMillis() + ".zip");
    }

}
