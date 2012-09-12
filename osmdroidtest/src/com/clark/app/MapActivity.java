package com.clark.app;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.MapView;

import android.app.Activity;
import android.os.Bundle;

public class MapActivity extends Activity implements MapListener {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = new MapView(getApplicationContext(), 256);
        setContentView(mapView);
        mapView.setMapListener(this);
        mapView.setBuiltInZoomControls(true);
        mapView.setKeepScreenOn(true);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(true);

        mapView.setTileSource(new GoogleChinaTileSource());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.setMapListener(null);
    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        return false;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        return false;
    }
}
