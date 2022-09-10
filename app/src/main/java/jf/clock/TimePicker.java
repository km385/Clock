package jf.clock;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;
import java.util.Date;


public class TimePicker extends AppCompatDialogFragment {
    private static final String TAG = "TimePicker";

    private android.widget.TimePicker mTimePicker;

    public static TimePicker newInstance(Date date, int position) {

        Bundle args = new Bundle();
        args.putSerializable("date", date);
        args.putInt("position", position);
        TimePicker fragment = new TimePicker();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(requireContext()).inflate(R.layout.time_setter, null);

        Date date = (Date) getArguments().getSerializable("date");
        int position = getArguments().getInt("position");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        mTimePicker = v.findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));

        return new AlertDialog.Builder(requireContext())
                .setView(v)
                .setTitle("yo")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.HOUR_OF_DAY, mTimePicker.getHour());
                        calendar1.set(Calendar.MINUTE, mTimePicker.getMinute());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("date", calendar1.getTime());
                        bundle.putInt("position", position);

                        getParentFragmentManager().setFragmentResult("requestKey", bundle);

                    }
                })
                .create();
    }
}
