package test

import woko.persistence.WokoAlternateKey

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class MyEntityAlternateKey {

    @Id
    @GeneratedValue
    Long id

    @WokoAlternateKey(
            altKeyProperty="alternateKey"
    )
    String foo

    String alternateKey

}
