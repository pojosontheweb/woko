package test

import javax.persistence.Entity
import javax.persistence.Id
import org.hibernate.validator.NotNull
import org.hibernate.search.annotations.Indexed
import org.hibernate.search.annotations.DocumentId
import org.hibernate.search.annotations.Field

@javax.persistence.Entity
@org.hibernate.search.annotations.Indexed
class MyBook {

  @org.hibernate.validator.NotNull
  @org.hibernate.search.annotations.Field
  String name

  @javax.persistence.Id
  @org.hibernate.search.annotations.DocumentId
  String _id

  @org.hibernate.search.annotations.Field
  int nbPages

  @org.hibernate.search.annotations.Field
  Date creationTime = new Date()

  /*
  transient List<MyBook> listOfMe = [this, this, this]
  transient MyBook me = this
  transient List<String> listOfStrings = ["abd", "ddd"]

  transient OtherPojo otherPojo = new OtherPojo()
  transient List<OtherPojo> otherPojos = [new OtherPojo()]

  transient OtherPojo2 otherPojo2 = new OtherPojo2()
  transient List<OtherPojo2> otherPojos2 = [new OtherPojo2()]
  */
  
}
