package com.wearezeta.auto.ios.tools;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class IOSSimulatorPhotoLibHelper {
	private static final String IOS_LIB_PATH_TEMPLATE8 = System.getProperty("user.home") + 
			"/Library/Developer/CoreSimulator/Devices/";	
	
	private static final String IOS_LIB_PATH_TEMPLATE7 = System.getProperty("user.home") + 
			"/Library/Application Support/iPhone Simulator/%s";	

	private static final String MEDIA_PATH_TEMPLATE = "%s/Media";
	public static final String PHOTO_DATA_PATH_TEMPLATE = "%s/PhotoData";
	public static final String PHOTOS_ROOT_TEMPLATE = "%s/DCIM/100APPLE";
	public static final String SIMULATOR_DEVICE_NAME = "iPhone 6";

	public IOSSimulatorPhotoLibHelper() {
		// TODO Auto-generated constructor stub
	}

	private static String GetiOSLibPath(String simulatorVersion) throws Exception {
		String result = "";
		if (simulatorVersion.equals("8.0")) {
			result =  IOS_LIB_PATH_TEMPLATE8;
		}
		else {
			result = String.format(IOS_LIB_PATH_TEMPLATE7, simulatorVersion);
		}
		return result;
	}

	private static String GetMediaPath(String libPath) {
		return String.format(MEDIA_PATH_TEMPLATE, libPath);
	}

	private static String GetPhotoDataPath(String libPath) {
		return String.format(PHOTO_DATA_PATH_TEMPLATE, GetMediaPath(libPath));
	}

	private static String GetPhotosRoot(String libPath) {
		return String.format(PHOTOS_ROOT_TEMPLATE, GetMediaPath(libPath));
	}

	private static void refreshFolder(File pathObj, boolean shouldDoCleanup) throws IOException {
		if (pathObj.exists() && shouldDoCleanup) {
			FileUtils.cleanDirectory(pathObj); 
		}
		pathObj.mkdirs();
	}
	
	private static boolean compareDeviceNameFromPlist(String filePath, String deviceName) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(filePath);
		
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("string");
		
		boolean flag = false;
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the employee element
				Element el = (Element)nl.item(i);
				
				if(el.getTextContent().equals(deviceName)) {
					flag = true;
					break;
				}
			}
		}
		
		return flag;
	}
	
	private static String FindSimultorFolder(String simulatorVersion) throws Exception {
		String libPath = GetiOSLibPath(simulatorVersion);
		
		if (!new File(libPath).exists()) {
			throw new Exception(
					String.format("IOS simulator v. %s has not been found on this system",
					simulatorVersion));
		}
		String result = "";
		
		File photosRootObj = new File(libPath);
	    File[] directoryListing = photosRootObj.listFiles();
	    if (directoryListing != null) {
	    	for (File child : directoryListing) {
	    		if(child.isDirectory()) {
	    			File device = new File(child.getAbsolutePath() + "/device.plist");
	    			if (device.exists()) {
	    				if (compareDeviceNameFromPlist(device.getAbsolutePath(), SIMULATOR_DEVICE_NAME)) {
	    					result = child.getAbsolutePath();
	    				}
	    			}
	    		}
	    	}
	    } 
	    
	    return result;
	}

	public static void CreateSimulatorPhotoLib(final String simulatorVersion, String[] srcPictPaths,
			boolean shouldDoCleanup) throws Exception {
		
		String libPath = FindSimultorFolder(simulatorVersion) + "/data/";
		
		if (!new File(libPath).exists()) {
			throw new Exception(
					String.format("IOS simulator v. %s has not been found on this system",
					simulatorVersion));
		}
		File mediaObj = new File(GetMediaPath(libPath));
		if (mediaObj.exists()) {
			FileUtils.deleteDirectory(mediaObj);
		}
		
		File photosRootObj = new File(libPath);

		for (String picturePath: srcPictPaths) {
			File from = new File(picturePath);
			File to = new File(photosRootObj.getAbsolutePath(), from.getName());
			if (from.isDirectory()) {
				FileUtils.copyDirectory(from, to, false);
			}
			else {
				FileUtils.copyFile(from, to);
			}
		}
	}
}
