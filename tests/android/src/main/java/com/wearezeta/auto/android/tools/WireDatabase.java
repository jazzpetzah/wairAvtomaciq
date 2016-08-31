package com.wearezeta.auto.android.tools;


import com.google.common.io.Files;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WireDatabase {
    private static final String COLUMN_ID = "_id";
    private static final String SE_DB_FILENAME_PATTERN = "^\\w+-\\w+-\\w+-\\w+-\\w+$";
    private static final Function<String, String> SHM_FILENAME = fileName -> String.format("%s-shm", fileName);
    private static final Function<String, String> WAL_FILENAME = fileName -> String.format("%s-wal", fileName);
    private String dbFileName;
    private String packageName;

    private final FunctionalInterfaces.FunctionFor2Parameters<String, String, String> enableDbFile
            = (packageName, dbFile) -> String.format("shell \"run-as %s chmod 666 /data/data/%s/databases/%s\"",
            packageName, packageName, dbFile);

    private final FunctionalInterfaces.FunctionFor2Parameters<String, String, String> disableDbFile
            = (packageName, dbFile) -> String.format("shell \"run-as %s chmod 600 /data/data/%s/databases/%s\"",
            packageName, packageName, dbFile);

    private final Function<String, String> guessDbFile = packageName
            -> String.format("shell \"run-as %s ls /data/data/%s/databases/\"", packageName, packageName);

    private final FunctionalInterfaces.FunctionFor3Parameters<String, String, String, String> copyRemoteDb
            = (packageName, dbFile, outputDir)
            -> String.format("pull /data/data/%s/databases/%s %s", packageName, dbFile, outputDir);


    public WireDatabase() throws Exception {
        Class.forName("org.sqlite.JDBC");
        packageName = CommonUtils.getAndroidPackageFromConfig(WireDatabase.class);
        dbFileName = guessDBFileName().orElseThrow(() ->
                new IllegalStateException("Guess Wire local database name failed"));
    }

    public Optional<String> getRecentMessageId(ClientUser myself, ClientUser otherUser) throws Exception {
        return getRecentMessageId(myself, otherUser.getId());
    }

    public Optional<String> getRecentMessageId(ClientUser myself, String conversationId) throws Exception {
        Connection connection = null;
        File outputDir = syncDBWithLocalFS();
        try {
            connection = createConnection(outputDir.getAbsolutePath());
            final String queryTpl = "SELECT %s FROM Messages " +
                    "WHERE user_id=\"%s\" AND conv_id=\"%s\" " +
                    "ORDER BY local_time DESC " +
                    "LIMIT 1";
            final ResultSet rs = getQueryResult(connection, queryTpl, COLUMN_ID, myself.getId(), conversationId);
            if (rs.next()) {
                return Optional.of(rs.getString(COLUMN_ID));
            }
            return Optional.empty();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (outputDir != null) {
                FileUtils.deleteDirectory(outputDir);
            }
        }
    }

    public boolean isMessageDeleted(final String messageId) throws Exception {
        Connection connection = null;
        File outputDir = syncDBWithLocalFS();
        try {
            connection = createConnection(outputDir.getAbsolutePath());
            final String zmessageQueryTpl = "SELECT * FROM Messages WHERE %s=\"%s\"";
            final ResultSet zmessageRS = getQueryResult(connection, zmessageQueryTpl, COLUMN_ID, messageId);
            return !zmessageRS.next();
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (outputDir != null) {
                FileUtils.deleteDirectory(outputDir);
            }
        }
    }

    /**
     * SE will not refresh the the sqlite db once you send message, the new messages are still in journal,
     * Thus, it is necessary to copy all journal file back to PC FS.
     * <p>
     * This function should be called when u want to retrieve something from local database,
     * cause you need to copy the latest db file from Device to PC FS.
     *
     * @return The temporay temporary dir
     * @throws Exception
     */
    private File syncDBWithLocalFS() throws Exception {
        File outputDir = Files.createTempDir();
        for (String fileName : new String[]{dbFileName, SHM_FILENAME.apply(dbFileName), WAL_FILENAME.apply(dbFileName)}) {
            AndroidCommonUtils.executeAdb(enableDbFile.apply(packageName, fileName));
            AndroidCommonUtils.executeAdb(copyRemoteDb.apply(packageName, fileName, outputDir.getAbsolutePath()));
            AndroidCommonUtils.executeAdb(disableDbFile.apply(packageName, fileName));
        }
        return outputDir;
    }

    private Connection createConnection(String outputDirAbsolutePath) throws SQLException {
        return DriverManager.getConnection(String.format("jdbc:sqlite:%s", Paths.get(outputDirAbsolutePath, dbFileName)));
    }

    private ResultSet getQueryResult(Connection conn, String queryTpl, Object... params) throws SQLException {
        final Statement st = conn.createStatement();
        final String selectSQL = String.format(queryTpl, params);
        return st.executeQuery(selectSQL);
    }

    private Optional<String> guessDBFileName() throws Exception {
        String fileList = AndroidCommonUtils.getAdbOutput(guessDbFile.apply(packageName));

        final Pattern p = Pattern.compile(SE_DB_FILENAME_PATTERN, Pattern.MULTILINE);
        final Matcher m = p.matcher(fileList);
        if (m.find()) {
            return Optional.of(m.group(0));
        }
        return Optional.empty();
    }
}
