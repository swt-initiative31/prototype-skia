package org.eclipse.swt.widgets;

import javax.swing.*;

import org.eclipse.swt.*;
import org.eclipse.swt.uno.*;

public abstract class Composite extends Scrollable {
	Layout layout;
	int layoutCount, backgroundMode;
	UnoWindow window ;


	public Composite(Composite parent, int style) {

		super(parent, style);

		window = new UnoWindow( parent.getHandle() , style );



	}

	Composite () {
	}

	@Override
	protected void checkSubclass () {
		/* Do nothing - Subclassing is allowed */
	}

	/**
	 * Returns a (possibly empty) array containing the receiver's children.
	 * Children are returned in the order that they are drawn.  The topmost
	 * control appears at the beginning of the array.  Subsequent controls
	 * draw beneath this control and appear later in the array.
	 * <p>
	 * Note: This is not the actual structure used by the receiver
	 * to maintain its list of children, so modifying the array will
	 * not affect the receiver.
	 * </p>
	 *
	 * @return an array of children
	 *
//	 * @see Control#moveAbove
//	 * @see Control#moveBelow
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public Control [] getChildren () {
		checkWidget();
		return _getChildren ();
	}

	Control [] _getChildren () {
		return new Control [0];
//      TODO
//		UnoWindow containerView = contentView();
//		if (containerView == null) return new Control [0];
//		Component[] component = containerView.getComponents();
//		int count = (int)component.length;
//		Control [] children = new Control [count];
//		if (count == 0) return children;
//		int j = 0;
//		for (int i=0; i<count; i++){
//			Widget widget = display.getWidget (component[count - i - 1]);
//			if (widget != null && widget != this && widget instanceof Control) {
//				children [j++] = (Control) widget;
//			}
//		}
//		if (j == count) return children;
//		Control [] newChildren = new Control [j];
//		System.arraycopy (children, 0, newChildren, 0, j);
//		return newChildren;
	}

	/**
	 * Sets the layout which is associated with the receiver to be
	 * the argument which may be null.
	 *
	 * @param layout the receiver's new layout or null
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setLayout (Layout layout) {
		checkWidget ();
		this.layout = layout;
	}

	/**
	 * If the argument is <code>true</code>, causes subsequent layout
	 * operations in the receiver or any of its children to be ignored.
	 * No layout of any kind can occur in the receiver or any of its
	 * children until the flag is set to false.
	 * Layout operations that occurred while the flag was
	 * <code>true</code> are remembered and when the flag is set to
	 * <code>false</code>, the layout operations are performed in an
	 * optimized manner.  Nested calls to this method are stacked.
	 *
	 * @param defer the new defer state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #layout(boolean)
	 * @see #layout(Control[])
	 *
	 * @since 3.1
	 */
	public void setLayoutDeferred (boolean defer) {
		checkWidget();
		if (!defer) {
			if (--layoutCount == 0) {
				if ((state & LAYOUT_CHILD) != 0 || (state & LAYOUT_NEEDED) != 0) {
					updateLayout (true);
				}
			}
		} else {
			layoutCount++;
		}
	}

	@Override
	void updateLayout (boolean all) {
		Composite parent = findDeferredControl ();
		if (parent != null) {
			parent.state |= LAYOUT_CHILD;
			return;
		}
		if ((state & LAYOUT_NEEDED) != 0) {
			boolean changed = (state & LAYOUT_CHANGED) != 0;
			state &= ~(LAYOUT_NEEDED | LAYOUT_CHANGED);
			display.runSkin ();
			layout.layout (this, changed);
		}
		if (all) {
			state &= ~LAYOUT_CHILD;
			Control [] children = _getChildren ();
			for (int i=0; i<children.length; i++) {
				children [i].updateLayout (all);
			}
		}
	}

	Composite findDeferredControl () {
		return layoutCount > 0 ? this : parent.findDeferredControl ();
	}

