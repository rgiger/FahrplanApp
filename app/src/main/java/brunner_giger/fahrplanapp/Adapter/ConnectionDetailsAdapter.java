package brunner_giger.fahrplanapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import brunner_giger.fahrplanapp.R;
import ch.schoeb.opendatatransport.model.Connection;

public class ConnectionDetailsAdapter extends BaseAdapter {

    private Context context;
    private Connection connection;

    public ConnectionDetailsAdapter(Context context, Connection connection) {
        this.context = context;
        this.connection = connection;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_details, parent, false);
        }
/*
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_details, parent, false);
        }
*/
        // Lookup view for data population
        ((TextView) convertView.findViewById(R.id.tvdetFrom)).setText(connection.getFrom().toString());



        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}