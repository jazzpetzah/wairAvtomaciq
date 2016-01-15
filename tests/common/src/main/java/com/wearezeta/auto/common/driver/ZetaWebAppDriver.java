package com.wearezeta.auto.common.driver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

@SuppressWarnings("deprecation")
public class ZetaWebAppDriver extends RemoteWebDriver implements ZetaDriver {

	private static final Logger log = ZetaLogger.getLog(ZetaWebAppDriver.class
			.getSimpleName());

	private String nodeIp = "127.0.0.1";

	public ZetaWebAppDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		try {
			if (CommonUtils.getInitNoteIpFromConfig(this.getClass())) {
				log.debug("Get current node ip through hub on " + remoteAddress);
				this.nodeIp = initNodeIp(remoteAddress);
				log.debug(String
						.format("Current Selenium node ip address is '%s'",
								this.nodeIp));
			}
		} catch (JSONException e) {
			// keep node ip unchanged
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isSessionLost() {
		return false;
	}

	private String initNodeIp(URL remoteAddress) throws Exception {
		HttpHost host = new HttpHost(remoteAddress.getHost(),
				remoteAddress.getPort());
		DefaultHttpClient client = new DefaultHttpClient();
		String url = String.format(
				"http://%s:%s/grid/api/testsession?session=%s",
				remoteAddress.getHost(), remoteAddress.getPort(),
				this.getSessionId());
		log.debug("Testsession: " + url);
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
		return address.getHostAddress();
	}

	public String getNodeIp() {
		return this.nodeIp;
	}
}