	/**
	 * Returns <code>true</code> if the receiver has deferred
	 * the performing of layout, and <code>false</code> otherwise.
	 *
	 * @return the receiver's deferred layout state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #setLayoutDeferred(boolean)
	 * @see #isLayoutDeferred()
	 *
	 * @since 3.1
	 */
	public boolean getLayoutDeferred () {
		checkWidget ();
		return layoutCount > 0 ;
	}

	/**
	 * Returns <code>true</code> if the receiver or any ancestor
	 * up to and including the receiver's nearest ancestor shell
	 * has deferred the performing of layouts.  Otherwise, <code>false</code>
	 * is returned.
	 *
	 * @return the receiver's deferred layout state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see #setLayoutDeferred(boolean)
	 * @see #getLayoutDeferred()
	 *
	 * @since 3.1
	 */
	public boolean isLayoutDeferred () {
		checkWidget ();
		return findDeferredControl () != null;
	}

	/**
	 * If the receiver has a layout, asks the layout to <em>lay out</em>
	 * (that is, set the size and location of) the receiver's children.
	 * If the receiver does not have a layout, do nothing.
	 * <p>
	 * Use of this method is discouraged since it is the least-efficient
	 * way to trigger a layout. The use of <code>layout(true)</code>
	 * discards all cached layout information, even from controls which
	 * have not changed. It is much more efficient to invoke
	 * {@link Control#requestLayout()} on every control which has changed
	 * in the layout than it is to invoke this method on the layout itself.
	 * </p>
	 * <p>
	 * This is equivalent to calling <code>layout(true)</code>.
	 * </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is
	 * moved or resized such that an area in the parent is
	 * exposed, then the parent will paint. If no child is
	 * affected, the parent will not paint.
	 * </p>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void layout () {
		checkWidget ();
		layout (true);
	}

	/**
	 * If the receiver has a layout, asks the layout to <em>lay out</em>
	 * (that is, set the size and location of) the receiver's children.
	 * If the argument is <code>true</code> the layout must not rely
	 * on any information it has cached about the immediate children. If it
	 * is <code>false</code> the layout may (potentially) optimize the
	 * work it is doing by assuming that none of the receiver's
	 * children has changed state since the last layout.
	 * If the receiver does not have a layout, do nothing.
	 * <p>
	 * It is normally more efficient to invoke {@link Control#requestLayout()}
	 * on every control which has changed in the layout than it is to invoke
	 * this method on the layout itself. Clients are encouraged to use
	 * {@link Control#requestLayout()} where possible instead of calling
	 * this method.
	 * </p>
	 * <p>
	 * If a child is resized as a result of a call to layout, the
	 * resize event will invoke the layout of the child.  The layout
	 * will cascade down through all child widgets in the receiver's widget
	 * tree until a child is encountered that does not resize.  Note that
	 * a layout due to a resize will not flush any cached information
	 * (same as <code>layout(false)</code>).
	 * </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is
	 * moved or resized such that an area in the parent is
	 * exposed, then the parent will paint. If no child is
	 * affected, the parent will not paint.
	 * </p>
	 *
	 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void layout (boolean changed) {
		checkWidget ();
		if (layout == null) return;
		layout (changed, false);
	}

	/**
	 * If the receiver has a layout, asks the layout to <em>lay out</em>
	 * (that is, set the size and location of) the receiver's children.
	 * If the changed argument is <code>true</code> the layout must not rely
	 * on any information it has cached about its children. If it
	 * is <code>false</code> the layout may (potentially) optimize the
	 * work it is doing by assuming that none of the receiver's
	 * children has changed state since the last layout.
	 * If the all argument is <code>true</code> the layout will cascade down
	 * through all child widgets in the receiver's widget tree, regardless of
	 * whether the child has changed size.  The changed argument is applied to
	 * all layouts.  If the all argument is <code>false</code>, the layout will
	 * <em>not</em> cascade down through all child widgets in the receiver's widget
	 * tree.  However, if a child is resized as a result of a call to layout, the
	 * resize event will invoke the layout of the child.  Note that
	 * a layout due to a resize will not flush any cached information
	 * (same as <code>layout(false)</code>).
	 * <p>
	 * It is normally more efficient to invoke {@link Control#requestLayout()}
	 * on every control which has changed in the layout than it is to invoke
	 * this method on the layout itself. Clients are encouraged to use
	 * {@link Control#requestLayout()} where possible instead of calling
	 * this method.
	 * </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is
	 * moved or resized such that an area in the parent is
	 * exposed, then the parent will paint. If no child is
	 * affected, the parent will not paint.
	 * </p>
	 *
	 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
	 * @param all <code>true</code> if all children in the receiver's widget tree should be laid out, and <code>false</code> otherwise
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.1
	 */
	public void layout (boolean changed, boolean all) {
		checkWidget ();
		if (layout == null && !all) return;
		markLayout (changed, all);
		updateLayout (all);
	}

