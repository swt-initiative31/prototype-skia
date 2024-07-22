package org.eclipse.swt.uno;

import com.sun.star.awt.*;
import com.sun.star.uno.*;

public abstract class UnoCanvas extends UnoScrollableControl {

	XWindow w;
	XGraphics g;

	public UnoCanvas(UnoControl parent) {
		super(parent);

		w = parent.getWindow();

	}

	public XGraphics getGraphics() {
		if (g == null) {
			XDevice dev = UnoRuntime.queryInterface(XDevice.class, w);
			XGraphics g = dev.createGraphics();
		}
		return g;
	}
}
