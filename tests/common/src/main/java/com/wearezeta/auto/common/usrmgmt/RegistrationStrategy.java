package com.wearezeta.auto.common.usrmgmt;

import com.wearezeta.auto.common.Platform;

public enum RegistrationStrategy {
	ByEmail, ByPhoneNumber;

	public static RegistrationStrategy getRegistrationStrategyForPlatform(
			Platform platform) {
		switch (platform) {
		case iOS:
		case Android:
			return RegistrationStrategy.ByPhoneNumber;
		default:
			return RegistrationStrategy.ByEmail;
		}
	}
}
