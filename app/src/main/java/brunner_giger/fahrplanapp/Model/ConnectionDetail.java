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

public class ConnectionDetail{
        //implements Parcelable {

    public ArrayList<ConnectionSection> ConnectionSections;


    public ConnectionDetail(Connection c) {

        ConnectionSections = new ArrayList<ConnectionSection>();
        for (Section s : c.getSections()) {
           /* intent.putExtra("From", connection.getFrom().getStation().getName());
            intent.putExtra("To", connection.getTo().getStation().getName());
            intent.putExtra("Duration", connection.getDuration());
            intent.putExtra("Transfers", connection.getTransfers().toString());
            intent.putExtra("Cap2", connection.getCapacity2nd().toString());
            intent.putExtra("Cap1", connection.getCapacity1st().toString());*/


            ConnectionSection cs = new ConnectionSection();
            cs.Departure = s.getDeparture().getStation().getName();
            cs.Arrival = s.getArrival().getStation().getName();
            ConnectionSections.add(cs);
        }

    }
    /*
    private ConnectionDetail(Parcel in) {
        in.readList(ConnectionSections, ConnectionSection.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(ConnectionSections);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ConnectionDetail> CREATOR
            = new Parcelable.Creator<ConnectionDetail>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public ConnectionDetail createFromParcel(Parcel in) {
            return new ConnectionDetail(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public ConnectionDetail[] newArray(int size) {
            return new ConnectionDetail[size];
        }
    };
*/
}
