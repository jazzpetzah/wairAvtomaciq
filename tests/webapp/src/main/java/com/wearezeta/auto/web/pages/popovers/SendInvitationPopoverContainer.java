package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class SendInvitationPopoverContainer extends AbstractPopoverContainer {
	private SendInvitationPage sendInvitationPage = null;

	public SendInvitationPopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.sendInvitationPage = new SendInvitationPage(lazyDriver, this);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SendInvitationPopover.xpathRootLocator;
	}

	public String getInvitationText() {
		return this.sendInvitationPage.getInvitationText();
	}

	public String parseInvitationLink() {
		final String invitationText = this.getInvitationText();
		final String regex = "(https://\\S+)";
		final Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		final Matcher urlMatcher = p.matcher(invitationText);
		if (urlMatcher.find()) {
			return urlMatcher.group(1);
		} else {
			throw new RuntimeException(String.format(
					"Invitation link could not be parsed from this text: '%s'",
					invitationText));
		}
	}
}
