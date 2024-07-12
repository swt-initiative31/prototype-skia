package org.eclipse.swt.uno;

import com.sun.star.awt.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;

public class UnoWindow extends UnoControl {

	com.sun.star.awt.Rectangle posSize;

	public UnoWindow() {
		super();

		w = UnoLoader.createWindow();

		// There seems to be a bug, we have to reset the position...
		posSize = new com.sun.star.awt.Rectangle(100, 100, 1000, 1000);
		w.setPosSize(posSize.X, posSize.Y, posSize.Width, posSize.Height, com.sun.star.awt.PosSize.POSSIZE);


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
				w.dispose();
				disposed = true;
				UnoLoader.terminate();
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

	// This is most probably wrong. In UNO there isn't something like a content pane
	public UnoWindow getContentPane() {
		return this;
	}

}
