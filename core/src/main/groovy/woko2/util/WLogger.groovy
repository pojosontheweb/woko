package woko2.util

class WLogger {

  static WLogger getLogger(Class categoryClass) {
    return new WLogger(categoryClass.simpleName)
  }

  private final String category

  public WLogger(String category) {
    this.category = category
  }

  long getTime() {
    return System.currentTimeMillis()
  }

  void debug(msg) {
    println "$time - [DEBUG][$category] $msg"
  }

  void info(msg) {
    println "$time - [INFO] [$category] $msg"
  }

  void warn(msg) {
    println "$time - [WARN] [$category] $msg"
  }

  void error(msg) {
    println "$time - [ERROR][$category] $msg"
  }

  void error(msg,e) {
    println "$time - [ERROR][$category] $msg"
    e.printStackTrace()
  }

}
