package brunner_giger.fahrplanapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import brunner_giger.fahrplanapp.Model.ConnectionSection;

public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private ArrayList<ConnectionSection> ConnectionSections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        final ArrayList<ConnectionSection> ConnectionSections = (ArrayList<ConnectionSection>)( this.getActivity().getIntent().getBundleExtra("bundle").getSerializable("connections"));

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

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
                // mMap.addMarker(new MarkerOptions().position(pos).title(city));

                double tx = Double.valueOf(ConnectionSections.get(0).ToEnd_Latitude);
                double ty = Double.valueOf(ConnectionSections.get(0).ToEnd_Longitude);
                city = ConnectionSections.get(0).ToEnd;
                pos = new LatLng(tx, ty);
                mMap.addMarker(new MarkerOptions().position(pos).title(city));

                LatLng posDelta = new LatLng((double) Math.abs((dx+tx)/2), (double)Math.abs((dy+ty)/2));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(posDelta));
                mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 8.0));

/*
                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/
            }
        });

        return rootView;
    }
}