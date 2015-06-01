package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.android.locators.AndroidLocators.AddPhoneNumberPage;
import com.wearezeta.auto.android.pages.registration.AddNamePage;
import com.wearezeta.auto.android.pages.registration.AreaCodePage;
import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.android.pages.registration.ProfilePicturePage;
import com.wearezeta.auto.android.pages.registration.VerificationPage;
import com.wearezeta.auto.android.pages.registration.WelcomePage;
import com.wearezeta.auto.common.AbstractPagesCollection;


public class PagesCollection extends AbstractPagesCollection{
	public static AndroidPage currentPage = null;
	public static LoginPage loginPage = null;
	public static CallingOverlayPage callingOverlayPage = null;
	public static CallingLockscreenPage callingLockscreenPage = null;
	public static ContactListPage contactListPage = null;
	public static PersonalInfoPage personalInfoPage = null;
	public static DialogPage dialogPage = null;
	public static PeoplePickerPage peoplePickerPage = null;
	public static ConnectToPage connectToPage = null;
	public static OtherUserPersonalInfoPage otherUserPersonalInfoPage = null;
	public static RegistrationPage registrationPage = null;
	public static SettingsPage settingsPage = null;
	public static AboutPage aboutPage = null; 
	public static CommonAndroidPage commonAndroidPage = null;
	public static UnknownUserDetailsPage unknownUserDetailsPage = null;
	
	
	//Registration
	public static WelcomePage welcomePage = null;
	public static EmailSignInPage emailSignInPage = null;
	public static AddPhoneNumberPage addPhoneNumberPage = null;
	public static AreaCodePage areaCodePage = null;
	public static VerificationPage verificationPage = null;
	public static AddNamePage addNamePage = null;
	public static ProfilePicturePage profilePicturePage = null;
}
