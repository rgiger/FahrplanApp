package brunner_giger.fahrplanapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import brunner_giger.fahrplanapp.R;
import ch.schoeb.opendatatransport.model.Connection;

/**
 * Created by r.giger on 04.02.2017.
 */

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
        TextView tvDeparture = (TextView) convertView.findViewById(R.id.tvDeparture);
        TextView tvArrival = (TextView) convertView.findViewById(R.id.tvArrival);
        TextView tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
        // Populate the data into the template view using the data object
        tvFrom.setText(connection.getFrom().getStation().getName());

        tvTo.setText(connection.getTo().getStation().getName());

            tvDuration.setText(connection.getDuration());
            tvDeparture.setText(connection.getFrom().getDeparture());
            tvArrival.setText(connection.getTo().getArrival());

        // Return the completed view to render on screen
        return convertView;
    }



}