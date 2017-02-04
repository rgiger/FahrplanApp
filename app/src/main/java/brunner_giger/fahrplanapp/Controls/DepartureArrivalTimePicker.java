package brunner_giger.fahrplanapp.Controls;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;


import brunner_giger.fahrplanapp.R;

/**
 * Created by r.giger on 04.02.2017.
 */

public class DepartureArrivalTimePicker
      extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.date_time_picker_dialog, null);
            builder.setView(view)

                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DepartureArrivalTimePicker.this.getDialog().cancel();
                        }
                    });
            SetupListener(view);
            return builder.create();
        }

    private void SetupListener( View view) {
        final ToggleButton tglDeparture= (ToggleButton) view.findViewById(R.id.toggleButtonAbfahrt);
        final ToggleButton tglArrival= (ToggleButton) view.findViewById(R.id.toggleButtonAnkunft);
        tglDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tglDeparture.isChecked()) {
                    tglArrival.setChecked(!tglDeparture.isChecked());
                }
                else
                {tglDeparture.setChecked(false);}
            }
        });
        tglArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tglArrival.isChecked()) {
                    tglDeparture.setChecked(!tglArrival.isChecked());
                }
                else {
                    tglArrival.setChecked(false);
                }
            }
        });
    }

}

