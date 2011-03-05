package woko.actions.nestedvalidation;

public class MyActionNotTypedNestedNotNull extends MyActionNotTypedNested {

  public MyActionNotTypedNestedNotNull() {
    setMyPojoNested(new MyPojoNested());
  }

}
