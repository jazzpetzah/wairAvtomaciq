package com.wearezeta.auto.common.wire_actors;

import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.common.rest.CommonRESTHandlers;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActorsRESTWrapper {
    public static boolean isAlive() {
        try {
            return CommonRESTHandlers.isAlive(new URL(ActorsREST.getBaseURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static String createDevice(Optional<String> name) throws Exception {
        final JSONObject response = ActorsREST.createDevice(name);
        return response.getString("uuid");
    }

    static void removeDevice(String uuid) throws Exception {
        ActorsREST.removeDevice(uuid);
    }

    static String loginToDevice(String uuid, ClientUser user) throws Exception {
        final JSONObject response = ActorsREST.loginToDevice(uuid, user.getEmail(), user.getPassword());
        return response.getString("uuid");
    }

    static String getDeviceFingerprint(String uuid) throws Exception {
        final JSONObject response = ActorsREST.getDeviceFingerprint(uuid);
        return response.getString("fingerprint");
    }

    static String getDeviceId(String uuid) throws Exception {
        final JSONObject response = ActorsREST.getDeviceId(uuid);
        return response.getString("deviceId");
    }

    static String getUniqueUsername(String uuid) throws Exception {
        final JSONObject response = ActorsREST.getUniqueUsername(uuid);
        return response.getJSONObject("user").getString("uniqueName");
    }

    static String setUniqueUsername(String uuid, String newUsername) throws Exception {
        final JSONObject response = ActorsREST.setUniqueUsername(uuid, newUsername);
        return response.getString("uuid");
    }

    static String setDeviceLabel(String uuid, String newLabel) throws Exception {
        final JSONObject response = ActorsREST.setDeviceLabel(uuid, newLabel);
        return response.getString("uuid");
    }

    public enum AssetsVersion {
        V2("2"), V3("3");

        private String strRepresentation;

        AssetsVersion(String strRepresentation) {
            this.strRepresentation = strRepresentation;
        }

        public String stringRepresentation() {
            return this.strRepresentation;
        }
    }

    static String setDeviceAssetsVersion(String uuid, AssetsVersion newVersion) throws Exception {
        final JSONObject response = ActorsREST.setDeviceAssetsVersion(uuid, newVersion.stringRepresentation());
        return response.getString("uuid");
    }

    static String sendMessage(String uuid, String convoId, String message) throws Exception {
        final JSONObject response = ActorsREST.sendMessage(uuid, convoId, message);
        return response.getString("uuid");
    }

    static String sendGiphy(String uuid, String convoId, String giphyTag) throws Exception {
        final JSONObject response = ActorsREST.sendGiphy(uuid, convoId, giphyTag);
        return response.getString("uuid");
    }

    public static class LocationInfo {
        private float lon;
        private float lat;
        private String address;
        private int zoom;

        public LocationInfo(float lon, float lat, String address, int zoom) {
            this.lon = lon;
            this.lat = lat;
            this.address = address;
            this.zoom = zoom;
        }

        public float getLon() {
            return lon;
        }

        public float getLat() {
            return lat;
        }

        public String getAddress() {
            return address;
        }

        public int getZoom() {
            return zoom;
        }
    }

    static String sendLocation(String uuid, String convoId, LocationInfo locationInfo) throws Exception {
        final JSONObject response = ActorsREST.sendLocation(uuid, convoId, locationInfo.getLon(),
                locationInfo.getLat(), locationInfo.getAddress(), locationInfo.getZoom());
        return response.getString("uuid");
    }

    static String sendLocation(String uuid, String convoId) throws Exception {
        final JSONObject response = ActorsREST.sendLocation(uuid, convoId);
        return response.getString("uuid");
    }

    private static String fileToBase64String(File srcFile) throws Exception {
        if (!srcFile.exists()) {
            throw new IllegalArgumentException(
                    String.format("The file at path '%s' is not accessible or does not exist",
                            srcFile.getAbsolutePath())
            );
        }
        final byte[] asBytes = Files.readAllBytes(srcFile.toPath());
        return Base64.encodeBase64String(asBytes);
    }

    static String sendImage(String uuid, String convoId, File image) throws Exception {
        final JSONObject response = ActorsREST.sendImage(uuid, convoId, fileToBase64String(image), image.getName());
        return response.getString("uuid");
    }

    static String sendFile(String uuid, String convoId, File file, String mimeType) throws Exception {
        final JSONObject response = ActorsREST.sendFile(uuid, convoId, fileToBase64String(file),
                mimeType, file.getName());
        return response.getString("uuid");
    }

    static String sendPing(String uuid, String convoId) throws Exception {
        final JSONObject response = ActorsREST.sendPing(uuid, convoId);
        return response.getString("uuid");
    }

    static String sendTyping(String uuid, String convoId) throws Exception {
        final JSONObject response = ActorsREST.sendTyping(uuid, convoId);
        return response.getString("uuid");
    }

    static String clearConversation(String uuid, String convoId) throws Exception {
        final JSONObject response = ActorsREST.clearConversation(uuid, convoId);
        return response.getString("uuid");
    }

    static String muteConversation(String uuid, String convoId) throws Exception {
        final JSONObject response = ActorsREST.muteConversation(uuid, convoId);
        return response.getString("uuid");
    }

    static String unmuteConversation(String uuid, String convoId) throws Exception {
        final JSONObject response = ActorsREST.unmuteConversation(uuid, convoId);
        return response.getString("uuid");
    }

    static String archiveConversation(String uuid, String convoId) throws Exception {
        final JSONObject response = ActorsREST.archiveConversation(uuid, convoId);
        return response.getString("uuid");
    }

    static String unarchiveConversation(String uuid, String convoId) throws Exception {
        final JSONObject response = ActorsREST.unarchiveConversation(uuid, convoId);
        return response.getString("uuid");
    }

    static String setEphemeralTimeout(String uuid, String convoId, Timedelta timeout) throws Exception {
        final JSONObject response = ActorsREST.setEpehemeralTimeout(uuid, convoId, timeout);
        return response.getString("uuid");
    }

    static String readEphemeralMessage(String uuid, String convoId, String msgId) throws Exception {
        final JSONObject response = ActorsREST.readEphemeralMessage(uuid, convoId, msgId);
        return response.getString("uuid");
    }

    static String cancelConnection(String uuid, String connId) throws Exception {
        final JSONObject response = ActorsREST.cancelConnection(uuid, connId);
        return response.getString("uuid");
    }

    static String deleteMessage(String uuid, String convId, String msgId) throws Exception {
        final JSONObject response = ActorsREST.deleteMessage(uuid, convId, msgId);
        return response.getString("uuid");
    }

    static String deleteMessageEverywhere(String uuid, String convId, String msgId) throws Exception {
        final JSONObject response = ActorsREST.deleteMessageEverywhere(uuid, convId, msgId);
        return response.getString("uuid");
    }

    static String updateMessage(String uuid, String msgId, String newMessage) throws Exception {
        final JSONObject response = ActorsREST.updateMessage(uuid, msgId, newMessage);
        return response.getString("uuid");
    }

    public enum MessageReaction {
        LIKE, UNLIKE
    }

    static String reactMessage(String uuid, String convId, String msgId, MessageReaction reaction)
            throws Exception {
        final JSONObject response = ActorsREST.reactMessage(uuid, convId, msgId, reaction.name());
        return response.getString("uuid");
    }

    public enum MessageType {
        TEXT,
        TEXT_EMOJI_ONLY,
        ASSET,
        ANY_ASSET,
        VIDEO_ASSET,
        AUDIO_ASSET,
        KNOCK,
        MEMBER_JOIN,
        MEMBER_LEAVE,
        CONNECT_REQUEST,
        CONNECT_ACCEPTED,
        RENAME,
        MISSED_CALL,
        INCOMING_CALL,
        RICH_MEDIA,
        OTR_ERROR,
        OTR_IDENTITY_CHANGED,
        OTR_VERIFIED,
        OTR_UNVERIFIED,
        OTR_DEVICE_ADDED,
        STARTED_USING_DEVICE,
        HISTORY_LOST,
        LOCATION,
        UNKNOWN,
        RECALLED
    }

    public static class MessageInfo {
        private String id;
        private MessageType type;
        private Timedelta timestamp;

        public MessageInfo(String id, MessageType type, Timedelta timestamp) {
            this.id = id;
            this.type = type;
            this.timestamp = timestamp;
        }

        public String getId() {
            return id;
        }

        public MessageType getType() {
            return type;
        }

        public Timedelta getTimestamp() {
            return timestamp;
        }
    }

    static List<MessageInfo> getMessagesInfo(String uuid, String convId) throws Exception {
        final List<MessageInfo> result = new ArrayList<>();
        final JSONObject response = ActorsREST.getMessagesInfo(uuid, convId);
        final JSONArray allConversations = response.getJSONArray("conversations");
        for (int i = 0; i < allConversations.length(); i++) {
            if (allConversations.getJSONObject(i).getString("id").equals(convId)) {
                final JSONArray allMessages = allConversations.getJSONObject(i).getJSONArray("messagesInfo");
                for (int j = 0; j < allMessages.length(); j++) {
                    final JSONObject messageInfoAsJson = allMessages.getJSONObject(i);
                    final MessageInfo messageInfo = new MessageInfo(
                            messageInfoAsJson.getString("messageId"),
                            MessageType.valueOf(messageInfoAsJson.getString("type")),
                            Timedelta.fromMilliSeconds(messageInfoAsJson.getLong("time"))
                    );
                    result.add(messageInfo);
                }
                break;
            }
        }
        return result;
    }

}


