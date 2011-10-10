#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.gae;

import java.util.Arrays;

import woko.inmemory.InMemoryUserManager;
import woko.users.UserManager;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.hibernate.validator.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;

import woko.gae.GaeWokoInitListener;

public class MyGaeInMemoryWokoInitListener extends GaeWokoInitListener {

    protected UserManager createUserManager() {
    InMemoryUserManager um = new InMemoryUserManager();
    um.addUser("wdevel", "wdevel", Arrays.asList("developer"));
    return um;
  }
}
