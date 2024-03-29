// Created by plusminus on 21:46:41 - 25.09.2008
package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.constants.IMapTileProviderConstants;
import org.osmdroid.tileprovider.constants.MapTileProviderConstantsFactory;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.drawable.Drawable;

/**
 * A tile provider that can serve tiles from an archive using the supplied tile
 * source. The tile provider will automatically find existing archives and use
 * each one that it finds.
 * 
 * @author Marc Kurtz
 * @author Nicolas Gramlich
 * 
 */
public class MapTileFileArchiveProvider extends MapTileFileStorageProviderBase {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final Logger logger = LoggerFactory
            .getLogger(MapTileFileArchiveProvider.class);

    private static IMapTileProviderConstants tileProviderConstants = MapTileProviderConstantsFactory
            .getDefault();

    // ===========================================================
    // Fields
    // ===========================================================

    private final ArrayList<IArchiveFile> mArchiveFiles = new ArrayList<IArchiveFile>();

    protected ITileSource mTileSource;

    /** Disable the search of archives if specified in constructor */
    private final boolean mSpecificArchivesProvided;

    // ===========================================================
    // Constructors
    // ===========================================================

    /**
     * The tiles may be found on several media. This one works with tiles stored
     * on the file system. It and its friends are typically created and
     * controlled by {@link MapTileProviderBase}.
     */
    public MapTileFileArchiveProvider(
            final IRegisterReceiver pRegisterReceiver,
            final ITileSource pTileSource, final IArchiveFile[] pArchives) {
        super(pRegisterReceiver, tileProviderConstants
                .getNumberOfTileFilesystemThreads(), tileProviderConstants
                .getTileFilesystemMaximumQueueSize());

        setTileSource(pTileSource);

        if (pArchives == null) {
            mSpecificArchivesProvided = false;
            findArchiveFiles();
        } else {
            mSpecificArchivesProvided = true;
            for (int i = pArchives.length - 1; i >= 0; i--) {
                mArchiveFiles.add(pArchives[i]);
            }
        }

    }

    public MapTileFileArchiveProvider(
            final IRegisterReceiver pRegisterReceiver,
            final ITileSource pTileSource) {
        this(pRegisterReceiver, pTileSource, null);
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
        return "File Archive Provider";
    }

    @Override
    protected String getThreadGroupName() {
        return "filearchive";
    }

    @Override
    protected Runnable getTileLoader() {
        return new TileLoader();
    }

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
    protected void onMediaMounted() {
        if (!mSpecificArchivesProvided) {
            findArchiveFiles();
        }
    }

    @Override
    protected void onMediaUnmounted() {
        if (!mSpecificArchivesProvided) {
            findArchiveFiles();
        }
    }

    @Override
    public void setTileSource(final ITileSource pTileSource) {
        ITileSource curTileSource = mTileSource;
        if(!pTileSource.equals(curTileSource)) {
            mTileSource = pTileSource;
            if (!mSpecificArchivesProvided) {
                findArchiveFiles();
            }
        }
    }

    @Override
    public void detach() {
        clearArchiveFiles();
        super.detach();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void findArchiveFiles() {
        clearArchiveFiles();

        if (!getSdCardAvailable()) {
            return;
        }

        // path should be optionally configurable
        final File[] files = tileProviderConstants.getOsmdroidPath()
                .listFiles();
        if (files != null) {
            for (final File file : files) {
                final IArchiveFile archiveFile = ArchiveFileFactory
                        .getArchiveFile(file);
                if (archiveFile != null) {
                    mArchiveFiles.add(archiveFile);
                }
            }
        }
    }

    private void clearArchiveFiles() {
        if(mArchiveFiles != null && mArchiveFiles.size() > 0) {
            for(IArchiveFile archiveFile : mArchiveFiles) {
                archiveFile.close();
            }
            mArchiveFiles.clear();
        }
    }

    private synchronized InputStream getInputStream(final MapTile pTile) {
        for (final IArchiveFile archiveFile : mArchiveFiles) {
            final InputStream in = archiveFile.getInputStream(mTileSource,
                    pTile);
            if (in != null) {
                if (IMapTileProviderConstants.DEBUGMODE) {
                    logger.debug("Found tile " + pTile + " in " + archiveFile);
                }
                return in;
            }
        }

        return null;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private class TileLoader extends MapTileModuleProviderBase.TileLoader {

        @Override
        public Drawable loadTile(final MapTileRequestState pState) {

            if (mTileSource == null) {
                return null;
            }

            final MapTile pTile = pState.getMapTile();

            // if there's no sdcard then don't do anything
            if (!getSdCardAvailable()) {
                if (IMapTileProviderConstants.DEBUGMODE) {
                    logger.debug("No sdcard - do nothing for tile: " + pTile);
                }
                return null;
            }

            InputStream inputStream = null;
            try {
                if (IMapTileProviderConstants.DEBUGMODE) {
                    logger.debug("Tile doesn't exist: " + pTile);
                }

                inputStream = getInputStream(pTile);
                if (inputStream != null) {
                    if (IMapTileProviderConstants.DEBUGMODE) {
                        logger.debug("Use tile from archive: " + pTile);
                    }
                    final Drawable drawable = mTileSource
                            .getDrawable(inputStream);
                    return drawable;
                }
            } catch (final Throwable e) {
                logger.error("Error loading tile", e);
            } finally {
                if (inputStream != null) {
                    StreamUtils.closeStream(inputStream);
                }
            }

            return null;
        }
    }
}
