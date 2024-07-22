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

			// Setze die Eigenschaften des Texts
			// Das scheint nicht richtig zu funktionieren, wir müssen später die position
			// nochmal setzen.
			propFixedText.setPropertyValue("PositionX", 500);
			propFixedText.setPropertyValue("PositionY", 500);
			propFixedText.setPropertyValue("Width", 800);
			propFixedText.setPropertyValue("Height", 400);
			propFixedText.setPropertyValue("Name", "myText");
			propFixedText.setPropertyValue("Label", "Dies ist ein Text");

//			// Erzeugung der UnoControlFixedText-Instanz und Einsetzen des FixedText-Models
//			Object oControl = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlFixedText", xContext);
//			xControl = UnoRuntime.queryInterface(XControl.class, oControl);
//			// das Modell setzen
//			xControl.setModel(UnoRuntime.queryInterface(XControlModel.class, fixedTextModel));
//
//			xFixedText = UnoRuntime.queryInterface(XFixedText.class, xControl);
//
//			xFixedText.setText("1234");
//
//			xControl.createPeer(xToolkit, parentWindowPeer);
//
//
			com.sun.star.awt.WindowDescriptor aDescriptor = new com.sun.star.awt.WindowDescriptor();

			aDescriptor.Type = com.sun.star.awt.WindowClass.CONTAINER;
			aDescriptor.WindowServiceName = "window";
			aDescriptor.ParentIndex = -1;
			aDescriptor.Parent = parentWindowPeer;
			aDescriptor.Bounds = new com.sun.star.awt.Rectangle(0, 0, 50, 50);

//			aDescriptor.WindowAttributes = com.sun.star.awt.WindowAttribute.BORDER
//					| com.sun.star.awt.WindowAttribute.MOVEABLE | com.sun.star.awt.WindowAttribute.SIZEABLE
//					| com.sun.star.awt.WindowAttribute.CLOSEABLE;

			XWindowPeer graphWinPeer = UnoLoader.xToolkit.createWindow(aDescriptor);
			graphWinPeer.invalidate(InvalidateStyle.NOERASE);
			XWindow graphWin = qi(com.sun.star.awt.XWindow.class, graphWinPeer);
			graphWin.setVisible(true);


			XWindow pWin = UnoRuntime.queryInterface(XWindow.class, parentWindowPeer);
			pWin.setVisible(true);
//
//			XWindow win = UnoRuntime.queryInterface(XWindow.class, xControl.getPeer());
			XDevice dev = UnoRuntime.queryInterface(XDevice.class, graphWin);

//			win.setVisible(true);

			XGraphics gr = dev.createGraphics();
			gr.setLineColor(1234151);
			gr.drawRect(0, 0, 20, 20);


//			XCanvas xcan = UnoRuntime.queryInterface(XCanvas.class, gr);
//			System.out.println("XCanvas: " + xcan);

			graphWin.addPaintListener(new XPaintListener() {

				@Override
				public void disposing(EventObject arg0) {

				}

				@Override
				public void windowPaint(PaintEvent arg0) {

//					Object o = arg0.Source;
//					Rectangle updateRect = arg0.UpdateRect;
//					XWindow w = (XWindow)o;
//
//					XWindowPeer peer = qi(XWindowPeer.class, w);
//
//					w.setVisible(true);
//
//					XDevice dev = UnoRuntime.queryInterface(XDevice.class, o);
//
//					XGraphics gr = dev.createGraphics();
//					gr.setLineColor(1234151);
//					gr.setFillColor(255*255*255);
//					gr.drawRect(0, 0, updateRect.Width, updateRect.Height);


				}
			});



			Object canvasFactory = xMCF.createInstanceWithContext("com.sun.star.rendering.CanvasFactory", xContext);
			XMultiComponentFactory xCanvasFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class, canvasFactory);


//			UnoDevice device = new UnoDevice();
//
//			XGraphics graphics = device.getDevice().createGraphics();
//			graphics.drawRect(10, 10, 20, 20);
//
//
//			Object canvas = xCanvasFactory.createInstanceWithContext("com.sun.star.rendering.Canvas", xContext);
//			XCanvas xCanvas = UnoRuntime.queryInterface(XCanvas.class, canvas);

//			xSimpleCanvas.drawRect(new RealRectangle2D(0,0,20,20));
//			xCanvas.drawLine(new RealPoint2D(0,0),new RealPoint2D(20,20), null, null);

//			System.out.println(xCanvas.getDevice().getPhysicalResolution().toString());

//			XControl xCanvasControl = UnoRuntime.queryInterface(XControl.class, canvas);
//			xCanvasControl.createPeer(xToolkit, parentWindowPeer);


//		-------------------------------------- just some testing, not important...
//			XView view = xControl.getView();
//			if (view != null)
//				System.out.println("view: " + view.getSize().Height + "  " + view.getSize().Width);
//			else
//				System.out.println("view is null");
//
//			Object context = xControl.getContext();
//			System.out.println("Context: " + context);
//
//			XControlModel model = xControl.getModel();
//			System.out.println("XControlModel: " + model);
//
//			// There seems to be a bug, we have to reset the position...
//			getWindow().setPosSize(0, 0, 500, 500, com.sun.star.awt.PosSize.POSSIZE);

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
