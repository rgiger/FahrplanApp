package brunner_giger.fahrplanapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.Station;

/**
 * Created by r.giger on 21.01.2017.
 */

public class FahrplanFragment extends Fragment {
    private static final int THRESHOLD = 2;
    View FahrplanView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        FahrplanView =  inflater.inflate(R.layout.content_fahrplan, container, false);
        Button btn = (Button) FahrplanView.findViewById(R.id.btnVerbindungSuchen);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadConnections();
            }
        });

        SetListenerForStationAutocomplete(R.id.txtFrom, R.id.pb_loading_indicatorFrom);
        SetListenerForStationAutocomplete(R.id.txtTo, R.id.pb_loading_indicatorTo);
        return FahrplanView;
    }

    private void SetListenerForStationAutocomplete(int autoCompleteTextView, int indicator) {
        final DelayAutoCompleteTextView stationName = (DelayAutoCompleteTextView) FahrplanView.findViewById(autoCompleteTextView);
        stationName.setThreshold(THRESHOLD);
        stationName.setAdapter(new StationAutoCompleteAdapter(FahrplanView.getContext()));
        stationName.setLoadingIndicator(
                (android.widget.ProgressBar) FahrplanView.findViewById(indicator));
        stationName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Station station = (Station) adapterView.getItemAtPosition(position);
                stationName.setText(station.getName());
            }
        });
    }

    private void LoadConnections() {

        EditText txtFrom = (EditText) FahrplanView.findViewById(R.id.txtFrom);
        EditText txtTo = (EditText) FahrplanView.findViewById(R.id.txtTo);
        ConnectionSearch cs = new ConnectionSearch(txtFrom.getText().toString(),txtTo.getText().toString());
        new LoaderTask().execute(cs);
    }

    public class ConnectionSearch
    {
        public String From;
        public String To;

        public ConnectionSearch(String from, String to)
        {
            From = from;
            To = to;
        }
    }
    //TODO In Service auslagern
    private class LoaderTask extends AsyncTask<ConnectionSearch, Void, ConnectionList> {


        @Override
        protected ConnectionList doInBackground(ConnectionSearch... params) {
            // Get Repository
            IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();

            ConnectionList connectionList = null;
            try {
                connectionList = repo.searchConnections(params[0].From, params[0].To);
            } catch (OpenDataTransportException e) {
                e.printStackTrace();
                //TODO Fehlerhandling
            }

            return connectionList;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(ConnectionList connectionList) {
            // Construct the data source
            List<Connection> listOfConnections =connectionList.getConnections();
// Create the adapter to convert the array to views
            ConnectionAdapter adapter = new ConnectionAdapter(getContext(), listOfConnections);
// Attach the adapter to a ListView
            ListView listView = (ListView) FahrplanView.findViewById(R.id.listVerbindungen);
            listView.setAdapter(adapter);

            // Hide the Keyboard
            Context mContext = getContext();
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(FahrplanView.getWindowToken(), 0);
        }
    }

    public class ConnectionAdapter extends ArrayAdapter<Connection> {
        public ConnectionAdapter(Context context, List<Connection> connections) {
            super(context, 0, connections);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Connection connection = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_connection, parent, false);
            }

            // Lookup view for data population
            TextView tvFrom = (TextView) convertView.findViewById(R.id.tvFrom);
            TextView tvTo = (TextView) convertView.findViewById(R.id.tvTo);
            TextView tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
            // Populate the data into the template view using the data object
            tvFrom.setText(connection.getFrom().getStation().getName());
            tvTo.setText(connection.getTo().getStation().getName());
            tvDuration.setText(connection.getDuration());
            // Return the completed view to render on screen
            return convertView;
        }

    }

}