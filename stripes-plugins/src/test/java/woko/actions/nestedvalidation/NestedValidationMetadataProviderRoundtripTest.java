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

package woko.actions.nestedvalidation;

import junit.framework.TestCase;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.mock.MockServletContext;
import net.sourceforge.stripes.validation.ValidationErrors;

import java.util.HashMap;
import java.util.Map;

public class NestedValidationMetadataProviderRoundtripTest extends TestCase {

  private MockServletContext createMockServletContext() {
    Map<String, String> params = new HashMap<String, String>();
    params.put("ActionResolver.Packages", "woko.actions");
    params.put("Extension.Packages", "woko.actions");
    MockServletContext mockServletContext = new MockServletContext("nestedvalidation");
    mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
    mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
    return mockServletContext;
  }

  private <T extends ActionBean> T trip(Class<T> beanClass, Map<String,String> params) throws Exception {
    MockRoundtrip rt = new MockRoundtrip(createMockServletContext(), beanClass);
    if (params!=null) {
      for (String paramName : params.keySet()) {
        String paramValue = params.get(paramName);
        rt.addParameter(paramName, paramValue);
      }
    }
    rt.execute();
    return rt.getActionBean(beanClass);

  }

  private void assertOneValidationError(ActionBean actionBean, String expectedKey) {
    ValidationErrors errors = actionBean.getContext().getValidationErrors();
    assertEquals(1, errors.size());
    assertEquals(expectedKey, errors.keySet().iterator().next());
  }

  public void testRoundtripTypedNull() throws Exception {
    MyActionTyped a = trip(MyActionTyped.class, null);
    // make sure property has not been bound
    assertNull(a.getMyPojo());
    // make sure we have a validation error
    assertOneValidationError(a, "myPojo.prop");
  }

  public void testRoundtripTypedNotNull() throws Exception {
    // bind a non validated prop in order to create the nested object
    Map<String,String> params = new HashMap<String,String>();
    params.put("myPojo.otherProp", "foobar");
    MyActionTyped a = trip(MyActionTyped.class, params);
    // make sure property has not been bound
    assertNull(a.getMyPojo().getProp());
    // make sure binding has worked on non validated prop
    assertEquals("foobar", a.getMyPojo().getOtherProp());
    // make sure we have a validation error
    assertOneValidationError(a, "myPojo.prop");
  }

  public void testRoundtripNotTypedNull() throws Exception {
    MyActionNotTyped a = trip(MyActionNotTyped.class, null);
    // make sure property has not been bound
    assertNull(a.getMyPojo());
    // make sure we have no validation errors (run time type unknow)
    assertEquals(0, a.getContext().getValidationErrors().size());
  }

  public void testRoundtripNotTypedNotNull() throws Exception {
    Map<String,String> params = new HashMap<String,String>();
    params.put("myPojo.otherProp", "foobar");
    MyActionNotTypedNotNull a = trip(MyActionNotTypedNotNull.class, params);
    // make sure property has not been bound
    MyPojo myPojo = (MyPojo)a.getMyPojo();
    assertNull(myPojo.getProp());
    // make sure binding has worked on non validated prop
    assertEquals("foobar", myPojo.getOtherProp());
    // make sure we have a validation error
    assertOneValidationError(a, "myPojo.prop");
  }

  public void testTwoLevelsRoundtripTypedNull() throws Exception {
    MyActionTypedNested a = trip(MyActionTypedNested.class, null);
    // make sure property has not been bound
    assertNull(a.getMyPojoNested());
    // make sure we have a validation error
    assertOneValidationError(a, "myPojoNested.myPojo.prop");
  }

  public void testTwoLevelsRoundtripTypedNotNull() throws Exception {
    // bind a non validated prop in order to create the nested object
    Map<String,String> params = new HashMap<String,String>();
    params.put("myPojoNested.otherProp", "foobar");
    MyActionTypedNested a = trip(MyActionTypedNested.class, params);
    // make sure property has not been bound
    assertNull(a.getMyPojoNested().getMyPojo());
    // make sure binding has worked on non validated prop
    assertEquals("foobar", a.getMyPojoNested().getOtherProp());
    // make sure we have a validation error
    assertOneValidationError(a, "myPojoNested.myPojo.prop");
  }

  public void testTwoLevelsRoundtripNotTypedNull() throws Exception {
    MyActionNotTypedNested a = trip(MyActionNotTypedNested.class, null);
    // make sure property has not been bound
    assertNull(a.getMyPojoNested());
    // make sure we have no validation errors (run time type unknow)
    assertEquals(0, a.getContext().getValidationErrors().size());
  }

  public void testTwoLevelsRoundtripNotTypedNotNull() throws Exception {
    Map<String,String> params = new HashMap<String,String>();
    params.put("myPojoNested.otherProp", "foobar");
    MyActionNotTypedNestedNotNull a = trip(MyActionNotTypedNestedNotNull.class, params);
    // make sure property has not been bound
    MyPojoNested myPojo = (MyPojoNested)a.getMyPojoNested();
    assertNull(myPojo.getMyPojo());
    // make sure binding has worked on non validated prop
    assertEquals("foobar", myPojo.getOtherProp());
    // make sure we have a validation error
    assertOneValidationError(a, "myPojoNested.myPojo.prop");
  }

}
