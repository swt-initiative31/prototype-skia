package org.eclipse.swt.uno;

public class UnoColor {

	private int color;

	public UnoColor(int[] color) {
		this(color[0], color[1], color[2], color[3]);
	}

	public UnoColor(int red, int green, int blue, int alpha) {
		setColor((255 - alpha) * (256 * 256 * 256) + red * (256 * 256) + green * (256) + blue);
	}

	public void release() {
		System.out.println("WARN: Not implemented yet: " + new Exception().getStackTrace()[0]);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}
