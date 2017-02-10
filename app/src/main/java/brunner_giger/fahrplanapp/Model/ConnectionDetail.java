package brunner_giger.fahrplanapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.Section;

/**
 * Created by r.giger on 08.02.2017.
 */

public class ConnectionDetail implements Serializable {
        //implements Parcelable {

    public ArrayList<ConnectionSection> ConnectionSections;


    public ConnectionDetail(Connection c) {

        ConnectionSections = new ArrayList<ConnectionSection>();

        for (Section s : c.getSections()) {

            ConnectionSection cs = new ConnectionSection();

            try {
                cs.From = c.getFrom().getStation().getName();
                cs.StartTime = c.getFrom().getDeparture();
                cs.ToEnd = c.getTo().getStation().getName();
                cs.EndTime = c.getTo().getArrival();
                cs.Duration = c.getDuration();
                cs.Products = c.getProducts().toString();

                cs.DepartureTime = s.getDeparture().getDeparture();
                cs.Departure = s.getDeparture().getStation().getName();
                cs.DeparturePlatform = s.getDeparture().getPlatform();
                cs.DeparturePrognosis = s.getDeparture().getPrognosis().getDeparture();

                cs.ArrivalTime = s.getArrival().getArrival();
                cs.Arrival = s.getArrival().getStation().getName();
                cs.ArrivalPlatform = s.getArrival().getPlatform();
                cs.ArrivalPrognosis = s.getArrival().getPrognosis().getArrival();

                cs.Capacity1st = s.getJourney().getCapacity1st();
                cs.Capacity2nd = s.getJourney().getCapacity2nd();
                cs.Name = s.getJourney().getName();
                cs.To = s.getJourney().getTo();
                cs.CategoryCode = s.getJourney().getCategoryCode();

                ConnectionSections.add(cs);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
