package com.wearezeta.auto.common.email.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import com.wearezeta.auto.common.email.MessagingUtils;

class JavaXMBoxChangesListener extends AbstractMboxChangesListener implements
		MessageCountListener {
	private CountDownLatch waitObj = new CountDownLatch(1);
	private Message matchedMessage = null;

	@Override
	protected JavaXMailbox getParentMbox() {
		return (JavaXMailbox) this.parentMBox;
	}

	public JavaXMBoxChangesListener(JavaXMailbox parentMBox,
			Map<String, String> expectedHeaders, int timeoutSeconds,
			long filterMessagesAfter) {
		super(parentMBox, expectedHeaders, timeoutSeconds, filterMessagesAfter);
	}

	private static class MessagesByDateComparator implements
			Comparator<Message> {
		@Override
		public int compare(Message m1, Message m2) {
			try {
				return m1.getSentDate().compareTo(m2.getSentDate());
			} catch (MessagingException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	private List<Message> preprocessReceivedMessages(Message[] deliveredMessages) {
		List<Message> result = new ArrayList<Message>();
		for (Message msg : deliveredMessages) {
			try {
				log.debug("\tMessage timestamp:" + msg.getSentDate().getTime());
				log.debug("\tListener timestamp:" + filterMessagesAfter);
				if (msg.getSentDate().getTime() >= filterMessagesAfter) {
					result.add(msg);
					log.debug("\tMessage accepted by timestamp");
				} else {
					log.error("\t!!! Message rejected because it is outdated. Please check your local time (should be in sync with world time)!");
				}
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(result,
				Collections.reverseOrder(new MessagesByDateComparator()));
		return result;
	}

	@Override
	public void messagesAdded(MessageCountEvent e) {
		for (Message msg : preprocessReceivedMessages(e.getMessages())) {
			if (areAllHeadersInMessage(msg)) {
				this.matchedMessage = msg;
				this.waitObj.countDown();
				return;
			}
		}
	}

	@Override
	public void messagesRemoved(MessageCountEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public String call() throws Exception {
		try {
			if (this.waitObj.await(timeoutSeconds, TimeUnit.SECONDS)) {
				return MessagingUtils.msgToString(this.matchedMessage);
			}
		} finally {
			this.getParentMbox().getFolder().removeMessageCountListener(this);
			this.getParentMbox().closeFolder(true);
		}
		throw new RuntimeException(
				String.format(
						"Email message containing headers %s has not been found within %d seconds",
						this.expectedHeaders.toString(), this.timeoutSeconds));
	}
}
