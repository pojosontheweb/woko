package test

import javax.persistence.Entity
import javax.persistence.Id
import org.hibernate.validator.NotNull
import org.compass.annotations.SearchableProperty
import org.compass.annotations.SearchableId
import org.compass.annotations.Searchable

@Entity
@Searchable
class MyBook {

  @NotNull
  @SearchableProperty
  String name

  @Id
  @SearchableId
  String _id

  @SearchableProperty
  int nbPages

  @SearchableProperty
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