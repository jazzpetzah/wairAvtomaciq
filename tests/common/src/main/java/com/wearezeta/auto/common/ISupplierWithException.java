package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.util.Optional;

@FunctionalInterface
public interface ISupplierWithException {
	public Optional<BufferedImage> getScreenshot() throws Exception;
}
