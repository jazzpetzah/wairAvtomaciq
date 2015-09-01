package com.wearezeta.auto.zephyr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.rc.IRCTestcasesStorage;
import com.wearezeta.auto.common.rc.RCTestcase;

public class ZephyrDB implements IRCTestcasesStorage {
	private Connection conn;
	private final static String DBNAME = "itcc";
	private final static int PORT = 37638;
	private final static String USER = "root";
	private final static String PASSWORD = "root";

	private final static long DEFAULT_TESTER_ID = 2;

	private String server;

	public String getServer() {
		return this.server;
	}

	public ZephyrDB(String server) throws SQLException {
		this.server = server;
		conn = DriverManager.getConnection(String.format(
				"jdbc:mysql://%s:%d/%s?user=%s&password=%s", server, PORT,
				DBNAME, USER, PASSWORD));
	}

	public static class NoSuchZephyrTestCycleException extends Exception {
		private static final long serialVersionUID = 9115288195981715847L;

		public NoSuchZephyrTestCycleException(String msg) {
			super(msg);
		}
	}

	private ZephyrTestcase getOriginalTestcaseById(String originalId) throws Exception {
		PreparedStatement prepStmt = conn
				.prepareStatement("SELECT id, name, tag, is_automated, script_name, script_path FROM testcase WHERE id=?");
		prepStmt.setLong(1, Long.parseLong(originalId));
		ResultSet rs = prepStmt.executeQuery();
		try {
			rs.next();
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
			return new ZephyrTestcase(Long.toString(id), name,
					RCTestcase.extractTagsFromString(tags), isAutomated,
					scriptName, scriptPath);
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
		final ZephyrTestcase originalTC = getOriginalTestcaseById(realId);
		final String name = originalTC.getName();
		final Set<String> tags = originalTC.getTags();
		final boolean isAutomated = originalTC.getIsAutomated();
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
					Long.toString(executedTcId), name, comment, status, tags,
					isAutomated);
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
				final ZephyrTestPhase phase = createPhaseById(phaseId);
				updateAutomatedStatusOnPhaseTestcases(phase);
				cyclePhases.add(phase);
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

	private long addTestcaseResult(final ExecutedZephyrTestcase tc)
			throws Exception {
		PreparedStatement prepStmt = null;
		final long executionId = Long.parseLong(tc.getExecutionId());
		final ZephyrExecutionStatus executionStatus = tc.getExecutionStatus();
		if (executionStatus == ZephyrExecutionStatus.Undefined) {
			return -1;
		}

		prepStmt = conn
				.prepareStatement("INSERT INTO test_result "
						+ "(execution_date, execution_notes, execution_status, "
						+ "attachment_location, status, defect_id, tester_id, release_test_schedule_id) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		try {
			prepStmt.setTimestamp(1,
					new java.sql.Timestamp(System.currentTimeMillis()));
			prepStmt.setNull(2, java.sql.Types.VARCHAR);
			prepStmt.setString(3,
					Integer.toString(executionStatus.getId().intValue()));
			prepStmt.setNull(4, java.sql.Types.VARCHAR);
			prepStmt.setNull(5, java.sql.Types.VARCHAR);
			prepStmt.setNull(6, java.sql.Types.BIGINT);
			prepStmt.setLong(7, DEFAULT_TESTER_ID);
			prepStmt.setLong(8, executionId);
			prepStmt.execute();
		} finally {
			prepStmt.close();
		}

		prepStmt = conn
				.prepareStatement("SELECT MAX(id) as max_id FROM test_result");
		ResultSet rs = prepStmt.executeQuery();
		try {
			rs.next();
			return rs.getLong("max_id");
		} finally {
			rs.close();
			prepStmt.close();
		}
	}

	private void updateTestcaseStatus(final ExecutedZephyrTestcase tc,
			final long lastTestResultId) throws Exception {
		final long executionId = Long.parseLong(tc.getExecutionId());
		final String executionComment = tc.getExecutionComment();
		final ZephyrExecutionStatus executionStatus = tc.getExecutionStatus();

		PreparedStatement prepStmt = conn
				.prepareStatement("UPDATE release_test_schedule SET comment=?, last_test_result=? WHERE id=?");
		try {
			if (executionComment == null) {
				prepStmt.setNull(1, java.sql.Types.VARCHAR);
			} else {
				prepStmt.setString(1, executionComment);
			}
			if (executionStatus == ZephyrExecutionStatus.Undefined) {
				prepStmt.setNull(2, java.sql.Types.BIGINT);
			} else {
				prepStmt.setLong(2, lastTestResultId);
			}
			prepStmt.setLong(3, executionId);
			prepStmt.executeUpdate();
		} finally {
			prepStmt.close();
		}
	}

	public int syncPhaseResults(ZephyrTestPhase phase) throws Exception {
		final List<ExecutedZephyrTestcase> phaseTestcases = phase
				.getTestcases();
		List<ExecutedZephyrTestcase> changedTestcases = phaseTestcases.stream()
				.filter(x -> x.getIsChanged()).collect(Collectors.toList());
		for (ExecutedZephyrTestcase executedTC : changedTestcases) {
			final long lastTestResultId = addTestcaseResult(executedTC);
			updateTestcaseStatus(executedTC, lastTestResultId);
		}
		return changedTestcases.size();
	}

	private void updateAutomatedStatusOnPhaseTestcases(ZephyrTestPhase phase)
			throws Exception {
		PreparedStatement prepStmt = conn
				.prepareStatement("UPDATE testcase SET is_automated=? WHERE id=?");
		for (ExecutedZephyrTestcase tc : phase.getTestcases()) {
			if (tc.getIsAutomated()) {
				continue;
			}
			try {
				prepStmt.setBoolean(1, true);
				prepStmt.setLong(2, Long.parseLong(tc.getId()));
				prepStmt.executeUpdate();
				tc.syncIsAutomatedStatus();
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
