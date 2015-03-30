package com.wearezeta.auto.osx.common;

public class OSXConstants {
	public static final class Scripts {

		private static final String SCRIPTS_FOLDER = "/scripts/";

		public static final String ACTIVATE_USER_SCRIPT = SCRIPTS_FOLDER
				+ "activate_registered_user.txt";

		public static final String PASSWORD_PAGE_VISIBLE_SCRIPT = SCRIPTS_FOLDER
				+ "change_password_page_visible.txt";

		public static final String OPEN_SAFARI_WITH_URL_SCRIPT = SCRIPTS_FOLDER
				+ "open_safari_with_url.txt";

		public static final String INPUT_PASSWORD_LOGIN_PAGE_SCRIPT = SCRIPTS_FOLDER
				+ "input_password_login_page.txt";
	}

	public static final class BrowserActions {

		public static final String SAFARI = "Safari";

		public static final String PASSWORD_RESET_SUCCESS_MESSAGE = "Change password";

		public static final String STAGING_CHANGE_PASSWORD_URL = "https://staging-website.wire.com/forgot/";
	}
}
