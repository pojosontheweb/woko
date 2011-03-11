package woko.util;

import net.sourceforge.stripes.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class WLogger {

  public static WLogger getLogger(Class categoryClass) {
    return new WLogger(categoryClass);
  }

  private final Log stripesLog;

  public WLogger(Class categoryClass) {
    this.stripesLog = Log.getInstance(categoryClass);
  }

  public void debug(String msg) {
    stripesLog.debug(msg);
  }

  public void info(String msg) {
    stripesLog.info(msg);
  }

  public void warn(String msg) {
    stripesLog.warn(msg);
  }

  public void error(String msg) {
    stripesLog.error(msg);
  }

  public void warn(String msg,Exception e) {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    stripesLog.warn(msg, "\nStack:\n", sw.toString());
  }

  public void error(String msg,Exception e) {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    stripesLog.error(msg, "\nStack:\n", sw.toString());
  }

}
