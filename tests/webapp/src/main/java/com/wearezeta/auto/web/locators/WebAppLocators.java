package com.wearezeta.auto.web.locators;

import com.wearezeta.auto.common.misc.FunctionalInterfaces.FunctionFor2Parameters;
import java.util.function.Function;

public final class WebAppLocators {

    public static final class ActivationPage {

        public static final String xpathSuccessfullResult = "//div[@id='200']//p[contains(@class, 'title') and contains(.," +
                "'Account created')]";
        public static final String cssBtnOpenWebApp = ".success:not(.hide) .btn-open-web";
    }

    public static final class AboutPage {

        public static final String xpathVersion = "//*[@id='version']";
    }

    public static final class LoginPage {

        public static final String xpathLoginPage = "//*[@data-uie-name='go-wire-dot-com']";

        public static final String cssEmailInput = "#form-account-login [data-uie-name='enter-email']";

        public static final String cssPasswordInput = "#form-account-login [data-uie-name='enter-password']";

        public static final String classNameProgressBar = "progress-bar";

        public static final String xpathCreateAccountButton = "//*[@data-uie-name='do-register']";

        public static final String xpathSignInButton = "//*[@data-uie-name='do-sign-in']";

        public static final String cssPhoneSignInButton = "[data-uie-name='go-sign-in-phone']";

        public static final String xpathSwitchToRegisterButtons = "//*[@data-uie-name='go-register']";

        public static final String xpathChangePasswordButton = "//*[@data-uie-name='go-forgot-password']";

        public static final String cssLoginErrorText = "[data-uie-name='status-error'] .error";
        
        public static final String cssSessionExpiredErrorText = "[data-uie-name='status-expired']";

        public static final String cssDescriptionText = "[data-uie-name='status-get-wire']";

        public static final String errorMarkedEmailField = "#form-account-login .input-error[data-uie-name='enter-email']";

        public static final String errorMarkedPasswordField = "#form-account-login " +
                ".input-error[data-uie-name='enter-password']";

        public static final String cssRememberMe = "#wire-login-password-remember label";

        public static final String cssForgotPassword = "[data-uie-name='go-forgot-password']";
    }

    public static final class ContactListPage {

        public static final String xpathParentContactListItem = "//div[@id='conversations']";
        public static final String xpathParentArchiveListItem = "//div[@id='archive']";
        public static final String cssParentContactListItem = "#conversations";
        public static final String cssParentArchiveListItem = "#archive";

        public static final String cssIncomingPendingConvoItem = "[data-uie-name='item-pending-requests']";

        public static final String cssIncomingPendingConvoItemSelected = ".left-list-item.text-theme " + cssIncomingPendingConvoItem;

        public static final String xpathOpenArchivedConvosButton = "//*[@data-uie-name='go-archive']";

        public static final Function<String, String> xpathListItemRootWithControlsByName = name -> String
                .format("//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/following-sibling::div[contains" +
                                "(@class, 'controls')]", name);

        public static final String cssArchiveButton = "[data-uie-name='do-archive']";

        public static final String cssCloseArchivedConvosButton = "[data-uie-name='do-close-archive']";

        public static final String cssMuteButton = "[data-uie-name='do-silence']";

        public static final String cssUnmuteButton = "[data-uie-name='do-notify']";

        public static final String cssDeleteButton = "[data-uie-name='do-clear']";

        public static final String cssBlockButton = "[data-uie-name='do-block']";

        public static final String cssLeaveButton = "[data-uie-name='do-leave']";

        public static final String cssCancelRequestButton = "#actions-bubble [data-uie-name='do-cancel-request']";

        public static final Function<String, String> cssContactListEntryByName = (
                name) -> String
                .format("%s div[data-uie-name='item-conversation'][data-uie-value='%s'], %s " +
                                "div[data-uie-name='item-call'][data-uie-value='%s']",
                        cssParentContactListItem, name,
                        cssParentContactListItem, name);

        public static final Function<String, String> cssSelectedContactListEntryByName = (
                name) -> String
                .format(".left-list-item.text-theme div[data-uie-name='item-conversation'][data-uie-value='%s'], " +
                                "div[data-uie-name='item-call'][data-uie-value='%s'].text-theme", name, name);

        public static final Function<String, String> cssOptionsButtonByContactName = (
                name) -> String
                .format("%s div[data-uie-name='item-conversation'][data-uie-value='%s']+ div span[data-uie-name='go-options']",
                        cssParentContactListItem, name,
                        cssParentContactListItem, name);

        public static final Function<String, String> cssArchiveListEntryByName = (
                name) -> String
                .format("%s div[data-uie-name='item-conversation-archived'][data-uie-value='%s']",
                        cssParentArchiveListItem, name);

        public static final String cssBackground = "#background";

        public static final String cssGearButton = "[data-uie-name='go-preferences']";

        public static final String xpathContactListEntries = xpathParentContactListItem
                + "//*[@data-uie-name='item-conversation' or @data-uie-name='item-call']";

        public static final String xpathActiveConversationEntry = xpathParentContactListItem
                + "//*[contains(@class, 'text-theme')]//*[@data-uie-name='item-conversation' or @data-uie-name='item-call']";

        public static final Function<Integer, String> xpathContactListEntryByIndex = (
                idx) -> String.format("(%s)[%s]", xpathContactListEntries, idx);

        public static final String xpathArchivedContactListEntries = xpathParentArchiveListItem
                + "//*[@data-uie-name='item-conversation-archived']";

        public static final Function<String, String> xpathArchivedContactListEntryByName = (
                name) -> String
                .format("%s//*[@data-uie-name='item-conversation-archived' and @data-uie-value='%s']",
                        xpathParentArchiveListItem, name);

