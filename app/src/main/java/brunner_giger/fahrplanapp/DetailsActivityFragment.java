package brunner_giger.fahrplanapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import brunner_giger.fahrplanapp.Adapter.ConnectionSectionAdapter;
import brunner_giger.fahrplanapp.Model.ConnectionSection;

public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
    private DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        final ArrayList<ConnectionSection> ConnectionSections = (ArrayList<ConnectionSection>)( this.getActivity().getIntent().getBundleExtra("bundle").getSerializable("connections"));

        Calendar departure = Calendar.getInstance();
        Calendar arrival = Calendar.getInstance();
        try {
            departure.setTime(inputFormat.parse(ConnectionSections.get(0).StartTime));
            arrival.setTime(inputFormat.parse(ConnectionSections.get(0).EndTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ((TextView) view.findViewById(R.id.tvdDepTimeAll)).setText(sdfTime.format(departure.getTime()));
        ((TextView) view.findViewById(R.id.tvdArrTimeAll)).setText(sdfTime.format(arrival.getTime()));
        ((TextView) view.findViewById(R.id.tvdDate)).setText(sdfDate.format(departure.getTime()));

        ((TextView) view.findViewById(R.id.tvdFrom)).setText(ConnectionSections.get(0).From);
        ((TextView) view.findViewById(R.id.tvdTo)).setText(ConnectionSections.get(0).ToEnd);
        ((TextView) view.findViewById(R.id.tvdDuration)).setText(ConnectionSections.get(0).Duration);
        ((TextView) view.findViewById(R.id.tvdProducts)).setText(ConnectionSections.get(0).Products);

        ConnectionSectionAdapter adapter = new ConnectionSectionAdapter(getContext(),ConnectionSections);
        ((ListView) view.findViewById(R.id.listConnectionDetails)).setAdapter(adapter);

        return view;
    }
}
