package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.common.notifications.Notification;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class NotificationPageSteps {

    private static final Logger LOG = ZetaLogger.getLog(NotificationPageSteps.class.getSimpleName());

    private final WebAppTestContext context;

    public NotificationPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @When("^I listen for notifications$")
    public void IListenForNotifications() throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingNotificationCheck()) {
            context.getNotificationManager().init();
        }
    }

    /**
     * This step does not fail after some time. Steps after this one have to verify a proper state.
     *
     * @see #IGotNotification()
     * @see #IGotNumberOfNotifications()
     * @param from
     * @param body
     * @throws Exception
     */
    @When("^I click next notification from (\\w+) with text (.*)$")
    public void IClickNotificationFromUserWithBody(String from, String body) throws Exception {
        // Since this step changes the test workflow the feature has to be enabled
        if (!WebAppExecutionContext.getBrowser().isSupportingNotificationCheck()) {
            throw new IllegalStateException(String.format("NotificationCheck is not supported with browser '%s'.", WebAppExecutionContext.getBrowserName()));
        }
        if (!context.getNotificationManager().isInitialised()) {
            throw new IllegalStateException(
                    "NotificationManager is not initialised. Please consider adding the step 'I listen for notifications'.");
        }
        // can be namealias or 'Someone'
        from = context.getUsersManager().replaceAliasesOccurences(from, ClientUsersManager.FindBy.NAME_ALIAS);
        context.getNotificationManager().waitAndClickNotificationWithBody(from, body);
    }

    @Then("^I saw notification from (\\w+) with text (.*)$")
    public void IGotNotification(String from, String body) throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingNotificationCheck()) {
            if (!context.getNotificationManager().isInitialised()) {
                throw new IllegalStateException(
                        "NotificationManager is not initialised. Please consider adding the step 'I listen for notifications'.");
            }
            // can be namealias or 'Someone'
            final String user = context.getUsersManager().replaceAliasesOccurences(from, ClientUsersManager.FindBy.NAME_ALIAS);
            List<Notification> allNotifications = context.getNotificationManager().getAllNotifications();
            List<Notification> filteredNotifications = allNotifications.stream()
                    .filter((notification)
                            -> user.equalsIgnoreCase(notification.getTitle())
                    && body.equalsIgnoreCase(notification.getBody()))
                    .collect(Collectors.toList());
            assertThat(String.format("Found Notification in %s", allNotifications), filteredNotifications, is(not(empty())));
        }
    }

    @Then("^I got (\\d+) notifications?$")
    public void IGotNumberOfNotifications(int expectedNumNotifications) throws Exception {
        if (WebAppExecutionContext.getBrowser().isSupportingNotificationCheck()) {
            if (!context.getNotificationManager().isInitialised()) {
                throw new IllegalStateException(
                        "NotificationManager is not initialised. Please consider adding the step 'I listen for notifications'.");
            }
            assertThat("Number of notifications",
                    context.getNotificationManager().getAllNotifications().size(),
                    is(expectedNumNotifications));
        }
    }
}
