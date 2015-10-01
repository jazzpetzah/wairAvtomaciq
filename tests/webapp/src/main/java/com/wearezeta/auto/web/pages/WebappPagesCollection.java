package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;
import com.wearezeta.auto.web.pages.external.PasswordChangePage;
import com.wearezeta.auto.web.pages.external.PasswordChangeRequestPage;
import com.wearezeta.auto.web.pages.external.PasswordChangeRequestSuccessfullPage;
import com.wearezeta.auto.web.pages.external.PasswordChangeSuccessfullPage;
import com.wearezeta.auto.web.pages.external.YouAreInvitedPage;
import com.wearezeta.auto.web.pages.popovers.AbstractPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.BringYourFriendsPopoverPage;

public class WebappPagesCollection extends AbstractPagesCollection<WebPage> {

	public static BringYourFriendsPopoverPage bringYourFriendsPopover = null;

	public static LoginPage loginPage = null;

	public static PhoneNumberLoginPage phoneNumberLoginPage = null;

	public static ContactListPage contactListPage = null;

	public static ConversationPage conversationPage = null;

	public static SelfProfilePage selfProfilePage = null;

	public static SettingsPage settingsPage = null;

	public static PendingConnectionsPage pendingConnectionsPage = null;

	public static AbstractPopoverContainer popoverPage = null;

	public static PeoplePickerPage peoplePickerPage = null;

	public static RegistrationPage registrationPage = null;

	public static SelfPictureUploadPage selfPictureUploadPage = null;

	public static ContactsUploadPage contactsUploadPage = null;

	public static GoogleLoginPage googleLoginPage = null;

	public static ProfilePicturePage profilePicturePage = null;

	public static PasswordChangeRequestPage passwordChangeRequestPage = null;

	public static PasswordChangeRequestSuccessfullPage passwordChangeRequestSuccessfullPage = null;

	public static PasswordChangePage passwordChangePage = null;

	public static PasswordChangeSuccessfullPage passwordChangeSuccessfullPage = null;

	public static YouAreInvitedPage youAreInvitedPage = null;

	public static WarningPage warningPage = null;

	public static PhoneNumberVerificationPage phoneNumberVerificationPage = null;

	public static AddEmailAddressPage addEmailAddressPage = null;

	public static GiphyPage giphyPage = null;

	private static WebappPagesCollection instance = null;

	public synchronized static WebappPagesCollection getInstance() {
		if (instance == null) {
			instance = new WebappPagesCollection();
		}
		return instance;
	}

}