        public static final String cssOpenStartUIButton = "[data-uie-name='go-people']";

        public static final Function<String, String> xpathMissedCallNotificationByContactName = (
                name) -> String
                .format("//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/..//*[local-name() = " +
                                "'svg' and @data-uie-name='status-unread']", name);

        public static final Function<String, String> xpathPingIconByContactName = (
                name) -> String
                .format("//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/parent::"
                                + "*//*[@data-uie-name='status-unread' and contains(@class, 'icon-ping')]", name);

        public static final Function<String, String> xpathUnreadDotByContactName = (
                name) -> String
                .format("//*[@data-uie-name='item-conversation' and " +
                                "@data-uie-value='%s']/..//*[@data-uie-name='status-unread']", name);

        public static final Function<String, String> xpathMuteIconByContactName = (
                name) -> String.format(
                "//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/following::"
                        + "*[@data-uie-name='status-silence' and contains(@class, 'conversation-muted')]", name);

        // leave warning
        public static final String cssLeaveModal = ".modal-leave";
        public static final String cssLeaveModalCancelButton = ".modal-leave .modal-close";
        public static final String cssLeaveModalActionButton = ".modal-leave .modal-action";

        // block warning
        public static final String cssBlockModal = ".modal-block";
        public static final String cssBlockModalCancelButton = ".modal-block .modal-close";
        public static final String cssBlockModalActionButton = ".modal-block .modal-action";

        // delete warning for group conversations
        public static final String cssDeleteModalGroup = ".modal-clear-group";
        public static final String cssDeleteModalCancelButtonGroup = ".modal-clear-group .modal-close";
        public static final String cssDeleteModalActionButtonGroup = ".modal-clear-group .modal-action";
        public static final String cssDeleteModalLeaveCheckboxGroup = "[data-uie-name='enter-leave-conversation']";

        // delete warning for 1:1 conversation
        public static final String cssDeleteModalSingle = ".modal-clear";
        public static final String cssDeleteModalCancelButtonSingle = ".modal-clear .modal-close";
        public static final String cssDeleteModalActionButtonSingle = ".modal-clear .modal-action";

        // little dot on avatar in accent color
        public static final String cssBadge = ".conversations-settings-badge";
    }

    public static final class CallPage {
        public static final String idSelfVideoPreview = "video-element-local";

        public static final Function<String, String> xpathMuteCallButtonByConversationName = (
                name) -> String
                .format("//*[@data-uie-name='item-call' and @data-uie-value='%s']/parent::"
                                + "*/parent::*//*[@data-uie-name='do-call-mute']",
                        name);

        public static final Function<String, String> xpathVideoButtonByConversationName = (
                name) -> String
                .format("//*[@data-uie-name='item-call' and @data-uie-value='%s']/parent::"
                                + "*/parent::*//*[@data-uie-name='do-call-video']",
                        name);

        public static final Function<String, String> xpathEndCallButtonByConversationName = (
                name) -> String
                .format("//*[@data-uie-name='item-call' and @data-uie-value='%s']/parent::"
                                + "*/parent::*//*[@data-uie-name='do-call-controls-call-ignore']",
                        name);

        public static final Function<String, String> xpathUserNameByConversationName = user -> String
                .format("//*[@data-uie-name='item-call' and @data-uie-value='%s']",
                        user);

        public static final Function<String, String> xpathOutgoingCallByConversationName = user -> String
                .format("//*[@data-uie-name='item-call' and @data-uie-value='%s']/following-sibling::*[@data-uie-name='call-label-outgoing']",
                        user);

        public static final Function<String, String> xpathIncomingCallByConversationName = user -> String
                .format("//*[@data-uie-name='item-call' and @data-uie-value='%s']/following-sibling::*[@data-uie-name='call-label-incoming']",
                        user);

        public static final Function<String, String> xpathOngoingCallByConversationName = user -> String
                .format("//*[@data-uie-name='item-call' and @data-uie-value='%s']/following-sibling::*[@data-uie-name='call-duration']",
                        user);

        public static final Function<String, String> xpathJoinCallButtonByConversationName = (
                name) -> String
                .format("//*[@data-uie-name='item-call' and @data-uie-value='%s']/parent::"
                                + "*//*[@data-uie-name='do-call-controls-call-join']",
                        name);

        public static final Function<String, String> xpathAcceptCallButtonByConversationName = (
                name) -> String
                .format("//*[@data-uie-name='item-call' and @data-uie-value='%s']/parent::"
                                + "*/parent::*//*[@data-uie-name='do-call-controls-call-accept']",
                        name);

        public static final Function<String, String> xpathDeclineCallButtonByConversationName = (
                name) -> String
                .format("//*[@data-uie-name='item-call' and normalize-space()='%s']/parent::" +
                                "*/parent::*//*[@data-uie-name='do-call-controls-call-decline']",
                        name);

        public static final Function<String, String> xpathMuteCallButtonPressed = (name) -> String.format("//div[@data-uie-name='do-call-mute'" +
                " and contains(@class, 'toggled')]", name);

        public static final Function<String, String> xpathMuteCallButtonNotPressed = (name) -> String.format("//div[@data-uie-name='do-call-mute'" +
                " and not(contains(@class, 'toggled'))]", name);
        
        public static final Function<String, String> cssAvatarInCallControlsByUserId = (id) -> String.format(".calls-controls-row-participants [user-id='%s']", id);
    }
    
    public static final class PreferencesPage {
    
        public static final String cssPreferencesCloseButton = "#preferences [data-uie-name='do-close-preferences']";
        
        public static final String cssPreferencesAccountButton = "#preferences [data-uie-name='go-account']";
        
