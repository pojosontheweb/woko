package facets

import woko.facets.builtin.all.LayoutAll
import net.sourceforge.jfacets.annotations.FacetKey

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 04/03/12
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
@FacetKey(name="layout", profileId="all")
class RpcLayout extends LayoutAll {

    @Override
    String getAppTitle() {
        return "RPC tests"
    }


}
