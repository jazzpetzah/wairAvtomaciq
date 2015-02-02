package com.wearezeta.auto.android.locators;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.CommonUtils;

public final class AndroidLocators {

	public static final String LOCATORS_PACKAGE = CommonUtils.getAndroidPackageFromConfig(AndroidLocators.class);
	
	public static final String CLASS_NAME = "com.wearezeta.auto.android.locators.AndroidLocators";
	
	public static final class SettingsPage {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$SettingsPage";
		
		public static final String xpathSettingPageTitle = "//android.widget.ListView[1]/android.widget.TextView[@text='Settings']";
	}
	
	public static final class LoginPage {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$LoginPage";
		
		public static final String idSignInButton = LOCATORS_PACKAGE + ":id/ttv__confirmation__confirm";
		
		public static final String idSignUpButton = LOCATORS_PACKAGE + ":id/ttv__confirmation__cancel";
		
		public static final String idLoginButton = LOCATORS_PACKAGE + ":id/zb__sign_in__button";
		
		public static final String xpathLoginInput = "//android.widget.EditText[ancestor::android.widget.LinearLayout[contains(@resource-id,'get__sign_in__email')]]";
		
		public static final String xpathPasswordInput =  "//android.widget.EditText[ancestor::android.widget.LinearLayout[contains(@resource-id,'get__sign_in__password')]]";
		
		public static final String xpathLoginPasswordInput42 = "//android.widget.EditText";
		
		public static final String xpathSignInButton42 = "//android.widget.TextView[contains(@text,'SIGN IN')]";
		
		public static final String xpathLoginButton42 = "//android.widget.TextView[contains(@text,'SIGN IN')]";
		
		public static final String xpathWelcomeSlogan42 = "//android.widget.TextView[contains(@text,'welcome to Wire')]" ;
		
		//public static final String idLoginField = LOCATORS_PACKAGE + ":id/ifv_signin_username_or_email";
		
		//public static final String idPasswordField = LOCATORS_PACKAGE + ":id/ifv_signin_password";
		
		public static final String idLoginProgressBar = LOCATORS_PACKAGE + ":id/pv__sign_in__preview";
		
		public static final String idWelcomeButtonsContainer = LOCATORS_PACKAGE + ":id/cm__choose";
		
		public static final String idWelcomeSlogan = LOCATORS_PACKAGE + ":id/tv__welcome__terms_of_service";
		
		public static final String idLoginError = LOCATORS_PACKAGE + ":id/ttv__sign_in__login_failed__guidance";
		
		public static final String xpathSignUpButton42 = "//android.widget.TextView[@text='REGISTER']";
		
		public static By getByForLoginPageRegistrationButton() {
			if (CommonUtils.getAndroidApiLvl(AndroidLocators.class) < 43) {
				return By
						.xpath(AndroidLocators.LoginPage.xpathSignUpButton42);
			} else {
				return By
						.id(AndroidLocators.LoginPage.idSignUpButton);
			}
		}
		
	}
	
	public static final class AboutPage {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$AboutPage";
		
		public static final String xpathAboutClose = "//android.widget.LinearLayout/android.widget.TextView";
		
		public static final String idAboutLogo =LOCATORS_PACKAGE + ":id/iv__about__logo";

	}
	
	public static final class ConnectToPage {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$ConnectToPage";
		
		public static final String idConnectToHeader = LOCATORS_PACKAGE + ":id/taet__participants__header";
		
		public static final String idConnectToCharCounter = LOCATORS_PACKAGE + ":id/ttv__send_connect_request__connect_button__character_counter";
		
		public static final String idConnectRequestAccept = LOCATORS_PACKAGE + ":id/zb__connect_request__accept_button";
		
		public static final String idConnectRequestIgnore = LOCATORS_PACKAGE + ":id/zb__connect_request__ignore_button";
		
		public static final String idPaticipantsPendingLabel = LOCATORS_PACKAGE + ":id/ttv__participants__pending_label";
		
		public static final String xpathConnectToHeader42 = "//android.widget.EditText[following-sibling::android.widget.TextView]";
		
