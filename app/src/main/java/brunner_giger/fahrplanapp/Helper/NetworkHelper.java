package brunner_giger.fahrplanapp.Helper;

import java.net.InetAddress;

public class NetworkHelper {
    //TODO: Check if Service is available
    public boolean isServiceAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("http://transport.opendata.ch");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
}
