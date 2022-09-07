package jf.clock;

import android.app.Dialog;
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
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavDirections;

import java.util.Calendar;
import java.util.Date;

public class TimePicker extends AppCompatDialogFragment {
    private static final String TAG = "TimePicker";

    private android.widget.TimePicker mTimePicker;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(requireContext()).inflate(R.layout.time_setter, null);

        TimePickerArgs args = TimePickerArgs.fromBundle(getArguments());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(args.getDate());
        mTimePicker = v.findViewById(R.id.time_picker);
        mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setMinute(calendar.get(Calendar.MINUTE));

        return new AlertDialog.Builder(requireContext())
                .setView(v)
                .setTitle("yo")
                .create();
    }
}
