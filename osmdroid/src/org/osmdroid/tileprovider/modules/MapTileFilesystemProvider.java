package org.osmdroid.tileprovider.modules;

import java.io.File;

import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.constants.IMapTileProviderConstants;
import org.osmdroid.tileprovider.constants.MapTileProviderConstantsFactory;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase.LowMemoryException;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.drawable.Drawable;

/**
 * Implements a file system cache and provides cached tiles. This functions as a
 * tile provider by serving cached tiles for the supplied tile source.
 * 
 * @author Marc Kurtz
 * @author Nicolas Gramlich
 * 
 */
public class MapTileFilesystemProvider extends MapTileFileStorageProviderBase {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final Logger logger = LoggerFactory
            .getLogger(MapTileFilesystemProvider.class);

    private static IMapTileProviderConstants tileProviderConstants = MapTileProviderConstantsFactory
            .getDefault();

    // ===========================================================
    // Fields
    // ===========================================================

    private final long mMaximumCachedFileAge;

    private ITileSource mTileSource;

    // ===========================================================
    // Constructors
    // ===========================================================

    public MapTileFilesystemProvider(final IRegisterReceiver pRegisterReceiver) {
        this(pRegisterReceiver, TileSourceFactory.DEFAULT_TILE_SOURCE);
    }

    public MapTileFilesystemProvider(final IRegisterReceiver pRegisterReceiver,
            final ITileSource aTileSource) {
        this(pRegisterReceiver, aTileSource, tileProviderConstants
                .getDefaultMaximumCachedFileAge());
    }

    /**
     * Provides a file system based cache tile provider. Other providers can
     * register and store data in the cache.
     * 
     * @param pRegisterReceiver
     */
    public MapTileFilesystemProvider(final IRegisterReceiver pRegisterReceiver,
            final ITileSource pTileSource, final long pMaximumCachedFileAge) {
        super(pRegisterReceiver, tileProviderConstants
                .getNumberOfTileFilesystemThreads(), tileProviderConstants
                .getTileFilesystemMaximumQueueSize());
        mTileSource = pTileSource;

        mMaximumCachedFileAge = pMaximumCachedFileAge;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass/Interfaces
    // ===========================================================

    @Override
    public boolean getUsesDataConnection() {
        return false;
    }

    @Override
    protected String getName() {
        return "File System Cache Provider";
    }

    @Override
    protected String getThreadGroupName() {
        return "filesystem";
    }

    @Override
    protected Runnable getTileLoader() {
        return new TileLoader();
    };

    @Override
    public int getMinimumZoomLevel() {
        return mTileSource != null ? mTileSource.getMinimumZoomLevel()
                : tileProviderConstants.getMinimumZoomlevel();
    }

    @Override
    public int getMaximumZoomLevel() {
        return mTileSource != null ? mTileSource.getMaximumZoomLevel()
                : tileProviderConstants.getMaximumZoomlevel();
    }

    @Override
    public void setTileSource(final ITileSource pTileSource) {
        mTileSource = pTileSource;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private class TileLoader extends MapTileModuleProviderBase.TileLoader {

        @Override
        public Drawable loadTile(final MapTileRequestState pState)
                throws CantContinueException {

            if (mTileSource == null) {
                return null;
            }

            final MapTile tile = pState.getMapTile();

            // if there's no sdcard then don't do anything
            if (!getSdCardAvailable()) {
                if (IMapTileProviderConstants.DEBUGMODE) {
                    logger.debug("No sdcard - do nothing for tile: " + tile);
                }
                return null;
            }

            // Check the tile source to see if its file is available and if so,
            // then render the
            // drawable and return the tile
            final File file = new File(tileProviderConstants.getTilePathBase(),
                    mTileSource.getTileRelativeFilenameString(tile)
                            + tileProviderConstants.getTilePathExtension());
            if (file.exists()) {

                try {
                    final Drawable drawable = mTileSource.getDrawable(file
                            .getPath());

                    // Check to see if file has expired
                    final long now = System.currentTimeMillis();
                    final long lastModified = file.lastModified();
                    final boolean fileExpired = lastModified < now
                            - mMaximumCachedFileAge;

                    if (fileExpired) {
                        if (IMapTileProviderConstants.DEBUGMODE) {
                            logger.debug("Tile expired: " + tile);
                        }
                        drawable.setState(new int[] { ExpirableBitmapDrawable.EXPIRED });
                    }

                    return drawable;
                } catch (final LowMemoryException e) {
                    // low memory so empty the queue
                    logger.warn("LowMemoryException downloading MapTile: "
                            + tile + " : " + e);
                    throw new CantContinueException(e);
                }
            }

            // If we get here then there is no file in the file cache
            return null;
        }
    }
}
