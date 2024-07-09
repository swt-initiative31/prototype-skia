package org.eclipse.swt.uno;

import org.eclipse.swt.graphics.*;

public abstract class UnoComponent {

	public UnoComponent() {
		UnoLoader.init();
	}


	public abstract Rectangle getFrame();
	public abstract void setFrame(Rectangle frame);
}