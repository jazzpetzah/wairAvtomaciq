package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class PopoverLocators {

	public static final class Shared {
		public static final String xpathUserName = "//*[@data-uie-name='status-user']";
		public static final String xpathUserEmail = "//*[@data-uie-name='status-user-email']";
		public static final String xpathAddButton = "//*[@data-uie-name='do-add-people']";
	}

	// public static final class PeoplePickerPage {
	//
	// public static final String classNameSearchInput = "search-input";
	//
	// public static final String classNameCreateConversationButton =
	// "search-button-add";
	//
	// public static final Function<String, String> xpathSearchResultByName = (
	// name) -> String.format(
	// "//div[@data-uie-name='item-user' and text()='%s']", name);
	//
	// public static final String xpathCloseSearchButton =
	// "//div[contains(@class,'search-close')]";
	//
	// public static final String xpathAddPeopleButton =
	// "//*[@data-uie-name='do-add-people']";
	//
	// public static final String xpathLeaveGroupChat =
	// "//*[@data-uie-name='do-leave']";
	//
	// public static final String xpathConfirmLeaveButton =
	// "//*[@data-uie-name='do-confirm' and @data-uie-value='leave']";
	//
	// private static final String xpathHeaderDiv =
	// "//div[contains(@class, 'participants-group-header')]";
	//
	// public static final String xpathConversationTitle = xpathHeaderDiv
	// + "/div[contains(@class, 'name')]/div";
	//
	// public static final String xpathConversationTitleInput = xpathHeaderDiv
	// + "/div[contains(@class, 'name')]/textarea";
	//
	// public static final String idConversationPopupPage =
	// "participants-bubble";
	// private static final String xpathConversationPopupPage =
	// "//div[@id='participants-bubble']";
	//
	// public static final String xpathAddPeopleMessage =
	// xpathConversationPopupPage
	// + "//div[contains(@class, 'confirm-content')]";
	//
	// public static final String xpathConfirmAddButton =
	// xpathConversationPopupPage
	// + "//*[@data-uie-name='do-confirm' and @data-uie-value='continue']";
	//
	// public static final String xpathProfilePageSearchHeader =
	// "//div[contains(@class, 'participants-search-header')]";
	//
	// public static final String xpathProfilePageSearchField =
	// xpathProfilePageSearchHeader
	// + "//input";
	//
	// public static final String xpathNameBlockButton =
	// "//*[@data-uie-name='do-block']";
	//
	// public static final String xpathUserName =
	// "//*[@data-uie-name='status-user']";
	//
	// public static final String xpathParticipantName =
	// "//div[@class='search-list-item-name' and text()='%s']/..";
	//
	// }

	public static final class ConnectToPopover {
		public static final String xpathRootLocator = "//div[@id='people-picker-user-bubble']";

		public static final class ConnectToPage {
			public static final String xpathUserName = xpathRootLocator
					+ Shared.xpathUserName;

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

			public static final String xpathUserName = xpathRootLocator
					+ Shared.xpathUserName;
			public static final String xpathUserEmail = xpathRootLocator
					+ Shared.xpathUserEmail;
			public static final String xpathAddButton = xpathRootLocator
					+ Shared.xpathAddButton;
		}
	}

	public static final class GroupPopover {
		public static final String xpathRootLocator = "//div[@id='participants-bubble']";

		public static final class ParticipantsListPage {
			public static final String xpathAddButton = xpathRootLocator
					+ Shared.xpathAddButton;

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

			public static final Function<String, String> xpathSearchResultByName = (
					name) -> String.format(
					"%s//div[@data-uie-name='item-user' and text()='%s']",
					xpathRootLocator, name);
		}

		public static final class RemoveParticipantConfirmationPage {
			public static final String xpathConfirmRemoveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='remove']";
		}

		public static final class ParticipantInfoPage {
			public static final String xpathUserName = xpathRootLocator
					+ Shared.xpathUserName;
			public static final String xpathRemoveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-remove']";
		}
	}

}
