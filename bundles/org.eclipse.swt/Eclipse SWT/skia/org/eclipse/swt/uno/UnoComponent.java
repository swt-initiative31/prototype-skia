package org.eclipse.swt.uno;

import com.sun.star.awt.*;

public abstract class UnoComponent {

	static {
		UnoLoader.init();
	}

	public abstract Rectangle getFrame();

	public abstract void setFrame(Rectangle frame);

	protected abstract XWindowPeer getPeer();

}