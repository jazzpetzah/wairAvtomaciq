package com.wearezeta.auto.ios.tools;

import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Tag;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;

public class IOSZetaFormatter extends ZetaFormatter {

	private static final Logger log = ZetaLogger.getLog(IOSZetaFormatter.class
			.getSimpleName());

	@Override
	public void startOfScenarioLifeCycle(Scenario scenario) {
		for (Tag t : scenario.getTags()) {
			if (t.getName().equals("@deployPictures")) {
				try {
					deploySimularotPictures();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				break;
			}
			if (t.getName().equals("@deployAddressBook")) {
				try {
					deployAddressBook();
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				break;
			}
		}
	}

	private void deploySimularotPictures() throws Exception {
		if (CommonUtils.getIsSimulatorFromConfig(IOSZetaFormatter.class)) {
			try {
				String[] picturepath = new String[] { CommonUtils
						.getUserPicturePathFromConfig(IOSZetaFormatter.class) };
				IOSSimulatorHelper.createSimulatorPhotoLib("8.x", picturepath, true);
				IOSSimulatorHelper.createSimulatorAddressBook("8.x", 
						CommonUtils.getUserAddressBookFromConfig(IOSZetaFormatter.class));
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("Failed to deploy pictures into simulator.\n"
						+ ex.getMessage());
			}
		}
	}
	
	private void deployAddressBook() throws Exception {
		if (CommonUtils.getIsSimulatorFromConfig(IOSZetaFormatter.class)) {
			try {
				IOSSimulatorHelper.createSimulatorAddressBook("8.x", 
						CommonUtils.getUserAddressBookFromConfig(IOSZetaFormatter.class));
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error("Failed to deploy address book into simulator.\n"
						+ ex.getMessage());
			}
		}
	}

}
