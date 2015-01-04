package com.wearezeta.auto.image_send;

import java.util.List;

public abstract class AssetProcessor {
	protected AssetData inData;

	public AssetData getInData() {
		return inData;
	}

	public AssetProcessor(AssetData inData) {
		this.inData = inData;
	}
	
	protected abstract List<AssetData> processAsset() throws Exception;
}
