package com.wearezeta.auto.osx.steps;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonCallingSteps;
import com.wearezeta.auto.common.log.ZetaLogger;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallingSteps {

	private static final Logger log = ZetaLogger.getLog(CallingSteps.class
			.getSimpleName());

	private final CommonCallingSteps commonCallingSteps = CommonCallingSteps
			.getInstance();

	// private AudioRecorder recorder;
	// private AudioPlayer player;

	/**
	 * Calls to specifed user
	 * 
	 * @step. ^(.*) calls (.*)$
	 * 
	 * @param callerUserNameAlias
	 *            name alias of user that calls
	 * @param conversationNameAlias
	 *            conversation name in which call will appear
	 * 
	 * @throws Exception
	 */
	@When("^(.*) calls (.*)$")
	public void UserCallsToConversation(String callerUserNameAlias,
			String conversationNameAlias) throws Exception {
		commonCallingSteps.UserXCallsToUserYUsingToolZ(callerUserNameAlias,
				conversationNameAlias);
	}

	/**
	 * Stops current call
	 * 
	 * @step. ^Caller dismisses call$
	 * 
	 * @throws Exception
	 */
	@When("^Caller dismisses call$")
	public void UserStopsCall() throws Exception {
		commonSteps.StopCurrentCall();
	}

	/**
	 * Start to listen what caller says
	 * 
	 * @step. ^I start listening to caller$
	 * 
	 * @throws Exception
	 */
	@When("^I start listening to caller$")
	public void IStartListeningToCaller() throws Exception {
		throw new PendingException(
				"This stuff has to be implemented on the calling backend");
		// CallingUtil.setSpeakerSource(CallingUtil.SOUNDFLOWER_2CH);
		// CallingUtil.setMicrophoneSource(CallingUtil.SOUNDFLOWER_2CH);
		// CallingUtil.setInputVolume(100);
		// CallingUtil.setOutputVolume(100);
		// recorder = new AudioRecorder(CallingUtil.AUDIO_TOOLS_PATH
		// + "/output.wav");
		// recorder.captureAudio();
	}

	/**
	 * Send audio file content as microphone input from autocall side
	 * 
	 * @step. ^Caller says (.*) to call$
	 * 
	 * @param filename
	 *            audio file
	 * 
	 * @throws IOException
	 */
	@When("^Caller says (.*) to call$")
	public void ContactSaysToCall(String filename) throws IOException {
		throw new PendingException(
				"This stuff has to be implemented on the calling backend");
		// player = new AudioPlayer(CallingUtil.AUDIO_TOOLS_PATH + "/" +
		// filename);
		// player.playAudio();
	}

	/**
	 * Stop listening for call content
	 * 
	 * @step. ^I stop listening to caller$
	 * 
	 * @throws Exception
	 */
	@When("^I stop listening to caller$")
	public void IStopListeningToContact() throws Exception {
		throw new PendingException(
				"This stuff has to be implemented on the calling backend");
		// CallingUtil.setInputVolume(50);
		// CallingUtil.setOutputVolume(50);
		// CallingUtil.setSpeakerSource(CallingUtil.HEADPHONES);
		// CallingUtil.setMicrophoneSource(CallingUtil.INTERNAL_MICROPHONE);
		// recorder.stopCapture();
	}

	/**
	 * Calculate PESQ score for transfered audio
	 * 
	 * @step. ^I check that I hear all as said from (.*)$
	 * 
	 * @param filename
	 *            source audio file
	 * 
	 * @throws Exception
	 */
	@Then("^I check that I hear all as said from (.*)$")
	public void ICheckThatIHearAllCorrect(String filename) throws Exception {
		throw new PendingException(
				"This stuff has to be implemented on the calling backend");
		// prepare output file
		// CallingUtil.removeSilenceFromFile(CallingUtil.AUDIO_TOOLS_PATH
		// + "/output.wav", CallingUtil.AUDIO_TOOLS_PATH
		// + "/clean_output.wav");
		// CallingUtil
		// .removeSilenceFromFile(CallingUtil.AUDIO_TOOLS_PATH + "/"
		// + filename, CallingUtil.AUDIO_TOOLS_PATH + "/clean_"
		// + filename);
		// double score = CallingUtil.calculatePESQScore(
		// CallingUtil.AUDIO_TOOLS_PATH + "/clean_" + filename,
		// CallingUtil.AUDIO_TOOLS_PATH + "/clean_output.wav");
		// log.debug(score);
		// Assert.assertTrue("Incorrect score (" + score + "< 3)", score > 3);
	}
}
