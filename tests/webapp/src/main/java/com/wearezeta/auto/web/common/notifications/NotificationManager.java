package com.wearezeta.auto.web.common.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

public class NotificationManager {

    private static final Logger LOG = ZetaLogger.getLog(NotificationManager.class.getSimpleName());
    private static final String NOTIFICATION_MANAGER_SCRIPT = "NotificationManager.js";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final WebAppTestContext context;
    private boolean initialised = false;

    public NotificationManager(WebAppTestContext context) throws Exception {
        this.context = context;
    }

    public void init() throws Exception {
        context.getDriver().executeScript(loadNotificationManagerScript());
        initialised = true;
    }

    public String loadNotificationManagerScript() throws Exception {
        final String srcScriptPath = String.format("/%s/%s",
                WebCommonUtils.Scripts.RESOURCES_SCRIPTS_ROOT, NOTIFICATION_MANAGER_SCRIPT);
        return new String(Files.readAllBytes(Paths.get(NotificationManager.class.getResource(srcScriptPath).toURI())));
    }

    public List<Notification> getAllNotifications() throws Exception {
        return ((List<String>) context.getDriver().executeScript("return self.persistentNotifications")).stream()
                .map((notificationString) -> {
                    try {
                        return MAPPER.readValue(notificationString, Notification.class);
                    } catch (IOException ex) {
                        return null;
                    }
                })
                .filter((t) -> t != null)
                .collect(Collectors.toList());
    }

    public void waitAndClickNotificationWithBody(String from, String body) throws Exception {
        context.getDriver().executeScript(
                "console.log('NotificationManager pushing '+arguments[1]+' from '+arguments[0]);\n"
                + "self.clickableNotifications.push([arguments[0], arguments[1]])", from, body);
    }

    public boolean isInitialised() {
        return initialised;
    }

}
