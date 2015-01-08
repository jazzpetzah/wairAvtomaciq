package com.wearezeta.auto.image_send;

public abstract class AssetData {
	public class MandatoryFieldNotSetException extends Exception {
		private static final long serialVersionUID = 1L;

		public MandatoryFieldNotSetException(String msg) {
			super(msg);
		}
	}
	
	public AssetData(String convId) {
		this.convId = convId;
	}

	private String convId = null;
	
	public String getConvId() {
		return convId;
	}

	public void setConvId(String convId) {
		this.convId = convId;
	}
	
	public void verifyMandatoryFields() throws MandatoryFieldNotSetException {
		if (this.getConvId() == null) {
			throw new MandatoryFieldNotSetException(
					"It is mandatory to set the 'conv_id' field");
		}
	}
}
