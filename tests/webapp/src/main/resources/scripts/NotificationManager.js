console.log('NotificationManager init');
if(typeof self.persistentNotifications === typeof undefined){
  self.persistentNotifications = [];
  self.clickableNotifications = [];
  function onPush(args) {
    self.persistentNotifications.push(args);
  }
  wire.app.repository.system_notification.notifications.push = function () {
    var push = Array.prototype.push.apply(this,arguments);
    for (var i = 0, l = arguments.length; i < l; i++) {
      var notification = {};
      notification["title"] = arguments[i].title;
      notification["body"] = arguments[i].body;
      notification["tag"] = arguments[i].tag;
      notification["silent"] = arguments[i].silent;
      notification["renotify"] = arguments[i].renotify;
      notification["requireInteraction"] = arguments[i].requireInteraction;
      notification["lang"] = arguments[i].lang;
      notification["dir"] = arguments[i].dir;
      notification["badge"] = arguments[i].badge;
      notification["timestamp"] = arguments[i].timestamp;
      notification["icon"] = arguments[i].icon;
      notification["conversation_id"] = arguments[i].data.conversation_id;
      notification["message_id"] = arguments[i].data.message_id;
      onPush(JSON.stringify(notification));
      for (var i = 0; i < clickableNotifications.length; i++) {
        console.log('NotificationManager checking '+clickableNotifications[i]+' | '+arguments[i].title+','+arguments[i].body);
        if(clickableNotifications[i][0] === arguments[i].title && 
	      clickableNotifications[i][1] === arguments[i].body){
          console.log('NotificationManager clicking '+arguments[i].title+','+arguments[i].body);
          arguments[i].dispatchEvent(new MouseEvent('click', {view: window}));
        }
      }
    }
    return push;
  };
}