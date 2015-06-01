package com.wearezeta.auto.android.pages;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AndroidTabletPage extends BasePage {

	@Override
	protected ZetaAndroidDriver getDriver() throws Exception {
		return (ZetaAndroidDriver) super.getDriver();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Future<ZetaAndroidDriver> getLazyDriver() {
		return (Future<ZetaAndroidDriver>) super.getLazyDriver();
	}

	public AndroidTabletPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private static Map<Class<? extends AndroidPage>, AndroidPage> connectedPagesMapping = new ConcurrentHashMap<Class<? extends AndroidPage>, AndroidPage>();

	protected AndroidPage getAndroidPageInstance(
			Class<? extends AndroidPage> pageClass) throws Exception {
		if (!connectedPagesMapping.containsKey(pageClass)) {
			connectedPagesMapping.put(pageClass,
					(AndroidPage) this.instantiatePage(pageClass));
		}
		return connectedPagesMapping.get(pageClass);
	}

	@Override
	public AndroidTabletPage swipeLeft(int time) throws Exception {
		return null;
	}

	@Override
	public AndroidTabletPage swipeRight(int time) throws Exception {
		return null;
	}

	@Override
	public AndroidTabletPage swipeUp(int time) throws Exception {
		return null;
	}

	@Override
	public AndroidTabletPage swipeDown(int time) throws Exception {
		return null;
	}

	public AndroidTabletPage returnBySwipe(SwipeDirection direction)
			throws Exception {
		return null;
	};
}
