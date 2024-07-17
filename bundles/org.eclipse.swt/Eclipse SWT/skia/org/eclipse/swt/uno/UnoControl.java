package org.eclipse.swt.uno;

import org.eclipse.swt.graphics.Rectangle;

import com.sun.star.awt.*;
import com.sun.star.uno.*;

public abstract class UnoControl {

	XWindow w;
	XWindowPeer p;
	XTopWindow tw;
	boolean disposed = false;

	UnoControl parent;

	static {
		UnoLoader.init();
	}

	protected static <T> T qi(Class<T> aType, Object o) {
		return UnoRuntime.queryInterface(aType, o);
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

	public Rectangle getBounds() {
		com.sun.star.awt.Rectangle posSize = w.getPosSize();
		return new Rectangle(posSize.X, posSize.Y, posSize.Width, posSize.Height);

	}

	public void setBounds(Rectangle frame) {
		w.setPosSize(frame.x, frame.y, frame.width, frame.height, com.sun.star.awt.PosSize.POSSIZE);
	}

	public void dispose() {
		w.dispose();
		disposed = true;
	}

	public void setLocation(int x, int y) {
		Rectangle current = getBounds();
		setBounds(new Rectangle(x, y, current.width, current.height));
	}

	public void setSize(int width, int height) {
		Rectangle current = getBounds();
		setBounds(new Rectangle(current.x, current.y, width, height));
	}


}