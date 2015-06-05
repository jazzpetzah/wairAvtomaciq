package com.wearezeta.auto.common;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class AbstractPagesCollection {

	private static final Logger log = ZetaLogger
			.getLog(AbstractPagesCollection.class.getSimpleName());

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
			log.debug(String
					.format("'%s' page has not been created yet. Trying to instantiate it for the first time...",
							pageClass.getSimpleName()));
			final BasePage parent = getCommonPage();
			pagesMapping.put(pageClass, parent.instantiatePage(pageClass));
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
		if (pagesMapping.size() > 0) {
			pagesMapping.clear();
			log.debug(String.format("Cleaned %d existing page objects",
					pagesMapping.size()));
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
			log.info("No pages are currently created");
			return;
		}
		log.info(String.format("%d pages are currently created:",
				pagesMapping.size()));
		for (final Class<? extends BasePage> pageClass : pagesMapping.keySet()) {
			log.info(" > " + pageClass.getSimpleName());
		}
	}

}
