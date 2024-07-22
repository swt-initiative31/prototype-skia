package org.eclipse.swt.uno;

import com.sun.star.awt.*;

public class UnoDevice {
	private XDevice device;

	public UnoDevice() {

		device = UnoLoader.xToolkit.createScreenCompatibleDevice(100, 100);

	}

	public XDevice getDevice() {
		return device;
	}
}
