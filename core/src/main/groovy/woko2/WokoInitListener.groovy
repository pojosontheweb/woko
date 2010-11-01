package woko2

import javax.servlet.ServletContextListener
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContext

abstract class WokoInitListener implements ServletContextListener {

  private ServletContext servletContext

  ServletContext getServletContext() {
    return servletContext
  }

  void contextInitialized(ServletContextEvent e) {
    servletContext = e.servletContext
    def woko = createWoko()
    servletContext.setAttribute Woko.CTX_KEY, woko
  }

  void contextDestroyed(ServletContextEvent e) {
    def woko = Woko.getWoko(e.servletContext)
    if (woko) {
      woko.close()
    }
  }

  abstract Woko createWoko()


}
