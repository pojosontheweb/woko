#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.model;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import javax.validation.constraints.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Searchable
public class MyEntity {

    @Id
    @SearchableId
    private Long id;

    @NotNull
    @SearchableProperty
    private String myProp;

     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMyProp() {
        return myProp;
    }

    public void setMyProp(String myProp) {
        this.myProp = myProp;
    }
}
