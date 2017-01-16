package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import cucumber.api.java.en.Then;
import java.util.Optional;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.logging.LogEntry;

public class SoundSteps {

    private static final Logger LOG = ZetaLogger.getLog(SoundSteps.class.getSimpleName());
    private static final int SOUND_LOG_TIMEOUT_SECONDS = 10;
    private static final int SOUND_LOG_SLEEP_MILLIS = 2000;
    // we might substract one or two seconds to prevent false results
    // on undefined order of appearence of sounds
    // but this can weaken the ability to ignore old sound events
    private static final int SOUND_LOG_BACKSCANNING_MILLIS = 0;
    private long lastSoundCheckMillis = 0;
    private final WebAppTestContext context;

    public SoundSteps(WebAppTestContext context) {
        this.context = context;
    }

    @Then("Soundfile ("
            + "alert|"
            + "ringing_from_me|"
            + "ringing_from_them|"
            + "ready_to_talk|"
            + "call_drop|"
            + "talk_later|"
            + "nw_interruption|"
            + "ping_from_me|"
            + "ping_from_them|"
            + "new_message"
            + ") did (not )?(start|stop) playing( in loop)?$")
    public void SoundfileAttemptSinceLastCheck(String soundName, String notString, String startStopString, String loopString)
            throws Exception {
        if (!WebAppExecutionContext.getBrowser().isSupportingConsoleLogManagement()) {
            LOG.warn(String.format("No log available for browser '%s'", WebAppExecutionContext.getBrowser()));
            return;
        }
        final boolean loop = loopString != null;
        final boolean not = notString == null;
        final boolean start = "start".equalsIgnoreCase(startStopString);
        final String searchString = start
                ? String.format("Playing sound '%s' (loop: '%s')", soundName, Boolean.toString(loop))
                : String.format("Stopping sound '%s'", soundName);
        if (!start && loop) {
            throw new IllegalStateException("Stopping sound does not have loop attribute");
        }
        Optional<LogEntry> found = context.getLogManager().searchUntilLogEntryAppears(searchString, lastSoundCheckMillis,
                SOUND_LOG_TIMEOUT_SECONDS, SOUND_LOG_SLEEP_MILLIS);
        found.ifPresent((entry) -> {
            lastSoundCheckMillis = entry.getTimestamp() - SOUND_LOG_BACKSCANNING_MILLIS;
        });
        if (not) {
            assertTrue(String.format("No attempt for '%s'", searchString), found.isPresent());
        } else {
            assertFalse(String.format("There was an attempt for '%s'", searchString), found.isPresent());
        }
    }

}
