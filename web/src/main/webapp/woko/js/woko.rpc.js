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
    url += "/" + oArgs.className
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







