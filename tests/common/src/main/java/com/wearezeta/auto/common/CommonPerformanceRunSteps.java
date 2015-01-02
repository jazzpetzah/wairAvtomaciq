package com.wearezeta.auto.common;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.user_management.UserChatsHelper;
import com.wearezeta.auto.user_management.UsersManager;

public abstract class CommonPerformanceRunSteps {
	protected final UsersManager usrMgr = UsersManager.getInstance();
	protected final UserChatsHelper chatHelper = UserChatsHelper.getInstance();
	
	protected final Logger log = ZetaLogger.getLog(CommonPerformanceRunSteps.class.getSimpleName());

	protected static final int SEND_MESSAGE_NUM = 4;
	protected static final int BACK_END_MESSAGE_COUNT = 5;
	protected static final int MIN_WAIT_VALUE_IN_MIN = 1;
	protected static final int MAX_WAIT_VALUE_IN_MIN = 2;
}
