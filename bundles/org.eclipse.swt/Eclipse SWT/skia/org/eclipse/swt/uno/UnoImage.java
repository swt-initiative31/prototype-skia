package org.eclipse.swt.uno;

import java.lang.IllegalArgumentException;

import com.sun.star.awt.*;
import com.sun.star.beans.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;


public class UnoImage implements UnoDrawable{


	private Object imageModel;
	private XWindow xWindow;
	private XBitmap bitmap;
	private int width;
	private int height;
	private XWindow window;

	public UnoImage(UnoDevice unoDevice, int width, int height) {
		this.width = width;
		this.height = height;

		com.sun.star.awt.WindowDescriptor aDescriptor = new com.sun.star.awt.WindowDescriptor();

		aDescriptor.Type = com.sun.star.awt.WindowClass.TOP;
		aDescriptor.WindowServiceName = "window";
		aDescriptor.ParentIndex = -1;
		aDescriptor.Parent = null;
		aDescriptor.Bounds = new com.sun.star.awt.Rectangle(100, 100, this.width, this.height);

		aDescriptor.WindowAttributes = com.sun.star.awt.WindowAttribute.BORDER
				| com.sun.star.awt.WindowAttribute.MOVEABLE | com.sun.star.awt.WindowAttribute.SIZEABLE
				| com.sun.star.awt.WindowAttribute.CLOSEABLE;

		XWindowPeer xWindowPeer = UnoLoader.xToolkit.createWindow(aDescriptor);
		xWindow = UnoRuntime.queryInterface(XWindow.class, xWindowPeer);
		xWindow.setVisible(true);
//		try {
//			imageModel = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlImageControlModel", xContext);
//
//			XPropertySet xPSet = UnoRuntime.queryInterface(XPropertySet.class, imageModel);
//			xPSet.setPropertyValue("Border", Short.valueOf((short) 1));
//			xPSet.setPropertyValue("Height", Integer.valueOf(50));
//			xPSet.setPropertyValue("Name", "ImageConrol");
//			xPSet.setPropertyValue("PositionX", Integer.valueOf(50));
//			xPSet.setPropertyValue("PositionY", Integer.valueOf(50));
//			xPSet.setPropertyValue("ScaleImage", Boolean.FALSE);
//			xPSet.setPropertyValue("HelpText", "Help Text");
//			xPSet.setPropertyValue("Width", Integer.valueOf(50));
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public UnoImage initWithSize(int width, int height) {
		if (imageModel != null) {
			XPropertySet xPSet = UnoRuntime.queryInterface(XPropertySet.class, imageModel);
			try {
				xPSet.setPropertyValue("Height", Integer.valueOf(height));
				xPSet.setPropertyValue("Width", Integer.valueOf(width));
			} catch (IllegalArgumentException | UnknownPropertyException |
					WrappedTargetException | com.sun.star.lang.IllegalArgumentException | com.sun.star.beans.PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this;
	}

	@Override
	public XWindow getXWindow() {
		return xWindow;
	}

	@Override
	public void setXBitmap(XBitmap bitmap) {
		this.bitmap = bitmap;
	}

	private class PaintListener implements XPaintListener{

		private XDisplayBitmap displayBitmap;
		private XGraphics xGraphics;

		public PaintListener(XGraphics xGraphics, XDisplayBitmap displayBitmap) {
			this.xGraphics = xGraphics;
			this.displayBitmap = displayBitmap;
		}

		@Override
		public void disposing(com.sun.star.lang.EventObject arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowPaint(PaintEvent arg0) {
			XWindow window = UnoRuntime.queryInterface(XWindow.class, arg0.Source);
			com.sun.star.awt.Rectangle posSize = window.getPosSize();
			xGraphics.draw(displayBitmap, 0, 0, posSize.Width, posSize.Height, 0, 0, posSize.Width, posSize.Height);
		}


	}

	public void showOn(UnoControl unoControl) {
		if (xWindow != null) {
			xWindow.dispose();
			xWindow = null;
		}
		com.sun.star.awt.WindowDescriptor aDescriptor = new com.sun.star.awt.WindowDescriptor();

		XWindow parentWindow = unoControl.getWindow();
		XWindowPeer parentWindowPeer = UnoRuntime.queryInterface(XWindowPeer.class, parentWindow);


		aDescriptor.Type = com.sun.star.awt.WindowClass.CONTAINER;
		aDescriptor.WindowServiceName = "window";
		aDescriptor.ParentIndex = -1;
		aDescriptor.Parent = parentWindowPeer;
		aDescriptor.Bounds = new com.sun.star.awt.Rectangle(0, 0, this.width, this.height);

//		aDescriptor.WindowAttributes = com.sun.star.awt.WindowAttribute.BORDER
//				| com.sun.star.awt.WindowAttribute.MOVEABLE | com.sun.star.awt.WindowAttribute.SIZEABLE
//				| com.sun.star.awt.WindowAttribute.CLOSEABLE;

		XWindowPeer xWindowPeer = UnoLoader.xToolkit.createWindow(aDescriptor);
		xWindow = UnoRuntime.queryInterface(XWindow.class, xWindowPeer);
		xWindow.setVisible(true);

		XDevice device = UnoRuntime.queryInterface(XDevice.class, xWindow);
		XGraphics graphics = device.createGraphics();
		XDisplayBitmap displayBitmap = device.createDisplayBitmap(bitmap);

		xWindow.addPaintListener(new PaintListener(graphics, displayBitmap));


	}
}
