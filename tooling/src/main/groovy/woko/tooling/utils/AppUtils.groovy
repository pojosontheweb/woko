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

class AppUtils {

    static String simpleAsk(String question){
        print "> $question :"
        DataInputStream stream = new DataInputStream(System.in);
        return stream.readLine()
    }
    
    static requiredAsk(String question){
        String response
        while(!response){
            print "> $question : "
            DataInputStream stream = new DataInputStream(System.in);
            response = stream.readLine()
        }
        return response
    }

    static String askWithDefault(String question, String defaultValue){
        print "> $question [$defaultValue] :"
        DataInputStream stream = new DataInputStream(System.in);
        String response = stream.readLine()
        return response ? response : defaultValue
    }

    static Boolean yesNoAsk(String question){
        print "> $question ? [y] :"
        DataInputStream stream = new DataInputStream(System.in);
        def response = stream.readLine()
        return response?.toLowerCase() != 'n'
    }

    static def extractPkgAndClazz(String fqcn) {
        int i = fqcn.lastIndexOf(".")
        if (i!=-1) {
            return [pkg:fqcn[0..i-1], clazz:fqcn[i+1..-1]]
        }
        return [pkg:'',clazz:fqcn]
    }

}
