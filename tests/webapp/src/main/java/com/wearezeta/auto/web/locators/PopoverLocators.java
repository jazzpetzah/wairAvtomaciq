package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class PopoverLocators {

	public static final class Shared {
		public static final String xpathUserName = "//*[@data-uie-name='status-user']";
		public static final String xpathUserEmail = "//*[@data-uie-name='status-user-email']";
		public static final String xpathAddButton = "//*[@data-uie-name='do-add-people']";
		public static final String xpathSearchInputField = "//input[@type='text' and contains(@class, 'search-input')]";
		public static final String xpathCreateConversationButton = "//div[contains[@class, 'search-button-add']]";
		public static final Function<String, String> xpathSearchResultByName = (
				name) -> String.format(
				"//div[@data-uie-name='item-user' and text()='%s']", name);
		public static final String xpathContinueButton = "//*[@data-uie-value='continue' and @data-uie-name='do-confirm']";
	}

	public static final class ConnectToPopover {
		public static final String xpathRootLocator = "//div[@id='people-picker-user-bubble']";

		public static final class ConnectToPage {
			public static final String xpathNameConnectionMessage = xpathRootLocator
					+ "//*[@data-uie-name='enter-connect-message']";

			public static final String xpathConnectButton = xpathRootLocator
					+ "//*[@data-uie-name='do-connect']";
		}
	}

	public static final class SingleUserPopover {
		public static final String xpathRootLocator = "//user-profile";

		public static final class SingleUserInfoPage {
			public static final String xpathBlockButton = xpathRootLocator
					+ "//*[@data-uie-name='do-block']";
		}
	}

	public static final class GroupPopover {
		public static final String xpathRootLocator = "//div[@id='participants-bubble']";

		public static final class ParticipantsListPage {
			private static final String xpathHeaderDiv = xpathRootLocator
					+ "//div[contains(@class, 'participants-group-header')]";

			public static final String xpathConversationTitle = xpathHeaderDiv
					+ "/div[contains(@class, 'name')]/div";

			public static final String xpathConversationTitleInput = xpathHeaderDiv
					+ "/div[contains(@class, 'name')]/textarea";

			public static final String xpathLeaveGroupChat = xpathRootLocator
					+ "//*[@data-uie-name='do-leave']";

			public static final String xpathConfirmLeaveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='leave']";

			public static final Function<String, String> xpathParticipantByName = (
					name) -> String.format(
					"%s//div[@data-uie-name='item-user' and text()='%s']",
					xpathRootLocator, name);
		}

		public static final class RemoveParticipantConfirmationPage {
			public static final String xpathConfirmRemoveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='remove']";
		}

		public static final class ParticipantInfoPage {
			public static final String xpathRemoveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-remove']";
		}
	}

}
