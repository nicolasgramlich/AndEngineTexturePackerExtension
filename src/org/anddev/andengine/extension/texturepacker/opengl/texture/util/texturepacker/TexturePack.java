package org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker;

import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.TextureRegionLibrary;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:23:47 - 30.07.2011
 */
public class TexturePack {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ITexture mTexture;
	private final TextureRegionLibrary mTextureRegionLibrary;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePack(final ITexture pTexture, final TextureRegionLibrary pTextureRegionLibrary) {
		this.mTexture = pTexture;
		this.mTextureRegionLibrary = pTextureRegionLibrary;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ITexture getTexture() {
		return this.mTexture;
	}

	public TextureRegionLibrary getTextureRegionLibrary() {
		return this.mTextureRegionLibrary;
	}

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