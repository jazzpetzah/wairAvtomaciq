package com.wearezeta.auto.image_send;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

public class ImageAssetProcessor extends AssetProcessor {
	public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
	public static final long MAX_NON_GIF_IMAGE_SIZE = 310 * 1024;
	public static final long MAX_INLINE_ITEM_SIZE = 8 * 1024;
	public static final String MEDIUM_TAG = "medium";
	private static final int MEDIUM_DIMENSION = 1448;
	public static final String PREVIEW_TAG = "preview";
	private static final int PREVIEW_DIMENSION = 64;
	public static final String MIME_TYPE_JPEG = "image/jpeg";
	public static final String MIME_TYPE_GIF = "image/gif";
	public static final String MIME_TYPE_PNG = "image/png";
	public static final String[] ACCEPTABLE_IMAGE_TYPES = new String[] {
		MIME_TYPE_JPEG, MIME_TYPE_PNG, MIME_TYPE_GIF };

	public class MimeTypeNotSupportedException extends Exception {
		private static final long serialVersionUID = -5727468348824499790L;

		public MimeTypeNotSupportedException(String msg) {
			super(msg);
		}

	}

	public ImageAssetProcessor(ImageAssetData inData)
			throws MimeTypeNotSupportedException {
		super(inData);

		if (!Arrays.asList(ACCEPTABLE_IMAGE_TYPES).contains(
				inData.getMimeType())) {
			throw new MimeTypeNotSupportedException(String.format(
					"MIME type '%s' is not supported", inData.getMimeType()));
		}
	}

	@Override
	public ImageAssetData getInData() {
		return (ImageAssetData) inData;
	}

	private class Size {
		private double width;
		private double height;

		public double getWidth() {
			return width;
		}

		public double getHeight() {
			return height;
		}

		public Size(double width, double height) {
			this.width = width;
			this.height = height;
		}
	}

	private static Boolean shouldScaleOriginalSize(double width, double height,
			double dimension) {
		final double maxPixelCount = 1.3 * dimension * dimension;
		return (width > 1.3 * dimension || height > 1.3 * dimension)
				&& width * height > maxPixelCount;
	}

	private Size getScaledSize(double origWidth, double origHeight,
			double dimension) {
		if (origWidth < 1 || origHeight < 1) {
			return new Size(1, 1);
		} else {
			double op1 = ((dimension / origWidth) < (dimension / origHeight)) ? (dimension / origWidth)
					: (dimension / origHeight);
			double op2 = dimension / Math.sqrt(origWidth * origHeight);
			double scale = op1 > op2 ? op1 : op2;
			double width = Math.ceil(scale * origWidth);
			return new Size(width, width / origWidth * origHeight);
		}
	}

	private static BufferedImage resizeImage(BufferedImage originalImage,
			int origWidth, int origHeight) {
		final int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
				: originalImage.getType();
		BufferedImage resizedImage = new BufferedImage(origWidth, origHeight,
				type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, origWidth, origHeight, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
	}

	private byte[] getScaledImage(BufferedImage inImage, String mimeType,
			long dataSize, double dimension) throws IOException,
			MimeTypeNotSupportedException {
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		int origWidth = inImage.getWidth();
		int origHeight = inImage.getHeight();
		String resultImageType;
		if (mimeType.equals(MIME_TYPE_GIF)) {
			resultImageType = "gif";
		} else if (mimeType.equals(MIME_TYPE_JPEG)) {
			resultImageType = "jpg";
		} else if (mimeType.equals(MIME_TYPE_PNG)) {
			resultImageType = "png";
		} else {
			throw new MimeTypeNotSupportedException(String.format(
					"MIME type '%s' is not supported", mimeType));
		}
		BufferedImage resultImage = inImage;
		if (shouldScaleOriginalSize(origWidth, origHeight, dimension)) {
			Size scaledSize = getScaledSize(origWidth, origHeight, dimension);
			resultImage = resizeImage(inImage, (int) scaledSize.getWidth(),
					(int) scaledSize.getHeight());
		}
		try {
			ImageIO.write(resultImage, resultImageType, resultStream);
			resultStream.flush();
			return resultStream.toByteArray();
		} finally {
			resultStream.close();
		}
	}

	private ImageAssetData getMediumImage(String correlationId)
			throws IOException, MimeTypeNotSupportedException {
		final ImageAssetData originalImageAsset = this.getInData();
		final String convId = originalImageAsset.getConvId();
		final byte[] imageData = originalImageAsset.getImageData();
		String mimeType = originalImageAsset.getMimeType();
		if ((mimeType == MIME_TYPE_GIF && imageData.length > MAX_IMAGE_SIZE)
				|| (mimeType != MIME_TYPE_GIF && imageData.length > MAX_NON_GIF_IMAGE_SIZE)) {
			mimeType = MIME_TYPE_JPEG;
		}
		final BufferedImage originalImage = ImageIO
				.read(new ByteArrayInputStream(imageData));
		final byte[] processedImageData = getScaledImage(originalImage,
				mimeType, imageData.length, MEDIUM_DIMENSION);

		ImageAssetData resultAssetData = new ImageAssetData(convId,
				processedImageData, mimeType);
		final BufferedImage processedImage = ImageIO
				.read(new ByteArrayInputStream(processedImageData));
		resultAssetData.setWidth(processedImage.getWidth());
		resultAssetData.setOriginalWidth(originalImage.getWidth());
		resultAssetData.setHeight(processedImage.getHeight());
		resultAssetData.setOriginalHeight(originalImage.getHeight());
		resultAssetData.setName(originalImageAsset.getName());
		resultAssetData.setTag(MEDIUM_TAG);
		resultAssetData.setCorrelationId(correlationId);
		resultAssetData.setIsInline(originalImageAsset.getIsInline());
		resultAssetData.setIsPublic(originalImageAsset.getIsPublic());
		if (originalImageAsset.getNonce() == null) {
			resultAssetData.setNonce(String.valueOf(UUID.randomUUID()));
		}
		resultAssetData.setNativePush(originalImageAsset.getNativePush());
		return resultAssetData;
	}

	private ImageAssetData getPreviewImage(String correlationId)
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
	protected List<AssetData> processAsset() throws IOException,
	MimeTypeNotSupportedException {

		String property = "java.io.tmpdir";
		String tempDir = System.getProperty(property);

		List<AssetData> resultList = new ArrayList<AssetData>();

		final String correlationId = (this.getInData().getCorrelationId() == null) ?
				String.valueOf(UUID.randomUUID()) : this.getInData().getCorrelationId();

				ImageAssetData previewImage = getPreviewImage(correlationId);
				FileUtils.writeByteArrayToFile(new File( tempDir +
						"/preview.jpg"), previewImage.getImageData());
				resultList.add(previewImage);

				ImageAssetData mediumImage = getMediumImage(correlationId);
				FileUtils.writeByteArrayToFile(
						new File(tempDir + "/medium.jpg"),
						mediumImage.getImageData());
				resultList.add(mediumImage);

				return resultList;
	}
}
