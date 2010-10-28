package woko2.mongo

import com.gmongo.GMongo
import woko2.Woko
import woko2.users.UserManager
import woko2.persistence.ObjectStore

class WokoMongo extends Woko {

  private GMongo mongo
  private def db

  private MongoUserManager userManager
  private MongoObjectStore objectStore

  def getDb() {
    return db
  }

  def void doInit() {
    mongo = new GMongo("127.0.0.1", 27017)
    db = mongo.getDB('wokoDb')
    userManager = new MongoUserManager(this)
    objectStore = new MongoObjectStore(this)
  }

  def UserManager getUserManager() {
    return userManager
  }

  ObjectStore getObjectStore() {
    return objectStore
  }

  def void doClose() {
    mongo.close()
  }


}
