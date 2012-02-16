var u = woko.util;
var pkg = woko.rpc = {};

/**
 * Constructor function for the RPC client.
 * @param baseUrl the base URL of the webapp (including context path)
 */
pkg.Client = function(baseUrl, config) {
    if (u.isUndefinedOrNull(baseUrl)) {
        throw "baseUrl not found in config object";
    }
    this.baseUrl = baseUrl;
    this.config = config || {};
};


/**
 * Invoke a facet (via XHR) with supplied args.
 * @param facetName the name of the facet
 * @param oArgs an object holding the optional arguments
 * @param oArgs.className {String} the className of the target object if any
 * @param oArgs.key {String} the key of the target object if any
 * @param oArgs.content {Object} an object containing request parameters to be sent (key/values)
 * @param oArgs.handleAs {String} the type of the response as a string ("text" or "json") - defaults to "json"
 * @param oArgs.onSuccess {Function} the callback to be called if the XHR call was successful (response is passed to the callback)
 * @param oArgs.onError {Function} the error callback (in case something went wrong during the XHR process)
 * @param oArgs.isPost {boolean} true if the request has to be POSTed, GET otherwise (defaults to GET)
 */
pkg.Client.prototype.invokeFacet = function(facetName, oArgs) {
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
    if (oArgs.onSuccess) {
        xhrArgs.load = oArgs.onSuccess;
    }
    xhrArgs.error =
        oArgs.onError ||
            function(err) {
                throw "error while trying to invoke facet " +
                    facetName + ", className " + oArgs.className +
                    ", key " + oArgs.key;
            };
    xhrArgs.handleAs = oArgs.handleAs || "json";

    if (oArgs.isPost) {
        dojo.xhrPost(xhrArgs);
    } else {
        dojo.xhrGet(xhrArgs);
    }
};

/**
 * Load a Woko-managed POJO by invoking the RPC APIs.
 * @param className {String} the class name of the target object
 * @param key {String} the key of the target object
 * @param oArgs {Object} an object holding the optional arguments
 * @param oArgs.content {Object} an object containing request parameters to be sent (key/values)
 * @param oArgs.handleAs {String} the type of the response as a string ("text" or "json") - defaults to "json"
 * @param oArgs.onSuccess {Function} the callback to be called if the XHR call was successful (response is passed to the callback)
 * @param oArgs.onError {Function} the error callback (in case something went wrong during the XHR process)
 * @param oArgs.isPost {boolean} true if the request has to be POSTed, GET otherwise (defaults to GET)
 */
pkg.Client.prototype.loadObject = function(className, key, oArgs) {
    if (u.isUndefinedOrNull(className)) {
        throw "className not found in arguments";
    }
    if (u.isUndefinedOrNull(key)) {
        throw "key not found in arguments";
    }
    this.invokeFacet("view", dojo.mixin(oArgs, {
        className: className,
        key: key
    }));
};

pkg.Client.prototype._setPaginationDetails = function(from, to) {
    if (from.resultsPerPage) {
        to.content = to.content || {};
        to.content["facet.resultsPerPage"] = from.resultsPerPage;
    }
    if (from.page) {
        to.content = to.content || {};
        to.content["facet.page"] = from.page;
    }
};


/**
 * Find Woko-managed POJOs by invoking the RPC APIs. Returns paginated results.
 * @param className {String} the class name of the objects to be listed
 * @param oArgs {Object} an object holding the optional arguments
 * @param oArgs.resultsPerPage {Number} the number of results to return per page
 * @param oArgs.page {Number} the number of the page (starting from 1)
 * @param oArgs.content {Object} an object containing request parameters to be sent (key/values)
 * @param oArgs.handleAs {String} the type of the response as a string ("text" or "json") - defaults to "json"
 * @param oArgs.onSuccess {Function} the callback to be called if the XHR call was successful (response is passed to the callback)
 * @param oArgs.onError {Function} the error callback (in case something went wrong during the XHR process)
 * @param oArgs.isPost {boolean} true if the request has to be POSTed, GET otherwise (defaults to GET)
 */
