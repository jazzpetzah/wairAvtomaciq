package com.wearezeta.auto.osx.util;

public class ElementRectangle {

	private int x;
	private int y;
	private int w;
	private int h;

	public ElementRectangle() {
		this.x = -1;
		this.y = -1;
		this.w = 0;
		this.h = 0;
	}

	public ElementRectangle(NSPoint position, NSPoint size) {
		this.x = position.x();
		this.y = position.y();
		this.w = size.x();
		this.h = size.y();
	}

	public ElementRectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.w = width;
	}

	public void setHeight(int height) {
		this.h = height;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int width() {
		return w;
	}

	public int height() {
		return h;
	}

	public String toString() {
		return String.format("[%s,%s] (size: w=%s, h=%s)", x, y, w, h);
	}
}
