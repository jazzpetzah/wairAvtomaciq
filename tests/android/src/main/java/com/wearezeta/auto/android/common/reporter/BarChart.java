package com.wearezeta.auto.android.common.reporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart {

	public static final int VALUE_BYTES = 1;
	public static final int VALUE_PERCENTS = 2;
	public static final int VALUE_SECONDS = 3;

	public static final int CHART_WIDTH = 640;
	public static final int CHART_HEIGHT = 480;

	public static byte[] buildRxTxBarChart(List<Double> rxValues,
			List<Double> txValues, List<String> dates, String chartName,
			String xName, String yName, int chartWidth, int chartHeight,
			String firstValuesName, String secondValuesName, String type)
			throws IOException {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Double> procAverage = new ArrayList<Double>();
		for (Double av : rxValues) {
			procAverage.add(av);
		}
		List<Double> procMedian = new ArrayList<Double>();
		for (Double me : txValues) {
			procMedian.add(me);
		}
		for (int i = 0; i < rxValues.size(); i++) {
			dataset.addValue(procAverage.get(i), firstValuesName, dates.get(i));
			dataset.addValue(procMedian.get(i), secondValuesName, dates.get(i));
		}
		double min = Double.MAX_VALUE;
		double max = 0;
		for (Double doub : procMedian) {
			if (doub < min)
				min = doub;
			if (doub > max)
				max = doub;
		}

		for (Double doub : procAverage) {
			if (doub < min)
				min = doub;
			if (doub > max)
				max = doub;
		}

		return buildChart(chartName, xName, yName, dataset, min, max,
				VALUE_BYTES, chartWidth, chartHeight);
	}

	public static byte[] buildTimeBarChart(List<Double> medianValues,
			List<String> dates, String chartName, String xName, String yName,
			int chartWidth, int chartHeight, String firstValuesName,
			String secondValuesName, String type) throws IOException {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < medianValues.size(); i++) {
			dataset.addValue(medianValues.get(i), secondValuesName,
					dates.get(i));
		}
		double min = Double.MAX_VALUE;
		double max = 0;
		for (Double doub : medianValues) {
			if (doub < min)
				min = doub;
			if (doub > max)
				max = doub;
		}

		return buildChart(chartName, xName, yName, dataset, min, max,
				VALUE_SECONDS, chartWidth, chartHeight);
	}

	public static byte[] buildAverageBarChart(List<Double> averageValues,
			List<Double> medianValues, List<String> dates, String chartName,
			String xName, String yName, int chartWidth, int chartHeight,
			String firstValuesName, String secondValuesName, String type)
			throws IOException {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < medianValues.size(); i++) {
			/*
			 * dataset.addValue(firstValuesList.get(i), firstValuesName,
			 * dates.get(i));
			 */
			dataset.addValue(medianValues.get(i), secondValuesName,
					dates.get(i));
		}
		double min = Double.MAX_VALUE;
		double max = 0;
		for (Double doub : medianValues) {
			if (doub < min)
				min = doub;
			if (doub > max)
				max = doub;
		}

		return buildChart(chartName, xName, yName, dataset, min, max,
				VALUE_PERCENTS, chartWidth, chartHeight);
	}

	private static byte[] buildChart(String chartName, String xName,
			String yName, DefaultCategoryDataset dataset, double minValue,
			double maxValue, int valueType, int chartWidth, int chartHeight)
			throws IOException {

		JFreeChart lineChartObject = ChartFactory.createBarChart(chartName,
				xName, yName, dataset, PlotOrientation.VERTICAL, true, true,
				false);
		final CategoryPlot plot = lineChartObject.getCategoryPlot();
		ValueAxis yAxis = plot.getRangeAxis();
		double lowerBound = minValue - (maxValue - minValue);
		double upperBound = maxValue + (maxValue - minValue);
		yAxis.setLowerBound(lowerBound < 0 ? 0 : lowerBound);
		yAxis.setUpperBound(upperBound);
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setItemMargin(0);
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setMaximumBarWidth(10);
		NumberFormat format = NumberFormat.getNumberInstance();
		switch (valueType) {
		case VALUE_BYTES:
			format = NumberFormat.getNumberInstance();
			renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
					"{2} Mb", format));

			renderer.setSeriesItemLabelGenerator(0,
					new StandardCategoryItemLabelGenerator("{2} Mb", format));
			break;
		case VALUE_SECONDS:
			format = NumberFormat.getNumberInstance();
			renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(
					"{2}ms", format));
			break;
		default:
			format = NumberFormat.getNumberInstance();
			renderer.setSeriesItemLabelGenerator(0,
					new StandardCategoryItemLabelGenerator("{2}%", format));
		}

		renderer.setSeriesItemLabelsVisible(0, true);
		if (chartWidth == 0 || chartHeight == 0) {
			chartWidth = CHART_WIDTH;
			chartHeight = CHART_HEIGHT;
		}
		ByteArrayOutputStream chart_out = new ByteArrayOutputStream();
		ChartUtilities.writeChartAsJPEG(chart_out, 1.0f, lineChartObject,
				chartWidth, chartHeight);
		/* We now read from the output stream and frame the input chart data */
		InputStream feed_chart_to_excel = new ByteArrayInputStream(
				chart_out.toByteArray());
		byte[] bytes = IOUtils.toByteArray(feed_chart_to_excel);
		feed_chart_to_excel.close();
		chart_out.close();
		return bytes;
	}
}
