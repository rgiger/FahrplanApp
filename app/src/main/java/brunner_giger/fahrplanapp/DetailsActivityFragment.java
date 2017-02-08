package brunner_giger.fahrplanapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import brunner_giger.fahrplanapp.Adapter.ConnectionDetailsAdapter;
import brunner_giger.fahrplanapp.Model.ConnectionDetail;
import brunner_giger.fahrplanapp.Model.ConnectionSection;
import ch.schoeb.opendatatransport.model.Connection;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        Toast.makeText(this.getActivity(), "Das ist ein Toast", Toast.LENGTH_LONG).show();


        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ArrayList<ConnectionSection> ConnectionSections = (ArrayList<ConnectionSection>)( this.getActivity().getIntent().getBundleExtra("bundle").getSerializable("connections"));
        ((TextView) view.findViewById(R.id.tvdFrom)).setText(ConnectionSections.get(0).Departure);
        ((TextView) view.findViewById(R.id.tvdTo)).setText(ConnectionSections.get(0).Arrival);
        /*((TextView) view.findViewById(R.id.tvdDuration)).setText(this.getActivity().getIntent().getStringExtra("Duration"));
        ((TextView) view.findViewById(R.id.tvdTransfers)).setText(this.getActivity().getIntent().getStringExtra("Transfers"));
        ((TextView) view.findViewById(R.id.tvdCap2)).setText(this.getActivity().getIntent().getStringExtra("Cap2"));
        ((TextView) view.findViewById(R.id.tvdCap1)).setText(this.getActivity().getIntent().getStringExtra("Cap1"));*/

        //return inflater.inflate(R.layout.fragment_details, container, false);
        return view;
    }
}
