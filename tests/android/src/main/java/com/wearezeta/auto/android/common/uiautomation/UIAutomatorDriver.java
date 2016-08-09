package com.wearezeta.auto.android.common.uiautomation;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIAutomatorDriver {
    private static final Logger log = ZetaLogger.getLog(UIAutomatorDriver.class.getSimpleName());

    private static final String DUMP_LOCATION = "/sdcard/view.xml";
    private static final int DEFAULT_TIMEOUT = 7; // seconds

    private DocumentBuilder documentBuilder;

    public UIAutomatorDriver() {
        final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        try {
            this.documentBuilder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }
    }

    public static String getUIDump() throws Exception {
        AndroidCommonUtils.getAdbOutput(String.format("shell uiautomator dump %s", DUMP_LOCATION));
        return AndroidCommonUtils.getAdbOutput(String.format("shell cat %s", DUMP_LOCATION));
    }

    private NodeList matchXPath(String xpathExpr, String document) throws Exception {
        final Document doc = documentBuilder.parse(new InputSource(new StringReader(document)));
        final XPathFactory factory = XPathFactory.newInstance();
        final XPath xpath = factory.newXPath();
        final XPathExpression expr = xpath.compile(xpathExpr);
        return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    }

    public Optional<Node> getElementIfPresent(String xpath, int timeoutSeconds) throws Exception {
        final long millisStarted = System.currentTimeMillis();
        String xml;
        do {
            xml = getUIDump();
            final NodeList nodes = matchXPath(xpath, xml);
            if (nodes.getLength() > 0) {
                return Optional.of(nodes.item(0));
            }
        } while (System.currentTimeMillis() - millisStarted <= timeoutSeconds * 1000);
        log.debug(xml);
        return Optional.empty();
    }

    public Optional<Node> getElementIfPresent(String xpath) throws Exception {
        return getElementIfPresent(xpath, DEFAULT_TIMEOUT);
    }

    public boolean waitUntilElementNotPresent(String xpath, int timeoutSeconds) throws Exception {
        final long millisStarted = System.currentTimeMillis();
        String xml;
        do {
            xml = getUIDump();
            final NodeList nodes = matchXPath(xpath, xml);
            if (nodes.getLength() == 0) {
                return true;
            }
        } while (System.currentTimeMillis() - millisStarted <= timeoutSeconds * 1000);
        log.debug(xml);
        return false;
    }

    public boolean waitUntilElementNotPresent(String xpath) throws Exception {
        return waitUntilElementNotPresent(xpath, DEFAULT_TIMEOUT);
    }

    public boolean waitUntilElementPresent(String xpath, int timeoutSeconds) throws Exception {
        return getElementIfPresent(xpath, timeoutSeconds).isPresent();
    }

    public void clickElement(String xpath) throws Exception {
        final Node dstNode = getElementIfPresent(xpath, DEFAULT_TIMEOUT).orElseThrow(() ->
                new IllegalStateException(String.format("There is no element matching xpath %s in the current documents",
                        xpath))
        );
        clickElement(dstNode);
    }

    /**
     * This method can only be used externally together with getElementIfPresent one
     *
     */
    public void clickElement(Node node) throws Exception {
        final NamedNodeMap attributes = node.getAttributes();
        final Node boundsAttr = attributes.getNamedItem("bounds");
        if (boundsAttr == null) {
            throw new IllegalStateException(String.format("bounds attribute is not present for the node %s",
                    node.toString()));
        }
        final String bounds = boundsAttr.getNodeValue();
        final Pattern pattern = Pattern.compile("\\[(\\d+),(\\d+)\\]\\[(\\d+),(\\d+)\\]");
        final Matcher matcher = pattern.matcher(bounds);
        if (matcher.find()) {
            final int tapX = Integer.parseInt(matcher.group(1)) +
                    (Integer.parseInt(matcher.group(3)) - Integer.parseInt(matcher.group(1))) / 2;
            final int tapY = Integer.parseInt(matcher.group(2)) +
                    (Integer.parseInt(matcher.group(4)) - Integer.parseInt(matcher.group(2))) / 2;
            AndroidCommonUtils.executeAdb(String.format("shell input tap %s %s", tapX, tapY));
        }
    }
}
