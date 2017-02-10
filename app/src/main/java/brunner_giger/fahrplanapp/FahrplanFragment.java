package brunner_giger.fahrplanapp;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import brunner_giger.fahrplanapp.Adapter.ConnectionAdapter;
import brunner_giger.fahrplanapp.Adapter.StationAutoCompleteAdapter;
import brunner_giger.fahrplanapp.Controls.DelayAutoCompleteTextView;
import brunner_giger.fahrplanapp.Dialog.DepartureArrivalTimePickerDialog;
import brunner_giger.fahrplanapp.Model.ConnectionDetail;
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
    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    TextView tvDateTop;
    RelativeLayout rlDateTop;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        FahrplanView =  inflater.inflate(R.layout.content_fahrplan, container, false);
        tvDateTop = (TextView) FahrplanView.findViewById(R.id.tvDateTop);
        rlDateTop = (RelativeLayout) FahrplanView.findViewById(R.id.rlDateTop);
        rlDateTop.setVisibility(View.GONE);
        SetupListener();
        SetupWhenButton();
        return FahrplanView;
    }

    private void SetupWhenButton() {
        _departurearrivaltime = new DepartureArrivalTime(getActivity());
        Button btn = (Button) FahrplanView.findViewById(R.id.btnSetWhen);
        UpdateWhenButton(btn);

    }
    int firstVisibleInList = 0;
    int VerbindungsCounter = 4;
    private void UpdateWhenButton(Button btn) {
        btn.setText(_departurearrivaltime.toString());
    }
    private void SetupListener() {
        Button btn = (Button) FahrplanView.findViewById(R.id.btnSearchConnection);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerbindungsCounter = 4;
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
                if(totalItemCount > 0) {
                  if (firstVisibleInList != firstVisibleItem) {
                        firstVisibleInList = firstVisibleItem;
                        try {
                            View v = view.getChildAt(firstVisibleItem);
                          //  RelativeLayout rl = (RelativeLayout)v.findViewById(R.id.rlDate);
                            int i = 0;
                            if(firstVisibleItem-1 > 0)
                            {
                                i = firstVisibleItem-1;
                           //     rl.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                           //     rl.setVisibility(View.GONE);
                            }
                            Connection c = listOfConnections.get(i);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(inputFormat.parse(c.getTo().getArrival()));
                            tvDateTop.setText(sdfDate.format(cal.getTime()));
                            rlDateTop.setVisibility(View.VISIBLE);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    //Check if the last view is visible
                    if (++firstVisibleItem + visibleItemCount > totalItemCount && totalItemCount >= VerbindungsCounter) {
                        VerbindungsCounter = VerbindungsCounter + 4;
                        LoadLaterConnections();
                    }
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

                ConnectionDetail cd = new ConnectionDetail(connection);
                Bundle b = new Bundle();
                b.putSerializable("connections",cd.ConnectionSections);
                intent.putExtra("bundle",  b);
                context.startActivity(intent);

            }
        });

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

    ConnectionSearch CS;
    private void LoadConnections() {
        listOfConnections = null;
        EditText txtFrom = (EditText) FahrplanView.findViewById(R.id.txtFrom);
        EditText txtTo = (EditText) FahrplanView.findViewById(R.id.txtTo);
        ProgressBar pb = (android.widget.ProgressBar) FahrplanView.findViewById(R.id.pb_loading_indicatorListConnections);
        pb.setVisibility(View.VISIBLE);
        CS = new ConnectionSearch(txtFrom.getText().toString(),txtTo.getText().toString(), sdfDate.format(_departurearrivaltime.When.getTime()), sdfTime.format(_departurearrivaltime.When.getTime()), _departurearrivaltime.isArrival);
        new LoaderTask().execute(CS);
    }

    private void LoadLaterConnections() {
        if (CS != null && listOfConnections != null && listOfConnections.size() >= 4) {
           ProgressBar pb = (android.widget.ProgressBar) FahrplanView.findViewById(R.id.pb_loading_indicatorListConnections);
           pb.setVisibility(View.VISIBLE);
            try {
                Calendar calendar  =  Calendar.getInstance();
                if(CS.isArrival) {
                    calendar.setTime(inputFormat.parse(listOfConnections.get(listOfConnections.size() - 1).getTo().getArrival()));
                    calendar.add(Calendar.SECOND, 1);
                }
                else
                {
                    calendar.setTime(inputFormat.parse(listOfConnections.get(listOfConnections.size() - 1).getTo().getArrival()));
                    calendar.add(Calendar.SECOND, 1);
                }
                CS.Date = sdfDate.format(calendar.getTime());
                CS.Time = sdfTime.format(calendar.getTime());
                new LoaderTask().execute(CS);
            } catch (Exception e) {
                e.printStackTrace();
                pb.setVisibility(View.GONE);
                //TODO Fehlerhandling}
            }
        }
    }


    List<Connection> listOfConnections;
    ListView listView;

    //TODO In (Service) auslagern
    private class LoaderTask extends AsyncTask<ConnectionSearch, Void, ConnectionList> {
        @Override
        protected ConnectionList doInBackground(ConnectionSearch... params) {
            // Get Repository
            IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();

            ConnectionList connectionList = null;
            try {
                connectionList = repo.searchConnections(params[0].From, params[0].To, "", params[0].Date, params[0].Time,  params[0].isArrival);

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
            if(connectionList != null ) {
                List<Connection> lconnections= connectionList.getConnections();
                if(lconnections != null) {
                    if (listOfConnections == null) {
                        listView = (ListView) FahrplanView.findViewById(R.id.listConnections);
                        listOfConnections = lconnections;
                        ConnectionAdapter adapter = new ConnectionAdapter(getContext(), listOfConnections);
                        listView.setAdapter(adapter);
                    } else {
                        listOfConnections.addAll(lconnections);
                        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                    }
                }
            }
// Create the adapter to convert the array to views

// Attach the adapter to a ListView
            ProgressBar pb = (android.widget.ProgressBar) FahrplanView.findViewById(R.id.pb_loading_indicatorListConnections);
            pb.setVisibility(View.GONE);


            // Hide the Keyboard
            Context mContext = getContext();
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(FahrplanView.getWindowToken(), 0);
        }
    }



}