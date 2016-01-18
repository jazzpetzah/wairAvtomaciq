package com.wearezeta.auto.ios.tools;

import java.io.*;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

import static com.wearezeta.auto.common.CommonUtils.getIOSToolsRoot;

public class IOSSimulatorHelper {
    public static final int SIMULATOR_INTERACTION_TIMEOUT = 20; //seconds

    private static final String IOS_LIB_PATH_TEMPLATE8 = System
            .getProperty("user.home")
            + "/Library/Developer/CoreSimulator/Devices/";

    private static final String IOS_LIB_PATH_TEMPLATE7 = System
            .getProperty("user.home")
            + "/Library/Application Support/iPhone Simulator/%s";

    private static final String MEDIA_PATH_TEMPLATE = "%s/Media";
    public static final String SIMCTL = "/Applications/Xcode.app/Contents/Developer/usr/bin/simctl";

    private static final Logger log = ZetaLogger
            .getLog(IOSSimulatorHelper.class.getSimpleName());

    public IOSSimulatorHelper() {
        // TODO Auto-generated constructor stub
    }

    private static String getiOSLibPath(String simulatorVersion) throws Exception {
        if (simulatorVersion.equals("8.x")) {
            return IOS_LIB_PATH_TEMPLATE8;
        } else {
            return String.format(IOS_LIB_PATH_TEMPLATE7, simulatorVersion);
        }
    }

    private static String getMediaPath(String libPath) {
        return String.format(MEDIA_PATH_TEMPLATE, libPath);
    }

    private static boolean compareDeviceNameFromPlist(String filePath,
                                                      String deviceName) throws SAXException, IOException,
            ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(filePath);

        Element docEle = dom.getDocumentElement();
        NodeList nl = docEle.getElementsByTagName("string");

        boolean flag = false;
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                // get the employee element
                Element el = (Element) nl.item(i);

                if (el.getTextContent().equals(deviceName)) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    private static String findSimultorFolder(String simulatorVersion)
            throws Exception {
        String deviceName = CommonUtils.getDeviceName(CommonUtils.class);
        String libPath = getiOSLibPath(simulatorVersion);

        if (!new File(libPath).exists()) {
            throw new Exception(String.format(
                    "IOS simulator v. %s has not been found on this system (lib path: " + libPath + ")",
                    simulatorVersion));
        }
        String result = "";

        File photosRootObj = new File(libPath);
        File[] directoryListing = photosRootObj.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isDirectory()) {
                    File device = new File(child.getAbsolutePath()
                            + "/device.plist");
                    if (device.exists()) {
                        try {
                            if (compareDeviceNameFromPlist(
                                    device.getAbsolutePath(),
                                    deviceName)) {
                                result = child.getAbsolutePath();
                            }
                        } catch (Exception ex) {
                            log.error(ex.getStackTrace());
                        }
                    }
                }
            }
        }

        return result;
    }

    //current users available : vb003 (vova+vb003@wire.com), Amelia(+49 170 502 78 82)
    public static void createSimulatorAddressBook(final String simulatorVersion, final String addressBookPath) throws Exception {
        String simPath = findSimultorFolder(simulatorVersion);
        String libPath = simPath + "/data/Library/";

        File rootObj = new File(libPath);

        File from = new File(addressBookPath);
        File to = new File(rootObj.getAbsolutePath(),
                from.getName());
        if (from.isDirectory()) {
            FileUtils.copyDirectory(from, to, false);
        } else {
            FileUtils.copyFile(from, to);
        }
    }

    public static void createSimulatorPhotoLib(final String simulatorVersion,
                                               String[] srcPictPaths, boolean useSimCtl) throws Exception {
        String simPath = findSimultorFolder(simulatorVersion);
        String libPath = simPath + "/data/";

        if (!new File(libPath).exists()) {
            log.error(String.format(
                    "IOS simulator v. %s has not been found on this system",
                    simulatorVersion));
            return;
        }
        File mediaObj = new File(getMediaPath(libPath));
        if (mediaObj.exists()) {
            if (useSimCtl) {
                CommonUtils.executeOsXCommand(new String[]{"/bin/bash", "-c",
                        SIMCTL + " erase " + new File(simPath).getName()});
            }
            FileUtils.deleteDirectory(mediaObj);
        }

        File photosRootObj = new File(libPath);

        for (String picturePath : srcPictPaths) {
            if (useSimCtl) {
                CommonUtils.executeOsXCommand(new String[]{"/bin/bash", "-c",
                        SIMCTL + " boot " + new File(simPath).getName()});
                CommonUtils.executeOsXCommand(new String[]{
                        "/bin/bash",
                        "-c",
                        SIMCTL + " addphoto " + new File(simPath).getName()
                                + " " + picturePath});
                CommonUtils.executeOsXCommand(new String[]{"/bin/bash", "-c",
                        SIMCTL + " shutdown " + new File(simPath).getName()});
            } else {
                File from = new File(picturePath);
                File to = new File(photosRootObj.getAbsolutePath(),
                        from.getName());
                if (from.isDirectory()) {
                    FileUtils.copyDirectory(from, to, false);
                } else {
                    FileUtils.copyFile(from, to);
                }
            }
        }
    }

    private static final int MAX_SWIPE_TIME_SECONDS = 40;

    private static final String SWIPE_SCRIPT_NAME = "swipeInWindow.py";

    public static void swipeDown() throws Exception {
        CommonUtils.executeUIShellScript(new String[] {
                String.format("/usr/bin/python '%s/%s' 0.8 0.25 0.8 0.85",
                        getIOSToolsRoot(IOSSimulatorHelper.class), SWIPE_SCRIPT_NAME)
        }).get(MAX_SWIPE_TIME_SECONDS, TimeUnit.SECONDS);
    }

    public static void swipeRight() throws Exception {
        CommonUtils.executeUIShellScript(new String[] {
                String.format("/usr/bin/python '%s/%s' 0.2 0.8 0.9 0.8",
                        getIOSToolsRoot(IOSSimulatorHelper.class), SWIPE_SCRIPT_NAME)
        }).get(MAX_SWIPE_TIME_SECONDS, TimeUnit.SECONDS);
    }
}
