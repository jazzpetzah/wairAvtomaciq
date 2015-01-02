package com.wearezeta.auto.common;

import java.util.ArrayList;
import java.util.List;

import com.wearezeta.auto.user_management.ClientUser;
import com.wearezeta.auto.user_management.UserChatsHelper;
import com.wearezeta.auto.user_management.UserCreationHelper;
import com.wearezeta.auto.user_management.OSXAddressBookHelpers;
import com.wearezeta.auto.user_management.ClientUsersManager;

public final class CommonSteps {
	public static final String CONNECTION_NAME = "CONNECT TO ";
	public static final String CONNECTION_MESSAGE = "Hello!";

	private static String pingId = null;

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	public ClientUsersManager getUserManager() {
		return this.usrMgr;
	}
	private final UserChatsHelper chatHelper = UserChatsHelper.getInstance();

	private static CommonSteps instance = null;
	public static CommonSteps getInstance() {
		if (instance == null) {
			instance = new CommonSteps();
		}
		return instance;
	}
	
	private CommonSteps() {}

	public void GivenConnectionRequestIsSendedToMe(String contact, String me)
			throws Throwable {
		ClientUser yourUser = usrMgr.findUserByNameAlias(me);
		ClientUser contactUser = usrMgr.findUserByNameAlias(contact);
		contactUser = BackEndREST.loginByUser(contactUser);
		BackEndREST.sendConnectRequest(contactUser, yourUser, CONNECTION_NAME
				+ contactUser.getName(), CONNECTION_MESSAGE);
		Thread.sleep(2000);
	}

	public void GivenMyContactCreateGroupChatWithMeAndHisContact(
			String contact1, String me, String contact2, String chatName)
			throws Exception {
		ClientUser yourUser = usrMgr.findUserByNameAlias(me);
		ClientUser yourContact = usrMgr.findUserByNameAlias(contact1);
		ClientUser contactContact = usrMgr.findUserByNameAlias(contact2);

		List<ClientUser> users = new ArrayList<ClientUser>();
		yourUser = BackEndREST.loginByUser(yourUser);
		yourContact = BackEndREST.loginByUser(yourContact);
		contactContact = BackEndREST.loginByUser(contactContact);
		users.add(yourUser);
		users.add(contactContact);
		BackEndREST.createGroupConversation(yourContact, users, chatName);
	}

	public void GivenUserIsConnectedWith(String contact1, String contact2)
			throws Exception {
		ClientUser contactInfo1 = usrMgr.findUserByNameAlias(contact1);
		ClientUser contactInfo2 = usrMgr.findUserByNameAlias(contact2);
		contactInfo1 = BackEndREST.loginByUser(contactInfo1);
		BackEndREST.autoTestSendRequest(contactInfo1, contactInfo2);
		BackEndREST.autoTestAcceptAllRequest(contactInfo2);
	}

	public void GivenIHaveGroupChatWith(String chatName, String contact1,
			String contact2) throws Exception {
		List<ClientUser> chatContacts = new ArrayList<ClientUser>();
		chatContacts.add(usrMgr.findUserByNameAlias(contact1));
		chatContacts.add(usrMgr.findUserByNameAlias(contact2));
		ClientUser user = usrMgr
				.findUserByNameAlias(ClientUsersManager.SELF_USER_ALIAS);
		user = BackEndREST.loginByUser(user);
		BackEndREST.createGroupConversation(user, chatContacts, chatName);
	}

	public void GivenGenerateAndConnectAdditionalUsers(int usersNum,
			String userName) throws Exception {
		ClientUser yourUser = usrMgr.findUserByNameAlias(userName);
		chatHelper.generateNUsers(usersNum);
		chatHelper.sendConnectionRequestInThreads(yourUser);
		yourUser = BackEndREST.loginByUser(yourUser);
		BackEndREST.acceptAllConnections(yourUser);
	}

	public void IgnoreConnectRequest(String contact) throws Exception {
		ClientUser yourСontact = usrMgr.findUserByNameAlias(contact);
		yourСontact = BackEndREST.loginByUser(yourСontact);
		BackEndREST.ignoreAllConnections(yourСontact);
	}

