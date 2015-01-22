package javadoc_exporter.confluence_rest_api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * http://stackoverflow.com/questions/24504631/how-to-update-a-page-in-
 * confluence-5-5-1-via-rest-call
 */
public class ConfluenceAPIWrappers {
	private static JSONObject getPageInfo(JSONObject children, String pageTitle)
			throws Exception {
		final int size = children.getInt("size");
		if (size > 0) {
			JSONArray foundPages = children.getJSONArray("results");
			for (int pageIdx = 0; pageIdx < size; pageIdx++) {
				final JSONObject currentPage = foundPages
						.getJSONObject(pageIdx);
				final String currentTitle = currentPage.getString("title");
				if (currentTitle.equals(pageTitle)) {
					return currentPage;
				}
			}
		}
		return null;
	}

	public static long updateConfluenceChildPageIfNecessary(long parentPageId,
			String spaceKey, String pageTitle, String pageBody)
			throws Exception {
		JSONObject children = RESTMethods.getChildrenPages(parentPageId,
				new String[] { "version" });
		JSONObject pageInfo = getPageInfo(children, pageTitle);
		if (pageInfo == null) {
			pageInfo = RESTMethods.createChildPage(parentPageId, spaceKey,
					pageTitle, pageBody);
		} else {
			String currentBodyHash = "";
			if (pageInfo.getJSONObject("version").has("message")) {
				currentBodyHash = pageInfo.getJSONObject("version").getString(
						"message");
			}
			if (!currentBodyHash.equals(RESTMethods.getBodyHash(pageBody))) {
				final int currentPageVersion = pageInfo
						.getJSONObject("version").getInt("number");
				RESTMethods.updateChildPage(parentPageId, spaceKey, pageTitle,
						pageBody, currentPageVersion + 1);
			}
		}
		return pageInfo.getLong("id");
	}

	public static void removeExtraChildren(long parentPageId,
			Set<String> expectedChildrenTitles) throws Exception {
		JSONObject children = RESTMethods.getChildrenPages(parentPageId,
				new String[] { "version" });
		Map<String, Long> confluenceChildren = new HashMap<String, Long>();
		final int size = children.getInt("size");
		if (size > 0) {
			JSONArray foundPages = children.getJSONArray("results");
			for (int pageIdx = 0; pageIdx < size; pageIdx++) {
				final JSONObject currentPage = foundPages
						.getJSONObject(pageIdx);
				confluenceChildren.put(currentPage.getString("title"),
						currentPage.getLong("id"));
			}
		} else {
			return;
		}

		Set<Long> pagesToDelete = new HashSet<Long>();
		for (Map.Entry<String, Long> entry : confluenceChildren.entrySet()) {
			if (!expectedChildrenTitles.contains(entry.getKey())) {
				pagesToDelete.add(entry.getValue());
			}
		}
		for (Long id : pagesToDelete) {
			RESTMethods.removeChildPage(id.longValue());
		}
	}
}
