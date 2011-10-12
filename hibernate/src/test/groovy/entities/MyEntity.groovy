package entities

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
class MyEntity {

    @Id
    Long id

    @NotNull
    String name

    String otherProp

}
