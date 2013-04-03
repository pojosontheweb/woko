package test

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class MyEntityWithTinyMCE {

    @Id
    @GeneratedValue
    Long id

    String foo
}
