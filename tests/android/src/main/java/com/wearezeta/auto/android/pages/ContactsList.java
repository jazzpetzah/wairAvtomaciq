package com.wearezeta.auto.android.pages;

import java.net.MalformedURLException;
import java.util.HashMap;

import com.wearezeta.auto.common.DriverUtils;

public class ContactsList extends AndroidPage {

	public ContactsList(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}
	
	public Boolean isVisible(String contact) {
		
		HashMap<String,Integer> usersMap = DriverUtils.waitForElementWithTextByClassName("android.widget.TextView", driver);
		
		return usersMap.containsKey(contact);
	}

}
