package com.wearezeta.auto.ios.tools.ABProvisioner;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ABContact {
    private String name;
    private List<String> emails = new ArrayList<>();
    private List<String> phoneNumbers = new ArrayList<>();

    public ABContact(String name, Optional<List<String>> emails, Optional<List<String>> phoneNumbers) {
        this.name = name;
        if (emails.isPresent()) {
            this.emails = emails.get();
        }
        if (phoneNumbers.isPresent()) {
            this.phoneNumbers = phoneNumbers.get();
        }
    }

    private static List<String> toStringList(JSONArray arr) {
        final List<String> result = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            result.add(arr.getString(i));
        }
        return result;
    }

    private static JSONArray toJSONArray(List<String> list) {
        final JSONArray result = new JSONArray();
        list.stream().forEach(x -> result.put(x));
        return result;
    }

    public static ABContact fromJSON(JSONObject contact) {
        final JSONArray emails = contact.getJSONArray("emails");
        final JSONArray phoneNumbers = contact.getJSONArray("phone_numbers");
        return new ABContact(contact.getString("name"),
                (emails.length() > 0) ? Optional.of(toStringList(emails)) : Optional.empty(),
                (phoneNumbers.length() > 0) ? Optional.of(toStringList(phoneNumbers)) : Optional.empty());
    }

    public JSONObject toJSON() {
        final JSONObject result = new JSONObject();
        result.put("name", this.name);
        result.put("phone_numbers", toJSONArray(this.phoneNumbers));
        result.put("emails", toJSONArray(this.emails));
        return result;
    }
}
