package test

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Defect203Entity {

    @Id @GeneratedValue
    Long id

    String stringProp

    Integer intProp

}