	/**
	 * Forces a lay out (that is, sets the size and location) of all widgets that
	 * are in the parent hierarchy of the changed control up to and including the
	 * receiver.  The layouts in the hierarchy must not rely on any information
	 * cached about the changed control or any of its ancestors.  The layout may
	 * (potentially) optimize the work it is doing by assuming that none of the
	 * peers of the changed control have changed state since the last layout.
	 * If an ancestor does not have a layout, skip it.
	 * <p>
	 * It is normally more efficient to invoke {@link Control#requestLayout()}
	 * on every control which has changed in the layout than it is to invoke
	 * this method on the layout itself. Clients are encouraged to use
	 * {@link Control#requestLayout()} where possible instead of calling
	 * this method.
	 * </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is
	 * moved or resized such that an area in the parent is
	 * exposed, then the parent will paint. If no child is
	 * affected, the parent will not paint.
	 * </p>
	 *
	 * @param changed a control that has had a state change which requires a recalculation of its size
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the changed array is null any of its controls are null or have been disposed</li>
	 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.1
	 */
	public void layout (Control [] changed) {
		checkWidget ();
		if (changed == null) error (SWT.ERROR_INVALID_ARGUMENT);
		layout (changed, SWT.NONE);
	}

	/**
	 * Forces a lay out (that is, sets the size and location) of all widgets that
	 * are in the parent hierarchy of the changed control up to and including the
	 * receiver.
	 * <p>
	 * The parameter <code>flags</code> may be a combination of:
	 * </p>
	 * <dl>
	 * <dt><b>SWT.ALL</b></dt>
	 * <dd>all children in the receiver's widget tree should be laid out</dd>
	 * <dt><b>SWT.CHANGED</b></dt>
	 * <dd>the layout must flush its caches</dd>
	 * <dt><b>SWT.DEFER</b></dt>
	 * <dd>layout will be deferred</dd>
	 * </dl>
	 * <p>
	 * When the <code>changed</code> array is specified, the flags <code>SWT.ALL</code>
	 * and <code>SWT.CHANGED</code> have no effect. In this case, the layouts in the
	 * hierarchy must not rely on any information cached about the changed control or
	 * any of its ancestors.  The layout may (potentially) optimize the
	 * work it is doing by assuming that none of the peers of the changed
	 * control have changed state since the last layout.
	 * If an ancestor does not have a layout, skip it.
	 * </p>
	 * <p>
	 * When the <code>changed</code> array is not specified, the flag <code>SWT.ALL</code>
	 * indicates that the whole widget tree should be laid out. And the flag
	 * <code>SWT.CHANGED</code> indicates that the layouts should flush any cached
	 * information for all controls that are laid out.
	 * </p>
	 * <p>
	 * The <code>SWT.DEFER</code> flag always causes the layout to be deferred by
	 * calling <code>Composite.setLayoutDeferred(true)</code> and scheduling a call
	 * to <code>Composite.setLayoutDeferred(false)</code>, which will happen when
	 * appropriate (usually before the next event is handled). When this flag is set,
	 * the application should not call <code>Composite.setLayoutDeferred(boolean)</code>.
	 * </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is
	 * moved or resized such that an area in the parent is
	 * exposed, then the parent will paint. If no child is
	 * affected, the parent will not paint.
	 * </p>
	 *
	 * @param changed a control that has had a state change which requires a recalculation of its size
	 * @param flags the flags specifying how the layout should happen
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if any of the controls in changed is null or has been disposed</li>
	 *    <li>ERROR_INVALID_PARENT - if any control in changed is not in the widget tree of the receiver</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.6
	 */
	public void layout (Control [] changed, int flags) {
		checkWidget ();
		if (changed != null) {
			for (Control control : changed) {
				if (control == null) error (SWT.ERROR_INVALID_ARGUMENT);
				if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
				boolean ancestor = false;
				Composite composite = control.parent;
				while (composite != null) {
					ancestor = composite == this;
					if (ancestor) break;
					composite = composite.parent;
				}
				if (!ancestor) error (SWT.ERROR_INVALID_PARENT);
			}
			int updateCount = 0;
			Composite [] update = new Composite [16];
			for (Control element : changed) {
				Control child = element;
				Composite composite = child.parent;
				// Update layout when the list of children has changed.
				// See bug 497812.
				child.markLayout(false, false);
				while (child != this) {
					if (composite.layout != null) {
						composite.state |= LAYOUT_NEEDED;
						if (!composite.layout.flushCache (child)) {
							composite.state |= LAYOUT_CHANGED;
						}
					}
					if (updateCount == update.length) {
						Composite [] newUpdate = new Composite [update.length + 16];
						System.arraycopy (update, 0, newUpdate, 0, update.length);
						update = newUpdate;
					}
					child = update [updateCount++] = composite;
					composite = child.parent;
				}
			}
			if (!display.externalEventLoop && (flags & SWT.DEFER) != 0) {
				setLayoutDeferred (true);
				display.addLayoutDeferred (this);
			}
			for (int i=updateCount-1; i>=0; i--) {
				update [i].updateLayout (false);
			}
		} else {
			if (layout == null && (flags & SWT.ALL) == 0) return;
			markLayout ((flags & SWT.CHANGED) != 0, (flags & SWT.ALL) != 0);
			if (!display.externalEventLoop && (flags & SWT.DEFER) != 0) {
				setLayoutDeferred (true);
				display.addLayoutDeferred (this);
			}
			updateLayout ((flags & SWT.ALL) != 0);
		}
	}

