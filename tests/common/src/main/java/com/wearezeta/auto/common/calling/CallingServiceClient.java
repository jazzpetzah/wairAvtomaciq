package com.wearezeta.auto.common.calling;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.wearezeta.auto.common.log.ZetaLogger;

public class CallingServiceClient {
	
	private static final Logger log = ZetaLogger.getLog(CallingServiceClient.class.getSimpleName());
	
	public class WireBackend {
		public static final String STAGING = "staging";
		public static final String PRODUCTION = "production";
	}
	
	private String host;
	private String port;
	
	public CallingServiceClient(String host, String port) {
		this.host = host;
		this.port = port;
	}
	
	public String makeCall(String email, String password, String conversationId, String backend, String callBackend) throws JSONException, Exception {
		JSONObject call = new JSONObject();
		call.put("email", email);
		call.put("password", password);
		call.put("conversationId", conversationId);
		call.put("wireBackend", backend);
		call.put("callBackend", callBackend);

		return request("/api/call", "POST", call).getString("callId");
	}
	
	public String waitToAcceptCall(String email, String password, String backend, String callBackend) throws JSONException, Exception {
		JSONObject waitingInstance = new JSONObject();
		waitingInstance.put("email", email);
		waitingInstance.put("password", password);
		waitingInstance.put("backend", backend);
		waitingInstance.put("callBackend", callBackend);
		
		String callId = request("/api/waitingInstance", "POST", waitingInstance).getString("callId");

		while(!getWaitingInstanceStatus(callId).equals("waiting")) {
			Thread.sleep(2000);
		}
		
		return callId;
	}
	
	public void stopCall(String callId) throws JSONException, Exception {
		request("/api/call/" + callId + "/stop", "PUT", new JSONObject());
		request("/api/waitingForInstances/" + callId + "/stop", "PUT", new JSONObject());
	}
	
	public String getCallStatus(String callId) throws JSONException, Exception {
		return request("/api/call/" + callId + "/status", "GET", new JSONObject()).getString("status");
	}
	
	public String getWaitingInstanceStatus(String callId) throws JSONException, Exception {
		return request("/api/waitingInstance/" + callId + "/status", "GET", new JSONObject()).getString("status");
	}
	
	private JSONObject request(String path, String requestMethod, JSONObject object) throws Exception {
		log.info("Sending object: " + object.toString());
		HttpURLConnection connection = null;
		try {
            URL url = new URL("http://" + this.host + ":" + this.port + path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(30000);
			if (!requestMethod.equals("GET")) {
				connection.setDoOutput(true);
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	            out.write(object.toString());
	            out.close();
			}
			Scanner s = null;
			String response = null;
			try {
				s = new Scanner(connection.getInputStream()).useDelimiter("\\A");
				response = s.hasNext() ? s.next() : "";
			} finally {
				s.close();
			}
            log.info("Response: " + response);
            return new JSONObject(response);
        } catch (Exception e) {
        	log.error(connection.getResponseMessage());
            throw new Exception("\nError while using calling service REST Service", e);
        }
	}

}
