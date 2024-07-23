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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.uno.*;

/**
 * Instances of this class implement the notebook user interface metaphor. It
 * allows the user to select a notebook page from set of pages.
 * <p>
 * The item children that may be added to instances of this class must be of
 * type <code>TabItem</code>. <code>Control</code> children are created and then
 * set into a tab item using <code>TabItem#setControl</code>.
 * </p>
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>, it
 * does not make sense to set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>TOP, BOTTOM</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tabfolder">TabFolder,
 *      TabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TabFolder extends Composite {
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
	 * @see SWT
	 * @see SWT#TOP
	 * @see SWT#BOTTOM
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TabFolder(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the user changes the receiver's selection, by sending it one of the messages
	 * defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * When <code>widgetSelected</code> is called, the item field of the event
	 * object is valid. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 *
	 * @param listener the listener which should be notified when the user changes
	 *                 the receiver's selection
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
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	static int checkStyle(int style) {
		style = checkBits(style, SWT.TOP, SWT.BOTTOM, 0, 0, 0, 0);

		/*
		 * Even though it is legal to create this widget with scroll bars, they serve no
		 * useful purpose because they do not automatically scroll the widget's client
		 * area. The fix is to clear the SWT style.
		 */
		return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass())
			error(SWT.ERROR_INVALID_SUBCLASS);
	}

	void createItem(TabItem item, int index) {
//	int count = (int)OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
//	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
//	if (count == items.length) {
//		TabItem [] newItems = new TabItem [items.length + 4];
//		System.arraycopy (items, 0, newItems, 0, items.length);
//		items = newItems;
//	}
//	TCITEM tcItem = new TCITEM ();
//	if (OS.SendMessage (handle, OS.TCM_INSERTITEM, index, tcItem) == -1) {
//		error (SWT.ERROR_ITEM_NOT_ADDED);
//	}
//	System.arraycopy (items, index, items, index + 1, count - index);
//	items [index] = item;
//
//	/*
//	* Send a selection event when the item that is added becomes
//	* the new selection.  This only happens when the first item
//	* is added.
//	*/
//	if (count == 0) {
//		Event event = new Event ();
//		event.item = items [0];
//		sendSelectionEvent (SWT.Selection, event, true);
//		// the widget could be destroyed at this point
//	}
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	void destroyItem(TabItem item) {
//	int count = (int)OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
//	int index = 0;
//	while (index < count) {
//		if (items [index] == item) break;
//		index++;
//	}
//	if (index == count) return;
//	int selectionIndex = (int)OS.SendMessage (handle, OS.TCM_GETCURSEL, 0, 0);
//	if (OS.SendMessage (handle, OS.TCM_DELETEITEM, index, 0) == 0) {
//		error (SWT.ERROR_ITEM_NOT_REMOVED);
//	}
//	System.arraycopy (items, index + 1, items, index, --count - index);
//	items [count] = null;
//	if (count == 0) {
//		if (imageList != null) {
//			OS.SendMessage (handle, OS.TCM_SETIMAGELIST, 0, 0);
//			display.releaseImageList (imageList);
//		}
//		imageList = null;
//		items = new TabItem [4];
//	}
//	if (count > 0 && index == selectionIndex) {
//		setSelection (Math.max (0, selectionIndex - 1), true);
//	}
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Returns the item at the given, zero-relative index in the receiver. Throws an
	 * exception if the index is out of range.
	 *
	 * @param index the index of the item to return
	 * @return the item at the given index
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_RANGE - if the index is
	 *                                     not between 0 and the number of elements
	 *                                     in the list minus 1 (inclusive)</li>
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
	public TabItem getItem(int index) {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	/**
	 * Returns the tab item at the given point in the receiver or null if no such
	 * item exists. The point is in the coordinate system of the receiver.
	 *
	 * @param point the point used to locate the item
	 * @return the tab item at the given point, or null if the point is not in a tab
	 *         item
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
	 *
	 * @since 3.4
	 */
	public TabItem getItem(Point point) {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	/**
	 * Returns the number of items contained in the receiver.
	 *
	 * @return the number of items
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getItemCount() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return -1;
	}

	/**
	 * Returns an array of <code>TabItem</code>s which are the items in the
	 * receiver.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain its
	 * list of items, so modifying the array will not affect the receiver.
	 * </p>
	 *
	 * @return the items in the receiver
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public TabItem[] getItems() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	/**
	 * Returns an array of <code>TabItem</code>s that are currently selected in the
	 * receiver. An empty array indicates that no items are selected.
	 * <p>
	 * Note: This is not the actual structure used by the receiver to maintain its
	 * selection, so modifying the array will not affect the receiver.
	 * </p>
	 *
	 * @return an array representing the selection
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public TabItem[] getSelection() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;
	}

	/**
	 * Returns the zero-relative index of the item which is currently selected in
	 * the receiver, or -1 if no item is selected.
	 *
	 * @return the index of the selected item
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getSelectionIndex() {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return -1;
	}

	/**
	 * Searches the receiver's list starting at the first item (index 0) until an
	 * item is found that is equal to the argument, and returns the index of that
	 * item. If no item is found, returns -1.
	 *
	 * @param item the search item
	 * @return the index of the item
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the item is
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
	public int indexOf(TabItem item) {
		checkWidget();
		if (item == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return -1;
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the user changes the receiver's selection.
	 *
	 * @param listener the listener which should no longer be notified
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
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	/**
	 * Sets the receiver's selection to the given item. The current selected is
	 * first cleared, then the new item is selected.
	 *
	 * @param item the item to select
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the item is
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
	 * @since 3.2
	 */
	public void setSelection(TabItem item) {
		checkWidget();
		if (item == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		setSelection(new TabItem[] { item });
	}

	/**
	 * Sets the receiver's selection to be the given array of items. The current
	 * selected is first cleared, then the new items are selected.
	 *
	 * @param items the array of items
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the items
	 *                                     array is null</li>
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
	public void setSelection(TabItem[] items) {
		checkWidget();
		if (items == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public void setFont(Font font) {
		checkWidget();
		super.setFont(font);
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver. If the
	 * item at the index was already selected, it remains selected. The current
	 * selection is first cleared, then the new items are selected. Indices that are
	 * out of range are ignored.
	 *
	 * @param index the index of the item to select
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setSelection(int index) {
		checkWidget();
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
	}

	@Override
	public UnoControl getHandle() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return null;

	}

	@Override
	public boolean setFocus() {
		System.out.println("WARN: Not implemented yet: " + new Throwable().getStackTrace()[0]);
		return false;

	}
}
