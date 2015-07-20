package com.wearezeta.auto.common.performance;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * http://www.highcharts.com/docs/chart-and-series-types/column-chart
 * 
 */
public class ReportWidget {
	private static final String Y_AXIS_NAME = "Time (ms)";

	public class Serio {
		private String color;
		private String name;
		private List<Long> values = new ArrayList<>();

		public Serio(String color, String name, List<Long> values) {
			this.color = color;
			this.name = name;
			this.values = new ArrayList<>(values);
		}

		protected JSONObject asJSON() {
			final JSONObject result = new JSONObject();
			result.put("color", this.color);
			result.put("name", this.name);
			result.put("data", new JSONArray(this.values));
			return result;
		}
	}

	private String title;
	private List<String> categories = new ArrayList<>();
	private List<Serio> series = new ArrayList<>();

	public ReportWidget(String title, List<String> categories,
			List<Serio> series) {
		this.title = title;
		this.categories = new ArrayList<>(categories);
		this.series = new ArrayList<>(series);
	}

	// + "\"data\":{"
	// + "\"highchart\": "
	// + "\"{chart:{type:\\\"column\\\", style: {color: \\\"#b9bbbb\\\"},"
	// + "renderTo:\\\"container\\\",backgroundColor:\\\"transparent\\\","
	// + "lineColor:\\\"rgba(35,37,38,100)\\\",plotShadow: false,},"
	// + "credits:{enabled:false},title:{style: {color: \\\"#b9bbbb\\\"},"
	// + "text:\\\"%s\\\"},"
	// + "xAxis:{categories:[%s]},"
	// +
	// "yAxis:{title:{style: {color: \\\"#b9bbbb\\\"}, text:\\\"Time (ms)\\\"}},"
	// + "legend:{itemStyle: {color: \\\"#b9bbbb\\\"}, layout:\\\"vertical\\\","
	// + "align:\\\"right\\\",verticalAlign:\\\"middle\\\",borderWidth:0},"
	// + "series:[{color:\\\"#108ec5\\\",name:\\\"10 users\\\",data:[%s]},"
	// + "{color:\\\"#52b238\\\",name:\\\"300 users\\\",data:[%s]},"
	// + "{color:\\\"#ee5728\\\",name:\\\"600 users\\\",data:[%s]},"
	// + "{color:\\\"#fefe22\\\",name:\\\"1000 users\\\",data:[%s]}]}\"}}'",
	//
	private static JSONObject getStyle(String color) {
		final JSONObject result = new JSONObject();
		result.put("color", color);
		return result;
	}

	private static JSONObject getChartProp() {
		final JSONObject result = new JSONObject();
		result.put("type", "column");
		result.put("style", getStyle("#b9bbbb"));
		result.put("renderTo", "container");
		result.put("backgroundColor", "transparent");
		result.put("lineColor", "rgba(35,37,38,100)");
		result.put("plotShadow", false);
		return result;
	}

	private static JSONObject getChartCredits() {
		final JSONObject result = new JSONObject();
		result.put("enabled", false);
		return result;
	}

	private JSONObject getTitleProps() {
		final JSONObject result = new JSONObject();
		result.put("style", getStyle("#b9bbbb"));
		result.put("text", this.title);
		return result;
	}

	private JSONObject getXAxisProps() {
		final JSONObject result = new JSONObject();
		result.put("categories", new JSONArray(categories));
		return result;
	}

	private JSONObject getYAxisProps() {
		final JSONObject result = new JSONObject();
		final JSONObject title = new JSONObject();
		title.put("style", getStyle("#b9bbbb"));
		title.put("text", Y_AXIS_NAME);
		return result;
	}

	private JSONObject getLegendProps() {
		final JSONObject result = new JSONObject();
		result.put("itemStyle", getStyle("#b9bbbb"));
		result.put("layout", "vertical");
		result.put("align", "right");
		result.put("verticalAlign", "middle");
		result.put("borderWidth", 0);
		return result;
	}

	private JSONArray getSeries() {
		final JSONArray result = new JSONArray();
		this.series.stream().forEach((x) -> result.put(x.asJSON()));
		return result;
	}

	public JSONObject asJSON() {
		final JSONObject result = new JSONObject();
		final JSONObject allProps = new JSONObject();
		allProps.put("chart", getChartProp());
		allProps.put("credits", getChartCredits());
		allProps.put("title", getTitleProps());
		allProps.put("xAxis", getXAxisProps());
		allProps.put("yAxis", getYAxisProps());
		allProps.put("legend", getLegendProps());
		allProps.put("series", getSeries());
		result.put("highchart", allProps);
		return result;
	}

}
