package com.wearezeta.auto.ios.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


public class IOSSimulatorPhotoLibHelper {
	private static final String IOS_LIB_PATH_TEMPLATE = System.getProperty("user.home") + 
			"/Library/Application Support/iPhone Simulator/%s";	
	private static final String MEDIA_PATH_TEMPLATE = "%s/Media";
	public static final String PHOTO_DATA_PATH_TEMPLATE = "%s/PhotoData";
	public static final String PHOTOS_ROOT_TEMPLATE = "%s/DCIM/100APPLE";

	public IOSSimulatorPhotoLibHelper() {
		// TODO Auto-generated constructor stub
	}

	private static String GetiOSLibPath(String simulatorVersion) throws Exception {
		String resultPath = String.format(IOS_LIB_PATH_TEMPLATE, simulatorVersion);

		return resultPath;
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

	public static void CreateSimulatorPhotoLib(final String simulatorVersion, String[] srcPictPaths,
			boolean shouldDoCleanup) throws Exception {
		String libPath = GetiOSLibPath(simulatorVersion);
		if (!new File(libPath).exists()) {
			throw new Exception(
					String.format("IOS simulator v. %s has not been found on this system",
					simulatorVersion));
		}
		File photosRootObj = new File(GetPhotosRoot(libPath));
		refreshFolder(photosRootObj, shouldDoCleanup);
		File photoDataObj = new File(GetPhotoDataPath(libPath));
		refreshFolder(photoDataObj, shouldDoCleanup);
		for (String picturePath: srcPictPaths) {
			File from = new File(picturePath);
			File to = new File(photosRootObj.getAbsolutePath(), from.getName());
			FileUtils.copyFile(from, to);
		}
	}
}
