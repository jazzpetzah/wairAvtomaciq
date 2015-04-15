package com.wearezeta.auto.common.calling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.calling.models.CallingServiceBackend;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

public class CallingUtil {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(CallingUtil.class
			.getSimpleName());

	public static String CALLING_UTIL_PATH = "";

	public static String AUDIO_TOOLS_PATH = "";

	public static String SOX_PREFIX = "/opt/local/bin/";

	public static final String INTERNAL_MICROPHONE = "Internal Microphone";
	public static final String INTERNAL_SPEAKERS = "Internal Speakers";
	public static final String HEADPHONES = "Headphones";
	public static final String SOUNDFLOWER_2CH = "Soundflower (2ch)";
	public static final String SOUNDFLOWER_64CH = "Soundflower (64ch)";

	static {
		try {
			CALLING_UTIL_PATH = CommonUtils
					.getJenkinsProjectDir(CallingUtil.class);
			AUDIO_TOOLS_PATH = CALLING_UTIL_PATH + "/audio/";
		} catch (Exception e) {
		}
	}

	private static String currentCallId = "";

	public static void setSpeakerSource(String source) throws Exception {
		String cmd = String.format("%saudiodevice output \"%s\"",
				AUDIO_TOOLS_PATH, source);
		CommonUtils.executeOsXCommand(new String[] { "bash", "-c", cmd });
	}

	public static void setMicrophoneSource(String source) throws Exception {
		String cmd = String.format("%saudiodevice input \"%s\"",
				AUDIO_TOOLS_PATH, source);
		CommonUtils.executeOsXCommand(new String[] { "bash", "-c", cmd });
	}

	public static void setInputVolume(int level) throws Exception {
		if (level < 0 || level > 100) {
			throw new Exception("Incorrect level for sound input: " + level
					+ ". Correct values from 0 to 100.");
		}
		String cmdLine = String.format(
				"osascript -e \"set volume input volume %s\"", level);
		CommonUtils.executeOsXCommand(new String[] { "bash", "-c", cmdLine });
	}

	public static void setOutputVolume(int level) throws Exception {
		if (level < 0 || level > 100) {
			throw new Exception("Incorrect level for sound input: " + level
					+ ". Correct values from 0 to 100.");
		}
		String cmdLine = String.format(
				"osascript -e \"set volume output volume %s\"", level);
		CommonUtils.executeOsXCommand(new String[] { "bash", "-c", cmdLine });
	}

	public static void removeSilenceFromFile(String inputFile, String outputFile)
			throws Exception {
		// To remove the silence from both beginning and end of the audio file,
		// we call sox silence command twice: once on normal file and again on
		// its reverse, then we reverse the final output.
		// Silence parameters are (in sequence):
		// ABOVE_PERIODS: The period for which silence occurs. Value 1 is used
		// for silence at beginning of audio.
		// DURATION: the amount of time in seconds that non-silence must be
		// detected before sox stops trimming audio.
		// THRESHOLD: value used to indicate what sample value is treats as
		// silence.
		String kAbovePeriods = "1";
		String kDuration = "2";
		String kTreshold = "3%";

		String cmdLine = String
				.format("%ssox %s %s silence %s %s %s reverse silence %s %s %s reverse",
						SOX_PREFIX, inputFile, outputFile, kAbovePeriods,
						kDuration, kTreshold, kAbovePeriods, kDuration,
						kTreshold);
		CommonUtils.executeOsXCommand(new String[] { "bash", "-c", cmdLine });
	}

	public static double calculatePESQScore(String sourceFile, String outputFile)
			throws Exception {
		String cmdLine = String.format("%sPESQ +16000 %s %s", AUDIO_TOOLS_PATH,
				sourceFile, outputFile);

		String output = CommonUtils.executeOsXCommandWithOutput(new String[] {
				"bash", "-c", cmdLine });

		Pattern pattern = Pattern
				.compile("P.862 Prediction[^=]*=\\s([0-9\\.]*)\\s([0-9\\.]*)");
		Matcher matcher = pattern.matcher(output);
		double result = 0;
		while (matcher.find()) {
			result = Double.parseDouble(matcher.group(1));
		}
		return result;
	}
}