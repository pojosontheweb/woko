package test

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class SubEntity {

    @Id @GeneratedValue
    Long id

    String name

    @ManyToOne
    test.EntityWithRelations daEntity

    transient OtherPojo nonPersistent = new OtherPojo()
}
