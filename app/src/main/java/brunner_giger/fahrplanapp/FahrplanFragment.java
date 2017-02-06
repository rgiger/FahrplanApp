package brunner_giger.fahrplanapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.List;

import brunner_giger.fahrplanapp.Adapter.ConnectionAdapter;
import brunner_giger.fahrplanapp.Adapter.ConnectionDetailsAdapter;
import brunner_giger.fahrplanapp.Adapter.StationAutoCompleteAdapter;
import brunner_giger.fahrplanapp.Controls.DelayAutoCompleteTextView;
import brunner_giger.fahrplanapp.Dialog.DepartureArrivalTimePickerDialog;
import brunner_giger.fahrplanapp.Model.ConnectionSearch;
import brunner_giger.fahrplanapp.Model.DepartureArrivalTime;
import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenDataTransportException;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.Station;

public class FahrplanFragment extends Fragment {
    private static final int THRESHOLD = 2;
    View FahrplanView = null;

    //Calendar When = Calendar.getInstance();
    DepartureArrivalTime _departurearrivaltime;
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        FahrplanView =  inflater.inflate(R.layout.content_fahrplan, container, false);
        SetupListener();
        SetupWhenButton();
        return FahrplanView;
    }

    private void SetupWhenButton() {
        _departurearrivaltime = new DepartureArrivalTime(getActivity());
        Button btn = (Button) FahrplanView.findViewById(R.id.btnSetWhen);
        UpdateWhenButton(btn);

    }

    private void UpdateWhenButton(Button btn) {
        btn.setText(_departurearrivaltime.toString());
    }

    private void SetupListener() {
        Button btn = (Button) FahrplanView.findViewById(R.id.btnSearchConnection);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadConnections();
            }
        });
        AddWhenButtonListener();
        ImageButton btnReverse= (ImageButton) FahrplanView.findViewById(R.id.btnReverseConnection);
        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ReverseConnection(); }
        });

        SetListenerForStationAutocomplete(R.id.txtFrom, R.id.pb_loading_indicatorFrom);
        SetListenerForStationAutocomplete(R.id.txtTo, R.id.pb_loading_indicatorTo);

        final ListView listConnection= (ListView) FahrplanView.findViewById(R.id.listConnections);
        listConnection.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //Check if the last view is visible
                if (++firstVisibleItem + visibleItemCount > totalItemCount) {

                    //load more content
                }
            }
        });

        listConnection.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ConnectionAdapter adapter = (ConnectionAdapter)listConnection.getAdapter();
                Connection connection = adapter.getItemAtPosition(position);

                Context context = view.getContext();
                Intent intent = new Intent(context, DetailsActivity.class);

//                ConnectionDetailsAdapter connectionDetailsAdapter = new ConnectionDetailsAdapter(view.getContext(), connection);
//                ListView listConnectionDetails = (ListView) view.findViewById(R.id.listConnectionDetails);
//                listConnectionDetails.setAdapter(connectionDetailsAdapter);


                SetConnectionToIntent(connection, intent);
                context.startActivity(intent);

            }
        });

    }

    private void SetConnectionToIntent(Connection connection, Intent intent) {

        // in dieser Methode werden die anzuzeigenden Daten ins Intent Objekt der Activity geschrieben
        intent.putExtra("From", connection.getFrom().getStation().getName());
        intent.putExtra("To", connection.getTo().getStation().getName());
        intent.putExtra("Duration", connection.getDuration());
        intent.putExtra("Transfers", connection.getTransfers().toString());
        intent.putExtra("Cap2", connection.getCapacity2nd().toString());
        intent.putExtra("Cap1", connection.getCapacity1st().toString());
    }

    private void AddWhenButtonListener() {
        Button btn = (Button) FahrplanView.findViewById(R.id.btnSetWhen);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDepartureArrivalTimePicker();
            }
        });
    }

    public void updateDepartureArrivalTime(DepartureArrivalTime departureArrivalTime)
    {
        _departurearrivaltime = departureArrivalTime;
        Button btn = (Button) FahrplanView.findViewById(R.id.btnSetWhen);
        UpdateWhenButton(btn);

    }

    private void LoadDepartureArrivalTimePicker() {
        DialogFragment newFragment = DepartureArrivalTimePickerDialog.newInstance(_departurearrivaltime);
        FragmentManager fragmentManager = getFragmentManager();
        newFragment.setTargetFragment(this, 0);

        newFragment.show(fragmentManager, "departureArrivalTimePicker");
    }

    private void ReverseConnection() {
        FahrplanView.clearFocus();
        final DelayAutoCompleteTextView txtFrom = (DelayAutoCompleteTextView) FahrplanView.findViewById(R.id.txtFrom);
        final DelayAutoCompleteTextView txtTo = (DelayAutoCompleteTextView) FahrplanView.findViewById(R.id.txtTo);
        txtFrom.SetIndicatorActive(false);
        txtTo.SetIndicatorActive(false);
        Editable from = txtFrom.getText();
        txtFrom.setText(txtTo.getText());
        txtTo.setText(from);
        txtFrom.SetIndicatorActive(true);
        txtTo.SetIndicatorActive(true);
    }

    private void SetListenerForStationAutocomplete(int autoCompleteTextView, int indicator) {
        final DelayAutoCompleteTextView stationName = (DelayAutoCompleteTextView) FahrplanView.findViewById(autoCompleteTextView);
        stationName.setThreshold(THRESHOLD);
        stationName.setAdapter(new StationAutoCompleteAdapter(FahrplanView.getContext()));
        stationName.setLoadingIndicator(
                (android.widget.ProgressBar) FahrplanView.findViewById(indicator));
        stationName.setOnItemClickListener(new OnItemClickListener() {
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

        ProgressBar pb = (android.widget.ProgressBar) FahrplanView.findViewById(R.id.pb_loading_indicatorListConnections);
        pb.setVisibility(View.VISIBLE);
        ConnectionSearch cs = new ConnectionSearch(txtFrom.getText().toString(),txtTo.getText().toString());
        new LoaderTask().execute(cs);
    }


    //TODO In Service auslagern
    private class LoaderTask extends AsyncTask<ConnectionSearch, Void, ConnectionList> {


        @Override
        protected ConnectionList doInBackground(ConnectionSearch... params) {
            // Get Repository
            IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();

            ConnectionList connectionList = null;
            try {
                connectionList = repo.searchConnections(params[0].From, params[0].To, "", sdfDate.format(_departurearrivaltime.When.getTime()), sdfTime.format(_departurearrivaltime.When.getTime()), _departurearrivaltime.isArrival);

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

            ProgressBar pb = (android.widget.ProgressBar) FahrplanView.findViewById(R.id.pb_loading_indicatorListConnections);
            pb.setVisibility(View.GONE);
            ListView listView = (ListView) FahrplanView.findViewById(R.id.listConnections);
            listView.setAdapter(adapter);

            // Hide the Keyboard
            Context mContext = getContext();
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(FahrplanView.getWindowToken(), 0);
        }
    }



}