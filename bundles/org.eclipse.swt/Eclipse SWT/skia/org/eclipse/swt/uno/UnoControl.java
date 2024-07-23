package org.eclipse.swt.uno;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.sun.star.awt.*;
import com.sun.star.uno.*;

public abstract class UnoControl {

	private final UnoControl parent;

	boolean disposed = false;

	static {
		UnoLoader.init();
	}

	public UnoControl(UnoControl parent) {
		this.parent = parent;
	}

	protected UnoControl getParent() {
		return parent;
	}

	protected static <T> T qi(Class<T> aType, Object o) {
		return UnoRuntime.queryInterface(aType, o);
	}

	public void setVisible(boolean visible) {
		getWindow().setVisible(visible);
	}

	public boolean isDisposed() {
		return disposed;
	}

	public Rectangle getBounds() {
		com.sun.star.awt.Rectangle posSize = getWindow().getPosSize();
		return new Rectangle(posSize.X, posSize.Y, posSize.Width, posSize.Height);

	}

	public void setBounds(Rectangle frame) {
		getWindow().setPosSize(frame.x, frame.y, frame.width, frame.height, com.sun.star.awt.PosSize.POSSIZE);
	}

	public void dispose() {
		getWindow().dispose();
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

	protected abstract XWindowPeer getPeer();

	protected abstract XWindow getWindow();

	public Point getLocation() {
		Rectangle bounds = getBounds();
		return new Point(bounds.x, bounds.y);
	}
}