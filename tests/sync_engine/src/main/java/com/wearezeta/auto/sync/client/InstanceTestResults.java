package com.wearezeta.auto.sync.client;

import java.util.ArrayList;

import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;

public class InstanceTestResults {

	private ArrayList<MessageEntry> allMessagesList = new ArrayList<MessageEntry>();

	private boolean orderCorrect;

	private WireInstance owner;

	public InstanceTestResults(WireInstance owner) {
		this.owner = owner;
	}

	public void copyWholeMessagesList() {
		if (owner.enabled) {
			this.allMessagesList = owner.listener.allMessagesList;
		}
	}

	public void checkMessagesOrderCorrectness() {
		orderCorrect = true;
		if (owner.enabled) {
			ArrayList<MessageEntry> sentMessages = new ArrayList<MessageEntry>(
					ExecutionContext.sentMessages.values());
			for (int i = 0; i < sentMessages.size(); i++) {
				try {
					if (!sentMessages.get(i).messageContent
							.equals(allMessagesList.get(i).messageContent))
						orderCorrect = false;
				} catch (Exception e) {
					orderCorrect = false;
				}
			}
		}
	}

	// getters and setters
	public ArrayList<MessageEntry> getAllMessagesList() {
		return allMessagesList;
	}

	public void setAllMessagesList(ArrayList<MessageEntry> allMessagesList) {
		this.allMessagesList = allMessagesList;
	}

	public boolean isOrderCorrect() {
		return orderCorrect;
	}

	public void setOrderCorrect(boolean orderCorrect) {
		this.orderCorrect = orderCorrect;
	}

}
