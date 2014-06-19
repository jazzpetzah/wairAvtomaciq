package com.wearezeta.auto.common;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import org.json.JSONArray;
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
		loginByUser(user);
		acceptAllConnections(user);
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

			System.out.println("Output from Server .... ");
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
			System.out.println("Output from Server .... ");
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

			System.out.println("Output from Server .... ");
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

			System.out.println("Output from Server .... ");
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

			System.out.println("Output from Server .... ");
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
			System.out.println("Output from Server .... ");
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

			System.out.println("Output from Server .... ");
			System.out.println("User activated\n");

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
	
	private static URI getBaseURI() throws IllegalArgumentException, UriBuilderException, IOException {
		return UriBuilder.fromUri(CommonUtils.getDefaultBackEndUrlFromConfig(CommonUtils.class)).build();
	}
}
