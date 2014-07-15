package com.wearezeta.auto.common;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class BackEndREST {
	ClientConfig config = new DefaultClientConfig();
	static Client client = Client.create();

	public static void autoTestSendRequest(ClientUser user, ClientUser contact)
	{
		user = loginByUser(user);
		user = getUserInfo(user);
		sendConnectRequest(user,contact,"NewConnect", "Hello!!!");
	}

	public static void autoTestAcceptAllRequest(ClientUser user)
	{
		 user = loginByUser(user);
		acceptAllConnections(user);
	}
	
	public static void sendDialogMessage(ClientUser fromUser, ClientUser toUser, String message) throws Exception{
		fromUser = loginByUser(fromUser);
		String id = getConversationWithSingleUser(fromUser,toUser);
		sendConversationMessage(fromUser, id, message);
	}

	public static ClientUser loginByUser(ClientUser user)
	{

		try {

			WebResource webResource = client.resource(getBaseURI() + "/login");
			String input =  "{\"email\":\""+ user.getEmail() +"\",\"password\":\""+ user.getPassword() +"\",\"label\":\"\"}";
			ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}	       
			String output = response.getEntity(String.class);
			JSONObject jsonObj =  new JSONObject(output);
			user.setAccessToken(jsonObj.getString("access_token"));
			user.setTokenType(jsonObj.getString("token_type"));

			System.out.println("Output from Server ....  login By User " + user.getEmail());
			System.out.println(output + "\n");
		} catch (Exception e) {

			e.printStackTrace();

		}
		return user;
	}

	public static ClientUser getUserInfo(ClientUser user)
	{
		try {
			WebResource webResource = client.resource(getBaseURI() + "/self");
			ClientResponse response = webResource.accept("application/json").header(HttpHeaders.AUTHORIZATION, user.getTokenType() + " " + user.getAccessToken()).get(ClientResponse.class);
			if (response.getStatus() != 200) {  throw new RuntimeException("Failed : HTTP error code : "	+ response.getStatus());}

			String output = response.getEntity(String.class);
			JSONObject jsonObj =  new JSONObject(output);

			user.setName(jsonObj.getString("name"));
			user.setId(jsonObj.getString("id"));

			// display response
			System.out.println("Output from Server .... get User Info " + user.getEmail());
			System.out.println(output + "\n");

		} catch (Exception e) {

			e.printStackTrace();

		}
		return user;
	}

	public static void sendConnectRequest(ClientUser user, ClientUser contact,String connectName, String message)
	{
		try {
			WebResource webResource = client.resource(getBaseURI() + "/self/connections");
			String input =  "{\"user\": \"" + contact.getId() + "\",\"name\": \"" + connectName + "\",\"message\": \"" + message + "\"}";
			ClientResponse response = webResource.accept("application/json").type("application/json").header(HttpHeaders.AUTHORIZATION, user.getTokenType() + " " + user.getAccessToken()).post(ClientResponse.class, input);
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}	       
			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... send Connect Request by" + user.getEmail());
			System.out.println(output + "\n");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void acceptAllConnections(ClientUser user)
	{
		try {
			WebResource webResource = client.resource(getBaseURI() + "/self/connections");
			ClientResponse response = webResource.accept("application/json").header(HttpHeaders.AUTHORIZATION, user.getTokenType() + " " + user.getAccessToken()).get(ClientResponse.class);
			if (response.getStatus() != 200) {  throw new RuntimeException("Failed : HTTP error code : "	+ response.getStatus());}

			String output = response.getEntity(String.class);

			System.out.println("Output from Server ....  accept All Connections " + user.getEmail());
			System.out.println(output + "\n");

			JSONArray newJArray = new JSONArray(output);

			for (int i=0; i < newJArray.length(); i++) 
			{
				String to = ((JSONObject)newJArray.get(i)).getString("to");
				changeConnectRequestStatus(user,to,"accepted");
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void changeConnectRequestStatus(ClientUser user, String connectionId, String newStatus){
		try {
			WebResource webResource = client.resource(getBaseURI() + "/self/connections/" + connectionId);
			String input = "{  \"status\": \"" + newStatus + "\"}";		    
			ClientResponse response = webResource.accept("application/json").header(HttpHeaders.AUTHORIZATION, user.getTokenType() + " " + user.getAccessToken()).type("application/json").put(ClientResponse.class, input);
			if (response.getStatus() != 200) {  throw new RuntimeException("Failed : HTTP error code : "	+ response.getStatus());}

			String output = response.getEntity(String.class);

			System.out.println("Output from Server ....  change Connect Request Status " + user.getEmail());
			System.out.println(output + "\n");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static void registerNewUser(String email, String userName, String password)
	{
		try {

			WebResource webResource = client.resource(getBaseURI() + "/register");
			String input =  "{\"email\": \"" + email + "\",\"name\": \"" + userName + "\",\"password\": \"" + password + "\"}";
			ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}	       
			String output = response.getEntity(String.class);
			JSONObject jsonObj =  new JSONObject(output);
			jsonObj.getString("id");
			System.out.println("Output from Server .... register New User "  + email);
			System.out.println(output + "\n");
		} catch (Exception e) {

			e.printStackTrace();

		}
	}
	
	public static void activateNewUser(String key, String code){
		try {
			WebResource webResource = client.resource(getBaseURI() + "/activate?code=" + code + "&key=" + key + "");
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
			if (response.getStatus() != 200) {  throw new RuntimeException("Failed : HTTP error code : "	+ response.getStatus());}

			System.out.println("Output from Server .... activate New User");
			System.out.println("User activated\n");

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
	
	public static void createGroupConveration(ClientUser user, List<ClientUser> contacts, String conversationName ){
		loginByUser(user);
		try{
			WebResource webResource = client.resource(getBaseURI() + "/conversations/");

			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < contacts.size(); i++){
				sb.append("\"");
				sb.append(contacts.get(i).getId());
				if(i == (contacts.size()-1)){
					sb.append("\"");
				}
				else{
					sb.append("\",");
				}
			}

			String input =  "{\"users\": [ " + sb.toString() + " ],\"name\": \"" + conversationName + "\" }";
			ClientResponse response = webResource.accept("application/json").type("application/json").header(HttpHeaders.AUTHORIZATION, user.getTokenType() + " " + user.getAccessToken()).post(ClientResponse.class, input);
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}	       
			String output = response.getEntity(String.class);

			System.out.println("Output from Server ....");
			System.out.println(output + "\n");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	private static String getConversationWithSingleUser(ClientUser fromUser, ClientUser toUser) throws Exception{
		String conversationId = null;
		boolean flag = false;
		JSONArray jsonArray = getConversations(fromUser);
		for(int i = 0 ; i < jsonArray.length(); i++){
			 JSONObject conversation =  (JSONObject) jsonArray.get(i);
			 conversationId =  conversation.getString("id");
			 conversation = (JSONObject) conversation.get("members");
			 JSONArray otherArray = (JSONArray) conversation.get("others");
			 if(otherArray.length() == 1){
				 String id = ((JSONObject)otherArray.get(0)).getString("id");
				 if(id.equals(toUser.getId())){
					 flag = true;
					 break;
				 }
			 }
			 if(flag){
				 break;
			 }
		 }
		return conversationId;
	}
	
	private static void sendConversationMessage(ClientUser user, String convId, String message ){
		try {
     
			String nonce = CommonUtils.generateGUID();
			
			WebResource webResource = client.resource(getBaseURI() + "/conversations/" + convId + "/messages");
			String input =  "{\"content\": \"" + message + "\", \"nonce\": \"" + nonce + "\"}";
			ClientResponse response = webResource.accept("application/json").type("application/json").header(HttpHeaders.AUTHORIZATION, user.getTokenType() + " " + user.getAccessToken()).post(ClientResponse.class, input);
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}	       
			String output = response.getEntity(String.class);

			System.out.println("Output from Server ....");
			System.out.println(output + "\n");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	private static JSONArray getConversations(ClientUser user) throws Exception
	{
		JSONObject jsonObj = null;
		try {
			WebResource webResource = client.resource(getBaseURI() + "/conversations");
			ClientResponse response = webResource.accept("application/json").header(HttpHeaders.AUTHORIZATION, user.getTokenType() + " " + user.getAccessToken()).get(ClientResponse.class);
			if (response.getStatus() != 200) {  throw new RuntimeException("Failed : HTTP error code : "	+ response.getStatus());}

			String output = response.getEntity(String.class);
			
			jsonObj =  new JSONObject(output);
						 
			// display response
			System.out.println("Output from Server .... get User Info ");
			System.out.println(output + "\n");

		} catch (Exception e) {

			e.printStackTrace();

		}
		return (JSONArray) jsonObj.get("conversations");
	}
	
	private static URI getBaseURI() throws IllegalArgumentException, UriBuilderException, IOException {
		return UriBuilder.fromUri(CommonUtils.getDefaultBackEndUrlFromConfig(CommonUtils.class)).build();
	}
	
	private static JSONArray getEventsfromConversation(String convID, ClientUser user) throws JSONException{
		
		JSONObject jsonObj = null;
		try {
			WebResource webResource = client.resource(getBaseURI() + "/conversations/" + convID + "/events");
			ClientResponse response = webResource.accept("application/json").header(HttpHeaders.AUTHORIZATION, user.getTokenType() + " " + user.getAccessToken()).get(ClientResponse.class);
			if (response.getStatus() != 200) {  throw new RuntimeException("Failed : HTTP error code : "	+ response.getStatus());}

			String output = response.getEntity(String.class);
			
			jsonObj =  new JSONObject(output);
						 
			// display response
			System.out.println("Output from Server .... get Events of Conversation ");
			System.out.println(output + "\n");

		} catch (Exception e) {

			e.printStackTrace();

		}
		return (JSONArray) jsonObj.get("events");
	}
	
	private static String getAssetsDownloadURL(String convID, String assetID, ClientUser user){
			
		//To download an asset (or more precisely, to receive a download link for an asset), send a GET request to the /assets/{id}?conv_id={convId} endpoint, 
		//including an asset ID and conversation ID, specifying the asset to retrieve and the conversational context, respectively
		
		//GET assets/02be406e-32bf-53f3-833b-5b0de3a7b9a5?conv_id=f2e0702c-df1c-4b09-bad3-179201b7ddfb HTTP/1.1
		//GET assets/assetID?conv_id=convID
		
		//A successful response will be a 302 redirect with a 'Location' header. The targeted URL is a CloudFront URL that can be used to download the asset from
		//a CloudFront edge location. For security reasons, the URL is signed and valid only for a short time frame.
		//By following the returned link, the asset can be downloaded.
		
			String downloadLink = null;
			JSONObject jsonObj = null;
			try {
				WebResource webResource = client.resource(getBaseURI() + "/assets/" + assetID + "?conv_id=" + convID);
				ClientResponse response = webResource.accept("application/json").header(HttpHeaders.AUTHORIZATION, user.getTokenType() + " " + user.getAccessToken()).get(ClientResponse.class);
				if (response.getStatus() != 200) {  throw new RuntimeException("Failed : HTTP error code : "	+ response.getStatus());}

				downloadLink = response.getEntity(String.class);
				jsonObj =  new JSONObject(downloadLink);
							 
				// display response
				System.out.println("Output from Server .... get Asset Download URL ");
				System.out.println(jsonObj + "\n");

			} catch (Exception e) {

				e.printStackTrace();

			}
			return downloadLink;
		}
	
	public static String getPictureAssetFromConversation() throws Exception{
		
		ClientUser fromUser = CommonUtils.yourUsers.get(0);
		ClientUser toUser = CommonUtils.contacts.get(0);
		String convID = getConversationWithSingleUser(fromUser, toUser);

		String assetID = null;
		JSONArray eventsOfConv = getEventsfromConversation(convID, fromUser);
		for(int i = 0 ; i < eventsOfConv.length(); i++){
			JSONObject event =  (JSONObject) eventsOfConv.get(i);
			String type = event.getString("type");
			if (type.equals("conversation.asset-add")){
				JSONObject data = (JSONObject) event.get("data");
				JSONObject info = (JSONObject) data.get("info");
					String tag = info.getString("tag");
					if (tag.equals("medium")){
						assetID = (String) data.get("id");
					}
				}
			}
		
		String pictureAssetDownloadLink = null;
		pictureAssetDownloadLink = getAssetsDownloadURL(convID, assetID, fromUser);
		
		return pictureAssetDownloadLink;
		
	}
}
