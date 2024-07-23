package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.uno.*;

public abstract class Control extends Widget implements Drawable {

	Composite parent;
	int drawCount, backgroundAlpha = 255;
	double[] foreground, background;

	Object layoutData;

	Font font;
	private Cursor cursor;
	private Menu menu;
	private String toolTipText;
	private Accessible accessible;

	Control() {
	}

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#BORDER
	 * @see SWT#LEFT_TO_RIGHT
	 * @see SWT#RIGHT_TO_LEFT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Control(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
		createWidget();
	}

	/**
	 * Returns the receiver's shell. For all controls other than shells, this simply
	 * returns the control's nearest ancestor shell. Shells return themselves, even
	 * if they are children of other shells.
	 *
	 * @return the receiver's shell
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getParent
	 */
	public Shell getShell() {
		checkWidget();
		return parent.getShell();
	}

	/**
	 * Returns the receiver's monitor.
	 *
	 * @return the receiver's monitor
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.0
	 */
	public Monitor getMonitor() {
		checkWidget();
		// TODO (VISJEE) return the correct one, not just the primary one.
		return display.getPrimaryMonitor();
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Composite</code> or null
	 * when the receiver is a shell that was created with null or a display for a
	 * parent.
	 *
	 * @return the receiver's parent
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Composite getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Sets the font that the receiver will use to paint textual information to the
	 * font specified by the argument, or to the default font for that kind of
	 * control if the argument is null.
	 *
	 * @param font the new font (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setFont(Font font) {
		checkWidget();
		if (font != null) {
			if (font.isDisposed())
				error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.font = font;
		setFont(font != null ? font.handle : defaultFont().handle);
	}

	void setFont(java.awt.Font font) {
//      TODO
//		if (view instanceof java.awt.Component) {
//			((java.awt.Component) view).setFont(font);
//		}
	}

	@Override
	void createWidget() {
		state |= DRAG_DETECT;
		checkOrientation(parent);
		super.createWidget();
		checkBackground();
		checkBuffered();
		setDefaultFont();
		setRelations();
		if ((state & PARENT_BACKGROUND) != 0) {
			setBackground();
		}
	}

	void checkBackground() {
		Shell shell = getShell();
		if (this == shell)
			return;
		state &= ~PARENT_BACKGROUND;
		Composite composite = parent;
		do {
			int mode = composite.backgroundMode;
			if (mode != 0 || backgroundAlpha == 0) {
				if (mode == SWT.INHERIT_DEFAULT || backgroundAlpha == 0) {
					Control control = this;
					do {
						if ((control.state & THEME_BACKGROUND) == 0) {
							return;
						}
						control = control.parent;
					} while (control != composite);
				}
				state |= PARENT_BACKGROUND;
				return;
			}
			if (composite == shell)
				break;
			composite = composite.parent;
		} while (true);
	}

	void checkBuffered() {
		style |= SWT.DOUBLE_BUFFERED;
	}

	void setDefaultFont() {
		if (display.smallFonts) {
			setFont(defaultFont().handle);
			setSmallSize();
		}
	}

	Font defaultFont() {
		if (display.smallFonts)
			return display.getSystemFont();
		return Font.swing_new(display, defaultNSFont());
	}

	void setSmallSize() {
		// TODO
//		if (view instanceof java.awt.Component) {
//			NSCell cell = ((java.awt.Component)view).cell();
//			if (cell != null) cell.setControlSize (OS.NSSmallControlSize);
//		}
	}

	java.awt.Font defaultNSFont() {
		return display.getSystemFont().handle;
	}

	UnoControl topView() {
		return getHandle();
	}

	UnoControl contentView() {
		return getHandle();
	}

	void setRelations() {
		if (parent == null)
			return;
		Control[] children = parent._getChildren();
		int count = children.length;
		if (count > 1) {
			/*
			 * the receiver is the last item in the list, so its predecessor will be the
			 * second-last item in the list
			 */
			Control child = children[count - 2];
			if (child != this) {
				child.addRelation(this);
			}
		}
	}

	void addRelation(Control control) {
	}

	void setBackground() {
		if (!drawsBackground())
			return;
		Control control = findBackgroundControl();
		if (control == null)
			control = this;
//		if (control.backgroundImage != null) {
//			setBackgroundImage (control.backgroundImage.handle);
//		} else {
		double[] color = control.background != null ? control.background : control.defaultBackground().handle;
		UnoColor unoColor = new UnoColor(color[0], color[1], color[2], color[3]);
		setBackgroundColor(unoColor);
//		}
	}

	boolean drawsBackground() {
		return true;
	}

	Control findBackgroundControl() {
		if ((// backgroundImage != null ||
		background != null) && backgroundAlpha > 0)
			return this;
		return (parent != null && !isTransparent() && (state & PARENT_BACKGROUND) != 0) ? parent.findBackgroundControl()
				: null;
	}

	Color defaultBackground() {
		return display.getWidgetColor(SWT.COLOR_WIDGET_BACKGROUND);
	}

	void setBackgroundColor(UnoColor UnoColor) {
	}

	boolean isTransparent() {
		if (background != null)
			return false;
		return parent.isTransparent();
	}

	void markLayout(boolean changed, boolean all) {
		/* Do nothing */
	}

	void updateLayout(boolean all) {
		/* Do nothing */
	}

	/**
	 * Returns the preferred size (in points) of the receiver.
	 * <p>
	 * The <em>preferred size</em> of a control is the size that it would best be
	 * displayed at. The width hint and height hint arguments allow the caller to
	 * ask a control questions such as "Given a particular width, how high does the
	 * control need to be to show all of the contents?" To indicate that the caller
	 * does not wish to constrain a particular dimension, the constant
	 * <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p>
	 *
	 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
	 * @return the preferred size of the control
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see Layout
	 * @see #getBorderWidth
	 * @see #getBounds
	 * @see #getSize
	 * @see #pack(boolean)
	 * @see "computeTrim, getClientArea for controls that implement them"
	 */
	public Point computeSize(int wHint, int hHint) {
		return computeSize(wHint, hHint, true);
	}

	/**
	 * Returns the preferred size (in points) of the receiver.
	 * <p>
	 * The <em>preferred size</em> of a control is the size that it would best be
	 * displayed at. The width hint and height hint arguments allow the caller to
	 * ask a control questions such as "Given a particular width, how high does the
	 * control need to be to show all of the contents?" To indicate that the caller
	 * does not wish to constrain a particular dimension, the constant
	 * <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p>
	 * <p>
	 * If the changed flag is <code>true</code>, it indicates that the receiver's
	 * <em>contents</em> have changed, therefore any caches that a layout manager
	 * containing the control may have been keeping need to be flushed. When the
	 * control is resized, the changed flag will be <code>false</code>, so layout
	 * manager caches can be retained.
	 * </p>
	 *
	 * @param wHint   the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint   the height hint (can be <code>SWT.DEFAULT</code>)
	 * @param changed <code>true</code> if the control's contents have changed, and
	 *                <code>false</code> otherwise
	 * @return the preferred size of the control.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see Layout
	 * @see #getBorderWidth
	 * @see #getBounds
	 * @see #getSize
	 * @see #pack(boolean)
	 * @see "computeTrim, getClientArea for controls that implement them"
	 */
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		int border = getBorderWidth();
		width += border * 2;
		height += border * 2;
		return new Point(width, height);
	}

	/**
	 * Returns the receiver's border width in points.
	 *
	 * @return the border width
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getBorderWidth() {
		checkWidget();
		return 0;
	}

	/**
	 * Sets the receiver's size and location in points to the rectangular area
	 * specified by the arguments. The <code>x</code> and <code>y</code> arguments
	 * are relative to the receiver's parent (or its display if its parent is null),
	 * unless the receiver is a shell. In this case, the <code>x</code> and
	 * <code>y</code> arguments are relative to the display.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause that value to be set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the receiver to a
	 * number higher or equal 2^14 will cause them to be set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param x      the new x coordinate for the receiver
	 * @param y      the new y coordinate for the receiver
	 * @param width  the new width for the receiver
	 * @param height the new height for the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setBounds(int x, int y, int width, int height) {
		checkWidget();
		setBounds(x, y, Math.max(0, width), Math.max(0, height), true, true);
	}

	void setBounds(int x, int y, int width, int height, boolean move, boolean resize) {
		/*
		 * Bug in Cocoa. On Mac 10.8, a text control loses and gains focus when its
		 * bounds changes. The fix is to ignore these events.
		 */
		Display display = this.display;
		Control oldIgnoreFocusControl = display.ignoreFocusControl;
		display.ignoreFocusControl = this;
		if (move && resize) {
			getHandle().setBounds(new Rectangle(x, y, width, height));
		} else if (move) {
			getHandle().setLocation(x, y);
		} else if (resize) {
			getHandle().setSize(width, height);
		}
		display.ignoreFocusControl = oldIgnoreFocusControl;
	}

	/**
	 * Returns a point describing the receiver's size in points. The x coordinate of
	 * the result is the width of the receiver. The y coordinate of the result is
	 * the height of the receiver.
	 *
	 * @return the receiver's size
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Point getSize() {
		checkWidget();
		Rectangle bounds = getHandle().getBounds();
		return new Point(bounds.width, bounds.height);
	}

	/**
	 * Sets the receiver's size to the point specified by the arguments.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause that value to be set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the receiver to a
	 * number higher or equal 2^14 will cause them to be set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param width  the new width in points for the receiver
	 * @param height the new height in points for the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setSize(int width, int height) {
		checkWidget();
		setBounds(0, 0, Math.max(0, width), Math.max(0, height), false, true);
	}

	/**
	 * Sets the receiver's size to the point specified by the argument.
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause them to be set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the receiver to a
	 * number higher or equal 2^14 will cause them to be set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param size the new size in points for the receiver
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the point is
	 *                                     null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setSize(Point size) {
		checkWidget();
		if (size == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		setBounds(0, 0, Math.max(0, size.x), Math.max(0, size.y), false, true);
	}

	/**
	 * Sets the receiver's size and location in points to the rectangular area
	 * specified by the argument. The <code>x</code> and <code>y</code> fields of
	 * the rectangle are relative to the receiver's parent (or its display if its
	 * parent is null).
	 * <p>
	 * Note: Attempting to set the width or height of the receiver to a negative
	 * number will cause that value to be set to zero instead.
	 * </p>
	 * <p>
	 * Note: On GTK, attempting to set the width or height of the receiver to a
	 * number higher or equal 2^14 will cause them to be set to (2^14)-1 instead.
	 * </p>
	 *
	 * @param rect the new bounds for the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setBounds(Rectangle rect) {
		checkWidget();
		if (rect == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		setBounds(rect.x, rect.y, Math.max(0, rect.width), Math.max(0, rect.height), true, true);
	}

	/**
	 * Returns layout data which is associated with the receiver.
	 *
	 * @return the receiver's layout data
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Object getLayoutData() {
		checkWidget();
		return layoutData;
	}

	/**
	 * Sets the layout data associated with the receiver to the argument.
	 *
	 * @param layoutData the new layout data for the receiver.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setLayoutData(Object layoutData) {
		checkWidget();
		this.layoutData = layoutData;
	}

	boolean hasBorder() {
		return (style & SWT.BORDER) != 0;
	}

	/*
	 * Answers a boolean indicating whether a Label that precedes the receiver in a
	 * layout should be read by screen readers as the recevier's label.
	 */
	boolean isDescribedByLabel() {
		return true;
	}

	Point computeSizeInPixels(int wHint, int hHint, boolean changed) {
		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		int border = getBorderWidthInPixels();
		width += border * 2;
		height += border * 2;
		return new Point(width, height);
	}

	int getBorderWidthInPixels() {

		// TODO: OS implementation...

		return 0;
	}

	public void setEnabled(boolean enabled) {
		checkWidget();
// TODO: UNO implementation
	}

	public void pack() {
		// TODO Auto-generated method stub

	}

	public void addControlListener(ControlListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Resize, typedListener);
		addListener(SWT.Move, typedListener);
	}

	/**
	 * Sets the receiver's location to the point specified by the arguments which
	 * are relative to the receiver's parent (or its display if its parent is null),
	 * unless the receiver is a shell. In this case, the point is relative to the
	 * display.
	 *
	 * @param location the new location for the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setLocation(Point location) {
		setLocation(location.x, location.y);
	}

	/**
	 * Sets the receiver's location to the point specified by the arguments which
	 * are relative to the receiver's parent (or its display if its parent is null),
	 * unless the receiver is a shell. In this case, the point is relative to the
	 * display.
	 *
	 * @param x the new x coordinate for the receiver
	 * @param y the new y coordinate for the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setLocation(int x, int y) {
		checkWidget();
		setBounds(x, y, 0, 0, true, false);
	}

	public void addPaintListener(PaintListener listener) {
		addTypedListener(listener, SWT.Paint);
	}

	public Color getBackground() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public Color getForeground() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	public void setBackground(Color c) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	public void setForeground(Color c) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * This method should be overriden by subclasses and the return type should be
	 * changed to the proper subclass of <code>UnoControl</code>
	 *
	 * @return the <code>handle</code>
	 */
	protected abstract UnoControl getHandle();

	public void setVisible(boolean b) {
		getHandle().setVisible(b);
	}

	/**
	 * Returns the receiver's cursor, or null if it has not been set.
	 * <p>
	 * When the mouse pointer passes over a control its appearance is changed to
	 * match the control's cursor.
	 * </p>
	 *
	 * @return the receiver's cursor or <code>null</code>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.3
	 */
	public Cursor getCursor() {
		checkWidget();
		return cursor;
	}

	/**
	 * Sets the receiver's cursor to the cursor specified by the argument, or to the
	 * default cursor for that kind of control if the argument is null.
	 * <p>
	 * When the mouse pointer passes over a control its appearance is changed to
	 * match the control's cursor.
	 * </p>
	 *
	 * @param cursor the new cursor (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setCursor(Cursor cursor) {
		checkWidget();
		if (cursor != null && cursor.isDisposed())
			error(SWT.ERROR_INVALID_ARGUMENT);
		this.cursor = cursor;
//		long hwndCursor = OS.GetCapture ();
//		if (hwndCursor == 0) {
//			POINT pt = new POINT ();
//			if (!OS.GetCursorPos (pt)) return;
//			long hwnd = hwndCursor = OS.WindowFromPoint (pt);
//			while (hwnd != 0 && hwnd != handle) {
//				hwnd = OS.GetParent (hwnd);
//			}
//			if (hwnd == 0) return;
//		}
//		Control control = display.getControl (hwndCursor);
//		if (control == null) control = this;
//		control.setCursor ();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Returns a rectangle describing the receiver's size and location in points
	 * relative to its parent (or its display if its parent is null), unless the
	 * receiver is a shell. In this case, the location is relative to the display.
	 *
	 * @return the receiver's bounding rectangle
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Rectangle getBounds() {
		checkWidget();
		return getHandle().getBounds();
	}

	/**
	 * Returns a point describing the receiver's location relative to its parent in
	 * points (or its display if its parent is null), unless the receiver is a
	 * shell. In this case, the point is relative to the display.
	 *
	 * @return the receiver's location
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Point getLocation() {
		checkWidget();
		return getHandle().getLocation();
	}

	/**
	 * Causes the rectangular area of the receiver specified by the arguments to be
	 * marked as needing to be redrawn. The next time a paint request is processed,
	 * that area of the receiver will be painted, including the background. If the
	 * <code>all</code> flag is <code>true</code>, any children of the receiver
	 * which intersect with the specified area will also paint their intersecting
	 * areas. If the <code>all</code> flag is <code>false</code>, the children will
	 * not be painted.
	 * <p>
	 * Schedules a paint request if the invalidated area is visible or becomes
	 * visible later. It is not necessary for the caller to explicitly call
	 * {@link #update()} after calling this method, but depending on the platform,
	 * the automatic repaints may be delayed considerably.
	 * </p>
	 *
	 * @param x      the x coordinate of the area to draw
	 * @param y      the y coordinate of the area to draw
	 * @param width  the width of the area to draw
	 * @param height the height of the area to draw
	 * @param all    <code>true</code> if children should redraw, and
	 *               <code>false</code> otherwise
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #update()
	 * @see PaintListener
	 * @see SWT#Paint
	 * @see SWT#NO_BACKGROUND
	 * @see SWT#NO_REDRAW_RESIZE
	 * @see SWT#NO_MERGE_PAINTS
	 * @see SWT#DOUBLE_BUFFERED
	 */
	public void redraw(int x, int y, int width, int height, boolean all) {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		getHandle().redraw(x, y, width, height, all);
	}

	/**
	 * Causes the entire bounds of the receiver to be marked as needing to be
	 * redrawn. The next time a paint request is processed, the control will be
	 * completely painted, including the background.
	 * <p>
	 * Schedules a paint request if the invalidated area is visible or becomes
	 * visible later. It is not necessary for the caller to explicitly call
	 * {@link #update()} after calling this method, but depending on the platform,
	 * the automatic repaints may be delayed considerably.
	 * </p>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #update()
	 * @see PaintListener
	 * @see SWT#Paint
	 * @see SWT#NO_BACKGROUND
	 * @see SWT#NO_REDRAW_RESIZE
	 * @see SWT#NO_MERGE_PAINTS
	 * @see SWT#DOUBLE_BUFFERED
	 */
	public void redraw() {
		checkWidget();
		getHandle().redraw();
	}

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Control</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all platforms,
	 * and should never be called from application code.
	 * </p>
	 *
	 * @param hDC  the platform specific GC handle
	 * @param data the platform specific GC data
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public void internal_dispose_GC(long hDC, GCData data) {
		checkWidget();

		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Control</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all platforms,
	 * and should never be called from application code.
	 * </p>
	 *
	 * @param data the platform specific GC data
	 * @return the platform specific GC handle
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public long internal_new_GC(GCData data) {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return 0;
	}

	/**
	 * Forces all outstanding paint requests for the widget to be processed before
	 * this method returns. If there are no outstanding paint request, this method
	 * does nothing.
	 * <p>
	 * Note:
	 * </p>
	 * <ul>
	 * <li>This method does not cause a redraw.</li>
	 * <li>Some OS versions forcefully perform automatic deferred painting. This
	 * method does nothing in that case.</li>
	 * </ul>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #redraw()
	 * @see #redraw(int, int, int, int, boolean)
	 * @see PaintListener
	 * @see SWT#Paint
	 */
	public void update() {
		checkWidget();
		update(false);
	}

	void update(boolean all) {
//		checkWidget ();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Returns <code>true</code> if the receiver is visible and all ancestors up to
	 * and including the receiver's nearest ancestor shell are visible. Otherwise,
	 * <code>false</code> is returned.
	 *
	 * @return the receiver's visibility state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getVisible
	 */
	public boolean isVisible() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return getVisible() && parent.isVisible();
	}

	/**
	 * Returns <code>true</code> if the receiver is visible, and <code>false</code>
	 * otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, this method may still indicate that it is
	 * considered visible even though it may not actually be showing.
	 * </p>
	 *
	 * @return the receiver's visibility state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public boolean getVisible() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return true;
	}

	/**
	 * Returns the receiver's pop up menu if it has one, or null if it does not. All
	 * controls may optionally have a pop up menu that is displayed when the user
	 * requests one for the control. The sequence of key strokes, button presses
	 * and/or button releases that are used to request a pop up menu is platform
	 * specific.
	 *
	 * @return the receiver's menu
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	@Override
	public Menu getMenu() {
		checkWidget();
		return menu;
	}

	/**
	 * Returns the receiver's tool tip text, or null if it has not been set.
	 *
	 * @return the receiver's tool tip text
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public String getToolTipText() {
		checkWidget();
		return toolTipText;
	}

	/**
	 * Returns the accessible object for the receiver.
	 * <p>
	 * If this is the first time this object is requested, then the object is
	 * created and returned. The object returned by getAccessible() does not need to
	 * be disposed.
	 * </p>
	 *
	 * @return the accessible object
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see Accessible#addAccessibleListener
	 * @see Accessible#addAccessibleControlListener
	 *
	 * @since 2.0
	 */
	public Accessible getAccessible() {
		checkWidget();
		if (accessible == null)
			accessible = new_Accessible(this);
		return accessible;
	}

	Accessible new_Accessible(Control control) {
		return Accessible.internal_new_Accessible(this);
	}

	/**
	 * Returns a point which is the result of converting the argument, which is
	 * specified in coordinates relative to the receiver, to display relative
	 * coordinates.
	 * <p>
	 * NOTE: To properly map a rectangle or a corner of a rectangle on a
	 * right-to-left platform, use {@link Display#map(Control, Control, Rectangle)}.
	 * </p>
	 *
	 * @param x the x coordinate to be translated
	 * @param y the y coordinate to be translated
	 * @return the translated coordinates
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 2.1
	 */
	public Point toDisplay(int x, int y) {
		checkWidget();
		return DPIUtil.autoScaleDown(toDisplayInPixels(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y)));
	}

	Point toDisplayInPixels(int x, int y) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	/**
	 * Returns a point which is the result of converting the argument, which is
	 * specified in display relative coordinates, to coordinates relative to the
	 * receiver.
	 * <p>
	 * NOTE: To properly map a rectangle or a corner of a rectangle on a
	 * right-to-left platform, use {@link Display#map(Control, Control, Rectangle)}.
	 * </p>
	 *
	 * @param x the x coordinate in points to be translated
	 * @param y the y coordinate in points to be translated
	 * @return the translated coordinates
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 2.1
	 */
	public Point toControl(int x, int y) {
		checkWidget();
		return DPIUtil.autoScaleDown(toControlInPixels(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y)));
	}

	Point toControlInPixels(int x, int y) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	/**
	 * Returns <code>true</code> if the receiver has the user-interface focus, and
	 * <code>false</code> otherwise.
	 *
	 * @return the receiver's focus state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public boolean isFocusControl() {
		checkWidget();
		Control focusControl = display.focusControl;
		if (focusControl != null && !focusControl.isDisposed()) {
			return this == focusControl;
		}
		return hasFocus();
	}

	boolean isFocusAncestor(Control control) {
		while (control != null && control != this && !(control instanceof Shell)) {
			control = control.parent;
		}
		return control == this;
	}

	boolean hasFocus() {
		/*
		 * If a non-SWT child of the control has focus, then this control is considered
		 * to have focus even though it does not have focus in Windows.
		 */
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	/**
	 * <<<<<<< HEAD Causes the receiver to have the <em>keyboard focus</em>, such
	 * that all keyboard events will be delivered to it. Focus reassignment will
	 * respect applicable platform constraints.
	 *
	 * @return <code>true</code> if the control got focus, and <code>false</code> if
	 *         it was unable to. ======= Returns a point which is the result of
	 *         converting the argument, which is specified in display relative
	 *         coordinates, to coordinates relative to the receiver.
	 *         <p>
	 *         NOTE: To properly map a rectangle or a corner of a rectangle on a
	 *         right-to-left platform, use
	 *         {@link Display#map(Control, Control, Rectangle)}.
	 *         </p>
	 *
	 * @param point the point to be translated (must not be null)
	 * @return the translated coordinates
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the point is
	 *                                     null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public Point toControl(Point point) {
		checkWidget();
		if (point == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		point = DPIUtil.autoScaleUp(point);
		return DPIUtil.autoScaleDown(toControlInPixels(point.x, point.y));
	}

	/**
	 * Causes the receiver to have the <em>keyboard focus</em>, such that all
	 * keyboard events will be delivered to it. Focus reassignment will respect
	 * applicable platform constraints.
	 *
	 * @return <code>true</code> if the control got focus, and <code>false</code> if
	 *         it was unable to.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #forceFocus
	 */
	public boolean setFocus() {
		checkWidget();
		if ((style & SWT.NO_FOCUS) != 0)
			return false;
		return forceFocus();
	}

	/**
	 * Forces the receiver to have the <em>keyboard focus</em>, causing all keyboard
	 * events to be delivered to it.
	 *
	 * @return <code>true</code> if the control got focus, and <code>false</code> if
	 *         it was unable to.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #setFocus
	 */
	public boolean forceFocus() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return isFocusControl();
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled and all ancestors up to
	 * and including the receiver's nearest ancestor shell are enabled. Otherwise,
	 * <code>false</code> is returned. A disabled control is typically not
	 * selectable from the user interface and draws with an inactive or "grayed"
	 * look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #getEnabled
	 */
	public boolean isEnabled() {
		checkWidget();
		return getEnabled() && parent.isEnabled();
	}

	/**
	 * Returns <code>true</code> if the receiver is enabled, and <code>false</code>
	 * otherwise. A disabled control is typically not selectable from the user
	 * interface and draws with an inactive or "grayed" look.
	 *
	 * @return the receiver's enabled state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #isEnabled
	 */
	public boolean getEnabled() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return true;
	}

	/**
	 * Sets the receiver's pop up menu to the argument. All controls may optionally
	 * have a pop up menu that is displayed when the user requests one for the
	 * control. The sequence of key strokes, button presses and/or button releases
	 * that are used to request a pop up menu is platform specific.
	 * <p>
	 * Note: Disposing of a control that has a pop up menu will dispose of the menu.
	 * To avoid this behavior, set the menu to null before the control is disposed.
	 * </p>
	 *
	 * @param menu the new pop up menu
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_MENU_NOT_POP_UP - the menu is
	 *                                     not a pop up menu</li>
	 *                                     <li>ERROR_INVALID_PARENT - if the menu is
	 *                                     not in the same widget tree</li>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the menu
	 *                                     has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setMenu(Menu menu) {
		checkWidget();
		if (menu != null) {
			if (menu.isDisposed())
				error(SWT.ERROR_INVALID_ARGUMENT);
			if ((menu.style & SWT.POP_UP) == 0) {
				error(SWT.ERROR_MENU_NOT_POP_UP);
			}
			if (menu.parent != menuShell()) {
				error(SWT.ERROR_INVALID_PARENT);
			}
		}
		this.menu = menu;
	}

	Decorations menuShell() {
		return parent.menuShell();
	}

	/**
	 * Sets the receiver's tool tip text to the argument, which may be null
	 * indicating that the default tool tip for the control will be shown. For a
	 * control that has a default tool tip, such as the Tree control on Windows,
	 * setting the tool tip text to an empty string replaces the default, causing no
	 * tool tip text to be shown.
	 * <p>
	 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip. To
	 * display a single '&amp;' in the tool tip, the character '&amp;' can be
	 * escaped by doubling it in the string.
	 * </p>
	 * <p>
	 * NOTE: This operation is a hint and behavior is platform specific, on Windows
	 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip
	 * text are not shown in tooltip.
	 * </p>
	 *
	 * @param string the new tool tip text (or null)
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setToolTipText(String string) {
		checkWidget();
		if (!Objects.equals(string, toolTipText)) {
			toolTipText = string;
			setToolTipText(getShell(), string);
		}
	}

	void setToolTipText(Shell shell, String string) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
//		shell.setToolTipText (handle, string);
	}

	boolean traverse(Event event) {
		/*
		 * It is possible (but unlikely), that application code could have disposed the
		 * widget in the traverse event. If this happens, return true to stop further
		 * event processing.
		 */
		sendEvent(SWT.Traverse, event);
		if (isDisposed())
			return true;
		if (!event.doit)
			return false;
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
//		switch (event.detail) {

