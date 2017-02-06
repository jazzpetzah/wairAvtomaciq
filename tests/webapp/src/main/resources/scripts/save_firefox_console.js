console.history = [];
var oldConsole = {};
for (var i in console) {
    if (typeof console[i] == 'function') {
        oldConsole[i] = console[i];
        var strr = '(function(){\
            console.history.push({func:\'' + i + '\',args : Array.prototype.slice.call(arguments)});\
            oldConsole[\'' + i + '\'].apply(console, arguments);\
        })';
        console[i] = eval(strr);
    }
}