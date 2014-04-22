package woko.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;

/**
 * Allows to expose a {@link woko.facets.FragmentFacet} as a {@link woko.facets.ResolutionFacet}, so
 * that clients can grab a subset of the whole HTML.
 * Allows to factor out server-side fragments and reuse them in AJAX environments.
 */
public class BaseFragmentToResolutionFacet<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    /**
     * The default suffix used in the facet name.
     */
    public static final String FACET_SUFFIX = "_toResolution";

    @Override
    public Resolution getResolution(ActionBeanContext abc) {

        WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        Object targetObject = facetContext.getTargetObject();
        Class<?> targetClass;
        if (targetObject!=null) {
            targetClass = targetObject.getClass();
        } else {
            targetClass = facetContext.getFacetDescriptor().getTargetObjectType();
        }
        HttpServletRequest request = getRequest();
        Object facet = getWoko().getFacet(getFragmentFacetName(), request, targetObject, targetClass, true);

        if (!(facet instanceof FragmentFacet)) {
            throw new IllegalStateException("facet " + facet + " is not a FragmentFacet");
        }

        FragmentFacet ff = (FragmentFacet)facet;
        String fragmentPath = ff.getFragmentPath(request);

        return new ForwardResolution(fragmentPath);
    }

    /**
     * Return the name of the fragment facet. This implementation
     * uses naming convention in order to extract the fragment facet name
     * from this facet's key. The '_toResolution' suffix is used by default.
     */
    protected String getFragmentFacetName() {
        String facetName = getFacetContext().getFacetDescriptor().getName();
        int lastIndex = facetName.lastIndexOf(FACET_SUFFIX);
        if (lastIndex!=-1) {
            return facetName.substring(0, lastIndex);
        } else {
            throw new IllegalStateException(this + " : Error trying to get fragment facet name from " +
                "this facet's key. This facet's name (" + facetName + ") doesn't end with the suffix " +
                FACET_SUFFIX + ". Please rename this facet so that it matches a fragment facet name, or override " +
                "this method in order to return the name you want.");
        }
    }
}
