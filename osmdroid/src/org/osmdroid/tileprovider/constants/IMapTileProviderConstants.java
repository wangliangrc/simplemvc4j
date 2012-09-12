package org.osmdroid.tileprovider.constants;

import java.io.File;

/**
 * 
 * This class contains constants used by the tile provider.
 * 
 * @author Neil Boyd
 * 
 */
public interface IMapTileProviderConstants {
    public static final boolean DEBUGMODE = false;

    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final long ONE_DAY = ONE_HOUR * 24;
    public static final long ONE_WEEK = ONE_DAY * 7;
    public static final long ONE_YEAR = ONE_DAY * 365;

    /** Minimum Zoom Level */
    int getMinimumZoomlevel();

    /**
     * Maximum Zoom Level - we use Integers to store zoom levels so overflow
     * happens at 2^32 - 1, but we also have a tile size that is typically 2^8,
     * so (32-1)-8-1 = 22
     */
    int getMaximumZoomlevel();

    /** Base path for osmdroid files. Zip files are in this folder. */
    File getOsmdroidPath();

    /** Base path for tiles. */
    File getTilePathBase();

    /** add an extension to files on sdcard so that gallery doesn't index them */
    String getTilePathExtension();

    /**
     * Initial tile cache size. The size will be increased as required by
     * calling {@link LRUMapTileCache.ensureCapacity(int)} The tile cache will
     * always be at least 3x3.
     */
    int getCacheMaptileCountDefault();

    /**
     * number of tile download threads, conforming to OSM policy:
     * http://wiki.openstreetmap.org/wiki/Tile_usage_policy
     */
    int getNumberOfTileDownloadThreads();

    int getNumberOfTileFilesystemThreads();

    long getDefaultMaximumCachedFileAge();

    int getTileDownloadMaximumQueueSize();

    int getTileFilesystemMaximumQueueSize();

    long getTileExpiryTimeMilliseconds();

    long getTileMaxCacheSizeBytes();

    long getTileTrimCacheSizeBytes();
}
