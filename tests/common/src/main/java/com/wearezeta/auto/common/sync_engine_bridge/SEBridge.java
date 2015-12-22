package com.wearezeta.auto.common.sync_engine_bridge;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class SEBridge {
	private static final Future<UserDevicePool> devicePool;
	private static final ExecutorService pool = Executors.newFixedThreadPool(1);
	private static SEBridge instance = null;
	private static final int POOL_CREATION_TIMEOUT = 60; // seconds

	private static final Logger LOG = ZetaLogger.getLog(SEBridge.class
			.getSimpleName());

	static {
		devicePool = pool.submit(() -> new UserDevicePool(CommonUtils
				.getBackendType(CommonUtils.class)));
		Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
	}

	private SEBridge() {
	}

	public static synchronized SEBridge getInstance() {
		if (instance == null) {
			instance = new SEBridge();
		}
		return instance;
	}

	private UserDevicePool getDevicePool() throws Exception {
		return devicePool.get(POOL_CREATION_TIMEOUT, TimeUnit.SECONDS);
	}

	private void login(ClientUser user, IDevice dstDevice) throws Exception {
		if (dstDevice.hasLoggedInUser() && !dstDevice.isLoggedInUser(user)) {
			LOG.info("Logout as user " + user.getName() + " with device "
					+ dstDevice.name());
			this.logout(user, dstDevice);
		}
		if (!dstDevice.isLoggedInUser(user)) {
			LOG.info("Login as user " + user.getName() + " with device "
					+ dstDevice.name());
			dstDevice.logInWithUser(user);
		}
	}

	private void logout(ClientUser user, IDevice dstDevice) throws Exception {
		if (dstDevice.hasLoggedInUser()) {
			dstDevice.logout();
		}
	}

	private IDevice getOrAddRandomDevice(ClientUser user) throws Exception {
		IDevice dstDevice = getDevicePool().getOrAddRandomDevice(user);
		this.login(user, dstDevice);
		return dstDevice;
	}

	private static void verifyPathExists(String path) {
		if (!new File(path).exists()) {
			throw new IllegalArgumentException(String.format(
					"The file %s is not accessible", path));
		}
	}

	public void sendConversationMessage(ClientUser userFrom, String convId,
			String message) throws Exception {
		getOrAddRandomDevice(userFrom).sendMessage(convId, message);
	}

	public void addRemoteDeviceToAccount(ClientUser user) throws Exception {
		IDevice dstDevice = this.getDevicePool().addDevice(user);
		this.login(user, dstDevice);
	}

	public void addRemoteDeviceToAccount(ClientUser user, String deviceName, String label)
			throws Exception {
		IDevice dstDevice = this.getDevicePool().addDevice(user, deviceName);
		this.login(user, dstDevice);
		dstDevice.setLabel(label);
	}

	public void sendImage(ClientUser userFrom, String convId, String path)
			throws Exception {
		verifyPathExists(path);
		getDevicePool().getRandomDevice(userFrom).sendImage(convId, path);
	}

	public synchronized void reset() throws Exception {
		this.getDevicePool().shutdown();
	}

	private synchronized static void shutdown() {
		if (devicePool != null) {
			if (devicePool.isDone()) {
				try {
					devicePool.get().shutdown();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			} else {
				devicePool.cancel(true);
			}
		}
	}
}