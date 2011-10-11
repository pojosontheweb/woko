#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.gae;

import java.util.Arrays;

import woko.inmemory.InMemoryUserManager;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.auth.builtin.SessionUsernameResolutionStrategy;

import woko.gae.GaeWokoInitListener;


public class MyGaeInMemoryWokoInitListener extends GaeWokoInitListener {

    protected UserManager createUserManager() {
        InMemoryUserManager um = new InMemoryUserManager();
        um.addUser("${woko-developer-username}", "${woko-developer-password}", Arrays.asList("developer"));
        return um;
    }

    @Override
    protected UsernameResolutionStrategy createUsernameResolutionStrategy() {
        return new SessionUsernameResolutionStrategy();
    }
}
