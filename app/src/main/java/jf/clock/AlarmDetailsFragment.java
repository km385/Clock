package jf.clock;

import android.annotation.SuppressLint;
import android.bluetooth.le.ScanSettings;
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
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import jf.clock.data.Alarm;
import jf.clock.repositories.DatabaseCallback;
import jf.clock.repositories.UpdateAlarmAsync;

public class AlarmDetailsFragment extends Fragment implements MenuProvider {
    private static final String TAG = "AlarmDetailsFragment";
    private Alarm mAlarm;
    private MenuHost mMenuHost;
    private TimePicker mTimePicker;
    private CheckBox mCheckBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmDetailsFragmentArgs args = AlarmDetailsFragmentArgs.fromBundle(getArguments());

        mAlarm = args.getAlarmObject();
        hideNavBar();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_details_fragment, container, false);

        mTimePicker = view.findViewById(R.id.details_time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setHour(mAlarm.getHour());
        mTimePicker.setMinute(mAlarm.getMinutes());

        mCheckBox = view.findViewById(R.id.checkBox);
        mCheckBox.setChecked(mAlarm.isVibrate());

        mMenuHost = requireActivity();
        mMenuHost.addMenuProvider(this, getViewLifecycleOwner());

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hideNavBar();
        //mMenuHost.removeMenuProvider(this);
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

    @SuppressLint("NewApi")
    public void saveChanges(){

        mAlarm.setHour(mTimePicker.getHour());
        mAlarm.setMinutes(mTimePicker.getMinute());
        mAlarm.setVibrate(mCheckBox.isChecked());
        new UpdateAlarmAsync(mAlarm, requireContext(), null);
    }
}
