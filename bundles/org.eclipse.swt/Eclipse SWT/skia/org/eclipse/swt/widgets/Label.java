package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.uno.*;

/**
 * Instances of this class represent a non-selectable user interface object that
 * displays a string or image. When SEPARATOR is specified, displays a single
 * vertical or horizontal line.
 * <p>
 * Shadow styles are hints and may not be honored by the platform. To create a
 * separator label with the default shadow style for the platform, do not
 * specify a shadow style.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SEPARATOR, HORIZONTAL, VERTICAL</dd>
 * <dd>SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dd>CENTER, LEFT, RIGHT, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of SHADOW_IN, SHADOW_OUT and SHADOW_NONE may be specified.
 * SHADOW_NONE is a HINT. Only one of HORIZONTAL and VERTICAL may be specified.
 * Only one of CENTER, LEFT and RIGHT may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#label">Label snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Label extends Control {
	String text = "";
	Image image;
	boolean isImageMode; // Resolves ambiguity when both image and text are set
	static final int MARGIN = 4;
	private final UnoLabelControl unoLabel;

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
	 * @see SWT#SEPARATOR
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see SWT#SHADOW_IN
	 * @see SWT#SHADOW_OUT
	 * @see SWT#SHADOW_NONE
	 * @see SWT#CENTER
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#WRAP
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Label(Composite parent, int style) {
		super(parent, checkStyle(style));
		this.unoLabel = new UnoLabelControl(parent.getHandle());
	}

	static int checkStyle(int style) {
		style |= SWT.NO_FOCUS;
		if ((style & SWT.SEPARATOR) != 0) {
			style = checkBits(style, SWT.VERTICAL, SWT.HORIZONTAL, 0, 0, 0, 0);
			return checkBits(style, SWT.SHADOW_OUT, SWT.SHADOW_IN, SWT.SHADOW_NONE, 0, 0, 0);
		}
		return checkBits(style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	}

	@Override
	Point computeSizeInPixels(int wHint, int hHint, boolean changed) {
		checkWidget();
		int width = 0, height = 0, border = getBorderWidthInPixels();
		if ((style & SWT.SEPARATOR) != 0) {
			int lineWidth = 0; // TODO OS implementation necessary
			if ((style & SWT.HORIZONTAL) != 0) {
				width = DEFAULT_WIDTH;
				height = lineWidth * 2;
			} else {
				width = lineWidth * 2;
				height = DEFAULT_HEIGHT;
			}
			if (wHint != SWT.DEFAULT)
				width = wHint;
			if (hHint != SWT.DEFAULT)
				height = hHint;
			width += border * 2;
			height += border * 2;
			return new Point(width, height);
		}
		if (isImageMode) {
			Rectangle rect = DPIUtil.autoScaleBounds(image.getBounds(), this.getZoom(), 100);
			width += rect.width;
			height += rect.height;
		} else {
// TODO: UNO implementation??

		}
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		width += border * 2;
		height += border * 2;
		return new Point(width, height);
	}

	private int getZoom() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	void createHandle() {
		super.createHandle();
		state |= THEME_BACKGROUND;
	}

	/**
	 * Returns a value which describes the position of the text or image in the
	 * receiver. The value will be one of <code>LEFT</code>, <code>RIGHT</code> or
	 * <code>CENTER</code> unless the receiver is a <code>SEPARATOR</code> label, in
	 * which case, <code>NONE</code> is returned.
	 *
	 * @return the alignment
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getAlignment() {
		checkWidget();
		if ((style & SWT.SEPARATOR) != 0)
			return 0;
		if ((style & SWT.LEFT) != 0)
			return SWT.LEFT;
		if ((style & SWT.CENTER) != 0)
			return SWT.CENTER;
		if ((style & SWT.RIGHT) != 0)
			return SWT.RIGHT;
		return SWT.LEFT;
	}

	/**
	 * Returns the receiver's image if it has one, or null if it does not.
	 *
	 * @return the receiver's image
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Image getImage() {
		checkWidget();
		return image;
	}

	@Override
	String getNameText() {
		return getText();
	}

	/**
	 * Returns the receiver's text, which will be an empty string if it has never
	 * been set or if the receiver is a <code>SEPARATOR</code> label.
	 *
	 * @return the receiver's text
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public String getText() {
		checkWidget();
		if ((style & SWT.SEPARATOR) != 0)
			return "";
		return text;
	}

	/**
	 * Controls how text and images will be displayed in the receiver. The argument
	 * should be one of <code>LEFT</code>, <code>RIGHT</code> or
	 * <code>CENTER</code>. If the receiver is a <code>SEPARATOR</code> label, the
	 * argument is ignored and the alignment is not changed.
	 *
	 * @param alignment the new alignment
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
	public void setEnabled(boolean enabled) {

		super.setEnabled(enabled);
	}

	/**
	 * Sets the receiver's text.
	 * <p>
	 * This method sets the widget label. The label may include the mnemonic
	 * character and line delimiters.
	 * </p>
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to be
	 * the mnemonic. When the user presses a key sequence that matches the mnemonic,
	 * focus is assigned to the control that follows the label. On most platforms,
	 * the mnemonic appears underlined but may be emphasised in a platform specific
	 * manner. The mnemonic indicator character '&amp;' can be escaped by doubling
	 * it in the string, causing a single '&amp;' to be displayed.
	 * </p>
	 * <p>
	 * Note: If control characters like '\n', '\t' etc. are used in the string, then
	 * the behavior is platform dependent.
	 * </p>
	 *
	 * @param string the new text
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the text is
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
	public void setText(String text) {
		checkWidget();
		this.text = text;
		_setText();
	}

	void updateStyleBits(boolean isEnabled) {
		// TODO: UNO implementation
	}

	@Override
	protected UnoLabelControl getHandle() {
		return unoLabel;

	}

	/**
	 * Sets the receiver's image to the argument, which may be null indicating that
	 * no image should be displayed.
	 *
	 * @param image the image to display on the receiver (may be null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the image
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
	public void setImage(Image image) {
		checkWidget();
		if ((style & SWT.SEPARATOR) != 0)
			return;
		if (image != null) {
			if (image.isDisposed())
				error(SWT.ERROR_INVALID_ARGUMENT);

			this.image = image;
			isImageMode = true;
//			NSImage current = imageView.image();
//			if (current != null && current.id == image.handle.id) {
//				imageView.setImage(null);
//			}
//			imageView.setImage(image.handle);
//			((NSBox) view).setContentView(imageView);
		} else {
			if (this.image == null)
				return; // do nothing if image is already null

			this.image = image;
//			imageView.setImage(null);
			_setText();
		}
	}

	private void _setText() {
		getHandle().setText(text);
	}
}