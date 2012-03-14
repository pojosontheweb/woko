/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