	public void WaitForTime(String seconds) throws NumberFormatException,
			InterruptedException {
		Thread.sleep(Integer.parseInt(seconds) * 1000);
	}

	public void BlockContact(String contact, String login) throws Exception {
		ClientUser yourUser = usrMgr.findUserByNameAlias(contact);
		yourUser = BackEndREST.loginByUser(yourUser);
		ClientUser userToBlock = usrMgr.findUserByNameAlias(login);
		userToBlock = BackEndREST.loginByUser(yourUser);
		try {
			BackEndREST.sendConnectRequest(yourUser, userToBlock, "connect",
					"Hello");
			Thread.sleep(1000);
		} catch (BackendRequestException e) {
			// Ignore silently
		}
		BackEndREST.changeConnectRequestStatus(yourUser, userToBlock.getId(),
				"blocked");
	}

	public void AcceptConnectRequest(String contact) throws Exception {
		ClientUser yourСontact = usrMgr.findUserByNameAlias(contact);
		yourСontact = BackEndREST.loginByUser(yourСontact);
		BackEndREST.acceptAllConnections(yourСontact);
		Thread.sleep(2000);
	}

	public void IHaveUsersAndConnections(int users, int connections,
			int usersWithContacts) throws Exception {
		usrMgr.generateUsers(users, connections);
		Thread.sleep(CommonUtils.BACKEND_SYNC_TIMEOUT);
		usrMgr.createContactLinks(usersWithContacts);
	}

	public void userPingedConversation(String contact, String conversationName)
			throws Exception {
		ClientUser yourСontact = usrMgr.findUserByNameAlias(contact);
		yourСontact = BackEndREST.loginByUser(yourСontact);
		pingId = BackEndREST.sendPingToConversation(yourСontact,
				conversationName);
		Thread.sleep(1000);
	}

	public void userHotPingedConversation(String contact,
			String conversationName) throws Exception {
		ClientUser yourСontact = usrMgr.findUserByNameAlias(contact);
		yourСontact = BackEndREST.loginByUser(yourСontact);
		BackEndREST.sendHotPingToConversation(yourСontact, conversationName,
				pingId);
		Thread.sleep(1000);
	}

	public void ISendInvitationToUserByContact(String user, String contact)
			throws Exception {
		BackEndREST.autoTestSendRequest(usrMgr.findUserByNameAlias(contact),
				usrMgr.findUserByNameAlias(user));
	}

	public void ISendInvitationToUserByContact(int requests, String user)
			throws Throwable {
		// limited to 4 users at time of creation to save time creating users
		if (requests < 5) {
			List<ClientUser> unconnectedUsers = usrMgr.getCreatedUsers().subList(1,
					requests + 1);
			ClientUser dstUser = usrMgr.findUserByNameAlias(user);
			for (int i = 0; i < requests; i++) {
				BackEndREST.autoTestSendRequest(unconnectedUsers.get(i),
						dstUser);
				Thread.sleep(2000);
			}
		}
	}

	public void AddContactsUsersToMacContacts() throws Exception {
		(new OSXAddressBookHelpers()).addUsersToContacts(usrMgr.getCreatedContacts());
	}

	public void IRemoveContactsListUsersFromMacContact() throws Exception {
		(new OSXAddressBookHelpers()).removeUsersFromContacts(usrMgr
				.getCreatedContacts());
	}

	public void GivenIHaveAtMinimumConnections(int minimumConnections)
			throws Exception {
		ClientUser selfUser = usrMgr
				.findUserByNameAlias(ClientUsersManager.SELF_USER_ALIAS);
		for (int i = 0; i < minimumConnections; i++) {
			ClientUser user = new ClientUser();
			UserCreationHelper.createWireUser(user);
			BackEndREST.autoTestSendRequest(user, selfUser);
		}
		BackEndREST.autoTestAcceptAllRequest(selfUser);
	}
}
