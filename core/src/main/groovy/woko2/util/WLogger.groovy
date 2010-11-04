package woko2.util

import net.sourceforge.stripes.util.Log

class WLogger {

  static WLogger getLogger(Class categoryClass) {
    return new WLogger(categoryClass)
  }

  private final Log stripesLog

  public WLogger(Class categoryClass) {
    this.stripesLog = Log.getInstance(categoryClass)
  }

  void debug(msg) {
    stripesLog.debug msg
  }

  void info(msg) {
    stripesLog.info msg
  }

  void warn(msg) {
    stripesLog.warn msg
  }

  void error(msg) {
    stripesLog.error msg
  }

  void error(msg,Exception e) {
    StringWriter sw = new StringWriter()
    e.printStackTrace(new PrintWriter(sw))
    stripesLog.error(msg, "\nStack:\n", sw.toString())
  }

}
