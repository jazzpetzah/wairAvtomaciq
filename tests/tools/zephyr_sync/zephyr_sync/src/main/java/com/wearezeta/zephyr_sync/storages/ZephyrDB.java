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

import com.wearezeta.zephyr_sync.ExecutedZephyrTestcase;
import com.wearezeta.zephyr_sync.Testcase;
import com.wearezeta.zephyr_sync.ZephyrTestcase;

public class ZephyrDB extends TestcasesStorage {
	private Connection conn;
	private final static String DBNAME = "itcc";
	private final static int PORT = 37638;
	private final static String USER = "root";
	private final static String PASSWORD = "root";

	private final static long DEFAULT_TESTER_ID = 1;

	public ZephyrDB(String server) throws SQLException {
		conn = DriverManager.getConnection(String.format(
				"jdbc:mysql://%s:%d/%s?user=%s&password=%s", server, PORT,
				DBNAME, USER, PASSWORD));
	}

	@Override
	public List<ZephyrTestcase> getTestcases() throws Exception {
		List<ZephyrTestcase> resultList = new ArrayList<ZephyrTestcase>();

		String query = "SELECT id, name, tag, is_automated, script_name, script_path FROM testcase";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		try {
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
				String scriptPath = rs.getString("script_path");
				if (scriptPath == null) {
					scriptPath = "";
				}
				ZephyrTestcase tc = new ZephyrTestcase(Long.toString(id), name,
						Testcase.extractTagsFromString(tags), isAutomated,
						scriptName, scriptPath);
				resultList.add(tc);
			}
		} finally {
			rs.close();
			st.close();
		}

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
			throws Exception {
		PreparedStatement prepStmt = conn
				.prepareStatement("UPDATE testcase SET tag=?, is_automated=?, script_name=?, script_path=? WHERE id=?");
		try {
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
					if (((ZephyrTestcase) tc).getAutomatedScriptPath() == null
							|| ((ZephyrTestcase) tc).getAutomatedScriptPath()
									.equals("")) {
						prepStmt.setNull(4, java.sql.Types.VARCHAR);
					} else {
						prepStmt.setString(4,
								((ZephyrTestcase) tc).getAutomatedScriptPath());
					}

					prepStmt.setLong(5, id);
					prepStmt.executeUpdate();
				}
			}
		} finally {
			prepStmt.close();
		}
	}

	public static class NoSuchZephyrTestCycleException extends Exception {
		private static final long serialVersionUID = 9115288195981715847L;

		public NoSuchZephyrTestCycleException(String msg) {
			super(msg);
		}
	}

	private String getTestcaseNameById(String realId) throws Exception {
		PreparedStatement prepStmt = conn
				.prepareStatement("SELECT name FROM testcase WHERE id=?");
		prepStmt.setLong(1, Long.parseLong(realId));
		ResultSet rs = prepStmt.executeQuery();
		try {
			rs.next();
			return rs.getString("name");
		} finally {
			rs.close();
			prepStmt.close();
		}
	}

	private String getRealTestcaseIdByExecutedTestcaseId(long executedTcId)
			throws Exception {
		PreparedStatement prepStmt = conn
				.prepareStatement("SELECT tcr_catalog_tree_testcase.testcase_id AS testcase_id "
						+ "FROM tcr_catalog_tree_testcase INNER JOIN release_test_schedule "
						+ "ON release_test_schedule.tcr_catalog_id=tcr_catalog_tree_testcase.id "
						+ "WHERE release_test_schedule.id=?");
		prepStmt.setLong(1, executedTcId);
		ResultSet rs = prepStmt.executeQuery();
		try {
			rs.next();
			return rs.getString("testcase_id");
		} finally {
			rs.close();
			prepStmt.close();
		}
	}

	private ExecutedZephyrTestcase createExecutedTestcaseById(long executedTcId)
			throws Exception {
		PreparedStatement prepStmt = conn
				.prepareStatement("SELECT release_test_schedule.comment AS comment, test_result.execution_status as execution_status "
						+ "FROM test_result "
						+ "INNER JOIN release_test_schedule ON test_result.release_test_schedule_id=release_test_schedule.id "
						+ "WHERE release_test_schedule.id=?");
		final String realId = getRealTestcaseIdByExecutedTestcaseId(executedTcId);
		final String name = getTestcaseNameById(realId);
		prepStmt.setLong(1, executedTcId);
		ResultSet rs = prepStmt.executeQuery();
		try {
			String comment = null;
			ZephyrExecutionStatus status = ZephyrExecutionStatus.Undefined;
			if (rs.first()) {
				comment = rs.getString("comment");
				status = ZephyrExecutionStatus.getById(Integer.parseInt(rs
						.getString("execution_status")));
			}
			return new ExecutedZephyrTestcase(realId,
					Long.toString(executedTcId), name, comment, status,
					(status != ZephyrExecutionStatus.Undefined));
		} finally {
			rs.close();
			prepStmt.close();
		}
	}

	private ZephyrTestPhase createPhaseById(long phaseId) throws Exception {
		PreparedStatement tcCountStmt = conn
				.prepareStatement("SELECT id FROM release_test_schedule WHERE cycle_catalog_id=?");
		tcCountStmt.setLong(1, phaseId);
		ResultSet rs = tcCountStmt.executeQuery();
		try {
			List<ExecutedZephyrTestcase> phaseTestcases = new ArrayList<ExecutedZephyrTestcase>();
			while (rs.next()) {
				final long executedTcId = rs.getLong("id");
				phaseTestcases.add(createExecutedTestcaseById(executedTcId));
			}
			PreparedStatement additionalPropsStmt = conn
					.prepareStatement("SELECT tcr_catalog_tree.name AS name, cycle_catalog_map.start_date AS start_date "
							+ "FROM tcr_catalog_tree "
							+ "INNER JOIN cycle_catalog_map ON tcr_catalog_tree.id=cycle_catalog_map.tcr_catalog_tree_id "
							+ "WHERE cycle_catalog_map.id=?");
			additionalPropsStmt.setLong(1, phaseId);
			ResultSet additionalPropsRS = additionalPropsStmt.executeQuery();
			try {
				additionalPropsRS.next();
				return new ZephyrTestPhase(Long.toString(phaseId),
						additionalPropsRS.getString("name"),
						additionalPropsRS.getDate("start_date"), phaseTestcases);
			} finally {
				additionalPropsRS.close();
				additionalPropsStmt.close();
			}
		} finally {
			rs.close();
			tcCountStmt.close();
		}
	}

	private ZephyrTestCycle createTestCycleById(long cycleId, String name)
			throws Exception {
		PreparedStatement prepStmt = conn
				.prepareStatement("SELECT id FROM cycle_catalog_map WHERE cycle_id=?");
		prepStmt.setLong(1, cycleId);
		ResultSet rs = prepStmt.executeQuery();
		try {
			List<ZephyrTestPhase> cyclePhases = new ArrayList<ZephyrTestPhase>();
			while (rs.next()) {
				final long phaseId = rs.getLong("id");
				cyclePhases.add(createPhaseById(phaseId));
			}
			return new ZephyrTestCycle(Long.toString(cycleId), name,
					cyclePhases);
		} finally {
			rs.close();
			prepStmt.close();
		}
	}

	public ZephyrTestCycle getTestCycle(String name) throws Exception {
		PreparedStatement prepStmt = conn
				.prepareStatement("SELECT id FROM cycle WHERE name=? ORDER BY start_date DESC");
		prepStmt.setString(1, name);
		ResultSet rs = prepStmt.executeQuery();
		try {
			if (rs.first()) {
				final long cycleId = rs.getLong("id");
				return createTestCycleById(cycleId, name);
			} else {
				throw new NoSuchZephyrTestCycleException(
						String.format(
								"Test cycle '%s' could not be found in current Zephyr database",
								name));
			}
		} finally {
			rs.close();
			prepStmt.close();
		}
	}

	public void syncPhaseResults(ZephyrTestPhase phase) throws Exception {
		final List<ExecutedZephyrTestcase> phaseTestcases = phase
				.getTestcases();
		for (ExecutedZephyrTestcase executedTC : phaseTestcases) {
			if (!executedTC.getIsChanged()) {
				continue;
			}
			PreparedStatement prepStmt = null;
			final long executionId = Long
					.parseLong(executedTC.getExecutionId());
			final ZephyrExecutionStatus executionStatus = executedTC
					.getExecutionStatus();
			final String executionComment = executedTC.getExecutionComment();

			if (executedTC.getIsExecuted()) {
				prepStmt = conn
						.prepareStatement("UPDATE test_result SET execution_status=? WHERE release_test_schedule_id=?");
				try {
					if (executionStatus == ZephyrExecutionStatus.Undefined) {
						throw new RuntimeException(String.format(
								"Execution status field MUST be defined for the test case '[%s] %s' "
										+ "in phase '%s'", executionId,
								executedTC.getName(), phase.getName()));
					}
					prepStmt.setString(1, Integer.toString(executionStatus
							.getId().intValue()));
					prepStmt.setLong(2, executionId);
					prepStmt.executeUpdate();
				} finally {
					prepStmt.close();
				}
			} else {
				prepStmt = conn
						.prepareStatement("INSERT INTO test_result "
								+ "(execution_date, execution_notes, execution_status, "
								+ "attachment_location, status, defect_id, tester_id, release_test_schedule_id) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				try {
					prepStmt.setDate(1,
							new java.sql.Date(System.currentTimeMillis()));
					prepStmt.setNull(2, java.sql.Types.VARCHAR);
					if (executionStatus == ZephyrExecutionStatus.Undefined) {
						throw new RuntimeException(String.format(
								"Execution status field MUST be defined for the test case '[%s] %s' "
										+ "in phase '%s'", executionId,
								executedTC.getName(), phase.getName()));
					}
					prepStmt.setString(3, Integer.toString(executionStatus
							.getId().intValue()));
					prepStmt.setNull(4, java.sql.Types.VARCHAR);
					prepStmt.setNull(5, java.sql.Types.VARCHAR);
					prepStmt.setNull(6, java.sql.Types.BIGINT);
					prepStmt.setLong(7, DEFAULT_TESTER_ID);
					prepStmt.setLong(8, executionId);
					prepStmt.execute();
				} finally {
					prepStmt.close();
				}
			}

			prepStmt = conn
					.prepareStatement("UPDATE release_test_schedule SET comment=? WHERE id=?");
			try {
				if (executionComment == null) {
					prepStmt.setNull(1, java.sql.Types.VARCHAR);
				} else {
					prepStmt.setString(1, executionComment);
				}
				prepStmt.setLong(2, executionId);
				prepStmt.executeUpdate();
			} finally {
				prepStmt.close();
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
