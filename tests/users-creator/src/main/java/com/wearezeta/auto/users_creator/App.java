package com.wearezeta.auto.users_creator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wearezeta.auto.cmdclient.ZBender;
import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.log.ZetaLogger;

public class App 
{
	private static final Logger log = ZetaLogger.getLog(App.class
			.getSimpleName());
	private static final Logger backendLog = ZetaLogger
			.getLog(BackEndREST.class.getSimpleName());
	private static final Logger createUserLog = ZetaLogger.getLog(CreateZetaUser.class.getSimpleName());
	public static List<ClientUser> contacts = new CopyOnWriteArrayList<ClientUser>();
	public static List<ConvPair> user_chats = new CopyOnWriteArrayList<ConvPair>();
	public static final int NUMBER_OF_THREADS = 5;
	public static final int GROUP_CHAT_CREATION_TIMEOUT = 60 * 5;
	public static ClientUser yourUser = new ClientUser();
	static int createdCount = 1;
	
	public static void main(String[] args) throws Throwable {
		backendLog.setLevel(Level.OFF);
		createUserLog.setLevel(Level.OFF);
		log.info("Valid arguments :\n"
				+ "-cont contacts number\n"
				+ "-group  group conversations number\n"
				+ "-f fill conversations (yes/no) \n"
				+ "-msgcount messages per conversation \n" 
				+ "-imgcount images per conversation \n");

		if (args.length % 2 == 1 || args.length == 0) {
			log.info("Invalid number of arguments");
			return;
		}
		
		if(!Arrays.asList(args).contains("-cont")){
			log.info("Invalid argument");
			return;
		}
		int contactNum = 0, converations = 0, messNum = 0, imgNum = 0;
		String fill = "";
		for (int i = 0; i < args.length; i = i + 2) {
			switch (args[i]) {
			case "-cont":
				contactNum = Integer.parseInt(args[i + 1]);
				break;
			case "-group":
				converations = Integer.parseInt(args[i + 1]);
				break;
			case "-msgcount":
				messNum = Integer.parseInt(args[i + 1]);
				break;
			case "-imgcount":
				imgNum = Integer.parseInt(args[i + 1]);
				break;
			case "-f":
				fill =  args[i + 1];
				break;
			}
		}
		
		yourUser = createUser();
		yourUser = BackEndREST.loginByUser(yourUser);
		yourUser = BackEndREST.getUserInfo(yourUser);
		createUsers(contactNum);
		acceptConnections();
		if(converations > 0){
			generateGroupChats(converations);
		}
		if(fill.equals("yes")){
			fillConversations(messNum, imgNum);
		}
		
		log.info("Your user email: " + yourUser.getEmail() + " Password: " + yourUser.getPassword() +"\n");
    }
	
	private static ClientUser createUser() throws IllegalArgumentException, UriBuilderException, IOException, MessagingException, JSONException, BackendRequestException, InterruptedException{
		String email = CreateZetaUser
				.registerUserAndReturnMail();
		ClientUser user = new ClientUser();
		user.setEmail(email);
		user.setPassword(CommonUtils
				.getDefaultPasswordFromConfig(CommonUtils.class));
		return user;
	}
	
	private static void createUsers(int contact) {
		ExecutorService executor = Executors
				.newFixedThreadPool(NUMBER_OF_THREADS);
		for (int i = 0; i < contact; i++) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						contacts.add(createUser());
					} catch (Exception e) {
						System.out.println("error" + e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void acceptConnections() throws IllegalArgumentException,
			UriBuilderException, IOException, JSONException,
			BackendRequestException, InterruptedException {
		ExecutorService executor = Executors
				.newFixedThreadPool(NUMBER_OF_THREADS);
		for (int i = 0; i < contacts.size(); i++) {
			final ClientUser user = contacts.get(i);
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						BackEndREST.autoTestSendRequest(user, yourUser);
						ConvPair pair = new ConvPair();
						pair.setContact(user);
						pair.setConvName(yourUser.getName());
						user_chats.add(pair);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
		BackEndREST.autoTestAcceptAllRequest(yourUser);
	}

	private static void generateGroupChats(int number)
			throws InterruptedException, BackendRequestException {
		ExecutorService executor = Executors
				.newFixedThreadPool(NUMBER_OF_THREADS);
		long startDate = new Date().getTime();
		for (int i = 0; i < number; i++) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						String chatName = "Group " + CommonUtils.generateGUID();
						ArrayList<ClientUser> chatParticipants = new ArrayList<ClientUser>();
						Collections.shuffle(contacts);
						chatParticipants.add(contacts.get(0));
						chatParticipants.add(contacts.get(1));
						BackEndREST.createGroupConversation(yourUser,
								chatParticipants, chatName);
						ConvPair pair = new ConvPair();
						pair.setContact(chatParticipants.get(0));
						pair.setConvName(chatName);
						user_chats.add(pair);
						chatParticipants.clear();
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		if (!executor.awaitTermination(GROUP_CHAT_CREATION_TIMEOUT,
				TimeUnit.SECONDS)) {
			throw new BackendRequestException(
					String.format(
							"The backend has failed to prepare predefined users within %d seconds timeout",
							GROUP_CHAT_CREATION_TIMEOUT));
		}
		long endDate = new Date().getTime();
		System.out.println("Group chats creation time = "
				+ (endDate - startDate) + "ms");
	}

	private static void sendPicture(ClientUser user, String contact,String path) throws Throwable {
				ZBender.sendPicture(user, contact, path);
	}

	private static void sendMessage(ClientUser user, String contact) throws Exception {

				BackEndREST.sendDialogMessageByChatName(
						user,
						contact,
						" " + CommonUtils.generateGUID());
	}
	
	private static void fillConversations(final int mesNum, final int imgNum) throws InterruptedException, BackendRequestException{
				ExecutorService executor = Executors
						.newFixedThreadPool(NUMBER_OF_THREADS);
				for (int i = 0; i < user_chats.size(); i++) {
					final ClientUser yourСontact = user_chats.get(i)
							.getContact();
					final String contact = user_chats.get(i).getConvName();
					Runnable worker = new Thread(new Runnable() {
						public void run() {
							try {
								for(int x = 0; x < mesNum; x ++){
								sendMessage(yourСontact, contact);
								}
								Thread.sleep( 1000 / user_chats.size());
								for(int x = 0; x < imgNum; x++){
								sendPicture(yourСontact, contact,"default");
								}
								
							} catch (Throwable e) {
								System.out.println(e.getMessage());
							}
						}
					});
					executor.submit(worker);
				}
				executor.shutdown();
				if (!executor.awaitTermination(GROUP_CHAT_CREATION_TIMEOUT,
						TimeUnit.SECONDS)) {
					throw new BackendRequestException(
							String.format(
									"The backend has failed to prepare predefined users within %d seconds timeout",
									GROUP_CHAT_CREATION_TIMEOUT));
				}
	}
}
