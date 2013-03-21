package entities

import woko.persistence.WokoAlternateKey

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class MyEntityWithAlternateKey {

    @Id
    Long id

    @WokoAlternateKey(
            altKeyProperty = "altKey"
    )
    String name

    String altKey

}
