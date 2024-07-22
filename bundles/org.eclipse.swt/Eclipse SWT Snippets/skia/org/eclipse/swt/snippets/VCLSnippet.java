package org.eclipse.swt.snippets;

import org.eclipse.swt.uno.*;

import com.sun.star.awt.*;
import com.sun.star.uno.*;

public class VCLSnippet {
	public static void main(String[] args) {

		UnoLoader.init();

		com.sun.star.awt.WindowDescriptor aDescriptor = new com.sun.star.awt.WindowDescriptor();

		aDescriptor.Type = com.sun.star.awt.WindowClass.TOP;
		aDescriptor.WindowServiceName = "window";
		aDescriptor.ParentIndex = -1;
		aDescriptor.Parent = null;
		aDescriptor.Bounds = new com.sun.star.awt.Rectangle(100, 100, 800, 800);

		aDescriptor.WindowAttributes = com.sun.star.awt.WindowAttribute.BORDER
				| com.sun.star.awt.WindowAttribute.MOVEABLE | com.sun.star.awt.WindowAttribute.SIZEABLE
				| com.sun.star.awt.WindowAttribute.CLOSEABLE;

		XWindowPeer parentPeer = UnoLoader.xToolkit.createWindow(aDescriptor);
		XWindow w = UnoRuntime.queryInterface(com.sun.star.awt.XWindow.class, parentPeer);

		parentPeer.invalidate(InvalidateStyle.NOTRANSPARENT);
		w.setVisible(true);

		XDevice dev = UnoRuntime.queryInterface(XDevice.class, w);

		XGraphics gr = dev.createGraphics();
		gr.setLineColor(1234151);
		gr.drawRect(0, 0, 100, 100);

		w.addPaintListener(new XPaintListener() {

			@Override
			public void disposing(com.sun.star.lang.EventObject arg0) {

			}

			@Override
			public void windowPaint(PaintEvent arg0) {

				Object o = arg0.Source;
				Rectangle updateRect = arg0.UpdateRect;
				XWindow w = (XWindow) o;

//				XWindowPeer peer = UnoRuntime.queryInterface(XWindowPeer.class, w);

				w.setVisible(true);

				XDevice dev = UnoRuntime.queryInterface(XDevice.class, o);

				XGraphics gr = dev.createGraphics();
				gr.setLineColor(1234151);
				gr.setFillColor(255 * 255 * 255);
				gr.drawRect(0, 0, updateRect.Width, updateRect.Height);

			}
		});

	}
}
