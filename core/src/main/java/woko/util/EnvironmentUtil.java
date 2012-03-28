/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EnvironmentUtil {

    public static final String ENVI_FILE = "woko.environment";

    public static String getEnvironment() {
        InputStream is = EnvironmentUtil.class.getResourceAsStream("/" + ENVI_FILE);
        if (is==null) {
            return null;
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        try {
            try {
                return r.readLine();
            } finally {
                r.close();
            }
        } catch(Exception e) {
            throw new RuntimeException("unable to read envi file !", e);
        }
    }

}
