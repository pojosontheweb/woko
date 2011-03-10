package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseFacet
import woko.facets.builtin.RenderPropertyValueJson
import javax.servlet.http.HttpServletRequest
import org.json.JSONArray
import woko.util.WLogger

@FacetKey(name='renderPropertyValueJson', profileId='all', targetObjectType=Collection.class)
class RenderPropertyValueJsonCollection extends BaseFacet implements RenderPropertyValueJson {

  private static final WLogger logger = WLogger.getLogger(RenderPropertyValueJsonCollection.class)

  Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    JSONArray arr = new JSONArray()
    propertyValue.each { item ->
      logger.debug("converting collection item $item to json...")
      if (item==null) {
        arr.put(null)
      }
      RenderPropertyValueJson rpvj = (RenderPropertyValueJson)facetContext.woko.getFacet('renderPropertyValue', request, item)
      if (rpvj==null) {
        logger.debug("... no renderPropertyValueJson facet found for collection item, adding null")
        arr.put(null)
      } else {
        def json = rpvj.propertyToJson(request, item)
        logger.debug("... converted item $item to $json")
        arr.put(json)
      }            
    }
    return arr
  }

}
