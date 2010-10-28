package woko2

import javax.servlet.ServletContextListener
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContext

abstract class WokoInitListener implements ServletContextListener {

  void contextInitialized(ServletContextEvent e) {
    def woko = createWoko()
    woko.init()
    e.servletContext.setAttribute Woko.CTX_KEY, woko
  }

  void contextDestroyed(ServletContextEvent e) {
    def woko = Woko.getWoko(e.servletContext)
    if (woko) {
      woko.close()
    }
  }

  abstract Woko createWoko()


}
