package woko.ext.blobs.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.blobs.BlobObject;
import woko.facets.builtin.RenderLinks;
import woko.facets.builtin.all.Link;
import woko.facets.builtin.all.RenderLinksImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.LinkUtil;

import java.util.ArrayList;
import java.util.List;

@FacetKey(name = RenderLinks.FACET_NAME, profileId = "developer", targetObjectType = BlobObject.class)
public class RenderLinksBlobObjectDeveloper<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderLinksImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public List<Link> getLinks() {
        ArrayList<Link> links = new ArrayList<Link>();
        links.addAll(super.getLinks());
        BlobObject blob = (BlobObject)getFacetContext().getTargetObject();
        String text = getWoko().getLocalizedMessage(getRequest(), "woko.ext.blobs.download.link");
        Link newLink = new Link(LinkUtil.getUrl(getWoko(), blob, DownloadBlob.FACET_NAME), text);
        links.add(newLink);
        return links;
    }
}
