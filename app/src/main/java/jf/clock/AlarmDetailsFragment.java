package jf.clock;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;

import jf.clock.data.Alarm;
import jf.clock.repositories.UpdateAlarmAsync;

public class AlarmDetailsFragment extends Fragment implements MenuProvider {
    private static final String TAG = "AlarmDetailsFragment";
    private Alarm mAlarm;
    private MenuHost mMenuHost;
    private TimePicker mTimePicker;
    private CheckBox mCheckBox;
    private TextView mRepeatTextView;
    private LinearLayout mRepeatDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmDetailsFragmentArgs args = AlarmDetailsFragmentArgs.fromBundle(getArguments());

        mAlarm = args.getAlarmObject();
        hideNavBar();

        getChildFragmentManager().setFragmentResultListener("weekDays", this, (requestKey, result) -> {
            boolean[] array = result.getBooleanArray("array");
            Log.i(TAG, "weekdays results" + array[0]);
            mAlarm.setWeekdays(array);
            setWeekDaysText();
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_details_fragment, container, false);

        mRepeatDialog = (LinearLayout) view.findViewById(R.id.repeat_dialog);
        mRepeatDialog.setOnClickListener(v -> {
            boolean[] alarm = Arrays.copyOf(mAlarm.getWeekdays(),7);
            AlarmRepeatBottomDialogFragment dialog = AlarmRepeatBottomDialogFragment.newInstance(alarm);
            dialog.show(getChildFragmentManager(), "TAG");
        });

        mRepeatTextView = view.findViewById(R.id.repeat_textView);
        setWeekDaysText();

        mTimePicker = view.findViewById(R.id.details_time_picker);
        mTimePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(mAlarm.getHour());
            mTimePicker.setMinute(mAlarm.getMinutes());
        } else {
            mTimePicker.setCurrentHour(mAlarm.getHour());
            mTimePicker.setCurrentMinute(mAlarm.getMinutes());
        }


        mCheckBox = view.findViewById(R.id.checkBox);
        mCheckBox.setChecked(mAlarm.isVibrate());

        mMenuHost = requireActivity();
        mMenuHost.addMenuProvider(this, getViewLifecycleOwner());

        return view;
    }

    private void setWeekDaysText() {
        if (Arrays.equals(mAlarm.getWeekdays(), new boolean[]{false, false, false, false, false, false, false})){
            mRepeatTextView.setText("Once");
        } else if(Arrays.equals(mAlarm.getWeekdays(), new boolean[]{true, true, true, true, true, false, false})){
            mRepeatTextView.setText("mon-fri");
        } else if(Arrays.equals(mAlarm.getWeekdays(), new boolean[]{true, true, true, true, true, true, true})){
            mRepeatTextView.setText("daily");
        } else {
            mRepeatTextView.setText("custom");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hideNavBar();
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.alarm_details_menu, menu);

    }

    public void hideNavBar(){
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        if (bottomNavigationView.getVisibility() != View.GONE){
            bottomNavigationView.setVisibility(View.GONE);
        }else{
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.save_changes:
                saveChanges();
                return true;
            default:
                return false;
        }

    }

    public void saveChanges(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarm.setHour(mTimePicker.getHour());
            mAlarm.setMinutes(mTimePicker.getMinute());
        } else {
            mAlarm.setHour(mTimePicker.getCurrentHour());
            mAlarm.setMinutes(mTimePicker.getCurrentMinute());
        }

        mAlarm.setVibrate(mCheckBox.isChecked());
        new UpdateAlarmAsync(mAlarm, requireContext(), null);

        Toast.makeText(requireContext(), "changes saved", Toast.LENGTH_SHORT).show();
    }
}
