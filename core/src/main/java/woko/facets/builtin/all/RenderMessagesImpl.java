package woko.facets.builtin.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.stripes.action.Message;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderMessages;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.List;

public class RenderMessagesImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends BaseFragmentFacet<OsType, UmType, UnsType, FdmType> implements RenderMessages {

    private static final String JSP_PATH = "/WEB-INF/woko/jsp/all/renderMessages.jsp";

    @Override
    public String getPath() {
        return JSP_PATH;
    }

    @SuppressWarnings("unchecked")
    public List<Message> getMessages() {
        return (List<Message>)getFacetContext().getTargetObject();
    }
}
