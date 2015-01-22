package javadoc_exporter.transformers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import javadoc_exporter.model.Step;
import javadoc_exporter.model.StepsContainer;

public class OutputTransformer {
	public static final String TRANSFORMED_FILE_EXT = "storage";
	public static final String STEPS_SUBFOLDER_NAME_SUFFIX = "_steps";
	public static final String DEFAULT_ENCODING = "UTF-8";

	private static final String CONTAINER_TEMPLATE_NAME = "container.template";
	private static final String STEP_TEMPLATE_NAME = "step.template";

	private static Map<Class<?>, Mustache> availableReplacers = new HashMap<Class<?>, Mustache>();
	static {
		MustacheFactory mf = new DefaultMustacheFactory();
		availableReplacers.put(StepsContainer.class,
				mf.compile(CONTAINER_TEMPLATE_NAME));
		availableReplacers.put(Step.class, mf.compile(STEP_TEMPLATE_NAME));
	}

	private StepsContainer sourceData;
	private String dstRoot;

	public OutputTransformer(StepsContainer sourceData, String dstRoot) {
		this.sourceData = sourceData;
		this.dstRoot = dstRoot;
	}

	private void writeTemplateToFile(Object scope, String dstPath)
			throws IOException {
		Mustache replacer = availableReplacers.get(scope.getClass());
		Writer writer = new FileWriter(dstPath);
		try {
			replacer.execute(writer, scope).flush();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public List<String> transform() throws Exception {
		List<String> result = new ArrayList<String>();
		File destinationFolder = new File(dstRoot);
		if (!destinationFolder.exists() && !(destinationFolder).mkdirs()) {
			throw new RuntimeException(String.format(
					"Failed to create destination folder '%s'", dstRoot));
		}

		final String containerNameEncoded = URLEncoder.encode(
				sourceData.getName(), DEFAULT_ENCODING);
		final String containerDstPath = String.format("%s/%s.%s", dstRoot,
				containerNameEncoded, TRANSFORMED_FILE_EXT);
		writeTemplateToFile(sourceData, containerDstPath);
		result.add(containerDstPath);

		final List<Step> steps = sourceData.getSteps();
		final String stepsRoot = String.format("%s/%s%s", dstRoot,
				containerNameEncoded, STEPS_SUBFOLDER_NAME_SUFFIX);
		if (steps.size() > 0) {
			File stepsRootFileObj = new File(stepsRoot);
			if (!stepsRootFileObj.exists() && !stepsRootFileObj.mkdirs()) {
				throw new RuntimeException(String.format(
						"Failed to create destination folder '%s'", stepsRoot));
			}
		}
		for (Step step : steps) {
			final String stepDstPath = String.format("%s/%s.%s", stepsRoot,
					URLEncoder.encode(step.getName(), DEFAULT_ENCODING),
					TRANSFORMED_FILE_EXT);
			writeTemplateToFile(step, stepDstPath);
			result.add(stepDstPath);
		}

		return result;
	}
}
