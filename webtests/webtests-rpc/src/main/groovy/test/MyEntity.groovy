package test

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class MyEntity {

  @Id
  Long id

  String prop1

  Integer prop2

}
