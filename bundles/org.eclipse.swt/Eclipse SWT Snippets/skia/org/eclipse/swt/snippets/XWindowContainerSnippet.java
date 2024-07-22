package org.eclipse.swt.snippets;

import com.sun.star.awt.*;
import com.sun.star.uno.*;

public class XWindowContainerSnippet {

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

		short flags = com.sun.star.awt.InvalidateStyle.NOCHILDREN;
		peer.invalidate(flags);
		aDescriptor = null;

		WindowDescriptor childDesc = new com.sun.star.awt.WindowDescriptor();

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


	}
}
