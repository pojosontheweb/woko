package test;

import woko.ri.RiWokoInitListener
import woko.users.UserManager
import woko.persistence.ObjectStore;

class UserManagementWTInitIListener extends RiWokoInitListener {

    private def store

    @Override
    protected ObjectStore createObjectStore() {
        store = super.createObjectStore()
    }

    @Override
    protected UserManager createUserManager() {
        return new MyUserManager(store).createDefaultUsers()
    }


}