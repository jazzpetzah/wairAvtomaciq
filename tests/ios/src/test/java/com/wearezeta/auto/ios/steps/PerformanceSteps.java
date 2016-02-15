package com.wearezeta.auto.ios.steps;

import java.util.Date;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.LoginPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;

import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.reporter.IOSLogListener;
import com.wearezeta.auto.ios.reporter.IOSPerfReportModel;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.performance.PerformanceCommon;
import com.wearezeta.auto.common.performance.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.common.performance.PerformanceHelpers;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PerformanceSteps {

    private static final Logger log = ZetaLogger.getLog(PerformanceSteps.class.getSimpleName());

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();
    private final PerformanceCommon perfCommon = PerformanceCommon.getInstance();
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
    private final IOSPagesCollection pagesCollecton = IOSPagesCollection.getInstance();

    private static final int DEFAULT_SWIPE_TIME = 500;
    private static final int MAX_MSGS_IN_CONVO_WINDOW = 50;

    private DialogPage getDialogPage() throws Exception {
        return pagesCollection.getPage(DialogPage.class);
    }

    private ContactListPage getContactListPage() throws Exception {
        return pagesCollection.getPage(ContactListPage.class);
    }

    /**
     * Send multiple messages from one of my contacts using the backend
     *
     * @param msgsCount count of messages to send. This should be greater or equal to
     *                  the maximum count of messages in convo window (which is
     *                  currently equal to 50 for iOS)
     * @param asContact from which contact should we send these messages
     * @throws Exception
     * @step. ^I receive (\\d+) messages? from contact (.*)
     */
    @Given("^I receive (\\d+) messages? from contact (.*)")
    public void IReceiveXMessagesFromContact(int msgsCount, String asContact)
            throws Exception {
        assert msgsCount >= MAX_MSGS_IN_CONVO_WINDOW : String.format(
                "The count of messages to send (%d) should be greater or equal to the max "
                        + "count of messages in conversation window (%d)",
                msgsCount, MAX_MSGS_IN_CONVO_WINDOW);
        sendXMessagesFromContact(asContact, msgsCount);
    }

    private void sendXMessagesFromContact(String contact, int msgsCount)
            throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        perfCommon.sendMultipleMessagesIntoConversation(contact, msgsCount);
    }

    private void visitConversationWhenAvailable(final String destConvoName)
            throws Exception {
        final int maxRetries = 15;
        int ntry = 1;
        do {
            try {
                log.debug("Tapping on conversation: " + new Date());
                getContactListPage().tapOnName(destConvoName);
            } catch (IllegalStateException | WebDriverException e) {
                if (ntry >= maxRetries) {
                    throw e;
                } else {
                    e.printStackTrace();
                }
            }
            Thread.sleep(3000);
            ntry++;
        } while (!getDialogPage().isCursorInputVisible() && ntry <= maxRetries);
        assert getDialogPage().isCursorInputVisible() : "The conversation has not been opened after "
                + maxRetries + " retries";
        log.debug("Conversation page with contact " + destConvoName
                + " opened.");
        ntry = 1;
        do {
            try {
                getDialogPage().tapOnCursorInput();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (ntry++ <= 3);
        getDialogPage().navigateBack(DEFAULT_SWIPE_TIME);
    }

    /**
     * Restarts application and starts new Appium session
     *
     * @throws Exception
     * @step. ^I restart application$
     */
    @Given("^I restart application$")
    public void IResetApplication() throws Exception {
        CommonIOSSteps commonSteps = new CommonIOSSteps();

        pagesCollecton.clearAllPages();
        if (PlatformDrivers.getInstance().hasDriver(CommonIOSSteps.CURRENT_PLATFORM)) {
            PlatformDrivers.getInstance().quitDriver(CommonIOSSteps.CURRENT_PLATFORM);
        }

        final Future<ZetaIOSDriver> lazyDriver = commonSteps.resetIOSDriver(true);
        ZetaFormatter.setLazyDriver(lazyDriver);
        pagesCollecton.setFirstPage(new LoginPage(lazyDriver));
    }

    /**
     * Waits until spinner after sign in disappers before contact list is shown
     *
     * @throws Exception
     * @step. ^I wait for contact list loaded$
     */
    @When("^I wait for contact list loaded$")
    public void IWaitForContactListLoaded() throws Exception {
        getContactListPage().waitForContactListToLoad();
    }

    /**
     * Starts standard actions loop (read messages/send messages) to measure
     * application performance
     *
     * @param timeout     number of minutes to run the loop
     * @param fromContact contact name/alias, from which I received messages
     * @throws Exception
     * @step. ^I start test cycle for (\\d+) minutes? with messages received
     * from (.*)
     */
    @When("^I start test cycle for (\\d+) minutes? with messages received from (.*)")
    public void WhenIStartTestCycleForNMinutes(int timeout, String fromContact)
            throws Exception {
        if (getDialogPage().waitForCursorInputVisible()) {
            DialogPageSteps steps = new DialogPageSteps();
            steps.INavigateToConversationsList();
        }
        getContactListPage().waitForContactListToLoad();
        final String destConvoName = usrMgr.findUserByNameOrNameAlias(
                fromContact).getName();
        String firstConvoName = getContactListPage().getFirstDialogName();
        int ntry = 1;
        do {
            // This contact, which received messages, should be the first
            // contact in the visible convo list now
            if (destConvoName.equals(firstConvoName)) {
                break;
            } else {
                Thread.sleep(10000);
            }
            firstConvoName = getContactListPage().getFirstDialogName();
            ntry++;
            if (!destConvoName.equals(firstConvoName)) {
                sendXMessagesFromContact(fromContact, 1);
            }
        } while (ntry <= 3);
        assert destConvoName.equals(firstConvoName) : String
                .format("The very first conversation name '%s' is not the same as expected one ('%s')",
                        firstConvoName, destConvoName);

        visitConversationWhenAvailable(destConvoName);

        perfCommon.runPerformanceLoop(new PerformanceLoop() {
            public void run() throws Exception {
                visitConversationWhenAvailable(destConvoName);
                String secondConvoName = getContactListPage()
                        .getConversationNameByIndex(2);
                visitConversationWhenAvailable(secondConvoName);
            }
        }, timeout);
    }

    /**
     * Generates iOS performance report
     *
     * @throws Exception
     * @step. ^I generate performance report for (\\d+) users?$
     */
    @Then("^I generate performance report for (\\d+) users?$")
    public void ThenIGeneratePerformanceReport(int usersCount) throws Exception {
        final IOSPerfReportModel dataModel = new IOSPerfReportModel();
        dataModel.setContactsCount(usersCount - 1);
        final String logOutput = IOSLogListener.getInstance().getStdOut();
        dataModel.loadDataFromLog(logOutput);
        PerformanceHelpers.storeWidgetDataAsJSON(dataModel,
                IOSCommonUtils.getPerfReportPathFromConfig(getClass()));
    }
}
