package tn.esprit.pdm.Map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle

@Composable
fun MapScreen() {
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                center(Point.fromLngLat(24.9384, 60.1699))
                zoom(9.0)
            }
        },
        style = {
            MapStyle(style = Style.SATELLITE_STREETS)
        }
    )
}
