package org.osmdroid.tileprovider.constants;

import java.io.File;

import android.os.Environment;

class DefaultMapTileProviderConstants implements IMapTileProviderConstants {
    private static final File OSMDROID_PATH = new File(
            Environment.getExternalStorageDirectory(), "osmdroid");
    private static final File TILE_PATH_BASE = new File(OSMDROID_PATH, "tiles");

    public static IMapTileProviderConstants getInstance() {
        return Holder.instance;
    }

    @Override
    public int getMinimumZoomlevel() {
        return 0;
    }

    @Override
    public int getMaximumZoomlevel() {
        return 22;
    }

    @Override
    public File getOsmdroidPath() {
        return OSMDROID_PATH;
    }

    @Override
    public File getTilePathBase() {
        return TILE_PATH_BASE;
    }

    @Override
    public String getTilePathExtension() {
        return ".tile";
    }

    @Override
    public int getCacheMaptileCountDefault() {
        return 9;
    }

    @Override
    public int getNumberOfTileDownloadThreads() {
        return 2;
    }

    @Override
    public int getNumberOfTileFilesystemThreads() {
        return 8;
    }

    @Override
    public long getDefaultMaximumCachedFileAge() {
        return ONE_WEEK;
    }

    @Override
    public int getTileDownloadMaximumQueueSize() {
        return 40;
    }

    @Override
    public int getTileFilesystemMaximumQueueSize() {
        return 40;
    }

    @Override
    public long getTileExpiryTimeMilliseconds() {
        /** 30 days */
        return 1000L * 60 * 60 * 24 * 30;
    }

    @Override
    public long getTileMaxCacheSizeBytes() {
        /** 600 Mb */
        return 600L * 1024 * 1024;
    }

    @Override
    public long getTileTrimCacheSizeBytes() {
        /** 500 Mb */
        return 500L * 1024 * 1024;
    }

    private DefaultMapTileProviderConstants() {
    }

    private static class Holder {
        static DefaultMapTileProviderConstants instance = new DefaultMapTileProviderConstants();
    }
}
