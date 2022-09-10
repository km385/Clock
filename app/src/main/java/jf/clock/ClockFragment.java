package jf.clock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jf.clock.data.Alarm;
import jf.clock.data.AlarmDao;
import jf.clock.data.AppDatabase;

public class ClockFragment extends Fragment {
    private static final String TAG = "ClockFragment";

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private AlarmAdapter mAdapter;
    private List<Alarm> mAlarmList;

    private AppDatabase mDb;
    private AlarmDao mAlarmDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mDb = Room.databaseBuilder(requireContext(),
                AppDatabase.class, "Sample.db")
                .build();
        mAlarmDao = mDb.mAlarmDao();
        // new LoadAlarms().execute();

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.clock_fragment, container, false);

        getChildFragmentManager().setFragmentResultListener("requestKey",
                this, (requestKey, result) -> {
                    if (requestKey.equals("requestKey")){
                        int pos = result.getInt("position");
                        mAlarmList.get(pos).setAlarmTime((Date) result.getSerializable("date"));
                        mAdapter.notifyItemChanged(pos);
                    }
                });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mAlarmList = new ArrayList<>();
        for (int i = 0; i < 5;i++){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,i);
            calendar.set(Calendar.MINUTE, i);
            Alarm alarm = new Alarm();
            alarm.setId(i);
            alarm.setAlarmSet(false);
            alarm.setAlarmTime(calendar.getTime());
            mAlarmList.add(alarm);
        }
        new LoadAlarms().execute();

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // delete alarm
            }
        });
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mAdapter = new AlarmAdapter(mAlarmList);
        mRecyclerView.setAdapter(mAdapter);
        Log.i(TAG, "onViewCreated: ");

    }

    public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder>{
        private List<Alarm> mAlarms;

        public AlarmAdapter(List<Alarm> alarms) {
            mAlarms = alarms;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView mTextView;
            private SwitchCompat mSwitch;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                mTextView = itemView.findViewById(R.id.alarm_id);
                mSwitch = itemView.findViewById(R.id.alarm_switch);

                // do things with your elements in each row
            }

            public void bind(Alarm alarm){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                mTextView.setText(simpleDateFormat.format(alarm.getAlarmTime()));
            }



            @Override
            public void onClick(View v) {
                // TODO date is saved as String, change to Date if possible
                Date date = mAlarms.get(getAdapterPosition()).getAlarmTime();

                FragmentManager fm = getChildFragmentManager();
                TimePicker dialog = TimePicker.newInstance(date, getAdapterPosition());
                dialog.show(fm, "dialog");

            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AlarmAdapter.ViewHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.alarm_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(mAlarms.get(position));
        }

        @Override
        public int getItemCount() {
            return mAlarms.size();
        }
    }

    public class LoadAlarms extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