		public static final String xpathConnectToCharCounter42 = ":id/ttv__send_connect_request__connect_button__character_counter";
		
		public static final String xpathConnectRequestAccept42 = "//android.widget.TextView[@text='Connect']";
		
		public static final String xpathConnectRequestIgnore42 = "//android.widget.TextView[@text='Ignore']";
	
		public static By getByForConnectToPageHeader() {
			if (CommonUtils.getAndroidApiLvl(AndroidLocators.class) < 43) {
				return By
						.xpath(AndroidLocators.ConnectToPage.idConnectToHeader);
			} else {
				return By
						.id(AndroidLocators.ConnectToPage.xpathConnectToHeader42);
			}
		}
	}
	
	public static final class ContactListPage {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$ContactListPage";
		
		public static final String xpathContactListNames42 = "//android.support.v4.view.ViewPager/android.widget.ListView/android.widget.FrameLayout/android.widget.TextView[4]";
		
		public static final String xpathSelfUserName42 =  "//android.support.v4.view.ViewPager/android.widget.ListView/android.widget.FrameLayout/android.widget.TextView[@visible='true'][1]";
		
		public static final String xpathConfirmCancelButton42 = "//android.widget.TextView[@text='LATER']";
		
		public static final String idContactListNames = LOCATORS_PACKAGE + ":id/tv_conv_list_topic";
		
		public static final String xpathContactListMute = "//android.widget.ListView/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.TextView[2]";
		
		public static final String idConfirmCancelButton = LOCATORS_PACKAGE + ":id/cancel";
		
		public static final String idConfirmCancelButtonPicker = LOCATORS_PACKAGE + ":id/zb__confirm_dialog__cancel_button";
		
		public static final String xpathContactFrame = "//android.widget.FrameLayout[child::android.widget.TextView[@text='%s']]";
		
		public static final String xpathContacts = "//android.widget.TextView[@text='%s']"; 
		
		public static final String idSimpleDialogPageText = LOCATORS_PACKAGE + ":id/ttv__simple_dialog__text";
		
		public static final String idMutedIcon = LOCATORS_PACKAGE + ":id/tv_conv_list_voice_muted";
		
		public static final String xpathMutedIcon42 = "//android.widget.ListView/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.TextView";
		
	}
	
	public static final class CommonLocators {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$CommonLocators";
		
		public static final String classNameFrameLayout = "android.widget.FrameLayout";
		
		public static final String classNameLoginPage = "android.support.v4.view.ViewPager";
		
		public static final String classListView = "android.widget.ListView";
		
		public static final String idConfirmBtn = LOCATORS_PACKAGE + ":id/confirm";
		
		public static final String classEditText = "android.widget.EditText";
		
		public static final String classNameTextView = "//android.support.v4.view.ViewPager[1]/android.widget.LinearLayout[1]/android.widget.TextView";
		
		public static final String xpathImagesFrameLayout = "//android.widget.GridView[@resource-id='com.android.documentsui:id/grid']/android.widget.FrameLayout";
		
		public static final String xpathImage = "//android.widget.LinearLayout/android.view.View";
		
		public static final String idGalleryBtn = LOCATORS_PACKAGE + ":id/gtv__camera_control__pick_from_gallery";
		
		public static final String idSearchHintClose = LOCATORS_PACKAGE + ":id/zb__search_hint__close_button";
		
		public static final String idDismissUpdateButton = "android:id/button2";
		
		public static final String xpathConfirmBtn42 = "//android.widget.TextView[@text='LEAVE' or @text='REMOVE']";
		
		public static final String xpathDismissUpdateButton42 = "//android.widget.Button[contains(@text,'Dismiss']";
		
		public static final String xpathGalleryBtn42 = "//android.widget.ViewAnimator/android.widget.FrameLayout/android.widget.TextView[1]";

	}
	
	public static final class DialogPage {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$DialogPage";
		
		public static final String idBackgroundOverlay = LOCATORS_PACKAGE + ":id/v_background_dark_overlay";
		
		public static final String idKnockMessage = LOCATORS_PACKAGE + ":id/ttv__row_conversation__knock_message";
		
		public static final String idKnockAction = LOCATORS_PACKAGE + ":id/ttv__row_conversation__action_text";
		
