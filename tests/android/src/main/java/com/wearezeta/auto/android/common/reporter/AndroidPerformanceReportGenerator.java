package com.wearezeta.auto.android.common.reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;

public class AndroidPerformanceReportGenerator {

	private static final Logger log = ZetaLogger
			.getLog(AndroidPerformanceReportGenerator.class.getSimpleName());

	private static final String RXLOG_FILENAME = "Resource0.csv";
	private static final String PERFORMANCE_REPORT_DATA_CSV = "PerfRunResult.csv";
	private static final String PERFORMANCE_REPORT_XLS_TEMPLATE = "AndroidPerformanceTemplate.xls";
	private static final String PERFORMANCE_REPORT_XLS_RESULT = "AndroidPerfReport.xls";

	public static String REPORT_DATA_PATH;
	public static String RXLOG_FILEPATH;
	private static String WIRE_PACKAGE;

	private static String buildNumber;
	private static int usersCount;
	private static ClientDeviceInfo deviceInfo;
	private static String networkType;
	private static String deviceModel;
	private static String deviceOSVersion;

	private static List<String[]> parsedRxLog = new ArrayList<String[]>();
	private static LinkedHashMap<String, Double> timeMeasurements = new LinkedHashMap<String, Double>();
	private static LinkedHashMap<String, AndroidResource> resources = new LinkedHashMap<String, AndroidResource>();

	static {
		try {
			REPORT_DATA_PATH = AndroidCommonUtils
					.getRxLogResultsPathFromConfig(AndroidPerformanceReportGenerator.class);
		} catch (Exception e) {
			REPORT_DATA_PATH = "";
			e.printStackTrace();
		}

		try {
			RXLOG_FILEPATH = AndroidCommonUtils
					.getRxLogResourceFilePathFromConfig(AndroidPerformanceReportGenerator.class);
		} catch (Exception e) {
			RXLOG_FILEPATH = REPORT_DATA_PATH + File.separator + RXLOG_FILENAME;
			e.printStackTrace();
		}

		try {
			WIRE_PACKAGE = CommonUtils
					.getAndroidPackageFromConfig(AndroidPerformanceReportGenerator.class);
		} catch (Exception e) {
			WIRE_PACKAGE = "com.waz.zclient.dev";
			e.printStackTrace();
		}
	}

	private static void initResources() {
		resources.put(ReporterConstants.Values.CPU, new AndroidResource());
		resources.put(ReporterConstants.Values.FOREGROUND_PACKAGE,
				new AndroidResource());
		resources.put(ReporterConstants.Values.FREE_PHYSICAL_MEM,
				new AndroidResource());
		resources.put(ReporterConstants.Values.FREE_STORAGE_MEM,
				new AndroidResource());
		resources.put(ReporterConstants.Values.TOTAL_TX, new AndroidResource());
		resources.put(ReporterConstants.Values.TOTAL_RX, new AndroidResource());

		String[] header = parsedRxLog.get(0);
		for (Map.Entry<String, AndroidResource> res : resources.entrySet()) {
			try {
				String key = res.getKey();
				AndroidResource resource = res.getValue();
				resource.setResourceName(res.getKey());
				resource.setResourceIndex(Arrays.asList(header).indexOf(key));
				log.debug(res.getValue());
			} catch (Exception e) {
				log.fatal("Failed set resources indexes reports.\n"
						+ e.getMessage());
				throw e;
			}
		}
		parsedRxLog.remove(0);
	}

	private static double calculateSum(List<Double> listValues) {
		double result = 0;
		for (Double value : listValues) {
			result = result + value;
		}
		result /= 1024.0;
		return result;
	}

	private static double calculateAverage(List<Double> values) {
		Double sum = 0d;
		if (!values.isEmpty()) {
			for (Double mark : values) {
				sum += mark;
			}
			return sum.doubleValue() / values.size();
		}
		return sum;
	}

	private static double calculateMedian(List<Double> listValues) {
		Double[] values = new Double[listValues.size()];
		listValues.toArray(values);
		Arrays.sort(values);
		double median;
		if (values.length % 2 == 0) {
			median = ((double) values[values.length / 2] + (double) values[values.length / 2 - 1]) / 2;
		} else {
			median = (double) values[values.length / 2];
		}
		return median;
	}

