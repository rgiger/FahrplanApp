package brunner_giger.fahrplanapp.Model;

import java.io.Serializable;

/**
 * Created by r.giger on 08.02.2017.
 */
public class ConnectionSection implements Serializable{

    // Overall information
    public String From;
    public String From_Latitude;
    public String From_Longitude;
    public String StartTime;
    public String ToEnd;
    public String ToEnd_Latitude;
    public String ToEnd_Longitude;
    public String EndTime;
    public String Duration;
    public String Products;

    // Departure
    public String DepartureTime;
    public String Departure;
    public String Dep_Latitude;
    public String Dep_Longitude;
    public String DeparturePlatform;
    public String DeparturePrognosis;

    // Arrival
    public String ArrivalTime;
    public String Arrival;
    public String ArrivalPlatform;
    public String ArrivalPrognosis;

    //Journey details
    public String Name;
    public String CategoryCode;
    public String To;
    public String Capacity1st;
    public String Capacity2nd;
}
