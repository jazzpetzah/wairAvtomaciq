package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class ExternalLocators {

	public static final class YouAreInvitedPage {
		public final static String xpathConnectButton = "//*[@data-ga-action='webapp']";
		public final static String xpathDownloadWireButton = "//*[@data-ga-action='download']";
	}

	public static final class PasswordChangePage {
		public final static String idPasswordInput = "password";
		public final static String xpathSubmitButton = "//button[@type='submit']";
	}

	public static final class PasswordChangeRequestPage {
		public final static String idEmailInput = "email";
		public final static String xpathSubmitButton = "//button[@type='submit']";
	}

	public static final class PasswordChangeRequestSuccessfullPage {
		public final static Function<String, String> xpathLabelByText = txt -> String
				.format("//*[contains(text(),'%s')]", txt);
	}

	public static final class PasswordChangeSuccessfullPage {
		public final static Function<String, String> xpathLabelByText = txt -> String
				.format("//*[contains(text(),'%s')]", txt);
	}

}
