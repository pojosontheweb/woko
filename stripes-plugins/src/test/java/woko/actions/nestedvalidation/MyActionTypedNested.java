/*
 * Copyright 2001-2012 Remi Vankeisbelck
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

package woko.actions.nestedvalidation;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

public class MyActionTypedNested implements ActionBean {

  private ActionBeanContext context;

  public ActionBeanContext getContext() {
    return context;
  }

  public void setContext(ActionBeanContext context) {
    this.context = context;
  }

  @ValidateNestedProperties({})
  private MyPojoNested myPojoNested;

  public MyPojoNested getMyPojoNested() {
    return myPojoNested;
  }

  public void setMyPojoNested(MyPojoNested myPojoNested) {
    this.myPojoNested = myPojoNested;
  }

  @DefaultHandler
  public Resolution doIt() {
    return new ForwardResolution("/foo");
  }

}
