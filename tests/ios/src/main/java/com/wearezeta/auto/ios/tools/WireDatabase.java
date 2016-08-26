package com.wearezeta.auto.ios.tools;

import com.wearezeta.auto.common.usrmgmt.ClientUser;

import java.io.File;
import java.sql.*;
import java.util.Optional;

public class WireDatabase {
    public static final String DB_FILE_NAME = "store.wiredatabase";

    private File dbFile;

    public WireDatabase(File dbFile) throws ClassNotFoundException {
        this.dbFile = dbFile;

        Class.forName("org.sqlite.JDBC");
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbFile.getAbsolutePath()));
    }

    public Optional<Long> getRecentMessageId(ClientUser user) throws SQLException {
        Connection connection = null;
        try {
            connection = createConnection();
            final String selectSQL = "SELECT ZMESSAGE.Z_PK AS MSG_ID " +
                    "FROM ZMESSAGE " +
                    "INNER JOIN ZUSER " +
                    "ON ZMESSAGE.ZSENDER=ZUSER.Z_PK " +
                    "WHERE ZMESSAGE.ZISENCRYPTED=1 AND ZUSER.ZEMAILADDRESS=\"?\" " +
                    "ORDER BY ZMESSAGE.ZUPDATEDTIMESTAMP DESC " +
                    "LIMIT 1";
            final PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, user.getEmail());
            final ResultSet rs = preparedStatement.executeQuery(selectSQL);
            if (rs.next()) {
                return Optional.of(rs.getLong("MSG_ID"));
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return Optional.empty();
    }

    public boolean isMessageFromUserDeleted(long recentMsgId) throws SQLException {
        Connection connection = null;
        boolean result = false;
        try {
            connection = createConnection();
            final String zmessageQuery = "SELECT ZSENDERCLIENTID, ZSENDER " +
                    "FROM ZMESSAGE " +
                    "WHERE Z_PK=?";
            final PreparedStatement zmessageStatement = connection.prepareStatement(zmessageQuery);
            zmessageStatement.setLong(1, recentMsgId);
            final ResultSet zmessageRS = zmessageStatement.executeQuery(zmessageQuery);
            if (zmessageRS.next()) {
                result = zmessageRS.getObject("ZSENDERCLIENTID") == null &&
                        zmessageRS.getObject("ZSENDER") == null;
            }
            if (!result) {
                return false;
            }
            final String zmsgdataQuery = "SELECT * " +
                    "FROM ZGENERICMESSAGEDATA " +
                    "WHERE ZMESSAGE=?";
            final PreparedStatement zmsgdataStatement = connection.prepareStatement(zmsgdataQuery);
            zmsgdataStatement.setLong(1, recentMsgId);
            final ResultSet zmsgdataRS = zmessageStatement.executeQuery(zmsgdataQuery);
            return !zmsgdataRS.next();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
