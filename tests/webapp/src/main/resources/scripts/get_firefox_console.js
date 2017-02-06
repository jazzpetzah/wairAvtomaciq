var logString = [];
for (var i in console.history){
    logString.push(console.history[i].args[0].toString());
}
return logString;