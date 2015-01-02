package com.wearezeta.auto.common;

import com.wearezeta.auto.user_management.UserChatsHelper;
import com.wearezeta.auto.user_management.ClientUsersManager;

public final class PerformanceRunCommon {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	public ClientUsersManager getUserManager() {
		return this.usrMgr;
	}
	private final UserChatsHelper chatHelper = UserChatsHelper.getInstance();
	public UserChatsHelper getUserChatsHelper() {
		return this.chatHelper;
	}
	
	public static final int SEND_MESSAGE_NUM = 4;
	public static final int BACK_END_MESSAGE_COUNT = 5;
	public static final int MIN_WAIT_VALUE_IN_MIN = 1;
	public static final int MAX_WAIT_VALUE_IN_MIN = 2;
	
	private static PerformanceRunCommon instance = null;
	private PerformanceRunCommon() {}
	public static PerformanceRunCommon getInstance() {
		if (instance == null) {
			instance = new PerformanceRunCommon();
		}
		return instance;
	}
}
