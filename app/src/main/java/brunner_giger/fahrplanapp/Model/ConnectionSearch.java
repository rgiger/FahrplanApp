package brunner_giger.fahrplanapp.Model;

/**
 * Created by r.giger on 04.02.2017.
 */

public class ConnectionSearch
{
    public String From;
    public String To;
    public String Time;
    public String Date;
    public boolean isArrival;

    public ConnectionSearch(String from, String to, String date, String time, boolean isarrival)
    {
        From = from;
        To = to;
        Time = time;
        Date = date;
        isArrival = isarrival;
    }
}
