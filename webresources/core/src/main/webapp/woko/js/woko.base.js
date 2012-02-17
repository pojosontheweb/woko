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

