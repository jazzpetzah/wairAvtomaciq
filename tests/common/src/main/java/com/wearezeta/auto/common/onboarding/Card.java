package com.wearezeta.auto.common.onboarding;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
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
		result.put("card_id", encodeItem(this.getId()));
		JSONArray data = new JSONArray();
		for (String phoneOrEmail : this.getCardData()) {
			data.put(encodeItem(phoneOrEmail));
		}
		result.put("contact", data);
		return result;
	}

	public static String encodeItem(String item) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(item.getBytes("UTF-8"));
		final byte[] digest = md.digest();
		return Base64.encodeBase64String(digest);
	}
}
