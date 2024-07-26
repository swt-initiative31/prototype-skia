package org.eclipse.swt.snippets;

import java.awt.*;

import javax.swing.*;

import com.sun.star.awt.*;
import com.sun.star.beans.*;
import com.sun.star.uno.*;
import com.sun.star.uno.Exception;

public class XTextFieldSnippet {
	public static void main(String[] args) {
		try {

			LocalUnoLoader.init();

			com.sun.star.awt.WindowDescriptor aDescriptor = new com.sun.star.awt.WindowDescriptor();

			aDescriptor.Type = com.sun.star.awt.WindowClass.TOP;
			aDescriptor.WindowServiceName = "window";
			aDescriptor.ParentIndex = -1;
			aDescriptor.Parent = null;
			aDescriptor.Bounds = new com.sun.star.awt.Rectangle(100, 100, 800,
					800);

			aDescriptor.WindowAttributes = com.sun.star.awt.WindowAttribute.BORDER
					| com.sun.star.awt.WindowAttribute.MOVEABLE
					| com.sun.star.awt.WindowAttribute.SIZEABLE
					| com.sun.star.awt.WindowAttribute.CLOSEABLE;

			XWindowPeer peer = LocalUnoLoader.xToolkit
					.createWindow(aDescriptor);
			XWindow w = UnoRuntime
					.queryInterface(com.sun.star.awt.XWindow.class, peer);
			// Show the main window
			w.setVisible(true);

			Object editControl = LocalUnoLoader.xMCF.createInstanceWithContext(
					"com.sun.star.awt.UnoControlEdit", LocalUnoLoader.xContext);

			// Create the text field model
			Object textFieldModel = LocalUnoLoader.xMCF
					.createInstanceWithContext(
							"com.sun.star.awt.UnoControlEditModel",
							LocalUnoLoader.xContext);
			XPropertySet xTextFieldModelPropSet = UnoRuntime
					.queryInterface(XPropertySet.class, textFieldModel);
			xTextFieldModelPropSet.setPropertyValue("PositionX", 50);
			xTextFieldModelPropSet.setPropertyValue("PositionY", 50);
			xTextFieldModelPropSet.setPropertyValue("Width", 200);
			xTextFieldModelPropSet.setPropertyValue("Height", 80);

			XControl xCon = UnoRuntime.queryInterface(XControl.class,
					editControl);
			xCon.setModel(UnoRuntime.queryInterface(XControlModel.class,
					textFieldModel));
			xCon.createPeer(LocalUnoLoader.xToolkit, peer);
			XTextComponent xTextComponent = UnoRuntime
					.queryInterface(XTextComponent.class, xCon);

			UnoRuntime.queryInterface(XWindow.class, xCon.getPeer()).setPosSize(
					100, 100, 500, 500, com.sun.star.awt.PosSize.POSSIZE);

			Font defaultFont = UIManager.getDefaults().getFont("Label.font");

			defaultFont = null;

			FontDescriptor fontDescriptor = new FontDescriptor();
			if (defaultFont != null) {
			    fontDescriptor.Name = defaultFont.getName(); // Set the font face name from the system's default
			    fontDescriptor.Height = (short) defaultFont.getSize(); // Set the font height from the system's default
			    fontDescriptor.Weight = (defaultFont.isBold()) ? com.sun.star.awt.FontWeight.BOLD : com.sun.star.awt.FontWeight.NORMAL;
			    fontDescriptor.Slant = (defaultFont.isItalic()) ? com.sun.star.awt.FontSlant.ITALIC : com.sun.star.awt.FontSlant.NONE;
			} else {

				// Set the font
				fontDescriptor.Name = "Consolas"; // Set the font face name
				fontDescriptor.Height = 8; // Set the font height
				fontDescriptor.Weight = com.sun.star.awt.FontWeight.NORMAL; // Set
																			// the
																			// font
																			// weight
																			// (e.g.,
																			// normal,
																			// bold)
				fontDescriptor.Slant = com.sun.star.awt.FontSlant.NONE; // Set
																			// the
																			// font
																			// slant
																			// (e.g.,
																			// none,
																			// italic)
				fontDescriptor.Underline = com.sun.star.awt.FontUnderline.NONE; // Set
																				// underline
				fontDescriptor.Strikeout = com.sun.star.awt.FontStrikeout.NONE; // Set
																				// strikeout
				fontDescriptor.CharSet = com.sun.star.awt.CharSet.SYSTEM; // Set
																			// character
																			// set

			}

			xTextFieldModelPropSet.setPropertyValue("FontDescriptor",
					fontDescriptor);

			// Set and read text
			xTextComponent.setText("New Text!");
			String text = xTextComponent.getText();
			System.out.println("Text in TextField: " + text);

			// Abfragen der Font-Eigenschaften
			XPropertySet xPropSet = UnoRuntime
					.queryInterface(XPropertySet.class, xCon.getModel());
			Object desc = xPropSet.getPropertyValue("FontDescriptor");

			System.out.println("Font desc: " + desc);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}