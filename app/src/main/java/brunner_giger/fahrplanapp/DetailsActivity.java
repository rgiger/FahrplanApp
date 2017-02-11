package brunner_giger.fahrplanapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;

import brunner_giger.fahrplanapp.Model.ConnectionSection;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.ConnectionDetails);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check the installed Google maps version
        String gmv = "";
        try {
            gmv = this.getPackageManager().getPackageInfo("com.google.android.gms",0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // Version on A5: 10.2.98 (438-146496160)
        // compiled version - com.google.android.gms:play-services:10.0.1
        while (gmv.lastIndexOf(".") > 2) {
            gmv = gmv.substring(0, gmv.lastIndexOf("."));
        }
        double dVer = 0.0;
        try {
            dVer = Double.valueOf(gmv);
        } catch (Exception e) {

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMap);
        // hide FloatingAction Button if Version is smaller than 10.0
        if (dVer < 10.0) {
            fab.setVisibility(View.GONE);
        } else {
            final ArrayList<ConnectionSection> ConnectionSections = (ArrayList<ConnectionSection>) (this.getIntent().getBundleExtra("bundle").getSerializable("connections"));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Context context = view.getContext();
                    Intent intent = new Intent(context, MapActivity.class);

                    Bundle b = new Bundle();
                    b.putSerializable("connections", ConnectionSections);
                    intent.putExtra("bundle", b);
                    context.startActivity(intent);


                    // TODO: 11.02.2017 Hiermit wird eine einfache Map angezeigt dafür in der Activity mit entsprechender Toolbar
                    Fragment fragment = null;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragment = new MapFragment();
                    fragment.setArguments(b);
                    // wenn dies auskommentiert wird, wird die Map angezeigt
//                    fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Up button
            navigateUpTo(new Intent(this, FahrplanFragment.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
