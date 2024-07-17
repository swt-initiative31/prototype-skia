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

		Object fixedTextModel;
		try {
			fixedTextModel = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlFixedTextModel", xContext);

			XPropertySet propFixedText = UnoRuntime.queryInterface(XPropertySet.class, fixedTextModel);

			// Setze die Eigenschaften des Texts
			// Das scheint nicht richtig zu funktionieren, wir müssen später die position
			// nochmal setzen.
			propFixedText.setPropertyValue("PositionX", 500);
			propFixedText.setPropertyValue("PositionY", 500);
			propFixedText.setPropertyValue("Width", 800);
			propFixedText.setPropertyValue("Height", 400);
			propFixedText.setPropertyValue("Name", "myText");
			propFixedText.setPropertyValue("Label", "Dies ist ein Text");

//		propFixedText.getPropertySetInfo();

			// Erzeugung der UnoControlFixedText-Instanz und Einsetzen des FixedText-Models
			Object oControl = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlFixedText", xContext);
			xControl = UnoRuntime.queryInterface(XControl.class, oControl);
			// das Modell setzen
			xControl.setModel(UnoRuntime.queryInterface(XControlModel.class, fixedTextModel));

			xFixedText = UnoRuntime.queryInterface(XFixedText.class, xControl);

			xFixedText.setText("1234");

			// test for setting colors: blue

			xControl.createPeer(xToolkit, parentWindowPeer);

//		-------------------------------------- just some testing, not important...
			XView view = xControl.getView();
			if (view != null)
				System.out.println("view: " + view.getSize().Height + "  " + view.getSize().Width);
			else
				System.out.println("view is null");

			Object context = xControl.getContext();
			System.out.println("Context: " + context);

			XControlModel model = xControl.getModel();
			System.out.println("XControlModel: " + model);

			// There seems to be a bug, we have to reset the position...
			getWindow().setPosSize(0, 0, 500, 500, com.sun.star.awt.PosSize.POSSIZE);

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
