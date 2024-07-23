/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.printing;


import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are used to print to a printer.
 * Applications create a GC on a printer using <code>new GC(printer)</code>
 * and then draw on the printer GC using the usual graphics calls.
 * <p>
 * A <code>Printer</code> object may be constructed by providing
 * a <code>PrinterData</code> object which identifies the printer.
 * A <code>PrintDialog</code> presents a print dialog to the user
 * and returns an initialized instance of <code>PrinterData</code>.
 * Alternatively, calling <code>new Printer()</code> will construct a
 * printer object for the user's default printer.
 * </p><p>
 * Application code must explicitly invoke the <code>Printer.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see PrinterData
 * @see PrintDialog
 * @see <a href="http://www.eclipse.org/swt/snippets/#printing">Printing snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Printer extends Device {

	@Override
	public long internal_new_GC(GCData data) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void internal_dispose_GC(long handle, GCData data) {
		// TODO Auto-generated method stub

	}

	public PrinterData getPrinterData() {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle computeTrim(int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		return null;
	}

	public Point getDPI() {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle getClientArea() {
		// TODO Auto-generated method stub
		return null;
	}

	public void startPage() {
		// TODO Auto-generated method stub

	}

	public void endPage() {
		// TODO Auto-generated method stub

	}

	public boolean startJob(String jobName) {
		// TODO Auto-generated method stub
		return false;
	}

	public void endJob() {
		// TODO Auto-generated method stub

	}


}
