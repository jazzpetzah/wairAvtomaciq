package com.wearezeta.auto.image_send;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

public class SelfImageProcessor extends ImageAssetProcessor {
	private static final int PREVIEW_DIMENSION = 280;
	public static final String SMALL_PICTURE_TAG = "smallProfile";

	public SelfImageProcessor(ImageAssetData inData)
			throws MimeTypeNotSupportedException {
		super(inData);
	}

	private ImageAssetData getSmallProfileImage(String correlationId)
			throws IOException, MimeTypeNotSupportedException {
		final ImageAssetData originalImageAsset = this.getInData();
		final String convId = originalImageAsset.getConvId();
		final String mimeType = MIME_TYPE_JPEG;
		final byte[] imageData = originalImageAsset.getImageData();
		final BufferedImage originalImage = ImageIO
				.read(new ByteArrayInputStream(imageData));
		final byte[] previewImageData = getScaledImage(originalImage, mimeType,
				imageData.length, PREVIEW_DIMENSION);
		ImageAssetData resultAssetData = new ImageAssetData(convId,
				previewImageData, mimeType);
		final BufferedImage previewImage = ImageIO
				.read(new ByteArrayInputStream(previewImageData));
		resultAssetData.setWidth(previewImage.getWidth());
		resultAssetData.setOriginalWidth(originalImage.getWidth());
		resultAssetData.setHeight(previewImage.getHeight());
		resultAssetData.setOriginalHeight(originalImage.getHeight());
		resultAssetData.setName(originalImageAsset.getName());
		resultAssetData.setTag(SMALL_PICTURE_TAG);
		resultAssetData.setCorrelationId(correlationId);
		resultAssetData.setIsInline(false);
		resultAssetData.setIsPublic(originalImageAsset.getIsPublic());
		resultAssetData.setNonce(originalImageAsset.getNonce());
		resultAssetData.setNativePush(originalImageAsset.getNativePush());
		return resultAssetData;
	}

	@Override
	protected List<AssetData> processAsset() throws Exception {
		List<AssetData> resultList = new ArrayList<AssetData>();

		final String correlationId = (this.getInData().getCorrelationId() == null) ? String
				.valueOf(UUID.randomUUID()) : this.getInData()
				.getCorrelationId();

		ImageAssetData smallProfileImage = getSmallProfileImage(correlationId);
		resultList.add(smallProfileImage);

		ImageAssetData mediumImage = getMediumImage(correlationId);
		resultList.add(mediumImage);

		return resultList;
	}

}
