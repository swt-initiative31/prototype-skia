/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.dnd;


/**
 * The class <code>RTFTransfer</code> provides a platform specific mechanism
 * for converting text in RTF format represented as a java <code>String</code>
 * to a platform specific representation of the data and vice versa.
 *
 * <p>An example of a java <code>String</code> containing RTF text is shown
 * below:</p>
 *
 * <pre><code>
 *     String rtfData = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i Hello World}";
 * </code></pre>
 *
 * @see Transfer
 */
public class RTFTransfer extends ByteArrayTransfer {

	private static RTFTransfer _instance = new RTFTransfer();
	private static final String CF_RTF = "Rich Text Format"; //$NON-NLS-1$

private RTFTransfer() {}

/**
 * Returns the singleton instance of the RTFTransfer class.
 *
 * @return the singleton instance of the RTFTransfer class
 */
public static RTFTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts RTF-formatted text
 * represented by a java <code>String</code> to a platform specific representation.
 *
 * @param object a java <code>String</code> containing RTF text
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 *
 * @see Transfer#nativeToJava
 */
@Override
public void javaToNative (Object object, TransferData transferData){

	return;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific
 * representation of RTF text to a java <code>String</code>.
 *
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing RTF text if the conversion was successful;
 * 		otherwise null
 *
 * @see Transfer#javaToNative
 */
@Override
public Object nativeToJava(TransferData transferData){
	return null;

}

}
