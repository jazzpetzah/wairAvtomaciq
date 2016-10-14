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

    public enum KeboardTab {
        RECENT(2), CATEGORY1(3), CATEGORY2(4), CATEGORY3(5), CATEGORY4(6), CATEGORY5(7), CATEGORY6(8),
        CATEGORY7(9), CATEGORY8(10), BACKSPACE(11);

        private int index;

        KeboardTab(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    private final static String strIdEmojiItem = "emoji_keyboard_item";
    private final static String strIdTab = "til__emoji_keyboard";

    private static final By idContainer = By.id(strIdTab);
    private static final Function<Integer, String> xpathEmojiItemByIndex =
            index -> String.format("(//*[@id='%s'])[%d]", strIdEmojiItem, index);
    private static final Function<String, String> xpathEmojiItemByValue =
            value -> String.format("//*[@id='%s' and @value='%s']", strIdEmojiItem, value);
    private static final Function<KeboardTab, String> xpathTabByEmojiTab =
            tab -> String.format("//*[@id='%s']/*[2]/*[%d]", strIdTab, tab.getIndex());

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

    public void tapEmojiKeyboardTab(KeboardTab tab) throws Exception {
        getElement(By.xpath(xpathTabByEmojiTab.apply(tab))).click();
    }
}
