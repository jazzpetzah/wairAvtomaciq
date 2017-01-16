package com.wearezeta.auto.common.onboarding;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.auto.common.CommonUtils;

public final class AddressBook {
    private List<String> selfData = new ArrayList<>();

    public List<String> getSelfData() {
        return new ArrayList<>(selfData);
    }

    public void setSelfData(List<String> selfData) {
        this.selfData = new ArrayList<>(selfData);
    }

    private List<Card> cards = new ArrayList<>();

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public void setCards(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public AddressBook() {
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public JSONObject asJSONObject() throws Exception {
        assert !this.getSelfData().isEmpty() || !this.getCards().isEmpty();
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
