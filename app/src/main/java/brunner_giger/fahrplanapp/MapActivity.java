package brunner_giger.fahrplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import brunner_giger.fahrplanapp.Model.ConnectionSection;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<ConnectionSection> ConnectionSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        ConnectionSections = (ArrayList<ConnectionSection>) (this.getIntent().getBundleExtra("bundle").getSerializable("connections"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.maptoolbar);
        toolbar.setTitle(R.string.ConnectionDetails);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final RadioGroup mapRadioGroup = (RadioGroup) findViewById(R.id.rbgMap);
        mapRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected and change Map-Type
                if (checkedId == R.id.rbNormal) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (checkedId == R.id.rbTerrain) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else if (checkedId == R.id.rbSatellite) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (checkedId == R.id.rbHybrid) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            }
        });
    }

    @Override
    public void onMapReady (GoogleMap googleMap){
        mMap = googleMap;

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
        LatLng posF = new LatLng(dx, dy);
        // this marker has not to be set !
        //        mMap.addMarker(new MarkerOptions().position(pos).title(city));

        double tx = Double.valueOf(ConnectionSections.get(0).ToEnd_Latitude);
        double ty = Double.valueOf(ConnectionSections.get(0).ToEnd_Longitude);
        city = ConnectionSections.get(0).ToEnd;
        LatLng posT = new LatLng(tx, ty);
        mMap.addMarker(new MarkerOptions().position(posT).title(city));

        LatLng posDelta = new LatLng(Math.abs((dx + tx) / 2), Math.abs((dy + ty) / 2));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posDelta));

        // Define the most S/W and N/E coordinates
        LatLng posSW = new LatLng(Math.min(posF.latitude, posT.latitude), Math.min(posF.longitude, posT.longitude));
        LatLng posNE = new LatLng(Math.max(posF.latitude, posT.latitude), Math.max(posF.longitude, posT.longitude));

        LatLngBounds llb = new LatLngBounds(posSW, posNE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(llb, 200));

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Up button
            navigateUpTo(new Intent(this, FahrplanFragment.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
