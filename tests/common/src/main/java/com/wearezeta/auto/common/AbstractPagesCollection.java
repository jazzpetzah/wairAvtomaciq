package com.wearezeta.auto.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class AbstractPagesCollection {

	private final Logger log = ZetaLogger.getLog(this.getClass()
			.getSimpleName());

	private final Map<Class<? extends BasePage>, BasePage> pagesMapping = new LinkedHashMap<Class<? extends BasePage>, BasePage>();

	public BasePage getCommonPage() throws Exception {
		if (pagesMapping.size() == 0) {
			throw new IllegalStateException(
					"No pages are created yet! Please set the first page first!");
		}
		return pagesMapping.get(pagesMapping.keySet().iterator().next());
	}

	public BasePage getPage(Class<? extends BasePage> pageClass)
			throws Exception {
		if (!pagesMapping.containsKey(pageClass)) {
			this.printPages();
			log.debug(String.format(" > +++ %s", pageClass.getSimpleName()));
			pagesMapping.put(pageClass,
					getCommonPage().instantiatePage(pageClass));
		}
		return pagesMapping.get(pageClass);
	}

	public boolean removePage(Class<? extends BasePage> pageClass) {
		if (!pagesMapping.containsKey(pageClass)) {
			return false;
		}
		pagesMapping.remove(pageClass);
		return true;
	}

	public boolean hasPage(Class<? extends BasePage> pageClass) {
		return this.pagesMapping.containsKey(pageClass);
	}

	public void clearAllPages() {
		final int pagesCount = pagesMapping.size();
		if (pagesCount > 0) {
			pagesMapping.clear();
			log.debug(String.format("Cleaned %d existing page objects",
					pagesCount));
		}
	}

	public void setFirstPage(BasePage page) {
		if (page == null) {
			throw new IllegalStateException(
					"Page object should be defined! 'null' values are not acceptable.");
		}
		this.clearAllPages();
		log.debug(String.format("Setting the first page object to '%s'", page
				.getClass().getSimpleName()));
		pagesMapping.put(page.getClass(), page);
	}

	public void printPages() {
		if (pagesMapping.size() == 0) {
			log.debug("No pages are currently created");
			return;
		}
		log.debug(String.format("%d page(s) are currently created:",
				pagesMapping.size()));
		for (final Class<? extends BasePage> pageClass : pagesMapping.keySet()) {
			log.info(" > " + pageClass.getSimpleName());
		}
	}

}
