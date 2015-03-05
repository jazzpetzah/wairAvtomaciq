package com.wearezeta.auto.sync;

public class SEConstants {

	public static final class Acceptance {
		
		public static final int ACCEPTED_RECEIVE_TIME_MS = 5000;
	}

	public static final class Execution {

		public static final int STARTUP_THREAD_POOL_SIZE = 1;

		public static final int SIGN_IN_THREAD_POOL_SIZE = 3;

		public static final int STARTUP_TIMEOUT_MIN = 10;

		public static final int SIGN_IN_TIMEOUT_MIN = 20;

		public static final int AWAITING_MESSAGES_TIMEOUT_MIN = 10;
	}

	public static final class Common {

		public static final String LAUNCH_TIME_RECORD_IN_APPIUM_LOG = "Application launch is started at ([0-9]*)";

		public static final String FAILED_CHECKS_LOG_OUTPUT = "Acceptance checks results:\n"
				+ "\tClients stability check - %s\n"
				+ "\tMessages receiving check - %s\n"
				+ "\tMessages receive time check - %s\n"
				+ "\tMessages order check - %s\n";

		public static final String TEST_CONVERSATION = "SyncEngineTest";
	}
}
