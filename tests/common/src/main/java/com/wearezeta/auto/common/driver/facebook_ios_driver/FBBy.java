package com.wearezeta.auto.common.driver.facebook_ios_driver;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.util.List;

public abstract class FBBy extends MobileBy {

    protected FBBy(String locatorString) {
        super(locatorString);
    }

    public static By AccessibilityId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Must supply a valid accessibility id string");
        } else {
            return new ByAccessibilityId(id);
        }
    }

    public static class ByAccessibilityId extends By implements Serializable {
        private final String id;

        public ByAccessibilityId(String uiautomatorText) {
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
            return "FBBy.AccessibilityId: " + this.id;
        }
    }

    public static By className(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Must supply a valid class xpath string");
        } else {
            return new ByClassName(name);
        }
    }

    public static class ByClassName extends By implements Serializable {
        private final String name;

        public ByClassName(String uiautomatorText) {
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
            return "FBBy.className: " + this.name;
        }
    }

    public static By xpath(String xpath) {
        if (xpath == null) {
            throw new IllegalArgumentException("Must supply a valid XPath string");
        } else {
            return new ByXPath(xpath);
        }
    }

    public static class ByXPath extends By implements Serializable {
        private final String xpath;

        public ByXPath(String uiautomatorText) {
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
            return "FBBy.xpath: " + this.xpath;
        }
    }

    public static By predicate(String predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("Must supply a valid predicate string");
        } else {
            return new ByPredicate(predicate);
        }
    }

    public static class ByPredicate extends By implements Serializable {
        private final String predicateText;

        public ByPredicate(String uiautomatorText) {
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
            return "FBBy.Predicate: " + this.predicateText;
        }
    }

    public static By classChain(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Must supply a valid class chain string");
        } else {
            return new ByClassChain(value);
        }
    }

    public static class ByClassChain extends By implements Serializable {
        private final String query;

        public ByClassChain(String query) {
            this.query = query;
        }

        @Override
        public List<WebElement> findElements(SearchContext context) {
            if (context instanceof FindsByFBClassChain) {
                return ((FindsByFBClassChain) context).findElementsByFBClassChain(this.query);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public WebElement findElement(SearchContext context) {
            if (context instanceof FindsByFBClassChain) {
                return ((FindsByFBClassChain) context).findElementByFBClassChain(this.query);
            } else {
                throw new IllegalArgumentException(String.format("%s context is not supported for element search",
                        context.getClass().getSimpleName()));
            }
        }

        @Override
        public String toString() {
            return "FBBy.ClassChain: " + this.query;
        }
    }
}
