var u = woko.util;
var pkg = woko.rpc = {};

pkg.Client = function(config) {
    config = config || {};
    config.baseUrl = config.baseUrl || "";
    if (u.isUndefinedOrNull(config.baseUrl)) {
        throw "baseUrl not found in config object";
    }
    this.baseUrl = config.baseUrl;
};

pkg.Client.prototype.invokeFacet = function(oArgs) {
    var facetName = oArgs.facetName;
    if (u.isUndefinedOrNull(facetName)) {
        throw "facetName not found in arguments";
    }
    var url = this.baseUrl + "/" + facetName;
    if (oArgs.className) {
        url += "/" + oArgs.className;
        if (oArgs.key) {
            url += "/" + oArgs.key;
        }
    }
    var xhrArgs = {
        url: url,
        content: dojo.mixin(oArgs.content || {}, {isRpc: true})
    };
    if (oArgs.load) {
        xhrArgs.load = oArgs.load;
    }
    xhrArgs.error =
        oArgs.error ||
            function(err) {
                throw "error while trying to invoke facet " +
                    facetName + ", className " + oArgs.className +
                    ", key " + oArgs.key;
            };
    xhrArgs.handleAs = oArgs.handleAs || "text";

    if (oArgs.isPost) {
        dojo.xhrPost(xhrArgs);
    } else {
        dojo.xhrGet(xhrArgs);
    }
};

pkg.Client.prototype.load = function(oArgs) {
    var className = oArgs.className;
    if (u.isUndefinedOrNull(className)) {
        throw "className not found in arguments";
    }
    var key = oArgs.key;
    if (u.isUndefinedOrNull(key)) {
        throw "key not found in arguments";
    }
    this.invokeFacet(dojo.mixin(oArgs, {
        handleAs: "json",
        facetName: "view"
    }));
};

pkg.Client.prototype._setPaginationDetails = function(from, to) {
    if (from.resultsPerPage) {
        to.content = to.content || {};
        to.content.resultsPerPage = from.resultsPerPage;
    }
    if (from.page) {
        to.content = to.content || {};
        to.content.page = from.page;
    }
};


pkg.Client.prototype.find = function(oArgs) {
    if (u.isUndefinedOrNull(oArgs.className)) {
        throw "className not found in arguments";
    }
    var args = dojo.mixin(oArgs, {
        handleAs: "json",
        facetName: "list"
    });
    this._setPaginationDetails(oArgs, args);
    this.invokeFacet(args);
};

pkg.Client.prototype.search = function(oArgs) {
    if (u.isUndefinedOrNull(oArgs.query)) {
        throw "query not found in arguments";
    }
    var args = dojo.mixin(oArgs, {
        handleAs: "json",
        facetName: "search"
    });
    this._setPaginationDetails(oArgs, args);
    args.content["facet.query"] = oArgs.query;
    this.invokeFacet(args);
};

pkg.Client.prototype.save = function(oArgs) {
    if (u.isUndefinedOrNull(oArgs.obj)) {
        throw "obj not found in arguments";
    }
    var obj = oArgs.obj;
    var className = oArgs.className || obj._className;
    if (u.isUndefinedOrNull(className)) {
        throw "className not found in arguments (not in oArgs, and not in passed obj)";
    }
    // create an object to hold the arguments using the "object." prefix
    var content = oArgs.content || {};
    var transformedParams = {};
    for (var p in obj) {
        if (obj.hasOwnProperty(p)) {
            transformedParams["object." + p] = obj[p];
        }
    }
    var args = dojo.mixin(oArgs, {
        className: className,
        handleAs: "json",
        facetName: "save",
        content: dojo.mixin(content, transformedParams)
    });
    if (oArgs.key) {
        args.key = oArgs.key;
    }
    this.invokeFacet(args);
};

pkg.Client.prototype.remove = function(oArgs) {
    var obj = oArgs.obj,
        className = oArgs.className,
        key = oArgs.key;
    if (obj) {
        className = obj._className || className;
        key = obj._id || key;
    }
    if (u.isUndefinedOrNull(className)) {
        throw "className not found in arguments. It must be provided either as an argument or as the _className field of " +
            " the instance to be deleted";
    }
    if (u.isUndefinedOrNull(key)) {
        throw "key not found in arguments. It must be provided either as an argument or as the _id field of " +
            " the instance to be deleted";
    }
    var content = oArgs.content || {};
    var args = dojo.mixin(oArgs, {
        className: className,
        key: key,
        handleAs: "json",
        facetName: "delete",
        content: dojo.mixin(content, {"facet.confirm": true})
    });
    this.invokeFacet(args);
};

/**
 * Used to convert stringified dates, like properties of JSON-ified objects, to real
 * javascript Date objects.
 * parameter should be a string like /Date(1328738360294)/ (with the number being the
 * good old ms/1970 thing).
 * return null if dateStr is null, not a string, empty string, or an unsupported string format.
 */
pkg.convertDate = function(dateStr) {
    if (!dateStr) {
        return null;
    }
    if (typeof dateStr != 'string') {
        return null;
    }
    if (!dateStr.indexOf("/Date(")==0) {
        return null;
    }
    if (dateStr.indexOf(")/")!=dateStr.length-2) {
        return null;
    }
    return new Date(parseInt(dateStr.substring(6)));
};





