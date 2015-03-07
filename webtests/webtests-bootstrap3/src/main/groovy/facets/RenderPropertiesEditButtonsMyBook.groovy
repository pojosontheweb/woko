package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyBook
import woko.facets.builtin.all.RenderPropertiesEditButtonsImpl

@FacetKey(name = "renderPropertiesEditButtons", profileId = "developer", targetObjectType = MyBook.class)
class RenderPropertiesEditButtonsMyBook extends RenderPropertiesEditButtonsImpl {

    @Override
    String getPath() {
        return "/WEB-INF/jsp/buttonsMyBook.jsp"
    }
}
