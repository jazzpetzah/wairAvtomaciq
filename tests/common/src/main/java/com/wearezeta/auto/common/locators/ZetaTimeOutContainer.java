package com.wearezeta.auto.common.locators;

import java.util.concurrent.TimeUnit;

class ZetaTimeOutContainer implements ResetImplicitlyWaitTimeOut{
	private long timeOutValue;
	private TimeUnit timeUnit;

	ZetaTimeOutContainer(long initialTimeOutValue, TimeUnit initialTimeUnit){
		this.timeOutValue = initialTimeOutValue;
		this.timeUnit     = initialTimeUnit;
	}

	@Override
	public void resetImplicitlyWaitTimeOut(long timeOut, TimeUnit timeUnit) {
		this.timeOutValue = timeOut;
		this.timeUnit     = timeUnit;		
	}

	long getTimeValue(){
		return timeOutValue;
	}

	TimeUnit getTimeUnitValue(){
		return timeUnit;
	}
}
