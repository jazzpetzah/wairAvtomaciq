package javadoc_exporter.confluence_rest_api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * http://stackoverflow.com/questions/24504631/how-to-update-a-page-in-
 * confluence-5-5-1-via-rest-call
 */
public class ConfluenceAPIWrappers {
	public static long createChildPage(long parentPageId, String spaceKey,
			String pageTitle, String pageBody) throws Exception {
		final JSONObject pageInfo = RESTMethods.createChildPage(parentPageId,
				spaceKey, pageTitle, pageBody);
		return pageInfo.getLong("id");
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
