package org.eclipse.swt.graphics;

import org.eclipse.swt.printing.*;

public class TextLayout extends Resource {

	public TextLayout(Device device) {
		// TODO Auto-generated constructor stub
	}

	public TextLayout(Printer printer) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getNextOffset(int offset, int movementCluster) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPreviousOffset(int nextOffset, int movementCluster) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setText(String t) {
		// TODO Auto-generated method stub

	}

	public void setFont(Font printerFont) {
		// TODO Auto-generated method stub

	}

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLineCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Rectangle getLineBounds(int lineCount) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSpacing() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void draw(GC gc, int drawX, int drawY) {
		// TODO Auto-generated method stub

	}

	public FontMetrics getLineMetrics(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAscent(int i) {
		// TODO Auto-generated method stub

	}

	public void setDescent(int descent) {
		// TODO Auto-generated method stub

	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setWidth(int i) {
		// TODO Auto-generated method stub

	}

	public TextStyle getStyle(int cap) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getRanges() {
		// TODO Auto-generated method stub
		return null;
	}

	public TextStyle[] getStyles() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getVerticalIndent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLineIndex(int offsetInLine) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getIndent() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLevel(int offsetInLine) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int[] getLineOffsets() {
		// TODO Auto-generated method stub
		return null;
	}

	public Point getLocation(int offsetInLine, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getOffset(int x, int y, int[] trailing) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Rectangle getBounds(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}


	public void draw(GC gc, int paintX, int paintY, int start, int i, Color selectionFg, Color selectionBg, int flags) {
		// TODO Auto-generated method stub

	}

	public void setSpacing(int newLineSpacing) {
		// TODO Auto-generated method stub

	}

	public void setFixedLineMetrics(FontMetrics fixedLineMetrics) {
		// TODO Auto-generated method stub

	}

	public void setOrientation(int orientation) {
		// TODO Auto-generated method stub

	}

	public void setSegments(int[] segments) {
		// TODO Auto-generated method stub

	}

	public void setSegmentsChars(char[] segmentChars) {
		// TODO Auto-generated method stub

	}

		// TODO Auto-generated method stub
	public void setTabs(int[] tabs) {

	}

	public void setDefaultTabWidth(int tabLength) {
		// TODO Auto-generated method stub

	}

	public void setIndent(int indent) {
		// TODO Auto-generated method stub

	}

	public void setVerticalIndent(int verticalIndent) {
		// TODO Auto-generated method stub

	}

	public void setWrapIndent(int wrapIndent) {
		// TODO Auto-generated method stub

	}

	public void setAlignment(int alignment) {
		// TODO Auto-generated method stub

	}

	public void setJustify(boolean justify) {
		// TODO Auto-generated method stub

	}

	public void setTextDirection(int textDirection) {
		// TODO Auto-generated method stub

	}

	public void setStyle(TextStyle style, int start, int end) {
		// TODO Auto-generated method stub

	}

}
