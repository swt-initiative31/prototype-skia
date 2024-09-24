package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.uno.*;

import com.sun.star.awt.*;

public class Text extends Scrollable {

	private final UnoTextControl handle;
	private Font font;

	public Text(Composite parent, int style) {
		super(parent, style);
		handle = new UnoTextControl(parent.getHandle(), this);

	}

	@Override
	protected UnoControl getHandle() {
		return handle;
	}

	public String getText() {
		return handle.getText();
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	public Point getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTextLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getEditable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFocusControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Font getFont() {

		FontDescriptor fd = handle.getFontDescriptor();

		FontData d = new FontData();
		d.setStyle(this.style);
		d.setHeight(fd.Height);

		font = new Font(display, new FontData[]{d});

		return font;
	}

	@Override
	public Menu getMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setText(String textValue) {
		handle.setText(textValue);

	}

	@Override
	public void setToolTipText(String tooltip) {
		// TODO Auto-generated method stub

	}

	public void setSelection(Point selection) {
		// TODO Auto-generated method stub

	}

	public void setTextLimit(int limit) {
		// TODO Auto-generated method stub

	}

	public void setEditable(boolean editable) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean setFocus() {
		// TODO Auto-generated method stub

		return false;

	}

	@Override
	public void setMenu(Menu menu) {
		// TODO Auto-generated method stub

	}

	public void clearSelection() {
		// TODO Auto-generated method stub

	}

	public void cut() {
		// TODO Auto-generated method stub

	}

	public int getLineHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void selectAll() {
		// TODO Auto-generated method stub

	}

	public void paste() {
		// TODO Auto-generated method stub

	}

	public void setSelection(int x, int y) {
		// TODO Auto-generated method stub

	}

	public Point getCaretLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean traverse(int event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void copy() {
		// TODO Auto-generated method stub

	}

	public int getCaretPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addModifyListener (ModifyListener listener) {
		addTypedListener(listener, SWT.Modify);
	}

}
