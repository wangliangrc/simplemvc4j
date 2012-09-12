package org.osmdroid.tileprovider.constants;

public final class MapTileProviderConstantsFactory {
    private static IMapTileProviderConstants defaultTileProviderConstants = DefaultMapTileProviderConstants
            .getInstance();

    public static void setDefault(
            IMapTileProviderConstants tileProviderConstants) {
        if (tileProviderConstants == null)
            return;
        defaultTileProviderConstants = tileProviderConstants;
    }

    public static IMapTileProviderConstants getDefault() {
        return defaultTileProviderConstants;
    }
}
