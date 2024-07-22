package org.eclipse.swt.uno;

import java.lang.Exception;
import java.lang.IllegalArgumentException;

import com.sun.star.awt.*;
import com.sun.star.beans.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;


public class UnoImage {


	private Object imageModel;
	private XGraphics graphics;

	public UnoImage(UnoDevice unoDevice) {
		XComponentContext xContext = UnoLoader.xContext;
		XMultiComponentFactory xMCF = UnoLoader.xMCF;
		XToolkit xToolkit = UnoLoader.xToolkit;


		try {
//
//
//
//
//			Object canvas = xMCF.createInstanceWithContext("com.sun.star.rendering.Canvas", xContext);
//			XCanvas xCanvas = UnoRuntime.queryInterface(XCanvas.class, canvas);
//
//			XDevice xDevice = unoDevice.getDevice();
//
//
//	        Rectangle r = new Rectangle();
//	        xCanvas.initialize(xDevice, r, XGraphicDevice.class);
//
//
//			XGraphicDevice device = xCanvas.getDevice();
//			graphics = unoDevice.getDevice().createGraphics();
//			graphics.drawRect(10, 10, 100, 100);
//
//
//	        XBitmap xBitmap = unoDevice.getDevice().createBitmap(0, 0, 100, 100);
//	        xBitmap.
//
//	        // Transform XBitmap into XGraphic
//	        Object bitmap = xMCF.createInstanceWithContext("com.sun.star.graphic.Bitmap", xContext);
//	        XGraphic xGraphic = UnoRuntime.queryInterface(XGraphic.class, bitmap);
//	        xGraphic.initialize(xBitmap.getDIB());

			imageModel = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlImageControlModel", xContext);

			XPropertySet xPSet = UnoRuntime.queryInterface(XPropertySet.class, imageModel);
			xPSet.setPropertyValue("Border", Short.valueOf((short) 1));
			xPSet.setPropertyValue("Height", Integer.valueOf(50));
			xPSet.setPropertyValue("Name", "ImageConrol");
			xPSet.setPropertyValue("PositionX", Integer.valueOf(50));
			xPSet.setPropertyValue("PositionY", Integer.valueOf(50));
			xPSet.setPropertyValue("ScaleImage", Boolean.FALSE);
			xPSet.setPropertyValue("HelpText", "Help Text");
			xPSet.setPropertyValue("Width", Integer.valueOf(50));
//			xPSet.setPropertyValue("Graphic", //put xGrahic here);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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


}
