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

import junit.framework.TestCase;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.validation.ValidationMetadata;

import java.util.Map;

public class NestedValidationMetadataProviderTest extends TestCase {

  private Map<String,ValidationMetadata> computeMetadata(ActionBean o) {
    NestedValidationMetadataProvider p = new NestedValidationMetadataProvider();
    p.setCurrentAction(o);
    return p.getValidationMetadata(o.getClass());
  }

  private void assertOneLevelResult(Map<String,ValidationMetadata> metadata) {
    assertOneLevelResult(metadata, "myPojo.prop");
  }

  private void assertOneLevelResult(Map<String,ValidationMetadata> metadata, String expectedKey) {
    assertNotNull(metadata);
    assertEquals(1, metadata.size());
    String key = metadata.keySet().iterator().next();
    assertEquals(expectedKey, key);
    ValidationMetadata vm = metadata.get(key);
    assertTrue(vm.required());
  }

  public void testTypedOneLevelNotNull() {
    MyActionTyped action = new MyActionTyped();
    action.setMyPojo(new MyPojo());
    assertOneLevelResult(
        computeMetadata(action)
    );
  }

  public void testTypedOneLevelNull() {
    MyActionTyped action = new MyActionTyped();
    assertOneLevelResult(
        computeMetadata(action)
    );
  }

  public void testUntypedOneLevelNotNull() {
    MyActionNotTypedNotNull action = new MyActionNotTypedNotNull();
    action.setMyPojo(new MyPojo());
    assertOneLevelResult(
        computeMetadata(action)
    );
  }

  public void testUntypedOneLevelNull() {
    MyActionNotTyped action = new MyActionNotTyped();
    Map<String,ValidationMetadata> metadata = computeMetadata(action);
    assertEquals(0, metadata.size());
  }

  public void testUntypedTwoLevelsNotNull() {
    MyActionNotTypedNestedNotNull a = new MyActionNotTypedNestedNotNull();
    assertOneLevelResult(
        computeMetadata(a),
        "myPojoNested.myPojo.prop"
    );
  }

  public void testUntypedTwoLevelsNull() {
    MyActionNotTypedNested action = new MyActionNotTypedNested();
    Map<String,ValidationMetadata> metadata = computeMetadata(action);
    assertEquals(0, metadata.size());
  }

  public void testTypedTwoLevelsNotNull() {
    MyActionTypedNested action = new MyActionTypedNested();
    action.setMyPojoNested(new MyPojoNested());
    assertOneLevelResult(
        computeMetadata(action),
        "myPojoNested.myPojo.prop"
    );
  }

  public void testTypedTwoLevelsNull() {
    MyActionTypedNested action = new MyActionTypedNested();
    assertOneLevelResult(
        computeMetadata(action),
        "myPojoNested.myPojo.prop"
    );
  }

}
