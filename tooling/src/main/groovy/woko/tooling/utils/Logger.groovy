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

package woko.tooling.utils


class Logger {

    Writer writer

    Logger(Writer writer) {
        this.writer = writer
    }

    void log(String msg){
        writer ? writer<<"$msg\n" : println(msg)
        writer.flush()
    }

    void error(String error){
        log("ERROR : $error")
    }

    void indentedLog(msg) {
        log("|  $msg")
    }


    void splashMsg(){
        def msg = """__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0
             POJOs on the Web !\n"""
        log(msg)
    }
}
