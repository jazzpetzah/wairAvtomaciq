package com.wearezeta.auto.common.driver.facebook_ios_driver;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.List;

public abstract class FBFindBy extends MobileBy {
    public FBFindBy() {
    }

    public static By FBAccessibilityId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Must supply a valid accessibility id string");
        } else {
            return new FBFindBy.FBAccessibilityId(id);
        }
    }

    public static class FBAccessibilityId extends By implements Serializable {
        private final String id;

        public FBAccessibilityId(String uiautomatorText) {
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
            return "By.FBAccessibilityId: " + this.id;
        }
    }

    public static By FBClassName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Must supply a valid class xpath string");
        } else {
            return new FBFindBy.FBClassName(name);
        }
    }

    public static class FBClassName extends By implements Serializable {
        private final String name;

        public FBClassName(String uiautomatorText) {
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
            return "By.FBClassName: " + this.name;
        }
    }

    public static By FBXPath(String xpath) {
        if (xpath == null) {
            throw new IllegalArgumentException("Must supply a valid XPath string");
        } else {
            return new FBFindBy.FBClassName(xpath);
        }
    }

    public static class FBXPath extends By implements Serializable {
        private final String xpath;

        public FBXPath(String uiautomatorText) {
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
            return "By.FBClassName: " + this.xpath;
        }
    }

    public static By FBPredicate(String predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("Must supply a valid predicate string");
        } else {
            return new FBFindBy.FBPredicate(predicate);
        }
    }

    public static class FBPredicate extends By implements Serializable {
        private final String predicateText;

        public FBPredicate(String uiautomatorText) {
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
            return "By.FBPredicate: " + this.predicateText;
        }
    }
}
