package woko.facets.builtin;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.stripes.action.Message;
import woko.facets.FragmentFacet;

import java.util.List;

/**
 * Fragment facet used to render Stripes messages.
 */
public interface RenderMessages extends IFacet, FragmentFacet {

    static final String FACET_NAME = "renderMessages";

    public List<Message> getMessages();

    public boolean isEscapeXml();

}