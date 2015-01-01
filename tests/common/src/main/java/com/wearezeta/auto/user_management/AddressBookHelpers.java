package com.wearezeta.auto.user_management;

import java.util.List;

public abstract class AddressBookHelpers {

	public AddressBookHelpers() {}

	public abstract void addUsersToContacts(List<ClientUser> users)
			throws Exception;

	public abstract void removeUsersFromContacts(List<ClientUser> Users)
			throws Exception;
}
