package org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackerParseException;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.TextureRegionLibrary;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 5:05:15 PM - Jul 29, 2011
 */
public class TexturePacker {
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

	public TexturePacker(final Context pContext) {
		this(pContext, "");
	}

	public TexturePacker(final Context pContext, final String pAssetBasePath) {
		this.mContext = pContext;
		this.mAssetBasePath = pAssetBasePath;
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

	public TexturePackerResult loadFromAsset(final Context pContext, final String pAssetPath) throws TexturePackerParseException {
		try {
			return this.load(pContext.getAssets().open(this.mAssetBasePath + pAssetPath));
		} catch (final IOException e) {
			throw new TexturePackerParseException("Could not load " + this.getClass().getSimpleName() + " data from asset: " + pAssetPath, e);
		}
	}

	public TexturePackerResult load(final InputStream pInputStream) throws TexturePackerParseException {
		try{
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();
			final TexturePackerParser texturePackerParser = new TexturePackerParser(this.mContext, this.mAssetBasePath);
			xr.setContentHandler(texturePackerParser);

			xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

			return texturePackerParser.getTexturePackerResult();
		} catch (final SAXException e) {
			throw new TexturePackerParseException(e);
		} catch (final ParserConfigurationException pe) {
			/* Doesn't happen. */
			return null;
		} catch (final IOException e) {
			throw new TexturePackerParseException(e);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TexturePackerResult {
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

		public TexturePackerResult(final ITexture pTexture, final TextureRegionLibrary pTextureRegionLibrary) {
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
}
