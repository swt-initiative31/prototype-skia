/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;

/**
 * Instances of this class represent areas of an x-y coordinate system that are
 * aggregates of the areas covered by a number of polygons.
 * <p>
 * Application code must explicitly invoke the <code>Region.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      GraphicsExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public final class Region extends Resource {

	/**
	 * Constructs a new empty region.
	 * <p>
	 * You must dispose the region when it is no longer required.
	 * </p>
	 *
	 * @exception SWTError
	 *                     <ul>
	 *                     <li>ERROR_NO_HANDLES if a handle could not be obtained
	 *                     for region creation</li>
	 *                     </ul>
	 *
	 * @see #dispose()
	 */
	public Region() {
		this(null);
	}

	/**
	 * Constructs a new empty region.
	 * <p>
	 * You must dispose the region when it is no longer required.
	 * </p>
	 *
	 * @param device the device on which to allocate the region
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if device is
	 *                                     null and there is no current device</li>
	 *                                     </ul>
	 * @exception SWTError
	 *                                     <ul>
	 *                                     <li>ERROR_NO_HANDLES if a handle could
	 *                                     not be obtained for region creation</li>
	 *                                     </ul>
	 *
	 * @see #dispose()
	 *
	 * @since 3.0
	 */
	public Region(Device device) {
		super(device);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		init();
	}

	/**
	 * Constructs a new region given a handle to the operating system resources that
	 * it should represent.
	 *
	 * @param handle the handle for the result
	 */
	Region(Device device, int handle) {
		super(device);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Adds the given polygon to the collection of polygons the receiver maintains
	 * to describe its area.
	 *
	 * @param pointArray points that describe the polygon to merge with the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void add(int[] pointArray) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (pointArray == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Adds the given rectangle to the collection of polygons the receiver maintains
	 * to describe its area.
	 *
	 * @param rect the rectangle to merge with the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     rectangle's width or height is
	 *                                     negative</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 */
	public void add(Rectangle rect) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (rect == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		rect = DPIUtil.autoScaleUp(rect);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Adds the given rectangle to the collection of polygons the receiver maintains
	 * to describe its area.
	 *
	 * @param x      the x coordinate of the rectangle
	 * @param y      the y coordinate of the rectangle
	 * @param width  the width coordinate of the rectangle
	 * @param height the height coordinate of the rectangle
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     rectangle's width or height is
	 *                                     negative</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.1
	 */
	public void add(int x, int y, int width, int height) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Adds all of the polygons which make up the area covered by the argument to
	 * the collection of polygons the receiver maintains to describe its area.
	 *
	 * @param region the region to merge
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 */
	public void add(Region region) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (region == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (region.isDisposed())
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Returns <code>true</code> if the point specified by the arguments is inside
	 * the area specified by the receiver, and <code>false</code> otherwise.
	 *
	 * @param x the x coordinate of the point to test for containment
	 * @param y the y coordinate of the point to test for containment
	 * @return <code>true</code> if the region contains the point and
	 *         <code>false</code> otherwise
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 */
	public boolean contains(int x, int y) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	/**
	 * Returns <code>true</code> if the given point is inside the area specified by
	 * the receiver, and <code>false</code> otherwise.
	 *
	 * @param pt the point to test for containment
	 * @return <code>true</code> if the region contains the point and
	 *         <code>false</code> otherwise
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 */
	public boolean contains(Point pt) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (pt == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	/**
	 * Compares the argument to the receiver, and returns true if they represent the
	 * <em>same</em> object using a class specific comparison.
	 *
	 * @param object the object to compare with this object
	 * @return <code>true</code> if the object is the same as this object and
	 *         <code>false</code> otherwise
	 *
	 * @see #hashCode
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof Region))
			return false;
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	/**
	 * Returns a rectangle which represents the rectangular union of the collection
	 * of polygons the receiver maintains to describe its area.
	 *
	 * @return a bounding rectangle for the region
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 *
	 * @see Rectangle#union
	 */
	public Rectangle getBounds() {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	/**
	 * Returns an integer hash code for the receiver. Any two objects that return
	 * <code>true</code> when passed to <code>equals</code> must return the same
	 * value for this method.
	 *
	 * @return the receiver's hash
	 *
	 * @see #equals
	 */
	@Override
	public int hashCode() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return -1;
	}

	/**
	 * Intersects the given rectangle to the collection of polygons the receiver
	 * maintains to describe its area.
	 *
	 * @param rect the rectangle to intersect with the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     rectangle's width or height is
	 *                                     negative</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void intersect(Rectangle rect) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (rect == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Intersects the given rectangle to the collection of polygons the receiver
	 * maintains to describe its area.
	 *
	 * @param x      the x coordinate of the rectangle
	 * @param y      the y coordinate of the rectangle
	 * @param width  the width coordinate of the rectangle
	 * @param height the height coordinate of the rectangle
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     rectangle's width or height is
	 *                                     negative</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.1
	 */
	public void intersect(int x, int y, int width, int height) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Intersects all of the polygons which make up the area covered by the argument
	 * to the collection of polygons the receiver maintains to describe its area.
	 *
	 * @param region the region to intersect
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void intersect(Region region) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (region == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (region.isDisposed())
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Returns <code>true</code> if the rectangle described by the arguments
	 * intersects with any of the polygons the receiver maintains to describe its
	 * area, and <code>false</code> otherwise.
	 *
	 * @param x      the x coordinate of the origin of the rectangle
	 * @param y      the y coordinate of the origin of the rectangle
	 * @param width  the width of the rectangle
	 * @param height the height of the rectangle
	 * @return <code>true</code> if the rectangle intersects with the receiver, and
	 *         <code>false</code> otherwise
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 *
	 * @see Rectangle#intersects(Rectangle)
	 */
	public boolean intersects(int x, int y, int width, int height) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	/**
	 * Returns <code>true</code> if the given rectangle intersects with any of the
	 * polygons the receiver maintains to describe its area and <code>false</code>
	 * otherwise.
	 *
	 * @param rect the rectangle to test for intersection
	 * @return <code>true</code> if the rectangle intersects with the receiver, and
	 *         <code>false</code> otherwise
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @see Rectangle#intersects(Rectangle)
	 */
	public boolean intersects(Rectangle rect) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (rect == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	/**
	 * Returns <code>true</code> if the region has been disposed, and
	 * <code>false</code> otherwise.
	 * <p>
	 * This method gets the dispose state for the region. When a region has been
	 * disposed, it is an error to invoke any other method (except
	 * {@link #dispose()}) using the region.
	 *
	 * @return <code>true</code> when the region is disposed, and <code>false</code>
	 *         otherwise
	 */
	@Override
	public boolean isDisposed() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	/**
	 * Returns <code>true</code> if the receiver does not cover any area in the (x,
	 * y) coordinate plane, and <code>false</code> if the receiver does cover some
	 * area in the plane.
	 *
	 * @return <code>true</code> if the receiver is empty, and <code>false</code>
	 *         otherwise
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 */
	public boolean isEmpty() {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	/**
	 * Subtracts the given polygon from the collection of polygons the receiver
	 * maintains to describe its area.
	 *
	 * @param pointArray points that describe the polygon to merge with the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void subtract(int[] pointArray) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (pointArray == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Subtracts the given rectangle from the collection of polygons the receiver
	 * maintains to describe its area.
	 *
	 * @param rect the rectangle to subtract from the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     rectangle's width or height is
	 *                                     negative</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void subtract(Rectangle rect) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (rect == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		rect = DPIUtil.autoScaleUp(rect);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Subtracts the given rectangle from the collection of polygons the receiver
	 * maintains to describe its area.
	 *
	 * @param x      the x coordinate of the rectangle
	 * @param y      the y coordinate of the rectangle
	 * @param width  the width coordinate of the rectangle
	 * @param height the height coordinate of the rectangle
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     rectangle's width or height is
	 *                                     negative</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.1
	 */
	public void subtract(int x, int y, int width, int height) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Subtracts all of the polygons which make up the area covered by the argument
	 * from the collection of polygons the receiver maintains to describe its area.
	 *
	 * @param region the region to subtract
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.0
	 */
	public void subtract(Region region) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (region == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (region.isDisposed())
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Translate all of the polygons the receiver maintains to describe its area by
	 * the specified point.
	 *
	 * @param x the x coordinate of the point to translate
	 * @param y the y coordinate of the point to translate
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 *
	 * @since 3.1
	 */
	public void translate(int x, int y) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Translate all of the polygons the receiver maintains to describe its area by
	 * the specified point.
	 *
	 * @param pt the point to translate
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the argument
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_GRAPHIC_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     </ul>
	 *
	 * @since 3.1
	 */
	public void translate(Point pt) {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (pt == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		pt = DPIUtil.autoScaleUp(pt);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Returns a string containing a concise, human-readable description of the
	 * receiver.
	 *
	 * @return a string representation of the receiver
	 */
	@Override
	public String toString() {
		if (isDisposed())
			return "Region {*DISPOSED*}";
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return "Region";
	}

}
