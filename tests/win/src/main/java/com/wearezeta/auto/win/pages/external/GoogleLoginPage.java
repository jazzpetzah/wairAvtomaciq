package com.wearezeta.auto.win.pages.external;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

import java.util.LinkedList;
import java.util.concurrent.Callable;

public class GoogleLoginPage extends
		com.wearezeta.auto.web.pages.external.GoogleLoginPage {

	public GoogleLoginPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public void setEmail(String email) throws Exception {
		executeOnLatestWindow(() -> {
			super.setEmail(email);
			return true;
		});
	}

	@Override
	public void setPassword(String password) throws Exception {
		executeOnLatestWindow(() -> {
			super.setPassword(password);
			return true;
		});
	}

	@Override
	public boolean hasNextButton() throws Exception {
		return executeOnLatestWindow(() -> {
			return super.hasNextButton();
		});
	}

	@Override
	public void clickNext() throws Exception {
		executeOnLatestWindow(() -> {
			super.clickNext();
			return true;
		});
	}

	@Override
	public boolean hasApproveButton() throws Exception {
		return executeOnLatestWindow(() -> {
			return super.hasApproveButton();
		});
	}

	@Override
	public void clickApprove() throws Exception {
		executeOnLatestWindow(() -> {
			super.clickApprove();
			return true;
		});
	}

	@Override
	public void clickSignIn() throws Exception {
		executeOnLatestWindow(() -> {
			super.clickSignIn();
			return true;
		});
	}

	private <T> T executeOnLatestWindow(Callable<T> function) throws Exception {
		LinkedList<String> windowHandles = new LinkedList<>(getDriver()
				.getWindowHandles());
		// switch to latest window
		getDriver().switchTo().window(windowHandles.getLast());
		T result = function.call();
		// switch to oldest window
		getDriver().switchTo().window(windowHandles.getFirst());
		return result;
	}

}
