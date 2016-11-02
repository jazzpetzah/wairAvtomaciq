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

    private ResultSet getQueryResult(Connection conn, String queryTpl, Object... params) throws SQLException {
        final Statement st = conn.createStatement();
        final String selectSQL = String.format(queryTpl, params);
        return st.executeQuery(selectSQL);
    }

    public Optional<Long> getRecentMessageId(ClientUser user) throws SQLException {
        Connection connection = null;
        try {
            connection = createConnection();
            final String queryTpl = "SELECT ZMESSAGE.Z_PK AS MSG_ID " +
                    "FROM ZMESSAGE " +
                    "INNER JOIN ZUSER " +
                    "ON ZMESSAGE.ZSENDER=ZUSER.Z_PK " +
                    "WHERE ZUSER.ZEMAILADDRESS=\"%s\" " +
                    "ORDER BY ZMESSAGE.ZSERVERTIMESTAMP DESC " +
                    "LIMIT 1";
            final ResultSet rs = getQueryResult(connection, queryTpl, user.getEmail());
            if (rs.next()) {
                return Optional.of(rs.getLong("MSG_ID"));
            }
            return Optional.empty();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public boolean isMessageDeleted(final long msgId) throws SQLException {
        Connection connection = null;
        try {
            connection = createConnection();
            final String zmessageQueryTpl = "SELECT * FROM ZMESSAGE WHERE Z_PK=%s";
            final ResultSet zmessageRS = getQueryResult(connection, zmessageQueryTpl, msgId);
            if (zmessageRS.next()) {
                if (zmessageRS.getString("ZSENDERCLIENTID") != null) {
                    return false;
                }
            }
            final String zmsgdataQueryTpl = "SELECT * FROM ZGENERICMESSAGEDATA WHERE ZMESSAGE=%s";
            final ResultSet zmsgdataRS = getQueryResult(connection, zmsgdataQueryTpl, msgId);
            return !zmsgdataRS.next();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public String getMessageContent(long msgId) throws SQLException {
        Connection connection = null;
        try {
            connection = createConnection();
            final String zmsgdataQueryTpl = "SELECT * FROM ZGENERICMESSAGEDATA WHERE ZMESSAGE=%s";
            final ResultSet zmsgdataRS = getQueryResult(connection, zmsgdataQueryTpl, msgId);
            if (zmsgdataRS.next()) {
                return zmsgdataRS.getString("ZDATA");
            }
            throw new IllegalArgumentException(
                    String.format("There are no messages in the database with id '%s'", msgId)
            );
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
