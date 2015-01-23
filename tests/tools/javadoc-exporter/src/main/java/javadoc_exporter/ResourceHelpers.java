package javadoc_exporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceHelpers {
	public static String getValueFromConfigFile(String key, String resourcePath)
			throws IOException {
		String val = "";
		InputStream configFileStream = null;
		try {
			final ClassLoader classLoader = ResourceHelpers.class
					.getClassLoader();
			configFileStream = classLoader.getResourceAsStream(resourcePath);
			Properties p = new Properties();
			p.load(configFileStream);
			val = (String) p.get(key);
		} finally {
			if (configFileStream != null) {
				configFileStream.close();
			}
		}
		return val;
	}
}
