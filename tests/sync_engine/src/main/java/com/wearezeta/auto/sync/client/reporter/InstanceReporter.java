package com.wearezeta.auto.sync.client.reporter;

import java.util.ArrayList;

import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.client.WireInstance;

public abstract class InstanceReporter {

	private long startupTime;
	private ArrayList<MessageEntry> allMessagesList = new ArrayList<MessageEntry>();
	private boolean orderCorrect;
	private BuildVersionInfo version;
	private ClientDeviceInfo device;

	private WireInstance owner;

	public InstanceReporter(WireInstance owner) {
		this.owner = owner;
	}

	public void copyWholeMessagesList() {
		if (owner.isEnabled()) {
			this.allMessagesList = owner.listener().allMessagesList;
		}
	}

	public void checkMessagesOrderCorrectness() {
		orderCorrect = true;
		if (owner.isEnabled()) {
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

	public void readClientVersionAndDeviceInfo() {
		if (owner.isEnabled()) {
			device = readClientDevice();
			version = readBuildVersion();
		}
	}

	protected abstract ClientDeviceInfo readClientDevice();

	protected abstract BuildVersionInfo readBuildVersion();

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

	public BuildVersionInfo getVersion() {
		return version;
	}

	public void setVersion(BuildVersionInfo version) {
		this.version = version;
	}

	public ClientDeviceInfo getDevice() {
		return device;
	}

	public void setDevice(ClientDeviceInfo device) {
		this.device = device;
	}

	public long getStartupTime() {
		return startupTime;
	}

	public void setStartupTime(long startupTime) {
		this.startupTime = startupTime;
	}
}
