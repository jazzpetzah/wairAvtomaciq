package com.wearezeta.auto.user_management;

import java.util.HashSet;
import java.util.Set;

public class ClientUser {
	private String name = null;
	private Set<String> nameAliases = new HashSet<String>();

	public Set<String> getNameAliases() {
		return new HashSet<String>(this.nameAliases);
	}

	public void addNameAlias(String alias) {
		this.nameAliases.add(alias);
	}

	public void clearNameAliases() {
		this.nameAliases.clear();
	}

	private String password = null;
	private Set<String> passwordAliases = new HashSet<String>();

	public Set<String> getPasswordAliases() {
		return new HashSet<String>(this.passwordAliases);
	}

	public void addPasswordAlias(String alias) {
		this.passwordAliases.add(alias);
	}

	public void clearPasswordAliases() {
		this.passwordAliases.clear();
	}

	private String email = null;
	private Set<String> emailAliases = new HashSet<String>();

	public Set<String> getEmailAliases() {
		return new HashSet<String>(this.emailAliases);
	}

	public void addEmailAlias(String alias) {
		this.emailAliases.add(alias);
	}

	public void clearEmailAliases() {
		this.emailAliases.clear();
	}

	private String id = null;
	private String accessToken = null;
	private String tokenType = null;
	private UserState userState = UserState.NotCreated;

	public ClientUser() {
	}

	public ClientUser(String email, String password, String name,
			UserState userState) {
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

	public UserState getUserState() {
		return userState;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
	}

	@Override
	public ClientUser clone() throws CloneNotSupportedException {
		ClientUser cloned = (ClientUser) super.clone();
		cloned.setId(this.getId());
		cloned.setEmail(this.getEmail());
		cloned.setName(this.getName());
		cloned.setAccessToken(this.getAccessToken());
		cloned.setPassword(this.getPassword());
		cloned.setUserState(this.getUserState());
		cloned.nameAliases = new HashSet<String>(this.getNameAliases());
		cloned.passwordAliases = new HashSet<String>(this.getPasswordAliases());
		cloned.emailAliases = new HashSet<String>(this.getEmailAliases());
		return cloned;
	}
}
