package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class ScrollBar extends Widget {

	Scrollable parent;

	/**
	 * Constructs a new instance of this class given its parent
	 * and a style value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in
	 * class <code>SWT</code> which is applicable to instances of this
	 * class, or must be built by <em>bitwise OR</em>'ing together
	 * (that is, using the <code>int</code> "|" operator) two or more
	 * of those <code>SWT</code> style constants. The class description
	 * lists the style constants that are applicable to the class.
	 * Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new instance (cannot be null)
	 * @param style the style of control to construct
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 *
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	ScrollBar (Scrollable parent, int style) {
		super (parent, checkStyle (style));
		this.parent = parent;
		createWidget ();
	}

	static int checkStyle (int style) {
		return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
	}

	/**
	 * Sets the maximum. If this value is negative or less than or
	 * equal to the minimum, the value is ignored. If necessary, first
	 * the thumb and then the selection are adjusted to fit within the
	 * new range.
	 *
	 * @param value the new maximum
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setMaximum (int value) {
		checkWidget();
		if (value < 0) return;
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Sets the minimum value. If this value is negative or greater
	 * than or equal to the maximum, the value is ignored. If necessary,
	 * first the thumb and then the selection are adjusted to fit within
	 * the new range.
	 *
	 * @param value the new minimum
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setMinimum (int value) {
		checkWidget();
		if (value < 0) return;
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Sets the thumb value. The thumb value should be used to represent
	 * the size of the visual portion of the current range. This value is
	 * usually the same as the page increment value.
	 * <p>
	 * This new value will be ignored if it is less than one, and will be
	 * clamped if it exceeds the receiver's current range.
	 * </p>
	 *
	 * @param value the new thumb value, which must be at least one and not
	 * larger than the size of the current range
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setThumb (int value) {
		checkWidget();
		if (value < 1) return;
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void setVisible(boolean visible) {
		checkWidget();
		if (visible == getVisible ()) return;
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Returns <code>true</code> if the receiver is visible, and
	 * <code>false</code> otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some
	 * other condition makes the receiver not visible, this method
	 * may still indicate that it is considered visible even though
	 * it may not actually be showing.
	 * </p>
	 *
	 * @return the receiver's visibility state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public boolean getVisible () {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return true;
	}

	/**
	 * Returns a point describing the receiver's size. The
	 * x coordinate of the result is the width of the receiver.
	 * The y coordinate of the result is the height of the
	 * receiver.
	 *
	 * @return the receiver's size
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public Point getSize () {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return new Point(10, 20);
	}
	/**
	 * Returns the single 'selection' that is the receiver's value.
	 *
	 * @return the selection
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public int getSelection() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	/**
	 * Sets the single <em>selection</em> that is the receiver's
	 * value to the argument which must be greater than or equal
	 * to zero.
	 *
	 * @param selection the new selection (must be zero or greater)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setSelection(int y) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Sets the amount that the receiver's value will be
	 * modified by when the up/down (or right/left) arrows
	 * are pressed to the argument, which must be at least
	 * one.
	 *
	 * @param value the new increment (must be greater than zero)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setIncrement(int averageCharacterWidth) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	/**
	 * Sets the amount that the receiver's value will be
	 * modified by when the page increment/decrement areas
	 * are selected to the argument, which must be at least
	 * one.
	 *
	 * @param value the page increment (must be greater than zero)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setPageIncrement(int width) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

}
