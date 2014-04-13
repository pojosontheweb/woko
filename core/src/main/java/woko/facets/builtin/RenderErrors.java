package woko.facets.builtin;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.stripes.validation.ValidationErrors;
import woko.facets.FragmentFacet;

/**
 * Fragment facet used to render Stripes validation errors.
 */
public interface RenderErrors extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderErrors";

    public ValidationErrors getErrors();

    public boolean isEscapeXml();

}