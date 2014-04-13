package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyBook
import woko.facets.WokoFacetContext
import woko.facets.builtin.all.RenderPropertyValueEditStripesText

//@FacetKey(name="renderPropertyValueEdit_description", profileId="all", targetObjectType=MyBook.class)
class RenderPropertyValueEdit_descriptionMyBook extends RenderPropertyValueEditStripesText {

    @Override
    boolean isTextArea() {
        return true
    }
}
