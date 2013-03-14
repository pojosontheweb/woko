package test.facet.pkg

import net.sourceforge.jfacets.annotations.FacetKey
import test.MyEntityWithTinyMCE
import woko.ext.raptor.RenderPropertyValueEditTinyMCE

@FacetKey(name="renderPropertyValueEdit_foo", profileId="developer", targetObjectType = MyEntityWithTinyMCE.class)
class RenderPropValueFooMyEntityWithTinyMCE extends RenderPropertyValueEditTinyMCE {
}
