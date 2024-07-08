package org.eclipse.swt.uno;

import org.eclipse.swt.graphics.Rectangle;

import com.sun.star.awt.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;

public class UnoInstance {

	XWindow w;
	XWindowPeer p;
	XTopWindow tw;
	boolean disposed = false;
	com.sun.star.awt.Rectangle rec;

	public UnoInstance() {

		UnoLoader.init();

		w = UnoLoader.createWindow();

		// There seems to be a bug, we have to reset the position...
		w.setPosSize(100, 100, 1000, 1000, com.sun.star.awt.PosSize.POSSIZE);
		rec = new com.sun.star.awt.Rectangle(100, 100, 1000, 1000);


		tw = UnoRuntime.queryInterface(XTopWindow.class, w);

		p = UnoRuntime.queryInterface(XWindowPeer.class, w);
		w.setVisible(false);



		w.addWindowListener(new XWindowListener() {

			@Override
			public void disposing(EventObject arg0) {
				System.out.println("dispose");
				disposed = true;

			}

			@Override
			public void windowShown(EventObject arg0) {
				System.out.println("windowShown");
			}

			@Override
			public void windowResized(WindowEvent arg0) {
				System.out.println("windowResized");

			}

			@Override
			public void windowMoved(WindowEvent arg0) {

				System.out.println("windowMoved");

			}

			@Override
			public void windowHidden(EventObject arg0) {
				System.out.println("windowHidden");

			}
		});


		tw.addTopWindowListener(new XTopWindowListener() {

			@Override
			public void disposing(EventObject arg0) {
				System.out.println("disposing");

			}

			@Override
			public void windowOpened(EventObject arg0) {
				System.out.println("windowOpened");

			}

			@Override
			public void windowNormalized(EventObject arg0) {
				System.out.println("windowNormalized");

			}

			@Override
			public void windowMinimized(EventObject arg0) {
				System.out.println("windowMinimized");

			}

			@Override
			public void windowDeactivated(EventObject arg0) {
				System.out.println("windowDeactivated");

			}

			@Override
			public void windowClosing(EventObject arg0) {
				System.out.println("windowClosing");
				UnoLoader.terminate();
				disposed = true;


			}

			@Override
			public void windowClosed(EventObject arg0) {
				System.out.println("windowClose");

			}

			@Override
			public void windowActivated(EventObject arg0) {
				System.out.println("windowActivated");

			}
		});

	}

	public void setVisible(boolean visible) {

		w.setVisible(visible);

	}

	public boolean isDisposed() {
		return disposed;
	}

	public XWindowPeer getPeer() {
		return p;
	}

	public Rectangle getClientArea() {


		return new Rectangle(rec.X, rec.Y, rec.Width, rec.Height);

	}

}
