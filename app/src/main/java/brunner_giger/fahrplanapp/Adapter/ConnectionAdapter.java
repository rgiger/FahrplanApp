package brunner_giger.fahrplanapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import brunner_giger.fahrplanapp.R;
import ch.schoeb.opendatatransport.model.Connection;


public class ConnectionAdapter extends ArrayAdapter<Connection> {
    public ConnectionAdapter(Context context, List<Connection> connections) {
        super(context, 0, connections);
        sdfTime = new SimpleDateFormat("HH:mm");
        sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdfDuration = new SimpleDateFormat("HH'h':mm'm'");
        inputFormatDuration = new SimpleDateFormat("dd'd'HH:mm:ss");
    }
    private SimpleDateFormat sdfTime;
    private SimpleDateFormat sdfDate;
    private DateFormat inputFormat;
    private SimpleDateFormat sdfDuration;
    private DateFormat inputFormatDuration;

    @NonNull
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
        TextView tvDepartureDate = (TextView) convertView.findViewById(R.id.tvDepartureDate);
        TextView tvDepartureTime = (TextView) convertView.findViewById(R.id.tvDepartureTime);
        TextView tvArrivalDate = (TextView) convertView.findViewById(R.id.tvArrivalDate);
        TextView tvArrivalTime = (TextView) convertView.findViewById(R.id.tvArrivalTime);
        TextView tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
        TextView tvdetPlatfArr = (TextView) convertView.findViewById(R.id.tvdetPlatfArr);
        TextView tvdetPatfDep = (TextView) convertView.findViewById(R.id.tvdetPatfDep);
        // Populate the data into the template view using the data object
        assert connection != null;
        tvFrom.setText(connection.getFrom().getStation().getName());
        RelativeLayout rlDate = (RelativeLayout) convertView.findViewById(R.id.rlDate);
        tvdetPatfDep.setText(connection.getFrom().getPlatform());
        tvdetPlatfArr.setText(connection.getTo().getPlatform());

        tvTo.setText(connection.getTo().getStation().getName());
        Calendar departure = Calendar.getInstance();
        Calendar arrival = Calendar.getInstance();

        try {
            departure.setTime(inputFormat.parse(connection.getFrom().getDeparture()));
            arrival.setTime(inputFormat.parse(connection.getTo().getArrival()));
            if(position > 0) {
                Connection previouConnection = getItem(position - 1);
                Calendar previousD = Calendar.getInstance();
                assert previouConnection != null;
                previousD.setTime(inputFormat.parse(previouConnection.getFrom().getDeparture()));
                if(previousD.get(Calendar.DATE) != departure.get(Calendar.DATE))
                {
                    rlDate.setVisibility(View.VISIBLE);
                }
                else
                {
                    rlDate.setVisibility(View.GONE);
                }
            }
            else
            {
                rlDate.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar duration = Calendar.getInstance();
        try {
            duration.setTime(inputFormatDuration.parse(connection.getDuration()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Check if Duration ist longer than a day
        String temp =  connection.getDuration();
        temp = temp.substring(0, temp.indexOf('d'));
        int iDay = Integer.valueOf(temp);
        String strDay =(iDay > 0) ?  String.valueOf(iDay) + convertView.getResources().getString(R.string.abkTag) : "";

        tvDuration.setText(new StringBuilder().append(strDay).append(sdfDuration.format(duration.getTime())).toString());
        tvDepartureDate.setText(sdfDate.format(departure.getTime()));
        tvDepartureTime.setText(sdfTime.format(departure.getTime()));
        tvArrivalDate.setText(sdfDate.format(arrival.getTime()));
        tvArrivalTime.setText(sdfTime.format(arrival.getTime()));

        // Return the completed view to render on screen
        return convertView;
    }


    public Connection getItemAtPosition(int position) {
        return getItem(position);
    }
}