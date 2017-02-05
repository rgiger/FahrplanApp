package brunner_giger.fahrplanapp.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.ToggleButton;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import brunner_giger.fahrplanapp.FahrplanFragment;
import brunner_giger.fahrplanapp.Model.DepartureArrivalTime;
import brunner_giger.fahrplanapp.R;

import static brunner_giger.fahrplanapp.R.attr.colorPrimary;

public class DepartureArrivalTimePickerDialog
      extends DialogFragment{

    DepartureArrivalTime _departureArrivalTime;

    public static DepartureArrivalTimePickerDialog newInstance(DepartureArrivalTime departureArrivalTime) {

        DepartureArrivalTimePickerDialog frag = new DepartureArrivalTimePickerDialog();
        Bundle args = new Bundle();
        args.putSerializable("departurearrivaltime", departureArrivalTime);
        frag.setArguments(args);
        return frag;
    }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            _departureArrivalTime = (DepartureArrivalTime) getArguments().getSerializable("departurearrivaltime");


            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final View view = inflater.inflate(R.layout.date_time_picker_dialog, null);
            builder.setView(view)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            ToggleButton tglArrival= (ToggleButton) view.findViewById(R.id.toggleButtonAnkunft);
                            _departureArrivalTime.isArrival = tglArrival.isChecked();


                            DatePicker datepicker= (DatePicker) view.findViewById(R.id.date_picker);
                            TimePicker timepicker= (TimePicker) view.findViewById(R.id.time_picker);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                _departureArrivalTime.When.set(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth(), timepicker.getHour(), timepicker.getMinute(), 0);
                            }
                            else
                            {
                                _departureArrivalTime.When.set(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth(), timepicker.getCurrentHour(), timepicker.getCurrentMinute(), 0);
                            }

                            ((FahrplanFragment)getTargetFragment()).updateDepartureArrivalTime(_departureArrivalTime);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DepartureArrivalTimePickerDialog.this.getDialog().cancel();
                        }
                    });

            SetupDialog(_departureArrivalTime, view);
            SetupListener(view);
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#3F51B5"));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3F51B5"));

                }
            });
            return dialog;
        }

    private void SetupDialog(DepartureArrivalTime departureArrivalTime,  View view) {
        ToggleButton tglDeparture= (ToggleButton) view.findViewById(R.id.toggleButtonAbfahrt);
        ToggleButton tglArrival= (ToggleButton) view.findViewById(R.id.toggleButtonAnkunft);
        DatePicker datepicker= (DatePicker) view.findViewById(R.id.date_picker);
        TimePicker timepicker= (TimePicker) view.findViewById(R.id.time_picker);
        tglArrival.setChecked(departureArrivalTime.isArrival);
        tglDeparture.setChecked(!departureArrivalTime.isArrival);
        datepicker.updateDate(departureArrivalTime.When.get(Calendar.YEAR),departureArrivalTime.When.get(Calendar.MONTH), departureArrivalTime.When.get(Calendar.DAY_OF_MONTH));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timepicker.setHour(departureArrivalTime.When.get(Calendar.HOUR_OF_DAY));
            timepicker.setMinute(departureArrivalTime.When.get(Calendar.MINUTE));
        }
        else
        {
            timepicker.setCurrentHour(departureArrivalTime.When.get(Calendar.HOUR_OF_DAY));
            timepicker.setCurrentMinute(departureArrivalTime.When.get(Calendar.MINUTE));
        }

    }


    private void SetupListener( View view) {
        final ToggleButton tglDeparture= (ToggleButton) view.findViewById(R.id.toggleButtonAbfahrt);
        final ToggleButton tglArrival= (ToggleButton) view.findViewById(R.id.toggleButtonAnkunft);
        tglDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tglDeparture.isChecked()) {
                    tglArrival.setChecked(!tglDeparture.isChecked());
                }
                else
                {tglDeparture.setChecked(true);}
            }
        });
        tglArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tglArrival.isChecked()) {
                    tglDeparture.setChecked(!tglArrival.isChecked());
                }
                else {
                    tglArrival.setChecked(true);
                }
            }
        });
    }

}

