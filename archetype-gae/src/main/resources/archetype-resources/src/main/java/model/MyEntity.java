#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MyEntity {

    @Id
    private Long id;


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
