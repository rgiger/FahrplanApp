package brunner_giger.fahrplanapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import brunner_giger.fahrplanapp.Model.ConnectionSection;
import brunner_giger.fahrplanapp.R;

public class ConnectionSectionAdapter extends ArrayAdapter<ConnectionSection>  {

    public ConnectionSectionAdapter(Context context, ArrayList<ConnectionSection> connectionSections) {
        super(context, 0, connectionSections);
    }

    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ConnectionSection connectionSection = (ConnectionSection) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_details, parent, false);
        }

        Calendar departure = Calendar.getInstance();
        Calendar arrival = Calendar.getInstance();
        Calendar prognosisDep = Calendar.getInstance();
        Calendar prognosisArr = Calendar.getInstance();

        try {
            departure.setTime(inputFormat.parse(connectionSection.DepartureTime));
            arrival.setTime(inputFormat.parse(connectionSection.ArrivalTime));

            if (connectionSection.DeparturePrognosis != null) {
                prognosisDep.setTime(inputFormat.parse(connectionSection.DeparturePrognosis));
            }
            if (connectionSection.ArrivalPrognosis != null) {
                prognosisArr.setTime(inputFormat.parse(connectionSection.ArrivalPrognosis));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ((TextView) convertView.findViewById(R.id.tvdetDepTime)).setText(sdfTime.format(departure.getTime()));
        ((TextView) convertView.findViewById(R.id.tvdetDeparture)).setText(connectionSection.Departure);
        ((TextView) convertView.findViewById(R.id.tvdetPatfDep)).setText(connectionSection.DeparturePlatform);

        if (connectionSection.DeparturePrognosis != null) {
            ((TextView) convertView.findViewById(R.id.tvdetProgDep)).setText(sdfTime.format(prognosisDep.getTime()));
        } else {
            ((TextView) convertView.findViewById(R.id.tvdetProgDep)).setText("");
        }


        ((TextView) convertView.findViewById(R.id.tvdetArrTime)).setText(sdfTime.format(arrival.getTime()));
        ((TextView) convertView.findViewById(R.id.tvdetArrival)).setText(connectionSection.Arrival);
        ((TextView) convertView.findViewById(R.id.tvdetPlatfArr)).setText(connectionSection.ArrivalPlatform);

        if (connectionSection.ArrivalPrognosis != null) {
            ((TextView) convertView.findViewById(R.id.tvdetProgArr)).setText(sdfTime.format(prognosisArr.getTime()));
        } else {
            ((TextView) convertView.findViewById(R.id.tvdetProgArr)).setText("");
        }

        ImageView imCap1 = (ImageView) convertView.findViewById(R.id.imCap1);
        ImageView imCap2 = (ImageView) convertView.findViewById(R.id.imCap2);

        if (connectionSection.Capacity1st != null) {
            switch (Integer.parseInt(connectionSection.Capacity1st)) {
                case 1:
                    imCap1.setImageResource(R.mipmap.cap_1);
                    break;
                case 2:
                    imCap1.setImageResource(R.mipmap.cap_2);
                    break;
                case 3:
                    imCap1.setImageResource(R.mipmap.cap_3);
                    break;
                default:
                    imCap1.setImageResource(android.R.color.transparent);
            }
        } else {
            imCap1.setImageResource(android.R.color.transparent);
        }

        if (connectionSection.Capacity2nd != null) {
            switch (Integer.parseInt(connectionSection.Capacity2nd)) {
                case 1:
                    imCap2.setImageResource(R.mipmap.cap_1);
                    break;
                case 2:
                    imCap2.setImageResource(R.mipmap.cap_2);
                    break;
                case 3:
                    imCap2.setImageResource(R.mipmap.cap_3);
                    break;
                default:
                    imCap2.setImageResource(android.R.color.transparent);
            }
        } else {
            imCap2.setImageResource(android.R.color.transparent);
        }

        ((TextView) convertView.findViewById(R.id.tvdetName)).setText(connectionSection.Name);
        ((TextView) convertView.findViewById(R.id.tvdetEndTo)).setText(connectionSection.To);
//        ((TextView) convertView.findViewById(R.id.tvdetCatCode)).setText(connectionSection.CategoryCode);

        // Return the completed view to render on screen
        return convertView;
    }
}