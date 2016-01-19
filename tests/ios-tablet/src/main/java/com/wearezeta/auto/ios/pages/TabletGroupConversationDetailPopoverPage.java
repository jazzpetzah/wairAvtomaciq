package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletGroupConversationDetailPopoverPage extends GroupChatInfoPage {
    private final static String AQA_PICTURE_CONTACT = "AQAPICTURECONTACT";
    private final static String AQA_AVATAR_CONTACT = "QAAVATAR";

    private static final By nameConversationMenu = By.name("metaControllerRightButton");

    private static final By nameRenameButtonEllipsisMenu = By.name("RENAME");

    private static final By xpathPopoverAvatarCollectionView = By.xpath(xpathStrMainWindow +
            "]/UIAPopover[1]/UIACollectionView[1]");

    private static final By xpathSilenceButtonEllipsisMenu = By.xpath(xpathStrMainWindow +
            "/UIAPopover[1]/UIAButton[@name='SILENCE']");

    private static final By xpathNotifyButtonEllipsisMenu = By.xpath(xpathStrMainWindow +
            "/UIAPopover[1]/UIAButton[@name='NOTIFY']");

    private static final By xpathGroupConvTotalNumber = By.xpath(
            "/UIAPopover[1]/UIAStaticText[contains(@name,'PEOPLE')]");

    private static final By xpathPopover = By.xpath("//UIAPopover[@visible='true']");

    private static final String NAME_PEOPLE_COUNT_WORD = " PEOPLE";

    public TabletGroupConversationDetailPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private final static double MIN_ACCEPTABLE_IMAGE_VALUE = 0.80;

    public void openConversationMenuOnPopover() throws Exception {
        getElement(nameConversationMenu).click();
    }

    public boolean waitConversationInfoPopoverToClose() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), xpathPopover, 10);
    }

    public void dismissPopover() throws Exception {
        for (int i = 0; i < 3; i++) {
            tapOnTopLeftScreen();
            if (waitConversationInfoPopoverToClose()) {
                break;
            }
        }
    }

    public void selectUserByNameOniPadPopover(String name) throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(), getDriver().findElementByName(name.toUpperCase()));
    }

    public void pressRenameEllipsesButton() throws Exception {
        getElement(nameRenameButtonEllipsisMenu).click();
    }

    public void exitGroupChatPopover() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), getElement(nameConversationMenu), 50, 50);
    }

    public int numberOfPeopleInGroupConversationOniPad() throws Exception {
        // FIXME: Optimize locators
        int result = -1;
        List<WebElement> elements = getElements(xpathGroupConvTotalNumber);
        for (WebElement element : elements) {
            String value = element.getText();
            if (value.contains(NAME_PEOPLE_COUNT_WORD)) {
                result = Integer.parseInt(value.substring(0, value.indexOf((NAME_PEOPLE_COUNT_WORD))));
            }
        }
        return result;
    }

    public boolean areParticipantAvatarCorrectOniPadPopover(String contact) throws Exception {
        String name = "", picture = "";
        if (contact.toLowerCase().contains(AQA_PICTURE_CONTACT.toLowerCase())) {
            name = AQA_PICTURE_CONTACT;
            picture = "pictureAvatariPad.png";
        } else {
            name = AQA_AVATAR_CONTACT;
            picture = "initialAvatariPad.png";
        }
        List<WebElement> participantAvatars = getCurrentParticipantsOnPopover();
        BufferedImage avatarIcon = null;
        boolean flag = false;
        for (WebElement avatar : participantAvatars) {
            avatarIcon = CommonUtils.getElementScreenshot(avatar,
                    this.getDriver()).orElseThrow(IllegalStateException::new);

            List<WebElement> avatarText = avatar.findElements(By
                    .className("UIAStaticText"));

            for (WebElement text : avatarText) {
                String avatarName = text.getAttribute("name");
                if (avatarName.equalsIgnoreCase(name)) {
                    BufferedImage realImage = ImageUtil
                            .readImageFromFile(IOSPage.getImagesPath()
                                    + picture);

                    double score = ImageUtil.getOverlapScore(realImage,
                            avatarIcon, ImageUtil.RESIZE_NORESIZE);
                    if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
                        return false;
                    } else {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    public List<WebElement> getCurrentParticipantsOnPopover() throws Exception {
        // FIXME: Optimize locators
        return getElement(xpathPopoverAvatarCollectionView).findElements(By
                .className("UIACollectionCell"));
    }

    public void pressSilenceEllipsisButton() throws Exception {
        getElement(xpathSilenceButtonEllipsisMenu).click();
    }

    public void pressNotifyEllipsisButton() throws Exception {
        getElement(xpathNotifyButtonEllipsisMenu).click();
    }

}
