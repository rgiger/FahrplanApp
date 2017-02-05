package brunner_giger.fahrplanapp.Model;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import brunner_giger.fahrplanapp.R;

public class DepartureArrivalTime implements Serializable {
    public Calendar When;
    public Boolean isArrival;
    private Context ctx;
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    public DepartureArrivalTime(Context c)
    {
        ctx = c;
        When = Calendar.getInstance();
        isArrival = false;
    }
    
    @Override
    public String toString()
    {
        String output =  isArrival ? ctx.getString(R.string.Arrival) : ctx.getString(R.string.Departure);
        return output + " " + sdfTime.format(When.getTime());

    }
}
