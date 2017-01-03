package com.wearezeta.auto.common.driver.facebook_ios_driver;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.rest.RESTError;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class FBElement implements WebElement, FindsByFBAccessibilityId, FindsByFBClassName,
        FindsByFBPredicate, FindsByFBXPath {
    private FBDriverAPI fbDriverAPI;

    private String uuid;

    public FBElement(String uuid, FBDriverAPI api) {
        this.uuid = uuid;
        this.fbDriverAPI = api;
    }

    @Override
    public void click() {
        try {
            fbDriverAPI.click(this.uuid);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void tap(double x, double y) {
        try {
            fbDriverAPI.tap(this.uuid, x, y);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void doubleTap() {
        try {
            fbDriverAPI.doubleTap(this.uuid);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void touchAndHold(Timedelta duration) {
        try {
            fbDriverAPI.touchAndHold(this.uuid, duration);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void longTap() {
        this.touchAndHold(Timedelta.fromMilliSeconds(DriverUtils.LONG_TAP_DURATION));
    }

    /**
     * https://github.com/facebook/WebDriverAgent/blob/master/WebDriverAgentLib/Commands/FBElementCommands.m
     */
    public void scroll(Optional<FBDriverAPI.ScrollingDirection> direction, Optional<String> toChildNamed,
                       Optional<String> predicateString, Optional<Boolean> toVisible) {
        try {
            fbDriverAPI.scroll(this.uuid, toChildNamed, direction, predicateString, toVisible);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void scrollUp() {
        this.scroll(Optional.of(FBDriverAPI.ScrollingDirection.UP),
                Optional.empty(), Optional.empty(), Optional.empty());
    }

    public void scrollDown() {
        this.scroll(Optional.of(FBDriverAPI.ScrollingDirection.DOWN),
                Optional.empty(), Optional.empty(), Optional.empty());
    }

    public void scrollRight() {
        this.scroll(Optional.of(FBDriverAPI.ScrollingDirection.RIGHT),
                Optional.empty(), Optional.empty(), Optional.empty());
    }

    public void scrollLeft() {
        this.scroll(Optional.of(FBDriverAPI.ScrollingDirection.LEFT),
                Optional.empty(), Optional.empty(), Optional.empty());
    }

    public void scrollTo() {
        this.scroll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(true));
    }

    public void scrollToChild(String childName) {
        this.scroll(Optional.empty(), Optional.of(childName), Optional.empty(), Optional.empty());
    }

    @Override
    public void submit() {
        throw new NotImplementedException(String.format(
                "This method is not implemented for %s instances", this.getClass().getSimpleName()));
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        try {
            fbDriverAPI.sendKeys(charSequences);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public void setValue(CharSequence... charSequences) {
        try {
            fbDriverAPI.setValue(this.uuid, charSequences);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public void clear() {
        try {
            fbDriverAPI.clear(this.uuid);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public String getTagName() {
        try {
            return fbDriverAPI.getTagName(this.uuid);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public String getAttribute(String attrName) {
        try {
            return fbDriverAPI.getAttribute(this.uuid, attrName);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public boolean isSelected() {
        throw new NotImplementedException(String.format(
                "This method is not implemented for %s instances", this.getClass().getSimpleName()));
    }

    public boolean isAccessible() {
        try {
            return fbDriverAPI.isAccessible(this.uuid);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public boolean isEnabled() {
        try {
            return fbDriverAPI.isEnabled(this.uuid);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public String getText() {
        try {
            return fbDriverAPI.getText(this.uuid);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<WebElement> findElements(By by) {
        return by.findElements(this);
    }

    @Override
    public WebElement findElement(By by) {
        return by.findElement(this);
    }

    @Override
    public boolean isDisplayed() {
        try {
            return fbDriverAPI.getIsDisplayed(this.uuid);
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public Point getLocation() {
        try {
            final JSONObject rect = new JSONObject(fbDriverAPI.getRect(this.uuid));
            // TODO: Adjust the interface after driver update
            if (rect.has("origin")) {
                return new Point(rect.getJSONObject("origin").getInt("x"),
                        rect.getJSONObject("origin").getInt("y"));
            } else {
                return new Point(rect.getInt("x"), rect.getInt("y"));
            }
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public Dimension getSize() {
        try {
            final JSONObject rect = new JSONObject(fbDriverAPI.getRect(this.uuid));
            // TODO: Adjust the interface after driver update
            if (rect.has("size")) {
                return new Dimension(rect.getJSONObject("size").getInt("width"),
                        rect.getJSONObject("size").getInt("height"));
            } else {
                return new Dimension(rect.getInt("width"), rect.getInt("height"));
            }
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public Dimension getWindowSize() {
        try {
            return apiStringToDimension(fbDriverAPI.getWindowSize(this.uuid));
        } catch (RESTError | FBDriverAPI.StatusNotZeroError e) {
            throw new WebDriverException(e);
        }
    }

    public static Dimension apiStringToDimension(String s) {
        final JSONObject rect = new JSONObject(s);
        return new Dimension(rect.getInt("width"), rect.getInt("height"));
    }

    @Override
    public String getCssValue(String s) {
        throw new NotImplementedException(String.format(
                "This method is not implemented for %s instances", this.getClass().getSimpleName()));
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        throw new NotImplementedException(String.format(
                "This method is not implemented for %s instances", this.getClass().getSimpleName()));
    }

    @Override
    public FBElement findElementByFBAccessibilityId(String value) {
        try {
            return fbDriverAPI.findChildElementByFBAccessibilityId(this.uuid, value)
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Cannot find nested %s using accessibility id '%s'",
                                    getClass().getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBAccessibilityId(String value) {
        try {
            return fbDriverAPI.findChildElementsByFBAccessibilityId(this.uuid, value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBClassName(String value) {
        try {
            return fbDriverAPI.findChildElementByFBClassName(this.uuid, value)
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Cannot find nested %s using class name '%s'",
                                    getClass().getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBClassName(String value) {
        try {
            return fbDriverAPI.findChildElementsByFBClassName(this.uuid, value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBPredicate(String value) {
        try {
            return fbDriverAPI.findChildElementByFBPredicate(this.uuid, value)
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Cannot find nested %s using predicate '%s'",
                                    getClass().getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBPredicate(String value) {
        try {
            return fbDriverAPI.findChildElementsByFBPredicate(this.uuid, value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public FBElement findElementByFBXPath(String value) {
        try {
            return fbDriverAPI.findChildElementByFBXPath(this.uuid, value)
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Cannot find nested %s using XPath '%s'",
                                    getClass().getSimpleName(), value)));
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    @Override
    public List<FBElement> findElementsByFBXPath(String value) {
        try {
            return fbDriverAPI.findChildElementsByFBXPath(this.uuid, value);
        } catch (RESTError e) {
            throw new WebDriverException(e);
        }
    }

    public Rectangle getRect() {
        final JSONObject wdRect = new JSONObject(getAttribute("wdRect"));
        return new Rectangle((int) wdRect.getDouble("x"),
                (int) wdRect.getDouble("y"),
                (int) wdRect.getDouble("width"),
                (int) wdRect.getDouble("height"));
    }
}
