package com.wearezeta.auto.web.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

@SuppressWarnings("deprecation")
public class WebCommonUtils extends CommonUtils {

	private static final Logger log = ZetaLogger.getLog(WebCommonUtils.class
			.getSimpleName());

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

	public static String getScriptsTemplatesPath() {
		return String.format("%s/Documents/scripts/", System.getProperty("user.home"));
	}
	
	public static String getPicturesPath() {
		return String.format("%s/Documents", System.getProperty("user.home"));
	}

	public static String getFullPicturePath(String pictureName) {
		return String.format("%s/Documents/%s",
				System.getProperty("user.home"), pictureName);
	}

	public static String getNodeIp(RemoteWebDriver driver) throws Exception {
		HttpHost host = new HttpHost(
				getHubHostFromConfig(WebCommonUtils.class),
				getHubPortFromConfig(WebCommonUtils.class));
		DefaultHttpClient client = new DefaultHttpClient();
		String url = String.format(
				"http://%s:%s/grid/api/testsession?session=%s",
				getHubHostFromConfig(WebCommonUtils.class),
				getHubPortFromConfig(WebCommonUtils.class),
				driver.getSessionId());
		URL testSessionApi = new URL(url);
		BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest(
				"POST", testSessionApi.toExternalForm());
		HttpResponse response = client.execute(host, r);
		String full = "";
		String s;
		BufferedReader br = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		while ((s = br.readLine()) != null) {
			full += s;
		}
		client.close();
		log.debug("Received session data response from Selenium grid. Content: "
				+ full);
		JSONObject object = new JSONObject(full);
		String hostname = object.getString("proxyId");
		InetAddress address = InetAddress
				.getByName(new URL(hostname).getHost());
		String ip = address.getHostAddress();
		log.debug("Test scenarios will be executed on node: " + ip);
		return ip;
	}

	public static void putScriptsOnExecutionNode(String node) throws Exception {
		String commandTemplate = "sshpass -p %s "
				+ "scp -r -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no "
				+ "%s/* %s@%s:%s";

		String command = String.format(commandTemplate,
				getJenkinsSuperUserPassword(CommonUtils.class),
				getJenkinsSuperUserLogin(CommonUtils.class),
				WebAppConstants.Scripts.SCRIPTS_FOLDER,
				getJenkinsSuperUserLogin(CommonUtils.class),
				WebAppExecutionContext.seleniumNodeIp,
				WebAppConstants.Scripts.SCRIPTS_FOLDER);
		WebCommonUtils
				.executeOsXCommand(new String[] { "bash", "-c", command });
	}

	public static void executeAppleScriptFromFile(String script)
			throws Exception {
		String commandTemplate = "sshpass -p %s "
				+ "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no "
				+ "%s@%s osascript %s";
		String command = String.format(commandTemplate,
				getJenkinsSuperUserPassword(CommonUtils.class),
				getJenkinsSuperUserLogin(CommonUtils.class),
				WebAppExecutionContext.seleniumNodeIp, script);
		WebCommonUtils
				.executeOsXCommand(new String[] { "bash", "-c", command });
	}

	public static void formatTextInFileAndSave(String srcFile, String dstFile, Object[] params)
			throws IOException {
		FileInputStream fis = new FileInputStream(srcFile);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		String script = "";
		String t;
		while ((t = br.readLine()) != null) {
			if (!t.trim().isEmpty()) {
				script += t + "\n";
			}
		}
		br.close();
		isr.close();
		fis.close();
		script = String.format(script, params);
		File dstFileInstance = new File(dstFile);
		dstFileInstance.getParentFile().mkdirs();
		dstFileInstance.createNewFile();
		PrintWriter out = new PrintWriter(dstFile);
		out.write(script);
		out.close();
	}
}
