package com.wearezeta.auto.web.locators;

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

        public static final String cssRememberMe = "#remember-me label";

        public static final String cssForgotPassword = "[data-uie-name='go-forgot-password']";
    }

    public static final class ContactListPage {

        public static final String xpathParentContactListItem = "//div[@id='conversation-list']";
        public static final String xpathParentArchiveListItem = "//div[@id='archive']";
        public static final String cssParentContactListItem = "#conversation-list";
        public static final String cssParentArchiveListItem = "#archive";

        public static final String cssIncomingPendingConvoItem = cssParentContactListItem
                + " [data-uie-name='item-pending-requests']";

        public static final String xpathOpenArchivedConvosButton = "//*[@data-uie-name='go-archive']";

        public static final Function<String, String> xpathListItemRootWithControlsByName = name -> String
                .format("//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/following-sibling::div[contains" +
                                "(@class, 'controls')]",
                        name);

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

        public static final Function<String, String> cssOptionsButtonByContactName = (
                name) -> String
                .format("%s div[data-uie-name='item-conversation'][data-uie-value='%s']+ div span[data-uie-name='go-options']",
                        cssParentContactListItem, name,
                        cssParentContactListItem, name);

        public static final Function<String, String> cssArchiveListEntryByName = (
                name) -> String
                .format("%s div[data-uie-name='item-conversation-archived'][data-uie-value='%s']",
                        cssParentArchiveListItem, name);

        public static final String cssSelfProfileButton = "[data-uie-name='go-self-profile']";

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
                .format("//*[contains(@class, 'conversation-list-item') and div[@data-uie-value='%s']]//*[local-name() = " +
                                "'svg' and @data-uie-name='status-unread']",
                        name);

        public static final Function<String, String> xpathJoinedGroupCallNotificationByConversationName = (
                name) -> String
                .format("//*[contains(@class, 'conversation-list-item') and " +
                                "div[@data-uie-value='%s']]//*[@data-uie-name='status-call' and " +
                                "@data-uie-value='participating-in-group-call']",
                        name);

        public static final Function<String, String> xpathUnjoinedGroupCallNotificationByConversationName = (
                name) -> String
                .format("//*[contains(@class, 'conversation-list-item') and " +
                                "div[@data-uie-value='%s']]//*[@data-uie-name='status-call' and " +
                                "@data-uie-value='not-participating-in-group-call']",
                        name);

        public static final Function<String, String> xpathPingIconByContactName = (
                name) -> String
                .format("//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/parent::"
                                + "*//*[@data-uie-name='status-unread' and contains(@class, 'icon-ping')]",
                        name);

        public static final Function<String, String> xpathUnreadDotByContactName = (
                name) -> String
                .format("//*[@data-uie-name='item-conversation' and " +
                                "@data-uie-value='%s']/..//*[@data-uie-name='status-unread']",
                        name);

        public static final Function<String, String> xpathMuteIconByContactName = (
                name) -> String.format(
                "//*[@data-uie-name='item-conversation' and @data-uie-value='%s']/following::"
                        + "*[@data-uie-name='status-silence']", name);

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
        public static final String cssBadge = ".conversation-list-settings-badge";
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
                                + "*/parent::*//*[@data-uie-name='do-video-call']",
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

        public static final Function<String, String> cssAvatarInCallControlsByUserId = (id) -> String.format(".conversation-list-call-controls-row-participants [user-id='%s']", id);
    }

    public static final class SettingsPage {

        public static final String xpathSettingsDialogRoot = "//div[@id='self-settings' and contains(@class, 'modal-show')]";

        public static final String cssSettingsCloseButton = "#self-settings [data-uie-name='do-close']";

        public static final String cssSoundAlertsLevel = "[data-uie-name=enter-sound-alerts]";

        public static final String cssDeviceLabels = ".self-settings-device-list device-card .label-xs";

        public static final Function<String, String> xpathDeviceLabel = (name) -> String
                .format("//device-card//span[contains(text(),'%s')]", name);

        public static final String cssCurrentDevice = "[data-uie-name='device-card-info-current']";

        public static final String cssDeleteAccountButton = "[data-uie-name='go-delete-account']";
        public static final String cssCancelDeleteAccountButton = "[data-uie-name='do-delete-cancel']";
        public static final String cssConfirmDeleteAccountButton = "[data-uie-name='do-delete-account']";
        public static final String cssConfirmText = "[data-uie-name='delete-confirm-text']";
        public static final String cssSentText = "[data-uie-name='delete-sent']";
        public static final String cssImportButton = "[data-uie-name='do-share-contacts']";
    }

    public static final class SelfProfilePage {

        public static final String cssGearButton = "[data-uie-name='do-settings-menu']";

        public static final String xpathGearMenuRoot = "//div[@id='setting-bubble' and contains(@class, 'bubble-show')]";

        public static final Function<String, String> xpathGearMenuItemByName = (
                name) -> String.format("%s//li[text()='%s']",
                xpathGearMenuRoot, name);

        public static final String xpathSelfUserName = "//*[@data-uie-name='enter-name']/span";

        public static final String xpathSelfUserNameInput = "//*[@data-uie-name='enter-name']/textarea";

        public static final String classNameSelfUserMail = "self-profile-mail";

        private static final String xpathAccentColorPicker = "//*[@data-uie-name='enter-accent-color']";

        public static final String xpathAccentColorPickerChildren = xpathAccentColorPicker
                + "/div";

        public static final Function<Integer, String> xpathAccentColorDivById = (
                id) -> String.format("%s[%s]", xpathAccentColorPickerChildren,
                id);

        public static final String xpathCurrentAccentColorCircleDiv = xpathAccentColorPicker
                + "/div[contains(@class, 'selected')]/div[contains(@class,'circle')]";

        public static final String xpathNameSelfUserMail = "//*[@data-uie-name='enter-email']";

        public static final String xpathNameSelfUserPhoneNumber = "//*[@data-uie-name='enter-phone']";

        public static final String xpathCameraButton = "//*[@data-uie-name='go-profile-picture-selection']";

        public static final String cssSelectPicture = "[data-uie-name='do-select-picture']";

        public static final String xpathBackgroundAvatarAccentColor = "//div[contains(@class, 'background-accent bg-theme')]";
    }

    public static final class ConversationPage {

        // content
        public static final String idConversation = "conversation";
        
        public static final String cssConversation = ".messages-wrap";

        public static final String idMessageList = "message-list";

        public static final String cssUserAvatar = ".user-avatar and .pending";
        
        public static final String cssCancelRequestButton = "#"+idConversation+" [data-uie-name='do-cancel-request']";

        // messages (including images, text, missed call notifications, pings)
        public static final String cssMessage = "[data-uie-name='item-message']";

        // message header contains action description
        public static String cssMessageHeader = cssMessage + " .message-header";

        public static String cssTextMessage = cssMessage + " .text";
        
        public static final String cssMessages = "[data-uie-name='item-message']";

        public static final Function<String, String> cssMessagesById = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s']", text);

        public static final Function<String, String> cssDeleteButtonByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] [data-uie-name='do-message-delete']", text);

        public static final String cssDoDelete = "[data-uie-name='do-delete']";

        // TODO: replace the bottom css with this, when implemented by developers
        //public static final Function<String, String> cssResetSessionByMessageId = text -> String
        //        .format("[data-uie-name='item-message'][data-uie-uid='%s'] [data-uie-name='do-reset-session']", text);

        public static final Function<String, String> cssResetSessionByMessageId = text -> String
                .format("[data-uie-name='item-message'][data-uie-uid='%s'] .message-header-decrypt-reset-session span", text);

        public static final String cssCloseResetSessionDialog = "[data-uie-name='modal-session-reset'] [data-uie-name='do-close']";

        public static final String cssLastMessage = "[data-uie-name='item-message']:last-child";

        public static final String cssSecondLastMessage = "[data-uie-name='item-message']:nth-last-child(2)";

        public static final String cssLastTextMessage = cssLastMessage
                + " .text";

        public static final String cssSecondLastTextMessage = cssSecondLastMessage
                + " .text";

        public static final String cssFirstAction = cssMessage + " .action";

        public static final String cssLastAction = cssLastMessage + " .action";

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

        // images

        public static final String cssFirstImage = "[data-uie-name='go-image-detail'][data-uie-visible='true']:not(.image-loading)";

        public static final String cssImageEntries = "[data-uie-name='go-image-detail'][data-uie-visible='true']";

        public static final String cssLoadingImageEntries = "[data-uie-name='go-image-detail'][data-uie-visible='true'].image-loading";

        public static final String cssMessageAmount = "[data-uie-name='item-message']";

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

        // input area (text input + buttons)
        // This is needed for IE workaround
        public static final String classNameShowParticipantsButton = "show-participants";

        public static final String cssShowParticipantsButton = "[data-uie-name='do-participants']";

        public static final String idConversationInput = "conversation-input-text";

        public static final String cssRightControlsPanel = "div.controls-right";

        public static final String cssSendImageInput = "input[data-uie-name=do-share-image]";

        public static final String cssSendFileButton = "#conversation-input-files";

        public static final String cssSendFileInput = "input[data-uie-name=do-share-file]";

        public static final String cssPingButton = "[data-uie-name='do-ping'], [data-uie-name='do-hot-ping']";

        public static final String cssCallButton = "[data-uie-name='do-call']";

        public static final String cssNobodyLeftModal = "[data-uie-name=modal-call-conversation-empty']";

        public static final String cssVideoCallButton = "[data-uie-name='do-video-call']";

        public static final String cssGIFButton = "[data-uie-name='do-giphy-popover']";

        // call controls
        public static String cssAcceptCallButton = "[data-uie-name='do-call-controls-call-accept']";

        public static String cssAcceptVideoCallButton = "[data-uie-name='do-call-controls-video-accept']";

        public static String cssSilenceIncomingCallButton = "[data-uie-name='do-call-controls-call-ignore']";

        public static final String cssLabelOnOutgoingCall = "#call-controls .cc-label-message";

        // image fullscreen
        public static final String cssModalDialog = ".modal-show";

        public static final String xpathXButton = "//div[contains(@class, 'detail-view-close-button')" +
                "]//*[@data-uie-name='do-close-detail-view']";

        public static final String idBlackBorder = "detail-view";

        public static final String cssFullscreenImage = ".detail-view-image";

        public static final Function<String, String> cssUserAvatarById = id -> String
                .format("[user-id='%s'] .user-avatar-border", id);

        public static final String cssConnectedMessageUser = ".message-connected-header";

        public static final String cssConnectedMessageLabel = ".message-connected .label-xs";

        public static final String cssConversationVerifiedIcon = ".conversation-verified";

        public static final String cssTitlebarLabel = ".conversation-titlebar-name-label";

        //Long message warning modal

        public static final String cssLongMessageDialog = "[data-uie-name='modal-too-long-message']";

        public static final String cssOKButtonOnLongMWarning = "toadd";

        public static final String xpathXButtonOnLongMWarning = "//div[contains(@class, 'detail-view-close-button')" +
                "]//*[@data-uie-name='do-close-detail-view']";
    }

    public static final class ConnectToPage {

        public static final String cssRequestEmailPartial = " .mail";
        public static final String cssRequestMessagePartial = " .message";

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
                .format("[data-uie-name='connect-request'][data-uie-uid='%s'] .image",
                        uid);

        public static final Function<String, String> cssKnownConnectionAvatarsById = uid -> String
                .format("[data-uie-name='connect-request'][data-uie-uid='%s'] user-avatar",
                        uid);

        public static final Function<String, String> cssKnownConnectionOthersTextById = uid -> String
                .format("[data-uie-name='connect-request'][data-uie-uid='%s'] [data-uie-value='others']",
                        uid);
    }

    public static final class StartUIPage {

        public static final String xpathRoot = "//div[@id='start-ui']";

        public static final String cssNameSearchInput = "[data-uie-name='enter-users']";

        public static final String cssOpenOrCreateConversationButton = "[data-uie-name='do-add-create']," +
                "[data-uie-name='do-open']";

        public static final String cssCallButton = "#start-ui-header [data-uie-name='do-audio-call']";

        public static final String cssVideoCallButton = "#start-ui-header [data-uie-name='do-video-call']";

        public static final Function<String, String> xpathSearchResultByName = (
                name) -> String.format(
                "%s//*[@data-uie-name='item-user' and .//*[text()='%s']]",
                xpathRoot, name);

        public static final Function<String, String> xpathSearchResultGroupByName = (
                name) -> String.format(
                "%s//*[@data-uie-name='item-group' and .//*[text()='%s']]",
                xpathRoot, name);

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

        public static final String cssBringFriendsFromGMailButton = ".start-ui-import-buttons";

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

    public static final class SelfPictureUploadPage {

        public static final String xpathRootDiv = "//div[@id='self-upload']";

        public static final String cssKeepPictureButton = "[data-uie-name='do-keep-picture']";

        public static final String cssChooseYourOwnInput = "[data-uie-name=do-select-picture]";

        public static final String xpathConfirmPictureSelectionButton = xpathRootDiv
                + "//*[@data-uie-name='do-set-picture']";

        public static final String xpathNextCarouselImageBtn = xpathRootDiv
                + "//div[contains(@class, 'carousel-arrows')]//span[contains(@class, 'carousel-arrow-right')]";

        public static final String xpathPreviousCarouselImageBtn = xpathRootDiv
                + "//div[contains(@class, 'carousel-arrows')]//span[contains(@class, 'carousel-arrow-left')]";
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

        public static final String cssDurationTimer = ".video-timer";

        public static final String xpathMuteCallButtonPressed = "//div[@data-uie-name='do-call-controls-video-call-mute'" +
                " and contains(@class, 'toggled')]";

        public static final String xpathMuteCallButtonNotPressed = "//div[@data-uie-name='do-call-controls-video-call-mute'" +
                " and not(contains(@class, 'toggled'))]";
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
    }

    public static final class PhoneNumberVerificationPage {

        public static final String cssErrorMessage = "#form-verify-code [data-uie-name='status-error']";
    }

    public static final class AddEmailAddressPage {

        public static final String cssErrorMessage = "#form-verify-email [data-uie-name='status-error']";

        public static final String cssSkipForNowButton = "[data-uie-name='go-add-later']";

        public static final String cssErrorMarkedEmailField = "#form-verify-email .input-error[data-uie-name='enter-email']";

        public static final String cssErrorMarkedPasswordField = "#form-verify-email " +
                ".input-error[data-uie-name='enter-password']";
    }

    public static final class PhoneNumberLoginPage {

        public static final String cssErrorMessage = "#login-method-phone [data-uie-name='status-error']";
    }
}
