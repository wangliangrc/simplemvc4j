package com.clark.app;

import org.osmdroid.ResourceProxy.string;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

public class GoogleEarthTileSource extends OnlineTileSourceBase {
    private static final String[] BASE_URL = {
            "http://khm0.google.com/kh/v=73&x=%d&y=%d&z=%d",
            "http://khm1.google.com/kh/v=73&x=%d&y=%d&z=%d",
            "http://khm2.google.com/kh/v=73&x=%d&y=%d&z=%d",
            "http://khm3.google.com/kh/v=73&x=%d&y=%d&z=%d" };
    private static final String NAME = "Google Earth";

    public GoogleEarthTileSource() {
        super(NAME, string.google_earth, 0, 20, 256, ".png", BASE_URL);
    }

    @Override
    public String getTileURLString(MapTile aTile) {
        return String.format(getBaseUrl(), aTile.getX(), aTile.getY(),
                aTile.getZoomLevel());
    }

}
