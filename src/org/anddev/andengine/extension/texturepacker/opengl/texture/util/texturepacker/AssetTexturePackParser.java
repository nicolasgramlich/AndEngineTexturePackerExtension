package org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:24:00 - 12.09.2011
 */
public class AssetTexturePackParser extends TexturePackParser {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final String mAssetBasePath;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AssetTexturePackParser(final Context pContext, final String pAssetBasePath) {
		this.mContext = pContext;
		this.mAssetBasePath = pAssetBasePath;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected InputStream onGetInputStream(final String pFilename) throws IOException {
		return this.mContext.getAssets().open(this.mAssetBasePath + pFilename);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