		public static final String idCursorFrame = LOCATORS_PACKAGE + ":id/cv";
		
		public static final String idKnockIcon = LOCATORS_PACKAGE + ":id/gtv__knock_icon";
		
		public static final String idMessage = LOCATORS_PACKAGE + ":id/ltv__row_conversation__message";	
		
		public static final String idDialogTakePhotoButton = LOCATORS_PACKAGE + ":id/gtv__camera_control__take_a_picture";
		
		public static final String idDialogChangeCameraButton = LOCATORS_PACKAGE + ":id/gtv__camera__top_control__back_camera";
		
		public static final String idConfirmButton = LOCATORS_PACKAGE + ":id/ttv__confirmation__confirm";
		
		public static final String idDialogImages = LOCATORS_PACKAGE + ":id/iv__row_conversation__message_image";
		
		public static final String idConnectRequestDialog = LOCATORS_PACKAGE + ":id/connect_request_root";
		
		public static final String idConnectRequestMessage = LOCATORS_PACKAGE + ":id/contact_request_message";
		
		public static final String idConnectRequestConnectTo = LOCATORS_PACKAGE + ":id/user_name";
		
		public static final String idDialogPageBottomFrameLayout = LOCATORS_PACKAGE + ":id/cv";
		
		public static final String idConnectRequestChatLabel = LOCATORS_PACKAGE + ":id/ttv__row_conversation__connect_request__chathead_footer__label";
		
		public static final String idConnectRequestChatUserName = LOCATORS_PACKAGE + ":id/ttv__row_conversation__connect_request__chathead_footer__username";
		
		public static final String xpathCloseCursor = "//android.support.v4.view.ViewPager[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]";
	
		public static final String xpathAddPicture = "//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.TextView[2]";
		
		public static final String xpathPing = "//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.TextView[3]";
		
		public static final String xpathFormatSpecificMessage = "//android.widget.TextView[@resource-id='" + LOCATORS_PACKAGE + ":id/ltv__row_conversation__message' and @text='%s']";
	
		public static final String xpathMessage = "//android.widget.LinearLayout/android.widget.TextView[contains(@text,'%s')]";
		
		public static final String xpathBackgroundOverlay42 = "//android.widget.FrameLayout/android.widget.ListView";
		
		public static final String xpathCursorFrame42 = "//android.support.v4.view.ViewPager/android.widget.ListView/android.widget.FrameLayout";

		public static final String xpathMessage42 = "//android.widget.LinearLayout[ancestor::android.widget.ListView]/android.widget.LinearLayout/android.widget.TextView";	
		
		public static final String xpathConnectRequestChatLabel42 = "//android.widget.TextView[@text='Connected to ']";
		
		public static final String xpathConnectRequestChatUserName42 = "//android.widget.TextView[preceding-sibling::android.widget.TextView[@text='Connected to ']]";
		
		public static final String xpathDialogTakePhotoButton42 = "//android.widget.ViewAnimator/android.widget.FrameLayout/android.widget.TextView[2]";
		
		public static final String xpathDialogChangeCameraButton42 = "//android.widget.FrameLayout[child::android.widget.ViewAnimator]/android.widget.FrameLayout/android.widget.TextView[2]";
		
		public static final String xpathConfirmButton42 = "//android.widget.TextView[@text='OK']";
		
		public static final String xpathDialogImages42 = "//android.widget.FrameLayout//android.widget.ImageView";
		
		public static By getByForDialogPageImage() {
			if (CommonUtils.getAndroidApiLvl(AndroidLocators.class) < 43) {
				return By
						.xpath(AndroidLocators.DialogPage.xpathDialogImages42);
			} else {
				return By
						.id(AndroidLocators.DialogPage.idDialogImages);
			}
		}
		
		public static By getByForDialogConfirmImageButtn() {
			if (CommonUtils.getAndroidApiLvl(AndroidLocators.class) < 43) {
				return By
						.xpath(AndroidLocators.DialogPage.xpathConfirmButton42);
			} else {
				return By
						.id(AndroidLocators.DialogPage.idConfirmButton);
			}
		}
	}
	
