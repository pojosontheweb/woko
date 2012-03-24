/*
 * Copyright 2001-2010 Remi Vankeisbelck
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

package woko.tooling.cli

import woko.tooling.utils.Logger

import woko.tooling.cli.commands.GenerateCmd


class Generate {

    void run(Logger logger){
        new GenerateCmd(logger).execute()
    }

    public static void main(String[] args) {
        Writer out = new OutputStreamWriter(System.out)
        try {
            new Generate().run(new Logger(out))
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            out.flush()
            out.close()
        }
    }
}
