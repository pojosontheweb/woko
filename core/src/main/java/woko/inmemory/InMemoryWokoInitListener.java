/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.inmemory;

import woko.WokoInitListener;
import woko.persistence.ObjectStore;
import woko.users.UserManager;

import java.util.Arrays;

/**
 * In-mem init listener. Not for production  use.
 */
@Deprecated
public class InMemoryWokoInitListener extends WokoInitListener {

    @Override
    protected ObjectStore createObjectStore() {
        return new InMemoryObjectStore();
    }

    @Override
    protected UserManager createUserManager() {
        InMemoryUserManager um = new InMemoryUserManager();
        um.addUser("wdevel", "wdevel", Arrays.asList("developer"));
        return um;
    }

}
