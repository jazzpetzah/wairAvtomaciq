package com.wearezeta.auto.web.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppConstants.Browser;

public class WebCommonUtils extends CommonUtils {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(WebCommonUtils.class
			.getSimpleName());

	// workaround for local executions in case sshpass is not in the PATH
	private static final String SSHPASS_PREFIX = "/usr/local/bin/";

	public static String getWebAppBrowserNameFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "browserName");
	}

	public static String getPlatformNameFromConfig(Class<?> c) throws Exception {
		return getValueFromConfig(c, "platformName");
	}

	public static String getHubHostFromConfig(Class<?> c) throws Exception {
		return getValueFromConfig(c, "hubHost");
	}

	public static int getHubPortFromConfig(Class<?> c) throws Exception {
		return Integer.parseInt(getValueFromConfig(c, "hubPort"));
	}

	public static String getExtendedLoggingLevelInConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "extendedLoggingLevel");
	}

	public static String getScriptsTemplatesPath() {
		return String.format("%s/Documents/scripts/",
				System.getProperty("user.home"));
	}

	public static String getPicturesPath() {
		return String.format("%s/Documents", System.getProperty("user.home"));
	}

	public static String getFullPicturePath(String pictureName) {
		return String.format("%s/Documents/%s",
				System.getProperty("user.home"), pictureName);
	}

	public static void putFileOnExecutionNode(String node, String srcPath,
			String dstPath) throws Exception {
		String commandTemplate = SSHPASS_PREFIX
				+ "sshpass -p %s "
				+ "scp -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no "
				+ "%s %s@%s:%s";
		String command = String.format(commandTemplate,
				getJenkinsSuperUserPassword(CommonUtils.class), srcPath,
				getJenkinsSuperUserLogin(CommonUtils.class), node, dstPath);
		WebCommonUtils
				.executeOsXCommand(new String[] { "bash", "-c", command });
	}

	public static void executeCommandOnNode(String node, String cmd)
			throws Exception {
		String commandTemplate = SSHPASS_PREFIX
				+ "sshpass -p %s "
				+ "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no "
				+ "%s@%s %s";
		String command = String.format(commandTemplate,
				getJenkinsSuperUserPassword(CommonUtils.class),
				getJenkinsSuperUserLogin(CommonUtils.class), node, cmd);
		WebCommonUtils
				.executeOsXCommand(new String[] { "bash", "-c", command });
	}

	public static void executeAppleScriptFileOnNode(String node,
			String scriptPath) throws Exception {
		String commandTemplate = SSHPASS_PREFIX
				+ "sshpass -p %s "
				+ "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no "
				+ "%s@%s osascript %s";
		String command = String.format(commandTemplate,
				getJenkinsSuperUserPassword(CommonUtils.class),
				getJenkinsSuperUserLogin(CommonUtils.class), node, scriptPath);
		if (WebCommonUtils.executeOsXCommand(new String[] { "bash", "-c",
				command }) == 255) {
			WebCommonUtils.executeOsXCommand(new String[] { "bash", "-c",
					command });
		}
	}

	private static void formatTextInFileAndSave(InputStream fis,
			String dstFile, Object[] params) throws IOException {
		String script = "";
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String t;
			while ((t = br.readLine()) != null) {
				if (!t.trim().isEmpty()) {
					script += t + "\n";
				}
			}
			script = String.format(script, params);
			File dstFileInstance = new File(dstFile);
			dstFileInstance.getParentFile().mkdirs();
			if (dstFileInstance.exists()) {
				dstFileInstance.delete();
			}
			dstFileInstance.createNewFile();
			PrintWriter out = new PrintWriter(dstFile);
			out.write(script);
			out.close();
		} finally {
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
		}
	}

	public static String getOperaProfileRoot() throws Exception {
		if (WebAppExecutionContext.isCurrentPlatfromWindows()) {
			return String
					.format("C:\\Users\\%s\\AppData\\Roaming\\Opera Software\\Opera Stable\\",
							CommonUtils
									.getJenkinsSuperUserLogin(WebCommonUtils.class));
		} else {
			// Should be Mac OS otherwise ;)
			return String
					.format("/Users/%s/Library/Application Support/Opera Software/Opera Stable/",
							CommonUtils
									.getJenkinsSuperUserLogin(WebCommonUtils.class));
		}
	}

	public static boolean isElementFocused(RemoteWebDriver driver,
			String cssLocator) {
		final String isFocusedScript = "return $(\"" + cssLocator
				+ "\").is(':focus');";
		return (Boolean) driver.executeScript(isFocusedScript);
	}

	public static void setFocusToElement(RemoteWebDriver driver,
			String cssLocator) {
		final String setFocusScript = "$(\"" + cssLocator + "\").focus();";
		driver.executeScript(setFocusScript);
	}

	private static void openNewTabInSafari(String url, String nodeIp)
			throws Exception {
		final ClassLoader classLoader = WebCommonUtils.class.getClassLoader();
		final InputStream scriptStream = classLoader.getResourceAsStream(String
				.format("%s/%s",
						WebAppConstants.Scripts.RESOURCES_SCRIPTS_ROOT,
						WebAppConstants.Scripts.SAFARI_OPEN_TAB_SCRIPT));
		final String srcScriptPath = String.format("%s/%s",
				WebAppConstants.TMP_ROOT,
				WebAppConstants.Scripts.SAFARI_OPEN_TAB_SCRIPT);
		try {
			formatTextInFileAndSave(scriptStream, srcScriptPath,
					new String[] { url });
		} finally {
			if (scriptStream != null) {
				scriptStream.close();
			}
		}
		final String dstScriptPath = srcScriptPath;
		try {
			putFileOnExecutionNode(nodeIp, srcScriptPath, dstScriptPath);
		} finally {
			new File(srcScriptPath).delete();
		}

		executeAppleScriptFileOnNode(nodeIp, dstScriptPath);
	}

	private static Set<String> previousHandles = null;

	/**
	 * Opens a new tab for the given URL
	 * 
	 * http://stackoverflow.com/questions/6421988/webdriver-open-new-tab
	 * https://code.google.com/p/selenium/issues/detail?id=7518
	 * 
	 * @param url
	 *            The URL to
	 * @throws Exception
	 * @throws RuntimeException
	 *             If unable to open tab
	 */
	public static void openUrlInNewTab(RemoteWebDriver driver, String url,
			String nodeIp) throws Exception {
		previousHandles = driver.getWindowHandles();
		if (WebAppExecutionContext.getCurrentBrowser() == Browser.Safari) {
			openNewTabInSafari(url, nodeIp);
		} else {
			String script = "var d=document,a=d.createElement('a');a.target='_blank';a.href='%s';a.innerHTML='.';d.body.appendChild(a);return a";
			Object element = driver.executeScript(String.format(script, url));
			if (element instanceof WebElement) {
				WebElement anchor = (WebElement) element;
				anchor.click();
				driver.executeScript(
						"var a=arguments[0];a.parentNode.removeChild(a);",
						element);
			} else {
				throw new RuntimeException("Unable to open a new tab");
			}
		}
		Set<String> currentHandles = driver.getWindowHandles();
		if (previousHandles.equals(currentHandles)) {
			throw new RuntimeException("Unable to open a new tab");
		}
		currentHandles.removeAll(previousHandles);
		final String newTabHandle = currentHandles.iterator().next();
		driver.switchTo().window(newTabHandle);
	}

	public static void switchToPreviousTab(RemoteWebDriver driver) {
		Set<String> currentHandles = driver.getWindowHandles();
		if (previousHandles.equals(currentHandles)) {
			return;
		}
		currentHandles.retainAll(previousHandles);
		final String oldTabHandle = currentHandles.iterator().next();
		driver.switchTo().window(oldTabHandle);
	}

	/**
	 * Workaround for https://code.google.com/p/selenium/issues/detail?id=4220
	 * 
	 * @param pictureName
	 *            the path to the original picture to be uploaded into
	 *            conversation
	 * @throws Exception
	 */
	public static void sendPictureInSafari(String pictureName, String nodeIp)
			throws Exception {
		final ClassLoader classLoader = WebCommonUtils.class.getClassLoader();
		final InputStream scriptStream = classLoader.getResourceAsStream(String
				.format("%s/%s",
						WebAppConstants.Scripts.RESOURCES_SCRIPTS_ROOT,
						WebAppConstants.Scripts.SAFARI_SEND_PICTURE_SCRIPT));
		final String srcScriptPath = String.format("%s/%s",
				WebAppConstants.TMP_ROOT,
				WebAppConstants.Scripts.SAFARI_SEND_PICTURE_SCRIPT);
		final File srcImage = new File(pictureName);
		assert srcImage.exists() : "There's no image by path "
				+ srcImage.getCanonicalPath() + " on your local file system";
		final File dstImage = new File(String.format("%s/%s",
				WebAppConstants.TMP_ROOT, srcImage.getName()));
		try {
			formatTextInFileAndSave(scriptStream, srcScriptPath, new String[] {
					dstImage.getParent(), dstImage.getName() });
		} finally {
			if (scriptStream != null) {
				scriptStream.close();
			}
		}
		final String dstScriptPath = srcScriptPath;
		try {
			putFileOnExecutionNode(nodeIp, srcImage.getAbsolutePath(),
					dstImage.getAbsolutePath());
			putFileOnExecutionNode(nodeIp, srcScriptPath, dstScriptPath);
		} finally {
			new File(srcScriptPath).delete();
		}

		executeAppleScriptFileOnNode(nodeIp, dstScriptPath);
	}

	public static void loadCustomJavascript(RemoteWebDriver driver,
			String scriptContent) {
		final String[] loaderJS = new String[] {
				"var scriptElt = document.createElement('script');",
				"scriptElt.type = 'text/javascript';",
				"scriptElt.innerHTML = '"
						+ StringEscapeUtils.escapeEcmaScript(scriptContent)
						+ "';", "$('head').append(scriptElt);" };
		driver.executeScript(StringUtils.join(loaderJS, "\n"));
	}

	public static void forceLogoutFromWebapp(RemoteWebDriver driver,
			boolean areExceptionsSilenced) {
		try {
			driver.executeScript("(typeof wire !== 'undefined') && (typeof wire.app !== 'undefined') && wire.app.logout();");
		} catch (Exception e) {
			if (!areExceptionsSilenced) {
				throw e;
			}
		}
	}
}
