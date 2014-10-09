package com.wearezeta.auto.perfhelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

public class PerfHelper {
	private static final Logger log = ZetaLogger.getLog(PerfHelper.class
			.getSimpleName());
	private static final Logger backendLog = ZetaLogger
			.getLog(BackEndREST.class.getSimpleName());
	public static final int NUMBER_OF_THREADS = 5;
	public static final int GROUP_CHAT_CREATION_TIMEOUT = 60 * 5;
	public static List<ClientUser> contacts = new CopyOnWriteArrayList<ClientUser>();
	public static List<ConvPair> user_chats = new CopyOnWriteArrayList<ConvPair>();
	public static ClientUser yourUser = new ClientUser();
	static int createdCount = 1, interval = 0, messageCount = -1;
	static Boolean sendImg = false, showContacts = false;

	public static void main(String[] args) throws Throwable {
		backendLog.setLevel(Level.INFO);

		log.info("Valid arguments :\n"
				+ "-m your zeta client email\n"
				+ "-p your zeta client password\n"
				+ "-contNum contacts number\n"
				+ "-convNum conversation number (should be bigger than contacts number)\n"
				+ "-i interval between messages/images (seconds), default is 0\n");

		if (args.length % 2 == 1 || args.length == 0) {
			log.info("Invalid number of arguments");

			return;
		}

		String login = "error", password = "error";
		int contactNum = 0, converations = 0;

		for (int i = 0; i < args.length; i = i + 2) {
			switch (args[i]) {
			case "-m":
				login = args[i + 1];
				break;

			case "-p":
				password = args[i + 1];
				break;

			case "-contNum":
				contactNum = Integer.parseInt(args[i + 1]);
				break;

			case "-convNum":
				converations = Integer.parseInt(args[i + 1]);
				break;

			case "-i":
				interval = Integer.parseInt(args[i + 1]);
				break;
			}
		}
		sendImg = true;
		fillUserData(login, password);
		createUsers(contactNum);
		acceptConnections();
		generateGroupChats(converations, contactNum);
		System.out.println("Press Enter to continue");
		System.in.read();

		long startDate = new Date().getTime();

		
		if (messageCount == -1) {
			int counter = 1;
			int imageCounter = 1;
			while (true) {
				final int count =	counter; 
				ExecutorService executor = Executors
						.newFixedThreadPool(NUMBER_OF_THREADS);
				for (int i = 0; i < user_chats.size(); i++) {
					final int imageCount = imageCounter;
					final ClientUser yourСontact = user_chats.get(i)
							.getContact();
					final String contact = user_chats.get(i).getConvName();
					Runnable worker = new Thread(new Runnable() {
						public void run() {
							try {
								sendMessage(yourСontact, contact, 1,count);
								Thread.sleep(interval * 1000 / user_chats.size());
								if (sendImg && imageCount == 5) {
											sendPicture(yourСontact, contact,1, "default",count);
								}
							} catch (Throwable e) {
								System.out.println(e.getMessage());
							}
						}
					});
					executor.submit(worker);
					if(imageCounter == 5){
						imageCounter = 1;
					}
					imageCounter++;
				}
				executor.shutdown();
				
				counter++;
			}
		}
		else{
			//TODO Implement specific counter
		}
		long endDate = new Date().getTime();

		log.info("Total execution time - " + (endDate - startDate) / 1000
				+ " seconds");
	}

	public static void fillUserData(String login, String password)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException {
		yourUser.setEmail(login);
		yourUser.setPassword(password);
		yourUser = BackEndREST.loginByUser(yourUser);
		yourUser = BackEndREST.getUserInfo(yourUser);

	}

	public static void createUsers(int contact) {
		ExecutorService executor = Executors
				.newFixedThreadPool(NUMBER_OF_THREADS);
		for (int i = 0; i < contact; i++) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						String email = CreateZetaUser
								.registerUserAndReturnMail();
						if (email == null)
							return;
						ClientUser user = new ClientUser();
						user.setEmail(email);
						user.setPassword(CommonUtils
								.getDefaultPasswordFromConfig(CommonUtils.class));
						contacts.add(user);

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

	public static void acceptConnections() throws IllegalArgumentException,
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

	public static void generateGroupChats(int convNum, int connNum)
			throws InterruptedException, BackendRequestException {
		int number = convNum - connNum;
		ExecutorService executor = Executors
				.newFixedThreadPool(NUMBER_OF_THREADS);
		long startDate = new Date().getTime();
		for (int i = 0; i < number; i++) {
			System.out.println("Creating chat #" + (i + 1));
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
						System.out.println("Chat " + (createdCount++)
								+ " created");
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

	public static void sendPicture(ClientUser user, String contact,
			int messageCount, String path,int counter) throws Throwable {
				log.info("Sending image " + Integer.toString(counter) + " From " + user.getName() 
						+ " to contact " + contact);

				ZBender.sendPicture(user, contact, path);
	}

	public static void sendMessage(ClientUser user, String contact,
			int messageCount,int counter) throws Exception {

				log.info("Sending message " + Integer.toString(counter) + " From " + user.getName()
						+ " to contact " + contact);

				BackEndREST.sendDialogMessageByChatName(
						user,
						contact,
						Integer.toString(counter) + " "
								+ CommonUtils.generateGUID());
	}
}
