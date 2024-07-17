package org.eclipse.swt.uno;

import org.eclipse.swt.graphics.Rectangle;

import com.sun.star.awt.*;
import com.sun.star.beans.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;
import com.sun.star.uno.Exception;

public class UnoButtonControl extends UnoControl{
	XButton xButton;

	public UnoButtonControl(UnoControl parent) {
		super(parent);

		XComponentContext xContext = UnoLoader.xContext;
		XMultiComponentFactory xMCF = UnoLoader.xMCF;
		XToolkit xToolkit = UnoLoader.xToolkit;

		Object buttonControl;
		try {
			buttonControl = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlButton", xContext);
			Object buttonModel = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlButtonModel", xContext);

			// Definiert die Eigenschaften des Buttons.
			XPropertySet xPSet = UnoRuntime.queryInterface(XPropertySet.class, buttonModel);
			xPSet.setPropertyValue("PositionX", 50);
			xPSet.setPropertyValue("PositionY", 50);
			xPSet.setPropertyValue("Width", 100);
			xPSet.setPropertyValue("Height", 50);
			xPSet.setPropertyValue("Name", "myButton");
			xPSet.setPropertyValue("PushButtonType", (short) PushButtonType.STANDARD_value);
			xPSet.setPropertyValue("Label", "My Button");

			XControl xcon = UnoRuntime.queryInterface(XControl.class, buttonControl);

			xButton = UnoRuntime.queryInterface(XButton.class, xcon);
			xcon.setModel(UnoRuntime.queryInterface(XControlModel.class, buttonModel));
			xcon.createPeer(xToolkit, getPeer());

			XControlModel model = xcon.getModel();

			System.out.println("XControlModel: " + model);

			// There seems to be a bug, we have to reset the position...
			getWindow().setPosSize(100, 100, 500, 500, com.sun.star.awt.PosSize.POSSIZE);

			getWindow().setVisible(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setText(String text) {
		xButton.setLabel(text);
	}

	@Override
	public void setBounds(Rectangle rect) {

		getWindow().setPosSize(rect.x, rect.y, rect.width, rect.height, com.sun.star.awt.PosSize.POSSIZE);

	}

	static int count = 0;

	public void addMouseListener(org.eclipse.swt.events.MouseListener listener) {


		getWindow().addMouseListener(new XMouseListener() {

			@Override
			public void disposing(EventObject arg0) {

			}

			@Override
			public void mouseReleased(com.sun.star.awt.MouseEvent arg0) {
				listener.mouseUp(null);
			}

			@Override
			public void mousePressed(com.sun.star.awt.MouseEvent arg0) {
				listener.mouseDown(null);

			}

			@Override
			public void mouseExited(com.sun.star.awt.MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(com.sun.star.awt.MouseEvent arg0) {

			}
		});

	}

	@Override
	protected XWindow getWindow() {
		XWindowPeer peer = UnoRuntime.queryInterface(XControl.class, xButton).getPeer();
		return UnoRuntime.queryInterface(XWindow.class, peer);
	}

	@Override
	protected XWindowPeer getPeer() {
		return getParent().getPeer();
	}

}
