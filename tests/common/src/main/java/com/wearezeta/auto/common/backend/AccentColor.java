package com.wearezeta.auto.common.backend;

import java.util.NoSuchElementException;

public enum AccentColor {
	Undefined(0, "Undefined"), StrongBlue(1, "StrongBlue"), StrongLimeGreen(2,
			"StrongLimeGreen"), BrightYellow(3, "BrightYellow"), VividRed(4,
			"VividRed"), BrightOrange(5, "BrightOrange"), SoftPink(6,
			"SoftPink"), Violet(7, "Violet");

	private final int id;
	private final String name;

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	private AccentColor(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public static AccentColor getById(int colorId) {
		for (AccentColor color : AccentColor.values()) {
			if (color.getId() == colorId) {
				return color;
			}
		}
		throw new NoSuchElementException(String.format(
				"Accent color id '%d' is unknown", colorId));
	}

	public static AccentColor getByName(String colorName) {
		for (AccentColor color : AccentColor.values()) {
			if (color.getName().equalsIgnoreCase(colorName)) {
				return color;
			}
		}
		throw new NoSuchElementException(String.format(
				"Accent color name '%s' is unknown", colorName));
	}
}
