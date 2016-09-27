package com.wearezeta.auto.android.pages;


import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class EmojiKeyboardOverlayPage extends AndroidPage {
    public EmojiKeyboardOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private final static String strIdEmojiItem = "emoji_keyboard_item";

    private static final By idContainer = By.id("ll__emoji_keyboard__emoji_container");
    private static final Function<Integer, String> xpathEmojiItemByIndex =
            index -> String.format("(//*[@id='%s'])[%d]", strIdEmojiItem, index);
    private static final Function<String, String> xpathEmojiItemByValue =
            value -> String.format("//*[@id='%s' and @value='%s']", strIdEmojiItem, value);

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idContainer);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idContainer);
    }

    public void tapEmojiByIndex(int index) throws Exception {
        getElement(By.xpath(xpathEmojiItemByIndex.apply(index))).click();
    }

    public void tapEmojiByValue(String value) throws Exception {
        getElement(By.xpath(xpathEmojiItemByValue.apply(value))).click();
    }


}
