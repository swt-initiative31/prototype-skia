package org.eclipse.swt.uno;

import java.util.*;

import com.sun.star.awt.*;
import com.sun.star.uno.*;

public class UnoGraphics {

	static long idCounter = 0;
	static HashMap<Long, UnoGraphics> graphicsMap = new HashMap<>();

	public static UnoGraphics Get(long id) {
		return graphicsMap.get(Long.valueOf(id));
	}

	private XGraphics xGraphics;
	private XDevice xDevice;
	private UnoDrawable unoDrawable;
	private long id;

	public UnoGraphics(UnoDevice unoDevice, UnoDrawable unoDrawable) {
		XWindow xWindow = unoDrawable.getXWindow();

		xDevice  = UnoRuntime.queryInterface(XDevice.class, xWindow);
		xGraphics = xDevice.createGraphics();
		this.unoDrawable = unoDrawable;
		registerGraphics();
	}

	private void registerGraphics() {
		this.id = idCounter++;
		graphicsMap.put(Long.valueOf(id), this);
	}

	public void fillRectangle(int x, int y, int width, int height) {
		XWindow window = unoDrawable.getXWindow();
		com.sun.star.awt.Rectangle posSize = window.getPosSize();

		xGraphics.setFillColor(255);
		xGraphics.setLineColor(255);
		xGraphics.drawRect(x,y, width, height);

		XBitmap bitmap = xDevice.createBitmap(0, 0, posSize.Width, posSize.Height);

		unoDrawable.setXBitmap(bitmap);
	}

	public long getId() {
		return id;
	}



}
