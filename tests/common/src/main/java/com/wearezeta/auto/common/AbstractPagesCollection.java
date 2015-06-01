package com.wearezeta.auto.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class AbstractPagesCollection {

	private static final Logger log = ZetaLogger
			.getLog(AbstractPagesCollection.class.getSimpleName());

	private final Map<Class<? extends BasePage>, BasePage> pagesMapping = new HashMap<Class<? extends BasePage>, BasePage>();

	public BasePage getCommonPage() throws Exception {
		if (pagesMapping.size() == 0) {
			throw new IllegalStateException("No pages are created yet!");
		}
		return pagesMapping.get(pagesMapping.keySet().iterator().next());
	}

	public BasePage getPage(Class<? extends BasePage> pageClass)
			throws Exception {
		if (!pagesMapping.containsKey(pageClass)) {
			this.printPages();
			throw new IllegalStateException(String.format(
					"The page '%s' has not been created yet!",
					pageClass.getSimpleName()));
		}
		return pagesMapping.get(pageClass);
	}

	public BasePage getPageOrElseInstantiate(Class<? extends BasePage> pageClass)
			throws Exception {
		if (!pagesMapping.containsKey(pageClass)) {
			if (pagesMapping.size() == 0) {
				throw new IllegalStateException(
						"No pages are created yet and thus no parent for the new page can be found!");
			}
			log.debug(String
					.format("'%s' page has not been created yet. Trying to instantiate it for the first time...",
							pageClass.getSimpleName()));
			final BasePage parent = pagesMapping.get(pagesMapping.keySet()
					.iterator().next());
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

	public boolean isPageCreated(Class<? extends BasePage> pageClass) {
		return this.pagesMapping.containsKey(pageClass);
	}

	public void clearAllPages() {
		pagesMapping.clear();
	}

	public void setPage(BasePage page) {
		if (page == null) {
			throw new IllegalStateException(
					"Page object should be defined! 'null' values are not acceptable.");
		}
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
