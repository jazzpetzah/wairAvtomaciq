package com.wearezeta.auto.common.usrmgmt;

import java.util.HashSet;
import java.util.Set;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.email.MessagingUtils;

public class ClientUser {
	private String name = null;

	public ClientUser(String email, String password, PhoneNumber phoneNumber,
			String name, UserState state) {
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.userState = state;
	}

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

	public void removeNameAlias(String alias) {
		this.nameAliases.remove(alias);
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

	public void removePasswordAlias(String alias) {
		this.passwordAliases.remove(alias);
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

	public void removeEmailAlias(String alias) {
		this.emailAliases.remove(alias);
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

	private AccentColor accentColor = AccentColor.Undefined;

	public void setAccentColor(AccentColor newColor) {
		this.accentColor = newColor;
	}

	public AccentColor getAccentColor() {
		return this.accentColor;
	}

	private PhoneNumber phoneNumber;

	public PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	private Set<String> phoneNumberAliases = new HashSet<String>();

	public Set<String> phoneNumberAliases() {
		return new HashSet<String>(this.phoneNumberAliases);
	}

	public void addPhoneNumberAlias(String alias) {
		this.phoneNumberAliases.add(alias);
	}

	public void removePhoneNumberAlias(String alias) {
		this.phoneNumberAliases.remove(alias);
	}

	public void clearPhoneNumberAliases() {
		this.phoneNumberAliases.clear();
	}
	
	private static String generateUniqName() {
		return CommonUtils.generateGUID().replace("-", "").substring(0, 8);
	}

	public static String generateEmail(String suffix) throws Exception {
		return generateEmail(MessagingUtils.getAccountName(), suffix);
	}

	private static String generateEmail(String basemail, String suffix) {
		String genmail = basemail.split("@")[0].concat("+").concat(suffix)
				.concat("@").concat(basemail.split("@")[1]);
		return genmail;
	}

	public ClientUser() throws Exception {
		this.name = generateUniqName();
		this.phoneNumber = new PhoneNumber(PhoneNumber.WIRE_COUNTRY_PREFIX);
		this.password = CommonUtils
				.getDefaultPasswordFromConfig(ClientUser.class);
		this.email = generateEmail(name);
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