pkg.Client.prototype.findObjects = function(className, oArgs) {
    if (u.isUndefinedOrNull(className)) {
        throw "className not found in arguments";
    }
    oArgs = oArgs || {};
    oArgs.className = className;
    this._setPaginationDetails(oArgs, oArgs);
    this.invokeFacet("list", oArgs);
};

/**
 * Search for Woko-managed POJOs by invoking the RPC APIs. Returns paginated results.
 * @param query {String} the query string
 * @param oArgs {Object} an object holding the optional arguments
 * @param oArgs.resultsPerPage {Number} the number of results to return per page
 * @param oArgs.page {Number} the number of the page (starting from 1)
 * @param oArgs.content {Object} an object containing request parameters to be sent (key/values)
 * @param oArgs.handleAs {String} the type of the response as a string ("text" or "json") - defaults to "json"
 * @param oArgs.onSuccess {Function} the callback to be called if the XHR call was successful (response is passed to the callback)
 * @param oArgs.onError {Function} the error callback (in case something went wrong during the XHR process)
 * @param oArgs.isPost {boolean} true if the request has to be POSTed, GET otherwise (defaults to GET)
 */
pkg.Client.prototype.searchObjects = function(query, oArgs) {
    if (u.isUndefinedOrNull(query)) {
        throw "query not found in arguments";
    }
    oArgs = oArgs || {};
    this._setPaginationDetails(oArgs, oArgs);
    oArgs.content = oArgs.content || {};
    oArgs.content["facet.query"] = query;
    this.invokeFacet("search", oArgs);
};

/**
 * Updates or saves a Woko-managed POJO by invoking the RPC APIs.
 * This function can be called in two flavors :
 * * passing a JS object as oArgs.obj that has been previously returned by 'loadObject' (and thereby containing all metadata required)
 * * passing a className and key, and submitting parameters as the content
 * @param oArgs {Object} an object holding the arguments
 * @param oArgs.obj {Object} the object to be saved with all Woko metadata (returned by a previous API call)
 * @param oArgs.className {String} the class name of the object to be updated
 * @param oArgs.key {Object} the key of the object to be updated
 * @param oArgs.content {Object} an object containing request parameters to be sent (key/values)
 * @param oArgs.handleAs {String} the type of the response as a string ("text" or "json") - defaults to "json"
 * @param oArgs.onSuccess {Function} the callback to be called if the XHR call was successful (response is passed to the callback)
 * @param oArgs.onError {Function} the error callback (in case something went wrong during the XHR process)
 * @param oArgs.isPost {boolean} true if the request has to be POSTed, GET otherwise (defaults to POST)
 */
pkg.Client.prototype.saveObject = function(oArgs) {
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
        content: dojo.mixin(content, transformedParams)
    });
    if (oArgs.key) {
        args.key = oArgs.key;
    }
    this.invokeFacet("save", args);
};

/**
 * Deletes a Woko-managed POJO by invoking the RPC APIs.
 * This function can be called in two flavors :
 * * passing a JS object as oArgs.obj that has been previously returned by 'loadObject' (and thereby containing all metadata required)
 * * passing a className and key, and submitting parameters as the content
 * @param oArgs {Object} an object holding the arguments
 * @param oArgs.obj {Object} the object to be deleted with all Woko metadata (returned by a previous API call)
 * @param oArgs.className {String} the class name of the object to be updated
 * @param oArgs.key {Object} the key of the object to be updated
 * @param oArgs.content {Object} an object containing request parameters to be sent (key/values)
 * @param oArgs.handleAs {String} the type of the response as a string ("text" or "json") - defaults to "json"
 * @param oArgs.onSuccess {Function} the callback to be called if the XHR call was successful (response is passed to the callback)
 * @param oArgs.onError {Function} the error callback (in case something went wrong during the XHR process)
 * @param oArgs.isPost {boolean} true if the request has to be POSTed, GET otherwise (defaults to POST)
 */
pkg.Client.prototype.removeObject = function(oArgs) {
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
        content: dojo.mixin(content, {"facet.confirm": true})
    });
    this.invokeFacet("delete", args);
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
