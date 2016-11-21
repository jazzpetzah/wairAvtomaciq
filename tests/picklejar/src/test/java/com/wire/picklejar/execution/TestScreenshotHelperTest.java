package com.wire.picklejar.execution;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestScreenshotHelperTest {
    
    @Test
    public void testGetReportFeatureName() {
        System.out.println("getReportFeatureName");
        assertEquals("test_test", new TestScreenshotHelper().getReportFeatureName("test test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportFeatureName("test:test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportFeatureName("test.test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportFeatureName("test?test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportFeatureName("test___test"));
        assertEquals("test1234test", new TestScreenshotHelper().getReportFeatureName("test1234test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportFeatureName("test      test"));
        assertEquals("test_test_", new TestScreenshotHelper().getReportFeatureName("test \"test\""));
    }

    @Test
    public void testGetReportScenarioName() {
        System.out.println("getReportScenarioName");
        assertEquals("test_test", new TestScreenshotHelper().getReportScenarioName("test test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportScenarioName("test:test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportScenarioName("test.test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportScenarioName("test?test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportScenarioName("test___test"));
        assertEquals("test1234test", new TestScreenshotHelper().getReportScenarioName("test1234test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportScenarioName("test      test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportScenarioName("test \"test\""));
        assertEquals("Verify_Start_Search_is_opened_when_you_press_N_Mac_or_alt_ctrl_N_Win_", 
                new TestScreenshotHelper().getReportScenarioName(
                        "Verify Start (Search) is opened when you press ⌥ ⌘ N (Mac) or alt + ctrl + N (Win)"));
    }

    @Test
    public void testGetReportStepName() {
        System.out.println("getReportStepName");
        assertEquals("test_test", new TestScreenshotHelper().getReportStepName("test test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportStepName("test:test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportStepName("test.test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportStepName("test?test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportStepName("test___test"));
        assertEquals("test1234test", new TestScreenshotHelper().getReportStepName("test1234test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportStepName("test      test"));
        assertEquals("test_test", new TestScreenshotHelper().getReportStepName("test \"test\""));
        assertEquals("test_test", new TestScreenshotHelper().getReportStepName("test[test]"));
        assertEquals("test_test", new TestScreenshotHelper().getReportStepName("test{test}"));
        assertEquals("I_see_intro_about_Wire_saying_Simple_private_secure_messenger_for_chat_calls_sharing_pics_music_videos_GIFs_and_more", 
                new TestScreenshotHelper().getReportStepName(
                        "I see intro about Wire saying Simple, private & secure messenger for chat, calls, sharing pics, music, videos, GIFs and more."));
        assertEquals("I_see_localytics_event_media_completed_media_action_with_attributes_action_text_conversation_type_one_to_one_is_ephemeral_true_ephemeral_time_15_with_bot_false", 
                new TestScreenshotHelper().getReportStepName(
                        "I see localytics event media.completed_media_action with attributes {\\\"action\\\":\\\"text\\\",\\\"conversation_type\\\":\\\"one_to_one\\\",\\\"is_ephemeral\\\":true,\\\"ephemeral_time\\\":15,\\\"with_bot\\\":false}\""));
    }
    
}
