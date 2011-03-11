package woko;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public abstract class WokoInitListener implements ServletContextListener {

  private ServletContext servletContext;

  public ServletContext getServletContext() {
    return servletContext;
  }

  public void contextInitialized(ServletContextEvent e) {
    servletContext = e.getServletContext();
    servletContext.setAttribute(Woko.CTX_KEY, createWoko());
  }

  public void contextDestroyed(ServletContextEvent e) {
    Woko woko = Woko.getWoko(e.getServletContext());
    if (woko!=null) {
      woko.close();
    }
  }

  protected abstract Woko createWoko();


}
