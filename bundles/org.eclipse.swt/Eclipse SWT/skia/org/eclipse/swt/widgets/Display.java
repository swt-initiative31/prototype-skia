package org.eclipse.swt.widgets;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.uno.*;

/**
 * Instances of this class are responsible for managing the connection between
 * SWT and the underlying operating system. Their most important function is to
 * implement the SWT event loop in terms of the platform event model. They also
 * provide various methods for accessing information about the operating system,
 * and have overall control over the operating system resources which SWT
 * allocates.
 * <p>
 * Applications which are built with SWT will <em>almost always</em> require
 * only a single display. In particular, some platforms which SWT supports will
 * not allow more than one <em>active</em> display. In other words, some
 * platforms do not support creating a new display if one already exists that
 * has not been sent the <code>dispose()</code> message.
 * <p>
 * In SWT, the thread which creates a <code>Display</code> instance is
 * distinguished as the <em>user-interface thread</em> for that display.
 * </p>
 * The user-interface thread for a particular display has the following special
 * attributes:
 * <ul>
 * <li>The event loop for that display must be run from the thread.</li>
 * <li>Some SWT API methods (notably, most of the public methods in
 * <code>Widget</code> and its subclasses), may only be called from the thread.
 * (To support multi-threaded user-interface applications, class
 * <code>Display</code> provides inter-thread communication methods which allow
 * threads other than the user-interface thread to request that it perform
 * operations on their behalf.)</li>
 * <li>The thread is not allowed to construct other <code>Display</code>s until
 * that display has been disposed. (Note that, this is in addition to the
 * restriction mentioned above concerning platform support for multiple
 * displays. Thus, the only way to have multiple simultaneously active displays,
 * even on platforms which support it, is to have multiple threads.)</li>
 * </ul>
 * <p>
 * Enforcing these attributes allows SWT to be implemented directly on the
 * underlying operating system's event model. This has numerous benefits
 * including smaller footprint, better use of resources, safer memory
 * management, clearer program logic, better performance, and fewer overall
 * operating system threads required. The down side however, is that care must
 * be taken (only) when constructing multi-threaded applications to use the
 * inter-thread communication mechanisms which this class provides when
 * required.
 * </p>
 * <p>
 * All SWT API methods which may only be called from the user-interface thread
 * are distinguished in their documentation by indicating that they throw the
 * "<code>ERROR_THREAD_INVALID_ACCESS</code>" SWT exception.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Close, Dispose, OpenDocument, Settings, Skin</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see #syncExec
 * @see #asyncExec
 * @see #wake
 * @see #readAndDispatch
 * @see #sleep
 * @see Device#dispose
 * @see <a href="http://www.eclipse.org/swt/snippets/#display">Display
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Display extends Device implements Executor {

	/* Windows and Events */
	Event[] eventQueue;
	EventTable eventTable, filterTable;
	boolean disposing;
	int sendEventCount;

	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.widgets.";

	/* Sync/Async Widget Communication */
	Synchronizer synchronizer;
	Consumer<RuntimeException> runtimeExceptionHandler = DefaultExceptionHandler.RUNTIME_EXCEPTION_HANDLER;
	Consumer<Error> errorHandler = DefaultExceptionHandler.RUNTIME_ERROR_HANDLER;
	Thread thread;
	boolean allowTimers = true, runAsyncMessages = true;

	/* Skinning support */
	static final int GROW_SIZE = 1024;
	Widget[] skinList = new Widget[GROW_SIZE];
	int skinCount;

	/* Timers */
	long[] timerIds;

	/* System Tray */
	Tray tray;
	TrayItem currentTrayItem;
	Menu trayItemMenu;

	/* Multiple Displays. */
	static Display Default;
	static Display[] Displays = new Display[1];

	/* Fonts */
	boolean smallFonts;

	/* System Colors */
	double[][] colors;
	int[] alternateSelectedControlTextColor, selectedControlTextColor;
	private int[] alternateSelectedControlColor, secondarySelectedControlColor;

	/* Timer */
	Runnable timerList[];
