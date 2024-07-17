package org.eclipse.swt.uno;

import org.eclipse.swt.graphics.Rectangle;

import com.sun.star.awt.*;
import com.sun.star.beans.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;
import com.sun.star.uno.Exception;

public class UnoProgressBar extends UnoControl {
	private XProgressBar xProgressBar;

	private int min;
	private int max;

	public UnoProgressBar(UnoControl parent) {
		this.parent = parent;

		XComponentContext xContext = UnoLoader.xContext;
		XMultiComponentFactory xMCF = UnoLoader.xMCF;
		XToolkit xToolkit = UnoLoader.xToolkit;

		Object progressBarControl;
		try {
			Object progressBarModel = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlProgressBarModel", xContext);

			XPropertySet xPSet = UnoRuntime.queryInterface(XPropertySet.class, progressBarModel);
			xPSet.setPropertyValue("PositionX", 50);
			xPSet.setPropertyValue("PositionY", 50);
			xPSet.setPropertyValue("Width", 100);
			xPSet.setPropertyValue("Height", 50);
			xPSet.setPropertyValue("Name", "progressBar");
			xPSet.setPropertyValue("Label", "Progress Bar");

			progressBarControl = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlProgressBar", xContext);
			XControl xControl = UnoRuntime.queryInterface(XControl.class, progressBarControl);

			xProgressBar = UnoRuntime.queryInterface(XProgressBar.class, xControl);
			xControl.setModel(UnoRuntime.queryInterface(XControlModel.class, progressBarModel));
			xControl.createPeer(xToolkit, getPeer());

			setBounds(new Rectangle(100, 100, 500, 500));
			setVisible(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public void setRange(int min, int max) {
		this.min = min;
		this.max = max;
		xProgressBar.setRange(min, max);
	}

	public int getValue() {
		return xProgressBar.getValue();
	}

	public void setValue(int value) {
		xProgressBar.setValue(value);
	}

	@Override
	public XWindowPeer getPeer() {
		return parent.getPeer();
	}

	@Override
	protected XWindow getWindow() {
		XWindowPeer peer = UnoRuntime.queryInterface(XControl.class, xProgressBar).getPeer();
		return UnoRuntime.queryInterface(XWindow.class, peer);
	}

}
