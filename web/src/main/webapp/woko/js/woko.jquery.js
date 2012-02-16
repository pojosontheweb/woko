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
