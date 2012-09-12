package com.clark.app;

import org.osmdroid.ResourceProxy.string;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

public class GoogleTerrainTileSource extends OnlineTileSourceBase {
    private static final String[] BASE_URL = {
            "http://mt0.google.com/vt/lyrs=t@125,r@135&hl=zh-CN&gl=cn&x=%d&y=%d&z=%d",
            "http://mt1.google.com/vt/lyrs=t@125,r@135&hl=zh-CN&gl=cn&x=%d&y=%d&z=%d",
            "http://mt2.google.com/vt/lyrs=t@125,r@135&hl=zh-CN&gl=cn&x=%d&y=%d&z=%d",
            "http://mt3.google.com/vt/lyrs=t@125,r@135&hl=zh-CN&gl=cn&x=%d&y=%d&z=%d" };
    private static final String NAME = "Google Terrain";

    public GoogleTerrainTileSource() {
        super(NAME, string.google_terrain, 0, 15, 256, ".png", BASE_URL);
    }

    @Override
    public String getTileURLString(MapTile aTile) {
        return String.format(getBaseUrl(), aTile.getX(), aTile.getY(),
                aTile.getZoomLevel());
    }

}
