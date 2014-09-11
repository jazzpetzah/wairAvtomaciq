package com.wearezeta.auto.image_send;


public class ImageAssetData extends AssetData {

	private String name = null;
	private Integer width = null;
	private Integer height = null;
	private Integer originalWidth = null;
	private Integer originalHeight = null;
	private Boolean isInline = false;
	private Boolean isPublic = null;
	private String correlationId = null;
	private String tag = null;
	private String nonce = null;
	private Boolean nativePush = null;

	private byte[] imageData = null;
	private String mimeType = null;

	public String getMimeType() {
		return mimeType;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getOriginalWidth() {
		return originalWidth;
	}

	public void setOriginalWidth(Integer originalWidth) {
		this.originalWidth = originalWidth;
	}

	public Integer getOriginalHeight() {
		return originalHeight;
	}

	public void setOriginalHeight(Integer originalHeight) {
		this.originalHeight = originalHeight;
	}

	public Boolean getIsInline() {
		return isInline;
	}

	public void setIsInline(Boolean isInline) {
		this.isInline = isInline;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public Boolean getNativePush() {
		return nativePush;
	}

	public void setNativePush(Boolean nativePush) {
		this.nativePush = nativePush;
	}

	public ImageAssetData(String convId, byte[] imageData, String mimeType) {
		super(convId);
		this.imageData = imageData;
		this.mimeType = mimeType.toLowerCase();
	}

	@Override
	public void verifyMandatoryFields() throws MandatoryFieldNotSetException {
		super.verifyMandatoryFields();
		if (this.getMimeType() == null) {
			throw new MandatoryFieldNotSetException(
					"It is mandatory to set the 'mimeType' field");
		}
		if (this.getImageData() == null) {
			throw new MandatoryFieldNotSetException(
					"It is mandatory to set the image payload data");
		}
	}

}
