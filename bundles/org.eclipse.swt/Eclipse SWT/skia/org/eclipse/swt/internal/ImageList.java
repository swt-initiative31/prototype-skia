/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.internal;


import org.eclipse.swt.graphics.*;

public class ImageList {
	public ImageList (int style, int zoom) {
	this (style, 32, 32, zoom);
}

public ImageList (int style, int width, int height, int zoom) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
}

public int add (Image image) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return -1;
}

public int addRef() {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return -1;
}

public void dispose () {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
}

public Image get (int index) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return null;
}

public int getStyle () {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return -1;
}

public long getHandle () {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return -1;
}

public Point getImageSize() {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	int [] cx = new int [1], cy = new int [1];
	return new Point (cx [0], cy [0]);
}

public int indexOf (Image image) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return -1;
}

public void put (int index, Image image) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
}

public void remove (int index) {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
}

public int removeRef() {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return -1;
}

public int size () {
	System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	return -1;
}

}