        public static final String cssPreferencesDevicesButton = "#preferences [data-uie-name='go-devices']";
        
        public static final String cssPreferencesOptionsButton = "#preferences [data-uie-name='go-options']";
        
        public static final String cssPreferencesAboutButton = "#preferences [data-uie-name='go-about']";
    }
    
    public static final class AccountPage {
    
        public static final String cssLogoutButton = "#preferences-account [data-uie-name='do-logout']";

        public static final String cssSelfNameInput = "[data-uie-name='enter-name']";

        public static final String cssUniqueUsername = "[data-uie-name='enter-username']";

        public static final String cssUniqueUsernameError = ".preferences-account-username-error";

        public static final String cssUniqueUsernameHint = ".preferences-account-username-hint";

        public static final String cssNameSelfUserMail = "[data-uie-name='enter-email']";

        public static final String cssNameSelfUserPhoneNumber = "[data-uie-name='enter-phone']";
        
        private static final String cssAccentColorPicker = "[data-uie-name='enter-accent-color']";
        
        public static final String cssAccentColorPickerLabels = cssAccentColorPicker + " label";
        
        public static final String cssAccentColorPickerInputs = cssAccentColorPicker + " input";
        
        public static final Function<Integer, String> cssAccentColorDivById = (
                id) -> String.format("%s.accent-color-%s", cssAccentColorPickerLabels, id);
        
        public static final String cssCurrentAccentColorCircleDiv = cssAccentColorPicker + ".selected .circle";
        
        public static final String cssBackgroundAvatarAccentColor = ".background-accent.bg-theme";

        public static final String cssPicture = "user-avatar";

        public static final String cssSelectPicture = "[data-uie-name='do-select-picture']";

        public static final String cssDeleteAccountButton = "[data-uie-name='go-delete-account']";

        public static final String cssCancelDeleteAccountButton = "[data-uie-name='modal-delete-account'] [data-uie-name='do-cancel']";

        public static final String cssConfirmDeleteAccountButton = "[data-uie-name='modal-delete-account'] [data-uie-name='do-send']";
    }

    public static final class DevicesPage {
        public static final String cssCurrentDevice = "[data-uie-name='preferences-device-current']";

        public static final String cssCurrentDeviceId = "[data-uie-name='preferences-device-current-id']";

        public static final String cssActiveDeviceIds = "[data-uie-name='preferences-device-active-id']";

        public static final String cssActiveDevicesLabels = "[data-uie-name='preferences-device-active-model']";

        public static final String cssXButtons = "[data-uie-name='do-device-remove']";

        public static final Function<String, String> xpathDeviceLabel = (name) -> String
                .format("//*[@data-uie-name='preferences-device-active-model' and contains(text(),'%s')]", name);
    }

    public static final class OptionsPage {

        public static final String cssImportButton = "[data-uie-name='do-share-contacts'] .preferences-option-label";

        public static final String cssReportOption = ".preferences-options-checkbox-label";

    }

    public static final class SettingsPage {

        public static final String xpathSettingsDialogRoot = "//div[@id='self-settings' and contains(@class, 'modal-show')]";

        public static final String cssSettingsCloseButton = "#self-settings [data-uie-name='do-close']";

        public static final String cssSoundAlertsLevel = "[data-uie-name=enter-sound-alerts]";

        public static final String cssConfirmText = "[data-uie-name='delete-confirm-text']";
        public static final String cssSentText = "[data-uie-name='delete-sent']";
        public static final String cssImportAddressbookButton = "[data-uie-name='do-share-osx-contacts']";//macOS
        public static final String cssBackButton = "[data-uie-name='do-device-close']";
        public static final String cssVerificationToggle = ".button-label";

    }

    public static final class SelfProfilePage {

        public static final String cssGearButton = "[data-uie-name='do-settings-menu']";

        public static final String xpathGearMenuRoot = "//div[@id='setting-bubble' and contains(@class, 'bubble-show')]";

        public static final Function<String, String> xpathGearMenuItemByName = (
                name) -> String.format("%s//li[text()='%s']",
                xpathGearMenuRoot, name);

        public static final String xpathCameraButton = "//*[@data-uie-name='go-profile-picture-selection']";

    }

    public static final class ConversationPage {
        // content
        public static final String idConversation = "conversation";
        
        public static final String cssConversation = ".messages-wrap";

        public static final String idMessageList = "message-list";

        public static final String cssUserAvatar = ".user-avatar and .pending";

        public static final String cssWatermark = "[data-uie-name='no-conversation']";

        public static final String cssUsername = ".message-connected-username.label-username";

        // messages (including images, text, missed call notifications, pings)
        public static final String cssMessage = "[data-uie-name='item-message']";

        // message header contains action description
        public static String cssMessageHeader = cssMessage + " .message-header";

        public static String cssTextMessage = cssMessage + " .text";
        
        public static final String cssMessages = "[data-uie-name='item-message']";

        public static final Function<String, String> cssMessagesById = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s']", text);

        public static final Function<String, String> xpathMessageHeaderByText = text -> String
                .format("//*[@data-uie-name='item-message']//*[contains(@class,'text') and contains(text(),'%s')]//../../..",
                        text);
        
        public static final FunctionFor2Parameters<String, String, String> xpathMessageTextByMessageId = (messageId, text) -> String
                .format("//*[@data-uie-name='item-message' and @data-uie-uid='%s']//*[contains(@class, 'text') and text()='%s']", messageId, text);

        public static final Function<String, String> cssUserThatLikeByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] [data-uie-name='message-liked-names']", text);

