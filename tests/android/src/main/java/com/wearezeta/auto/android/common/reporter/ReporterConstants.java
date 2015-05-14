package com.wearezeta.auto.android.common.reporter;

public class ReporterConstants {

	public static final class Columns {

		public static final int MEASUREMENT_COLUMN_ID = 0;
	}

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

	public static final class Xls {

		public static final int MEDIAN_VALUE_INDEX = 3;

		public static final int AVERAGE_VALUE_INDEX = 1;

		public static final int TOTAL_VALUE_INDEX = 5;

		public static final int CHART_CELL_OFFSET = 1;

		public static final String SUMMARY_SHEET = "Summary";

		public static final int BUILD_NUMBER_COLUMN = 2;
		public static final int BUILD_NUMBER_ROW = 3;
		public static final int NUMBER_OF_CONVERSATIONS_ROW = 4;
		public static final int NETWORK_TYPE_ROW = 5;
		public static final int DEVICE_MODEL_ROW = 6;
		public static final int DEVICE_VERSION_ROW = 7;

		private static final int VALUES_OFFSET = 10;
		public static final int CPU_STATS_ROW = VALUES_OFFSET;
		public static final int FREE_PHYSICAL_MEMORY_STATS_ROW = VALUES_OFFSET + 1;
		public static final int FREE_STORAGE_MEMORY_STATS_ROW = VALUES_OFFSET + 2;
		public static final int TOTAL_RX_STATS_ROW = VALUES_OFFSET + 3;
		public static final int TOTAL_TX_STATS_ROW = VALUES_OFFSET + 4;
		private static final int TIME_OFFSET = VALUES_OFFSET + 6;
		public static final int APPLICATION_STARTUP_TIME_ROW = TIME_OFFSET;
		public static final int LOGIN_TIME_ROW = TIME_OFFSET + 1;
		public static final int CONVERSATION_LOADING_TIME_ROW = TIME_OFFSET + 2;
	}
}
