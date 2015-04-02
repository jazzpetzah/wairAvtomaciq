package com.wearezeta.auto.osx.pages;

import java.lang.reflect.Field;

import com.wearezeta.auto.common.AbstractPagesCollection;
import com.wearezeta.auto.osx.pages.calling.CallPage;
import com.wearezeta.auto.osx.pages.common.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.common.MainMenuAndDockPage;
import com.wearezeta.auto.osx.pages.common.NoInternetConnectionPage;
import com.wearezeta.auto.osx.pages.common.ProblemReportPage;
import com.wearezeta.auto.osx.pages.floating.CallingFloatingPage;
import com.wearezeta.auto.osx.pages.welcome.LoginPage;
import com.wearezeta.auto.osx.pages.welcome.RegistrationPage;
import com.wearezeta.auto.osx.pages.welcome.VerificationPage;
import com.wearezeta.auto.osx.pages.welcome.WelcomePage;

public class PagesCollection extends AbstractPagesCollection {

	// welcome pages
	public static WelcomePage welcomePage = null;

	public static LoginPage loginPage = null;

	public static RegistrationPage registrationPage = null;

	public static VerificationPage verificationPage = null;

	public static ContactListPage contactListPage = null;

	public static ConversationPage conversationPage = null;

	public static ChoosePicturePage choosePicturePage = null;

	public static PeoplePickerPage peoplePickerPage = null;

	public static ConversationInfoPage conversationInfoPage = null;

	public static SelfProfilePage selfProfilePage = null;

	public static ChangePasswordPage changePasswordPage = null;

	public static CallPage callPage = null;

	// common pages
	public static MainMenuAndDockPage mainMenuPage = null;

	public static NoInternetConnectionPage noInternetPage = null;

	public static ProblemReportPage problemReportPage = null;

	// floating pages
	public static CallingFloatingPage callingFloatingPage = null;

	public void closeAllPages() throws Exception {
		for (Field f : this.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (OSXPage.class.isAssignableFrom(f.getType())) {
				OSXPage page = (OSXPage) f.get(this);
				if (page != null) {
					page.close();
				}
			}
		}
	}
}