	public static final class OtherUserPersonalInfoPage {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$OtherUserPersonalInfoPage";
		
		public static final String classNameGridView = "android.widget.GridView";
		
		public static final String xpathGroupChatInfoLinearLayout = "//android.widget.GridView/android.widget.LinearLayout";
		
		public static final String xpathGroupChatInfoLinearLayoutId = "//android.widget.GridView/android.widget.LinearLayout[%1$s]";
		
		public static final String xpathGroupChatInfoContacts = "//android.widget.GridView/android.widget.LinearLayout[%1$s]/android.widget.TextView";
		
		public static final String idParticipantsHeader = LOCATORS_PACKAGE + ":id/ttv__participants__header";
		
		public static final String idParticipantsSubHeader = LOCATORS_PACKAGE + ":id/ttv__participants__sub_header";
		
		public static final String idOtherUserPersonalInfoSingleName = LOCATORS_PACKAGE + ":id/ttv__single_participants__header";
		
		public static final String idOtherUserPersonalInfoSingleMail = LOCATORS_PACKAGE + ":id/ttv__single_participants__sub_header";
		
		public static final String idUserProfileConfirmationMenu = LOCATORS_PACKAGE + ":id/user_profile_confirmation_menu";
		
		public static final String idLeftActionButton = LOCATORS_PACKAGE + ":id/gtv__participants__left__action";
		
		public static final String idRightActionButton = LOCATORS_PACKAGE + ":id/gtv__participants__right__action";
		
		public static final String idUnblockBtn = LOCATORS_PACKAGE + ":id/zb__connect_request__unblock_button";
		
		public static final String xpathOtherUserPersonalInfoSingleName42 = "//android.widget.LinearLayout[child::android.widget.TextView]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.TextView";
		
		public static final String xpathLeftActionButton42 = "//android.widget.RelativeLayout/android.widget.RelativeLayout[following-sibling::android.widget.TextView]/android.widget.TextView";
		
		public static final String xpathRightActionButton42 = "//android.widget.RelativeLayout/android.widget.TextView[preceding-sibling::android.widget.RelativeLayout]";
		
		public static final String xpathParticipantsHeader42 = "//android.widget.FrameLayout[following-sibling::android.widget.TextView]/android.widget.LinearLayout/android.widget.TextView";
		
		public static final String xpathParticipantsSubHeader42 = "//android.widget.FrameLayout/android.widget.LinearLayout[child::android.widget.FrameLayout]/android.widget.TextView";
		
		public static final String xpathOtherUserPersonalInfoSingleMail42 = "//android.widget.FrameLayout/android.widget.LinearLayout[child::android.widget.FrameLayout]/android.widget.TextView";
		
		public static final String xpathUnblockBtn42 = "//android.widget.FrameLayout[NOT_IMPLEMENTED='']";
		
		public static By getByForOtherUserPersonalInfoUnlockButton() {
			if (CommonUtils.getAndroidApiLvl(AndroidLocators.class) < 43) {
				return By
						.xpath(AndroidLocators.OtherUserPersonalInfoPage.xpathUnblockBtn42);
			} else {
				return By
						.id(AndroidLocators.OtherUserPersonalInfoPage.idUnblockBtn);
			}
		}
	}
	
	public static final class PeoplePickerPage {
		
		public static final String idParticipantsClose = LOCATORS_PACKAGE + ":id/gtv__participants__close";
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$PeoplePickerPage";
		
		public static final String idPickerUsersUnselected = LOCATORS_PACKAGE + ":id/pick_user_chathead_unselected";
		
		public static final String idPickerBtnDone = LOCATORS_PACKAGE + ":id/ttv_pickuser_confirmbutton__title";
		
		public static final String idPickerSearch = LOCATORS_PACKAGE + ":id/puet_pickuser__searchbox";
		
		public static final String idPickerGrid = LOCATORS_PACKAGE + ":id/gv_pickuser__topresult__gridview";
		
		public static final String idPickerSearchUsers = LOCATORS_PACKAGE + ":id/ttv_pickuser__searchuser_name";
		
