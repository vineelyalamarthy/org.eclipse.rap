/**
* This code is property of Apple Inc.
* This code is intended for informational use only. 
* Apple has granted no license for distribution or use.
**/

const iPad = true;
const ADSupportsTouches = ("createTouch" in document);
const ADStartEvent = ADSupportsTouches ? "touchstart": "mousedown";
const ADMoveEvent = ADSupportsTouches ? "touchmove": "mousemove";
const ADEndEvent = ADSupportsTouches ? "touchend": "mouseup";
function ADUtils() {}
ADUtils.assetsPath = "";
ADUtils.t = function(b, a) {
    return ADUtils.t3d(b, a, 0)
};
ADUtils.t3d = function(c, a, b) {
    return "translate3d(" + c + "px, " + a + "px, " + b + "px)"
};
ADUtils.r3d = function(a, b, c, d) {
    return "rotate3d(" + a + ", " + b + ", " + c + ", " + d + "rad)"
};
ADUtils.px = function(a) {
    return a + "px"
};
ADUtils.degreesToRadians = function(a) {
    return (a / 360) * (Math.PI * 2)
};
ADUtils.radiansToDegrees = function(a) {
    return (a / (Math.PI * 2)) * 360
};
ADUtils.copyPropertiesFromSourceToTarget = function(c, b) {
    for (var a in c) {
        b[a] = c[a]
    }
};
ADUtils.objectIsFunction = function(a) {
    return (typeof a == "function")
};
ADUtils.objectIsUndefined = function(a) {
    return (a === undefined)
};
ADUtils.objectIsString = function(a) {
    return (typeof a == "string" || a instanceof String)
};
ADUtils.objectIsArray = function(a) {
    return (a instanceof Array)
};
ADUtils.objectHasMethod = function(b, a) {
    return (b !== null && !this.objectIsUndefined(b[a]) && this.objectIsFunction(b[a]))
};
ADUtils.disableScrolling = function(a) {
    a.stopPropagation();
    window.addEventListener("touchmove", ADUtils.preventEventDefault, true);
    window.addEventListener("touchend", ADUtils.restoreScrollingBehavior, true);
    window.addEventListener("touchcancel", ADUtils.restoreScrollingBehavior, true)
};
ADUtils.preventEventDefault = function(a) {
    a.preventDefault()
};
ADUtils.restoreScrolling = function() {
    window.removeEventListener("touchmove", ADUtils.preventEventDefault, true);
    window.removeEventListener("touchend", ADUtils.restoreScrollingBehavior, true);
    window.removeEventListener("touchcancel", ADUtils.restoreScrollingBehavior, true)
};
ADUtils.createUIEvent = function(a, b) {
    return ADSupportsTouches ? this.createEventWithTouch(a, b) : this.createEventWithMouse(a, b)
};
ADUtils.createEventWithTouch = function(c, a) {
    var b = document.createEvent("TouchEvent");
    b.initTouchEvent(c, a.bubbles, a.cancelable, window, a.detail, a.screenX, a.screenY, a.clientX, a.clientY, a.ctrlKey, a.altKey, a.shiftKey, a.metaKey, a.touches, a.targetTouches, a.changedTouches, a.scale, a.rotation);
    return b
};
ADUtils.createEventWithMouse = function(a, b) {
    var c = document.createEvent("MouseEvent");
    c.initMouseEvent(a, b.bubbles, b.cancelable, document.defaultView, b.detail, b.screenX, b.screenY, b.clientX, b.clientY, b.ctrlKey, b.altKey, b.shiftKey, b.metaKey, b.metaKey, b.button, b.relatedTarget);
    return c
};
ADUtils.init = function() {
    if (iPad) {
        document.body.addClassName("iPad")
    }
    for (var b = 0; b < document.styleSheets.length; b++) {
        var c = document.styleSheets[b];
        var a = c.href ? c.href.indexOf("dist/AdLib") : -1;
        if (a != -1) {
            ADUtils.assetsPath = c.href.substring(0, a) + "assets/";
            break
        }
    }
};
ADUtils.preloadImageAsset = function(a) {
    new Image().src = ADUtils.assetsPath + a
};
ADUtils.setupDisplayNames = function(a, d) {
    var c = d || a.name;
    for (var e in a) {
        if (a.__lookupGetter__(e)) {
            continue
        }
        var b = a[e];
        if (ADUtils.objectIsFunction(b)) {
            b.displayName = ADUtils.createDisplayName(c, e)
        }
    }
    for (var e in a.prototype) {
        if (a.prototype.__lookupGetter__(e)) {
            continue
        }
        var b = a.prototype[e];
        if (ADUtils.objectIsFunction(b)) {
            b.displayName = ADUtils.createDisplayName(c, e)
        }
    }
};
ADUtils.createDisplayName = function(b, a) {
    return b + "." + a + "()"
};
ADUtils.createNodeFromString = function(b) {
    var a = document.implementation.createHTMLDocument();
    a.write(b);
    return document.importNode(a.body.firstElementChild, true)
};
window.addEventListener("DOMContentLoaded", ADUtils.init, false);
ADUtils.setupDisplayNames(ADUtils, "ADUtils");
var ADEventTriage = {};
ADEventTriage.handleEvent = function(b) {
    if (this instanceof ADObject) {
        this.callSuper(b)
    }
    var c = b.type;
    var a = "handle" + c.charAt(0).toUpperCase() + c.substr(1);
    if (ADUtils.objectHasMethod(this, a)) {
        this[a](b)
    }
};
ADUtils.setupDisplayNames(ADEventTriage, "ADEventTriage");
var ADEventTarget = {};
ADEventTarget.addEventListener = function(c, b, a) {
    this.eventTarget.addEventListener(c, b, a)
};
ADEventTarget.removeEventListener = function(c, b, a) {
    this.eventTarget.removeEventListener(c, b, a)
};
ADEventTarget.dispatchEvent = function(a) {
    this.eventTarget.dispatchEvent(a)
};
ADUtils.setupDisplayNames(ADEventTarget, "ADEventTarget");
var ADPropertyTriage = {};
ADPropertyTriage.handlePropertyChange = function(b, c) {
    var a = "handle" + c.charAt(0).toUpperCase() + c.substr(1) + "Change";
    if (ADUtils.objectHasMethod(this, a)) {
        this[a](b)
    }
};
ADUtils.setupDisplayNames(ADPropertyTriage, "ADPropertyTriage");
Element.prototype.hasClassName = function(a) {
    return new RegExp("(?:^|\\s+)" + a + "(?:\\s+|$)").test(this.className)
};
Element.prototype.addClassName = function(a) {
    if (!this.hasClassName(a)) {
        this.className = [this.className, a].join(" ")
    }
};
Element.prototype.removeClassName = function(b) {
    if (this.hasClassName(b)) {
        var a = this.className;
        this.className = a.replace(new RegExp("(?:^|\\s+)" + b + "(?:\\s+|$)", "g"), " ")
    }
};
Element.prototype.toggleClassName = function(a) {
    this[this.hasClassName(a) ? "removeClassName": "addClassName"](a)
};
ADUtils.setupDisplayNames(Element, "Element");
Node.prototype.getNearestView = function() {
    var a = this;
    while (ADUtils.objectIsUndefined(a._view) && a.parentNode) {
        a = a.parentNode
    }
    return (ADUtils.objectIsUndefined(a._view)) ? null: a._view
};
ADUtils.setupDisplayNames(Node, "Node");
window.classes = [];
function ADClass(b) {
	window.classes.push(b);
    if (ADUtils.objectIsUndefined(b.inherits) && b !== ADObject) {
        b.inherits = ADObject
    }
    if (b.includes) {
        ADClass.mixin(b.prototype, b.includes)
    }
    var e = (b.synthetizes) ? b.synthetizes: [];
    for (var a = 0; a < e.length; a++) {
        ADClass.synthetizeProperty(b.prototype, e[a], true)
    }
    var d = b;
    var e = [];
    while (d.inherits) {
        d = d.inherits;
        if (d.synthetizes) {
            e = d.synthetizes.concat(e)
        }
    }
    for (var a = 0; a < e.length; a++) {
        ADClass.synthetizeProperty(b.prototype, e[a], false)
    }
    for (var a in b.prototype) {
        if (b.prototype.__lookupGetter__(a)) {
            continue
        }
        var c = b.prototype[a];
        if (ADUtils.objectIsFunction(c)) {
            c._class = b;
            c._name = a;
            c.displayName = ADUtils.createDisplayName(b.name, a)
        }
    }
    if (b !== ADObject) {
        b.prototype.__proto__ = b.inherits.prototype
    }
}
ADClass.synthetizeProperty = function(j, f, e) {
    var h = f.charAt(0).toUpperCase() + f.substr(1);
    var g = "get" + h;
    var i = "set" + h;
    var b = ADUtils.objectHasMethod(j, g);
    var d = ADUtils.objectHasMethod(j, i);
    if (!e && !(b || d)) {
        return
    }
    if (d) {
        var k = function(l) {
            j[i].call(this, l);
            this.notifyPropertyChange(f)
        };
        k.displayName = "Specified setter for ." + f + " on " + j.constructor.name;
        j.__defineSetter__(f, k)
    } else {
        var a = function(l) {
            this["_" + f] = l;
            this.notifyPropertyChange(f)
        };
        a.displayName = "Default setter for ." + f + " on " + j.constructor.name;
        j.__defineSetter__(f, a)
    }
    if (b) {
        j.__defineGetter__(f, j[g])
    } else {
        var c = function() {
            return this["_" + f]
        };
        c.displayName = "Default getter for ." + f + " on " + j.constructor.name;
        j.__defineGetter__(f, c)
    }
};
ADClass.mixin = function(b, a) {
    for (var c = 0; c < a.length; c++) {
        ADUtils.copyPropertiesFromSourceToTarget(a[c], b)
    }
};
ADUtils.setupDisplayNames(ADClass, "ADClass");
const ADObjectPropertyChanged = "handlePropertyChange";
function ADObject() {
    this.observedProperties = {}
}
ADObject.prototype.callSuper = function() {
    var a = ADObject.prototype.callSuper.caller;
    if (ADUtils.objectHasMethod(a, "inherits")) {
        a.inherits.apply(this, arguments)
    } else {
        var b = a._class.inherits.prototype;
        var c = a._name;
        if (ADUtils.objectHasMethod(b, c)) {
            return b[c].apply(this, arguments)
        }
    }
};
ADObject.prototype.isPropertyObserved = function(a) {
    return ! ADUtils.objectIsUndefined(this.observedProperties[a])
};
ADObject.prototype.addPropertyObserver = function(b, c, a) {
    if (!this.isPropertyObserved(b)) {
        this.observedProperties[b] = new Array()
    } else {
        if (this.observedProperties[b].indexOf(c) > -1) {
            return
        }
    }
    var a = a || ADObjectPropertyChanged;
    if (ADUtils.objectHasMethod(c, a)) {
        this.observedProperties[b].push({
            observer: c,
            methodName: a
        })
    }
};
ADObject.prototype.removePropertyObserver = function(d, a) {
    if (!this.isPropertyObserved(d)) {
        return false
    }
    var b = this.observedProperties[d];
    var c = b.indexOf(a);
    if (c > -1) {
        b.splice(c, 1)
    }
    return (c > -1)
};
ADObject.prototype.notifyPropertyChange = function(d) {
    if (!this.isPropertyObserved(d)) {
        return
    }
    var b = this.observedProperties[d];
    for (var c = 0; c < b.length; c++) {
        var a = b[c];
        a.observer[a.methodName](this, d)
    }
};
ADObject.prototype.callMethodNameAfterDelay = function(a, c) {
    var b = this;
    var d = Array.prototype.slice.call(arguments, 2);
    var e = function() {
        b[a].apply(b, d)
    };
    e.displayName = ADUtils.createDisplayName(this.constructor.name, a);
    return setTimeout(e, c)
};
ADClass(ADObject, "ADObject");
function ADPoint(a, b) {
    this.x = (a != null && !isNaN(a)) ? a: 0;
    this.y = (b != null && !isNaN(b)) ? b: 0
}
ADPoint.fromEvent = function(a) {
    var a = (a.touches && a.touches.length > 0) ? a.touches[0] : a;
    return new ADPoint(a.pageX, a.pageY)
};
ADPoint.fromEventInElement = function(b, a) {
    var b = (b.touches && b.touches.length > 0) ? b.touches[0] : b;
    return window.webkitConvertPointFromPageToNode(a, new WebKitPoint(b.pageX, b.pageY))
};
ADPoint.prototype.toString = function() {
    return "ADPoint[" + this.x + "," + this.y + "]"
};
ADPoint.prototype.copy = function() {
    return new ADPoint(this.x, this.y)
};
ADPoint.prototype.equals = function(a) {
    return (this.x == a.x && this.y == a.y)
};
ADUtils.setupDisplayNames(ADPoint, "ADPoint");
function ADSize(b, a) {
    this.width = (b != null && !isNaN(b)) ? b: 0;
    this.height = (a != null && !isNaN(a)) ? a: 0
}
ADSize.prototype.toString = function() {
    return "ADSize[" + this.width + "," + this.height + "]"
};
ADSize.prototype.copy = function() {
    return new ADSize(this.width, this.height)
};
ADSize.prototype.equals = function(a) {
    return (this.width == a.width && this.height == a.height)
};
ADUtils.setupDisplayNames(ADSize);
function ADEdgeInsets(b, d, a, c) {
    this.top = b;
    this.right = d;
    this.bottom = a;
    this.left = c
}
ADImage.inherits = ADObject;
ADImage.synthetizes = ["width", "height"];
function ADImage(a) {
    this.callSuper();
    this.url = a;
    this.loaded = false;
    this.element = new Image();
    this.element.src = a;
    this.element.addEventListener("load", this, false);
    this._width = 0;
    this._height = 0
}
ADImage.prototype.getWidth = function() {
    return this.element.width
};
ADImage.prototype.getHeight = function() {
    return this.element.height
};
ADImage.prototype.handleEvent = function(a) {
    this.loaded = true;
    this.notifyPropertyChange("loaded")
};
ADClass(ADImage);
const ADTransitionDidCompleteDelegate = "transitionDidComplete";
const ADTransitionDefaults = {
    duration: 0.5,
    delay: 0,
    removesTargetUponCompletion: false,
    revertsToOriginalValues: false
};
const ADTransitionStyles = ["-webkit-transition-property", "-webkit-transition-duration", "-webkit-transition-timing-function", "-webkit-transition-delay", "-webkit-transition"];
function ADTransition(a) {
    this.target = null;
    this.properties = null;
    this.duration = null;
    this.delay = null;
    this.from = null;
    this.to = null;
    this.timingFunction = null;
    this.delegate = null;
    this.removesTargetUponCompletion = null;
    this.revertsToOriginalValues = null;
    this.defaultsApplied = false;
    this.archivedStyles = null;
    this.archivedValues = [];
    ADUtils.copyPropertiesFromSourceToTarget(a, this)
}
ADTransition.prototype.applyDefaults = function() {
    if (this.defaultsApplied) {
        return
    }
    for (var a in ADTransitionDefaults) {
        if (this[a] === null) {
            this[a] = ADTransitionDefaults[a]
        }
    }
    this.defaultsApplied = true
};
ADTransition.prototype.archiveTransitionStyles = function() {
    if (this.archivedStyles !== null) {
        return
    }
    var b = (this.target instanceof ADView) ? this.target.layer: this.target;
    this.archivedStyles = [];
    for (var a = 0; a < ADTransitionStyles.length; a++) {
        this.archivedStyles.push(b.style.getPropertyValue(ADTransitionStyles[a]))
    }
};
ADTransition.prototype.restoreTransitionStyles = function() {
    for (var a = 0; a < ADTransitionStyles.length; a++) {
        this.element.style.setProperty(ADTransitionStyles[a], this.archivedStyles[a], "")
    }
    this.archivedStyles = null
};
ADTransition.prototype.archiveBaseValues = function() {
    if (!this.revertsToOriginalValues) {
        return
    }
    if (this.target instanceof ADView) {
        for (var a = 0; a < this.properties.length; a++) {
            this.archivedValues.push(this.target[this.properties[a]])
        }
    } else {
        for (var a = 0; a < this.properties.length; a++) {
            this.archivedValues.push(this.target.layer.style.getPropertyValue(this.properties[a]))
        }
    }
};
ADTransition.prototype.restoreBaseValues = function() {
    if (this.target instanceof ADView) {
        for (var a = 0; a < this.properties.length; a++) {
            this.target[this.properties[a]] = this.archivedValues[a]
        }
    } else {
        for (var a = 0; a < this.properties.length; a++) {
            this.target.layer.style.setProperty(this.properties[a], this.archivedValues[a], null)
        }
    }
};
ADTransition.prototype.start = function() {
    if (ADTransaction.openTransactions > 0) {
        ADTransaction.addTransition(this);
        return
    }
    this.applyDefaults();
    if (this.from === null) {
        this.applyToState()
    } else {
        this.applyFromState();
        var a = this;
        setTimeout(function() {
            a.applyToState()
        },
        0)
    }
};
ADTransition.prototype.applyFromState = function() {
    if (this.from === null) {
        return
    }
    this.applyDefaults();
    this.archiveTransitionStyles();
    if (this.target instanceof ADView) {
        this.target.layer.style.webkitTransitionDuration = 0;
        for (var a = 0; a < this.properties.length; a++) {
            this.target[this.properties[a]] = this.processTransitionValue(this.from[a])
        }
    } else {
        this.target.style.webkitTransitionDuration = 0;
        for (var a = 0; a < this.properties.length; a++) {
            this.target.style.setProperty(this.properties[a], this.from[a], "")
        }
    }
};
ADTransition.prototype.applyToState = function() {
    this.applyDefaults();
    this.archiveTransitionStyles();
    this.archiveBaseValues();
    var e = (this.target instanceof ADView);
    this.cssProperties = [];
    var b = [];
    for (var g = 0; g < this.properties.length; g++) {
        var d = (e) ? this.target.cssPropertyNameForJSProperty(this.properties[g]) : this.properties[g];
        if (this.cssProperties.indexOf(d) > -1) {
            continue
        }
        var c = (ADUtils.objectIsArray(this.duration)) ? this.duration[g] : this.duration;
        var f = (ADUtils.objectIsArray(this.timingFunction)) ? this.timingFunction[g] : this.timingFunction;
        var a = (ADUtils.objectIsArray(this.delay)) ? this.delay[g] : this.delay;
        b.push([d, c + "s", f, a + "s"].join(" "));
        this.cssProperties.push(d)
    }
    if (e) {
        this.element = this.target.layer;
        for (var g = 0; g < this.properties.length; g++) {
            this.target[this.properties[g]] = this.processTransitionValue(this.to[g])
        }
    } else {
        this.element = this.target;
        for (var g = 0; g < this.properties.length; g++) {
            this.target.style.setProperty(this.properties[g], this.to[g], "")
        }
    }
    this.element.style.webkitTransition = b.join(", ");
    this.element.addEventListener("webkitTransitionEnd", this, false);
    this.completedTransitions = 0
};
ADTransition.prototype.handleEvent = function(a) {
    if (a.currentTarget !== this.element) {
        return
    }
    this.completedTransitions++;
    if (this.completedTransitions != this.cssProperties.length) {
        return
    }
    if (ADUtils.objectHasMethod(this.delegate, ADTransitionDidCompleteDelegate)) {
        this.delegate[ADTransitionDidCompleteDelegate](this)
    }
    if (this.removesTargetUponCompletion) {
        var b = this.target;
        if (this.target instanceof ADView) {
            b.removeFromSuperview()
        } else {
            b.parentNode.removeChild(b)
        }
    } else {
        this.restoreTransitionStyles()
    }
    if (this.revertsToOriginalValues) {
        this.restoreBaseValues()
    }
};
const ADTransitionWidthRegExp = new RegExp(/\$width/g);
const ADTransitionHeightRegExp = new RegExp(/\$height/g);
ADTransition.prototype.processTransitionValue = function(a) {
    if (!ADUtils.objectIsString(a)) {
        return a
    }
    a = a.replace(ADTransitionWidthRegExp, ADUtils.px(this.target.size.width));
    return a.replace(ADTransitionHeightRegExp, ADUtils.px(this.target.size.height))
};
ADClass(ADTransition);
var ADTransaction = {
    transitions: [],
    openTransactions: 0,
    defaults: {},
    defaultsStates: []
};
ADTransaction.begin = function() {
    if (this.openTransactions == 0) {
        this.transitions = [];
        this.defaults = {}
    } else {
        this.defaultsStates.push(this.defaults)
    }
    this.openTransactions++
};
ADTransaction.addTransition = function(b) {
    for (var a in this.defaults) {
        if (b[a] === null) {
            b[a] = this.defaults[a]
        }
    }
    this.transitions.push(b)
};
ADTransaction.commit = function() {
    if (this.openTransactions == 0) {
        return
    }
    this.openTransactions--;
    if (this.openTransactions != 0) {
        this.defaults = this.defaultsStates.pop();
        return
    }
    var b = this.transitions;
    for (var a = 0; a < b.length; a++) {
        b[a].applyFromState()
    }
    setTimeout(function() {
        for (var c = 0; c < b.length; c++) {
            b[c].applyToState()
        }
    },
    0)
};
ADUtils.setupDisplayNames(ADTransaction, "ADTransaction");
const ADViewTransitionDissolveOut = {
    properties: ["opacity"],
    from: [1],
    to: [0]
};
const ADViewTransitionDissolveIn = {
    properties: ["opacity"],
    from: [0],
    to: [1]
};
const ADViewTransitionZoomIn = {
    properties: ["opacity", "transform"],
    from: [0, "scale(0.2)"],
    to: [1, "scale(1)"]
};
const ADViewTransitionZoomOut = {
    properties: ["opacity", "transform"],
    from: [0, "scale(1.2)"],
    to: [1, "scale(1)"]
};
const ADViewTransitionCrossSpinRight = {
    properties: ["opacity", "transform"],
    from: [0, "rotate(20deg)"],
    to: [1, "rotate(0)"]
};
const ADViewTransitionCrossSpinLeft = {
    properties: ["opacity", "transform"],
    from: [0, "rotate(-20deg)"],
    to: [1, "rotate(0)"]
};
const ADViewTransitionFlipLeftOut = {
    properties: ["transform"],
    from: ["perspective(800) rotateY(0deg)"],
    to: ["perspective(800) rotateY(-180deg)"]
};
const ADViewTransitionFlipLeftIn = {
    properties: ["transform"],
    from: ["perspective(800) rotateY(180deg)"],
    to: ["perspective(800) rotateY(0deg)"]
};
const ADViewTransitionFlipRightOut = {
    properties: ["transform"],
    from: ["perspective(800) rotateY(0deg)"],
    to: ["perspective(800) rotateY(180deg)"]
};
const ADViewTransitionFlipRightIn = {
    properties: ["transform"],
    from: ["perspective(800) rotateY(-180deg)"],
    to: ["perspective(800) rotateY(0deg)"]
};
const ADViewTransitionCubeLeftOut = {
    base: ["anchorPoint", new ADPoint(1, 0.5)],
    properties: ["transform"],
    from: ["perspective(800) rotateY(0deg) translateZ(0)"],
    to: ["perspective(800) rotateY(-90deg) translateZ($width)"]
};
const ADViewTransitionCubeLeftIn = {
    base: ["anchorPoint", new ADPoint(0, 0.5)],
    properties: ["transform"],
    from: ["perspective(800) rotateY(90deg) translateZ($width)"],
    to: ["perspective(800) rotateY(0deg) translateZ(0)"]
};
const ADViewTransitionCubeRightOut = {
    base: ["anchorPoint", new ADPoint(0, 0.5)],
    properties: ["transform"],
    from: ["perspective(800) rotateY(0deg) translateZ(0)"],
    to: ["perspective(800) rotateY(90deg) translateZ($width)"]
};
const ADViewTransitionCubeRightIn = {
    base: ["anchorPoint", new ADPoint(1, 0.5)],
    properties: ["transform"],
    from: ["perspective(800) rotateY(-90deg) translateZ($width)"],
    to: ["perspective(800) rotateY(0deg) translateZ(0)"]
};
const ADViewTransitionDoorOpenLeftOut = {
    base: ["anchorPoint", new ADPoint(0, 0.5), "zIndex", 1],
    properties: ["transform"],
    from: ["perspective(800) rotateY(0deg)"],
    to: ["perspective(800) rotateY(-90deg)"]
};
const ADViewTransitionDoorCloseLeftIn = {
    base: ["anchorPoint", new ADPoint(0, 0.5), "zIndex", 2],
    properties: ["transform"],
    from: ["perspective(800) rotateY(-90deg)"],
    to: ["perspective(800) rotateY(0deg)"]
};
const ADViewTransitionDoorOpenRightOut = {
    base: ["anchorPoint", new ADPoint(1, 0.5), "zIndex", 1],
    properties: ["transform"],
    from: ["perspective(800) rotateY(0deg)"],
    to: ["perspective(800) rotateY(90deg)"]
};
const ADViewTransitionDoorCloseRightIn = {
    base: ["anchorPoint", new ADPoint(1, 0.5), "zIndex", 2],
    properties: ["transform"],
    from: ["perspective(800) rotateY(90deg)"],
    to: ["perspective(800) rotateY(0deg)"]
};
const ADViewTransitionRevolveTowardsLeftOut = {
    base: ["anchorPoint", new ADPoint(0, 0.5)],
    properties: ["transform", "opacity"],
    from: ["perspective(800) rotateY(0deg)", 1],
    to: ["perspective(800) rotateY(-90deg)", 0]
};
const ADViewTransitionRevolveTowardsLeftIn = {
    base: ["anchorPoint", new ADPoint(0, 0.5)],
    properties: ["transform"],
    from: ["perspective(800) rotateY(90deg)"],
    to: ["perspective(800) rotateY(0deg)"]
};
const ADViewTransitionRevolveAwayLeftOut = {
    base: ["anchorPoint", new ADPoint(0, 0.5)],
    properties: ["transform"],
    from: ["perspective(800) rotateY(0deg)"],
    to: ["perspective(800) rotateY(90deg)"]
};
const ADViewTransitionRevolveAwayLeftIn = {
    base: ["anchorPoint", new ADPoint(0, 0.5)],
    properties: ["transform", "opacity"],
    from: ["perspective(800) rotateY(-90deg)", 0],
    to: ["perspective(800) rotateY(0deg)", 1]
};
const ADViewTransitionRevolveTowardsRightOut = {
    base: ["anchorPoint", new ADPoint(1, 0.5)],
    properties: ["transform", "opacity"],
    from: ["perspective(800) rotateY(0deg)", 1],
    to: ["perspective(800) rotateY(90deg)", 0]
};
const ADViewTransitionRevolveTowardsRightIn = {
    base: ["anchorPoint", new ADPoint(1, 0.5)],
    properties: ["transform"],
    from: ["perspective(800) rotateY(-90deg)"],
    to: ["perspective(800) rotateY(0deg)"]
};
const ADViewTransitionRevolveAwayRightOut = {
    base: ["anchorPoint", new ADPoint(1, 0.5)],
    properties: ["transform"],
    from: ["perspective(800) rotateY(0deg)"],
    to: ["perspective(800) rotateY(-90deg)"]
};
const ADViewTransitionRevolveAwayRightIn = {
    base: ["anchorPoint", new ADPoint(1, 0.5)],
    properties: ["transform", "opacity"],
    from: ["perspective(800) rotateY(90deg)", 0],
    to: ["perspective(800) rotateY(0deg)", 1]
};
const ADViewTransitionSpinOut = {
    properties: ["transform", "opacity"],
    from: ["perspective(800) rotate(0)", 1],
    to: ["perspective(800) rotate(-180deg)", 0]
};
const ADViewTransitionSpinIn = {
    base: ["zIndex", 1],
    properties: ["transform", "opacity"],
    from: ["perspective(800) rotate(-180deg)", 0],
    to: ["perspective(800) rotate(0)", 1]
};
const ADViewTransitionScaleIn = {
    base: ["zIndex", 1],
    properties: ["transform"],
    from: ["scale(0.01)"],
    to: ["scale(1)"]
};
const ADViewTransitionScaleOut = {
    base: ["zIndex", 1],
    properties: ["transform"],
    from: ["scale(1)"],
    to: ["scale(0.01)"]
};
function ADViewLayerInsertionNotificationHelper() {
    this.callSuper();
    this.views = [];
    document.addEventListener("DOMSubtreeModified", this, true)
}
ADViewLayerInsertionNotificationHelper.prototype.considerView = function(a) {
    if (this.views.indexOf(a) == -1) {
        a._ignoreView = false;
        this.views.push(a)
    }
};
ADViewLayerInsertionNotificationHelper.prototype.handleEvent = function(a) {
    this.processViews()
};
ADViewLayerInsertionNotificationHelper.prototype.processViews = function() {
    if (this.views.length < 1) {
        return
    }
    var a;
    for (var b = 0; b < this.views.length; ++b) {
        a = this.views[b];
        for (var c = a.superview; c && !a._ignoreView; c = c.superview) {
            if ("_ignoreView" in c) {
                a._ignoreView = true
            }
        }
    }
    while (this.views.length > 0) {
        a = this.views.pop();
        if (a._ignoreView) {
            delete a._ignoreView;
            continue
        }
        delete a._ignoreView;
        a.dispatchNotificationOfLayerInsertionIntoDocument()
    }
};
ADClass(ADViewLayerInsertionNotificationHelper);
ADView.inherits = ADObject;
ADView.synthetizes = ["id", "position", "size", "transform", "anchorPoint", "doubleSided", "zIndex", "opacity", "clipsToBounds", "transitionsEnabled", "transitionsDuration", "hostingLayer"];
ADView.layerInsertionNotificationHelper = new ADViewLayerInsertionNotificationHelper();
function ADView() {
    this.callSuper();
    this.superview = null;
    this.subviews = [];
    this.tracksTouchesOnceTouchesBegan = false;
    this.userInteractionEnabled = true;
    this.autoresizesSubviews = true;
    this.autoresizingMask = ADViewAutoresizingNone;
    this.layerIsInDocument = false;
    this._position = new ADPoint();
    this._size = new ADSize();
    this._anchorPoint = new ADPoint(0.5, 0.5);
    this._doubleSided = true;
    this._zIndex = 0;
    this._transform = ADUtils.t(0, 0);
    this._clipsToBounds = false;
    this._transitionsEnabled = false;
    this._transitionsDuration = 0.5;
    this._hostingLayer = null;
    if (ADUtils.objectIsUndefined(this.layer) || this.layer === null) {
        this.createLayer()
    }
    this.layer.addEventListener(ADStartEvent, this, false);
    this.layer.addEventListener("DOMNodeInsertedIntoDocument", this, true);
    this.layer.addEventListener("DOMNodeRemovedFromDocument", this, true);
    this.layer._view = this
}
ADView.prototype.toString = function() {
    return [this.constructor.name, "[", this._size.width, "x", this._size.height, "@", this._position.x, ",", this._position.y, "]"].join("")
};
ADView.prototype.getId = function() {
    return this.layer.id
};
ADView.prototype.setId = function(a) {
    this.layer.id = a
};
ADView.prototype.setPosition = function(a) {
    if (this._position.equals(a)) {
        return
    }
    this._position = a;
    this.updateLayerTransform()
};
ADView.prototype.setSize = function(a) {
    if (this._size.equals(a)) {
        return
    }
    var b = this._size.copy();
    this._size = a;
    this.layer.style.width = a.width + "px";
    this.layer.style.height = a.height + "px";
    if (this.autoresizesSubviews) {
        this.resizeSubviewsWithOldSize(b)
    }
};
ADView.prototype.setTransform = function(a) {
    this._transform = a;
    this.updateLayerTransform()
};
ADView.prototype.setAnchorPoint = function(a) {
    this._anchorPoint = a;
    this.layer.style.webkitTransformOrigin = Math.round(a.x * 100) + "% " + Math.round(a.y * 100) + "% 0"
};
ADView.prototype.setDoubleSided = function(a) {
    this._doubleSided = a;
    this.layer.style.webkitBackfaceVisibility = a ? "visible": "hidden"
};
ADView.prototype.setZIndex = function(a) {
    this._zIndex = a;
    this.layer.style.zIndex = a
};
ADView.prototype.updateLayerTransform = function() {
    this.layer.style.webkitTransform = ADUtils.t(this._position.x, this._position.y) + this._transform
};
ADView.prototype.getOpacity = function() {
    return Number(window.getComputedStyle(this.layer).opacity)
};
ADView.prototype.setOpacity = function(a) {
    this.layer.style.opacity = a
};
ADView.prototype.setTransitionsEnabled = function(a) {
    if (a) {
        this.layer.style.webkitTransitionProperty = "-webkit-transform, opacity";
        this.layer.style.webkitTransitionDuration = this._transitionsDuration + "s"
    } else {
        this.layer.style.webkitTransitionDuration = "0s"
    }
    this._transitionsEnabled = a
};
ADView.prototype.setTransitionsDuration = function(a) {
    this.layer.style.webkitTransitionDuration = a + "s";
    this._transitionsDuration = a
};
ADView.prototype.setClipsToBounds = function(a) {
    this._clipsToBounds = a;
    this.layer.style.overflow = a ? "hidden": "visible"
};
ADView.prototype.getHostingLayer = function() {
    return (this._hostingLayer != null) ? this._hostingLayer: this.layer
};
ADView.prototype.addSubview = function(a) {
    return this.insertSubviewAtIndex(a, this.subviews.length)
};
ADView.prototype.removeFromSuperview = function() {
    if (this.superview == null) {
        return
    }
    this.willMoveToSuperview(null);
    this.superview.willRemoveSubview(this);
    var a = this._indexInSuperviewSubviews;
    this.superview.subviews.splice(a, 1);
    for (var b = a; b < this.superview.subviews.length; b++) {
        this.superview.subviews[b]._indexInSuperviewSubviews = b
    }
    this.layer.parentNode.removeChild(this.layer);
    this.superview = null;
    this.didMoveToSuperview()
};
ADView.prototype.insertSubviewAtIndex = function(b, a) {
    if (a > this.subviews.length) {
        return
    }
    if (b.superview === this) {
        a--
    }
    b.removeFromSuperview();
    b.willMoveToSuperview(this);
    this.subviews.splice(a, 0, b);
    b._indexInSuperviewSubviews = a;
    for (var d = a + 1; d < this.subviews.length; d++) {
        this.subviews[d]._indexInSuperviewSubviews = d
    }
    var c = this.hostingLayer;
    if (a == this.subviews.length - 1) {
        c.appendChild(b.layer)
    } else {
        c.insertBefore(b.layer, this.subviews[a + 1].layer)
    }
    b.superview = this;
    b.didMoveToSuperview();
    this.didAddSubview(b);
    return b
};
ADView.prototype.insertSubviewAfterSubview = function(c, a) {
    if (a.superview !== this) {
        return
    }
    var b = a._indexInSuperviewSubviews + 1;
    if (b < this.subviews.length) {
        this.insertSubviewAtIndex(c, b)
    } else {
        this.addSubview(c)
    }
    return c
};
ADView.prototype.insertSubviewBeforeSubview = function(b, a) {
    if (a.superview !== this) {
        return
    }
    return this.insertSubviewAtIndex(b, a._indexInSuperviewSubviews)
};
ADView.prototype.exchangeSubviewsAtIndices = function(c, d) {
    if (c >= this.subviews.length || d >= this.subviews.length) {
        return
    }
    var a = this.subviews[c];
    var g = this.subviews[d];
    this.subviews[c] = g;
    this.subviews[d] = a;
    a._indexInSuperviewSubviews = d;
    g._indexInSuperviewSubviews = c;
    var f = a.layer;
    var h = g.layer;
    var e = this.hostingLayer;
    var i = f.nextSibling;
    var b = h.nextSibling;
    if (i != null) {
        e.insertBefore(h, i)
    } else {
        e.appendChild(h)
    }
    if (b != null) {
        e.insertBefore(f, b)
    } else {
        e.appendChild(f)
    }
};
ADView.prototype.isDescendantOfView = function(c) {
    var b = false;
    var a = this;
    while (a.superview != null) {
        if (a.superview === c) {
            b = true;
            break
        }
        a = a.superview
    }
    return b
};
ADView.prototype.createLayer = function() {
    this.layer = document.createElement("div");
    this.layer.className = "ad-view"
};
ADView.prototype.willMoveToSuperview = function(a) {};
ADView.prototype.didMoveToSuperview = function() {};
ADView.prototype.didAddSubview = function(a) {};
ADView.prototype.willRemoveSubview = function(a) {};
ADView.prototype.layerWasInsertedIntoDocument = function() {
    this.layerIsInDocument = true
};
ADView.prototype.layerWasRemovedFromDocument = function() {};
ADView.prototype.handleLayerInsertionIntoDocument = function(a) {
    if (a.target !== this.layer || this.layer.ownerDocument !== document || this.layerIsInDocument) {
        return
    }
    a.stopPropagation();
    ADView.layerInsertionNotificationHelper.considerView(this)
};
ADView.prototype.dispatchNotificationOfLayerInsertionIntoDocument = function() {
    var d = [];
    var a = [].concat(this.subviews);
    var c = 0;
    while (a.length > 0) {
        var b = a.shift();
        if (typeof(b) === "number") {
            c = b
        } else {
            if (d[c] === undefined) {
                d[c] = []
            }
            d[c].push(b);
            a = a.concat(c + 1, b.subviews)
        }
    }
    var e;
    while (d.length > 0) {
        e = d.pop();
        while (e.length > 0) {
            e.pop().layerWasInsertedIntoDocument()
        }
    }
    this.layerWasInsertedIntoDocument()
};
ADView.prototype.handleLayerRemovalFromDocument = function(a) {
    if (a.target === this.layer && this.layer.ownerDocument === document) {
        a.stopPropagation();
        this.layerIsInDocument = false;
        this.layerWasRemovedFromDocument()
    }
};
ADView.prototype.handleEvent = function(a) {
    switch (a.type) {
    case ADStartEvent:
        this.touchesBegan(a);
        break;
    case ADMoveEvent:
        this.touchesMoved(a);
        break;
    case ADEndEvent:
        this.touchesEnded(a);
        break;
    case "touchcancel":
        this.touchesCancelled(a);
        break;
    case "DOMNodeInsertedIntoDocument":
        this.handleLayerInsertionIntoDocument(a);
        break;
    case "DOMNodeRemovedFromDocument":
        this.handleLayerRemovalFromDocument(a);
        break
    }
};
ADView.prototype.touchesBegan = function(a) {
    if (!this.userInteractionEnabled) {
        return
    }
    if (this.tracksTouchesOnceTouchesBegan) {
        window.addEventListener(ADMoveEvent, this, true);
        window.addEventListener(ADEndEvent, this, true);
        window.addEventListener("touchcancel", this, true)
    }
};
ADView.prototype.touchesMoved = function(a) {
    if (!this.userInteractionEnabled) {
        return
    }
    a.preventDefault()
};
ADView.prototype.touchesEnded = function(a) {
    if (!this.userInteractionEnabled) {
        return
    }
    window.removeEventListener(ADMoveEvent, this, true);
    window.removeEventListener(ADEndEvent, this, true);
    window.removeEventListener("touchcancel", this, true)
};
ADView.prototype.touchesCancelled = function(a) {
    if (!this.userInteractionEnabled) {
        return
    }
    window.removeEventListener(ADMoveEvent, this, true);
    window.removeEventListener(ADEndEvent, this, true);
    window.removeEventListener("touchcancel", this, true)
};
ADView.prototype.pointInside = function(a) {
    return (a.x >= 0 && a.x <= this.size.width && a.y >= 0 && a.y <= this.size.height)
};
const ADViewAutoresizingNone = 0;
const ADViewAutoresizingFlexibleLeftMargin = 1 << 0;
const ADViewAutoresizingFlexibleWidth = 1 << 1;
const ADViewAutoresizingFlexibleRightMargin = 1 << 2;
const ADViewAutoresizingFlexibleTopMargin = 1 << 3;
const ADViewAutoresizingFlexibleHeight = 1 << 4;
const ADViewAutoresizingFlexibleBottomMargin = 1 << 5;
ADView.prototype.resizeSubviewsWithOldSize = function(b) {
    for (var a = 0; a < this.subviews.length; a++) {
        this.subviews[a].resizeWithOldSuperviewSize(b)
    }
};
ADView.prototype.resizeWithOldSuperviewSize = function(d) {
    var a = this._position.copy();
    var e = this._size.copy();
    var g = this.autoresizingMask;
    var f = (g & ADViewAutoresizingFlexibleLeftMargin) + (g & ADViewAutoresizingFlexibleWidth) + (g & ADViewAutoresizingFlexibleRightMargin);
    switch (f) {
    case ADViewAutoresizingNone:
        break;
    case ADViewAutoresizingFlexibleLeftMargin:
        a.x += this.superview._size.width - d.width;
        break;
    case ADViewAutoresizingFlexibleWidth:
        e.width = this.superview._size.width - (d.width - this._size.width);
        break;
    case ADViewAutoresizingFlexibleLeftMargin | ADViewAutoresizingFlexibleWidth: var b = (d.width - this._size.width - this._position.x);
        a.x = (this._position.x / (d.width - b)) * (this.superview._size.width - b);
        e.width = this.superview._size.width - a.x - b;
        break;
    case ADViewAutoresizingFlexibleRightMargin:
        break;
    case ADViewAutoresizingFlexibleLeftMargin | ADViewAutoresizingFlexibleRightMargin: var b = (d.width - this._size.width - this._position.x);
        a.x += (this.superview._size.width - d.width) * (this.position.x / (this.position.x + b));
        break;
    case ADViewAutoresizingFlexibleRightMargin | ADViewAutoresizingFlexibleWidth: var b = (d.width - this._size.width - this._position.x);
        scaled_right_margin = (b / (d.width - this._position.x)) * (this.superview._size.width - this._position.x);
        e.width = this.superview._size.width - a.x - scaled_right_margin;
        break;
    case ADViewAutoresizingFlexibleLeftMargin | ADViewAutoresizingFlexibleWidth | ADViewAutoresizingFlexibleRightMargin: a.x = (this._position.x / d.width) * this.superview._size.width;
        e.width = (this._size.width / d.width) * this.superview._size.width;
        break
    }
    var h = (g & ADViewAutoresizingFlexibleTopMargin) + (g & ADViewAutoresizingFlexibleHeight) + (g & ADViewAutoresizingFlexibleBottomMargin);
    switch (h) {
    case ADViewAutoresizingNone:
        break;
    case ADViewAutoresizingFlexibleTopMargin:
        a.y += this.superview._size.height - d.height;
        break;
    case ADViewAutoresizingFlexibleHeight:
        e.height = this.superview._size.height - (d.height - this._size.height);
        break;
    case ADViewAutoresizingFlexibleTopMargin | ADViewAutoresizingFlexibleHeight: var c = (d.height - this._size.height - this._position.y);
        a.y = (this._position.y / (d.height - c)) * (this.superview._size.height - c);
        e.height = this.superview._size.height - a.y - c;
        break;
    case ADViewAutoresizingFlexibleBottomMargin:
        break;
    case ADViewAutoresizingFlexibleTopMargin | ADViewAutoresizingFlexibleBottomMargin: var c = (d.height - this._size.height - this._position.y);
        a.y += (this.superview._size.height - d.height) * (this.position.y / (this.position.y + c));
        break;
    case ADViewAutoresizingFlexibleBottomMargin | ADViewAutoresizingFlexibleHeight: var c = (d.height - this._size.height - this._position.y);
        scaled_bottom_margin = (c / (d.height - this._position.y)) * (this.superview._size.height - this._position.y);
        e.height = this.superview._size.height - a.y - scaled_bottom_margin;
        break;
    case ADViewAutoresizingFlexibleTopMargin | ADViewAutoresizingFlexibleHeight | ADViewAutoresizingFlexibleBottomMargin: a.y = (this._position.y / d.height) * this.superview._size.height;
        e.height = (this._size.height / d.height) * this.superview._size.height;
        break
    }
    this.position = a;
    this.size = e
};
const ADViewPropertyMapping = {
    opacity: "opacity",
    transform: "-webkit-transform",
    position: "-webkit-transform",
    anchorPoint: "-webkit-transform-origin",
    doubleSided: "-webkit-backface-visibility",
    zIndex: "z-index"
};
ADView.prototype.cssPropertyNameForJSProperty = function(a) {
    return ADViewPropertyMapping[a]
};
ADView.prototype.applyTransition = function(e, c) {
    if (e === null) {
        return
    }
    var d = new ADTransition(e);
    d.target = this;
    if (c) {
        var b = d.from;
        d.from = d.to;
        d.to = b
    }
    if (e.base) {
        for (var a = 0; a < e.base.length; a += 2) {
            this[e.base[a]] = e.base[a + 1]
        }
    }
    d.start()
};
ADView.getViewById = function(b) {
    var a = document.getElementById(b);
    return (a && !ADUtils.objectIsUndefined(a._view)) ? a._view: null
};
ADClass(ADView);
ADContentView.inherits = ADView;
function ADContentView(b) {
    var a = b;
    if (ADUtils.objectIsString(b)) {
        a = document.querySelector(b)
    }
    this.layer = a;
    this.callSuper();
    this.layer.addClassName("ad-view");
    if (a === document.body) {
        this.size = new ADSize(window.innerWidth, window.innerHeight)
    }
}
ADContentView.prototype.layerWasInsertedIntoDocument = function() {
    this.callSuper();
    this.refreshSize()
};
ADContentView.prototype.refreshSize = function() {
    var a = window.getComputedStyle(this.layer);
    this._size = new ADSize(parseInt(a.width), parseInt(a.height))
};
ADClass(ADContentView);
ADRootView.inherits = ADContentView;
ADRootView.synthetizes = ["disablesDefaultScrolling"];
function ADRootView(a) {
    this.callSuper(a);
    this._disablesDefaultScrolling = true;
    this.disablesDefaultScrolling = true;
    if (this.layer === document.body) {
        window.addEventListener("orientationchange", this, false);
        this.layer.removeClassName("ad-view")
    }
}
ADRootView.prototype.setDisablesDefaultScrolling = function(a) {
    this.layer[a ? "addEventListener": "removeEventListener"](ADMoveEvent, ADUtils.preventEventDefault, false);
    this._disablesDefaultScrolling = a
};
ADRootView.prototype.handleEvent = function(a) {
    this.callSuper(a);
    if (a.type == "orientationchange") {
        var b = this;
        setTimeout(function() {
            b.size = new ADSize(window.innerWidth, window.innerHeight);
            window.scrollTo(0, 0)
        },
        0)
    }
};
ADRootView._sharedRoot = null;
ADRootView.__defineGetter__("sharedRoot",
function() {
    if (ADRootView._sharedRoot === null) {
        ADRootView._sharedRoot = new ADRootView(document.body)
    }
    return ADRootView._sharedRoot
});
ADRootView.__defineSetter__("sharedRoot",
function(a) {
    ADRootView._sharedRoot = a
});
ADClass(ADRootView);
const ADHTMLFragmentLoaderDidFail = "htmlFragmentLoaderDidFail";
const ADHTMLFragmentLoaderDidLoad = "htmlFragmentLoaderDidLoad";
ADViewController.inherits = ADObject;
ADHTMLFragmentLoader.includes = [ADEventTriage];
function ADHTMLFragmentLoader(a, b) {
    this.callSuper();
    this.url = a;
    this.delegate = b;
    this.fragment = null;
    this.request = new XMLHttpRequest();
    this.request.addEventListener("load", this, false);
    this.request.addEventListener("error", this, false);
    if (this.url !== undefined) {
        this.load()
    }
}
ADHTMLFragmentLoader.prototype.load = function() {
    this.fragment = null;
    this.request.abort();
    var b = (this.url.indexOf("http") == 0);
    this.request.open("GET", this.url, b);
    try {
        this.request.send()
    } catch(a) {
        this.requestDidFail()
    }
    return b
};
ADHTMLFragmentLoader.prototype.handleLoad = function(a) {
    this.requestDidLoad()
};
ADHTMLFragmentLoader.prototype.handleError = function(a) {
    this.requestDidFail()
};
ADHTMLFragmentLoader.prototype.requestDidLoad = function() {
    this.fragment = ADUtils.createNodeFromString(this.request.responseText);
    if (ADUtils.objectHasMethod(this.delegate, ADHTMLFragmentLoaderDidLoad)) {
        this.delegate[ADHTMLFragmentLoaderDidLoad](this)
    }
};
ADHTMLFragmentLoader.prototype.requestDidFail = function() {
    if (ADUtils.objectHasMethod(this.delegate, ADHTMLFragmentLoaderDidFail)) {
        this.delegate[ADHTMLFragmentLoaderDidFail](this)
    }
};
ADClass(ADHTMLFragmentLoader);
const ADScrollIndicatorThickness = 7;
const ADScrollIndicatorEndSize = 3;
const ADScrollIndicatorTypeHorizontal = "horizontal";
const ADScrollIndicatorTypeVertical = "vertical";
ADScrollIndicator.inherits = ADView;
ADScrollIndicator.synthetizes = ["visible", "width", "height", "style"];
function ADScrollIndicator(a) {
    this.callSuper();
    this.type = a;
    this.layer.addClassName(a);
    this._visible = false;
    this._width = ADScrollIndicatorThickness;
    this._height = ADScrollIndicatorThickness;
    this.position = new ADPoint( - ADScrollIndicatorThickness, -ADScrollIndicatorThickness);
    this.positionBeforeHide = this.position;
    this.lastPositionUpdateInHide = false;
    this._style = ADScrollViewIndicatorStyleDefault;
    this.visible = false
}
ADScrollIndicator.prototype.createLayer = function() {
    this.layer = document.createElement("div");
    this.layer.addClassName("ad-scroll-indicator");
    this.layer.addEventListener("webkitTransitionEnd", this, false);
    this.start = this.layer.appendChild(document.createElement("div"));
    this.middle = this.layer.appendChild(document.createElement("img"));
    this.end = this.layer.appendChild(document.createElement("div"))
};
ADScrollIndicator.prototype.setPosition = function(a) {
    a.x = Math.round(a.x);
    a.y = Math.round(a.y);
    this.callSuper(a);
    this.lastPositionUpdateInHide = false
};
ADScrollIndicator.prototype.setSize = function(a) {
    this.width = a.width;
    this.height = a.height;
    this._size = a
};
ADScrollIndicator.prototype.setStyle = function(c) {
    this._style = c;
    this.layer.removeClassName(this._style);
    this.layer.addClassName(this._style);
    var a = (this.type === ADScrollIndicatorTypeHorizontal) ? "Horizontal": "Vertical";
    var b = "Default";
    switch (c) {
    case ADScrollViewIndicatorStyleWhite:
        b = "White";
        break;
    case ADScrollViewIndicatorStyleBlack:
        b = "Black";
        break
    }
    this.middle.src = ADUtils.assetsPath + "scrollindicator/UIScrollerIndicator" + b + a + "Middle.png"
};
ADScrollIndicator.prototype.setWidth = function(a) {
    this.middle.style.webkitTransform = "translate3d(0,0,0) scale(" + (a - ADScrollIndicatorEndSize * 2) + ",1)";
    this.end.style.webkitTransform = "translate3d(" + (a - ADScrollIndicatorEndSize) + "px,0,0)";
    this._width = a
};
ADScrollIndicator.prototype.setHeight = function(a) {
    this.middle.style.webkitTransform = "translate3d(0,0,0) scale(1," + (a - ADScrollIndicatorEndSize * 2) + ")";
    this.end.style.webkitTransform = "translate3d(0," + (a - ADScrollIndicatorEndSize) + "px,0)";
    this._height = a
};
ADScrollIndicator.prototype.setVisible = function(a) {
    if (a) {
        this.fading = false;
        this.opacity = 1;
        this.position = this.lastPositionUpdateInHide ? this.positionBeforeHide: this.position
    } else {
        if (!this.fading) {
            this.fading = true;
            this.opacity = 0;
            this.lastPositionUpdateInHide = true;
            this.positionBeforeHide = this.position
        }
    }
    this._visible = a
};
ADScrollIndicator.prototype.flash = function() {
    this.flashing = true
};
ADScrollIndicator.prototype.handleEvent = function(a) {
    if (a.type != "webkitTransitionEnd") {
        return
    }
    this.callSuper(a);
    if (this.flashing) {
        this.flashing = false
    } else {
        if (this.fading) {
            this.position = new ADPoint( - ADScrollIndicatorThickness, -ADScrollIndicatorThickness);
            this.fading = false
        }
    }
};
ADClass(ADScrollIndicator);
const ADScrollViewWillBeginDragging = "scrollViewWillBeginDragging";
const ADScrollViewDidEndScrollingAnimation = "scrollViewDidEndScrollingAnimation";
const ADScrollViewDidScroll = "scrollViewDidScroll";
const ADScrollViewDidEndDragging = "scrollViewDidEndDragging";
const ADScrollViewWillBeginDecelerating = "scrollViewWillBeginDecelerating";
const ADScrollViewDidEndDecelerating = "scrollViewDidEndDecelerating";
const ADScrollViewMinimumTrackingForDrag = 5;
const ADScrollViewPagingTransitionDuration = "0.25s";
const ADScrollViewMinIndicatorLength = 34;
const ADScrollViewAcceleration = 15;
const ADScrollViewMaxTimeForTrackingDataPoints = 100;
const ADScrollViewDecelerationFrictionFactor = 0.95;
const ADScrollViewDesiredAnimationFrameRate = 1000 / 60;
const ADScrollViewMinimumVelocity = 0.05;
const ADScrollViewPenetrationDeceleration = 0.08;
const ADScrollViewPenetrationAcceleration = 0.15;
const ADScrollViewMinVelocityForDeceleration = 1;
const ADScrollViewMinVelocityForDecelerationWithPaging = 4;
const ADScrollViewMaxVelocityForBouncingWithPaging = 20;
const ADScrollViewClickableElementNames = ["a", "button", "input", "select"];
const ADScrollViewContentTouchesDelay = 150;
const ADScrollViewAutomatedContentSize = -1;
const ADScrollViewIndicatorStyleDefault = "indicator-default";
const ADScrollViewIndicatorStyleBlack = "indicator-black";
const ADScrollViewIndicatorStyleWhite = "indicator-white";
ADScrollView.inherits = ADView;
ADScrollView.synthetizes = ["contentOffset", "contentSize", "indicatorStyle", "scrollEnabled", "scrollIndicatorInsets"];
function ADScrollView() {
    this.callSuper();
    this._contentOffset = new ADPoint();
    this._contentSize = ADScrollViewAutomatedContentSize;
    this.adjustedContentSize = new ADSize();
    this.tracking = false;
    this.dragging = false;
    this.horizontalScrollEnabled = true;
    this.verticalScrollEnabled = true;
    this.decelerating = false;
    this.decelerationTimer = null;
    this._indicatorStyle = "";
    this.showsHorizontalScrollIndicator = true;
    this.showsVerticalScrollIndicator = true;
    this.scrollIndicatorsNeedFlashing = false;
    this._scrollIndicatorInsets = new ADEdgeInsets(0, 0, 0, 0);
    this.pagingEnabled = false;
    this.bounces = true;
    this.clipsToBounds = true;
    this.delegate = null;
    this.layer.addEventListener("webkitTransitionEnd", this, false);
    this.hostingLayer.addEventListener("webkitTransitionEnd", this, false);
    this.indicatorStyle = ADScrollViewIndicatorStyleDefault;
    this.tracksTouchesOnceTouchesBegan = true;
    this.layer.addEventListener(ADStartEvent, this, true);
    this.delaysContentTouches = true;
    this.canCancelContentTouches = true;
    this._scrollEnabled = true;
    this._setContentOffsetWithAnimationCalledFromSetter = false
}
ADScrollView.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-scroll-view");
    this.horizontalScrollIndicator = new ADScrollIndicator(ADScrollIndicatorTypeHorizontal);
    this.verticalScrollIndicator = new ADScrollIndicator(ADScrollIndicatorTypeVertical);
    this.layer.appendChild(this.horizontalScrollIndicator.layer);
    this.layer.appendChild(this.verticalScrollIndicator.layer);
    this.hostingLayer = this.layer.insertBefore(document.createElement("div"), this.horizontalScrollIndicator.layer);
    this.hostingLayer.className = "hosting-layer"
};
ADScrollView.prototype.setSize = function(a) {
    this.callSuper(a);
    this.adjustContentSize(true)
};
ADScrollView.prototype.setScrollEnabled = function(a) {
    this._scrollEnabled = a;
    if (!a) {
        this.stopTrackingTouches()
    }
};
ADScrollView.prototype.setContentOffset = function(a) {
    this._setContentOffsetWithAnimationCalledFromSetter = true;
    this.setContentOffsetWithAnimation(a, false)
};
ADScrollView.prototype.setContentOffsetWithAnimation = function(b, a) {
    if (b.equals(this._contentOffset)) {
        return
    }
    this._contentOffset = b;
    if (!this.dragging && !this.decelerating) {
        this.adjustContentSize(false);
        this._contentOffset.x = Math.max(Math.min(this.maxPoint.x, this._contentOffset.x), 0);
        this._contentOffset.y = Math.max(Math.min(this.maxPoint.y, this._contentOffset.y), 0)
    }
    this.hostingLayer.style.webkitTransform = ADUtils.t( - this._contentOffset.x, -this._contentOffset.y);
    if (a) {
        this.scrollTransitionsNeedRemoval = true;
        this.hostingLayer.style.webkitTransitionDuration = ADScrollViewPagingTransitionDuration
    } else {
        this.didScroll(false)
    }
    if (!a) {
        if (this.horizontalScrollEnabled && this.showsHorizontalScrollIndicator) {
            this.updateHorizontalScrollIndicator()
        }
        if (this.verticalScrollEnabled && this.showsVerticalScrollIndicator) {
            this.updateVerticalScrollIndicator()
        }
    }
    if (!this._setContentOffsetWithAnimationCalledFromSetter) {
        this.notifyPropertyChange("contentOffset")
    }
    this._setContentOffsetWithAnimationCalledFromSetter = false
};
ADScrollView.prototype.snapContentOffsetToBounds = function(a) {
    var b = false;
    var c = new ADPoint();
    if (this.pagingEnabled) {
        c.x = Math.round(this._contentOffset.x / this._size.width) * this._size.width;
        c.y = Math.round(this._contentOffset.y / this._size.height) * this._size.height;
        b = true
    } else {
        if (this.bounces) {
            c.x = Math.max(Math.min(this.maxPoint.x, this._contentOffset.x), 0);
            c.y = Math.max(Math.min(this.maxPoint.y, this._contentOffset.y), 0);
            b = (c.x != this._contentOffset.x || c.y != this._contentOffset.y)
        }
    }
    if (b) {
        this.setContentOffsetWithAnimation(c, a)
    }
};
ADScrollView.prototype.getContentSize = function() {
    var c = this._contentSize;
    if (c === ADScrollViewAutomatedContentSize) {
        c = new ADSize(this._hostingLayer.offsetWidth, this._hostingLayer.offsetHeight);
        if (this.subviews.length) {
            for (var a = 0; a < this.subviews.length; a++) {
                var b = this.subviews[a];
                c.width = Math.max(c.width, b.position.x + b.size.width);
                c.height = Math.max(c.height, b.position.y + b.size.height)
            }
        }
    }
    return c
};
ADScrollView.prototype.setContentSize = function(a) {
    this._contentSize = a;
    this.adjustContentSize(false)
};
ADScrollView.prototype.adjustContentSize = function(a) {
    if (a) {
        var b = new ADPoint();
        if (this.adjustedContentSize.width != 0) {
            b.x = this._contentOffset.x / this.adjustedContentSize.width
        }
        if (this.adjustedContentSize.height != 0) {
            b.y = this._contentOffset.y / this.adjustedContentSize.height
        }
    }
    this.adjustedContentSize.width = Math.max(this._size.width, this.contentSize.width);
    this.adjustedContentSize.height = Math.max(this._size.height, this.contentSize.height);
    this.maxPoint = new ADPoint(this.adjustedContentSize.width - this._size.width, this.adjustedContentSize.height - this._size.height);
    if (a) {
        this.contentOffset = new ADPoint(Math.min(b.x * this.adjustedContentSize.width, this.maxPoint.x), Math.min(b.y * this.adjustedContentSize.height, this.maxPoint.y))
    }
};
ADScrollView.prototype.setIndicatorStyle = function(a) {
    this._indicatorStyle = a;
    this.horizontalScrollIndicator.style = a;
    this.verticalScrollIndicator.style = a
};
ADScrollView.prototype.setScrollIndicatorInsets = function(a) {
    this._scrollIndicatorInsets = a;
    if (this.horizontalScrollIndicator.visible) {
        this.updateHorizontalScrollIndicator()
    }
    if (this.verticalScrollIndicator.visible) {
        this.updateVerticalScrollIndicator()
    }
};
ADScrollView.prototype.updateHorizontalScrollIndicator = function() {
    var d = (this.verticalScrollEnabled && this.showsVerticalScrollIndicator) ? ADScrollIndicatorEndSize * 2: 1;
    var b = this._size.width - this._scrollIndicatorInsets.left - this._scrollIndicatorInsets.right - d;
    var e = Math.max(ADScrollViewMinIndicatorLength, Math.round((this._size.width / this.adjustedContentSize.width) * b));
    var a = (this._contentOffset.x / (this.adjustedContentSize.width - this._size.width)) * (b - d - e) + this._scrollIndicatorInsets.left;
    var c = this._size.height - ADScrollIndicatorThickness - 1 - this._scrollIndicatorInsets.bottom;
    if (this._contentOffset.x < 0) {
        e = Math.round(Math.max(e + this._contentOffset.x, ADScrollIndicatorThickness));
        a = 1 + this._scrollIndicatorInsets.left
    } else {
        if (this._contentOffset.x > this.maxPoint.x) {
            e = Math.round(Math.max(e + this.adjustedContentSize.width - this._size.width - this._contentOffset.x, ADScrollIndicatorThickness));
            a = this._size.width - e - d - this._scrollIndicatorInsets.right
        }
    }
    this.horizontalScrollIndicator.position = new ADPoint(a, c);
    this.horizontalScrollIndicator.width = e
};
ADScrollView.prototype.updateVerticalScrollIndicator = function() {
    var c = (this.horizontalScrollEnabled && this.showsHorizontalScrollIndicator) ? ADScrollIndicatorEndSize * 2: 1;
    var d = this._size.height - this._scrollIndicatorInsets.top - this._scrollIndicatorInsets.bottom - c;
    var e = Math.max(ADScrollViewMinIndicatorLength, Math.round((this._size.height / this.adjustedContentSize.height) * d));
    var a = this._size.width - ADScrollIndicatorThickness - 1 - this._scrollIndicatorInsets.right;
    var b = (this._contentOffset.y / (this.adjustedContentSize.height - this._size.height)) * (d - c - e) + this._scrollIndicatorInsets.top;
    if (this._contentOffset.y < 0) {
        e = Math.round(Math.max(e + this._contentOffset.y, ADScrollIndicatorThickness));
        b = 1 + this._scrollIndicatorInsets.top
    } else {
        if (this._contentOffset.y > this.maxPoint.y) {
            e = Math.round(Math.max(e + this.adjustedContentSize.height - this._size.height - this._contentOffset.y, ADScrollIndicatorThickness));
            b = this._size.height - e - c - this._scrollIndicatorInsets.bottom
        }
    }
    this.verticalScrollIndicator.position = new ADPoint(a, b);
    this.verticalScrollIndicator.height = e
};
ADScrollView.prototype.flashScrollIndicators = function(a) {
    if (a) {
        this.scrollIndicatorsNeedFlashing = true;
        return
    }
    if (this.horizontalScrollEnabled && this.showsHorizontalScrollIndicator && (this.adjustedContentSize.width > this._size.width)) {
        this.updateHorizontalScrollIndicator();
        this.horizontalScrollIndicator.flash()
    }
    if (this.verticalScrollEnabled && this.showsVerticalScrollIndicator && (this.adjustedContentSize.height > this._size.height)) {
        this.updateVerticalScrollIndicator();
        this.verticalScrollIndicator.flash()
    }
};
ADScrollView.prototype.hideScrollIndicators = function() {
    this.horizontalScrollIndicator.visible = false;
    this.verticalScrollIndicator.visible = false
};
ADScrollView.prototype.handleEvent = function(a) {
    this.callSuper(a);
    if (a.type == "webkitTransitionEnd") {
        this.transitionEnded(a)
    }
};
ADScrollView.prototype.touchesBegan = function(a) {
    if (!this._scrollEnabled) {
        return
    }
    if (a.eventPhase == Event.CAPTURING_PHASE) {
        if (a._manufactured) {
            return
        }
        this.originalTarget = (ADSupportsTouches ? a.touches[0] : a).target;
        if (this.delaysContentTouches) {
            a.stopPropagation();
            this.callMethodNameAfterDelay("beginTouchesInContent", ADScrollViewContentTouchesDelay, a);
            if (!this.tracking) {
                this.beginTracking(a)
            }
        }
    } else {
        if (!this.tracking) {
            this.beginTracking(a)
        }
    }
};
ADScrollView.prototype.beginTouchesInContent = function(a) {
    if (this.tracking && !this.dragging) {
        var b = ADUtils.createUIEvent(ADStartEvent, a);
        b._manufactured = true;
        this.originalTarget.dispatchEvent(b);
        if (!this.canCancelContentTouches) {
            this.touchesEnded(ADUtils.createUIEvent(ADEndEvent, a))
        }
    }
};
ADScrollView.prototype.beginTracking = function(a) {
    a.preventDefault();
    this.stopDecelerationAnimation();
    this.hostingLayer.style.webkitTransitionDuration = 0;
    this.adjustContentSize();
    this.snapContentOffsetToBounds(false);
    var b = this._contentOffset.copy();
    this.trackingDataPoints = [];
    this.addTrackingDataPoint(a.timeStamp, b);
    this.startContentOffset = b;
    this.startTouchPosition = ADPoint.fromEventInElement(a, this.layer);
    this.tracking = true;
    this.dragging = false;
    this.touchesHaveMoved = false;
    window.addEventListener(ADMoveEvent, this, true);
    window.addEventListener(ADEndEvent, this, true);
    window.addEventListener("touchcancel", this, true);
    window.addEventListener(ADEndEvent, this, false)
};
ADScrollView.prototype.touchesMoved = function(d) {
    this.touchesHaveMoved = true;
    this.callSuper(d);
    var e = ADPoint.fromEventInElement(d, this.layer);
    var b = e.x - this.startTouchPosition.x;
    var c = e.y - this.startTouchPosition.y;
    if (!this.dragging) {
        if ((Math.abs(b) >= ADScrollViewMinimumTrackingForDrag && this.horizontalScrollEnabled) || (Math.abs(c) >= ADScrollViewMinimumTrackingForDrag && this.verticalScrollEnabled)) {
            if (ADUtils.objectHasMethod(this.delegate, ADScrollViewWillBeginDragging)) {
                this.delegate[ADScrollViewWillBeginDragging](this)
            }
            this.dragging = true;
            this.firstDrag = true;
            if (this.horizontalScrollEnabled && this.showsHorizontalScrollIndicator && (this.adjustedContentSize.width > this._size.width)) {
                this.horizontalScrollIndicator.visible = true
            }
            if (this.verticalScrollEnabled && this.showsVerticalScrollIndicator && (this.adjustedContentSize.height > this._size.height)) {
                this.verticalScrollIndicator.visible = true
            }
        }
    }
    if (this.dragging) {
        d.stopPropagation();
        var f = this.horizontalScrollEnabled ? (this.startContentOffset.x - b) : this._contentOffset.x;
        var a = this.verticalScrollEnabled ? (this.startContentOffset.y - c) : this._contentOffset.y;
        if (this.bounces) {
            f -= ((f > this.maxPoint.x) ? (f - this.maxPoint.x) : ((f < 0) ? f: 0)) / 2;
            a -= ((a > this.maxPoint.y) ? (a - this.maxPoint.y) : ((a < 0) ? a: 0)) / 2
        } else {
            f = Math.max(Math.min(this.maxPoint.x, f), 0);
            a = Math.max(Math.min(this.maxPoint.y, a), 0)
        }
        if (this.firstDrag) {
            this.firstDrag = false;
            this.startTouchPosition = e;
            return
        }
        this.contentOffset = new ADPoint(f, a)
    }
    this.addTrackingDataPoint(d.timeStamp, this._contentOffset.copy())
};
ADScrollView.prototype.touchesEnded = function(a) {
    this.callSuper(a);
    this.tracking = false;
    if (this.dragging) {
        this.dragging = false;
        a.stopPropagation();
        this.purgeTrackingDataPointsWithTime(a.timeStamp);
        if (this.trackingDataPoints.length > 1) {
            this._contentOffsetBeforeDeceleration = this._contentOffset.copy();
            this.startDecelerationAnimation()
        }
        window.removeEventListener(ADEndEvent, this, false);
        if (ADUtils.objectHasMethod(this.delegate, ADScrollViewDidEndDragging)) {
            this.delegate[ADScrollViewDidEndDragging](this)
        }
    }
    if (!this.decelerating) {
        this.snapContentOffsetToBounds(true);
        this.hideScrollIndicators()
    }
    if (a.eventPhase == Event.BUBBLING_PHASE) {
        window.removeEventListener(ADEndEvent, this, false);
        if (!this.touchesHaveMoved && this.originalTarget !== null && a.type == ADEndEvent) {
            this.activateOriginalTarget()
        }
    }
};
ADScrollView.prototype.touchesCancelled = function(a) {
    this.callSuper(a);
    this.touchesEnded(a)
};
ADScrollView.prototype.stopTrackingTouches = function() {
    if (!this.tracking) {
        return
    }
    this.tracking = false;
    if (this.dragging) {
        this.dragging = false;
        this.snapContentOffsetToBounds(true);
        if (ADUtils.objectHasMethod(this.delegate, ADScrollViewDidEndDragging)) {
            this.delegate[ADScrollViewDidEndDragging](this)
        }
        this.hideScrollIndicators()
    }
    window.removeEventListener(ADMoveEvent, this, true);
    window.removeEventListener(ADEndEvent, this, true);
    window.removeEventListener(ADEndEvent, this, false);
    window.removeEventListener("touchcancel", this, true)
};
ADScrollView.prototype.purgeTrackingDataPointsWithTime = function(a) {
    while (this.trackingDataPoints.length > 0) {
        if (a - this.trackingDataPoints[0].time <= ADScrollViewMaxTimeForTrackingDataPoints) {
            break
        }
        this.trackingDataPoints.shift()
    }
};
ADScrollView.prototype.addTrackingDataPoint = function(b, a) {
    this.purgeTrackingDataPointsWithTime(b);
    this.trackingDataPoints.push({
        time: b,
        contentOffset: a
    })
};
ADScrollView.prototype.transitionEnded = function(a) {
    if (this.scrollIndicatorsNeedFlashing && a.currentTarget === this.layer) {
        this.scrollIndicatorsNeedFlashing = false;
        this.flashScrollIndicators()
    }
    if (this.scrollTransitionsNeedRemoval && a.currentTarget === this.hostingLayer) {
        this.scrollTransitionsNeedRemoval = false;
        this.hostingLayer.style.webkitTransitionDuration = 0;
        this.didScroll(true)
    }
};
ADScrollView.prototype.didScroll = function(a) {
    if (a && ADUtils.objectHasMethod(this.delegate, ADScrollViewDidEndScrollingAnimation)) {
        this.delegate[ADScrollViewDidEndScrollingAnimation](this)
    }
    if (ADUtils.objectHasMethod(this.delegate, ADScrollViewDidScroll)) {
        this.delegate[ADScrollViewDidScroll](this)
    }
};
ADScrollView.prototype.startDecelerationAnimation = function() {
    if (this.bounces && (this._contentOffset.x > this.maxPoint.x || this._contentOffset.y > this.maxPoint.y || this._contentOffset.x < 0 || this._contentOffset.y < 0)) {
        return
    }
    var d = this.trackingDataPoints[0];
    var b = this.trackingDataPoints[this.trackingDataPoints.length - 1];
    var a = new ADPoint(b.contentOffset.x - d.contentOffset.x, b.contentOffset.y - d.contentOffset.y);
    var c = (b.time - d.time) / ADScrollViewAcceleration;
    this.decelerationVelocity = new ADPoint(a.x / c, a.y / c);
    this.minDecelerationPoint = new ADPoint(0, 0);
    this.maxDecelerationPoint = this.maxPoint.copy();
    if (this.pagingEnabled) {
        this.minDecelerationPoint.x = Math.max(0, Math.floor(this._contentOffsetBeforeDeceleration.x / this._size.width) * this._size.width);
        this.minDecelerationPoint.y = Math.max(0, Math.floor(this._contentOffsetBeforeDeceleration.y / this._size.height) * this._size.height);
        this.maxDecelerationPoint.x = Math.min(this.maxPoint.x, Math.ceil(this._contentOffsetBeforeDeceleration.x / this._size.width) * this._size.width);
        this.maxDecelerationPoint.y = Math.min(this.maxPoint.y, Math.ceil(this._contentOffsetBeforeDeceleration.y / this._size.height) * this._size.height)
    }
    this.penetrationDeceleration = ADScrollViewPenetrationDeceleration;
    this.penetrationAcceleration = ADScrollViewPenetrationAcceleration;
    if (this.pagingEnabled) {
        this.penetrationDeceleration *= 5
    }
    var e = this.pagingEnabled ? ADScrollViewMinVelocityForDecelerationWithPaging: ADScrollViewMinVelocityForDeceleration;
    if (Math.abs(this.decelerationVelocity.x) > e || Math.abs(this.decelerationVelocity.y) > e) {
        this.decelerating = true;
        this.decelerationTimer = this.callMethodNameAfterDelay("stepThroughDecelerationAnimation", ADScrollViewDesiredAnimationFrameRate);
        this.lastFrame = new Date();
        if (ADUtils.objectHasMethod(this.delegate, ADScrollViewWillBeginDecelerating)) {
            this.delegate[ADScrollViewWillBeginDecelerating](this)
        }
    }
};
ADScrollView.prototype.stopDecelerationAnimation = function() {
    this.decelerating = false;
    clearTimeout(this.decelerationTimer)
};
ADScrollView.prototype.stepThroughDecelerationAnimation = function(f) {
    if (!this.decelerating) {
        return
    }
    var d = new Date();
    var k = d - this.lastFrame;
    var l = f ? 0: (Math.round(k / ADScrollViewDesiredAnimationFrameRate) - 1);
    for (var j = 0; j < l; j++) {
        this.stepThroughDecelerationAnimation(true)
    }
    var g = this._contentOffset.x + this.decelerationVelocity.x;
    var h = this._contentOffset.y + this.decelerationVelocity.y;
    if (!this.bounces) {
        var a = Math.max(Math.min(this.maxPoint.x, g), 0);
        if (a != g) {
            g = a;
            this.decelerationVelocity.x = 0
        }
        var c = Math.max(Math.min(this.maxPoint.y, h), 0);
        if (c != h) {
            h = c;
            this.decelerationVelocity.y = 0
        }
    }
    if (f) {
        this._contentOffset.x = g;
        this._contentOffset.y = h
    } else {
        this.contentOffset = new ADPoint(g, h)
    }
    if (!this.pagingEnabled) {
        this.decelerationVelocity.x *= ADScrollViewDecelerationFrictionFactor;
        this.decelerationVelocity.y *= ADScrollViewDecelerationFrictionFactor
    }
    var b = Math.abs(this.decelerationVelocity.x);
    var i = Math.abs(this.decelerationVelocity.y);
    if (!f && b <= ADScrollViewMinimumVelocity && i <= ADScrollViewMinimumVelocity) {
        this.hideScrollIndicators();
        this.decelerationAnimationCompleted();
        return
    }
    if (!f) {
        this.decelerationTimer = this.callMethodNameAfterDelay("stepThroughDecelerationAnimation", ADScrollViewDesiredAnimationFrameRate)
    }
    if (this.bounces) {
        var e = new ADPoint(0, 0);
        if (g < this.minDecelerationPoint.x) {
            e.x = this.minDecelerationPoint.x - g
        } else {
            if (g > this.maxDecelerationPoint.x) {
                e.x = this.maxDecelerationPoint.x - g
            }
        }
        if (h < this.minDecelerationPoint.y) {
            e.y = this.minDecelerationPoint.y - h
        } else {
            if (h > this.maxDecelerationPoint.y) {
                e.y = this.maxDecelerationPoint.y - h
            }
        }
        if (e.x != 0) {
            if (this.pagingEnabled && Math.abs(this.decelerationVelocity.x) >= ADScrollViewMaxVelocityForBouncingWithPaging) {
                this.decelerationAnimationCompleted();
                return
            }
            if (e.x * this.decelerationVelocity.x <= 0) {
                this.decelerationVelocity.x += e.x * this.penetrationDeceleration
            } else {
                this.decelerationVelocity.x = e.x * this.penetrationAcceleration
            }
        }
        if (e.y != 0) {
            if (this.pagingEnabled && Math.abs(this.decelerationVelocity.y) >= ADScrollViewMaxVelocityForBouncingWithPaging) {
                this.decelerationAnimationCompleted();
                return
            }
            if (e.y * this.decelerationVelocity.y <= 0) {
                this.decelerationVelocity.y += e.y * this.penetrationDeceleration
            } else {
                this.decelerationVelocity.y = e.y * this.penetrationAcceleration
            }
        }
    }
    if (!f) {
        this.lastFrame = d
    }
};
ADScrollView.prototype.decelerationAnimationCompleted = function() {
    this.stopDecelerationAnimation();
    if (this.pagingEnabled) {
        this.setContentOffsetWithAnimation(new ADPoint(Math.round(this._contentOffset.x / this._size.width) * this._size.width, Math.round(this._contentOffset.y / this._size.height) * this._size.height), false)
    }
    if (ADUtils.objectHasMethod(this.delegate, ADScrollViewDidEndDecelerating)) {
        this.delegate[ADScrollViewDidEndDecelerating](this)
    }
};
ADScrollView.prototype.activateOriginalTarget = function() {
    var b = this.originalTarget;
    while (b.parentNode && b !== this.hostingLayer) {
        if (b.nodeType == Node.ELEMENT_NODE) {
            if (ADScrollViewClickableElementNames.indexOf(b.localName) != -1) {
                break
            }
        }
        b = b.parentNode
    }
    if (!ADSupportsTouches) {
        return
    }
    var a = document.createEvent("MouseEvent");
    a.initMouseEvent("click", true, true, document.defaultView, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
    a._manufactured = true;
    b.dispatchEvent(a)
};
ADClass(ADScrollView);
const ADTableViewCellForRowAtPath = "tableViewCellForRowAtPath";
const ADTableViewNumberOfSectionsInTableView = "numberOfSectionsInTableView";
const ADTableViewNumberOfRowsInSection = "tableViewNumberOfRowsInSection";
const ADTableViewTitleForHeaderInSection = "tableViewTitleForHeaderInSection";
const ADTableViewTitleForFooterInSection = "tableViewTitleForFooterInSection";
const ADTableViewCustomCellForRowAtPath = "tableViewCustomCellForRowAtPath";
const ADTableViewDidSelectRowAtPath = "tableViewDidSelectRowAtPath";
const ADTableViewDidSelectAccessoryForRowAtPath = "tableViewDidSelectAccessoryForRowAtPath";
const ADTableViewCustomCellCSS = "ad-custom-table-view-cell";
const ADTableViewStylePlain = "plain";
const ADTableViewStyleCustom = "custom";
const ADTableViewStyleGrouped = "grouped";
const ADTableViewMinTouchDurationForCellSelection = 150;
ADTableView.inherits = ADScrollView;
ADTableView.synthetizes = ["style", "separatorStyle"];
ADTableView.includes = [ADPropertyTriage];
function ADTableView() {
    this.callSuper();
    this._style = ADTableViewStyleCustom;
    this._separatorStyle = ADTableViewCellSeparatorStyleSingleLine;
    this.horizontalScrollEnabled = false;
    this.delegate = null;
    this.dataSource = null;
    this.touchedCell = null;
    this.touchedAccessory = null;
    this.shouldPreventScrolling = false;
    this.numberOfSections = 1;
    this.numberOfRows = [];
    this.sections = [];
    this.headers = [];
    this.sectionMetrics = [];
    this.selectedCell = null;
    this.populated = false
}
ADTableView.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-table-view")
};
ADTableView.prototype.layerWasInsertedIntoDocument = function() {
    this.callSuper();
    this.notifyCellsOfInsertionIntoDocument();
    this.updateSectionMetrics()
};
ADTableView.prototype.setContentOffsetWithAnimation = function(b, a) {
    this.callSuper(b, a);
    this.updateSectionHeaders()
};
ADTableView.prototype.setStyle = function(a) {
    this.layer.removeClassName(this._style);
    this.layer.addClassName(a);
    this._style = a
};
ADTableView.prototype.setSeparatorStyle = function(a) {
    this.layer.removeClassName(this._separatorStyle);
    this.layer.addClassName(a);
    this._separatorStyle = a
};
ADTableView.prototype.numberOfRowsInSection = function(a) {
    if (a > this.numberOfSections - 1) {
        return
    }
    return this.numberOfRows[a]
};
ADTableView.prototype.cellForRowAtPath = function(a) {
    if (this._style === ADTableViewStyleCustom || a.section > this.numberOfSections - 1 || a.row > this.numberOfRows[a.section]) {
        return null
    }
    return this.hostingLayer.querySelector(".section:nth-of-type(" + (a.section + 1) + ") .cells > div:nth-of-type(" + (a.row + 1) + ")")._view
};
ADTableView.prototype.customCellForRowAtPath = function(a) {
    if (this._style !== ADTableViewStyleCustom || a.section > this.numberOfSections - 1 || a.row > this.numberOfRows[a.section]) {
        return null
    }
    return this.hostingLayer.querySelector(".section:nth-of-type(" + (a.section + 1) + ") .cells > ." + ADTableViewCustomCellCSS + ":nth-of-type(" + (a.row + 1) + ")")
};
ADTableView.prototype.pathForCell = function(a) {
    if (this._style === ADTableViewStyleCustom) {
        return null
    }
    return a._tableViewDataSourcePath
};
ADTableView.prototype.pathForCustomCell = function(a) {
    if (this._style !== ADTableViewStyleCustom) {
        return null
    }
    return a._tableViewDataSourcePath
};
ADTableView.prototype.notifyCellsOfInsertionIntoDocument = function() {
    if (!this.layerIsInDocument || !this.populated) {
        return
    }
    var a = this.hostingLayer.querySelectorAll(".ad-table-view-cell");
    for (var b = 0; b < a.length; b++) {
        a[b]._view.layerWasInsertedIntoDocument()
    }
};
ADTableView.prototype.reloadData = function() {
    var c = (this._style === ADTableViewStyleCustom);
    if (c) {
        if (!ADUtils.objectHasMethod(this.dataSource, ADTableViewCustomCellForRowAtPath) || !ADUtils.objectHasMethod(this.dataSource, ADTableViewNumberOfRowsInSection)) {
            console.error("An ADTableView's dataSource must implement all required methods");
            return
        }
    } else {
        if (!ADUtils.objectHasMethod(this.dataSource, ADTableViewCellForRowAtPath) || !ADUtils.objectHasMethod(this.dataSource, ADTableViewNumberOfRowsInSection)) {
            console.error("An ADTableView's dataSource must implement all required methods");
            return
        }
    }
    this._hostingLayer.innerText = "";
    this.sections = [];
    this.headers = [];
    if (ADUtils.objectHasMethod(this.dataSource, ADTableViewNumberOfSectionsInTableView)) {
        this.numberOfSections = this.dataSource[ADTableViewNumberOfSectionsInTableView](this);
        if (this.numberOfSections < 1) {
            console.error("An ADTableView must have at least one section");
            return
        }
    }
    for (var i = 0; i < this.numberOfSections; i++) {
        var d = document.createElement("div");
        d.className = "section";
        this.sections[i] = d;
        if (ADUtils.objectHasMethod(this.dataSource, ADTableViewTitleForHeaderInSection)) {
            var b = this.dataSource[ADTableViewTitleForHeaderInSection](this, i);
            if (b !== null) {
                var a = d.appendChild(document.createElement("h1"));
                a.innerText = b;
                this.headers[i] = a
            }
        }
        var g = this.dataSource[ADTableViewNumberOfRowsInSection](this, i);
        if (g > 0) {
            var k = d.appendChild(document.createElement("div"));
            k.className = "cells";
            for (var e = 0; e < g; e++) {
                var f = new ADCellPath(i, e);
                if (c) {
                    var h = this.dataSource[ADTableViewCustomCellForRowAtPath](this, f);
                    h.addClassName(ADTableViewCustomCellCSS);
                    h._tableViewDataSourcePath = f;
                    k.appendChild(h)
                } else {
                    var h = this.dataSource[ADTableViewCellForRowAtPath](this, f);
                    h._tableViewDataSourcePath = f;
                    h._table = this;
                    k.appendChild(h.layer)
                }
            }
        }
        if (ADUtils.objectHasMethod(this.dataSource, ADTableViewTitleForFooterInSection)) {
            var j = this.dataSource[ADTableViewTitleForFooterInSection](this, i);
            if (j !== null) {
                d.appendChild(document.createElement("span")).innerText = j
            }
        }
        this.numberOfRows[i] = g;
        this._hostingLayer.appendChild(d)
    }
    this.populated = true;
    this.updateSectionMetrics();
    this.notifyCellsOfInsertionIntoDocument()
};
ADTableView.prototype.updateSectionMetrics = function() {
    if (!this.layerIsInDocument || this._style !== ADTableViewStylePlain || !this.populated) {
        return
    }
    this.sectionMetrics = [];
    for (var a = 0; a < this.sections.length; a++) {
        this.sectionMetrics[a] = {
            y: this.sections[a].offsetTop,
            height: this.sections[a].offsetHeight
        }
    }
};
ADTableView.prototype.touchesBegan = function(e) {
    if (e._manufactured) {
        return
    }
    this.wasDeceleratingWhenTouchesBegan = this.decelerating;
    this.callSuper(e);
    this.touchedCell = null;
    this.touchedAccessory = null;
    this.shouldPreventScrolling = false;
    if (this.wasDeceleratingWhenTouchesBegan || !this.tracking) {
        return
    }
    var c = ADPoint.fromEvent(e);
    var d = document.elementFromPoint(c.x, c.y);
    if (this._style === ADTableViewStyleCustom) {
        var a = d;
        while (a.parentNode) {
            if (a.hasClassName(ADTableViewCustomCellCSS)) {
                this.touchedCell = a;
                this.touchedCellWasSelected = this.touchedCell.hasClassName(ADControlStateSelectedCSS);
                break
            }
            a = a.parentNode
        }
    } else {
        var b = (d._view !== undefined) ? d._view: d.getNearestView();
        if (b instanceof ADTableViewCell) {
            this.touchedCell = b;
            this.touchedCellWasSelected = this.touchedCell.selected
        } else {
            if (b instanceof ADButton) {
                this.touchedAccessory = b
            }
        }
    }
    if (this.touchedCell !== null || this.touchedAccessory !== null) {
        this.callMethodNameAfterDelay("detectedStationaryTouch", ADTableViewMinTouchDurationForCellSelection)
    }
};
ADTableView.prototype.touchesMoved = function(b) {
    if (this.shouldPreventScrolling) {
        return
    }
    var a = this.dragging;
    this.callSuper(b);
    if (this.wasDeceleratingWhenTouchesBegan) {
        return
    }
    if (a != this.dragging && this.touchedCell !== null && !this.touchedCellWasSelected) {
        if (this.touchedCell instanceof ADTableViewCell) {
            this.touchedCell.selected = false
        } else {
            this.touchedCell.removeClassName(ADControlStateSelectedCSS)
        }
    }
};
ADTableView.prototype.touchesEnded = function(b) {
    var a = this.dragging;
    this.callSuper(b);
    if (this.wasDeceleratingWhenTouchesBegan) {
        return
    }
    if (b.type != ADEndEvent) {
        return
    }
    if (b.eventPhase == Event.BUBBLING_PHASE && !a) {
        if (this.touchedAccessory !== null && !this.shouldPreventScrolling) {
            this.disclosureButtonWasSelectedAtPath(this.touchedAccessory.superview._tableViewDataSourcePath)
        } else {
            if (this.touchedCell !== null) {
                this.selectRowAtPath(this.touchedCell._tableViewDataSourcePath)
            }
        }
    }
};
ADTableView.prototype.pathForSelectedRow = function() {
    if (this.selectedCell === null) {
        return null
    }
    return (this._style === ADTableViewStyleCustom) ? this.pathForCustomCell(this.selectedCell) : this.pathForCell(this.selectedCell)
};
ADTableView.prototype.deselectRowAtPathAnimated = function(b, c) {
    if (b === null) {
        return
    }
    var a = (this._style === ADTableViewStyleCustom) ? this.customCellForRowAtPath(b) : this.cellForRowAtPath(b);
    if (a !== null) {
        this.markCellAsSelectedAnimated(a, false, c)
    }
};
ADTableView.prototype.selectRowAtPath = function(b) {
    var a = (this._style === ADTableViewStyleCustom) ? this.customCellForRowAtPath(b) : this.cellForRowAtPath(b);
    if (a === null) {
        throw (new Error("No cell at " + b.toString()));
        return
    }
    this.deselectRowAtPathAnimated(this.pathForSelectedRow(), false);
    this.selectedCell = a;
    this.markCellAsSelectedAnimated(this.selectedCell, true, false);
    if (ADUtils.objectHasMethod(this.delegate, ADTableViewDidSelectRowAtPath)) {
        this.delegate[ADTableViewDidSelectRowAtPath](this, b)
    }
};
ADTableView.prototype.detectedStationaryTouch = function() {
    if (!this.dragging && this.tracking) {
        if (this.touchedCell !== null) {
            this.markCellAsSelectedAnimated(this.touchedCell, true, false)
        } else {
            if (this.touchedAccessory !== null) {
                this.shouldPreventScrolling = true
            }
        }
    }
};
ADTableView.prototype.markCellAsSelectedAnimated = function(a, c, b) {
    if (a instanceof ADTableViewCell) {
        a.setSelectedAnimated(c, b)
    } else {
        a[c ? "addClassName": "removeClassName"](ADControlStateSelectedCSS)
    }
};
ADTableView.prototype.disclosureButtonWasSelectedAtPath = function(b) {
    var a = this.cellForRowAtPath(b);
    if (a.accessoryType === ADTableViewCellAccessoryDetailDisclosureButton && ADUtils.objectHasMethod(this.delegate, ADTableViewDidSelectAccessoryForRowAtPath)) {
        this.delegate[ADTableViewDidSelectAccessoryForRowAtPath](this, b)
    }
};
const ADTableViewPlainHeaderHeight = 23;
ADTableView.prototype.updateSectionHeaders = function() {
    if (this.sectionMetrics.length != this.numberOfSections || this.style !== ADTableViewStylePlain) {
        return
    }
    var h = this.contentOffset.y;
    for (var c = 0; c < this.numberOfSections; c++) {
        var b = this.headers[c];
        if (b === undefined) {
            continue
        }
        var d = this.sectionMetrics[c];
        var e = d.y;
        var f = e + d.height;
        var g = f - h;
        var a = 0;
        if (g > 0 && g < (ADTableViewPlainHeaderHeight - 1)) {
            a = d.height - ADTableViewPlainHeaderHeight
        } else {
            if (e <= h && f > h) {
                a = Math.abs(e - h) - 1
            }
        }
        b.style.webkitTransform = ADUtils.t(0, a)
    }
};
ADClass(ADTableView);
ADTableView.init = function() {
    ADUtils.preloadImageAsset("tableview/UITableSelection.png")
};
window.addEventListener("load", ADTableView.init, false);
function ADCellPath(a, b) {
    this.section = a || 0;
    this.row = b || 0
}
ADCellPath.prototype.toString = function() {
    return "ADCellPath with section " + this.section + " and row " + this.row
};
const ADTableViewCellAccessoryNone = "no-accessory";
const ADTableViewCellAccessoryDisclosureIndicator = "disclosure-accessory";
const ADTableViewCellAccessoryDetailDisclosureButton = "detail-accessory";
const ADTableViewCellSelectionStyleNone = "no-selection";
const ADTableViewCellSelectionStyleBlue = "blue-selection";
const ADTableViewCellSelectionStyleGray = "gray-selection";
const ADTableViewCellStyleDefault = "style-default";
const ADTableViewCellStyleValue1 = "style-value-1";
const ADTableViewCellStyleValue2 = "style-value-2";
const ADTableViewCellStyleSubtitle = "style-subtitle";
const ADTableViewCellSeparatorStyleNone = "separator-none";
const ADTableViewCellSeparatorStyleSingleLine = "separator-single-line";
const ADTableViewCellSeparatorStyleSingleLineEtched = "separator-single-line-etched";
ADTableViewCell.inherits = ADView;
ADTableViewCell.synthetizes = ["text", "detailedText", "selectionStyle", "accessoryType", "selected"];
function ADTableViewCell(a) {
    this.style = a || ADTableViewCellStyleDefault;
    this.callSuper();
    this._selectionStyle = ADTableViewCellSelectionStyleBlue;
    this._accessoryType = ADTableViewCellAccessoryNone;
    this._selected = false;
    this.layer.removeEventListener(ADStartEvent, this, false)
}
ADTableViewCell.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-table-view-cell " + this.style);
    this.layer.setAttribute("role", "button");
    this.accessory = new ADButton(ADButtonTypeDetailDisclosure);
    this.accessory.addEventListener(ADControlTouchUpInsideEvent, this, false);
    this.addSubview(this.accessory);
    this.textLabel = this.layer.appendChild(document.createElement("span"));
    this.textLabel.addClassName("text-label");
    this.detailedTextLabel = this.layer.appendChild(document.createElement("span"));
    this.detailedTextLabel.addClassName("detailed-text-label")
};
ADTableViewCell.prototype.handleEvent = function(c) {
    if (c.currentTarget === this.accessory.layer) {
        var a = this._table;
        var b = this._tableViewDataSourcePath;
        if (a !== undefined && b !== undefined) {
            a.disclosureButtonWasSelectedAtPath(b)
        }
    } else {
        this.callSuper(c)
    }
};
ADTableViewCell.prototype.getText = function() {
    return this.textLabel.innerText
};
ADTableViewCell.prototype.setText = function(a) {
    this.textLabel.innerText = a;
    this.updateTextLayout()
};
ADTableViewCell.prototype.getDetailedText = function() {
    return this.detailedTextLabel.innerText
};
ADTableViewCell.prototype.setDetailedText = function(a) {
    this.detailedTextLabel.innerText = a;
    this.updateTextLayout()
};
ADTableViewCell.prototype.setSelectionStyle = function(a) {
    this.layer.removeClassName(this._selectionStyle);
    this.layer.addClassName(a);
    this._selectionStyle = a
};
const ADTableViewCellAccessoryDisclosureIndicatorWidth = 10;
const ADTableViewCellAccessoryDetailDisclosureButtonWidth = 44;
ADTableViewCell.prototype.setAccessoryType = function(a) {
    this.layer.removeClassName(this._accessoryType);
    this.layer.addClassName(a);
    this._accessoryType = a
};
ADTableViewCell.prototype.setSelected = function(a) {
    this.setSelectedAnimated(a, false)
};
ADTableViewCell.prototype.setSelectedAnimated = function(a, b) {
    if (this._selected == a) {
        return
    }
    this._selected = a;
    this.layer[a ? "addClassName": "removeClassName"](ADControlStateSelectedCSS)
};
ADTableViewCell.prototype.layerWasInsertedIntoDocument = function() {
    this.callSuper();
    this.updateTextLayout()
};
const ADTableViewCellStyleValue1Margin = 10;
ADTableViewCell.prototype.updateTextLayout = function() {
    if (this.style != ADTableViewCellStyleValue1 || !this.layerIsInDocument) {
        return
    }
    var c = this.textLabel.offsetWidth - 2 * ADTableViewCellStyleValue1Margin;
    this.textLabel.style.right = "auto !important";
    this.detailedTextLabel.style.right = "auto !important";
    var d = Math.min(this.textLabel.offsetWidth, c);
    var a = Math.min(this.detailedTextLabel.offsetWidth, c);
    this.textLabel.setAttribute("style", "");
    this.detailedTextLabel.setAttribute("style", "");
    if (d + a > c) {
        var b = Math.floor((d / (d + a)) * c);
        if (d > a) {
            this.textLabel.style.width = ADUtils.px(b);
            this.detailedTextLabel.style.left = ADUtils.px(b + ADTableViewCellStyleValue1Margin * 2)
        } else {
            this.textLabel.style.width = ADUtils.px(b + ADTableViewCellStyleValue1Margin);
            this.detailedTextLabel.style.left = ADUtils.px(b + ADTableViewCellStyleValue1Margin * 3)
        }
    }
};
ADClass(ADTableViewCell);
const ADNavigationBarStyleDefault = "default";
const ADNavigationBarStyleBlack = "black";
const ADNavigationBarStyleBlackTranslucent = "black-translucent";
const ADNavigationBarButtonMarginLeft = 5;
const ADNavigationBarButtonMarginRight = 8;
const ADNavigationBarHeight = 44;
const ADNavigationBarAnimationDuration = 0.35;
const ADNavigationBarShouldPushItem = "navigationBarShouldPushItem";
const ADNavigationBarDidPushItem = "navigationBarDidPushItem";
const ADNavigationBarShouldPopItem = "navigationBarShouldPopItem";
const ADNavigationBarDidPopItem = "navigationBarDidPopItem";
ADNavigationBar.inherits = ADView;
ADNavigationBar.synthetizes = ["barStyle", "items", "topItem", "backItem"];
function ADNavigationBar() {
    this.callSuper();
    this.delegate = null;
    this._barStyle = "";
    this._items = [];
    this.busy = false;
    this.itemsToSetupAfterAnimatedChange = null;
    this.barStyle = ADNavigationBarStyleDefault
}
ADNavigationBar.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-navigation-bar")
};
ADNavigationBar.prototype.setSize = function(a) {
    this.callSuper(new ADSize(a.width, ADNavigationBarHeight));
    this.updateTopItemLayout()
};
ADNavigationBar.prototype.willMoveToSuperview = function(a) {
    if (a !== null && this._size.width == 0) {
        this.size = new ADSize(a._size.width, ADNavigationBarHeight)
    }
};
ADNavigationBar.prototype.layerWasInsertedIntoDocument = function(a) {
    this.callSuper();
    this.updateTopItemLayout()
};
ADNavigationBar.prototype.setBarStyle = function(a) {
    this.layer.removeClassName(this._barStyle);
    this.layer.addClassName(a);
    this._barStyle = a
};
ADNavigationBar.prototype.getTopItem = function() {
    return (this._items.length > 0) ? this._items[this._items.length - 1] : null
};
ADNavigationBar.prototype.getBackItem = function() {
    return (this._items.length > 1) ? this._items[this._items.length - 2] : null
};
ADNavigationBar.prototype.handleEvent = function(a) {
    this.callSuper(a);
    if (this.busy && a.type == "webkitTransitionEnd") {
        this.transitionsEnded()
    }
};
ADNavigationBar.prototype.setItems = function(a) {
    this.setItemsAnimated(a, false)
};
ADNavigationBar.prototype.setItemsAnimated = function(g, c) {
    if (this.busy || g.length == 0) {
        return
    }
    ADTransaction.begin();
    var f = this.topItem;
    var b = (g.length > 1) ? g[g.length - 2] : null;
    var d = g[g.length - 1];
    for (var e = 0; e < this._items.length; e++) {
        this._items[e].navigationBar = null
    }
    for (var e = 0; e < g.length; e++) {
        g[e].navigationBar = this
    }
    if (f === null) {
        if (!this.shouldPushItem(d)) {
            ADTransaction.commit();
            return
        }
        this.addItemViews(d, null);
        d.sizeItemsAndComputePositionsWithBackItem(null);
        d.updateLayout();
        this._items = g;
        if (ADUtils.objectHasMethod(this.delegate, ADNavigationBarDidPushItem)) {
            this.delegate[ADNavigationBarDidPushItem](this, d)
        }
        ADTransaction.commit()
    } else {
        if (f === d) {
            this.removeViewsForItem(f);
            while (this.subviews.length) {
                this.subviews[0].removeFromSuperview()
            }
            this.addItemViews(d, b);
            d.sizeItemsAndComputePositionsWithBackItem(b);
            d.updateLayout();
            this._items = g;
            ADTransaction.commit()
        } else {
            var a = (this._items.indexOf(d) == -1);
            if ((a && !this.shouldPushItem(d)) || (!a && !this.shouldPopItem(f))) {
                ADTransaction.commit();
                return
            }
            this.addItemViews(d, b);
            d.sizeItemsAndComputePositionsWithBackItem(b);
            this.itemsAfterTransition = g;
            this.transitionToItem(d, f, a, c)
        }
    }
};
ADNavigationBar.prototype.pushNavigationItemAnimated = function(b, a) {
    if (this.busy) {
        return
    }
    ADTransaction.begin();
    if (!this.shouldPushItem(b)) {
        ADTransaction.commit();
        return
    }
    this.setItemsAnimated(this._items.concat([b]), a);
    ADTransaction.commit()
};
ADNavigationBar.prototype.popNavigationItemAnimated = function(a) {
    if (this.busy || this._items.length < 2) {
        return
    }
    ADTransaction.begin();
    if (!this.shouldPopItem(this.topItem)) {
        ADTransaction.commit();
        return
    }
    this.setItemsAnimated(this._items.slice(0, this._items.length - 1), a);
    ADTransaction.commit()
};
ADNavigationBar.prototype.addItemViews = function(b, d) {
    var e = b._leftBarButtonItem || ((d !== null) ? d.backBarButtonItem: null) || null;
    var f = [e, b.rightBarButtonItem, b.titleView];
    for (var c = 0; c < f.length; c++) {
        var b = f[c];
        if (ADUtils.objectIsUndefined(b) || b === null) {
            continue
        }
        var a = b.view;
        if (a.superview === null || a.superview !== this) {
            this.addSubview(a)
        }
    }
};
ADNavigationBar.prototype.transitionToItem = function(b, d, f, c) {
    this.busy = c;
    this.previousItem = d;
    this.transitionWentForward = f;
    ADTransaction.defaults.duration = c ? ADNavigationBarAnimationDuration: 0;
    ADTransaction.defaults.properties = ["opacity", "position"];
    if (d._leftBarButtonItem !== null) {
        if (d._leftBarButtonItem !== b._leftBarButtonItem) {
            new ADTransition({
                target: d._leftBarButtonItem.view,
                properties: ["opacity"],
                to: [0]
            }).start()
        }
    } else {
        if (d.leftButton !== null) {
            var a = (f) ? ( - d.leftButton.view.size.width - ADNavigationItemLeftButtonLeftMargin) : b.positions.title;
            new ADTransition({
                target: d.leftButton.view,
                to: [0, new ADPoint(a, 0)]
            }).start()
        }
    }
    var a = (f) ? ADNavigationItemLeftButtonLeftMargin: this.size.width;
    new ADTransition({
        target: d.titleView.view,
        to: [0, new ADPoint(a, 0)]
    }).start();
    if (d._rightBarButtonItem !== null && d._rightBarButtonItem !== b._rightBarButtonItem) {
        new ADTransition({
            target: d._rightBarButtonItem.view,
            properties: ["opacity"],
            to: [0]
        }).start()
    }
    if (b._leftBarButtonItem !== null) {
        if (b._leftBarButtonItem !== d._leftBarButtonItem) {
            b._leftBarButtonItem.view.position = new ADPoint(b.positions.leftButton, 0);
            new ADTransition({
                target: b._leftBarButtonItem.view,
                properties: ["opacity"],
                from: [0],
                to: [1]
            }).start()
        }
    } else {
        if (b.leftButton !== null) {
            var e = (f) ? d.positions.title: ( - d.leftButton.view.size.width - ADNavigationItemLeftButtonLeftMargin);
            new ADTransition({
                target: b.leftButton.view,
                from: [0, new ADPoint(e, 0)],
                to: [b.hidesBackButton ? 0: 1, new ADPoint(b.positions.leftButton, 0)]
            }).start()
        }
    }
    var e = (f) ? this.size.width: ADNavigationItemLeftButtonLeftMargin;
    new ADTransition({
        target: b.titleView.view,
        from: [0, new ADPoint(e, 0)],
        to: [1, new ADPoint(b.positions.title, 0)]
    }).start();
    if (b._rightBarButtonItem !== null && b._rightBarButtonItem !== d._rightBarButtonItem) {
        b._rightBarButtonItem.view.position = new ADPoint(b.positions.rightButton, 0);
        new ADTransition({
            target: b._rightBarButtonItem.view,
            properties: ["opacity"],
            from: [0],
            to: [1]
        }).start()
    }
    if (c) {
        d.titleView.view.layer.addEventListener("webkitTransitionEnd", this, false)
    }
    ADTransaction.commit();
    if (!c) {
        this.transitionsEnded()
    }
};
ADNavigationBar.prototype.transitionsEnded = function() {
    this._items = this.itemsAfterTransition;
    this.busy = false;
    if (this.transitionWentForward) {
        if (ADUtils.objectHasMethod(this.delegate, ADNavigationBarDidPushItem)) {
            this.delegate[ADNavigationBarDidPushItem](this, this.topItem)
        }
    } else {
        if (ADUtils.objectHasMethod(this.delegate, ADNavigationBarDidPopItem)) {
            this.delegate[ADNavigationBarDidPopItem](this, this.previousItem)
        }
    }
    this.removeViewsForItem(this.previousItem);
    if (this.superview instanceof ADNavigationView) {
        this.superview.viewController.transitionsEnded()
    }
};
ADNavigationBar.prototype.removeViewsForItem = function(a) {
    if (a.leftButton !== null && a.leftButton !== this.topItem.leftButton) {
        a.leftButton.view.removeFromSuperview()
    }
    a.titleView.view.removeFromSuperview();
    if (a._rightBarButtonItem !== null && a._rightBarButtonItem !== a._rightBarButtonItem) {
        a._rightBarButtonItem.view.removeFromSuperview()
    }
    if (a.leftButton !== null) {
        a.leftButton.removeEventListener(ADControlTouchUpInsideEvent, this, false)
    }
};
ADNavigationBar.prototype.updateTopItemLayout = function() {
    if (this._items.length > 0) {
        this.topItem.updateLayoutIfTopItem()
    }
};
ADNavigationBar.prototype.shouldPushItem = function(a) {
    if (ADUtils.objectHasMethod(this.delegate, ADNavigationBarShouldPushItem)) {
        return this.delegate[ADNavigationBarShouldPushItem](this, a)
    }
    return true
};
ADNavigationBar.prototype.shouldPopItem = function(a) {
    if (ADUtils.objectHasMethod(this.delegate, ADNavigationBarShouldPopItem)) {
        return this.delegate[ADNavigationBarShouldPopItem](this, a)
    }
    return true
};
ADClass(ADNavigationBar);
const ADNavigationItemLeftButtonLeftMargin = 5;
const ADNavigationItemLeftButtonRightMargin = 8;
const ADNavigationItemRightButtonLeftMargin = 11;
const ADNavigationItemRightButtonRightMargin = 5;
ADNavigationItem.inherits = ADObject;
ADNavigationItem.synthetizes = ["title", "backBarButtonItem", "leftBarButtonItem", "rightBarButtonItem", "hidesBackButton"];
ADNavigationItem.includes = [ADEventTriage];
function ADNavigationItem(a) {
    this.callSuper();
    this._title = "";
    this._backBarButtonItem = new ADBarButtonItem(ADBarButtonItemTypeBack);
    this._hidesBackButton = false;
    this._leftBarButtonItem = null;
    this._rightBarButtonItem = null;
    this.titleView = new ADBarButtonItem(ADBarButtonItemTypePlain);
    this.titleView.view.layer.setAttribute("role", "header");
    this.buttons = null;
    this.positions = null;
    this.navigationBar = null;
    this.viewController = null;
    this.title = a || "";
    this._backBarButtonItem.addEventListener(ADControlTouchUpInsideEvent, this, false)
}
ADNavigationItem.prototype.handleControlTouchUpInside = function(a) {
    if (this._hidesBackButton) {
        return
    }
    if (this.viewController !== null && this.viewController.parentViewController !== null) {
        this.viewController.parentViewController.popViewControllerAnimated(true)
    } else {
        if (this.navigationBar !== null) {
            this.navigationBar.popNavigationItemAnimated(true)
        }
    }
};
ADNavigationItem.prototype.setTitle = function(a) {
    this._title = a;
    this.updateLayoutIfTopItem()
};
ADNavigationItem.prototype.setBackBarButtonItem = function(a) {
    if (this.navigationBar !== null && this.navigationBar.backItem === this) {
        if (this._backBarButtonItem !== null) {
            this._backBarButtonItem.view.removeFromSuperview()
        }
        this._backBarButtonItem = a;
        this.navigationBar.addSubview(this._backBarButtonItem.view);
        this.navigationBar.topItem.updateLayoutIfTopItem()
    } else {
        this._backBarButtonItem = a
    }
    if (this._backBarButtonItem !== null) {
        this._backBarButtonItem.addEventListener(ADControlTouchUpInsideEvent, this, false)
    }
};
ADNavigationItem.prototype.setHidesBackButton = function(a) {
    this.setHidesBackButtonWithAnimation(a, false)
};
ADNavigationItem.prototype.setLeftBarButtonItem = function(a) {
    if (this.navigationBar !== null && this.navigationBar.topItem === this) {
        var b = this.getDefaultBackButton();
        if (this.leftButton !== null) {
            this.leftButton.view.removeFromSuperview()
        }
        this._leftBarButtonItem = a;
        var c = this._leftBarButtonItem || this.getDefaultBackButton();
        if (c !== null) {
            this.navigationBar.addSubview(c.view);
            if (this._leftBarButtonItem === null && !this.hidesBackButton) {
                c.view.opacity = 1
            }
        }
        this.updateLayoutIfTopItem()
    } else {
        this._leftBarButtonItem = a
    }
};
ADNavigationItem.prototype.setRightBarButtonItem = function(a) {
    if (this.navigationBar !== null && this.navigationBar.topItem === this) {
        if (this._rightBarButtonItem !== null) {
            this._rightBarButtonItem.view.removeFromSuperview()
        }
        this._rightBarButtonItem = a;
        this.navigationBar.addSubview(this._rightBarButtonItem.view);
        this.updateLayoutIfTopItem()
    } else {
        this._rightBarButtonItem = a
    }
};
ADNavigationItem.prototype.setHidesBackButtonWithAnimation = function(a, c) {
    var b = this.getDefaultBackButton();
    if (this._hidesBackButton == a) {
        return
    }
    this._hidesBackButton = a;
    if (b === null) {
        return
    }
    b.view.transitionsEnabled = c;
    b.view.opacity = a ? 0: 1
};
ADNavigationItem.prototype.setLeftBarButtonItemWithAnimation = function(d, a) {
    if (!a || this.navigationBar === null || this.navigationBar.topItem !== this) {
        this.leftBarButtonItem = d;
        return
    }
    ADTransaction.begin();
    ADTransaction.defaults.duration = 0.5;
    ADTransaction.defaults.properties = ["opacity"];
    var b = this.leftButton;
    if (b !== null) {
        new ADTransition({
            target: b.view,
            to: [0],
            removesTargetUponCompletion: true
        }).start()
    }
    this._leftBarButtonItem = d;
    var c = this._leftBarButtonItem || this.getDefaultBackButton();
    if (c !== null) {
        this.navigationBar.addSubview(c.view);
        if (this._leftBarButtonItem !== null || !this._hidesBackButton) {
            new ADTransition({
                target: c.view,
                from: [0],
                to: [1]
            }).start()
        }
    }
    this.updateLayoutIfTopItem();
    ADTransaction.commit()
};
ADNavigationItem.prototype.setRightBarButtonItemWithAnimation = function(c, a) {
    if (!a || this.navigationBar === null || this.navigationBar.topItem !== this) {
        this.rightBarButtonItem = c;
        return
    }
    ADTransaction.begin();
    ADTransaction.defaults.duration = 0.5;
    ADTransaction.defaults.properties = ["opacity"];
    var b = this._rightBarButtonItem;
    if (b !== null) {
        new ADTransition({
            target: b.view,
            to: [0],
            removesTargetUponCompletion: true
        }).start()
    }
    this._rightBarButtonItem = c;
    if (this._rightBarButtonItem !== null) {
        this.navigationBar.addSubview(this._rightBarButtonItem.view);
        new ADTransition({
            target: this._rightBarButtonItem.view,
            from: [0],
            to: [1]
        }).start()
    }
    this.updateLayoutIfTopItem();
    ADTransaction.commit()
};
ADNavigationItem.prototype.getDefaultBackButton = function() {
    return (this.navigationBar !== null && this.navigationBar.backItem !== null) ? this.navigationBar.backItem.backBarButtonItem: null
};
ADNavigationItem.prototype.sizeItemsAndComputePositionsWithBackItem = function(d) {
    if (this.navigationBar === null) {
        return
    }
    var i = this._leftBarButtonItem || ((d !== null) ? d.backBarButtonItem: null);
    var e = (this._rightBarButtonItem !== null) ? this._rightBarButtonItem.view.size.width: 0;
    var n = this.navigationBar.size.width - ADNavigationItemLeftButtonLeftMargin - ADNavigationItemRightButtonRightMargin;
    if (i !== null) {
        n -= ADNavigationItemLeftButtonRightMargin
    }
    if (this._rightBarButtonItem !== null) {
        n -= ADNavigationItemRightButtonLeftMargin + e
    }
    var f = 0;
    if (i !== null) {
        i.maxWidth = this.navigationBar.size.width / 2;
        if (i !== this._leftBarButtonItem && i.title == "" && i.image === null) {
            i.title = d.title
        }
        f = i.view.size.width
    }
    this.titleView.maxWidth = 0;
    this.titleView.title = this._title;
    var g = this.titleView.view.size.width;
    if (g + f > n) {
        if (i !== null) {
            i.maxWidth = Math.min(Math.max(n / 3, n - g), i.maxWidth);
            f = i.view.size.width
        }
        this.titleView.maxWidth = n - f;
        g = this.titleView.view.size.width
    }
    var b = ADNavigationItemLeftButtonLeftMargin;
    var k = this.navigationBar.size.width - ADNavigationItemRightButtonRightMargin - e;
    var j = b + ((i != null) ? f + ADNavigationItemLeftButtonRightMargin: 0);
    var a = k - g - ((e > 0) ? ADNavigationItemRightButtonLeftMargin: 0);
    var l = (this.navigationBar.size.width - g) / 2;
    var h = l;
    if (l > a || l < j) {
        var c = Math.abs(l - j);
        var m = Math.abs(l - a);
        h = (c < m) ? j: a
    }
    this.leftButton = i;
    this.positions = {
        leftButton: b,
        title: h,
        rightButton: k
    }
};
ADNavigationItem.prototype.updateLayout = function() {
    if (this.positions === null || this.button === null) {
        return
    }
    if (this.leftButton != null) {
        this.leftButton.view.position = new ADPoint(this.positions.leftButton, 0)
    }
    if (this._rightBarButtonItem != null) {
        this._rightBarButtonItem.view.position = new ADPoint(this.positions.rightButton, 0)
    }
    this.titleView.view.position = new ADPoint(this.positions.title, 0)
};
ADNavigationItem.prototype.updateLayoutIfTopItem = function(a) {
    if (this.navigationBar === null || this.navigationBar.topItem !== this) {
        return
    }
    this.sizeItemsAndComputePositionsWithBackItem(this.navigationBar.backItem);
    this.updateLayout()
};
ADClass(ADNavigationItem);
const ADToolbarHeight = 44;
const ADToolbarEdgeMargin = 6;
const ADToolbarItemMargin = 10;
const ADToolbarStyleDefault = "default";
const ADToolbarStyleBlack = "black";
const ADToolbarStyleBlackTranslucent = "black-translucent";
const ADToolbarAnimationDuration = 0.2;
const ADToolbarFadeOutTransition = {
    properties: ["transform", "opacity"],
    to: ["scale(0.01)", 0]
};
ADToolbar.inherits = ADView;
ADToolbar.synthetizes = ["items", "style"];
function ADToolbar() {
    this._items = [];
    this._style = "";
    this.callSuper();
    this.layer.addEventListener("webkitTransitionEnd", this, false);
    this.style = ADToolbarStyleDefault;
    this.clipsToBounds = true
}
ADToolbar.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-toolbar");
    this.glow = this.layer.appendChild(document.createElement("div"));
    this.glow.className = "glow"
};
ADToolbar.prototype.setPosition = function(a) {
    this.callSuper(a);
    if (this.layerIsInDocument) {
        this.callMethodNameAfterDelay("updateBackgroundWithPosition", 0)
    }
};
ADToolbar.prototype.setSize = function(a) {
    a.height = ADToolbarHeight;
    this.callSuper(a);
    this.updateLayout()
};
ADToolbar.prototype.willMoveToSuperview = function(a) {
    if (a !== null && this._size.width == 0) {
        this.size = new ADSize(a.size.width, ADToolbarHeight)
    }
};
ADToolbar.prototype.layerWasInsertedIntoDocument = function() {
    this.updateBackgroundWithPosition()
};
ADToolbar.prototype.setStyle = function(a) {
    this.layer.removeClassName(this._style);
    this.layer.addClassName(a);
    this._style = a
};
ADToolbar.prototype.setItems = function(a) {
    this.setItemsAnimated(a, false)
};
ADToolbar.prototype.setItemsAnimated = function(h, a) {
    ADTransaction.begin();
    ADTransaction.defaults.duration = a ? ADToolbarAnimationDuration: 0;
    for (var k = 0; k < this._items.length; k++) {
        var e = this._items[k];
        var g = e.view;
        if (h.indexOf(e) == -1) {
            if (!a) {
                g.removeFromSuperview()
            } else {
                g.needsRemoval = true;
                g.applyTransition(ADToolbarFadeOutTransition)
            }
        }
    }
    for (var k = 0; k < h.length; k++) {
        e = h[k];
        g = e.view;
        if (g.superview !== this) {
            e._newItem = true;
            this.addSubview(g);
            g.addPropertyObserver("size", this);
            if (e.type == ADBarButtonItemTypePlain) {
                g.addEventListener(ADControlTouchStateChangeEvent, this, false)
            }
        }
    }
    var l = 0;
    var d = 0;
    for (var k = 0; k < h.length; k++) {
        e = h[k];
        if (e.type == ADBarButtonItemTypeFlexibleSpace) {
            l++
        } else {
            d += e.view.size.width
        }
    }
    var c = this.size.width - d - ADToolbarItemMargin * (h.length - 1) - ADToolbarEdgeMargin * 2;
    var f = (l > 0) ? (c / l) : 0;
    var i = ADToolbarEdgeMargin;
    var b;
    for (var k = 0; k < h.length; k++) {
        b = ADToolbarItemMargin;
        e = h[k];
        g = e.view;
        if (e.type == ADBarButtonItemTypeFlexibleSpace) {
            i += f
        } else {
            if (e.type == ADBarButtonItemTypeFixedSpace) {
                i += g.size.width;
                b = 0
            } else {
                var j = new ADPoint(i, (this.size.height - g.size.height) / 2);
                if (e._newItem) {
                    g.position = j;
                    e._newItem = false;
                    if (a) {
                        g.applyTransition(ADViewTransitionDissolveIn)
                    }
                } else {
                    g.applyTransition({
                        properties: ["position"],
                        to: [j]
                    })
                }
                i += g.size.width
            }
        }
        i += b
    }
    ADTransaction.commit();
    this._items = h
};
ADToolbar.prototype.updateLayout = function() {
    if (this._items.length > 0) {
        this.setItemsAnimated(this._items, false)
    }
};
ADToolbar.prototype.updateBackgroundWithPosition = function() {
    var a = window.webkitConvertPointFromNodeToPage(this.layer, new WebKitPoint(0, 0));
    this.layer[a.y == 0 ? "addClassName": "removeClassName"]("top")
};
ADToolbar.prototype.handleEvent = function(a) {
    this.callSuper(a);
    if (a.type == "webkitTransitionEnd") {
        if (a.target === this.layer) {
            return
        } else {
            if (a.target !== this.glow) {
                this.removeItemViewIfNeeded(a.target._control)
            } else {
                if (this.glow.style.opacity == 0) {
                    this.glow.style.display = "none"
                }
            }
        }
    } else {
        if (a.type == ADControlTouchStateChangeEvent) {
            var b = a.control;
            this.glow.style.webkitTransform = ADUtils.t(b.position.x + b.size.width / 2 - 50, 0);
            this.glow.style.opacity = b.touchInside ? 1: 0;
            this.glow.style.display = "block"
        }
    }
};
ADToolbar.prototype.removeItemViewIfNeeded = function(a) {
    if (a.needsRemoval) {
        a.removeFromSuperview();
        a.needsRemoval = false;
        a.transitionsEnabled = false;
        a.transform = "scale(1)";
        a.opacity = 1
    }
};
ADToolbar.prototype.handlePropertyChange = function(b, a) {
    this.setItemsAnimated(this._items, false)
};
ADClass(ADToolbar);
ADToolbar.init = function() {
    ADUtils.preloadImageAsset("bar/UINavigationBarDefaultBackground.png");
    ADUtils.preloadImageAsset("bar/UINavigationBarBlackOpaqueBackground.png");
    ADUtils.preloadImageAsset("bar/UINavigationBarBlackTranslucentBackground.png");
    ADUtils.preloadImageAsset("bar/toolbar_glow.png")
};
window.addEventListener("load", ADToolbar.init, false);
const ADControlTouchDownEvent = "controlTouchDown";
const ADControlTouchDragInsideEvent = "controlTouchDragInside";
const ADControlTouchDragOutsideEvent = "controlTouchDragOutside";
const ADControlTouchDragEnterEvent = "controlTouchDragEnter";
const ADControlTouchDragExitEvent = "controlTouchDragExit";
const ADControlTouchUpInsideEvent = "controlTouchUpInside";
const ADControlTouchUpOutsideEvent = "controlTouchUpOutside";
const ADControlTouchCancelEvent = "controlTouchCancel";
const ADControlValueChangeEvent = "controlValueChange";
const ADControlTouchStateChangeEvent = "controlTouchStateChange";
const ADControlStateNormal = 0;
const ADControlStateNormalCSS = "normal";
const ADControlStateHighlighted = 1 << 0;
const ADControlStateHighlightedCSS = "highlighted";
const ADControlStateDisabled = 1 << 1;
const ADControlStateDisabledCSS = "disabled";
const ADControlStateSelected = 1 << 2;
const ADControlStateSelectedCSS = "selected";
ADControl.inherits = ADView;
ADControl.includes = [ADEventTarget];
ADControl.synthetizes = ["state", "enabled", "selected", "highlighted", "touchLayer"];
function ADControl() {
    this.tag = 0;
    this._enabled = true;
    this._selected = false;
    this._highlighted = false;
    this._touchLayer = null;
    this.callSuper();
    this.tracking = false;
    this.touchInside = false;
    this.layer._control = this;
    this.eventTarget = this.layer;
    this.tracksTouchesOnceTouchesBegan = true;
    this.layer.removeEventListener(ADStartEvent, this, false);
    this.touchLayer.addEventListener(ADStartEvent, this, false)
}
ADControl.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-control")
};
ADControl.prototype.getState = function() {
    return (ADControlStateNormal | (this._highlighted ? ADControlStateHighlighted: 0) | (this._enabled ? 0: ADControlStateDisabled) | (this._selected ? ADControlStateSelected: 0))
};
ADControl.prototype.setEnabled = function(a) {
    if (a == this._enabled) {
        return
    }
    this.layer[a ? "removeClassName": "addClassName"](ADControlStateDisabledCSS);
    this._enabled = a;
    this.tracksTouchesOnceTouchesBegan = a;
    this.notifyPropertyChange("state")
};
ADControl.prototype.setSelected = function(a) {
    if (a == this._selected) {
        return
    }
    this.layer[a ? "addClassName": "removeClassName"](ADControlStateSelectedCSS);
    this._selected = a;
    this.notifyPropertyChange("state")
};
ADControl.prototype.setHighlighted = function(a) {
    if (a == this._highlighted) {
        return
    }
    this.layer[a ? "addClassName": "removeClassName"](ADControlStateHighlightedCSS);
    this._highlighted = a;
    this.notifyPropertyChange("state")
};
ADControl.prototype.getTouchLayer = function() {
    return (this._touchLayer != null) ? this._touchLayer: this.layer
};
ADControl.prototype.touchesBegan = function(a) {
    this.callSuper(a);
    if (!this._enabled) {
        return
    }
    a.stopPropagation();
    a.preventDefault();
    this.touchInside = true;
    this.highlighted = true;
    this.dispatchEvent(this.createUIEvent(ADControlTouchDownEvent, a));
    this.dispatchEvent(this.createEvent(ADControlTouchStateChangeEvent));
    this.lastProcessedEvent = a
};
ADControl.prototype.touchesMoved = function(c) {
    this.callSuper(c);
    this.tracking = true;
    var a = this.pointInside(ADPoint.fromEventInElement(c, this.layer));
    var b = a ? ADControlTouchDragInsideEvent: ADControlTouchDragOutsideEvent;
    if (a != this.touchInside) {
        this.touchInside = a;
        this.highlighted = a;
        b = a ? ADControlTouchDragEnterEvent: ADControlTouchDragExitEvent;
        this.dispatchEvent(this.createEvent(ADControlTouchStateChangeEvent))
    }
    this.dispatchEvent(this.createUIEvent(b, c));
    this.lastProcessedEvent = c
};
ADControl.prototype.touchesEnded = function(b) {
    this.callSuper(b);
    this.tracking = false;
    this.highlighted = false;
    var a = this.touchInside ? ADControlTouchUpInsideEvent: ADControlTouchUpOutsideEvent;
    this.dispatchEvent(this.createUIEvent(a, this.lastProcessedEvent));
    this.touchInside = false;
    this.dispatchEvent(this.createEvent(ADControlTouchStateChangeEvent))
};
ADControl.prototype.touchesCancelled = function(a) {
    this.callSuper(a);
    this.dispatchEvent(this.createUIEvent(ADControlTouchCancelEvent, a))
};
ADControl.prototype.createEvent = function(a) {
    var b = document.createEvent("Event");
    b.initEvent(a, true, false);
    b.control = this;
    return b
};
ADControl.prototype.createUIEvent = function(c, b) {
    var a = ADUtils.createUIEvent(c, b);
    a.control = this;
    return a
};
ADClass(ADControl);
const ADBarItemDisabledCSS = "disabled";
ADBarItem.inherits = ADObject;
ADBarItem.synthetizes = ["enabled", "image", "imageOffset", "title"];
function ADBarItem() {
    this.callSuper();
    this._enabled = true;
    this._image = null;
    this._imageOffset = null;
    this._title = "";
    this.tag = 0;
    this.view = null
}
ADClass(ADBarItem);
const ADBarButtonHeight = 30;
const ADBarButtonPointyXOffset = 3;
ADBarButton.inherits = ADControl;
ADBarButton.synthetizes = ["maxWidth", "width", "type", "style", "image", "imageOffset", "title"];
function ADBarButton(a) {
    this.callSuper();
    this._maxWidth = 0;
    this._width = 0;
    this._type = "";
    this._style = "";
    this._image = null;
    this._imageOffset = null;
    this._title = "";
    this.type = (a != null) ? a: ADBarButtonItemTypeSquare;
    this.style = ADBarButtonItemStyleDefault;
    this.usesAutomaticImageOffset = true
}
ADBarButton.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-bar-button");
    this.layer.setAttribute("role", "button");
    this.background = this.layer.appendChild(document.createElement("div"));
    this.icon = this.layer.appendChild(document.createElement("img"))
};
ADBarButton.prototype.layerWasInsertedIntoDocument = function() {
    this.callSuper();
    if (this._title != "" && this._width == 0) {
        this.autoSizeTitle()
    }
};
ADBarButton.prototype.getSize = function() {
    var a = (this._maxWidth == 0) ? this._size.width: Math.min(this._maxWidth, this._size.width);
    return new ADSize(a, this._size.height)
};
ADBarButton.prototype.setSize = function(a) {
    a.height = ADBarButtonHeight;
    this.callSuper(a)
};
ADBarButton.prototype.touchesBegan = function(a) {
    if (this.type == ADBarButtonItemTypeFlexibleSpace || this.type == ADBarButtonItemTypeFixedSpace) {
        a.preventDefault();
        return
    }
    this.callSuper(a)
};
ADBarButton.prototype.setMaxWidth = function(a) {
    this.background.style.maxWidth = (a == 0) ? "inherit": ADUtils.px(a);
    this._maxWidth = a;
    if (this._width == 0) {
        this.autoSizeTitle()
    }
};
ADBarButton.prototype.setWidth = function(a) {
    if (a == 0) {
        this.autoSizeTitle();
        this.autoSizeImage();
        this.positionImage()
    } else {
        this._width = a;
        this.size = new ADSize(a, ADBarButtonHeight);
        this.positionImage()
    }
};
ADBarButton.prototype.autoSizeTitle = function() {
    if (this._title != "" && this.layerIsInDocument) {
        this.layer.style.width = "auto";
        var a = parseInt(window.getComputedStyle(this.layer).width);
        this.size = new ADSize(a, this._size.height)
    }
};
ADBarButton.prototype.autoSizeImage = function() {
    if (this._image !== null) {
        var b = this.getBorderXOffsets();
        var a = this._image.width + b.left + b.right;
        this.size = new ADSize(a, this._size.height)
    }
};
ADBarButton.prototype.setTitle = function(a) {
    this.background.innerText = a;
    this._title = a;
    if (this._width == 0) {
        this.autoSizeTitle()
    }
};
ADBarButton.prototype.setImage = function(a) {
    this.icon.src = a.url;
    this._image = a;
    if (a.loaded) {
        this.positionImage()
    } else {
        a.addPropertyObserver("loaded", this)
    }
};
ADBarButton.prototype.getImageOffset = function() {
    return new ADPoint(parseInt(this.icon.style.left), parseInt(this.icon.style.top))
};
ADBarButton.prototype.setImageOffset = function(a) {
    this.icon.style.left = ADUtils.px(a.x);
    this.icon.style.top = ADUtils.px(a.y);
    this.usesAutomaticImageOffset = false
};
ADBarButton.prototype.positionImage = function() {
    if (this._image === null || !this.usesAutomaticImageOffset) {
        return
    }
    var a = Math.min(this._image.height, this.size.height);
    this.icon.height = a;
    var b = new ADPoint((this._size.width - this.icon.width) / 2, (ADBarButtonHeight - this.icon.height) / 2);
    if (this._type == ADBarButtonItemTypeBack) {
        b.x += ADBarButtonPointyXOffset
    }
    if (this._type == ADBarButtonItemTypeForward) {
        b.x -= ADBarButtonPointyXOffset
    }
    this.imageOffset = b;
    this.usesAutomaticImageOffset = true
};
ADBarButton.prototype.setType = function(a) {
    this.layer.removeClassName(this._type);
    this.layer.addClassName(a);
    this._type = a
};
ADBarButton.prototype.setStyle = function(a) {
    this.layer.removeClassName(this._style);
    this.layer.addClassName(a);
    this._style = a
};
ADBarButton.prototype.getBorderXOffsets = function() {
    var c = window.getComputedStyle(this.background);
    var b = c.getPropertyCSSValue("border-left-width");
    var a = c.getPropertyCSSValue("border-right-width");
    return {
        left: (b !== null) ? b.getFloatValue(CSSPrimitiveValue.CSS_PX) : 0,
        right: (a !== null) ? a.getFloatValue(CSSPrimitiveValue.CSS_PX) : 0
    }
};
ADBarButton.prototype.handlePropertyChange = function(b, a) {
    if (this._width == 0) {
        this.autoSizeImage()
    }
    this.positionImage()
};
ADBarButton.prototype.dispatchEvent = function(a) {
    a.barButtonItem = this.barButtonItem;
    this.callSuper(a)
};
ADClass(ADBarButton);
ADBarButton.init = function() {
    var a = ["UINavigationBarDoneButtonPressed", "UINavigationBarDoneButton", "UINavigationBarDefaultForwardPressed", "UINavigationBarDefaultForward", "UINavigationBarDefaultButtonPressed", "UINavigationBarDefaultButton", "UINavigationBarDefaultBackPressed", "UINavigationBarDefaultBack", "UINavigationBarBlackTranslucentForward", "UINavigationBarBlackTranslucentButton", "UINavigationBarBlackTranslucentBack", "UINavigationBarBlackOpaqueForward", "UINavigationBarBlackOpaqueButton", "UINavigationBarBlackOpaqueBack", "UINavigationBarBlackForwardPressed", "UINavigationBarBlackButtonPressed", "UINavigationBarBlackBackPressed"];
    for (var b = 0; b < a.length; b++) {
        ADUtils.preloadImageAsset("button/" + a[b] + ".png")
    }
};
window.addEventListener("load", ADBarButton.init, false);
const ADBarButtonItemTypePlain = "plain";
const ADBarButtonItemTypeSquare = "square";
const ADBarButtonItemTypeBack = "back";
const ADBarButtonItemTypeForward = "forward";
const ADBarButtonItemTypeFlexibleSpace = "flexible-space";
const ADBarButtonItemTypeFixedSpace = "fixed-space";
const ADBarButtonItemStyleBlack = "black";
const ADBarButtonItemStyleDefault = "default";
const ADBarButtonItemStyleLightBlue = "lightblue";
ADBarButtonItem.inherits = ADBarItem;
ADBarButtonItem.includes = [ADEventTarget];
ADBarButtonItem.synthetizes = ["maxWidth", "customView", "width", "type", "style"];
function ADBarButtonItem(a) {
    this.callSuper();
    this.view = new ADBarButton(a);
    this.view.isCustomView = false;
    this.view.barButtonItem = this;
    this.eventTarget = this.view
}
ADBarButtonItem.prototype.getEnabled = function() {
    return (!this.view.isCustomView) ? this.view.enabled: this.view.userInteractionEnabled
};
ADBarButtonItem.prototype.setEnabled = function(a) {
    if (this.view.isCustomView) {
        this.view.userInteractionEnabled = a
    } else {
        this.view.enabled = a
    }
};
ADBarButtonItem.prototype.getMaxWidth = function() {
    return (!this.view.isCustomView) ? this.view.maxWidth: 0
};
ADBarButtonItem.prototype.setMaxWidth = function(a) {
    if (!this.view.isCustomView) {
        this.view.maxWidth = a
    }
};
ADBarButtonItem.prototype.getWidth = function() {
    return (!this.view.isCustomView) ? this.view.width: this.view.size.width
};
ADBarButtonItem.prototype.setWidth = function(a) {
    if (this.view.isCustomView) {
        this.view.size = new ADSize(a, this.view.size.height)
    } else {
        this.view.width = a
    }
};
ADBarButtonItem.prototype.getTitle = function() {
    return (!this.view.isCustomView) ? this.view.title: ""
};
ADBarButtonItem.prototype.setTitle = function(a) {
    if (!this.view.isCustomView) {
        this.view.title = a
    }
};
ADBarButtonItem.prototype.getImage = function() {
    return (!this.view.isCustomView) ? this.view.image: null
};
ADBarButtonItem.prototype.setImage = function(a) {
    if (!this.view.isCustomView) {
        this.view.image = a
    }
};
ADBarButtonItem.prototype.getImageOffset = function() {
    return (!this.view.isCustomView) ? this.view.imageOffset: null
};
ADBarButtonItem.prototype.setImageOffset = function(a) {
    if (!this.view.isCustomView) {
        this.view.imageOffset = a
    }
};
ADBarButtonItem.prototype.getType = function() {
    return (!this.view.isCustomView) ? this.view.type: ""
};
ADBarButtonItem.prototype.setType = function(a) {
    if (!this.view.isCustomView) {
        this.view.type = a
    }
};
ADBarButtonItem.prototype.getStyle = function(a) {
    return (!this.view.isCustomView) ? this.view.style: ""
};
ADBarButtonItem.prototype.setStyle = function(a) {
    if (!this.view.isCustomView) {
        this.view.style = a
    }
};
ADBarButtonItem.prototype.getCustomView = function() {
    return (this.view.isCustomView) ? this.view: null
};
ADBarButtonItem.prototype.setCustomView = function(a) {
    if (a === this.view) {
        return
    }
    if (!this.view.isCustomView) {
        this.view = undefined
    }
    a.isCustomView = true;
    a.barButtonItem = this;
    this.view = a
};
ADClass(ADBarButtonItem);
const ADTabBarItemHeight = 44;
const ADTabBarItemCanvasHeight = 34;
ADTabBarItem.inherits = ADControl;
ADTabBarItem.synthetizes = ["title", "image", "badgeValue"];
function ADTabBarItem(b, c, a) {
    this._title = "";
    this._image = "";
    this._badgeValue = 0;
    this.callSuper()
}
ADTabBarItem.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-tab-bar-item");
    this.canvas = this.layer.appendChild(document.createElement("canvas"));
    this.label = this.layer.appendChild(document.createElement("div"));
    this.badge = this.layer.appendChild(document.createElement("div"));
    this.badgeContent = this.badge.appendChild(document.createElement("span"));
    this.canvas.height = ADTabBarItemCanvasHeight
};
ADTabBarItem.prototype.setSize = function(a) {
    this.callSuper(a);
    this.badgeContent.style.maxWidth = ADUtils.px(this.size.width - 12);
    this.updateBadgePosition();
    this.canvas.width = this.size.width;
    this.drawImage()
};
ADTabBarItem.prototype.didMoveToSuperview = function() {
    this.callSuper();
    var a = this;
    setTimeout(function() {
        a.updateBadgePosition()
    },
    0)
};
ADTabBarItem.prototype.setSelected = function(a) {
    this.callSuper(a);
    this.drawImage()
};
ADTabBarItem.prototype.setTitle = function(a) {
    this.label.textContent = a;
    this._title = a
};
ADTabBarItem.prototype.setImage = function(a) {
    this._image = a;
    if (a.loaded) {
        this.drawImage()
    } else {
        a.addPropertyObserver("loaded", this)
    }
};
ADTabBarItem.prototype.setBadgeValue = function(a) {
    this._badgeValue = a;
    if (a > 0) {
        this.badge.style.display = "block";
        this.badgeContent.textContent = a;
        this.updateBadgePosition()
    } else {
        this.badge.style.display = "none"
    }
};
ADTabBarItem.prototype.updateBadgePosition = function() {
    if (this.superview == null) {
        return
    }
    if (this.badge.offsetWidth < this.size.width / 2) {
        this.badge.style.left = ADUtils.px(Math.round(this.size.width / 2))
    } else {
        this.badge.style.right = ADUtils.px( - 4)
    }
};
ADTabBarItem.prototype.drawImage = function() {
    if (this._image === null || !this._image.loaded) {
        return
    }
    var a = this.canvas.getContext("2d");
    a.clearRect(0, 0, this.size.width, this.size.height);
    var b = (ADTabBarItemCanvasHeight - this._image.height) / 2;
    a.globalCompositeOperation = "source-over";
    a.drawImage(this._image.element, (this.size.width - this._image.width) / 2, b);
    if (this.selected) {
        a.globalCompositeOperation = "source-in";
        a.drawImage(ADTabBarItem.prototype.maskImage.element, (this.size.width - ADTabBarItem.prototype.maskImage.width) / 2, b)
    }
};
ADTabBarItem.prototype.handlePropertyChange = function(b, a) {
    this.drawImage()
};
ADClass(ADTabBarItem);
ADTabBarItem.init = function() {
    ADTabBarItem.prototype.maskImage = new ADImage(ADUtils.assetsPath + "bar/tab_bar_blue_gradient.png")
};
ADTabBarItem.initWithTitleImageAndTag = function(b, c, a) {
    var d = new ADTabBarItem();
    d.title = b;
    d.image = c;
    d.tag = a;
    return d
};
window.addEventListener("load", ADTabBarItem.init, false);
const ADSearchBarHeight = 44;
const ADSearchBarStyleDefault = "default";
const ADSearchBarStyleBlack = "black";
const ADSearchBarStyleBlackTranslucent = "black-translucent";
const ADSearchBarTextDidChange = "searchBarTextDidChange";
const ADSearchBarTextDidBeginEditing = "searchBarTextDidBeginEditing";
const ADSearchBarTextDidEndEditing = "searchBarTextDidEndEditing";
const ADSearchBarCancelButtonClicked = "searchBarCancelButtonClicked";
const ADSearchBarShowsCancelButtonCSS = "shows-cancel-button";
const ADSearchBarDisplaysPlaceholder = "displays-placeholder";
ADSearchBar.inherits = ADView;
ADSearchBar.includes = [ADEventTriage];
ADSearchBar.synthetizes = ["style", "placeholder", "text", "showsCancelButton", "editing"];
function ADSearchBar() {
    this.delegate = null;
    this._style = ADSearchBarStyleDefault;
    this._placeholder = "";
    this._text = "";
    this._showsCancelButton = false;
    this._editing = false;
    this.hasBeenExplicitelySized = false;
    this.callSuper();
    this.field.addEventListener("focus", this, false);
    this.field.addEventListener("blur", this, false);
    this.field.addEventListener("input", this, false);
    this.field.parentNode.addEventListener("submit", this, false);
    this.button.addEventListener(ADControlTouchUpInsideEvent, this, false);
    this.emptyButton.addEventListener(ADControlTouchUpInsideEvent, this, false);
    this.button.addPropertyObserver("size", this, "updateLayout");
    this.autoresizesSubviews = false
}
ADSearchBar.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-search-bar");
    var a = this.layer.appendChild(document.createElement("form"));
    this.label = a.appendChild(document.createElement("div"));
    this.field = a.appendChild(document.createElement("input"));
    this.field.type = "text";
    this.button = new ADBarButtonItem();
    this.button.title = "Cancel";
    this.button.view.addPropertyObserver("size", this, "updateLayout");
    this.addSubview(this.button.view);
    this.emptyButton = this.addSubview(new ADButton(ADButtonTypeCustom))
};
ADSearchBar.prototype.setSize = function(a) {
    a.height = ADSearchBarHeight;
    this.callSuper(a);
    this.hasBeenExplicitelySized = true;
    this.updateLayout()
};
ADSearchBar.prototype.updateLayout = function() {
    if (!this.layerIsInDocument) {
        return
    }
    var a = this.showsCancelButton ? (this.button.view.size.width + 5) : 0;
    this.field.parentNode.style.right = ADUtils.px(a + 5);
    this.emptyButton.layer.style.right = ADUtils.px(a + 10)
};
ADSearchBar.prototype.didMoveToSuperview = function() {
    this.callSuper();
    if (this.hasBeenExplicitelySized || this.superview === null) {
        return
    }
    this.size = new ADSize(this.superview.size.width, ADSearchBarHeight);
    this.hasBeenExplicitelySized = false
};
ADSearchBar.prototype.setStyle = function(a) {
    this.layer.removeClassName(this._style);
    this.layer.addClassName(a);
    this._style = a
};
ADSearchBar.prototype.setPlaceholder = function(a) {
    this._placeholder = a;
    this.checkForPlaceholderDisplay()
};
ADSearchBar.prototype.getText = function(a) {
    return this.field.value
};
ADSearchBar.prototype.setText = function(a) {
    this.label.innerText = a;
    this.field.value = a;
    if (ADUtils.objectHasMethod(this.delegate, ADSearchBarTextDidChange)) {
        this.delegate[ADSearchBarTextDidChange](this, a)
    }
    this.checkForPlaceholderDisplay()
};
ADSearchBar.prototype.setShowsCancelButton = function(a) {
    if (this._showsCancelButton == a) {
        return
    }
    this.layer[a ? "addClassName": "removeClassName"](ADSearchBarShowsCancelButtonCSS);
    this._showsCancelButton = a;
    this.updateLayout()
};
ADSearchBar.prototype.checkForPlaceholderDisplay = function() {
    this.layer[this.text == "" ? "addClassName": "removeClassName"](ADSearchBarDisplaysPlaceholder);
    if (this.text == "") {
        this.label.innerText = this._placeholder
    }
};
ADSearchBar.prototype.setEditing = function(a) {
    this._editing = a;
    this.field[a ? "focus": "blur"]()
};
ADSearchBar.prototype.handleFocus = function(a) {
    if (ADUtils.objectHasMethod(this.delegate, ADSearchBarTextDidBeginEditing)) {
        this.delegate[ADSearchBarTextDidBeginEditing](this)
    }
    this.editing = true
};
ADSearchBar.prototype.handleBlur = function(a) {
    if (ADUtils.objectHasMethod(this.delegate, ADSearchBarTextDidEndEditing)) {
        this.delegate[ADSearchBarTextDidEndEditing](this)
    }
    this.label.innerText = this.field.value;
    this.checkForPlaceholderDisplay();
    this.editing = false
};
ADSearchBar.prototype.handleInput = function(a) {
    this.checkForPlaceholderDisplay();
    if (ADUtils.objectHasMethod(this.delegate, ADSearchBarTextDidChange)) {
        this.delegate[ADSearchBarTextDidChange](this, this.field.value)
    }
};
ADSearchBar.prototype.handleSubmit = function(a) {
    a.preventDefault();
    this.editing = false
};
ADSearchBar.prototype.handleControlTouchUpInside = function(a) {
    if (a.control === this.emptyButton) {
        this.text = "";
        this.checkForPlaceholderDisplay()
    } else {
        if (ADUtils.objectHasMethod(this.delegate, ADSearchBarCancelButtonClicked)) {
            this.delegate[ADSearchBarCancelButtonClicked](this)
        }
    }
};
ADClass(ADSearchBar);
ADSearchBar.init = function() {
    ADUtils.preloadImageAsset("search/background_default.png");
    ADUtils.preloadImageAsset("search/cancel_touched.png")
};
window.addEventListener("load", ADSearchBar.init, false);
const ADButtonTypeCustom = "custom-type";
const ADButtonTypeRoundedRect = "rounded-rect-type";
const ADButtonTypeDetailDisclosure = "detail-disclosure-type";
const ADButtonTypeInfoLight = "info-light-type";
const ADButtonTypeInfoDark = "info-dark-type";
const ADButtonTypeContactAdd = "contact-add-type";
const ADButtonDefaultHeight = 37;
ADButton.inherits = ADControl;
ADButton.synthetizes = ["currentTitle", "autosized"];
function ADButton(a) {
    this.type = a;
    this.callSuper();
    this._autosized = true;
    this.titles = [""];
    this.syncTitleToState();
    this.addPropertyObserver("state", this, "syncTitleToState")
}
ADButton.prototype.createLayer = function() {
    this.callSuper();
    this.layer.addClassName("ad-button " + this.type)
};
ADButton.prototype.getSize = function() {
    var a = window.getComputedStyle(this.layer);
    return new ADSize(parseInt(a.width), parseInt(a.height))
};
ADButton.prototype.setSize = function(a) {
    this._autoSized = false;
    this.callSuper(a)
};
ADButton.prototype.getCurrentTitle = function() {
    return this.layer.innerText
};
ADButton.prototype.setAutosized = function(a) {
    if (a) {
        this.layer.style.width = "auto";
        this.layer.style.height = ADUtils.px(ADButtonDefaultHeight)
    }
    this._autosized = a
};
ADButton.prototype.titleForState = function(a) {
    return this.titles[a] || null
};
ADButton.prototype.setTitleForState = function(b, a) {
    this.titles[a] = b;
    if (a == this.state) {
        this.syncTitleToState()
    }
};
ADButton.prototype.syncTitleToState = function() {
    if (! (this.type === ADButtonTypeRoundedRect || this.type === ADButtonTypeCustom)) {
        return
    }
    this.layer.innerText = this.titles[this.state] || this.titles[ADControlStateNormal]
};
ADClass(ADButton);
ADButton.init = function() {
    ADUtils.preloadImageAsset("tableview/UITableSelection.png")
};
window.addEventListener("load", ADButton.init, false);
ADInterfaceOrientationPortrait = 0;
ADInterfaceOrientationPortraitUpsideDown = 1;
ADInterfaceOrientationLandscapeLeft = 2;
ADInterfaceOrientationLandscapeRight = 3;
const ADViewControllerTransitionInFromRight = {
    properties: ["transform"],
    from: ["translateX($width)"],
    to: ["translateX(0)"]
};
const ADViewControllerTransitionInFromLeft = {
    properties: ["transform"],
    from: ["translateX(-$width)"],
    to: ["translateX(0)"]
};
const ADViewControllerTransitionOutToRight = {
    properties: ["transform"],
    from: ["translateX(0)"],
    to: ["translateX($width)"]
};
const ADViewControllerTransitionOutToLeft = {
    properties: ["transform"],
    from: ["translateX(0)"],
    to: ["translateX(-$width)"]
};
ADViewController.inherits = ADObject;
ADViewController.synthetizes = ["view", "title", "navigationItem", "toolbarItems", "navigationController", "tabBarItem", "tabBarController"];
function ADViewController(a) {
    this.callSuper();
    this.viewArchiveURL = a;
    this._view = null;
    this._title = "";
    this._tabBarItem = null;
    this._navigationItem = null;
    this._toolbarItems = null;
    this.hidesBottomBarWhenPushed = false;
    this.modalViewController = null;
    this.parentViewController = null;
    this.modalTransitionStyle = null;
    this.searchDisplayController = null;
    this.wasBackItemTransition = ADViewControllerTransitionInFromLeft;
    this.becomesBackItemTransition = ADViewControllerTransitionOutToLeft;
    this.wasTopItemTransition = ADViewControllerTransitionOutToRight;
    this.becomesTopItemTransition = ADViewControllerTransitionInFromRight;
    this.interfaceOrientation = ADInterfaceOrientationPortrait
}
ADViewController.prototype.parentControllerOfKind = function(b) {
    var a = this;
    var c = null;
    while (a.parentViewController !== null) {
        if (a instanceof b) {
            c = a;
            break
        }
        a = a.parentViewController
    }
    return c
};
ADViewController.prototype.getNavigationController = function() {
    return this.parentControllerOfKind(ADNavigationController)
};
ADViewController.prototype.getTabBarController = function() {
    return this.parentControllerOfKind(ADTabBarController)
};
ADViewController.prototype.getView = function() {
    if (this._view === null) {
        this.loadView()
    }
    return this._view
};
ADViewController.prototype.loadView = function() {
    if (this.viewArchiveURL) {
        this._view = new ADView();
        var a = new ADHTMLFragmentLoader(this.viewArchiveURL, this)
    } else {
        this.createDefaultView()
    }
};
ADViewController.prototype.createDefaultView = function() {
    this._view = new ADView();
    this.viewDidLoad()
};
ADViewController.prototype.isViewLoaded = function() {
    return (this._view != null)
};
ADViewController.prototype.viewDidLoad = function() {};
ADViewController.prototype.viewDidUnload = function() {};
ADViewController.prototype.setTitle = function(a) {
    this._title = a;
    if (this.parentViewController instanceof ADTabBarController) {
        this.tabBarItem.title = a
    } else {
        if (this.parentViewController instanceof ADNavigationController) {
            this.navigationItem.title = a
        }
    }
};
ADViewController.prototype.viewWillAppear = function(a) {};
ADViewController.prototype.viewDidAppear = function(a) {};
ADViewController.prototype.viewWillDisappear = function(a) {};
ADViewController.prototype.viewDidDisappear = function(a) {};
ADViewController.prototype.willRotateToInterfaceOrientation = function(a) {};
ADViewController.prototype.didRotateFromInterfaceOrientation = function(a) {};
ADViewController.prototype.presentModalViewControllerAnimated = function(b, a) {
    this.modalViewController = b
};
ADViewController.prototype.dismissModalViewControllerAnimated = function(a) {
    this.modalViewController = null
};
ADViewController.prototype.getNavigationItem = function() {
    if (this._navigationItem === null) {
        this._navigationItem = new ADNavigationItem(this.title);
        this._navigationItem.viewController = this
    }
    return this._navigationItem
};
ADViewController.prototype.getTabBarItem = function() {
    if (this._tabBarItem === null) {
        this._tabBarItem = new ADTabBarItem();
        this._tabBarItem.title = this.title;
        this._tabBarItem.viewController = this
    }
    return this._tabBarItem
};
ADViewController.prototype.setToolbarItems = function(a) {
    this.setToolbarItemsAnimated(a, false)
};
ADViewController.prototype.setToolbarItemsAnimated = function(c, b) {
    this._toolbarItems = c;
    var a = this.parentViewController;
    if (a !== null && a instanceof ADNavigationController) {
        a.toolbar.setItemsAnimated(c, b)
    }
};
ADViewController.prototype.htmlFragmentLoaderDidLoad = function(a) {
    var c = new ADContentView(a.fragment);
    if (this._view !== null && this._view.superview) {
        var b = this._view.layer;
        var d = c.layer;
        d.setAttribute("style", c.layer.style.cssText + b.style.cssText);
        b.parentNode.replaceChild(d, b)
    }
    this._view.layer = c.layer;
    c = null;
    this.viewDidLoad()
};
ADViewController.prototype.htmlFragmentLoaderDidFail = function(a) {
    console.warn("ADViewController — could not load view archive at URL " + a.url);
    this.createDefaultView()
};
ADClass(ADViewController);
const ADNavigationControllerWillShowViewController = "navigationControllerWillShowViewControllerAnimated";
const ADNavigationControllerDidShowViewController = "navigationControllerDidShowViewControllerAnimated";
const ADNavigationControllerHideShowBarDuration = 0.2;
ADNavigationController.inherits = ADViewController;
ADNavigationController.synthetizes = ["viewControllers", "topViewController", "visibleViewController", "navigationBarHidden", "toolbarHidden"];
function ADNavigationController(a, b) {
    this.callSuper(a);
    this.delegate = null;
    this._viewControllers = [];
    this._navigationBarHidden = false;
    this._toolbarHidden = true;
    this.navigationBar = new ADNavigationBar();
    this.toolbar = null;
    this.busy = false;
    this.requiresDeferredHostViewSizeUpdate = false;
    this.previousController = null;
    if (b !== undefined) {
        this.viewControllers = [b]
    }
}
ADNavigationController.prototype.loadView = function() {
    if (this.isViewLoaded()) {
        return
    }
    this._view = new ADNavigationView(this);
    this._view.layer.addClassName("ad-navigation-controller-view");
    this._view.clipsToBounds = true;
    this._view.addSubview(this.navigationBar);
    this.navigationBar.autoresizingMask = ADViewAutoresizingFlexibleWidth;
    this.hostView = this._view.addSubview(new ADView());
    this.hostView.layer.addClassName("ad-navigation-controller-host-view");
    this.toolbar = this._view.addSubview(new ADToolbar());
    this.toolbar.autoresizingMask = ADViewAutoresizingFlexibleWidth;
    this._view.addPropertyObserver("size", this, "sizeChanged");
    this._view.size = new ADSize(window.innerWidth, window.innerHeight);
    this._view.autoresizingMask = ADViewAutoresizingFlexibleWidth | ADViewAutoresizingFlexibleHeight
};
ADNavigationController.prototype.viewMovedToNewSuperview = function() {
    var a = this.navigationBar.topItem;
    if (a !== null) {
        a.updateLayoutIfTopItem()
    }
};
ADNavigationController.prototype.sizeChanged = function() {
    var c = -ADNavigationBarHeight;
    var a = 0;
    var b = this._view.size.height;
    if (!this._navigationBarHidden) {
        c = 0;
        a += ADNavigationBarHeight
    }
    if (!this._toolbarHidden) {
        b -= ADToolbarHeight
    }
    this.navigationBar.position = new ADPoint(0, c);
    this.toolbar.position = new ADPoint(0, b);
    this.hostView.position = new ADPoint(0, a);
    this.updateHostViewSize()
};
ADNavigationController.prototype.updateHostViewSize = function() {
    var a = this._view.size.height;
    if (!this._navigationBarHidden) {
        a -= ADNavigationBarHeight
    }
    if (!this._toolbarHidden) {
        a -= ADToolbarHeight
    }
    this.hostView.size = new ADSize(this._view.size.width, a)
};
ADNavigationController.prototype.getTopViewController = function() {
    return (this._viewControllers.length > 0) ? this._viewControllers[this._viewControllers.length - 1] : null
};
ADNavigationController.prototype.getVisibleViewController = function() {
    var a = this.topViewController;
    return a.modalViewController || a
};
ADNavigationController.prototype.setViewControllers = function(a) {
    this.setViewControllersAnimated(a, false)
};
ADNavigationController.prototype.setViewControllersAnimated = function(c, f) {
    if (this.busy || c.length == 0) {
        return
    }
    ADTransaction.begin();
    this.loadView();
    var d = this.topViewController;
    var h = c[c.length - 1];
    var b = (this._viewControllers.indexOf(h) != -1);
    if (d !== null) {
        d.viewWillDisappear(f)
    }
    h.viewWillAppear(f);
    for (var g = 0; g < this._viewControllers.length; g++) {
        this._viewControllers[g].parentViewController = null
    }
    for (var g = 0; g < c.length; g++) {
        c[g].parentViewController = this
    }
    h.view.size = this.hostView.size.copy();
    h.view.autoresizingMask = ADViewAutoresizingFlexibleWidth | ADViewAutoresizingFlexibleHeight;
    this.hostView.addSubview(h.view);
    if (d === null) {
        if (ADUtils.objectHasMethod(this.delegate, ADNavigationControllerWillShowViewController)) {
            this.delegate[ADNavigationControllerWillShowViewController](this, h, f)
        }
        h.viewDidAppear()
    } else {
        if (d !== h) {
            if (ADUtils.objectHasMethod(this.delegate, ADNavigationControllerWillShowViewController)) {
                this.delegate[ADNavigationControllerWillShowViewController](this, h, f)
            }
            this.transitionToController(h, d, !b, f)
        }
    }
    this._viewControllers = c;
    var a = [];
    for (var g = 0; g < c.length; g++) {
        a.push(c[g].navigationItem)
    }
    this.navigationBar.setItemsAnimated(a, f);
    var e = h.toolbarItems;
    if (e !== null) {
        this.toolbar.setItemsAnimated(e, f)
    }
    ADTransaction.commit()
};
ADNavigationController.prototype.pushViewControllerAnimated = function(a, b) {
    this.setViewControllersAnimated(this._viewControllers.concat([a]), b)
};
ADNavigationController.prototype.popViewControllerAnimated = function(b) {
    if (this._viewControllers.length > 1) {
        var a = this.topViewController;
        this.setViewControllersAnimated(this._viewControllers.slice(0, this._viewControllers.length - 1), b);
        return a
    }
    return null
};
ADNavigationController.prototype.popToRootViewControllerAnimated = function(a) {
    return this.popToViewControllerAnimated(this._viewControllers[0], a)
};
ADNavigationController.prototype.popToViewControllerAnimated = function(a, c) {
    var b = this._viewControllers.indexOf(a);
    if (b < 0 || b >= this._viewControllers.length - 1) {
        return []
    }
    var d = this._viewControllers.slice(b + 1);
    this.setViewControllersAnimated(this._viewControllers.slice(0, b + 1), c);
    return d
};
ADNavigationController.prototype.transitionToController = function(b, c, a, d) {
    this.busy = d;
    this.previousController = c;
    ADTransaction.defaults.duration = d ? ADNavigationBarAnimationDuration: 0;
    if (c !== null) {
        c.view.applyTransition(a ? c.becomesBackItemTransition: c.wasTopItemTransition, false)
    }
    b.view.applyTransition(a ? b.becomesTopItemTransition: b.wasBackItemTransition, false)
};
ADNavigationController.prototype.transitionsEnded = function() {
    var a = this.busy;
    if (this.previousController !== null) {
        this.previousController.view.removeFromSuperview();
        this.previousController.viewDidDisappear(a)
    }
    this.topViewController.viewDidAppear(a);
    this.busy = false;
    if (ADUtils.objectHasMethod(this.delegate, ADNavigationControllerDidShowViewController)) {
        this.delegate[ADNavigationControllerDidShowViewController](this, this.topViewController, a)
    }
};
ADNavigationController.prototype.setNavigationBarHidden = function(a) {
    this.setNavigationBarHiddenAnimated(a, false)
};
ADNavigationController.prototype.setNavigationBarHiddenAnimated = function(b, a) {
    if (this._navigationBarHidden == b) {
        return
    }
    this._navigationBarHidden = b;
    if (!this.isViewLoaded()) {
        return
    }
    ADTransaction.begin();
    ADTransaction.defaults.duration = a ? ADNavigationControllerHideShowBarDuration: 0;
    ADTransaction.defaults.properties = ["position"];
    new ADTransition({
        target: this.navigationBar,
        to: [new ADPoint(0, b ? -ADNavigationBarHeight: 0)]
    }).start();
    new ADTransition({
        target: this.hostView,
        to: [new ADPoint(0, b ? 0: ADNavigationBarHeight)],
        delegate: this
    }).start();
    ADTransaction.commit();
    if (!a || b) {
        this.updateHostViewSize()
    } else {
        this.requiresDeferredHostViewSizeUpdate = true
    }
};
ADNavigationController.prototype.transitionDidComplete = function(a) {
    if (this.requiresDeferredHostViewSizeUpdate) {
        this.requiresDeferredHostViewSizeUpdate = false;
        this.updateHostViewSize()
    }
};
ADNavigationController.prototype.setToolbarHidden = function(a) {
    this.setToolbarHiddenAnimated(a, false)
};
ADNavigationController.prototype.setToolbarHiddenAnimated = function(b, a) {
    if (this._toolbarHidden == b) {
        return
    }
    this._toolbarHidden = b;
    if (!this.isViewLoaded()) {
        return
    }
    new ADTransition({
        target: this.toolbar,
        properties: ["position"],
        to: [new ADPoint(0, this._view.size.height - (b ? 0: ADNavigationBarHeight))],
        duration: a ? ADNavigationControllerHideShowBarDuration: 0,
        delegate: this
    }).start();
    if (!a || b) {
        this.updateHostViewSize()
    } else {
        this.requiresDeferredHostViewSizeUpdate = true
    }
};
ADClass(ADNavigationController);
ADNavigationView.inherits = ADView;
function ADNavigationView(a) {
    this.callSuper();
    this.viewController = a
}
ADNavigationView.prototype.didMoveToSuperview = function(a) {
    this.callSuper(a);
    if (a !== null) {
        this.viewController.viewMovedToNewSuperview()
    }
};
ADClass(ADNavigationView);
const ADTabBarControllerShouldSelectViewController = "tabBarControllerShouldSelectViewController";
const ADTabBarControllerDidSelectViewController = "tabBarControllerDidSelectViewController";
const ADTabBarControllerMaxItems = 5;
ADTabBarController.inherits = ADViewController;
ADTabBarController.synthetizes = ["viewControllers", "selectedViewController", "selectedIndex"];
function ADTabBarController(a) {
    this.callSuper(a);
    this.delegate = null;
    this.tabBar = new ADTabBar();
    this._viewControllers = [];
    this.customizableViewControllers = [];
    this.moreNavigationController = null;
    this.tabBar.delegate = this
}
ADTabBarController.prototype.loadView = function() {
    if (this.isViewLoaded()) {
        return
    }
    this._view = new ADView(this);
    this._view.layer.addClassName("ad-tab-bar-controller-view");
    this._view.size = new ADSize(window.innerWidth, window.innerHeight);
    this._view.autoresizingMask = ADViewAutoresizingFlexibleWidth | ADViewAutoresizingFlexibleHeight;
    this.hostView = this._view.addSubview(new ADView());
    this.hostView.layer.addClassName("ad-tab-bar-controller-host-view");
    this.hostView.size = new ADSize(this._view.size.width, this._view.size.height - ADTabBarHeight);
    this.hostView.autoresizingMask = ADViewAutoresizingFlexibleWidth | ADViewAutoresizingFlexibleHeight;
    this._view.addSubview(this.tabBar);
    this.tabBar.autoresizingMask = ADViewAutoresizingFlexibleWidth | ADViewAutoresizingFlexibleTopMargin
};
ADTabBarController.prototype.tabBarDidSelectItem = function(d, a) {
    while (this.hostView.subviews.length) {
        this.hostView.subviews[0].removeFromSuperview()
    }
    var b = this.selectedViewController;
    var c = b.view;
    b.viewWillAppear(false);
    c.size = this.hostView.size.copy();
    c.autoresizingMask = ADViewAutoresizingFlexibleWidth | ADViewAutoresizingFlexibleHeight;
    this.hostView.addSubview(c);
    if (ADUtils.objectHasMethod(this.delegate, ADTabBarControllerDidSelectViewController)) {
        this.delegate[ADTabBarControllerDidSelectViewController](this, b)
    }
    b.viewDidAppear(false)
};
ADTabBarController.prototype.setViewControllers = function(a) {
    this.setViewControllersAnimated(a, false)
};
ADTabBarController.prototype.setViewControllersAnimated = function(b, c) {
    if (b.length > ADTabBarControllerMaxItems) {}
    this._viewControllers = b;
    var a = [];
    for (var d = 0; d < b.length; d++) {
        a.push(b[d].tabBarItem)
    }
    this.tabBar.setItemsAnimated(a, c)
};
ADTabBarController.prototype.getSelectedViewController = function() {
    var b = null;
    var a = this.selectedIndex;
    if (a >= 0 && a < this._viewControllers.length) {
        b = this._viewControllers[a]
    }
    return b
};
ADTabBarController.prototype.setSelectedViewController = function(a) {
    if (this._viewControllers.indexOf(a) != -1) {
        this.tabBar.selectedItem = a.tabBarItem
    }
};
ADTabBarController.prototype.getSelectedIndex = function() {
    return this.tabBar.items.indexOf(this.tabBar.selectedItem)
};
ADTabBarController.prototype.setSelectedIndex = function(a) {
    if (a >= 0 && a < this._viewControllers.length) {
        this.tabBar.selectedItem = this._viewControllers[a]
    }
};
ADClass(ADTabBarController);