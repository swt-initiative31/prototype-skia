package org.eclipse.swt.uno;

import com.sun.star.awt.*;
import com.sun.star.beans.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;
import com.sun.star.uno.Exception;

public class UnoLabelControl extends UnoControl {

	private XFixedText xFixedText;
	private XControl xControl;

	public UnoLabelControl(UnoControl parent) {
		super(parent);

		XComponentContext xContext = UnoLoader.xContext;
		XMultiComponentFactory xMCF = UnoLoader.xMCF;
		XToolkit xToolkit = UnoLoader.xToolkit;

		XWindowPeer parentWindowPeer = parent.getPeer();
		parentWindowPeer.invalidate(InvalidateStyle.NOERASE);

		Object fixedTextModel;
		try {
			fixedTextModel = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlFixedTextModel", xContext);

			XPropertySet propFixedText = UnoRuntime.queryInterface(XPropertySet.class, fixedTextModel);

			propFixedText.setPropertyValue("PositionX", 500);
			propFixedText.setPropertyValue("PositionY", 500);
			propFixedText.setPropertyValue("Width", 800);
			propFixedText.setPropertyValue("Height", 400);
			propFixedText.setPropertyValue("Name", "myText");

			Object oControl = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlFixedText", xContext);
			xControl = UnoRuntime.queryInterface(XControl.class, oControl);

			xControl.setModel(UnoRuntime.queryInterface(XControlModel.class, fixedTextModel));

			xFixedText = UnoRuntime.queryInterface(XFixedText.class, xControl);
			xControl.createPeer(xToolkit, parentWindowPeer);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setText(String text) {
		xFixedText.setText(text);
	}

	@Override
	protected XWindow getWindow() {
		XWindowPeer peer = xControl.getPeer();
		return UnoRuntime.queryInterface(XWindow.class, peer);
	}

	@Override
	protected XWindowPeer getPeer() {
		return getParent().getPeer();
	}
}
