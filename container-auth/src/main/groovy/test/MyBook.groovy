package test

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class MyBook {

  String name

  @Id
  String _id
  
  int nbPages
  Date creationTime = new Date()
  transient List<MyBook> listOfMe = [this, this, this]
  transient MyBook me = this
  transient List<String> listOfStrings = ["abd", "ddd"]
  
}
