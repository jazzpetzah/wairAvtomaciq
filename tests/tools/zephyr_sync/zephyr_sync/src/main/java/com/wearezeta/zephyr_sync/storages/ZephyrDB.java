package com.wearezeta.zephyr_sync.storages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.wearezeta.zephyr_sync.Testcase;
import com.wearezeta.zephyr_sync.ZephyrTestcase;

public class ZephyrDB extends TestcasesStorage {
	private Connection conn;
	private final static String DBNAME = "itcc";
	private final static int PORT = 37638;
	private final static String USER = "root";
	private final static String PASSWORD = "root";

	public ZephyrDB(String server) throws SQLException {
		conn = DriverManager.getConnection(String.format(
				"jdbc:mysql://%s:%d/%s?user=%s&password=%s", server, PORT,
				DBNAME, USER, PASSWORD));
	}

	@Override
	public List<ZephyrTestcase> getTestcases() throws Throwable {
		List<ZephyrTestcase> resultList = new ArrayList<ZephyrTestcase>();

		String query = "SELECT id, name, tag, is_automated, script_name FROM testcase";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			final long id = rs.getLong("id");
			final String name = rs.getString("name");
			String tags = rs.getString("tag");
			if (tags == null) {
				tags = "";
			}
			boolean isAutomated = rs.getBoolean("is_automated");
			String scriptName = rs.getString("script_name");
			if (scriptName == null) {
				scriptName = "";
			}
			ZephyrTestcase tc = new ZephyrTestcase(Long.toString(id), name,
					Testcase.extractTagsFromString(tags), isAutomated,
					scriptName);
			resultList.add(tc);
		}
		st.close();

		return resultList;
	}

	private static String tagsToDBString(Set<String> tags) {
		Set<String> resultTags = new LinkedHashSet<String>();
		for (String tag : tags) {
			if (tag.startsWith(Testcase.MAGIC_TAG_PREFIX)) {
				resultTags
						.add(tag.substring(Testcase.MAGIC_TAG_PREFIX.length()));
			} else {
				resultTags.add(tag);
			}
		}
		return StringUtils.join(resultTags, " ");
	}

	@Override
	public void syncTestcases(List<? extends Testcase> testcases)
			throws Throwable {
		PreparedStatement prepStmt = conn
				.prepareStatement("UPDATE testcase SET tag=?, is_automated=?, script_name=? WHERE id=?");
		for (Testcase tc : testcases) {
			if (!tc.getIsChanged()) {
				continue;
			}
			for (String tcId : tc.getId().split("\\s+")) {
				long id;
				try {
					id = Long.parseLong(tcId);
				} catch (NumberFormatException e) {
					continue;
				}

				if (tc.getTags().size() > 0) {
					prepStmt.setString(1, tagsToDBString(tc.getTags()));
				} else {
					prepStmt.setNull(1, java.sql.Types.VARCHAR);
				}
				if (tc.getIsAutomated()) {
					prepStmt.setBoolean(2, true);
					prepStmt.setString(3,
							((ZephyrTestcase) tc).getAutomatedScriptName());
				} else {
					prepStmt.setBoolean(2, false);
					prepStmt.setNull(3, java.sql.Types.VARCHAR);
				}
				prepStmt.setLong(4, id);
				prepStmt.executeUpdate();
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			conn.close();
		} finally {
			super.finalize();
		}
	}
}
