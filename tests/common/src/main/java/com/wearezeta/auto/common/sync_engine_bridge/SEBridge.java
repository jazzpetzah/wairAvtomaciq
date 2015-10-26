package com.wearezeta.auto.common.sync_engine_bridge;

import com.wearezeta.auto.common.usrmgmt.ClientUser;

import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SEBridge {
	private static final Future<DevicePool> devicePool;
	private static final ExecutorService pool = Executors.newFixedThreadPool(1);
	static {
		devicePool = pool.submit(() -> new DevicePool());
	}
	private static SEBridge instance = null;
	private static final int POOL_CREATION_TIMEOUT = 60; // seconds

	public static synchronized SEBridge getInstance() {
		if (instance == null) {
			instance = new SEBridge();
		}
		return instance;
	}

	private SEBridge() {
	}

	private static void shutdown() {
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

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
	}

	private DevicePool getDevicePool() throws Exception {
		return devicePool.get(POOL_CREATION_TIMEOUT, TimeUnit.SECONDS);
	}

	private Map<ClientUser, List<IDevice>> usersMapping = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	private void login(ClientUser user, IDevice dstDevice) {
		if (dstDevice.hasLoggedInUser() && !dstDevice.isLoggedInUser(user)) {
			this.logout(user, dstDevice);
		}
		if (!dstDevice.isLoggedInUser(user)) {
			dstDevice.logInWithUser(user);
		}
		if (this.usersMapping.containsKey(user)) {
			final List<IDevice> mappedDevices = this.usersMapping.get(user);
			mappedDevices.add(dstDevice);
		} else {
			this.usersMapping.put(user,
					Arrays.asList(new IDevice[] { dstDevice }));
		}
	}

	private void logout(ClientUser user, IDevice dstDevice) {
		if (dstDevice.hasLoggedInUser()) {
			dstDevice.logout();
		}
		final List<IDevice> mappedDevices = this.usersMapping.get(user);
		if (mappedDevices != null && mappedDevices.contains(dstDevice)) {
			mappedDevices.remove(dstDevice);
		}
	}

	public void sendConversationMessage(ClientUser userFrom, String convId,
			String message) throws Exception {
		IDevice dstDevice = null;
		if (this.usersMapping.containsKey(userFrom)) {
			// We don't care about multiple devices yet. Just take the first
			// available one
			dstDevice = this.usersMapping.get(userFrom).get(0);
		} else {
			dstDevice = this.getDevicePool().addDevice();
			this.login(userFrom, dstDevice);
		}
		dstDevice.sendMessage(convId, message);
	}

	public void reset() throws Exception {
		this.usersMapping.clear();
		this.getDevicePool().clear();
	}
}