// package declaration
var woko = woko || {};
woko.util = {};

woko.util.isUndefinedOrNull = function(obj) {
  return obj===null || typeof obj === 'undefined';
};

woko.util.fade = function(elemId, timeToFade, startFadeAfter) {
    timeToFade = timeToFade || 1000.0;
    setTimeout(function() {
        var element = document.getElementById(elemId);
        if (element == null)
            return;

        if (element.FadeState == null) {
            if (element.style.opacity == null
                    || element.style.opacity == ''
                    || element.style.opacity == '1') {
                element.FadeState = 2;
            }
            else {
                element.FadeState = -2;
            }
        }

        if (element.FadeState == 1 || element.FadeState == -1) {
            element.FadeState = element.FadeState == 1 ? -1 : 1;
            element.FadeTimeLeft = timeToFade - element.FadeTimeLeft;
        }
        else {
            element.FadeState = element.FadeState == 2 ? -1 : 1;
            element.FadeTimeLeft = timeToFade;
            setTimeout("woko.util.animateFade(" + new Date().getTime() + ",'" + elemId + "', " + timeToFade + ")", 33);
        }
    }, startFadeAfter || 10);
};

woko.util.animateFade = function(lastTick, eid, timeToFade)
{
  var curTick = new Date().getTime();
  var elapsedTicks = curTick - lastTick;

  var element = document.getElementById(eid);

  if(element.FadeTimeLeft <= elapsedTicks)
  {
    element.style.opacity = element.FadeState == 1 ? '1' : '0';
    element.style.filter = 'alpha(opacity = '
        + (element.FadeState == 1 ? '100' : '0') + ')';
    element.FadeState = element.FadeState == 1 ? 2 : -2;
    return;
  }

  element.FadeTimeLeft -= elapsedTicks;
  var newOpVal = element.FadeTimeLeft/timeToFade;
  if(element.FadeState == 1)
    newOpVal = 1 - newOpVal;

  element.style.opacity = newOpVal;
  element.style.filter = 'alpha(opacity = ' + (newOpVal*100) + ')';

  setTimeout("woko.util.animateFade(" + curTick + ",'" + eid + "'," + timeToFade + ")", 33);
};

woko.util.mixin = function(o1,o2) {
  return dojo.mixin(o1,o2);
};

woko.util.xhrPost = function(oArgs) {
    return dojo.xhrPost(oArgs);
};

woko.util.xhrGet = function(oArgs) {
    return dojo.xhrGet(oArgs);
};
