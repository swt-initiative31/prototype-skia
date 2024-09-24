package org.eclipse.swt.examples.helloworld;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class CanvasExample {

	public static void main(String[] args) {
		var display = new Display();
		var shell = new Shell(display);
		shell.setText("Snippet 245");

		Canvas canvas = new Canvas(shell, SWT.NONE);

		canvas.addPaintListener(e -> {

			Rectangle clientArea = canvas.getClientArea();
			e.gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
			e.gc.fillOval(0, 0, clientArea.width, clientArea.height);

		});
		Rectangle clientArea = canvas.getClientArea();
		canvas.setBounds(clientArea.x + 10, clientArea.y + 10, 800, 600);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
