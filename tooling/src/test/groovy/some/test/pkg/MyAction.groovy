package some.test.pkg

import woko.actions.BaseActionBean
import net.sourceforge.stripes.action.StrictBinding

@StrictBinding(
    defaultPolicy=StrictBinding.Policy.ALLOW,
    deny=[
            "nested.bar",
            "nestedObjects.baz"
    ]
)
class MyAction extends BaseActionBean {

    String foo
    List<MyActionNestedObject> nestedObjects
    MyActionNestedObject nested

}

class MyActionNestedObject {

    String bar
    String baz

}
