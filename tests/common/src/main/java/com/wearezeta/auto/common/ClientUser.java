package com.wearezeta.auto.common;

public class ClientUser {
	
	private String email = null;
	private String name = null;
	private String id = null;
	private String password = null;
	private String accessToken = null;
	private String tokenType = null;
	private UsersState userState = UsersState.NotCreated;
	
	public ClientUser() { }
	
	public ClientUser(String email, String password, String name, UsersState userState) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.userState = userState;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public UsersState getUserState() {
		return userState;
	}
	public void setUserState(UsersState userState) {
		this.userState = userState;
	}
	
	
}