		public static final String idPickerRows = LOCATORS_PACKAGE + ":id/ll_pickuser__rowview_searchuser";
		
		public static final String idPeoplePickerClearbtn = LOCATORS_PACKAGE + ":id/gtv_pickuser__clearbutton";
		
		public static final String idCreateConversationIcon = LOCATORS_PACKAGE + ":id/gtv_pickuser_confirmbutton__icon";
		
		public static final String idConnectionRequiesMessage = LOCATORS_PACKAGE + ":id/cet__send_connect_request__first_message";
		
		public static final String idSendConnectionRequestButton = LOCATORS_PACKAGE + ":id/zb__send_connect_request__connect_button";
		
		public static final String idNoResultsFound = LOCATORS_PACKAGE + ":id/ttv_pickuser__error_header";
		
		public static final String idPeoplePickerSerchConversations = LOCATORS_PACKAGE + ":id/ttv_pickuser_searchconversation_name";
		
		public static final String xpathOtherText = "//android.widget.ListView/android.widget.LinearLayout/android.widget.TextView[@text='OTHERS']";
		
		public static final String xpathPeoplePickerGroup = "//android.widget.TextView[@resource-id='" + LOCATORS_PACKAGE + ":id/ttv_pickuser_searchconversation_name' and @text='%s']";
		
		public static final String xpathPeoplePickerContact = "//android.widget.TextView[@resource-id='" + LOCATORS_PACKAGE + ":id/ttv_pickuser__searchuser_name' and @text='%s']";
		
		public static final String xpathPeoplePickerClearbtn42 = "//android.widget.FrameLayout/android.widget.TextView[preceding-sibling::android.widget.ScrollView]";
		
		public static final String xpathPickerSearch42 = "//android.widget.FrameLayout/android.widget.ScrollView/android.widget.FrameLayout/android.widget.EditText";
		
		public static final String xpathPickerSearchUsers42 = "//android.widget.FrameLayout/android.widget.ListView/android.widget.FrameLayout";
		
		public static final String xpathConnectionRequiesMessage42 = "//android.widget.LinearLayout/android.widget.EditText[preceding-sibling::android.widget.RelativeLayout]";
		
		public static final String xpathSendConnectionRequestButton42 = "//android.widget.FrameLayout[preceding-sibling::android.widget.RelativeLayout]/android.widget.TextView";
		
		public static final String xpathPickerBtnDone42 = "//android.widget.TextView[@text='ADD TO CONVERSATION']";
		
	}
	
	public static final class PersonalInfoPage {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$PersonalInfoPage";
		
		public static final String idBackgroundOverlay = LOCATORS_PACKAGE + ":id/v_background_dark_overlay"; 
		
		public static final String idSettingsBox = LOCATORS_PACKAGE + ":id/ll__settings_box_container";
		
		public static final String idEmailField = LOCATORS_PACKAGE + ":id/ttv__profile__email";
		
		public static final String idNameField = LOCATORS_PACKAGE + ":id/ttv__profile__name";
		
		public static final String idNameEdit =LOCATORS_PACKAGE + ":id/tet__profile__guided";
		
		public static final String idSettingsBtn = LOCATORS_PACKAGE + ":id/ttv__profile__settings_box__settings";
		
		public static final String idChangePhotoBtn = LOCATORS_PACKAGE + ":id/gtv__camera_control__change_image_source";
		
		public static final String idGalleryBtn = LOCATORS_PACKAGE + ":id/gtv__camera_control__pick_from_gallery";
		
		public static final String idProfileOptionsButton = LOCATORS_PACKAGE + ":id/gtv__profile__settings_button";
		
		public static final String idAboutButton = LOCATORS_PACKAGE + ":id/ttv__profile__settings_box__about";
		
		public static final String idSignOutBtn = LOCATORS_PACKAGE + ":id/ttv__profile__settings_box__signout";
		
		public static final String idOpenFrom = "com.google.android.apps.plus:id/tiles";
		
		public static final String xpathProfileOptionsButton42 = "//android.widget.FrameLayout[child::android.widget.LinearLayout]/android.widget.TextView";
		
		public static final String xpathSignOutBtn42 = "//android.widget.TextView[@text='Sign Out']";
		
