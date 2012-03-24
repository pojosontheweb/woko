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

// package declaration
var woko = woko || {};
woko.util = {};

(function() {

    var u = woko.util;

    u.isUndefinedOrNull = function(obj) {
      return obj===null || typeof obj === 'undefined';
    };

    u._throwNotImplemented = function() {
        throw "function is not implemented by adapter !"
    };

    u.mixin = function(o1,o2) {
        u._throwNotImplemented();
    };

    u.xhrPost = function(oArgs) {
        u._throwNotImplemented();
    };

    u.xhrGet = function(oArgs) {
        u._throwNotImplemented();
    };

})();