	private static LinkedHashMap<String, Double> readTimeMeasurementsData(
			String applicationLog) {
		LinkedHashMap<String, Double> times = new LinkedHashMap<String, Double>();
		times.put(ReporterConstants.Values.APPLICATION_STARTUP_TIME,
				(double) readApplicationStartupTime(applicationLog));
		times.put(ReporterConstants.Values.LOGIN_TIME,
				(double) readSuccessfulLoginTime(applicationLog));
		times.put(ReporterConstants.Values.CONV_PAGE_LOADING_TIME,
				readConversationPageLoadingTime(applicationLog));
		return times;
	}

	private static int readApplicationStartupTime(String output) {
		int result = -1;
		Pattern pattern = Pattern
				.compile(ReporterConstants.Log.APP_LAUNCH_TIME_REGEX);
		Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			try {
				result = (int) (Long.parseLong(matcher.group(1)) / 1000000);
			} catch (NumberFormatException e) {
			}
		}
		return result;
	}

	private static int readSuccessfulLoginTime(String output) {
		int result = -1;
		Pattern pattern = Pattern
				.compile(ReporterConstants.Log.LOGIN_SUCCESS_REGEX);
		Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			try {
				result = (int) (Long.parseLong(matcher.group(1)) / 1000000);
			} catch (NumberFormatException e) {
			}
		}
		return result;
	}

	private static double readConversationPageLoadingTime(String output) {
		double result = 0;
		int count = 0;
		Pattern pattern = Pattern
				.compile(ReporterConstants.Log.CONVERSATION_PAGE_VISIBLE_REGEX);
		Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			try {
				result = (int) (Long.parseLong(matcher.group(1)) / 1000000);
				log.debug("Conversation loading time " + result);
				count++;
			} catch (NumberFormatException e) {
			}
		}
		log.debug("Conversation loading count " + count);
		return result;
	}

	private static List<String[]> readRxLog() throws IOException {
		CSVReader reader = null;
		List<String[]> values = new ArrayList<String[]>();
		try {
			reader = new CSVReader(new FileReader(RXLOG_FILEPATH));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				values.add(nextLine);
			}
		} finally {
			if (reader != null)
				reader.close();
		}
		return values;
	}

	private static void readResourcesData() {
		try {
			for (String[] rxLogEntry : parsedRxLog) {
				List<String> rxLogEntryList = Arrays.asList(rxLogEntry);
				if (rxLogEntryList.get(
						resources.get(
								ReporterConstants.Values.FOREGROUND_PACKAGE)
								.getResourceIndex()).equals(WIRE_PACKAGE)) {
					AndroidResource cpu = resources
							.get(ReporterConstants.Values.CPU);
					cpu.addValue(Double.parseDouble(rxLogEntryList.get(cpu
							.getResourceIndex())));
					AndroidResource physicalMemory = resources
							.get(ReporterConstants.Values.FREE_PHYSICAL_MEM);
					physicalMemory.addValue(Double.parseDouble(rxLogEntryList
							.get(physicalMemory.getResourceIndex())));
					AndroidResource storageMemory = resources
							.get(ReporterConstants.Values.FREE_STORAGE_MEM);
					storageMemory.addValue(Double.parseDouble(rxLogEntryList
							.get(storageMemory.getResourceIndex())));
					AndroidResource transmittedData = resources
							.get(ReporterConstants.Values.TOTAL_TX);
					transmittedData.addValue(Double.parseDouble(rxLogEntryList
							.get(transmittedData.getResourceIndex())));
					AndroidResource receivedData = resources
							.get(ReporterConstants.Values.TOTAL_RX);
					receivedData.addValue(Double.parseDouble(rxLogEntryList
							.get(receivedData.getResourceIndex())));
				}
			}
		} catch (Exception ex) {
			log.error("Failed set resources values reports.\n"
					+ ex.getMessage());
			throw (ex);
		}
	}

	private static List<Double> convertBytesToMegabytes(List<Double> values) {
		List<Double> normalizedValues = new ArrayList<>();
		for (Double value : values) {
			normalizedValues.add(value / 1024 / 1024);
		}
		return normalizedValues;
	}

	private static List<Double> convertAbsoluteDataValuesToDifference(
			List<Double> values) {
		List<Double> normalizedValues = new ArrayList<Double>();
		normalizedValues.add(0.0d);
		for (int i = 1; i < values.size(); i++) {
			normalizedValues.add(values.get(i) - values.get(i - 1));
		}
		return normalizedValues;
	}

	private static void normalizeResourcesValues() {
		AndroidResource freePhysicalMemory = resources
				.get(ReporterConstants.Values.FREE_PHYSICAL_MEM);
		AndroidResource freeStorageMemory = resources
				.get(ReporterConstants.Values.FREE_STORAGE_MEM);
		AndroidResource transmittedData = resources
				.get(ReporterConstants.Values.TOTAL_TX);
		AndroidResource receivedData = resources
				.get(ReporterConstants.Values.TOTAL_RX);
		freePhysicalMemory.setValues(convertBytesToMegabytes(freePhysicalMemory
				.getValues()));
		freeStorageMemory.setValues(convertBytesToMegabytes(freeStorageMemory
				.getValues()));
		transmittedData
				.setValues(convertAbsoluteDataValuesToDifference(transmittedData
						.getValues()));
		receivedData
				.setValues(convertAbsoluteDataValuesToDifference(receivedData
						.getValues()));
	}

	private static void calculateAverageAndMedianResourcesValues() {
		AndroidResource cpu = resources.get(ReporterConstants.Values.CPU);
		AndroidResource freePhysicalMemory = resources
				.get(ReporterConstants.Values.FREE_PHYSICAL_MEM);
		AndroidResource freeStorageMemory = resources
				.get(ReporterConstants.Values.FREE_STORAGE_MEM);
		cpu.setAverageValue(calculateAverage(cpu.getValues()));
		cpu.setMedianValue(calculateMedian(cpu.getValues()));
		freePhysicalMemory.setAverageValue(calculateAverage(freePhysicalMemory
				.getValues()));
		freePhysicalMemory.setMedianValue(calculateMedian(freePhysicalMemory
				.getValues()));
		freeStorageMemory.setAverageValue(calculateAverage(freeStorageMemory
				.getValues()));
		freeStorageMemory.setMedianValue(calculateMedian(freeStorageMemory
				.getValues()));
	}

	private static void calculateTotalResourcesValues() {
		AndroidResource transmittedData = resources
				.get(ReporterConstants.Values.TOTAL_TX);
		AndroidResource receivedData = resources
				.get(ReporterConstants.Values.TOTAL_RX);
		transmittedData
				.setTotalValue(calculateSum(transmittedData.getValues()));
		receivedData.setTotalValue(calculateSum(receivedData.getValues()));
	}

	private static LinkedHashMap<String, AndroidResource> convertTimesToResources() {
		LinkedHashMap<String, AndroidResource> resources = new LinkedHashMap<String, AndroidResource>();
		for (Map.Entry<String, Double> time : timeMeasurements.entrySet()) {
			AndroidResource res = new AndroidResource();
			res.setResourceName(time.getKey());
			res.setTotalValue(time.getValue());
			resources.put(time.getKey(), res);
		}
		return resources;
	}

	private static void storeFilteredAndNormalizedResults(String filePath)
			throws IOException {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(filePath),
					CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			String[] entries = { ReporterConstants.Values.CPU,
					ReporterConstants.Values.FREE_PHYSICAL_MEM_NORMALIZED,
					ReporterConstants.Values.FREE_STORAGE_MEM_NORMALIZED,
					ReporterConstants.Values.TOTAL_TX,
					ReporterConstants.Values.TOTAL_RX };
			writer.writeNext(entries);

			AndroidResource cpu = resources.get(ReporterConstants.Values.CPU);
			AndroidResource freePhysicalMemory = resources
					.get(ReporterConstants.Values.FREE_PHYSICAL_MEM);
			AndroidResource freeStorageMemory = resources
					.get(ReporterConstants.Values.FREE_STORAGE_MEM);
			AndroidResource transmittedData = resources
					.get(ReporterConstants.Values.TOTAL_TX);
			AndroidResource receivedData = resources
					.get(ReporterConstants.Values.TOTAL_RX);

			for (int i = 0; i < cpu.getValues().size(); i++) {
				String[] values = { Double.toString(cpu.getValues().get(i)),
						Double.toString(freePhysicalMemory.getValues().get(i)),
						Double.toString(freeStorageMemory.getValues().get(i)),
						Double.toString(transmittedData.getValues().get(i)),
						Double.toString(receivedData.getValues().get(i)) };
				writer.writeNext(values);
			}
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			log.error("Failed save normalized results.\n" + ex);
			ex.printStackTrace();
			throw (ex);
		}
	}

	private static Double diffInPercents(Double previousValue,
			Double currentValue) {
		return currentValue / previousValue * 100 - 100;
	}

	private static String[] generateSummaryReportString(
			AndroidResource resouce, String[] previousRun) {
		Double previousAverageValue = previousRun[ReporterConstants.Xls.AVERAGE_VALUE_INDEX]
				.equals("null") ? null
				: Double.parseDouble(previousRun[ReporterConstants.Xls.AVERAGE_VALUE_INDEX]);
		Double previousMedianValue = previousRun[ReporterConstants.Xls.MEDIAN_VALUE_INDEX]
				.equals("null") ? null
				: Double.parseDouble(previousRun[ReporterConstants.Xls.MEDIAN_VALUE_INDEX]);
		Double previousTotalValue = Double
				.parseDouble(previousRun[ReporterConstants.Xls.TOTAL_VALUE_INDEX]);
		Double averageValueDiff = previousAverageValue != null
				&& previousAverageValue != 0 ? diffInPercents(
				previousAverageValue, resouce.getAverageValue()) : 0;
		Double medianValueDiff = previousMedianValue != null
				&& previousMedianValue != 0 ? diffInPercents(
				previousMedianValue, resouce.getMedianValue()) : 0;
		Double totalValueDiff = previousTotalValue != 0 ? diffInPercents(
				previousTotalValue, resouce.getTotalValue()) : 0;
		String[] entries = {
				resouce.getResourceName(),
				resouce.getAverageValue() != null ? resouce.getAverageValue()
						.toString() : "null",
				averageValueDiff != null ? averageValueDiff.toString() : "null",
				resouce.getMedianValue() != null ? resouce.getMedianValue()
						.toString() : "null",
				medianValueDiff != null ? medianValueDiff.toString() : "null",
				String.valueOf(resouce.getTotalValue()),
				totalValueDiff != null ? totalValueDiff.toString() : "null" };
		return entries;
	}

	private static String[] generateSummaryReportString(AndroidResource resouce) {
		String[] entries = {
				resouce.getResourceName(),
				resouce.getAverageValue() != null ? resouce.getAverageValue()
						.toString() : "null",
				"0",
				resouce.getMedianValue() != null ? resouce.getMedianValue()
						.toString() : "null", "0",
				String.valueOf(resouce.getTotalValue()), "0" };
		return entries;
	}

	private static void saveSummaryReport() throws Exception {
		boolean finished = false;
		List<String[]> savedData = new ArrayList<>();
		List<String[]> newData = new ArrayList<>();
		String resultsPath = String.format("%s%s%s_%s_%s",
				AndroidPerformanceReportGenerator.REPORT_DATA_PATH,
				File.separator, usersCount, networkType,
				PERFORMANCE_REPORT_DATA_CSV);
		File f = new File(resultsPath);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		} else {
			CSVReader reader = null;
			try {
				reader = new CSVReader(new FileReader(resultsPath));
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					savedData.add(nextLine);
				}
			} finally {
				if (reader != null)
					reader.close();
			}
		}
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(resultsPath),
					CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			if (savedData.size() > 0) {
				String[] entries = {
						new SimpleDateFormat("yyyy-MM-dd HH:mm")
								.format(Calendar.getInstance().getTime()), " ",
						buildNumber };
				newData.add(entries);
				for (Map.Entry<String, AndroidResource> entry : resources
						.entrySet()) {
					if (entry.getKey().equals(
							ReporterConstants.Values.FOREGROUND_PACKAGE))
						continue;
					boolean isFound = false;
					for (int i = 1; i < 9; i++) {
						String[] savedEntry = savedData.get(savedData.size()
								- i);
						if (savedEntry[0].equals(entry.getKey())) {
							isFound = true;
							newData.add(generateSummaryReportString(
									entry.getValue(), savedEntry));
							break;
						}
					}
					if (!isFound) {
						newData.add(generateSummaryReportString(entry
								.getValue()));
					}
				}
				finished = true;
			} else {
				String[] entries = new String[] { "Measurement",
						"Average Value", "Average Diff %", "Median Value",
						"Median Diff %", "Total Value", "Total Diff %" };

				String[] nextEntries = {
						new SimpleDateFormat("yyyy-MM-dd HH:mm")
								.format(Calendar.getInstance().getTime()), " ",
						buildNumber };
				newData.add(entries);
				newData.add(nextEntries);
				for (Map.Entry<String, AndroidResource> entry : resources
						.entrySet()) {
					newData.add(generateSummaryReportString(entry.getValue()));
				}
				finished = true;
			}
		} catch (Exception ex) {
			log.error("Failed to generate summary report string.\n"
					+ ex.getMessage());
			throw (ex);
		} finally {
			if (savedData.size() > 0) {
				writer.writeAll(savedData);
			}
			if (finished) {
				writer.writeAll(newData);
			}
			writer.flush();
			writer.close();
		}
	}

	private static String createResultFile() throws Exception {
		String resultsPath = AndroidPerformanceReportGenerator.REPORT_DATA_PATH;
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		resultsPath = resultsPath + File.separator + timeStamp + ".csv";
		try {
			File f = new File(resultsPath);
			f.getParentFile().mkdirs();
			f.createNewFile();
		} catch (Exception ex) {
			log.error("Failed to create result file.\n" + ex.getMessage());
			throw (ex);
		}
		return resultsPath;
	}

	private static void fillBuildAndDeviceData() {
		try {
			buildNumber = AndroidCommonUtils.readClientVersionFromAdb();
			deviceInfo = AndroidCommonUtils.readDeviceInfo();
			log.debug("Execution device info: " + deviceInfo.toString());
			try {
				networkType = deviceInfo.isWifiEnabled() ? "Wifi" : deviceInfo
						.getGsmNetworkType();
			} catch (NullPointerException e) {
				networkType = "Unknown";
			}
			try {
				deviceModel = deviceInfo.getDeviceName();
			} catch (NullPointerException e) {
				deviceModel = "Unknown";
			}
			try {
				deviceOSVersion = deviceInfo.getOperatingSystemBuild();
			} catch (NullPointerException e) {
				deviceOSVersion = "Unknown";
			}
		} catch (Exception e) {
		}
	}

	public static boolean updateReportDataWithCurrentRun(String applicationLog) {
		boolean generationPassed = false;
		fillBuildAndDeviceData();
		try {
			parsedRxLog = readRxLog();
			timeMeasurements = readTimeMeasurementsData(applicationLog);
			initResources();
			readResourcesData();

			normalizeResourcesValues();
			calculateAverageAndMedianResourcesValues();
			calculateTotalResourcesValues();

			storeFilteredAndNormalizedResults(createResultFile());

			resources.putAll(convertTimesToResources());

			saveSummaryReport();
			generationPassed = true;
		} catch (Exception ex) {
			log.error("Failed generate reports.\n" + ex.getMessage());
		}
		return generationPassed;
	}

	private static List<Double> averageCPU = new ArrayList<>();
	private static List<Double> medianCPU = new ArrayList<>();
	private static List<Double> averagePhysMem = new ArrayList<>();
	private static List<Double> medianPhysMem = new ArrayList<>();
	private static List<Double> averageStorageMem = new ArrayList<>();
	private static List<Double> medianStorageMem = new ArrayList<>();
	private static List<String> runDate = new ArrayList<>();
	private static List<Double> totalRX = new ArrayList<>();
	private static List<Double> totalTX = new ArrayList<>();
	private static List<Double> applicationStartupTime = new ArrayList<Double>();
	private static List<Double> loginTime = new ArrayList<Double>();
	private static List<Double> conversationLoadingTime = new ArrayList<Double>();

	private static void fillSheet(HSSFSheet summary, List<Double> values,
			int row, boolean valueShouldBeDecreased) {
		HSSFCell currentBuildCell = summary.getRow(row).getCell(2);
		HSSFCell previousBuildCell = summary.getRow(row).getCell(3);
		HSSFCell diffCell = summary.getRow(row).getCell(4);

		double currentBuild = values.get(values.size() - 1).doubleValue();
		double previousBuild = values.get(values.size() - 2).doubleValue();
		double diff = 0;
		if (valueShouldBeDecreased) {
			diff = (100 - (currentBuild / (previousBuild / 100))) / 100;
		} else {
			diff = ((currentBuild / (previousBuild / 100)) - 100) / 100;
		}
		if (currentBuildCell == null)
			currentBuildCell = summary.getRow(row).createCell(2);
		if (previousBuildCell == null)
			previousBuildCell = summary.getRow(row).createCell(3);
		if (diffCell == null)
			diffCell = summary.getRow(row).createCell(4);
		currentBuildCell.setCellValue(currentBuild);
		previousBuildCell.setCellValue(previousBuild);
		diffCell.setCellValue(diff);
	}

	public static void generateSummary(HSSFWorkbook wb) throws IOException {
		readExecutionsData();
		HSSFSheet summary = wb.getSheet(ReporterConstants.Xls.SUMMARY_SHEET);

		HSSFCell buildVersion = summary.getRow(
				ReporterConstants.Xls.BUILD_NUMBER_ROW).getCell(
				ReporterConstants.Xls.BUILD_NUMBER_COLUMN);
		HSSFCell numberOfConversations = summary.getRow(
				ReporterConstants.Xls.NUMBER_OF_CONVERSATIONS_ROW).getCell(
				ReporterConstants.Xls.BUILD_NUMBER_COLUMN);
		HSSFCell networkTypeCell = summary.getRow(
				ReporterConstants.Xls.NETWORK_TYPE_ROW).getCell(
				ReporterConstants.Xls.BUILD_NUMBER_COLUMN);
		HSSFCell phoneModelCell = summary.getRow(
				ReporterConstants.Xls.DEVICE_MODEL_ROW).getCell(
				ReporterConstants.Xls.BUILD_NUMBER_COLUMN);
		HSSFCell phoneOSVersionCell = summary.getRow(
				ReporterConstants.Xls.DEVICE_VERSION_ROW).getCell(
				ReporterConstants.Xls.BUILD_NUMBER_COLUMN);
		HSSFCell previousBuildVersionCell = summary.getRow(
				ReporterConstants.Xls.CPU_STATS_ROW - 1).getCell(3);

		CellStyle cs = wb.createCellStyle();
		cs.setWrapText(true);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		previousBuildVersionCell.setCellStyle(cs);
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cs.setFont(font);

		buildVersion.setCellValue(runDate.get(runDate.size() - 1));
		numberOfConversations.setCellValue(usersCount);
		networkTypeCell.setCellValue(networkType);
		phoneModelCell.setCellValue(deviceModel);
		phoneOSVersionCell.setCellValue(deviceOSVersion);
		previousBuildVersionCell.setCellValue("Previous Build\n"
				+ runDate.get(runDate.size() - 2));
		try {
			fillSheet(summary, medianCPU, ReporterConstants.Xls.CPU_STATS_ROW,
					true);
			fillSheet(summary, medianPhysMem,
					ReporterConstants.Xls.FREE_PHYSICAL_MEMORY_STATS_ROW, false);
			fillSheet(summary, medianStorageMem,
					ReporterConstants.Xls.FREE_STORAGE_MEMORY_STATS_ROW, false);
			fillSheet(summary, totalRX,
					ReporterConstants.Xls.TOTAL_RX_STATS_ROW, true);
			fillSheet(summary, totalTX,
					ReporterConstants.Xls.TOTAL_TX_STATS_ROW, true);

			fillSheet(summary, applicationStartupTime,
					ReporterConstants.Xls.APPLICATION_STARTUP_TIME_ROW, true);
			fillSheet(summary, loginTime, ReporterConstants.Xls.LOGIN_TIME_ROW,
					true);
			fillSheet(summary, conversationLoadingTime,
					ReporterConstants.Xls.CONVERSATION_LOADING_TIME_ROW, true);
		} catch (ArrayIndexOutOfBoundsException e) {
			log.debug("It's first execution for "
					+ usersCount
					+ " users' performance test. No diff info will be available in report.");
		}
	}

	private static LinkedHashMap<String, String[]> readExecutionsData()
			throws IOException {
		LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();

		List<String[]> savedData = new ArrayList<>();
		String resultsPath = String.format("%s%s%s_%s_%s",
				AndroidPerformanceReportGenerator.REPORT_DATA_PATH,
				File.separator, usersCount, networkType,
				PERFORMANCE_REPORT_DATA_CSV);
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(resultsPath));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				savedData.add(nextLine);
			}
		} finally {
			if (reader != null)
				reader.close();
		}
		savedData.remove(0);
		for (String[] string : savedData) {
			List<String> line = Arrays.asList(string);
			switch (line.get(ReporterConstants.Columns.MEASUREMENT_COLUMN_ID)) {
			case ReporterConstants.Values.CPU:
				averageCPU.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.AVERAGE_VALUE_INDEX)));
				medianCPU.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.MEDIAN_VALUE_INDEX)));
				break;
			case ReporterConstants.Values.FREE_PHYSICAL_MEM:
				averagePhysMem.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.AVERAGE_VALUE_INDEX)));
				medianPhysMem.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.MEDIAN_VALUE_INDEX)));
				break;
			case ReporterConstants.Values.FREE_STORAGE_MEM:
				averageStorageMem.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.AVERAGE_VALUE_INDEX)));
				medianStorageMem.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.MEDIAN_VALUE_INDEX)));
				break;
			case ReporterConstants.Values.TOTAL_RX:
				totalRX.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.TOTAL_VALUE_INDEX)));
				break;
			case ReporterConstants.Values.TOTAL_TX:
				totalTX.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.TOTAL_VALUE_INDEX)));
				break;
			case ReporterConstants.Values.APPLICATION_STARTUP_TIME:
				applicationStartupTime.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.TOTAL_VALUE_INDEX)));
				break;
			case ReporterConstants.Values.LOGIN_TIME:
				loginTime.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.TOTAL_VALUE_INDEX)));
				break;
			case ReporterConstants.Values.CONV_PAGE_LOADING_TIME:
				conversationLoadingTime.add(Double.parseDouble(line
						.get(ReporterConstants.Xls.TOTAL_VALUE_INDEX)));
				break;
			default:
				String str = line.get(2);
				String[] splited = str.split("\\s+");
				runDate.add(splited[0]);
			}
		}

		return map;
	}

	public static boolean generateRunReport() throws IOException {
		FileOutputStream fileOut = null;
		HSSFWorkbook wb = null;
		try {
			wb = new HSSFWorkbook(new FileInputStream(REPORT_DATA_PATH
					+ PERFORMANCE_REPORT_XLS_TEMPLATE));

			generateSummary(wb);

			parsedRxLog = readRxLog();
			for (Map.Entry<String, byte[]> entry : ChartBuilder.generateCharts(
					String.format("%s%s%s_%s_%s",
							AndroidPerformanceReportGenerator.REPORT_DATA_PATH,
							File.separator, usersCount, networkType,
							PERFORMANCE_REPORT_DATA_CSV)).entrySet()) {
				HSSFSheet cpuSheet = wb.getSheet(entry.getKey());
				if (cpuSheet == null) {
					cpuSheet = wb.createSheet(entry.getKey());
				}
				byte[] picture = entry.getValue();
				int my_picture_id = wb.addPicture(picture,
						Workbook.PICTURE_TYPE_JPEG);
				HSSFPatriarch drawing = cpuSheet.createDrawingPatriarch();
				ClientAnchor my_anchor = new HSSFClientAnchor();
				my_anchor.setCol1(ReporterConstants.Xls.CHART_CELL_OFFSET);
				my_anchor.setRow1(ReporterConstants.Xls.CHART_CELL_OFFSET);
				HSSFPicture my_picture = drawing.createPicture(my_anchor,
						my_picture_id);
				my_picture.resize();
			}

			fileOut = new FileOutputStream(REPORT_DATA_PATH
					+ PERFORMANCE_REPORT_XLS_RESULT);
			wb.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (fileOut != null) {
				fileOut.flush();
				fileOut.close();
			}
			if (wb != null)
				wb.close();
		}
		return true;
	}

	public static void setUsersCount(int usersCount) {
		AndroidPerformanceReportGenerator.usersCount = usersCount;
	}
}
