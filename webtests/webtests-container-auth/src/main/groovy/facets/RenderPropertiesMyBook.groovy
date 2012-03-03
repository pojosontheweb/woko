package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyBook
import woko.facets.builtin.all.RenderPropertiesImpl

@FacetKey(name="renderProperties", profileId="all", targetObjectType=MyBook.class)
class RenderPropertiesMyBook extends RenderPropertiesImpl {

    @Override
    List<String> getPropertyNames() {
        def props = []
        props.addAll(super.getPropertyNames())
        props << "notexistingprop" // add non existing prop to test that is doesn't crash !
        return props
    }

}
