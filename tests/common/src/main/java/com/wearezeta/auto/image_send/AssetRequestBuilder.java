package com.wearezeta.auto.image_send;

import java.util.List;

public abstract class AssetRequestBuilder {
	protected static final String ZASSET_MARKER = "zasset";
	protected static final String SEPARATOR = ";";
	
	protected AssetProcessor processor = null;
	
	protected AssetProcessor getProcessor() {
		return processor;
	}

	public AssetRequestBuilder(AssetProcessor processor) {
		this.processor = processor;
	}
	
	protected List<AssetData> getProcessedAssets() throws Exception {
		return this.getProcessor().processAsset();
	}
	
	public abstract List<AssetRequest> getRequests() throws Throwable;
}
