package woko2.mongo

import woko2.persistence.ObjectStore

class MongoObjectStore implements ObjectStore {

  private final WokoMongo wokoMongo

  public MongoObjectStore(WokoMongo wokoMongo) {
    this.wokoMongo = wokoMongo;
  }

  def load(String className, String key) {
    return wokoMongo.db[className].findOne([_id:key])
  }

  def Object save(Object obj) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  def Object delete(Object obj) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  def String getKey(Object obj) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  def String getClassName(Object obj) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  def Class<?> getMappedClass(String className) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }


}
