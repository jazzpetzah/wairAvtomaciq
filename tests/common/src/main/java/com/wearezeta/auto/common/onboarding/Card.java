package com.wearezeta.auto.common.onboarding;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.auto.common.CommonUtils;

public final class Card {
	List<String> cardData = new ArrayList<String>();

	public List<String> getCardData() {
		return new ArrayList<String>(cardData);
	}

	public void setCardData(List<String> cardData) {
		this.cardData = new ArrayList<String>(cardData);
	}

	final String id = CommonUtils.generateGUID();

	public String getId() {
		return this.id;
	}

	public Card() {
	}

	public Card(List<String> cardData) {
		this.cardData = new ArrayList<String>(cardData);
	}

	public void addContact(String phoneOrEmail) {
		this.cardData.add(phoneOrEmail);
	}

	public JSONObject asJSONObject() throws Exception {
		JSONObject result = new JSONObject();
		result.put("card_id", CommonUtils.encodeSHA256Base64(this.getId()));
		JSONArray data = new JSONArray();
		for (String phoneOrEmail : this.getCardData()) {
			data.put(CommonUtils.encodeSHA256Base64(phoneOrEmail));
		}
		result.put("contact", data);
		return result;
	}
}
