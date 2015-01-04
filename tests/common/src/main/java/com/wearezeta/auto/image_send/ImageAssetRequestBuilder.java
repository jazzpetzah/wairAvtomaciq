package com.wearezeta.auto.image_send;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.wearezeta.auto.image_send.AssetData.MandatoryFieldNotSetException;

public class ImageAssetRequestBuilder extends AssetRequestBuilder {

	public ImageAssetRequestBuilder(ImageAssetProcessor processor) {
		super(processor);
	}

	private static String GetBase64EncodedMD5(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data, 0, data.length);
		byte[] hash = md.digest();
		byte[] byteArray = Base64.encodeBase64(hash);
		return new String(byteArray);
	}

	private long getContentLength(ImageAssetData assetData) throws IOException {
		return getPayload(assetData).length;
	}

	private byte[] getPayload(ImageAssetData assetData) {
		return assetData.getImageData();
	}
	
	private String getContentType(ImageAssetData assetData) {
		return assetData.getMimeType();
	}

	private String getContentDisposition(ImageAssetData assetData)
			throws MandatoryFieldNotSetException, NoSuchAlgorithmException,
			IOException {
		List<String> result = new ArrayList<String>();
		result.add(ZASSET_MARKER);
		assetData.verifyMandatoryFields();

		final String conv_id = String.format("conv_id=%s",
				assetData.getConvId());
		result.add(conv_id);
		final String md5 = String.format("md5=%s",
				GetBase64EncodedMD5(assetData.getImageData()));
		result.add(md5);
		if (assetData.getName() != null) {
			final String rawName = assetData.getName();
			final byte[] utf8Name = rawName.getBytes("UTF-8");
			final String base64Name = new String(Base64.encodeBase64(utf8Name));
			final String name = String.format("name=%s", base64Name);
			result.add(name);
		}
		if (assetData.getWidth() != null) {
			final String width = String
					.format("width=%d", assetData.getWidth());
			result.add(width);
		}
		if (assetData.getHeight() != null) {
			final String height = String.format("height=%d",
					assetData.getHeight());
			result.add(height);
		}
		if (assetData.getOriginalWidth() != null) {
			final String original_width = String.format("original_width=%d",
					assetData.getOriginalWidth());
			result.add(original_width);
		}
		if (assetData.getOriginalHeight() != null) {
			final String original_height = String.format("original_height=%d",
					assetData.getOriginalHeight());
			result.add(original_height);
		}
		if (assetData.getIsInline() != null) {
			final String is_inline = String.format("inline=%b",
					assetData.getIsInline());
			result.add(is_inline);
		}
		if (assetData.getIsPublic() != null) {
			final String is_public = String.format("public=%b",
					assetData.getIsPublic());
			result.add(is_public);
		}
		if (assetData.getCorrelationId() != null) {
			final String correlation_id = String.format("correlation_id=%s",
					assetData.getCorrelationId());
			result.add(correlation_id);
		}
		if (assetData.getTag() != null) {
			final String tag = String.format("tag=%s", assetData.getTag());
			result.add(tag);
		}
		if (assetData.getNonce() != null) {
			final String nonce = String
					.format("nonce=%s", assetData.getNonce());
			result.add(nonce);
		}
		if (assetData.getNativePush() != null) {
			final String native_push = String.format("native_push=%b",
					assetData.getNativePush());
			result.add(native_push);
		}

		return StringUtils.join(result, SEPARATOR);
	}

	@Override
	public List<AssetRequest> getRequests() throws Exception {
		List<AssetRequest> resultList = new ArrayList<AssetRequest>();

		List<AssetData> processedImageAssets = getProcessedAssets();
		for (AssetData processedImageAsset: processedImageAssets) {
			AssetRequest req = new AssetRequest();
			ImageAssetData curentAssetData = (ImageAssetData) processedImageAsset;
			req.setContentDisposition(this.getContentDisposition(curentAssetData));
			req.setContentLength(this.getContentLength(curentAssetData));
			req.setContentType(this.getContentType(curentAssetData));
			req.setPayload(this.getPayload(curentAssetData));
			resultList.add(req);
		}

		return resultList;
	}

}
