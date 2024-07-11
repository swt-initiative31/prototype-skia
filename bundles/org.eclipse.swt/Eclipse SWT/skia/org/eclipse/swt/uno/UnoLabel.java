package org.eclipse.swt.uno;

import org.eclipse.swt.graphics.*;
import org.jfree.layouting.input.style.keys.font.*;

public class UnoLabel extends UnoComponent {

	public void init() {


		Object oFontDescriptor = UnoLoader.xMCF.createInstanceWithContext("com.sun.star.awt.FontDescriptor", UnoLoader.xContext);
		oFontDescriptor.Name = "Arial";
		oFontDescriptor.Height = 14;
		oFontDescriptor.Weight = FontWeight.BOLD;
		oFontDescriptor.Slant = FontSlant.ITALIC;
	}


	@Override
	public Rectangle getFrame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFrame(Rectangle frame) {
		// TODO Auto-generated method stub

	}

}
