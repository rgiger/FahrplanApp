package brunner_giger.fahrplanapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import brunner_giger.fahrplanapp.Model.ConnectionSection;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<ConnectionSection> ConnectionSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        ConnectionSections = (ArrayList<ConnectionSection>)( this.getIntent().getBundleExtra("bundle").getSerializable("connections"));

        // Get the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Change Map-Type
/*
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
*/
        String city = "";

        for (int i = 0; i < ConnectionSections.size(); i++) {

            double x = Double.valueOf(ConnectionSections.get(i).Dep_Latitude);
            double y = Double.valueOf(ConnectionSections.get(i).Dep_Longitude);
            city = ConnectionSections.get(i).Departure;
            LatLng pos = new LatLng(x, y);
            mMap.addMarker(new MarkerOptions().position(pos).title(city));

        }

        double dx = Double.valueOf(ConnectionSections.get(0).From_Latitude);
        double dy = Double.valueOf(ConnectionSections.get(0).From_Longitude);
        city = ConnectionSections.get(0).From;
        LatLng pos = new LatLng(dx, dy);
        // this marker has not to be set !
//        mMap.addMarker(new MarkerOptions().position(pos).title(city));

        double tx = Double.valueOf(ConnectionSections.get(0).ToEnd_Latitude);
        double ty = Double.valueOf(ConnectionSections.get(0).ToEnd_Longitude);
        city = ConnectionSections.get(0).ToEnd;
        pos = new LatLng(tx, ty);
        mMap.addMarker(new MarkerOptions().position(pos).title(city));

        LatLng posDelta = new LatLng((double) Math.abs((dx+tx)/2), (double)Math.abs((dy+ty)/2));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posDelta));
        mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 8.0));

    }
}