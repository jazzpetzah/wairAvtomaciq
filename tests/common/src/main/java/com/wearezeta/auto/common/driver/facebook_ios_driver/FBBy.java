package com.wearezeta.auto.common.driver.facebook_ios_driver;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.List;

public abstract class FBBy extends MobileBy {
    protected FBBy() {
    }

    public static By FBAccessibilityId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Must supply a valid accessibility id string");
        } else {
            return new ByFBAccessibilityId(id);
        }
    }

    public static class ByFBAccessibilityId extends By implements Serializable {
        private final String id;

        public ByFBAccessibilityId(String uiautomatorText) {
            this.id = uiautomatorText;
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            if (context instanceof FindsByFBAccessibilityId) {
                return ((FindsByFBAccessibilityId) context).findElementsByFBAccessibilityId(this.id);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public WebElement findElement(SearchContext context) {
            if (context instanceof FindsByFBAccessibilityId) {
                return ((FindsByFBAccessibilityId) context).findElementByFBAccessibilityId(this.id);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public String toString() {
            return "By.ByFBAccessibilityId: " + this.id;
        }
    }

    public static By FBClassName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Must supply a valid class xpath string");
        } else {
            return new ByFBClassName(name);
        }
    }

    public static class ByFBClassName extends By implements Serializable {
        private final String name;

        public ByFBClassName(String uiautomatorText) {
            this.name = uiautomatorText;
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            if (context instanceof FindsByFBAccessibilityId) {
                return ((FindsByFBClassName) context).findElementsByFBClassName(this.name);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public WebElement findElement(SearchContext context) {
            if (context instanceof FindsByFBClassName) {
                return ((FindsByFBClassName) context).findElementByFBClassName(this.name);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public String toString() {
            return "By.ByFBClassName: " + this.name;
        }
    }

    public static By FBXPath(String xpath) {
        if (xpath == null) {
            throw new IllegalArgumentException("Must supply a valid XPath string");
        } else {
            return new ByFBClassName(xpath);
        }
    }

    public static class ByFBXPath extends By implements Serializable {
        private final String xpath;

        public ByFBXPath(String uiautomatorText) {
            this.xpath = uiautomatorText;
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            if (context instanceof FindsByFBXPath) {
                return ((FindsByFBXPath) context).findElementsByFBXPath(this.xpath);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public WebElement findElement(SearchContext context) {
            if (context instanceof FindsByFBXPath) {
                return ((FindsByFBXPath) context).findElementByFBXPath(this.xpath);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public String toString() {
            return "By.ByFBClassName: " + this.xpath;
        }
    }

    public static By FBPredicate(String predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("Must supply a valid predicate string");
        } else {
            return new ByFBPredicate(predicate);
        }
    }

    public static class ByFBPredicate extends By implements Serializable {
        private final String predicateText;

        public ByFBPredicate(String uiautomatorText) {
            this.predicateText = uiautomatorText;
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            if (context instanceof FindsByFBPredicate) {
                return ((FindsByFBPredicate) context).findElementsByFBPredicate(this.predicateText);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public WebElement findElement(SearchContext context) {
            if (context instanceof FindsByFBPredicate) {
                return ((FindsByFBPredicate) context).findElementByFBPredicate(this.predicateText);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public String toString() {
            return "By.ByFBPredicate: " + this.predicateText;
        }
    }
}
