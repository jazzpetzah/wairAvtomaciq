package com.wearezeta.auto.android.common.reporter;

public class ReporterConstants {

	public static final class Values {

		public static final String CPU = "Current CPU %";

		public static final String FREE_PHYSICAL_MEM = "Free Physical Mem (b)";

		public static final String FREE_PHYSICAL_MEM_NORMALIZED = "Free Physical Mem";

		public static final String FREE_STORAGE_MEM = "Free Storage Mem (b)";

		public static final String FREE_STORAGE_MEM_NORMALIZED = "Free Storage Mem";

		public static final String TOTAL_TX = "Total TX";

		public static final String TOTAL_RX = "Total RX";

		public final static String FOREGROUND_PACKAGE = "Foreground Package";

		public static final String APPLICATION_STARTUP_TIME = "Application Startup Time (ms)";

		public static final String LOGIN_TIME = "Sign In Time (ms)";

		public static final String CONV_PAGE_LOADING_TIME = "Conversation Page Loading Time (ms)";

		public static final String CONV_LOADING_MEDIAN_TIME = "Conversation loading (median)";
		
		public static final String CONV_CONTENT_SYNC_TIME = "Conversation Content Sync Time (ms)";
	}

	public static final class Log {

		public static final String APP_LAUNCH_TIME_REGEX = "App launch time ([\\d]*)";

		public static final String LOGIN_SUCCESS_REGEX = "Login success after ([\\d]*)";

		// TODO: set value
		public static final String FAILED_LOGIN_REGEX = "";

		public static final String CONVERSATION_PAGE_VISIBLE_REGEX = "Conversation page visible after ([\\d]*)";

		public static final String CONVERSATION_SYNC_TIME_REGEX = "";
	}
}
