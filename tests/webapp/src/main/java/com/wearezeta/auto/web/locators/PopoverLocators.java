package com.wearezeta.auto.web.locators;

import java.util.function.Function;

public final class PopoverLocators {

	public static final class Shared {

		public static final String xpathUserName = "//*[@data-uie-name='status-user']";
		public static final String xpathUserEmail = "//*[@data-uie-name='status-user-email']";
		public static final String xpathAddButton = "//*[@data-uie-name='do-add-people']";
		public static final String xpathSearchInputField = "//input[@type='text' and contains(@class, 'search-input')]";
		public static final String cssSearchResultItems = ".participants-search .search-list-item";
		public static final String xpathCreateConversationButton = "//div[contains(@class, 'search-button-add')]";
		public static final Function<String, String> xpathSearchResultByName = (
				name) -> String.format(
				"//*[@data-uie-name='item-user' and .//*[text()='%s']]", name);
		public static final String xpathContinueButton = "//*[@data-uie-value='continue' and @data-uie-name='do-confirm']";
		public static final String xpathBackButton = "//*[@data-uie-name='do-back']";
	}

	public static final class ConnectToPopover {

		public static final String xpathRootLocator = "//div[@id='people-picker-user-bubble']";

		public static final class ConnectToPage {

			public static final String xpathNameConnectionMessage = xpathRootLocator
					+ "//*[@data-uie-name='enter-connect-message']";

			public static final String xpathConnectButton = xpathRootLocator
					+ "//*[@data-uie-name='do-connect']";
		}
		
		public static final class PendingOutgoingConnectionPage {

//			public static final String xpathPendingConnectionButton = xpathRootLocator
//					+ "//*[@data-uie-name='go-conversation']";
//			
			public static final String xpathCancelRequestButton = xpathRootLocator
					+ "//*[@data-uie-name='go-cancel']";
		}
		
		public static final class CancelRequestConfirmationPage {
			
			public static final String xpathNoButton = "//*data-uie-name='do-cancel']";
		
			public static final String xpathYesButton = "//*data-uie-name='do-confirm']";
		}
	}

	public static final class SendInvitationPopover {

		public static final String xpathRootLocator = "//div[@id='invite-bubble']";

		public static final class SendInvitationPage {

			public static final String xpathInvitationText = xpathRootLocator
					+ "//textarea";
			
			public static final String cssInvitationText = ".invite-link-box .message"; 
		}
	}

	public static final class SingleUserPopover {

		public static final String xpathRootLocator = "//div[@id='participants-bubble']";

		public static final class SingleUserInfoPage {

			public static final String xpathPageRootLocator = "//user-profile";

			public static final String xpathBlockButton = xpathRootLocator
					+ xpathPageRootLocator + "//*[@data-uie-name='do-block']";

			public static final String xpathOpenConversationButton = xpathRootLocator
					+ xpathPageRootLocator
					+ "//*[@data-uie-name='go-conversation']";
			
			public static final String xpathUnblockButton = xpathPageRootLocator
					+ "//*[@data-uie-name='do-unblock']";
		}

		public static final class BlockUserConfirmationPage {

			public static final String xpathConfirmBlockButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='block']";
		}
	}

	public static final class GroupPopover {

		public static final String xpathRootLocator = "//div[@id='participants-bubble']";

		public static final class ParticipantsListPage {

			public static final String xpathPageRootLocator = "//div[contains(@class, 'participants-group')]";

			private static final String xpathHeaderDiv = xpathRootLocator
					+ xpathPageRootLocator
					+ "//div[contains(@class, 'participants-group-header')]";

			public static final String xpathConversationTitle = xpathRootLocator
					+ "//*[@data-uie-name='status-name']";

			public static final String xpathConversationTitleInput = xpathHeaderDiv
					+ "/div[contains(@class, 'name')]/textarea";

			public static final String xpathLeaveGroupChat = xpathRootLocator
					+ xpathPageRootLocator + "//*[@data-uie-name='do-leave']";

			public static final String cssParticipants = "#participants-bubble [data-uie-name='item-user']";

			public static final Function<String, String> xpathParticipantByName = (
					name) -> String.format(
					"%s//*[@data-uie-name='item-user' and .//*[text()='%s']]",
					xpathRootLocator + xpathPageRootLocator, name);

			public static final String cssPeopleCount = ".participants-group-header .people span";
		}

		public static final class LeaveGroupConfirmationPage {

			public static final String xpathConfirmLeaveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='leave']";
		}

		public static final class RemoveParticipantConfirmationPage {

			public static final String xpathConfirmRemoveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='remove']";
		}

		public static final class UnblockUserConfirmationPage {

			public static final String xpathConfirmUnblockButton = xpathRootLocator
					+ "//*[@data-uie-name='do-confirm' and @data-uie-value='unblock']";
		}

		public static final class ConnectParticipantConfirmationPage {

			public static final String xpathConfirmConnectButton = xpathRootLocator
					+ "//*[@data-uie-name='do-connect']";

			public static final String xpathIgnoreConnectButton = xpathRootLocator
					+ "//*[@data-uie-name='do-ignore']";
		}

		public static final class ParticipantInfoPage {

			public static final String xpathRemoveButton = xpathRootLocator
					+ "//*[@data-uie-name='do-remove']";

			public static final String xpathEmailLabel = xpathRootLocator
					+ "//*[@data-uie-name='status-user-email']";

			public static final String xpathAvatar = xpathRootLocator
					+ "//*[@data-uie-name='status-profile-picture']";

			public static final String xpathUserName = xpathRootLocator
					+ "//*[@data-uie-name='status-user']";
		}

		public static final class SelfInfoPage {

			public static final String xpathProfileButton = xpathRootLocator
					+ "//*[@data-uie-name='go-profile']";
		}

		public static final class PendingParticipantPage {

			public static final String xpathPendingButton = xpathRootLocator
					+ "//*[@data-uie-name='go-conversation']";

			public static final String xpathPendingTextBox = xpathRootLocator
					+ "//*[@data-uie-name='enter-connect-message']";
		}

		public static final class ConnectedParticipantPage {

			public static final String xpathOpenConversationButton = xpathRootLocator
					+ "//*[@data-uie-name='go-conversation']";
		}

		public static final class NonConnectedParticipantPage {

			public static final String xpathConnectButton = xpathRootLocator
					+ "//*[@data-uie-name='do-connect']";
		}

		public static final class BlockedParticipantPage {

			public static final String xpathUnblockButton = xpathRootLocator
					+ "//*[@data-uie-name='do-unblock']";
		}

	}
}
