package javadoc_exporter.confluence_rest_api;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * http://stackoverflow.com/questions/24504631/how-to-update-a-page-in-
 * confluence-5-5-1-via-rest-call
 */
public class ConfluenceAPIWrappers {
	private static final int MAX_RETRIES = 3;

	private static final Random random = new Random();

	public static long createChildPage(long parentPageId, String spaceKey,
			String pageTitle, String pageBody) throws Exception {
		int tryNum = 1;
		JSONException savedException = null;
		do {
			try {
				final JSONObject pageInfo = RESTMethods.createChildPage(
						parentPageId, spaceKey, pageTitle, pageBody);
				return pageInfo.getLong("id");
			} catch (JSONException e) {
				savedException = e;
				Thread.sleep((random.nextInt(5) + 1) * 1000);
				tryNum++;
			}
		} while (tryNum <= MAX_RETRIES);
		throw savedException;
	}

	public static void removeChildren(long parentPageId, boolean recursive)
			throws Exception {
		JSONObject children = RESTMethods.getChildrenPages(parentPageId,
				new String[] { "version" });
		final int size = children.getInt("size");
		if (size > 0) {
			JSONArray foundPages = children.getJSONArray("results");
			for (int pageIdx = 0; pageIdx < size; pageIdx++) {
				final JSONObject currentPage = foundPages
						.getJSONObject(pageIdx);
				final long id = currentPage.getLong("id");
				if (recursive) {
					ConfluenceAPIWrappers.removeChildren(id, recursive);
				}
				RESTMethods.removePage(id);
			}
		}
	}
}