	@Override
	void markLayout (boolean changed, boolean all) {
		if (layout != null) {
			state |= LAYOUT_NEEDED;
			if (changed) state |= LAYOUT_CHANGED;
		}
		if (all) {
			for (Control element : _getChildren ()) {
				element.markLayout (changed, all);
			}
		}
	}

	@Override
	void createHandle () {
		state |= CANVAS;
		boolean scrolled = (style & (SWT.V_SCROLL | SWT.H_SCROLL)) != 0;
		if (!scrolled)  state |= THEME_BACKGROUND;
		java.awt.Rectangle rect = new java.awt.Rectangle();
//		TODO
//		if (scrolled || hasBorder ()) {
//			NSScrollView scrollWidget = (NSScrollView)new SWTScrollView().alloc();
//			scrollWidget.initWithFrame (rect);
//			scrollWidget.setDrawsBackground(false);
//			if ((style & SWT.H_SCROLL) != 0) scrollWidget.setHasHorizontalScroller(true);
//			if ((style & SWT.V_SCROLL) != 0) scrollWidget.setHasVerticalScroller(true);
//			scrollWidget.setBorderType(hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder);
//			scrollView = scrollWidget;
//		}
		JPanel widget = new JPanel();
		widget.setBounds(rect);
//		widget.setFocusRingType(OS.NSFocusRingTypeExterior);
//      TODO  view = widget.get;
//		if (scrollView != null) {
//			NSClipView contentView = scrollView.contentView();
//			contentView.setAutoresizesSubviews(true);
//			view.setAutoresizingMask(OS.NSViewWidthSizable | OS.NSViewHeightSizable);
//		}
	}
	/**
	 * If the receiver has a layout, asks the layout to <em>lay out</em>
	 * (that is, set the size and location of) the receiver's children.
	 * If the argument is <code>true</code> the layout must not rely
	 * on any information it has cached about the immediate children. If it
	 * is <code>false</code> the layout may (potentially) optimize the
	 * work it is doing by assuming that none of the receiver's
	 * children has changed state since the last layout.
	 * If the receiver does not have a layout, do nothing.
	 * <p>
	 * It is normally more efficient to invoke {@link Control#requestLayout()}
	 * on every control which has changed in the layout than it is to invoke
	 * this method on the layout itself. Clients are encouraged to use
	 * {@link Control#requestLayout()} where possible instead of calling
	 * this method.
	 * </p>
	 * <p>
	 * If a child is resized as a result of a call to layout, the
	 * resize event will invoke the layout of the child.  The layout
	 * will cascade down through all child widgets in the receiver's widget
	 * tree until a child is encountered that does not resize.  Note that
	 * a layout due to a resize will not flush any cached information
	 * (same as <code>layout(false)</code>).
	 * </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is
	 * moved or resized such that an area in the parent is
	 * exposed, then the parent will paint. If no child is
	 * affected, the parent will not paint.
	 * </p>
	 *
	 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void layout (boolean changed) {
		checkWidget ();
		if (layout == null) return;
		layout (changed, false);
	}

	/**
	 * If the receiver has a layout, asks the layout to <em>lay out</em>
	 * (that is, set the size and location of) the receiver's children.
	 * If the changed argument is <code>true</code> the layout must not rely
	 * on any information it has cached about its children. If it
	 * is <code>false</code> the layout may (potentially) optimize the
	 * work it is doing by assuming that none of the receiver's
	 * children has changed state since the last layout.
	 * If the all argument is <code>true</code> the layout will cascade down
	 * through all child widgets in the receiver's widget tree, regardless of
	 * whether the child has changed size.  The changed argument is applied to
	 * all layouts.  If the all argument is <code>false</code>, the layout will
	 * <em>not</em> cascade down through all child widgets in the receiver's widget
	 * tree.  However, if a child is resized as a result of a call to layout, the
	 * resize event will invoke the layout of the child.  Note that
	 * a layout due to a resize will not flush any cached information
	 * (same as <code>layout(false)</code>).
	 * <p>
	 * It is normally more efficient to invoke {@link Control#requestLayout()}
	 * on every control which has changed in the layout than it is to invoke
	 * this method on the layout itself. Clients are encouraged to use
	 * {@link Control#requestLayout()} where possible instead of calling
	 * this method.
	 * </p>
	 * <p>
	 * Note: Layout is different from painting. If a child is
	 * moved or resized such that an area in the parent is
	 * exposed, then the parent will paint. If no child is
	 * affected, the parent will not paint.
	 * </p>
	 *
	 * @param changed <code>true</code> if the layout must flush its caches, and <code>false</code> otherwise
	 * @param all <code>true</code> if all children in the receiver's widget tree should be laid out, and <code>false</code> otherwise
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @since 3.1
	 */
	public void layout (boolean changed, boolean all) {
		checkWidget ();
		if (layout == null && !all) return;
		markLayout (changed, all);
		updateLayout (all);
	}

	@Override
	void markLayout (boolean changed, boolean all) {
		if (layout != null) {
			state |= LAYOUT_NEEDED;
			if (changed) state |= LAYOUT_CHANGED;
		}
		if (all) {
			for (Control element : _getChildren ()) {
				element.markLayout (changed, all);
			}
		}
	}
}