		public static final String xpathEmailField42 = "//android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.TextView";
		
		public static final String xpathNameField42 = "//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[following-sibling::android.widget.TextView]/android.widget.TextView[1]";

		public static final String xpathChangePhotoBtn42 = "//android.widget.ViewAnimator/android.widget.FrameLayout/android.widget.TextView";
		
		public static final String xpathNameEdit42 ="//android.widget.FrameLayout/android.widget.EditText";
		
		public static By getByForProfileOptionsButton() {
			if (CommonUtils.getAndroidApiLvl(AndroidLocators.class) < 43) {
				return By
						.xpath(AndroidLocators.PersonalInfoPage.xpathProfileOptionsButton42);
			} else {
				return By
						.id(AndroidLocators.PersonalInfoPage.idProfileOptionsButton);
			}
		}
	}
	
	public static final class RegistrationPage {
		
		public static final String CLASS_NAME = AndroidLocators.CLASS_NAME + "$RegistrationPage";
		
		public static final String idNameEdit =LOCATORS_PACKAGE + ":id/tet__profile__guided";
		
		public static final String idCreateUserBtn = LOCATORS_PACKAGE + ":id/zb__sign_up__create_account";
		
		public static final String idVerifyEmailBtn = LOCATORS_PACKAGE + ":id/ttv__sign_up__resend";
		
		public static final String idNewPasswordField = LOCATORS_PACKAGE + ":id/tet__sign_up__password";
		
		public static final String idNextArrow = LOCATORS_PACKAGE + ":id/gtv__sign_up__next";
		
		public static final String xpathNameEdit42 = "//android.widget.EditText";
		
		public static final String xpathNewPasswordField42 = "//android.widget.EditText";
		
		public static final String xpathNextArrow42 = "//android.widget.FrameLayout[child::android.widget.LinearLayout]/android.widget.TextView[2]";
		
		public static final String xpathCreateUserBtn42 = "//android.widget.TextView[@text='CREATE ACCOUNT']";
		
		public static final String xpathVerifyEmailBtn42 = "//android.widget.TextView[@text='Re-send']";
		
	}
	
	//public static final String idInstructions  = LOCATORS_PACKAGE + ":id/instructions";
	
	//public static final String idContent = LOCATORS_PACKAGE + ":id/content";
	
	//public static final String idCursorInput = LOCATORS_PACKAGE + ":id/cursor_input";
	
	//public static final String idCreateConversation = LOCATORS_PACKAGE + ":id/ll_pickuser_confirmbutton";
	
	//public static final String idDialogPageContainer = LOCATORS_PACKAGE + ":id/ptopc__conversation__list_view_container";
	
	//public static final String idConnectToSend = LOCATORS_PACKAGE + ":id/send";
	
	//public static final String idInstructionsRequestIgnoreBtn = LOCATORS_PACKAGE + ":id/button_block";
	
	//public static final String idInstructionsRequestConnectBtn = LOCATORS_PACKAGE + ":id/button_connect";
	
	//public static final String idLeaveConversationConfirmationMenu = LOCATORS_PACKAGE + ":id/meta_confirmation_menu";
	
	//public static final String idGroupChatUserGrid = LOCATORS_PACKAGE + ":id/gv__participants__group__users";
	
	//public static final String idConfirmDialogHeader = LOCATORS_PACKAGE + ":id/ttv__confirm_dialog__header";
	
	//public static final String idConfirmDialogCancelButton = LOCATORS_PACKAGE + ":id/zb__confirm_dialog__cancel_button";
	
	//public static final String idConfirmDialogConfirmButton = LOCATORS_PACKAGE + ":id/zb__confirm_dialog__confirm_button";
	
	//public static final String xpathNameLoginPage = "//android.support.v4.view.ViewPager";
	
	//public static final String classNameLinearLayout = "android.widget.LinearLayout";
	
	//public static final String xpathGroupChatContact = "//android.widget.LinearLayout[child::android.widget.TextView[@text='%s']]";;
	
	//public static final String idPersonalInfoForm =LOCATORS_PACKAGE + ":id/ll_self_form";
}
