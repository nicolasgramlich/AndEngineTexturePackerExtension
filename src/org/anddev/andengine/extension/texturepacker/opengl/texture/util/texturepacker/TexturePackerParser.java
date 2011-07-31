package org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker;

import java.io.IOException;
import java.io.InputStream;

import org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePacker.TexturePackerResult;
import org.anddev.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackerParseException;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.Texture.PixelFormat;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;
import org.anddev.andengine.opengl.texture.compressed.pvr.PVRCCZTexture;
import org.anddev.andengine.opengl.texture.compressed.pvr.PVRGZTexture;
import org.anddev.andengine.opengl.texture.compressed.pvr.PVRTexture;
import org.anddev.andengine.opengl.texture.compressed.pvr.PVRTexture.PVRTextureFormat;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegionLibrary;
import org.anddev.andengine.util.SAXUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 5:19:26 PM - Jul 29, 2011
 */
public class TexturePackerParser extends DefaultHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String TAG_TEXTURE = "texture";
	private static final String TAG_TEXTURE_ATTRIBUTE_FILE = "file";
	private static final String TAG_TEXTURE_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_TEXTURE_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_TEXTURE_ATTRIBUTE_MINFILTER = "minfilter";
	private static final String TAG_TEXTURE_ATTRIBUTE_MAGFILTER = "magfilter";
	private static final String TAG_TEXTURE_ATTRIBUTE_WRAPX = "wrapx";
	private static final String TAG_TEXTURE_ATTRIBUTE_WRAPY = "wrapy";
	private static final String TAG_TEXTURE_ATTRIBUTE_PREMULTIPLYALPHA = "premultiplyalpha";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE = "type";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVRCCZ = "pvrccz";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVRGZ = "pvrgz";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVR = "pvr";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PNG = "png";
	private static final String TAG_TEXTURE_ATTRIBUTE_PIXELFORMAT = "pixelformat";

	private static final String TAG_TEXTUREREGION = "textureregion";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_ID = "id";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_X = "x";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_Y = "y";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_ROTATED = "rotated";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_TRIMMED = "trimmed";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE = "src";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_X = "srcx";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_Y = "srcy";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_WIDTH = "srcwidth";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_HEIGHT = "srcheight";

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;

	private TexturePackerResult mTexturePackerResult;
	private final String mAssetBasePath;
	private TextureRegionLibrary mTextureRegionLibrary;
	private ITexture mTexture;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePackerParser(final Context pContext, final String pAssetBasePath) {
		this.mContext = pContext;
		this.mAssetBasePath = pAssetBasePath;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public TexturePackerResult getTexturePackerResult() {
		return this.mTexturePackerResult;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void startElement(final String pUri, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
		if(pLocalName.equals(TexturePackerParser.TAG_TEXTURE)) {
			final String file = SAXUtils.getAttributeOrThrow(pAttributes, TexturePackerParser.TAG_TEXTURE_ATTRIBUTE_FILE);
			final String type = SAXUtils.getAttributeOrThrow(pAttributes, TexturePackerParser.TAG_TEXTURE_ATTRIBUTE_TYPE);
			final PixelFormat pixelFormat = this.parsePixelFormat(pAttributes);

			final TextureOptions textureOptions = this.parseTextureOptions(pAttributes);

			this.mTexture = this.parseTexture(file, type, pixelFormat, textureOptions, pAttributes);
			this.mTextureRegionLibrary = new TextureRegionLibrary(10);

			this.mTexturePackerResult = new TexturePackerResult(this.mTexture, this.mTextureRegionLibrary);
		} else if(pLocalName.equals(TexturePackerParser.TAG_TEXTUREREGION)) {
			final int id = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackerParser.TAG_TEXTUREREGION_ATTRIBUTE_ID);
			final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackerParser.TAG_TEXTUREREGION_ATTRIBUTE_X);
			final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackerParser.TAG_TEXTUREREGION_ATTRIBUTE_Y);
			final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackerParser.TAG_TEXTUREREGION_ATTRIBUTE_WIDTH);
			final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackerParser.TAG_TEXTUREREGION_ATTRIBUTE_HEIGHT);

			this.mTextureRegionLibrary.put(id, TextureRegionFactory.extractFromTexture(this.mTexture, x, y, width, height, true));
		} else {
			throw new TexturePackerParseException("Unexpected end tag: '" + pLocalName + "'.");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private PixelFormat parsePixelFormat(final Attributes pAttributes) {
		return PixelFormat.valueOf(SAXUtils.getAttributeOrThrow(pAttributes, TexturePackerParser.TAG_TEXTURE_ATTRIBUTE_PIXELFORMAT));
	}

	private TextureOptions parseTextureOptions(final Attributes pAttributes) {
		// TODO Actually parse the TextureOptions
		return TextureOptions.DEFAULT;
	}

	private ITexture parseTexture(final String pFile, final String pType, final PixelFormat pPixelFormat, final TextureOptions pTextureOptions, final Attributes pAttributes) throws TexturePackerParseException {
		// TODO Parse all supported Types
		if(pType.equals(TexturePackerParser.TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PNG)) {
			try {
				return new BitmapTexture(BitmapTextureFormat.fromPixelFormat(pPixelFormat), pTextureOptions) {
					@Override
					protected InputStream onGetInputStream() throws IOException {
						return TexturePackerParser.this.mContext.getAssets().open(TexturePackerParser.this.mAssetBasePath + pFile);
					}
				};
			} catch (final IOException e) {
				throw new TexturePackerParseException(e);
			}
		} else if(pType.equals(TexturePackerParser.TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVR)) {
			try {
				return new PVRTexture(PVRTextureFormat.fromPixelFormat(pPixelFormat), pTextureOptions) {
					@Override
					protected InputStream onGetInputStream() throws IOException {
						return TexturePackerParser.this.mContext.getAssets().open(TexturePackerParser.this.mAssetBasePath + pFile);
					}
				};
			} catch (final IOException e) {
				throw new TexturePackerParseException(e);
			}
		} else if(pType.equals(TexturePackerParser.TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVRGZ)) {
			try {
				return new PVRGZTexture(PVRTextureFormat.fromPixelFormat(pPixelFormat), pTextureOptions) {
					@Override
					protected InputStream onGetInputStream() throws IOException {
						return TexturePackerParser.this.mContext.getAssets().open(TexturePackerParser.this.mAssetBasePath + pFile);
					}
				};
			} catch (final IOException e) {
				throw new TexturePackerParseException(e);
			}
		} else if(pType.equals(TexturePackerParser.TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVRCCZ)) {
			try {
				return new PVRCCZTexture(PVRTextureFormat.fromPixelFormat(pPixelFormat), pTextureOptions) {
					@Override
					protected InputStream onGetInputStream() throws IOException {
						return TexturePackerParser.this.mContext.getAssets().open(TexturePackerParser.this.mAssetBasePath + pFile);
					}
				};
			} catch (final IOException e) {
				throw new TexturePackerParseException(e);
			}
		} else {
			throw new TexturePackerParseException(new IllegalArgumentException("Unsupported pTextureFormat: '" + pType + "'."));
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
