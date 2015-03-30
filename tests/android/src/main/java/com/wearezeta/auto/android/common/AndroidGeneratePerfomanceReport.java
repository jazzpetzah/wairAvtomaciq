package com.wearezeta.auto.android.common;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class AndroidGeneratePerfomanceReport {
	private static final Logger log = ZetaLogger
			.getLog(AndroidCommonUtils.class.getSimpleName());
	private final static String RUN_RESULT_NAME = "PerfRunResult.csv";
	private final static String CPU = "Current CPU %";
	private final static String PACKAGE = "Foreground Package";
	private final static String FREE_PHYSICAL_MEM = "Free Physical Mem (b)";
	private final static String FREE_STORAGE_MEM = "Free Storage Mem (b)";
	private final static String TOTAL_TX = "Total TX";
	private final static String TOTAL_RX = "Total RX";
	private static CSVReader reader;
	private static List<String[]> resultValues = new ArrayList<>();
	private static AndroidResource cpu = new AndroidResource();
	private static AndroidResource packageName = new AndroidResource();
	private static AndroidResource physMem = new AndroidResource();
	private static AndroidResource storMem = new AndroidResource();
	private static AndroidResource tx = new AndroidResource();
	private static AndroidResource rx = new AndroidResource();
	private static CSVWriter writer;

	public static boolean generateRunReport() throws Exception {
		boolean generationPassed = false;
		try {
			resultValues = readFromCSV();
			setResourcesNames();
			setResourcesIndexes();
			setResourcesValues();
			normalizeResourcesValues();
			getAverageResourcesValues();
			getMedianResourcesValues();
			getTotalResourcesValues();
			saveNormalizedResults(createResultFile());
			saveSummaryReport();
			generationPassed = true;
		} catch (Exception ex) {
			log.error("Failed generate reports.\n" + ex.getMessage());
		}
		return generationPassed;
	}

	private static List<String[]> readFromCSV() throws Exception {
		reader = new CSVReader(
				new FileReader(
						AndroidCommonUtils
								.getRxLogResourceFilePathFromConfig(AndroidGeneratePerfomanceReport.class)));
		String[] nextLine;
		List<String[]> values = new ArrayList<String[]>();
		while ((nextLine = reader.readNext()) != null) {
			values.add(nextLine);
		}
		return values;
	}

	private static void setResourcesNames() {
		cpu.setResourceName(CPU);
		packageName.setResourceName(PACKAGE);
		physMem.setResourceName(FREE_PHYSICAL_MEM);
		storMem.setResourceName(FREE_STORAGE_MEM);
		tx.setResourceName(TOTAL_TX);
		rx.setResourceName(TOTAL_RX);
	}

	private static void setResourcesIndexes() {
		String[] resourceNames = resultValues.get(0);
		try {
			cpu.setResourceIndex(Arrays.asList(resourceNames).indexOf(
					cpu.getResourceName()));
			packageName.setResourceIndex(Arrays.asList(resourceNames).indexOf(
					packageName.getResourceName()));
			physMem.setResourceIndex(Arrays.asList(resourceNames).indexOf(
					physMem.getResourceName()));
			storMem.setResourceIndex(Arrays.asList(resourceNames).indexOf(
					storMem.getResourceName()));
			tx.setResourceIndex(Arrays.asList(resourceNames).indexOf(
					tx.getResourceName()));
			rx.setResourceIndex(Arrays.asList(resourceNames).indexOf(
					rx.getResourceName()));
			resultValues.remove(0);
		} catch (Exception ex) {
			log.error("Failed set resources indexes reports.\n"
					+ ex.getMessage());
			throw (ex);
		}
	}

	private static void setResourcesValues() {
		try {
			for (Iterator<String[]> iterator = resultValues.iterator(); iterator
					.hasNext();) {
				String[] values = iterator.next();
				if (Arrays
						.asList(values)
						.get(packageName.getResourceIndex())
						.equals(CommonUtils
								.getAndroidPackageFromConfig(AndroidGeneratePerfomanceReport.class))) {
					cpu.addValue(Double.parseDouble(Arrays.asList(values).get(
							cpu.getResourceIndex())));
					physMem.addValue(Double.parseDouble(Arrays.asList(values)
							.get(physMem.getResourceIndex())));
					storMem.addValue(Double.parseDouble(Arrays.asList(values)
							.get(storMem.getResourceIndex())));
					tx.addValue(Double.parseDouble(Arrays.asList(values).get(
							tx.getResourceIndex())));
					rx.addValue(Double.parseDouble(Arrays.asList(values).get(
							rx.getResourceIndex())));
				}
			}
		} catch (Exception ex) {
			log.error("Failed set resources values reports.\n"
					+ ex.getMessage());
			throw (ex);
		}
	}

	private static void normalizeResourcesValues() {
		physMem.setValues(memoryValuesNormalizer(physMem.getValues()));
		storMem.setValues(memoryValuesNormalizer(storMem.getValues()));
		tx.setValues(networkValuesNormalizer(tx.getValues()));
		rx.setValues(networkValuesNormalizer(rx.getValues()));
	}

	private static void getAverageResourcesValues() {
		cpu.setAverageValue(calculateAverage(cpu.getValues()));
		physMem.setAverageValue(calculateAverage(physMem.getValues()));
		storMem.setAverageValue(calculateAverage(storMem.getValues()));
	}

	private static void getMedianResourcesValues() {
		cpu.setMedianValue(calculateMedian(cpu.getValues()));
		physMem.setMedianValue(calculateMedian(physMem.getValues()));
		storMem.setMedianValue(calculateMedian(storMem.getValues()));
	}

	private static void getTotalResourcesValues() {
		tx.setTotalValue(calculateSum(tx.getValues()));
		rx.setTotalValue(calculateSum(rx.getValues()));
	}

	private static List<Double> memoryValuesNormalizer(List<Double> values) {
		List<Double> normalizedValues = new ArrayList<>();
		for (Double value : values) {
			normalizedValues.add(value / 1024 / 1024); // In Mb
		}
		return normalizedValues;
	}

	private static List<Double> networkValuesNormalizer(List<Double> values) {
		List<Double> normalizedValues = new ArrayList<>();
		Double startValue = values.get(0);
		for (Double value : values) {
			normalizedValues.add(value - startValue);
		}
		return normalizedValues;
	}

	private static double calculateSum(List<Double> listValues) {
		double result = 0;
		for (Double value : listValues) {
			result = result + value;
		}
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

	private static void saveNormalizedResults(String filePath)
			throws IOException {
		try {
			writer = new CSVWriter(new FileWriter(filePath),
					CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			String[] entries = {
					cpu.getResourceName(),
					physMem.getResourceName().substring(0,
							physMem.getResourceName().length() - 3),
					storMem.getResourceName().substring(0,
							storMem.getResourceName().length() - 3),
					tx.getResourceName(), rx.getResourceName() };
			writer.writeNext(entries);

			for (int i = 0; i < cpu.getValues().size(); i++) {
				String[] values = { Double.toString(cpu.getValues().get(i)),
						Double.toString(physMem.getValues().get(i)),
						Double.toString(storMem.getValues().get(i)),
						Double.toString(tx.getValues().get(i)),
						Double.toString(rx.getValues().get(i)) };
				writer.writeNext(values);
			}
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			log.error("Failed save normalized results.\n" + ex.getMessage());
			throw (ex);
		}
	}

	private static void saveSummaryReport() throws Exception {
		boolean finished = false;
		List<String[]> savedData = new ArrayList<>();
		List<String[]> newData = new ArrayList<>();
		String resultsPath = AndroidCommonUtils
				.getRxLogResultsPathFromConfig(AndroidGeneratePerfomanceReport.class)
				+ File.separator + RUN_RESULT_NAME;
		File f = new File(resultsPath);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		} else {
			reader = new CSVReader(new FileReader(resultsPath));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				savedData.add(nextLine);
			}
		}
		try {
			writer = new CSVWriter(new FileWriter(resultsPath),
					CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			if (savedData.size() > 0) {
				String[] entries = {
						new SimpleDateFormat("yyyy-MM-dd HH:mm")
								.format(Calendar.getInstance().getTime()), " ",
						AndroidCommonUtils.readClientVersionFromAdb() };
				newData.add(entries);
				newData.add(generateSummaryReportString(cpu,
						savedData.get(savedData.size() - 5)));
				newData.add(generateSummaryReportString(physMem,
						savedData.get(savedData.size() - 4)));
				newData.add(generateSummaryReportString(storMem,
						savedData.get(savedData.size() - 3)));
				newData.add(generateSummaryReportString(tx,
						savedData.get(savedData.size() - 2)));
				newData.add(generateSummaryReportString(rx,
						savedData.get(savedData.size() - 1)));
				finished = true;
			} else {
				String[] entries = "Measurement,Average Value,Average Diff %,Median Value,Median Diff %,Total Value,Total Diff %"
						.split(",");
				String[] nextEntries = {
						new SimpleDateFormat("yyyy-MM-dd HH:mm")
								.format(Calendar.getInstance().getTime()), " ",
						AndroidCommonUtils.readClientVersionFromAdb() };
				newData.add(entries);
				newData.add(nextEntries);
				newData.add(generateSummaryReportString(cpu));
				newData.add(generateSummaryReportString(physMem));
				newData.add(generateSummaryReportString(storMem));
				newData.add(generateSummaryReportString(tx));
				newData.add(generateSummaryReportString(rx));
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
		String resultsPath = AndroidCommonUtils
				.getRxLogResultsPathFromConfig(AndroidGeneratePerfomanceReport.class);
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

	private static String[] generateSummaryReportString(
			AndroidResource resouce, String[] previousRun) {
		Double previousAverageValue = previousRun[1].equals("null") ? null
				: Double.parseDouble(previousRun[1]);
		Double previousMedianValue = previousRun[3].equals("null") ? null
				: Double.parseDouble(previousRun[3]);
		Double previousTotalValue = Double.parseDouble(previousRun[5]);
		Double averageValueDif = previousAverageValue != null
				&& previousAverageValue != 0 ? (resouce.getAverageValue()
				/ previousAverageValue * 100 - 100) : 0;
		Double medianValueDif = previousMedianValue != null
				&& previousMedianValue != 0 ? (resouce.getMedianValue()
				/ previousMedianValue * 100 - 100) : 0;
		Double totalValueDif = previousTotalValue != 0 ? resouce
				.getTotalValue() / previousTotalValue * 100 - 100 : 0;
		String[] entries = {
				resouce.getResourceName(),
				resouce.getAverageValue() != null ? resouce.getAverageValue()
						.toString() : "null",
				averageValueDif != null ? averageValueDif.toString() : "null",
				resouce.getMedianValue() != null ? resouce.getMedianValue()
						.toString() : "null",
				medianValueDif != null ? medianValueDif.toString() : "null",
				String.valueOf(resouce.getTotalValue()),
				totalValueDif != null ? totalValueDif.toString() : "null" };
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
}