//	NSTimer nsTimers [];

	/* Deferred Layout list */
	Composite[] layoutDeferred;
	int layoutDeferredCount;

	Control currentControl, trackingControl, tooltipControl, ignoreFocusControl;
	Widget tooltipTarget;
	Control focusControl;

	static Map<UnoControl, Widget> widgetMap;
	int loopCount;

	GCData[] contexts;
	private Image errorIcon;
	private Image infoIcon;
	private Image questionIcon;
	private Image warningIcon;
	private Monitor[] monitors;
	private Monitor primaryMonitor;
	private Shell activeShell;

	Cursor [] cursors = new Cursor [SWT.CURSOR_HAND + 1];

	// TODO (visjee) doesn't do anything (yet)
	boolean externalEventLoop; // events are dispatched outside SWT, e.g. TrackPopupMenu or DoDragDrop

	/*
	* TEMPORARY CODE.  Install the runnable that
	* gets the current display. This code will
	* be removed in the future.
	*/
	static {
		DeviceFinder = () -> {
			Device device = getCurrent ();
			if (device == null) {
				device = getDefault ();
			}
			setDevice (device);
		};
	}

	/*
	* TEMPORARY CODE... yeah, right
	*/
	static void setDevice (Device device) {
		CurrentDevice = device;
	}

	/**
	 * Constructs a new instance of this class.
	 * <p>
	 * Note: The resulting display is marked as the <em>current</em> display. If
	 * this is the first display which has been constructed since the application
	 * started, it is also marked as the <em>default</em> display.
	 * </p>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if called from a
	 *                         thread that already created an existing display</li>
	 *                         <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                         allowed subclass</li>
	 *                         </ul>
	 *
	 * @see #getCurrent
	 * @see #getDefault
	 * @see Widget#checkSubclass
	 * @see Shell
	 */
	public Display() {
		this(null);
	}

	/**
	 * Constructs a new instance of this class using the parameter.
	 *
	 * @param data the device data
	 */
	public Display(DeviceData data) {
		super(data);
	}

	/**
	 * Creates the device in the operating system. If the device does not have a
	 * handle, this method may do nothing depending on the device.
	 * <p>
	 * This method is called before <code>init</code>.
	 * </p>
	 *
	 * @param data the DeviceData which describes the receiver
	 *
	 * @see #init
	 */
	@Override
	protected void create(DeviceData data) {
		checkSubclass();
		checkDisplay(thread = Thread.currentThread(), false);
		createDisplay(data);
		register(this);
		synchronizer = new Synchronizer(this);
		if (Default == null)
			Default = this;
	}

	/**
	 * Checks that this class can be subclassed.
	 * <p>
	 * IMPORTANT: See the comment in <code>Widget.checkSubclass()</code>.
	 * </p>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                         allowed subclass</li>
	 *                         </ul>
	 *
	 * @see Widget#checkSubclass
	 */
	protected void checkSubclass() {
		if (!Display.isValidClass(getClass()))
			error(SWT.ERROR_INVALID_SUBCLASS);
	}

	static void checkDisplay(Thread thread, boolean multiple) {
		synchronized (Device.class) {
			for (int i = 0; i < Displays.length; i++) {
				if (Displays[i] != null) {
					if (!multiple)
						SWT.error(SWT.ERROR_NOT_IMPLEMENTED, null, " [multiple displays]");
					if (Displays[i].thread == thread)
						SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
				}
			}
		}
	}

	void createDisplay(DeviceData data) {
		// I guess this is not necessary for Swing, but most probably for UNO
	}

	/**
	 * Executes the given runnable in the user-interface thread of this Display.
	 * <ul>
	 * <li>If the calling thread is the user-interface thread of this display it is
	 * executed immediately and the method returns after the command has run, as
	 * with the method {@link Display#syncExec(Runnable)}.</li>
	 * <li>In all other cases the <code>run()</code> method of the runnable is
	 * asynchronously executed as with the method
	 * {@link Display#asyncExec(Runnable)} at the next reasonable opportunity. The
	 * caller of this method continues to run in parallel, and is not notified when
	 * the runnable has completed.</li>
	 * </ul>
	 * <p>
	 * This can be used in cases where one want to execute some piece of code that
	 * should be guaranteed to run in the user-interface thread regardless of the
	 * current thread.
	 * </p>
	 *
	 * <p>
	 * Note that at the time the runnable is invoked, widgets that have the receiver
	 * as their display may have been disposed. Therefore, it is advised to check
	 * for this case inside the runnable before accessing the widget.
	 * </p>
	 *
	 * @param runnable the runnable to execute in the user-interface thread, never
	 *                 <code>null</code>
	 * @throws RejectedExecutionException if this task cannot be accepted for
	 *                                    execution
	 * @throws NullPointerException       if runnable is null
	 */
	@Override
	public void execute(Runnable runnable) {
		Objects.requireNonNull(runnable);
		if (isDisposed()) {
			throw new RejectedExecutionException(new SWTException(SWT.ERROR_WIDGET_DISPOSED, null));
		}
		if (thread == Thread.currentThread()) {
			syncExec(runnable);
		} else {
			asyncExec(runnable);
		}
	}

	/**
	 * Causes the <code>run()</code> method of the runnable to be invoked by the
	 * user-interface thread at the next reasonable opportunity. The caller of this
	 * method continues to run in parallel, and is not notified when the runnable
	 * has completed. Specifying <code>null</code> as the runnable simply wakes the
	 * user-interface thread when run.
	 * <p>
	 * Note that at the time the runnable is invoked, widgets that have the receiver
	 * as their display may have been disposed. Therefore, it is necessary to check
	 * for this case inside the runnable before accessing the widget.
	 * </p>
	 *
	 * @param runnable code to run on the user-interface thread or <code>null</code>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 *
	 * @see #syncExec
	 */
	public void asyncExec(Runnable runnable) {
		synchronized (Device.class) {
			if (isDisposed())
				error(SWT.ERROR_DEVICE_DISPOSED);
			synchronizer.asyncExec(runnable);
		}
	}

	/**
	 * Causes the <code>run()</code> method of the runnable to be invoked by the
	 * user-interface thread at the next reasonable opportunity. The thread which
	 * calls this method is suspended until the runnable completes. Specifying
	 * <code>null</code> as the runnable simply wakes the user-interface thread.
	 * <p>
	 * Note that at the time the runnable is invoked, widgets that have the receiver
	 * as their display may have been disposed. Therefore, it is necessary to check
	 * for this case inside the runnable before accessing the widget.
	 * </p>
	 *
	 * @param runnable code to run on the user-interface thread or <code>null</code>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_FAILED_EXEC - if an exception occurred when
	 *                         executing the runnable</li>
	 *                         <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 *
	 * @see #asyncExec
	 */
	public void syncExec(Runnable runnable) {
		Synchronizer synchronizer;
		synchronized (Device.class) {
			if (isDisposed())
				error(SWT.ERROR_DEVICE_DISPOSED);
			synchronizer = this.synchronizer;
		}
		synchronizer.syncExec(runnable);
	}

	static void register(Display display) {
		synchronized (Device.class) {
			for (int i = 0; i < Displays.length; i++) {
				if (Displays[i] == null) {
					Displays[i] = display;
					return;
				}
			}
			Display[] newDisplays = new Display[Displays.length + 4];
			System.arraycopy(Displays, 0, newDisplays, 0, Displays.length);
			newDisplays[Displays.length] = display;
			Displays = newDisplays;
		}
	}

	/**
	 * Reads an event from the operating system's event queue, dispatches it
	 * appropriately, and returns <code>true</code> if there is potentially more
	 * work to do, or <code>false</code> if the caller can sleep until another event
	 * is placed on the event queue.
	 * <p>
	 * In addition to checking the system event queue, this method also checks if
	 * any inter-thread messages (created by <code>syncExec()</code> or
	 * <code>asyncExec()</code>) are waiting to be processed, and if so handles them
	 * before returning.
	 * </p>
	 *
	 * @return <code>false</code> if the caller can sleep upon return from this
	 *         method
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_FAILED_EXEC - if an exception occurred
	 *                         while running an inter-thread message</li>
	 *                         </ul>
	 *
	 * @see #sleep // * @see #wake
	 */
	public boolean readAndDispatch() {
		checkDevice();
		// Not needed for Swing, maybe for UNO
		//
		// if (sendEventCount == 0 && loopCount == poolCount - 1 &&
		// Callback.getEntryCount () == 0) removePool ();
		// addPool ();
		runSkin();
		runDeferredLayouts();
		loopCount++;
		boolean events = false;
		try {
			events |= runSettings();
			events |= runTimers();
			events |= runContexts();
			events |= runPopups();
			// TODO
//			NSEvent event = application.nextEventMatchingMask(OS.NSAnyEventMask, null, OS.NSDefaultRunLoopMode, true);
//			if ((event != null) && (application != null)) {
//				events = true;
//				application.sendEvent(event);
//			}
			events |= runPaint();
			events |= runDeferredEvents();
			if (!events) {
				events = isDisposed() || runAsyncMessages(false);
			}
		} finally {
			// Not needed for Swing, maybe for UNO
			//
			// removePool ();
			loopCount--;
			// if (sendEventCount == 0 && loopCount == poolCount && Callback.getEntryCount
			// () == 0) addPool ();
		}
		return events;
	}

	/**
	 * Causes the user-interface thread to <em>sleep</em> (that is, to be put in a
	 * state where it does not consume CPU cycles) until an event is received or it
	 * is otherwise awakened.
	 *
	 * @return <code>true</code> if an event requiring dispatching was placed on the
	 *         queue.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 *
	 *                         // * @see #wake
	 */
	public boolean sleep() {
		// TODO; needs to be checked what shall happen here

		checkDevice();
		if (!synchronizer.isMessagesEmpty())
			return true;
		sendPreExternalEventDispatchEvent();
		try {
			// addPool();
			allowTimers = runAsyncMessages = false;
			// NSRunLoop.currentRunLoop().runMode(OS.NSDefaultRunLoopMode,
			// NSDate.distantFuture());
			allowTimers = runAsyncMessages = true;
		} finally {
			// removePool();
		}
		sendPostExternalEventDispatchEvent();
		return true;
	}

	/**
	 * If the receiver's user-interface thread was <code>sleep</code>ing, causes it
	 * to be awakened and start running again. Note that this method may be called
	 * from any thread.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 *
	 * @see #sleep
	 */
	public void wake() {
		synchronized (Device.class) {
			if (isDisposed())
				error(SWT.ERROR_DEVICE_DISPOSED);
			if (thread == Thread.currentThread())
				return;
			wakeThread();
		}
	}

	boolean runAsyncMessages(boolean all) {
		return synchronizer.runAsyncMessages(all);
	}

	boolean runPaint() {
		return false;
//		if (needsDisplay == null && needsDisplayInRect == null) return false;
//		if (needsDisplay != null) {
//			long count = needsDisplay.count();
//			for (int i = 0; i < count; i++) {
//				OS.objc_msgSend(needsDisplay.objectAtIndex(i).id, OS.sel_setNeedsDisplay_, true);
//			}
//			needsDisplay.release();
//			needsDisplay = null;
//		}
//		if (needsDisplayInRect != null) {
//			long count = needsDisplayInRect.count();
//			for (int i = 0; i < count; i+=2) {
//				NSValue value = new NSValue(needsDisplayInRect.objectAtIndex(i+1));
//				OS.objc_msgSend(needsDisplayInRect.objectAtIndex(i).id, OS.sel_setNeedsDisplayInRect_, value.rectValue());
//			}
//			needsDisplayInRect.release();
//			needsDisplayInRect = null;
//		}
//		return true;
	}

	boolean runContexts() {
//		if (contexts != null) {
//			for (int i = 0; i < contexts.length; i++) {
//				if (contexts[i] != null && contexts[i].flippedContext != null) {
//					contexts[i].flippedContext.flushGraphics();
//				}
//			}
//		}
		return false;
	}

	boolean runPopups() {
		return false;
//		if (popups == null) return false;
//		boolean result = false;
//		while (popups != null) {
//			Menu menu = popups [0];
//			if (menu == null) break;
//			int length = popups.length;
//			System.arraycopy (popups, 1, popups, 0, --length);
//			popups [length] = null;
//			runDeferredEvents ();
//			if (!menu.isDisposed ()) menu._setVisible (true);
//			result = true;
//		}
//		popups = null;
//		return result;
	}

	boolean runSettings() {
		return false;
//		if (!runSettings) return false;
//		runSettings = false;
//
//		boolean ignoreColorChange = false;
//		/*
//		 * Feature in Cocoa: When dark mode is enabled on OSX version >= 10.10 and a SWT TrayItem (NSStatusItem) is present in the menubar,
//		 * changing the OSX appearance or changing the configuration of attached displays causes the textColor and textBackground color to change.
//		 * This sets the text foreground of several widgets as white and hence text is invisible. The workaround is to detect this case and prevent
//		 * the update of LIST_FOREGROUND, LIST_BACKGROUND and COLOR_WIDGET_FOREGROUND colors.
//		 */
//		if (tray != null && tray.itemCount > 0) {
//			/*
//			 * osxMode will be "Dark" when in OSX dark mode. Otherwise, it'll be null.
//			 */
//			NSString osxMode = NSUserDefaults.standardUserDefaults ().stringForKey (NSString.stringWith ("AppleInterfaceStyle"));
//			if (osxMode != null && "Dark".equals (osxMode.getString ())) {
//				ignoreColorChange = true;
//			}
//		}
//		initColors (ignoreColorChange);
//
//		sendEvent (SWT.Settings, null);
//		Shell [] shells = getShells ();
//		for (int i=0; i<shells.length; i++) {
//			Shell shell = shells [i];
//			if (!shell.isDisposed ()) {
//				shell.redraw (true);
//				shell.layout (true, true);
//			}
//		}
//		return true;
	}

	boolean runSkin() {
		if (skinCount > 0) {
			Widget[] oldSkinWidgets = skinList;
			int count = skinCount;
			skinList = new Widget[GROW_SIZE];
			skinCount = 0;
			if (eventTable != null && eventTable.hooks(SWT.Skin)) {
				for (int i = 0; i < count; i++) {
					Widget widget = oldSkinWidgets[i];
					if (widget != null && !widget.isDisposed()) {
						widget.state &= ~Widget.SKIN_NEEDED;
						oldSkinWidgets[i] = null;
						Event event = new Event();
						event.widget = widget;
						sendEvent(SWT.Skin, event);
					}
				}
			}
			return true;
		}
		return false;
	}

	boolean runTimers() {
		if (timerList == null)
			return false;
		boolean result = false;
		for (int i = 0; i < timerList.length; i++) {
			if (timerList[i] != null) {
				Runnable runnable = timerList[i];
				timerList[i] = null;
				if (runnable != null) {
					result = true;
					runnable.run();
				}
			}
		}
		return result;
	}

	boolean runDeferredEvents() {
		boolean run = false;
		/*
		 * Run deferred events. This code is always called in the Display's thread so it
		 * must be re-enterant need not be synchronized.
		 */
		while (eventQueue != null) {

			/* Take an event off the queue */
			Event event = eventQueue[0];
			if (event == null)
				break;
			int length = eventQueue.length;
			System.arraycopy(eventQueue, 1, eventQueue, 0, --length);
			eventQueue[length] = null;

			/* Run the event */
			Widget widget = event.widget;
			if (widget != null && !widget.isDisposed()) {
				Widget item = event.item;
				if (item == null || !item.isDisposed()) {
					run = true;
					widget.notifyListeners(event.type, event);
				}
			}

			/*
			 * At this point, the event queue could be null due to a recursive invokation
			 * when running the event.
			 */
		}

		/* Clear the queue */
		eventQueue = null;
		return run;
	}

	boolean runDeferredLayouts() {
		if (layoutDeferredCount != 0) {
			Composite[] temp = layoutDeferred;
			int count = layoutDeferredCount;
			layoutDeferred = null;
			layoutDeferredCount = 0;
			for (int i = 0; i < count; i++) {
				Composite comp = temp[i];
				if (!comp.isDisposed())
					comp.setLayoutDeferred(false);
			}
			return true;
		}
		return false;
	}

	static boolean isValidClass(Class<?> clazz) {
		String name = clazz.getName();
		int index = name.lastIndexOf('.');
		return name.substring(0, index + 1).equals(PACKAGE_PREFIX);
	}

	boolean filters(int eventType) {
		if (filterTable == null)
			return false;
		return filterTable.hooks(eventType);
	}

	void sendEvent(int eventType, Event event) {
		if (eventTable == null && filterTable == null) {
			return;
		}
		if (event == null)
			event = new Event();
		event.display = this;
		event.type = eventType;
		if (event.time == 0)
			event.time = getLastEventTime();
		sendEvent(eventTable, event);
	}

	void sendEvent(EventTable table, Event event) {
		try {
			sendEventCount++;
			if (!filterEvent(event)) {
				if (table != null) {
					int type = event.type;
					sendPreEvent(type);
					try {
						table.sendEvent(event);
					} finally {
						sendPostEvent(type);
					}
				}
			}
		} finally {
			sendEventCount--;
		}
	}

	void sendPreEvent(int eventType) {
		if (eventType != SWT.PreEvent && eventType != SWT.PostEvent && eventType != SWT.PreExternalEventDispatch
				&& eventType != SWT.PostExternalEventDispatch) {
			sendJDKInternalEvent(SWT.PreEvent, eventType);
		}
	}

	void sendPostEvent(int eventType) {
		if (eventType != SWT.PreEvent && eventType != SWT.PostEvent && eventType != SWT.PreExternalEventDispatch
				&& eventType != SWT.PostExternalEventDispatch) {
			sendJDKInternalEvent(SWT.PostEvent, eventType);
		}
	}

	/**
	 * Sends a SWT.PreExternalEventDispatch event.
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void sendPreExternalEventDispatchEvent() {
		sendJDKInternalEvent(SWT.PreExternalEventDispatch);
	}

	/**
	 * Sends a SWT.PostExternalEventDispatch event.
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void sendPostExternalEventDispatchEvent() {
		sendJDKInternalEvent(SWT.PostExternalEventDispatch);
	}

	private void sendJDKInternalEvent(int eventType) {
		sendJDKInternalEvent(eventType, 0);
	}

	/** does sent event with JDK time **/
	private void sendJDKInternalEvent(int eventType, int detail) {
		if (eventTable == null || !eventTable.hooks(eventType)) {
			return;
		}
		Event event = new Event();
		event.detail = detail;
		event.display = this;
		event.type = eventType;
		// time is set for debugging purpose only:
		event.time = (int) (System.nanoTime() / 1000_000L);
		if (!filterEvent(event)) {
			sendEvent(eventTable, event);
		}
	}

	boolean isValidThread() {
		return thread == Thread.currentThread();
	}

	/**
	 * Returns the display which the currently running thread is the user-interface
	 * thread for, or null if the currently running thread is not a user-interface
	 * thread for any display.
	 *
	 * @return the current display
	 */
	public static Display getCurrent() {
		return findDisplay(Thread.currentThread());
	}

	/**
	 * Returns the default display. One is created (making the thread that invokes
	 * this method its user-interface thread) if it did not already exist.
	 *
	 * @return the default display
	 */
	public static Display getDefault() {
		synchronized (Device.class) {
			if (Default == null)
				Default = new Display();
			return Default;
		}
	}

	/**
	 * Returns the display which the given thread is the user-interface thread for,
	 * or null if the given thread is not a user-interface thread for any display.
	 * Specifying <code>null</code> as the thread will return <code>null</code> for
	 * the display.
	 *
	 * @param thread the user-interface thread
	 * @return the display for the given thread
	 */
	public static Display findDisplay(Thread thread) {
		synchronized (Device.class) {
			for (int i = 0; i < Displays.length; i++) {
				Display display = Displays[i];
				if (display != null && display.thread == thread) {
					return display;
				}
			}
			return null;
		}
	}

	void postEvent(Event event) {
		/*
		 * Place the event at the end of the event queue. This code is always called in
		 * the Display's thread so it must be re-enterant but does not need to be
		 * synchronized.
		 */
		if (eventQueue == null)
			eventQueue = new Event[4];
		int index = 0;
		int length = eventQueue.length;
		while (index < length) {
			if (eventQueue[index] == null)
				break;
			index++;
		}
		if (index == length) {
			Event[] newQueue = new Event[length + 4];
			System.arraycopy(eventQueue, 0, newQueue, 0, length);
			eventQueue = newQueue;
		}
		eventQueue[index] = event;
	}

	boolean filterEvent(Event event) {
		if (filterTable != null) {
			int type = event.type;
			sendPreEvent(type);
			try {
				filterTable.sendEvent(event);
			} finally {
				sendPostEvent(type);
			}
		}
		return false;
	}

	int getLastEventTime() {
		// TODO
		return 0;
	}

	void addSkinnableWidget(Widget widget) {
		if (skinCount >= skinList.length) {
			Widget[] newSkinWidgets = new Widget[(skinList.length + 1) * 3 / 2];
			System.arraycopy(skinList, 0, newSkinWidgets, 0, skinList.length);
			skinList = newSkinWidgets;
		}
		skinList[skinCount++] = widget;
	}

	static boolean getSheetEnabled() {
		return !"false".equals(System.getProperty("org.eclipse.swt.sheet"));
	}

	Color getWidgetColor(int id) {
		if (0 <= id && id < colors.length && colors[id] != null) {
			return Color.skia_new(this, colors[id]);
		}
		return null;
	}

	Widget getWidget(java.awt.Component view) {
		return GetWidget(view);
	}

	static Widget GetWidget(java.awt.Component view) {
		if (view == null)
			return null;
		return widgetMap.get(view);
	}

	void addWidget(UnoControl view, Widget widget) {
		if (view == null)
			return;

		if (widgetMap.get(view) == null) {
			widgetMap.put(view, widget);
		}
	}

	Widget removeWidget(UnoControl view) {
		if (view == null)
			return null;

		Widget widget = widgetMap.get(view);
		widgetMap.remove(view);
		return widget;
	}

	void error(int code) {
		SWT.error(code);
	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Display</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all platforms,
	 * and should never be called from application code.
	 * </p>
	 *
	 * @param data the platform specific GC data
	 * @return the platform specific GC handle
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_DEVICE_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         </ul>
	 * @exception SWTError
	 *                         <ul>
	 *                         <li>ERROR_NO_HANDLES if a handle could not be
	 *                         obtained for gc creation</li>
	 *                         </ul>
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public long internal_new_GC(GCData data) {
		return 0;
//		if (isDisposed()) error(SWT.ERROR_DEVICE_DISPOSED);
//		if (screenWindow == null) {
//			NSWindow window = (NSWindow) new NSWindow ().alloc ();
//			NSRect rect = new NSRect();
//			window = window.initWithContentRect(rect, OS.NSBorderlessWindowMask, OS.NSBackingStoreBuffered, false);
//			window.setReleasedWhenClosed(false);
//			screenWindow = window;
//		}
//		NSGraphicsContext context = screenWindow.graphicsContext();
//		if (context == null) {
//			// create a bitmap based context, which will still work e.g. for text size computations
//			// it is unclear if the bitmap needs to be larger than the text to be measured.
//			// the following values should be big enough in any case.
//			int width = 1920;
//			int height = 256;
//			NSBitmapImageRep rep = (NSBitmapImageRep) new NSBitmapImageRep().alloc();
//			rep = rep.initWithBitmapDataPlanes(0, width, height, 8, 3, false, false, OS.NSDeviceRGBColorSpace,
//					OS.NSAlphaFirstBitmapFormat, width * 4, 32);
//			context = NSGraphicsContext.graphicsContextWithBitmapImageRep(rep);
//			rep.release();
//		}
////		NSAffineTransform transform = NSAffineTransform.transform();
////		NSSize size = handle.size();
////		transform.translateXBy(0, size.height);
////		transform.scaleXBy(1, -1);
////		transform.set();
//		if (data != null) {
//			int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
//			if ((data.style & mask) == 0) {
//				data.style |= SWT.LEFT_TO_RIGHT;
//			}
//			data.device = this;
//			data.background = getSystemColor(SWT.COLOR_WHITE).handle;
//			data.foreground = getSystemColor(SWT.COLOR_BLACK).handle;
//			data.font = getSystemFont();
//		}
//		return context.id;
	}

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Display</code>. It is marked public only so that it can be shared
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
		if (isDisposed())
			error(SWT.ERROR_DEVICE_DISPOSED);
	}

	/**
	 * Returns the current exception handler. It will receive all exceptions thrown
	 * by listeners and external callbacks in this display. If code wishes to
	 * temporarily replace the exception handler (for example, during a unit test),
	 * it is common practice to invoke this method prior to replacing the exception
	 * handler so that the old handler may be restored afterward.
	 *
	 * @return the current exception handler. Never <code>null</code>.
	 * @since 3.106
	 */
	public final Consumer<RuntimeException> getRuntimeExceptionHandler() {
		return runtimeExceptionHandler;
	}

	/**
	 * Returns the current exception handler. It will receive all errors thrown by
	 * listeners and external callbacks in this display. If code wishes to
	 * temporarily replace the error handler (for example, during a unit test), it
	 * is common practice to invoke this method prior to replacing the error handler
	 * so that the old handler may be restored afterward.
	 *
	 * @return the current error handler. Never <code>null</code>.
	 * @since 3.106
	 */
	public final Consumer<Error> getErrorHandler() {
		return errorHandler;
	}

	void wakeThread() {
//		TODO
//		//new pool?
//		NSObject object = new NSObject().alloc().init();
//		object.performSelectorOnMainThread(OS.sel_release, null, false);
	}

	public Image getSystemImage(int id) {
		checkDevice();
		switch (id) {
		case SWT.ICON_ERROR: {
			if (errorIcon != null)
				return errorIcon;
			return errorIcon = extracted(id);
		}
		case SWT.ICON_WORKING:
		case SWT.ICON_INFORMATION: {
			if (infoIcon != null)
				return infoIcon;
			return infoIcon = extracted(id);
		}
		case SWT.ICON_QUESTION: {
			if (questionIcon != null)
				return questionIcon;
			return questionIcon = extracted(id);
		}
		case SWT.ICON_WARNING: {
			if (warningIcon != null)
				return warningIcon;
			return warningIcon = extracted(id);
		}
		}
		return null;
	}

	private Image extracted(int id) {
		// TODO (visjee) get the proper image/icon from the system based on the
		// parameter

		// Create an image with a white background
		Image image = new Image(this, 200, 200);
		GC gc = new GC(image);
		gc.setBackground(getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, 200, 200);

		// Draw an oval shape on the image
		gc.setBackground(getSystemColor(SWT.COLOR_BLACK));
		gc.fillOval(50, 50, 100, 100);

		// Dispose of the GC
		gc.dispose();
		return image;
	}

	public Monitor[] getMonitors() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);

		if (monitors == null) {
			monitors = new Monitor[] { getPrimaryMonitor() };
		}

		return monitors;
	}

	public void timerExec(int wakeTime, Runnable runnable) {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Returns the primary monitor for that device.
	 *
	 * @return the primary monitor
	 *
	 * @since 3.0
	 */
	public Monitor getPrimaryMonitor() {
		checkDevice();
		// The current implementation is bad
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		if (primaryMonitor == null) {
			primaryMonitor = new Monitor();
			primaryMonitor.clientX = 0;
			primaryMonitor.clientY = 0;
			primaryMonitor.width = 800;
			primaryMonitor.height = 600;
			primaryMonitor.clientHeight = primaryMonitor.height - 20;
			primaryMonitor.clientWidth = primaryMonitor.width - 20;
			primaryMonitor.zoom = 100;
		}

		return primaryMonitor;
	}

	public Shell[] getShells() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	/**
	 * Returns the currently active <code>Shell</code>, or null
	 * if no shell belonging to the currently running application
	 * is active.
	 *
	 * @return the active shell or null
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public Shell getActiveShell () {
		checkDevice ();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return getShells()[0];
	}

	/**
	 * Returns the matching standard platform cursor for the given
	 * constant, which should be one of the cursor constants
	 * specified in class <code>SWT</code>. This cursor should
	 * not be free'd because it was allocated by the system,
	 * not the application.  A value of <code>null</code> will
	 * be returned if the supplied constant is not an SWT cursor
	 * constant.
	 *
	 * @param id the SWT cursor constant
	 * @return the corresponding cursor or <code>null</code>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 *
	 * @see SWT#CURSOR_ARROW
	 * @see SWT#CURSOR_WAIT
	 * @see SWT#CURSOR_CROSS
	 * @see SWT#CURSOR_APPSTARTING
	 * @see SWT#CURSOR_HELP
	 * @see SWT#CURSOR_SIZEALL
	 * @see SWT#CURSOR_SIZENESW
	 * @see SWT#CURSOR_SIZENS
	 * @see SWT#CURSOR_SIZENWSE
	 * @see SWT#CURSOR_SIZEWE
	 * @see SWT#CURSOR_SIZEN
	 * @see SWT#CURSOR_SIZES
	 * @see SWT#CURSOR_SIZEE
	 * @see SWT#CURSOR_SIZEW
	 * @see SWT#CURSOR_SIZENE
	 * @see SWT#CURSOR_SIZESE
	 * @see SWT#CURSOR_SIZESW
	 * @see SWT#CURSOR_SIZENW
	 * @see SWT#CURSOR_UPARROW
	 * @see SWT#CURSOR_IBEAM
	 * @see SWT#CURSOR_NO
	 * @see SWT#CURSOR_HAND
	 *
	 * @since 3.0
	 */
	public Cursor getSystemCursor (int id) {
		checkDevice ();
		if (!(0 <= id && id < cursors.length)) return null;
		if (cursors [id] == null) {
			cursors [id] = new Cursor (this, id);
		}
		return cursors [id];
	}

	void addLayoutDeferred (Composite comp) {
		if (layoutDeferred == null) layoutDeferred = new Composite [64];
		if (layoutDeferredCount == layoutDeferred.length) {
			Composite [] temp = new Composite [layoutDeferred.length + 64];
			System.arraycopy (layoutDeferred, 0, temp, 0, layoutDeferred.length);
			layoutDeferred = temp;
		}
		layoutDeferred[layoutDeferredCount++] = comp;
	}

	/**
	 * Maps a point from one coordinate system to another.
	 * When the control is null, coordinates are mapped to
	 * the display.
	 * <p>
	 * NOTE: On right-to-left platforms where the coordinate
	 * systems are mirrored, special care needs to be taken
	 * when mapping coordinates from one control to another
	 * to ensure the result is correctly mirrored.
	 *
	 * Mapping a point that is the origin of a rectangle and
	 * then adding the width and height is not equivalent to
	 * mapping the rectangle.  When one control is mirrored
	 * and the other is not, adding the width and height to a
	 * point that was mapped causes the rectangle to extend
	 * in the wrong direction.  Mapping the entire rectangle
	 * instead of just one point causes both the origin and
	 * the corner of the rectangle to be mapped.
	 * </p>
	 *
	 * @param from the source <code>Control</code> or <code>null</code>
	 * @param to the destination <code>Control</code> or <code>null</code>
	 * @param rectangle to be mapped
	 * @return rectangle with mapped coordinates
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the Control from or the Control to have been disposed</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 *
	 * @since 2.1.2
	 */
	public Rectangle map (Control from, Control to, Rectangle rectangle) {
		checkDevice ();
		if (rectangle == null) error (SWT.ERROR_NULL_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return rectangle;
	}

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when an event of the given type occurs anywhere in
	 * a widget. The event type is one of the event constants defined
	 * in class <code>SWT</code>.
	 *
	 * @param eventType the type of event to listen for
	 * @param listener the listener which should no longer be notified when the event occurs
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 *
	 * @see Listener
	 * @see SWT
	 * @see #addFilter
	 * @see #addListener
	 *
	 * @since 3.0
	 */
	public void removeFilter (int eventType, Listener listener) {
		checkDevice ();
		if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
		if (filterTable == null) return;
		filterTable.unhook (eventType, listener);
		if (filterTable.size () == 0) filterTable = null;
	}

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when an event of the given type occurs anywhere
	 * in a widget. The event type is one of the event constants
	 * defined in class <code>SWT</code>. When the event does occur,
	 * the listener is notified by sending it the <code>handleEvent()</code>
	 * message.
	 * <p>
	 * Setting the type of an event to <code>SWT.None</code> from
	 * within the <code>handleEvent()</code> method can be used to
	 * change the event type and stop subsequent Java listeners
	 * from running. Because event filters run before other listeners,
	 * event filters can both block other listeners and set arbitrary
	 * fields within an event. For this reason, event filters are both
	 * powerful and dangerous. They should generally be avoided for
	 * performance, debugging and code maintenance reasons.
	 * </p>
	 *
	 * @param eventType the type of event to listen for
	 * @param listener the listener which should be notified when the event occurs
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 *
	 * @see Listener
	 * @see SWT
	 * @see #removeFilter
	 * @see #removeListener
	 *
	 * @since 3.0
	 */
	public void addFilter (int eventType, Listener listener) {
		checkDevice ();
		if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
		if (filterTable == null) filterTable = new EventTable ();
		filterTable.hook (eventType, listener);
	}

	/**
	 * Adds the listener to the collection of listeners who will
	 * be notified when an event of the given type occurs. The event
	 * type is one of the event constants defined in class <code>SWT</code>.
	 * When the event does occur in the display, the listener is notified by
	 * sending it the <code>handleEvent()</code> message.
	 *
	 * @param eventType the type of event to listen for
	 * @param listener the listener which should be notified when the event occurs
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 *
	 * @see Listener
	 * @see SWT
	 * @see #removeListener
	 *
	 * @since 2.0
	 */
	public void addListener (int eventType, Listener listener) {
		checkDevice ();
		if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null) eventTable = new EventTable ();
		eventTable.hook (eventType, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will
	 * be notified when an event of the given type occurs. The event type
	 * is one of the event constants defined in class <code>SWT</code>.
	 *
	 * @param eventType the type of event to listen for
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 *
	 * @see Listener
	 * @see SWT
	 * @see #addListener
	 *
	 * @since 2.0
	 */
	public void removeListener (int eventType, Listener listener) {
		checkDevice ();
		if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null) return;
		eventTable.unhook (eventType, listener);
	}




	public Control getFocusControl() {
		checkDevice();

		System.err.println(new IllegalStateException().getStackTrace());

		return null;
	}


	@Override
	public Rectangle getClientArea() {
		checkDevice();
		return DPIUtil.autoScaleDown(getClientAreaInPixels());
	}

	Rectangle getClientAreaInPixels() {
		checkDevice();
		System.err.println(new IllegalStateException().getStackTrace());
		return null;
	}

	public Point getCursorLocation() {
		checkDevice();
		return DPIUtil.autoScaleDown(getCursorLocationInPixels());
	}

	Point getCursorLocationInPixels() {
		System.err.println(new IllegalStateException().getStackTrace());
		return null;
	}


	@Override
	public Rectangle getBounds() {
		checkDevice ();
		return DPIUtil.autoScaleDown(getBoundsInPixels());
	}

	Rectangle getBoundsInPixels () {
		checkDevice ();
		System.err.println(new IllegalStateException().getStackTrace());
		return null;
	}

	public Point map (Control from, Control to, int x, int y) {
		checkDevice ();
		x = DPIUtil.autoScaleUp(x);
		y = DPIUtil.autoScaleUp(y);
		return DPIUtil.autoScaleDown(mapInPixels(from, to, x, y));
	}

	Point mapInPixels (Control from, Control to, int x, int y) {
		System.err.println(new IllegalStateException().getStackTrace());
		return null;
	}

	public Point map (Control from, Control to, Point point) {
		checkDevice ();
		if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
		point = DPIUtil.autoScaleUp(point);
		return DPIUtil.autoScaleDown(mapInPixels(from, to, point));
	}

	Point mapInPixels (Control from, Control to, Point point) {
		return mapInPixels (from, to, point.x, point.y);
	}


}
