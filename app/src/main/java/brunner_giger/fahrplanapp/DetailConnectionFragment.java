package brunner_giger.fahrplanapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by r.giger on 21.01.2017.
 */

public class DetailConnectionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
      //  container.clearDisappearingChildren();
        return inflater.inflate(R.layout.content_connectiondetail, container, false);
    }
}