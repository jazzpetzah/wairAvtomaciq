package com.wearezeta.auto.user_management;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;

public final class OSXAddressBookHelpers {
	private static ScriptEngine engine = (new ScriptEngineManager())
			.getEngineByName("AppleScript");

	public OSXAddressBookHelpers() {
	}

	private static void addUserToMacContactsByAppleScript(String firsname,
			String email) throws ScriptException {
		final String[] scriptArr = new String[] {
				"property first_name : \"" + firsname + "\"",
				"property user_email : \"" + email + "\"",
				"property laba : \"Work\"",
				"	tell application \"Contacts\"",
				"		set thePerson to make new person with properties {first name:first_name}",
				"		tell thePerson",
				"			make new email at end of emails of thePerson with properties {label:laba, value:user_email}",
				"		end tell", "		save", "	end tell" };
		final String script = StringUtils.join(scriptArr, "\n");
		engine.eval(script);
	}

	private static void removeUserFromMacContactsByAppleScript(String firstname)
			throws ScriptException {
		final String[] scriptArr = new String[] {
				"property first_name : \"" + firstname + "\"",
				"	tell application \"Contacts\"",
				"		set thePerson to every person whose first name = first_name",
				"		repeat with osoba in thePerson", "			delete osoba",
				"		end repeat", "		save", "	end tell" };
		final String script = StringUtils.join(scriptArr, "\n");
		engine.eval(script);
	}

	public static void addUsersToContacts(List<ClientUser> users)
			throws Exception {
		for (ClientUser user : users) {
			addUserToMacContactsByAppleScript(user.getName(), user.getEmail());
		}
	}

	public static void removeUsersFromContacts(List<ClientUser> users)
			throws Exception {
		for (ClientUser user : users) {
			removeUserFromMacContactsByAppleScript(user.getName());
		}
	}
}
