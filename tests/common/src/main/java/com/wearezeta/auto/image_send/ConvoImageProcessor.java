package com.wearezeta.auto.image_send;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class ConvoImageProcessor extends ImageAssetProcessor {
	public static final String PREVIEW_TAG = "preview";
	private static final int PREVIEW_DIMENSION = 64;

	public ConvoImageProcessor(ImageAssetData inData)
			throws MimeTypeNotSupportedException {
		super(inData);
	}

	protected ImageAssetData getPreviewImage(String correlationId)
			throws IOException, MimeTypeNotSupportedException {
		final ImageAssetData originalImageAsset = this.getInData();
		final String convId = originalImageAsset.getConvId();
		final String mimeType = MIME_TYPE_JPEG;
		final byte[] imageData = originalImageAsset.getImageData();
		final BufferedImage originalImage = ImageIO
				.read(new ByteArrayInputStream(imageData));
		final byte[] previewImageData = getScaledImage(originalImage, mimeType,
				imageData.length, PREVIEW_DIMENSION);

		// Old clients assume that previews should be always inline
		ImageAssetData resultAssetData = new ImageAssetData(convId,
				Base64.encodeBase64(previewImageData), mimeType);
		final BufferedImage previewImage = ImageIO
				.read(new ByteArrayInputStream(previewImageData));
		resultAssetData.setWidth(previewImage.getWidth());
		resultAssetData.setOriginalWidth(originalImage.getWidth());
		resultAssetData.setHeight(previewImage.getHeight());
		resultAssetData.setOriginalHeight(originalImage.getHeight());
		resultAssetData.setName(originalImageAsset.getName());
		resultAssetData.setTag(PREVIEW_TAG);
		resultAssetData.setCorrelationId(correlationId);
		resultAssetData.setIsInline(true);
		resultAssetData.setIsPublic(originalImageAsset.getIsPublic());
		resultAssetData.setNonce(String.valueOf(UUID.randomUUID()));
		resultAssetData.setNativePush(originalImageAsset.getNativePush());
		return resultAssetData;
	}

	@Override
	public List<AssetData> processAsset() throws IOException,
			MimeTypeNotSupportedException {
		List<AssetData> resultList = new ArrayList<AssetData>();

		final String correlationId = (this.getInData().getCorrelationId() == null) ? String
				.valueOf(UUID.randomUUID()) : this.getInData()
				.getCorrelationId();

		ImageAssetData previewImage = getPreviewImage(correlationId);
		resultList.add(previewImage);

		ImageAssetData mediumImage = getMediumImage(correlationId);
		resultList.add(mediumImage);

		return resultList;
	}
}
