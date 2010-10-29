package woko2

import javax.servlet.http.HttpServlet

abstract class WokoInitServlet extends HttpServlet {

  def void init() {
    super.init()
    def w = createWoko()   
    servletContext.setAttribute Woko.CTX_KEY, w
  }

  def void destroy() {
    super.destroy()
    def w = Woko.getWoko(servletContext)
    if (w) {
      w.close()
    }
  }

  abstract Woko createWoko()

}
