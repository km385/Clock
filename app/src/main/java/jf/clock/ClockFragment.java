package jf.clock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClockFragment extends Fragment {
    private static final String TAG = "ClockFragment";

    private RecyclerView mRecyclerView;
    private AlarmAdapter mAdapter;
    private Alarm mAlarm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.clock_fragment, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        List<Alarm> alarms = new ArrayList<>();
        for (int i = 0; i < 5;i++){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,i);
            calendar.set(Calendar.MINUTE, i);
            Alarm alarm = new Alarm();
            alarm.setAlarmTime(calendar.getTime());
            alarms.add(alarm);
        }

        String[] yoyo = {
                "06:30",
                "09:00",
                "04:20"
        };


        mAdapter = new AlarmAdapter(alarms);
        mRecyclerView.setAdapter(mAdapter);

    }

}
