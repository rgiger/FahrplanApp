package brunner_giger.fahrplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import brunner_giger.fahrplanapp.Adapter.ConnectionAdapter;
import brunner_giger.fahrplanapp.Adapter.ConnectionDetailsAdapter;
import ch.schoeb.opendatatransport.model.Connection;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


/*
        Bundle arguments = new Bundle();
        DetailsActivityFragment fragment = new DetailsActivityFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.listConnectionDetails, fragment)
                .commit();

/*
        Connection connection = new Connection();
        ConnectionDetailsAdapter adapter = new ConnectionDetailsAdapter(getApplicationContext(), connection);

        ListView listConnectionDetails = (ListView) findViewById(R.id.listConnectionDetails);
        //ListView listDetails = (ListView) DetailsActivity.findViewById(R.id.listConnectionDetails);
        listConnectionDetails.setAdapter(adapter);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, FahrplanFragment.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