//			case SWT.TRAVERSE_NONE:			return true;
//			case SWT.TRAVERSE_ESCAPE:			return traverseEscape ();
//			case SWT.TRAVERSE_RETURN:			return traverseReturn ();
//			case SWT.TRAVERSE_TAB_NEXT:		return traverseGroup (true);
//			case SWT.TRAVERSE_TAB_PREVIOUS:	return traverseGroup (false);
//			case SWT.TRAVERSE_ARROW_NEXT:		return traverseItem (true);
//			case SWT.TRAVERSE_ARROW_PREVIOUS:	return traverseItem (false);
//			case SWT.TRAVERSE_MNEMONIC:		return traverseMnemonic (event.character);
//			case SWT.TRAVERSE_PAGE_NEXT:		return traversePage (true);
//			case SWT.TRAVERSE_PAGE_PREVIOUS:	return traversePage (false);
//		}
		return false;
	}

	/**
	 * Based on the argument, perform one of the expected platform traversal action.
	 * The argument should be one of the constants:
	 * <code>SWT.TRAVERSE_ESCAPE</code>, <code>SWT.TRAVERSE_RETURN</code>,
	 * <code>SWT.TRAVERSE_TAB_NEXT</code>, <code>SWT.TRAVERSE_TAB_PREVIOUS</code>,
	 * <code>SWT.TRAVERSE_ARROW_NEXT</code>,
	 * <code>SWT.TRAVERSE_ARROW_PREVIOUS</code>, <code>SWT.TRAVERSE_PAGE_NEXT</code>
	 * and <code>SWT.TRAVERSE_PAGE_PREVIOUS</code>.
	 *
	 * @param traversal the type of traversal
	 * @return true if the traversal succeeded
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public boolean traverse(int traversal) {
		checkWidget();
		Event event = new Event();
		event.doit = true;
		event.detail = traversal;
		return traverse(event);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * traversal events occur, by sending it one of the messages defined in the
	 * <code>TraverseListener</code> interface.
	 *
	 * @param listener the listener which should be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see TraverseListener
	 * @see #removeTraverseListener
	 */
	public void addTraverseListener(TraverseListener listener) {
		addTypedListener(listener, SWT.Traverse);
	}

	/**
	 * Returns the font that the receiver will use to paint textual information.
	 *
	 * @return the receiver's font
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Font getFont() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	/**
	 * Returns the text direction of the receiver, which will be one of the
	 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
	 *
	 * @return the text direction style
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.102
	 */
	public int getTextDirection() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return SWT.LEFT_TO_RIGHT;
	}

	/**
	 * If the argument is <code>false</code>, causes subsequent drawing operations
	 * in the receiver to be ignored. No drawing of any kind can occur in the
	 * receiver until the flag is set to true. Graphics operations that occurred
	 * while the flag was <code>false</code> are lost. When the flag is set to
	 * <code>true</code>, the entire widget is marked as needing to be redrawn.
	 * Nested calls to this method are stacked.
	 * <p>
	 * Note: This operation is a hint and may not be supported on some platforms or
	 * for some widgets.
	 * </p>
	 *
	 * @param redraw the new redraw state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #redraw(int, int, int, int, boolean)
	 * @see #update()
	 */
	public void setRedraw(boolean redraw) {
		checkWidget();
		/*
		 * Feature in Windows. When WM_SETREDRAW is used to turn off drawing in a
		 * widget, it clears the WS_VISIBLE bits and then sets them when redraw is
		 * turned back on. This means that WM_SETREDRAW will make a widget unexpectedly
		 * visible. The fix is to track the visibility state while drawing is turned off
		 * and restore it when drawing is turned back on.
		 */
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Returns <code>true</code> if the receiver is detecting drag gestures, and
	 * <code>false</code> otherwise.
	 *
	 * @return the receiver's drag detect state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.3
	 */
	public boolean getDragDetect() {
		checkWidget();
		return (state & DRAG_DETECT) != 0;
	}

	/**
	 * Returns the orientation of the receiver, which will be one of the constants
	 * <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
	 *
	 * @return the orientation style
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.7
	 */
	public int getOrientation() {
		checkWidget();
		return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	}

	/**
	 * Detects a drag and drop gesture. This method is used to detect a drag gesture
	 * when called from within a mouse down listener.
	 *
	 * <p>
	 * By default, a drag is detected when the gesture occurs anywhere within the
	 * client area of a control. Some controls, such as tables and trees, override
	 * this behavior. In addition to the operating system specific drag gesture,
	 * they require the mouse to be inside an item. Custom widget writers can use
	 * <code>setDragDetect</code> to disable the default detection, listen for mouse
	 * down, and then call <code>dragDetect()</code> from within the listener to
	 * conditionally detect a drag.
	 * </p>
	 *
	 * @param event the mouse down event
	 *
	 * @return <code>true</code> if the gesture occurred, and <code>false</code>
	 *         otherwise.
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT if the event is
	 *                                     null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see DragDetectListener
	 * @see #addDragDetectListener
	 *
	 * @see #getDragDetect
	 * @see #setDragDetect
	 *
	 * @since 3.3
	 */
	public boolean dragDetect(Event event) {
		checkWidget();
		if (event == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		Point loc = event.getLocationInPixels();
		return dragDetect(event.button, event.count, event.stateMask, loc.x, loc.y);
	}

	/**
	 * Detects a drag and drop gesture. This method is used to detect a drag gesture
	 * when called from within a mouse down listener.
	 *
	 * <p>
	 * By default, a drag is detected when the gesture occurs anywhere within the
	 * client area of a control. Some controls, such as tables and trees, override
	 * this behavior. In addition to the operating system specific drag gesture,
	 * they require the mouse to be inside an item. Custom widget writers can use
	 * <code>setDragDetect</code> to disable the default detection, listen for mouse
	 * down, and then call <code>dragDetect()</code> from within the listener to
	 * conditionally detect a drag.
	 * </p>
	 *
	 * @param event the mouse down event
	 *
	 * @return <code>true</code> if the gesture occurred, and <code>false</code>
	 *         otherwise.
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT if the event is
	 *                                     null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see DragDetectListener
	 * @see #addDragDetectListener
	 *
	 * @see #getDragDetect
	 * @see #setDragDetect
	 *
	 * @since 3.3
	 */
	public boolean dragDetect(MouseEvent event) {
		checkWidget();
		if (event == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		return dragDetect(event.button, event.count, event.stateMask, DPIUtil.autoScaleUp(event.x),
				DPIUtil.autoScaleUp(event.y)); // To Pixels
	}

	boolean dragDetect(int button, int count, int stateMask, int x, int y) {
		if (button != 1 || count != 1)
			return false;
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;
	}

	/**
	 * Sets the receiver's drag detect state. If the argument is <code>true</code>,
	 * the receiver will detect drag gestures, otherwise these gestures will be
	 * ignored.
	 *
	 * @param dragDetect the new drag detect state
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.3
	 */
	public void setDragDetect(boolean dragDetect) {
		checkWidget();
		if (dragDetect) {
			state |= DRAG_DETECT;
		} else {
			state &= ~DRAG_DETECT;
		}
		enableDrag(dragDetect);
	}

	void enableDrag(boolean enabled) {
		/* Do nothing */
	}

	public boolean setRadioFocus(boolean tabbing) {
		return false;
	}

	public void setOrientation(int orientation) {
		checkWidget();
		int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
		if ((orientation & flags) == 0 || (orientation & flags) == flags)
			return;
		style &= ~SWT.MIRRORED;
		style &= ~flags;
		style |= orientation & flags;
		style &= ~SWT.FLIP_TEXT_DIRECTION;
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Sets the base text direction (a.k.a. "paragraph direction") of the receiver,
	 * which must be one of the constants <code>SWT.LEFT_TO_RIGHT</code>,
	 * <code>SWT.RIGHT_TO_LEFT</code>, or <code>SWT.AUTO_TEXT_DIRECTION</code>.
	 * <p>
	 * <code>setOrientation</code> would override this value with the text direction
	 * that is consistent with the new orientation.
	 * </p>
	 * <p>
	 * <b>Warning</b>: This API is currently only implemented on Windows. It doesn't
	 * set the base text direction on GTK and Cocoa.
	 * </p>
	 *
	 * @param textDirection the base text direction style
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see SWT#LEFT_TO_RIGHT
	 * @see SWT#RIGHT_TO_LEFT
	 * @see SWT#AUTO_TEXT_DIRECTION
	 * @see SWT#FLIP_TEXT_DIRECTION
	 *
	 * @since 3.102
	 */
	public void setTextDirection(int textDirection) {
		checkWidget();
		textDirection &= (SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT);
		updateTextDirection(textDirection);
		if (textDirection == AUTO_TEXT_DIRECTION) {
			state |= HAS_AUTO_DIRECTION;
		} else {
			state &= ~HAS_AUTO_DIRECTION;
		}
	}

	boolean updateTextDirection(int textDirection) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return true;
	}

}
