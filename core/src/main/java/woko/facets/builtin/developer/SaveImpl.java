package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.rpc.RpcResolutionWrapper;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Edit;
import woko.facets.builtin.Json;
import woko.facets.builtin.Save;
import woko.facets.builtin.Validate;
import woko.util.WLogger;

@FacetKey(name = "save", profileId = "developer")
public class SaveImpl extends BaseResolutionFacet implements Save {

    private final static WLogger logger = WLogger.getLogger(SaveImpl.class);

    public Resolution getResolution(final ActionBeanContext abc) {
        // try to find a validation facet for the object
        final WokoFacetContext facetContext = getFacetContext();
        final Woko woko = facetContext.getWoko();
        final Object targetObject = facetContext.getTargetObject();
        Class<?> clazz = targetObject.getClass();
        Validate validateFacet = (Validate) woko.getFacet("validate", abc.getRequest(), targetObject, clazz);
        if (validateFacet != null) {
            logger.debug("Validation facet found, validating before saving...");
            if (validateFacet.validate(abc)) {
                doSave(abc);
            } else {
                logger.debug("Validate facet raised validation errors, not saving");
                // forward to the edit fragment
                Edit editFacet = (Edit) woko.getFacet("edit", abc.getRequest(), targetObject, clazz, true);
                return new ForwardResolution(editFacet.getFragmentPath());
            }
        } else {
            logger.debug("No validation facet found, saving...");
            doSave(abc);
        }

        Resolution resolution = new RedirectResolution(woko.facetUrl("edit",targetObject));

        return new RpcResolutionWrapper(resolution) {
            @Override
            public Resolution getRpcResolution() {
                Json json = (Json)woko.getFacet("json", facetContext.getRequest(), targetObject);
                return json==null ? null : json.getResolution(abc);
            }
        };


    }

    protected void doSave(ActionBeanContext abc) {
        WokoFacetContext facetContext = getFacetContext();
        facetContext.getWoko().getObjectStore().save(facetContext.getTargetObject());
        abc.getMessages().add(new SimpleMessage("Object saved"));
    }

}
