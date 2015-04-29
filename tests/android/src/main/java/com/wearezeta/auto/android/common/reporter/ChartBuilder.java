package com.wearezeta.auto.android.common.reporter;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.opencsv.CSVReader;

public class ChartBuilder {
	private static CSVReader reader;
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

	public static LinkedHashMap<String, byte[]> generateCharts(
			String resultsPath) throws IOException {
		LinkedHashMap<String, byte[]> charts = new LinkedHashMap<String, byte[]>();

		List<String[]> savedData = new ArrayList<>();
		reader = new CSVReader(new FileReader(resultsPath));
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			savedData.add(nextLine);
		}
		savedData.remove(0);
		for (String[] string : savedData) {
			List<String> line = Arrays.asList(string);
			switch (line.get(ReporterConstants.Columns.MEASUREMENT_COLUMN_ID)) {
			case ReporterConstants.Values.CPU:
				averageCPU.add(Double.parseDouble(line.get(1)));
				medianCPU.add(Double.parseDouble(line.get(3)));
				break;
			case ReporterConstants.Values.FREE_PHYSICAL_MEM:
				averagePhysMem.add(Double.parseDouble(line.get(1)));
				medianPhysMem.add(Double.parseDouble(line.get(3)));
				break;
			case ReporterConstants.Values.FREE_STORAGE_MEM:
				averageStorageMem.add(Double.parseDouble(line.get(1)));
				medianStorageMem.add(Double.parseDouble(line.get(3)));
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

		charts.put(ReporterConstants.Values.CPU, BarChart.buildAverageBarChart(
				averageCPU, medianCPU, runDate, ReporterConstants.Values.CPU,
				"Runs", "Load", 1024, 700, "Average", "Median", "%"));
		charts.put(ReporterConstants.Values.FREE_PHYSICAL_MEM, BarChart
				.buildAverageBarChart(averagePhysMem, medianPhysMem, runDate,
						ReporterConstants.Values.FREE_PHYSICAL_MEM, "Runs",
						"Size", 1024, 700, "Average", "Median", "mb"));
		charts.put(ReporterConstants.Values.FREE_STORAGE_MEM, BarChart
				.buildAverageBarChart(averageStorageMem, medianStorageMem,
						runDate, ReporterConstants.Values.FREE_STORAGE_MEM,
						"Runs", "Size", 1024, 700, "Average", "Median", "mb"));
		charts.put(ReporterConstants.Values.TOTAL_RX + " "
				+ ReporterConstants.Values.TOTAL_TX, BarChart
				.buildRxTxBarChart(totalRX, totalTX, runDate,
						ReporterConstants.Values.TOTAL_RX + " "
								+ ReporterConstants.Values.TOTAL_TX, "Runs",
						"Size", 1024, 700, "Total RX", "Total TX", "mb"));
		charts.put(ReporterConstants.Values.APPLICATION_STARTUP_TIME, BarChart
				.buildTimeBarChart(applicationStartupTime, runDate,
						ReporterConstants.Values.APPLICATION_STARTUP_TIME,
						"Runs", "Size", 1024, 700, "Average", "Median", "ms"));
		charts.put(ReporterConstants.Values.LOGIN_TIME, BarChart
				.buildTimeBarChart(loginTime, runDate,
						ReporterConstants.Values.LOGIN_TIME, "Runs", "Size",
						1024, 700, "Average", "Median", "ms"));
		charts.put(ReporterConstants.Values.CONV_PAGE_LOADING_TIME, BarChart
				.buildTimeBarChart(conversationLoadingTime, runDate,
						ReporterConstants.Values.CONV_PAGE_LOADING_TIME,
						"Runs", "Size", 1024, 700, "Average", "Median", "ms"));
		return charts;
	}
}