        public static final Function<String, String> cssLikeWithoutOtherLikesByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .message-body-like-icon:not(.like-button-liked)", text);
        
        public static final Function<String, String> cssUnlikeWithoutOtherLikesByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .message-body-like-icon.like-button-liked", text);
        
        public static final Function<String, String> cssFooterByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .message-footer", text);
        
        public static final Function<String, String> cssLikeWithOtherLikesByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .message-footer .like-button:not(.like-button-liked)", text);
        
        public static final Function<String, String> cssUnlikeWithOtherLikesByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .message-footer .like-button.like-button-liked", text);
        
        public static final Function<String, String> cssLikeTextElementByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .message-footer-label span", text);//TODO

        public static final Function<String, String> cssContextMenuButtonByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] context-menu", text);

        public static final Function<String, String> cssDeleteEverywhereByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] context-menu [data-context-action='delete-everyone']", text);

        public static final Function<String, String> cssDeleteForMeByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] context-menu [data-context-action='delete']", text);

        public static final Function<String, String> cssReactByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] context-menu [data-context-action='react']", text);
        
        public static final Function<String, String> cssLinkPreviewLinkByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .link-preview-site", text);

        public static final String cssDeleteForMeInContext = "[data-context-action='delete']";

        public static final String cssDeleteForEveryoneInContext = "[data-context-action='delete-everyone']";

        public static final Function<String, String> cssLikeSymbol = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .message-body-like", text);

        public static final String xpathLikeInContext = "//li[contains(@data-context-action, 'react') and text()='Like']";

        public static final String xpathUnlikeInContext = "//li[contains(@data-context-action, 'react') and text()='Unlike']";
        
        public static final String cssDownloadInContext = "[data-context-action='download']";

        public static final Function<String, String> cssDeleteButtonByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] [data-uie-name='do-message-delete']", text);

        public static final Function<String, String> cssEditButtonByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] [data-uie-name='do-message-edit']", text);

        public static final Function<String, String> cssLikeButtonByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] [data-uie-name='do-message-react']", text);

        public static final String cssDoDelete = "[data-uie-name='do-delete']";

        public static final String cssDoDeleteForEveryone = "[data-uie-name='do-delete-everyone']";

        public static final Function<String, String> cssEditMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] context-menu [data-context-action='edit']", text);
        
        public static final String cssDoEditMessage = "context-menu [data-context-action='edit']";

        // TODO: replace the bottom css with this, when implemented by developers
        //public static final Function<String, String> cssResetSessionByMessageId = text -> String
        //        .format("[data-uie-name='item-message'][data-uie-uid='%s'] [data-uie-name='do-reset-session']", text);

        public static final Function<String, String> cssResetSessionByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .message-header-decrypt-reset-session span", text);

        public static final String cssCloseResetSessionDialog = "[data-uie-name='modal-session-reset'] [data-uie-name='do-close']";

        public static final String cssLastMessage = "[data-uie-name='item-message']:nth-last-child(1)";

        public static final String cssSecondLastMessage = "[data-uie-name='item-message']:nth-last-child(2)";

        public static final Function<Integer, String> cssLastMessageByIndex = (
                index) -> String.format("%s:nth-last-child(%s)", cssMessage, index);

        public static final String cssLastTextMessage = cssLastMessage + " .text";

        public static final String cssSecondLastTextMessage = cssSecondLastMessage + " .text";

        public static final String cssFirstAction = cssMessage + " .action";

        public static final String cssLastAction = cssLastMessage + " .action";

        public static final String cssLastEditTimestamp = cssLastMessage + " .icon-edit";

        public static final String cssSecondLastEditTimestamp = cssSecondLastMessage + " .icon-edit";

        public static final String cssLastMsgHeader = cssLastMessage + " .message-header-label";

        public static final String cssSecondLastMsgHeader = cssSecondLastMessage + " .message-header-label";

        public static final String cssLastLikeLine = cssLastMessage + " [data-uie-name='message-liked-names']";

        public static final String cssLastLikeList = cssLastMessage + " [data-uie-name='message-liked-avatars']";

        public static final String cssXLastLikeList = cssLastMessage + " .icon-close";

        public static final String cssLikeListAvatars = cssLastLikeList + " .user-avatar-xs";

        //Link preview

        public static final String cssLinkTitle = "[data-uie-name='link-preview-title']";

        public static final String cssLinkPreviewImage = "[data-uie-name='link-preview-image']";

        public static final String cssLinkPreviewLink = "[data-uie-name='link-preview-url']";

        //Location sharing

        public static final String cssSharedLocation = "[data-uie-name='location-name']";

        public static final String xpathSharedLocationLink = "//div[contains(@data-uie-name, 'location-name')]/following-sibling::a[last()]";

        // File transfer

        public static final String cssFile = "[data-uie-name='file'][data-uie-value='%s']";

        public static final String cssFileIcon = cssFile + " [data-uie-name='file-icon']";

        public static final String cssFileName = cssFile + " [data-uie-name='file-name']";

        public static final String cssFileSize = cssFile + " [data-uie-name='file-size']";

        public static final String cssFileStatus = cssFile + " [data-uie-name='file-status']";

        public static final String cssFilePlaceholder = cssFile + " .asset-placeholder";

        public static final String cssFileType = cssFile + " [data-uie-name='file-type']";

        public static final String cssFileCancelUpload = cssFile + " .icon-close";

        // Audio message

        public static final String cssAudio = "[data-uie-name='audio-asset'][data-uie-value='%s']";

        public static final String cssAudioPlay = cssAudio + " [data-uie-name='do-play-media']";

        public static final String cssAudioLoading = cssAudio + " [data-uie-name='status-loading-media']";

        public static final String cssAudioSeekbar = cssAudio + " [data-uie-name='status-audio-seekbar']";

        public static final String cssAudioTime = cssAudio + " [data-uie-name='status-audio-time']";

        // Video message

        public static final String cssVideo = "[data-uie-name='video-asset'][data-uie-value='%s']";

        public static final String cssVideoLoading = cssVideo + " [data-uie-name='status-loading-media']";

        public static final String cssVideoPlay = cssVideo + " [data-uie-name='do-play-media']";

        public static final String cssVideoPause = cssVideo + " [data-uie-name='do-pause-media']";

        public static final String cssVideoSeekbar = cssVideo + " [data-ui-name='status-video-seekbar']";

        public static final String cssVideoTime = cssVideo + " [data-uie-name='status-video-time']";

        public static final String cssVideoCancelUpload = cssVideo + " .media-button-lg";

        public static final String cssVideoCancelDownload = cssVideo + " [data-uie-name='status-loading-media']";

        // images

        public static final String cssFirstImage = "[data-uie-name='go-image-detail'][data-uie-visible='true']:not(.image-loading)";

        public static final String cssImageEntries = "[data-uie-name='go-image-detail'][data-uie-visible='true']";

        public static final String cssLoadingImageEntries = "[data-uie-name='go-image-detail'][data-uie-visible='true'].image-loading";

        public static final String cssMessageAmount = "[data-uie-name='item-message']";

        public static final String cssDeletedMessageAmount = "[data-uie-name='item-message'][data-uie-value='delete']";

        public static final String cssPingMessage = ".pinged";

        // special message identifier
        public static final Function<String, String> xpathMessageEntryByText = text -> String
                .format("//*[@data-uie-name='item-message']//div[contains(@class, 'text') and text()='%s']",
                        text);

        public static final Function<String, String> textMessageByText = text -> String
                .format("//*[@data-uie-name='item-message']//*[text()='%s']",
                        text);

        public static final Function<String, String> xpathEmbeddedYoutubeVideoById = text -> String
                .format("//iframe[contains(@src, '%s')]", text);

        public static final Function<String, String> xpathEmbeddedSoundcloudById = text -> String
                .format("//iframe[contains(@class, 'soundcloud') and contains(@src, '%s')]", text);

        public static final Function<String, String> xpathEmbeddedVimeoById = text -> String
                .format("//iframe[contains(@class, 'vimeo') and contains(@src, '%s')]", text);

        public static final Function<String, String> xpathEmbeddedSpotifyById = text -> String
                .format("//iframe[contains(@class, 'spotify') and contains(@src, '%s')]", text);

        // input area (text input + buttons)
        // This is needed for IE workaround
        public static final String classNameShowParticipantsButton = "show-participants";

        //public static final String cssCollectionsButton = "[data-uie-name='do-collections']";
        public static final String cssCollectionsButton = ".icon-collection";

        public static final String cssShowParticipantsButton = "[data-uie-name='do-participants']";

        public static final String idConversationInput = "conversation-input-text";

        public static final String cssRightControlsPanel = "div.controls-right";

        public static final String cssEphemeralButton = "[data-uie-name='do-set-ephemeral-timer']";

        public static final Function<String, String> xpathEphemeralButtonByTime = time -> String
                .format("//*[@data-uie-name='do-set-ephemeral-timer']//div//span[contains(@class, 'full-screen') and text()='%s']", time);
        
        public static final Function<String, String> xpathEphemeralButtonByUnit = unit -> String
                .format("//*[@data-uie-name='do-set-ephemeral-timer']//div//span[contains(@class, 'ephemeral-timer-button-unit') and text()='%s']", unit);

        public static final String cssEphemeralTimers = "[data-context-tag='ephemeral'] .bubble-menu li";

        public static final String cssSendImageInput = "input[data-uie-name=do-share-image]";

        public static final String cssSendFileButton = "#conversation-input-files";

        public static final String cssSendFileInput = "input[data-uie-name=do-share-file]";

        public static final String cssPingButton = "[data-uie-name='do-ping'], [data-uie-name='do-hot-ping']";

        public static final String cssCallButton = "[data-uie-name='do-call']";

        public static final String cssNobodyLeftModal = "[data-uie-name=modal-call-conversation-empty']";

        public static final String cssVideoCallButton = "[data-uie-name='do-video-call']";

        public static final String cssBroadcastIndicatorVideo = "[data-uie-name='status-self-video']";

        public static final String cssBroadcastIndicatorScreensharing = "[data-uie-name='status-self-screensharing']";

        public static final String cssGIFButton = "[data-uie-name='do-giphy-popover']";

        public static final String cssXbuttonEdit = "[data-uie-name='do-cancel-edit']";

        // call controls
        public static String cssAcceptCallButton = "[data-uie-name='do-call-controls-call-accept']";

        public static String cssAcceptVideoCallButton = "[data-uie-name='do-call-controls-video-accept']";

        public static String cssSilenceIncomingCallButton = "[data-uie-name='do-call-controls-call-ignore']";

        public static final String cssLabelOnOutgoingCall = "#call-controls .cc-label-message";

        public static final Function<String, String> cssUserAvatarById = id -> String
                .format("[user-id='%s'].user-avatar-sm", id);

        public static final String cssConnectedMessageUser = ".message-connected-header";

        public static final String cssConnectedMessageLabel = ".message-connected .label-xs";

        public static final String cssConversationVerifiedIcon = ".conversation-verified";

        public static final String cssTitlebarLabel = ".conversation-titlebar-name-label";

        //Long message warning modal

        public static final String cssLongMessageDialog = "[data-uie-name='modal-too-long-message']";

        public static final String xpathOKButtonOnLongMWarning = "//div[contains(@class, 'modal-too-long-message')" +
                "]//*[@data-uie-name='do-close']";

        public static final String xpathXButtonOnLongMWarning = "//div[contains(@class, 'modal-too-long-message')" +
                "]//div[contains(@class, 'modal-close')]";

        public static final String cssFirstTimeExperienceMessage = "[data-uie-name='start-conversation-hint']";
    }
    
    public static final class PictureFullscreenPage {
        public static final String cssModalDialog = ".modal-show";
        
        public static final String cssModalBackground = "#detail-view.modal";

        public static final String cssXButton = "[data-uie-name='do-close-detail-view']";

        public static final String idBlackBorder = "detail-view";

        public static final String cssFullscreenImage = ".detail-view-image";
    }

    public static final class CollectionPage {
        public static final String cssCloseButton = "#collection .icon-close";

        public static final String cssPictures = "[data-uie-name='collection-section-image'] image-component";

        public static final String cssPictureCollectionSize = "[data-uie-name='collection-section-image'] [data-uie-name='collection-show-all']";

        public static final String cssVideos = "[data-uie-name='collection-section-video'] video-asset";

        public static final String cssVideoCollectionSize = "[data-uie-name='collection-section-video'] [data-uie-name='collection-size']";

        public static final String cssFiles = "[data-uie-name='collection-section-file'] [data-uie-name='file-name']";

        public static final String cssFileCollectionSize = "[data-uie-name='collection-section-file'] [data-uie-name='collection-size']";

        public static final String cssLinkPreviewUrls = "[data-uie-name='collection-section-link'] [data-uie-name='link-preview-url']";

        public static final String cssLinkPreviewTitles = "[data-uie-name='collection-section-link'] [data-uie-name='link-preview-title']";

        public static final String cssLinkPreviewImages = "[data-uie-name='collection-section-link'] image-component";

        public static final String cssLinkPreviewFroms = "[data-uie-name='collection-section-link'] asset-header span";

        public static final String cssLinkCollectionSize = "[data-uie-name='collection-section-link'] [data-uie-name='collection-size']";

        public static final String cssNoItemsPlaceholder = "[data-uie-name='collection-no-items']";

        public static final String cssShowAllPictures = "[data-uie-name='collection-section-image'] [data-uie-name='collection-show-all']";

        public static final String idCollectionDetails = "collection-details";

        public static final String cssBackToCollectionButton = "[data-uie-name='do-collection-details-close']";

        public static final String cssPicturesOnCollectionDetails = "[data-uie-name='collection-section-image'] image-component";

    }

    public static final class ConnectToPage {

        public static final String cssRequestEmailPartial = " .mail";
        public static final String cssRequestMessagePartial = " .message";
        public static final String cssRequestUniqueUsernamePartial = " .connect-request-username";

        public static final Function<String, String> cssRequestById = uid -> String
                .format("[data-uie-name='connect-request'][data-uie-uid='%s']",
                        uid);

        public static final Function<String, String> cssAcceptRequestButtonByUserId = uid -> String
                .format("[data-uie-name='connect-request'][data-uie-uid='%s'] [data-uie-name='do-accept']",
                        uid);

        public static final Function<String, String> cssIgnoreRequestButtonById = uid -> String
                .format("[data-uie-name='connect-request'][data-uie-uid='%s'] [data-uie-name='do-ignore']",
                        uid);

        public static final String cssAllConnectionRequests = "[data-uie-name='connect-request']";

        public static final Function<String, String> cssRequestAvatarByUserId = uid -> String
                .format("[data-uie-name='connect-request'][data-uie-uid='%s'] user-avatar",
                        uid);

        public static final Function<String, String> cssKnownConnectionAvatarsById = uid -> String
                .format("[data-uie-name='connect-request'][data-uie-uid='%s'] user-avatar",
                        uid);

        public static final Function<String, String> cssKnownConnectionOthersTextById = uid -> String
                .format("[data-uie-name='connect-request'][data-uie-uid='%s'] [data-uie-value='others']",
                        uid);

        public static final Function<String, String> cssCommonFriendsById = uid -> String
                .format("[data-uie-name='connect-request'][data-uie-uid='%s'] common-contacts",
                        uid);
    }

    public static final class OutgoingRequestPage {

        public static final String idConversation = "conversation";

        public static final String cssCancelRequestButton = "#" + idConversation + " [data-uie-name='do-cancel-request']";

        public static final String cssUniqueUsernameOutgoing = ".message-connected-username.label-username";

        public static final String cssCommonFriends = ".message-connected-contacts";

    }

    public static final class StartUIPage {

        public static final String xpathRoot = "//div[@id='start-ui']";

        public static final String xpathTopPeopleRoot = "//div[@class='start-ui-list-top-conversations']";

        public static final String cssNameSearchInput = "[data-uie-name='enter-users']";

        public static final String cssOpenOrCreateConversationButton = "[data-uie-name='do-add-create']," +
                "[data-uie-name='do-open']";

        public static final String cssCallButton = "#start-ui-header [data-uie-name='do-audio-call']";

        public static final String cssVideoCallButton = "#start-ui-header [data-uie-name='do-video-call']";

        public static final Function<String, String> xpathSearchResultByName = (
                name) -> String.format(
                "%s//*[@data-uie-name='item-user' and .//*[contains(@class,'search-list-item-content-name') and text()='%s']]",
                xpathRoot, name);

        public static final String xpathSearchResultByNameAndUniqueUsername = xpathRoot + "//*[@data-uie-name='item-user' and "
                + ".//*[contains(@class,'search-list-item-content-name') and text()='%s']"
                + "/parent::*//*[contains(@class,'search-list-item-content-username') and text()='%s']]";

        public static final String xpathTopPeopleName = xpathTopPeopleRoot + "//*[@data-uie-name='item-user' and "
                + ".//*[contains(@class,'search-list-item-content-name') and text()='%s']"
                + "/parent::*//*[not(contains(@class,'search-list-item-content-username') and text()='%s')]]";

        public static final Function<String, String> xpathSearchResultGroupByName = (
                name) -> String.format(
                "%s//*[@data-uie-name='item-group' and .//*[text()='%s']]",
                xpathRoot, name);

        public static final Function<String, String> xpathTopPeopleList = (
                name) -> String.format(
                "%s//*[@data-uie-name='item-user' and .//*[contains(@class,'search-list-item-content-name') and text()='%s']]",
                xpathTopPeopleRoot, name);

        public static final String cssCloseStartUIButton = ".start-ui-header [data-uie-name='do-close']";

        public static final Function<String, String> cssDismissIconByName = (
                name) -> String.format(
                "div[data-uie-value='%s'] span.icon-dismiss", name);

        public static final Function<String, String> cssAddIconByName = (name) -> String
                .format("div[data-uie-value='%s'] span.icon-add", name);

        public static final String classNameStartUIVisible = "start-ui-is-visible";

        public static final String cssBringYourFriendsOrInvitePeopleButton = "#invite-button";

        public static final Function<String, String> xpathSearchPendingResultByName = (
                name) -> String
                .format("%s//*[@data-uie-name='item-user' and .//*[text()='%s'] and .//div[contains(@class,'checkmark " +
                                "icon-check')]]",
                        xpathRoot, name);

        public static final String xpathTopPeople = "//*[@data-uie-name='status-top-people']";

        public static final Function<String, String> cssTopPeopleListByName = (
                name) -> String
                .format(".start-ui-list-top-conversations [data-uie-value='%s'] user-avatar",
                        name);

        public static final String xpathSelectedTopPeopleList = "//user-list[contains('top_users')]"
                + "//*[@data-uie-name='item-user' and .//*[contains(@class,'selected')]]";
        
        public static final String xpathSuggestedContacts = "//*[contains(@class,'start-ui-list-suggestions')]//div[@data-uie-name='item-user']";

        public static final String cssSearchField = "[data-uie-name='enter-users']";

        public static final String cssBringFriendsFromGMailButton = "[data-uie-name='from-gmail']";

        public static final String cssBringFriendsFromContactsButton = "[data-uie-name='from-contacts']";
    }

    public static final class RegistrationPage {

        public static final String cssSwitchToSignInButton = "[data-uie-name='go-sign-in']:not(.disabled)";

        public static final String xpathRootForm = "//form[@id='form-account-register']";
        public static final String cssRootForm = "#form-account-register";

        public static final String cssNameFiled = cssRootForm
                + " [data-uie-name=enter-name]";

        public static final String cssEmailFiled = cssRootForm
                + " [data-uie-name=enter-email]";

        public static final String cssPasswordFiled = cssRootForm
                + " [data-uie-name=enter-password]";

        public static final String cssCreateButton = "[data-uie-name='do-register']";

        public static final String cssVerificationEmail = "#wire-resend";

        public static final String cssPendingEmail = "#wire-pending-resend";

        public static final String cssErrorMarkedEmailField = "#form-account-register " +
                ".input-error[data-uie-name='enter-email']";

        public static final String cssErrorMessages = "#form-account-register [data-uie-name='status-error'] .error";

        public static final String xpathVerifyLaterButton = "//div[@id='posted-later-link']";

        public static final String cssTermsOfUseCheckbox = ".checkbox-terms-of-use span";
    }

    public static final class ContactsUploadPage {

        public static final String xpathRootDiv = "//div[@id='self-upload']";

        public static final String xpathCloseButton = xpathRootDiv
                + "//*[@data-uie-name='do-close']";

        public static final String xpathShowSearchButton = xpathRootDiv
                + "//*[@data-uie-name='go-search']";
    }

    public static final class Common {

        public static final String CONTACT_LIST_ONE_PERSON_WAITING = "1 person waiting";

        public static final String CONTACT_LIST_X_PEOPLE_WAITING = " people waiting";

        public static final String TITLE_ATTRIBUTE_LOCATOR = "title";

        public static final String HREF_ATTRIBUTE_LOCATOR = "href";
    }

    public static final class ProfilePicturePage {

        private static final String xpathRootDiv = "//div[@id='self-upload']";

        public static final String xpathSelectPictureButton = xpathRootDiv
                + "//*[@data-uie-name='do-select-picture']/following-sibling::span";

        public static final String xpathConfirmPictureSelectionButton = xpathRootDiv
                + "//*[@data-uie-name='do-set-picture']";

        public static String cssDropZone = "#self-upload .self-upload-center";
    }

    public static final class VideoCallPage {

        public static final String cssEndVideoCallButton = "[data-uie-name='do-call-controls-video-call-cancel']";

        public static final String cssMuteCallButton = "[data-uie-name='do-call-controls-video-call-mute']";

        public static final String cssMinimizeVideoCallButton = "[data-uie-name='do-call-controls-video-minimize']";

        public static final String cssMaximizeVideoCallButtonOnRemotevideo = "[data-uie-name='do-call-controls-video-maximize']";

        public static final String cssMaximizeVideoCallButton = "[data-uie-name='do-maximize-call']";

        public static final String cssDurationTimer = ".video-timer";

        public static final String xpathMuteCallButtonPressed = "//div[@data-uie-name='do-call-controls-video-call-mute'" +
                " and contains(@class, 'toggled')]";

        public static final String xpathMuteCallButtonNotPressed = "//div[@data-uie-name='do-call-controls-video-call-mute'" +
                " and not(contains(@class, 'toggled'))]";

        public static final String cssVideoPortrait = ".video-mode-portrait";

        public static final String cssCameraButton = ".icon-video";

        public static final String cssCameraButtonPressed = cssCameraButton + ".toggled";

        public static final String cssCameraButtonNotPressed = cssCameraButton + ":not(.toggled)";

        public static final String cssSelfVideo = ".video-element-local";
        
        public static final String cssSelfVideoOff = ".video-element-overlay.icon-video-off";

        public static final String cssMinimizedRemoteVideo = "#video-element-remote";

        public static final String cssMaximizedRemoteVideo = ".video-element-portrait";

        public static final String cssScreenShareButton = ".icon-screensharing";

        public static final String cssLocalScreenShareVideo = ".video-element-local";
    }

    public static final class WarningPage {

        private static final String xpathWarningBarRootDiv = "//div[@id='warnings']";
        private static final String cssWarningBarRootDiv = "div#warnings";
        private static final String xpathWarningModalRootDiv = "//div[@id='modals']";
        private static final String cssWarningModalRootDiv = "div#modals";

        public static final String cssMissingWebRTCSupportWarningBar = cssWarningBarRootDiv
                + " .warning";

        public static final String cssMissingWebRTCSupportWarningBarClose = cssWarningBarRootDiv
                + " .warning-bar-close";

        public static final String cssAnotherCallWarningModal = cssWarningModalRootDiv
                + " .modal-call-second.modal-show";

        public static final String cssAnotherCallWarningModalClose = cssAnotherCallWarningModal
                + " .icon-close";

        public static final String cssFullCallWarningModal = cssWarningModalRootDiv
                + " .modal-call-voice-channel-full.modal-show";
        
        public static final String cssFullConversationWarningModal = cssWarningModalRootDiv
                + " .modal-call-conversation-full.modal-show";

        public static final String cssFullCallWarningModalClose = cssFullCallWarningModal
                + " .icon-close";
        
        public static final String cssFullConversationWarningModalClose = cssFullConversationWarningModal
                + " .icon-close";

        public static final Function<String, String> xpathMissingWebRTCSupportWarningBarLinkByCaption = (
                name) -> String
                .format("%s//div[contains(@class, 'warning-bar-message')]//a[text()='%s']",
                        xpathWarningBarRootDiv, name);

        public static final Function<String, String> xpathAnotherCallWarningModalButtonByCaption = (
                name) -> String
                .format("%s//div[@data-uie-name='modal-call-second' and contains(@class, 'modal-show')]//div[contains" +
                                "(@class," +
                                " 'button') and text()='%s']",
                        xpathWarningModalRootDiv, name);

        public static final Function<String, String> xpathFullCallWarningModalButtonByCaption = (
                name) -> String
                .format("%s//div[@data-uie-name='modal-call-voice-channel-full' and contains(@class, 'modal-show')" +
                                "]//div[contains(@class, 'button') and text()='%s']",
                        xpathWarningModalRootDiv, name);
        
        public static final Function<String, String> xpathFullConversationWarningModalButtonByCaption = (
                name) -> String
                .format("%s//div[@data-uie-name='modal-call-conversation-full' and contains(@class, 'modal-show')" +
                                "]//div[contains(@class, 'button') and text()='%s']",
                        xpathWarningModalRootDiv, name);

        public static final String cssFileTransferLimitWarningModal = ".modal-asset-upload-too-large";
        public static final String cssFileTransferLimitWarningModalButton = cssFileTransferLimitWarningModal + " " +
                "[data-uie-name='do-close']";

        public static final String cssFullHouseWarningModal = cssWarningModalRootDiv
                + " .modal-too-many-members.modal-show";

        public static final String cssFullHouseWarningModalClose = cssFullHouseWarningModal
                + " [data-uie-name='do-close']";
    }

    public static final class PhoneNumberVerificationPage {

        public static final String cssErrorMessage = "#form-verify-code [data-uie-name='status-error']";
    }

    public static final class AddEmailAddressPage {

        public static final String cssErrorMessage = "#form-verify-account [data-uie-name='status-error']";

        public static final String cssErrorMarkedEmailField = "#form-verify-account .input-error[data-uie-name='enter-email']";

        public static final String cssErrorMarkedPasswordField = "#form-verify-account " +
                ".input-error[data-uie-name='enter-password']";
    }

    public static final class PhoneNumberLoginPage {

        public static final String cssErrorMessage = "#login-method-phone [data-uie-name='status-error']";
        
        public static final String cssRememberMe = "#wire-login-phone-remember label";
    }
    
    public static final class PhoneNumberPasswordPage {
        public static final String cssErrorMessage = "#form-verify-phone-password [data-uie-name='status-error']";
        
        public static final String cssPasswordInput = "#wire-verify-password-input";
        
        public static final String cssSignInButton = "#wire-verify-password";
    }

    public static final class HistoryInfoPage {
        public static final String cssConfirmButton = "[data-uie-name='do-history-confirm']";
    }

    public static final class TakeOverScreenPage {
        public static final String idTakeOverScreen = "takeover";

        public static final String cssChooseYourOwnButton = "[data-uie-name='do-takeover-choose']";

        public static final String cssTakeThisOneButton = "[data-uie-name='do-takeover-keep']";

        public static final String cssTakeOverName = "[data-uie-name='takeover-name']";

        public static final String cssTakeOverUniqueUsername = "[data-uie-name='takeover-username']";
    }
}
