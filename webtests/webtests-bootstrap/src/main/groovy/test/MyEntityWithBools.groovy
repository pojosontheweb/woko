package test

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class MyEntityWithBools {

    @Id @GeneratedValue
    Long id

    String name

    Boolean boolObjectProp

    boolean boolRawProp = false

}
