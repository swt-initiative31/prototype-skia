package org.eclipse.swt.widgets;



import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.uno.*;


/**
 * Instances of this class represent the "windows"
 * which the desktop or "window manager" is managing.
 * Instances that do not have a parent (that is, they
 * are built using the constructor, which takes a
 * <code>Display</code> as the argument) are described
 * as <em>top level</em> shells. Instances that do have
 * a parent are described as <em>secondary</em> or
 * <em>dialog</em> shells.
 * <p>
 * Instances are always displayed in one of the maximized,
 * minimized or normal states:</p>
 * <ul>
 * <li>
 * When an instance is marked as <em>maximized</em>, the
 * window manager will typically resize it to fill the
 * entire visible area of the display, and the instance
 * is usually put in a state where it can not be resized
 * (even if it has style <code>RESIZE</code>) until it is
 * no longer maximized.
 * </li><li>
 * When an instance is in the <em>normal</em> state (neither
 * maximized or minimized), its appearance is controlled by
 * the style constants which were specified when it was created
 * and the restrictions of the window manager (see below).
 * </li><li>
 * When an instance has been marked as <em>minimized</em>,
 * its contents (client area) will usually not be visible,
 * and depending on the window manager, it may be
 * "iconified" (that is, replaced on the desktop by a small
 * simplified representation of itself), relocated to a
 * distinguished area of the screen, or hidden. Combinations
 * of these changes are also possible.
 * </li>
 * </ul>
 * <p>
 * The <em>modality</em> of an instance may be specified using
 * style bits. The modality style bits are used to determine
 * whether input is blocked for other shells on the display.
 * The <code>PRIMARY_MODAL</code> style allows an instance to block
 * input to its parent. The <code>APPLICATION_MODAL</code> style
 * allows an instance to block input to every other shell in the
 * display. The <code>SYSTEM_MODAL</code> style allows an instance
 * to block input to all shells, including shells belonging to
 * different applications.
 * </p><p>
 * Note: The styles supported by this class are treated
 * as <em>HINT</em>s, since the window manager for the
 * desktop on which the instance is visible has ultimate
 * control over the appearance and behavior of decorations
 * and modality. For example, some window managers only
 * support resizable windows and will always assume the
 * RESIZE style, even if it is not set. In addition, if a
 * modality style is not supported, it is "upgraded" to a
 * more restrictive modality style that is supported. For
 * example, if <code>PRIMARY_MODAL</code> is not supported,
 * it would be upgraded to <code>APPLICATION_MODAL</code>.
 * A modality style may also be "downgraded" to a less
 * restrictive style. For example, most operating systems
 * no longer support <code>SYSTEM_MODAL</code> because
 * it can freeze up the desktop, so this is typically
 * downgraded to <code>APPLICATION_MODAL</code>.</p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER, CLOSE, MIN, MAX, NO_MOVE, NO_TRIM, RESIZE, TITLE, ON_TOP, TOOL, SHEET</dd>
 * <dd>APPLICATION_MODAL, MODELESS, PRIMARY_MODAL, SYSTEM_MODAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Activate, Close, Deactivate, Deiconify, Iconify</dd>
 * </dl>
 * <p>
 * Class <code>SWT</code> provides two "convenience constants"
 * for the most commonly required style combinations:</p>
 * <dl>
 * <dt><code>SHELL_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required
 * to produce a typical application top level shell: (that
 * is, <code>CLOSE | TITLE | MIN | MAX | RESIZE</code>)
 * </dd>
 * <dt><code>DIALOG_TRIM</code></dt>
 * <dd>
 * the result of combining the constants which are required
 * to produce a typical application dialog shell: (that
 * is, <code>TITLE | CLOSE | BORDER</code>)
 * </dd>
 * </dl>
 * <p>
 * Note: Only one of the styles APPLICATION_MODAL, MODELESS,
 * PRIMARY_MODAL and SYSTEM_MODAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see Decorations
 * @see SWT
 * @see <a href="http://www.eclipse.org/swt/snippets/#shell">Shell snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Shell extends Decorations {

	UnoWindow window = null;
	boolean opened, moved, resized, fullScreen, center, deferFlushing, scrolling, isPopup;


	/**
	 * Constructs a new instance of this class. This is equivalent
	 * to calling <code>Shell((Display) null)</code>.
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 */
	public Shell () {
		this ((Display) null);
	}

	/**
	 * Constructs a new instance of this class given only the style
	 * value describing its behavior and appearance. This is equivalent
	 * to calling <code>Shell((Display) null, style)</code>.
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
	 * @param style the style of control to construct
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 *
	 * @see SWT#BORDER
	 * @see SWT#CLOSE
	 * @see SWT#MIN
	 * @see SWT#MAX
	 * @see SWT#RESIZE
	 * @see SWT#TITLE
	 * @see SWT#TOOL
	 * @see SWT#NO_TRIM
	 * @see SWT#NO_MOVE
	 * @see SWT#SHELL_TRIM
	 * @see SWT#DIALOG_TRIM
	 * @see SWT#ON_TOP
	 * @see SWT#MODELESS
	 * @see SWT#PRIMARY_MODAL
	 * @see SWT#APPLICATION_MODAL
	 * @see SWT#SYSTEM_MODAL
	 * @see SWT#SHEET
	 */
	public Shell (int style) {
		this ((Display) null, style);
	}

	/**
	 * Constructs a new instance of this class given only the display
	 * to create it on. It is created with style <code>SWT.SHELL_TRIM</code>.
	 * <p>
	 * Note: Currently, null can be passed in for the display argument.
	 * This has the effect of creating the shell on the currently active
	 * display if there is one. If there is no current display, the
	 * shell is created on a "default" display. <b>Passing in null as
	 * the display argument is not considered to be good coding style,
	 * and may not be supported in a future release of SWT.</b>
	 * </p>
	 *
	 * @param display the display to create the shell on
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 */
	public Shell (Display display) {
		this (display, SWT.SHELL_TRIM);
	}

	/**
	 * Constructs a new instance of this class given the display
	 * to create it on and a style value describing its behavior
	 * and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in
	 * class <code>SWT</code> which is applicable to instances of this
	 * class, or must be built by <em>bitwise OR</em>'ing together
	 * (that is, using the <code>int</code> "|" operator) two or more
	 * of those <code>SWT</code> style constants. The class description
	 * lists the style constants that are applicable to the class.
	 * Style bits are also inherited from superclasses.
	 * </p><p>
	 * Note: Currently, null can be passed in for the display argument.
	 * This has the effect of creating the shell on the currently active
	 * display if there is one. If there is no current display, the
	 * shell is created on a "default" display. <b>Passing in null as
	 * the display argument is not considered to be good coding style,
	 * and may not be supported in a future release of SWT.</b>
	 * </p>
	 *
	 * @param display the display to create the shell on
	 * @param style the style of control to construct
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
	 * </ul>
	 *
	 * @see SWT#BORDER
	 * @see SWT#CLOSE
	 * @see SWT#MIN
	 * @see SWT#MAX
	 * @see SWT#RESIZE
	 * @see SWT#TITLE
	 * @see SWT#TOOL
	 * @see SWT#NO_TRIM
	 * @see SWT#NO_MOVE
	 * @see SWT#SHELL_TRIM
	 * @see SWT#DIALOG_TRIM
	 * @see SWT#ON_TOP
	 * @see SWT#MODELESS
	 * @see SWT#PRIMARY_MODAL
	 * @see SWT#APPLICATION_MODAL
	 * @see SWT#SYSTEM_MODAL
	 * @see SWT#SHEET
	 */
	public Shell (Display display, int style) {
		this (display, null, style, 0, false);
	}



	Shell (Display display, Shell parent, int style, long handle, boolean embedded) {
		super ();
		checkSubclass ();
		if (display == null) display = Display.getCurrent ();
		if (display == null) display = Display.getDefault ();
		if (!display.isValidThread ()) {
			error (SWT.ERROR_THREAD_INVALID_ACCESS);
		}
		if (parent != null && parent.isDisposed ()) {
			error (SWT.ERROR_INVALID_ARGUMENT);
		}
		if (!Display.getSheetEnabled ()) {
			this.center = parent != null && (style & SWT.SHEET) != 0;
		}
		this.style = checkStyle (style);
		this.parent = parent;
		this.display = display;
		if (handle != 0) {
			if (embedded) {
//				view = new NSView(handle);
			} else {
				state |= FOREIGN_HANDLE;
			}
		}
		reskinWidget();
		createWidget ();
	}

	static int checkStyle (Shell parent, int style) {
		style = Decorations.checkStyle (style);
		style &= ~SWT.TRANSPARENT;
		int mask = SWT.SYSTEM_MODAL | SWT.APPLICATION_MODAL | SWT.PRIMARY_MODAL;
		if ((style & SWT.SHEET) != 0) {
			if (Display.getSheetEnabled ()) {
				style &= ~(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.MAX);
				if (parent == null) {
					style &= ~SWT.SHEET;
					style |= SWT.SHELL_TRIM;
				}
			} else {
				style &= ~SWT.SHEET;
				style |= parent == null ? SWT.SHELL_TRIM : SWT.DIALOG_TRIM;
			}
			if ((style & mask) == 0) {
				style |= parent == null ? SWT.APPLICATION_MODAL : SWT.PRIMARY_MODAL;
			}
		}
		int bits = style & ~mask;
		if ((style & SWT.SYSTEM_MODAL) != 0) return bits | SWT.SYSTEM_MODAL;
		if ((style & SWT.APPLICATION_MODAL) != 0) return bits | SWT.APPLICATION_MODAL;
		if ((style & SWT.PRIMARY_MODAL) != 0) return bits | SWT.PRIMARY_MODAL;
		return bits;
	}



	public void setText(String text) {
//		TODO Implement setTitle. Is not supported by UNO's XWindow
//		window.setTitle("Swing PoC: " + text);
	}

	public void open() {
		window.setVisible(true);
	}

	@Override
	public boolean isDisposed() {
		return window.isDisposed();
	}

	@Override
	public Shell getShell () {
		checkWidget();
		return this;
	}

	@Override
	void createHandle () {
		state |= HIDDEN;
		if (window == null && handle == null) {
			window = new UnoWindow();
			handle = window;
//			int styleMask = OS.NSBorderlessWindowMask;
//			if ((style & (SWT.TOOL | SWT.SHEET)) != 0) {
//				window = (NSWindow) new SWTWindow().alloc ();
//				if ((style & SWT.SHEET) != 0) {
//					styleMask |= OS.NSDocModalWindowMask;
//				} else {
//					styleMask |= OS.NSUtilityWindowMask | OS.NSNonactivatingPanelMask;
//				}
//			} else {
//				window = (NSWindow) new SWTWindow().alloc ();
//			}
//			if ((style & SWT.NO_TRIM) == 0) {
//				if ((style & SWT.TITLE) != 0) styleMask |= OS.NSTitledWindowMask;
//				if ((style & SWT.CLOSE) != 0) styleMask |= OS.NSClosableWindowMask;
//				if ((style & SWT.MIN) != 0) styleMask |= OS.NSMiniaturizableWindowMask;
//				if ((style & SWT.MAX) != 0) styleMask |= OS.NSResizableWindowMask;
//				if ((style & SWT.RESIZE) != 0) styleMask |= OS.NSResizableWindowMask;
//			}
//			NSScreen screen = null;
//			NSScreen primaryScreen = new NSScreen(NSScreen.screens().objectAtIndex(0));
//			if (parent != null) screen = parentWindow ().screen();
//			if (screen == null) screen = primaryScreen;
//			window = window.initWithContentRect(new NSRect(), styleMask, OS.NSBackingStoreBuffered, (style & SWT.ON_TOP) != 0, screen);			frame.in
//			if ((style & (SWT.NO_TRIM | SWT.BORDER | SWT.SHELL_TRIM)) == 0 || (style & (SWT.TOOL | SWT.SHEET)) != 0) {
//				window.setHasShadow (true);  // NOT SUPPORTED BY SWING
//			}
//			if ((style & SWT.NO_MOVE) != 0) {
//				window.setMovable(false);    // NOT SUPPORTED BY SWING
//			}
//			if ((style & SWT.TOOL) != 0) {
//				// Feature in Cocoa: NSPanels that use NSUtilityWindowMask are always promoted to the floating window layer.
//				// Fix is to call setFloatingPanel:NO, which turns off this behavior.
//				((NSPanel)window).setFloatingPanel(false);
//				// By default, panels hide on deactivation.
//				((NSPanel)window).setHidesOnDeactivate(false);
//				// Normally a panel doesn't become key unless something inside it needs to be first responder.
//				// TOOL shells always become key, so disable that behavior.
//				((NSPanel)window).setBecomesKeyOnlyIfNeeded(false);
//			}
//			if ((style & SWT.NO_TRIM) == 0) {
//				NSSize size = window.minSize();
//				size.width = NSWindow.minFrameWidthWithTitle(NSString.string(), styleMask);
//				window.setMinSize(size);
//			}
//			if (fixResize ()) {
//				if (window.respondsToSelector(OS.sel_setMovable_)) {
//					OS.objc_msgSend(window.id, OS.sel_setMovable_, 0);
//				}
//			}
//			display.cascadeWindow(window, screen);
			Point screenFrame = UnoLoader.GetScreenSize();
			int width = screenFrame.x * 5 / 8;
			int height = screenFrame.y * 5 / 8;
			com.sun.star.awt.Rectangle frame = window.getFrame();
			frame.Y = screenFrame.y - ((screenFrame.y - (frame.Y + frame.Height)) + height);
			frame.Width = width;
			frame.Height = height;
			window.setFrame(frame);
//			if ((style & SWT.ON_TOP) != 0) {
//				window.setLevel(OS.NSStatusWindowLevel);
//			}
			super.createHandle ();
			window.setVisible(false);
		} else {
			state &= ~HIDDEN;

			if (window != null) {
				// In the FOREIGN_HANDLE case, 'window' is an NSWindow created on our behalf.
				// It may already have a content view, so if it does, grab and retain, since we release()
				// the view at disposal time.  Otherwise, create a new 'view' that will be used as the window's
				// content view in setZOrder.
				handle = window.getContentPane();

				if (handle == null) {
					super.createHandle();
//				} else {
//					TODO ? view.retain();
				}
			} else {
				// In the embedded case, 'view' is already set to the NSView we should add the window's content view to as a subview.
				// In that case we will hold on to the foreign view, create our own SWTCanvasView (which overwrites 'view') and then
				// add it to the foreign view.
				super.createHandle();
				// TODO view.add(topView());
			}

			style |= SWT.NO_BACKGROUND;
		}

//		windowDelegate = (SWTWindowDelegate)new SWTWindowDelegate().alloc().init();
//
//		if (window == null) {
//			NSWindow hostWindow = view.window();
//			attachObserversToWindow(hostWindow);
//		} else {
//			int behavior;
//			if (parent != null) {
//				behavior = OS.NSWindowCollectionBehaviorMoveToActiveSpace;
//			} else if ((style & SWT.TOOL) != 0) {
//				behavior = OS.NSWindowCollectionBehaviorFullScreenAuxiliary;
//			} else {
//				behavior = OS.NSWindowCollectionBehaviorFullScreenPrimary;
//			}
//			window.setCollectionBehavior(behavior);
//			window.setAcceptsMouseMovedEvents(true);
//			window.setDelegate(windowDelegate);
//		}
//
//		NSWindow fieldEditorWindow = window;
//		if (fieldEditorWindow == null) fieldEditorWindow = view.window();
//		id id = fieldEditorWindow.fieldEditor (true, null);
//		if (id != null) {
//			OS.object_setClass (id.id, OS.objc_getClass ("SWTEditorView"));
//			new NSTextView(id).setAllowsUndo(true);
//		}

	}


}
