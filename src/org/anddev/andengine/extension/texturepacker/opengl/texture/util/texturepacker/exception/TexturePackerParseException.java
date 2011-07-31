package org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception;

import org.xml.sax.SAXException;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 5:29:20 PM - Jul 29, 2011
 */
public class TexturePackerParseException extends SAXException {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 5773816582330137037L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePackerParseException() {
		super();
	}

	public TexturePackerParseException(final String pDetailMessage) {
		super(pDetailMessage);
	}

	public TexturePackerParseException(final Exception pException) {
		super(pException);
	}

	public TexturePackerParseException(final String pMessage, final Exception pException) {
		super(pMessage, pException);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
