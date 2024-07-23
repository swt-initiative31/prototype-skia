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
package org.eclipse.swt.accessibility;

import org.eclipse.swt.widgets.*;

/**
 * Instances of this class provide a bridge between application
 * code and assistive technology clients. Many platforms provide
 * default accessible behavior for most widgets, and this class
 * allows that default behavior to be overridden. Applications
 * can get the default Accessible object for a control by sending
 * it <code>getAccessible</code>, and then add an accessible listener
 * to override simple items like the name and help string, or they
 * can add an accessible control listener to override complex items.
 * As a rule of thumb, an application would only want to use the
 * accessible control listener to implement accessibility for a
 * custom control.
 *
 * @see Control#getAccessible
 * @see AccessibleListener
 * @see AccessibleEvent
 * @see AccessibleControlListener
 * @see AccessibleControlEvent
 * @see <a href="http://www.eclipse.org/swt/snippets/#accessibility">Accessibility snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 2.0
 */
public class Accessible {

	public void addAccessibleListener(AccessibleAdapter accessibleAdapter) {
		// TODO Auto-generated method stub

	}

	public void addAccessibleControlListener(AccessibleControlAdapter accessibleControlAdapter) {
		// TODO Auto-generated method stub

	}

	public void addAccessibleAttributeListener(AccessibleAttributeAdapter accAttributeAdapter) {
		// TODO Auto-generated method stub

	}

	public void addAccessibleEditableTextListener(AccessibleEditableTextListener accEditableTextListener) {
		// TODO Auto-generated method stub

	}

	public void addAccessibleTextListener(AccessibleTextExtendedAdapter accTextExtendedAdapter) {
		// TODO Auto-generated method stub

	}

	public void addAccessibleTextListener(AccessibleTextAdapter accessibleTextAdapter) {
		// TODO Auto-generated method stub

	}

	public void textCaretMoved(int i) {
		// TODO Auto-generated method stub

	}

	public void textChanged(int textDelete, int start, int replaceCharCount) {
		// TODO Auto-generated method stub

	}

	public Object setFocus(int childidSelf) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeAccessibleControlListener(AccessibleControlAdapter accControlAdapter) {
		// TODO Auto-generated method stub

	}

	public void removeAccessibleAttributeListener(AccessibleAttributeAdapter accAttributeAdapter) {
		// TODO Auto-generated method stub

	}

	public void removeAccessibleEditableTextListener(AccessibleEditableTextListener accEditableTextListener) {
		// TODO Auto-generated method stub

	}

	public void removeAccessibleTextListener(AccessibleTextExtendedAdapter accTextExtendedAdapter) {
		// TODO Auto-generated method stub

	}

	public void removeAccessibleListener(AccessibleAdapter accAdapter) {
		// TODO Auto-generated method stub

	}

	public void textSelectionChanged() {
		// TODO Auto-generated method stub

	}

	public static Accessible internal_new_Accessible(Control control) {
		// TODO Auto-generated method stub
		return null;
	}

}
