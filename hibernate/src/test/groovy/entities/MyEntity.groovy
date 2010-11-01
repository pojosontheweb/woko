package entities

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class MyEntity {

  @Id
  Long id

  String name

}
