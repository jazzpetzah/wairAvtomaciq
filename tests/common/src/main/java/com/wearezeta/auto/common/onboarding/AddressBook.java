package com.wearezeta.auto.common.onboarding;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.auto.common.CommonUtils;

public final class AddressBook {
	List<String> selfData = new ArrayList<String>();

	public List<String> getSelfData() {
		return new ArrayList<String>(selfData);
	}

	public void setSelfData(List<String> selfData) {
		this.selfData = new ArrayList<String>(selfData);
	}

	List<Card> cards = new ArrayList<Card>();

	public List<Card> getCards() {
		return new ArrayList<Card>(cards);
	}

	public void setCards(List<Card> cards) {
		this.cards = new ArrayList<Card>(cards);
	}

	public AddressBook() {
	}

	public AddressBook(List<String> selfData, List<Card> cards) {
		this.selfData = new ArrayList<String>(selfData);
		this.cards = new ArrayList<Card>(cards);
	}

	public void addCard(Card card) {
		this.cards.add(card);
	}

	public void addSelfItem(String phoneOrEmail) {
		this.selfData.add(phoneOrEmail);
	}

	public JSONObject asJSONObject() throws Exception {
		JSONObject result = new JSONObject();
		JSONArray selfData = new JSONArray();
		for (String phoneOrEmail : this.getSelfData()) {
			selfData.put(CommonUtils.encodeSHA256Base64(phoneOrEmail));
		}
		result.put("self", selfData);
		JSONArray cards = new JSONArray();
		for (Card card : this.getCards()) {
			cards.put(card.asJSONObject());
		}
		result.put("cards", cards);
		return result;
	}
}
