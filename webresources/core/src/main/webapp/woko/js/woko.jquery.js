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

(function() {

    woko.util.mixin = function(o1, o2) {
        var newObj = {};
        jQuery.extend(newObj, o1, o2);
        return newObj;
    };

    var _jqueryArgs = function(oArgs, isPost) {
        var jqueryArgs = {
            type: isPost ? 'POST' : 'GET',
            data: oArgs.content || {},
            dataType: oArgs.handleAs === 'json' ? 'json' : 'html'
        };
        if (oArgs.error) {
            jqueryArgs.error = oArgs.error;
        }
        if (oArgs.load) {
            jqueryArgs.success = oArgs.load;
        }
        return jqueryArgs;
    };

    woko.util.xhrPost = function(oArgs) {
        return jQuery.ajax(oArgs.url, _jqueryArgs(oArgs, true));
    };

    woko.util.xhrGet = function(oArgs) {
        return jQuery.ajax(oArgs.url, _jqueryArgs(oArgs, false));
    };

})();
