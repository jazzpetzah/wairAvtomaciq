package javadoc_exporter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ResourceHelpers {
	public static String getValueFromConfigFile(Class<?> c, String key,
			String resourcePath) throws IOException {
		String val = "";
		InputStream configFileStream = null;
		try {
			URL configFile = c.getClass().getResource("/" + resourcePath);
			configFileStream = configFile.openStream();
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
