package org.eclipse.swt.snippets;

import com.sun.star.awt.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;

public class XGraphicsSnippet {

	static int calls = 0;

	public static void main(String[] args) {

		LocalUnoLoader.init();

		com.sun.star.awt.WindowDescriptor aDescriptor = new com.sun.star.awt.WindowDescriptor();

		aDescriptor.Type = com.sun.star.awt.WindowClass.TOP;
		aDescriptor.WindowServiceName = "window";
		aDescriptor.ParentIndex = -1;
		aDescriptor.Parent = null;
		aDescriptor.Bounds = new com.sun.star.awt.Rectangle(100, 100, 800, 800);

		aDescriptor.WindowAttributes = com.sun.star.awt.WindowAttribute.BORDER
				| com.sun.star.awt.WindowAttribute.MOVEABLE | com.sun.star.awt.WindowAttribute.SIZEABLE
				| com.sun.star.awt.WindowAttribute.CLOSEABLE;

		XWindowPeer peer = LocalUnoLoader.xToolkit.createWindow(aDescriptor);
		XWindow w = UnoRuntime.queryInterface(com.sun.star.awt.XWindow.class, peer);

		w.setVisible(true);

		XTopWindow top = UnoRuntime.queryInterface(com.sun.star.awt.XTopWindow.class, peer);

		top.addTopWindowListener(new XTopWindowListener() {

			@Override
			public void disposing(EventObject arg0) {
			}

			@Override
			public void windowOpened(EventObject arg0) {
			}

			@Override
			public void windowNormalized(EventObject arg0) {
			}

			@Override
			public void windowMinimized(EventObject arg0) {
			}

			@Override
			public void windowDeactivated(EventObject arg0) {

			}

			@Override
			public void windowClosing(EventObject arg0) {

			}

			@Override
			public void windowClosed(EventObject arg0) {
			w.dispose();
			}

			@Override
			public void windowActivated(EventObject arg0) {
			}
		});



		aDescriptor = null;


		com.sun.star.awt.WindowDescriptor childDesc  = new com.sun.star.awt.WindowDescriptor();

		childDesc.Type = com.sun.star.awt.WindowClass.CONTAINER;
		childDesc.WindowServiceName = "window";
		childDesc.ParentIndex = 1;
		childDesc.Parent = peer;
		childDesc.Bounds = new com.sun.star.awt.Rectangle(0, 0, 400, 400);

		childDesc.WindowAttributes = com.sun.star.awt.WindowAttribute.BORDER
				| com.sun.star.awt.WindowAttribute.MOVEABLE | com.sun.star.awt.WindowAttribute.SIZEABLE
				| com.sun.star.awt.WindowAttribute.CLOSEABLE;

		XWindowPeer childPeer = LocalUnoLoader.xToolkit.createWindow(childDesc);
		XWindow childW = UnoRuntime.queryInterface(com.sun.star.awt.XWindow.class, childPeer);


		childPeer.setBackground(255 * 255 * 255);
		childW.setVisible(true);

		XDevice dev = UnoRuntime.queryInterface(XDevice.class, childW);

		XGraphics gr = dev.createGraphics();
		gr.setLineColor(1234151);
		gr.setFillColor(1234151);
		gr.drawEllipse(0, 0, 100, 100);

		// This circle vanishes after a resize or any kind of redraw.


		w.addPaintListener(new XPaintListener() {

			@Override
			public void disposing(com.sun.star.lang.EventObject arg0) {

			}

			@Override
			public void windowPaint(PaintEvent arg0) {

				Object o = arg0.Source;
				Rectangle updateRect = arg0.UpdateRect;
				XWindow w = (XWindow) o;

				System.out.println("windowPaint:" + calls);
				System.out.println(w.getPosSize().Width + "  " + w.getPosSize().Height);


				calls++;


				XDevice dev = UnoRuntime.queryInterface(XDevice.class, o);

				XGraphics gr = dev.createGraphics();
				gr.setLineColor(1234151);
				gr.setFillColor(1234151);
				gr.drawRect(0, 0, updateRect.Width, updateRect.Height);

			}
		});

	}
}
