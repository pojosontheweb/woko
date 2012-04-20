package test

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class EntityWithRelations {

    @Id @GeneratedValue
    Long id

    String name

    @OneToMany(mappedBy="daEntity")
    Collection<SubEntity> subEntities
    
}
