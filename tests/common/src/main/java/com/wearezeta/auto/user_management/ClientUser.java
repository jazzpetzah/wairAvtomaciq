package com.wearezeta.auto.user_management;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.wearezeta.auto.common.CommonUtils;

public class ClientUser implements Cloneable {
	private String name = null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	private String accessToken = null;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	private String tokenType = null;
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	private UserState userState = UserState.NotCreated;
	public UserState getUserState() {
		return userState;
	}
	public void setUserState(UserState userState) {
		this.userState = userState;
	}

	public ClientUser() throws IOException {
		this.name = UserCreationHelper.generateUniqName();
		this.password = CommonUtils
				.getDefaultPasswordFromConfig(ClientUser.class);
		this.email = UserCreationHelper.generateUniqEmail(
				UserCreationHelper.getMboxName(), name);
	}

	@Override
	public ClientUser clone() throws CloneNotSupportedException {
		ClientUser cloned = (ClientUser) super.clone();
		cloned.setId(this.getId());
		cloned.setEmail(this.getEmail());
		cloned.setName(this.getName());
		cloned.setAccessToken(this.getAccessToken());
		cloned.setTokenType(this.getTokenType());
		cloned.setPassword(this.getPassword());
		cloned.setUserState(this.getUserState());
		cloned.nameAliases = this.getNameAliases();
		cloned.passwordAliases = this.getPasswordAliases();
		cloned.emailAliases = this.getEmailAliases();
		return cloned;
	}
}
