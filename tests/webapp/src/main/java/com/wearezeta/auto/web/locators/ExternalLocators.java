package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class ExternalLocators {

	public static final class YouAreInvitedPage {
		public final static String xpathConnectButton = "//*[@data-ga-action='webapp']";
		public final static String cssDownloadButton = "[data-ga-category='connect'][data-ga-action='download']";
		public final static String cssConnectWireButton = "[data-ga-category='connect'][data-ga-action='connect']";
		public final static String cssDownloadWireButton = "[data-ga-category='connect'][data-ga-action='webapp']";
	}

	public static final class PasswordChangePage {
		public final static String idPasswordInput = "password";
		public final static String cssSubmitButton = "button.btn-lg";
	}

	public static final class PasswordChangeRequestPage {
		public final static String idEmailInput = "email";
		public final static String cssSubmitButton = "button";
	}

	public static final class PasswordChangeRequestSuccessfullPage {
		public final static Function<String, String> xpathLabelByText = txt -> String
				.format("//*[contains(text(),'%s')]", txt);
	}

	public static final class PasswordChangeSuccessfullPage {
		public final static Function<String, String> xpathLabelByText = txt -> String
				.format("//*[contains(text(),'%s')]", txt);
	}
	
	public static final class DownloadPage {
		public final static String cssDownloadIOS = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='ios']";
		public final static String cssDownloadAndroid = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='android']";
		public final static String cssDownloadOSX = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='osx']";
		public final static String cssDownloadWindows = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='windows']";
		public final static String cssDownloadWebapp = "[data-ga-category='download'][data-ga-action='download'][data-ga-value='webapp']";
	}

}
