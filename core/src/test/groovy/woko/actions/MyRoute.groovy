package woko.actions

import net.sourceforge.stripes.action.ActionBean
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.DefaultHandler
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.UrlBinding

@UrlBinding("/route/to/book/{bookId}")
class MyRoute implements  ActionBean {

    ActionBeanContext context

    Long bookId

    @DefaultHandler
    Resolution getRoute() {
        return new ForwardResolution("/testWithRoute/woko.inmemory.Book/${bookId}")
    }

}
