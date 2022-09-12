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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    public static final int UPDATE_FIELD = 2;
    public static final int DELETE = 3;

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private AlarmAdapter mAdapter;
    private FloatingActionButton mFab;
    private List<Alarm> mAlarmList;

    private AppDatabase mDb;
    private AlarmDao mAlarmDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mDb = Room.databaseBuilder(requireContext(),
                AppDatabase.class, "Sample.db")
                .build();
        mAlarmDao = mDb.mAlarmDao();

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
                        if (mAlarmList.size() == pos){
                            // add new alarm
                            Alarm alarm = new Alarm();
                            alarm.setAlarmTime((Date) result.getSerializable("date"));
                            alarm.setAlarmSet(true);
                            mAlarmList.add(alarm);
                            //mAdapter.setAlarms(mAlarmList);
                            mAdapter.notifyItemInserted(pos);
                            new LoadAlarms(INSERT, pos).execute(alarm);
                        } else {
                            // update current alarm
                            mAlarmList.get(pos).setAlarmTime((Date) result.getSerializable("date"));
                            //mAdapter.setAlarms(mAlarmList);
                            mAdapter.notifyItemChanged(pos);
                            new LoadAlarms(UPDATE).execute(mAlarmList.get(pos));
                        }
                    }
                });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mFab = view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = Calendar.getInstance().getTime();

                FragmentManager fm = getChildFragmentManager();
                TimePicker dialog = TimePicker.newInstance(date, mAlarmList.size());
                dialog.show(fm, "dialog");
            }
        });

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mAlarmList = new ArrayList<>();

        // setup adapter
        new LoadAlarms(-1).execute();

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
                new LoadAlarms(DELETE, viewHolder.getAdapterPosition())
                        .execute(mAlarmList.get(viewHolder.getAdapterPosition()));
                mAlarmList.remove(viewHolder.getAdapterPosition());
            }
        });
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);


    }

    public void setupAdapter(){
        Log.i(TAG, "setupAdapter");
        if (isAdded()){
            mAdapter = new AlarmAdapter(mAlarmList);
            mRecyclerView.setAdapter(mAdapter);
        }

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
                mSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAlarmList.get(getAdapterPosition()).setAlarmSet(mSwitch.isChecked());
                        new LoadAlarms(
                                UPDATE_FIELD,
                                mAlarms.get(getAdapterPosition()).getId(),
                                getAdapterPosition(),
                                mSwitch.isChecked()).execute();
                        Log.i(TAG, "onClick: " + mAlarms.get(getAdapterPosition()).getId());
                    }
                });

                // do things with your elements in each row
            }

            public void bind(Alarm alarm){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                mTextView.setText(simpleDateFormat.format(alarm.getAlarmTime()));
                //mTextView.setText(alarm.getId());
                mSwitch.setChecked(alarm.isAlarmSet());
            }



            @Override
            public void onClick(View v) {
                // TODO changing switch right after creating an alarm does not work
                Date date = mAlarms.get(getAdapterPosition()).getAlarmTime();

                FragmentManager fm = getChildFragmentManager();
                TimePicker dialog = TimePicker.newInstance(date, getAdapterPosition());
                dialog.show(fm, "dialog");
                Log.i(TAG, "onClick: " + mAlarms.get(getAdapterPosition()).getId());

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

        public void setAlarms(List<Alarm> alarms){
            mAlarms = alarms;
        }
    }

    public class LoadAlarms extends AsyncTask<Alarm,Void,Void>{
        private int mAction;
        private int mPos;
        private int mId;
        private boolean mValue;

        public LoadAlarms(Integer value) {
            mAction = value;
        }

        public LoadAlarms(Integer value, Integer pos){
            mAction = value;
            mPos = pos;
        }

        public LoadAlarms(Integer action, Integer id, Integer pos, boolean value){
            mAction = action;
            mPos = pos;
            mValue = value;
            mId = id;
        }

        @Override
        protected Void doInBackground(Alarm... alarms) {
            switch (mAction){
                case INSERT:
                    mAlarmDao.insertAlarm(alarms);
                    break;
                case UPDATE:
                    Log.i(TAG, "UPDATE");
                    mAlarmDao.updateAlarm(alarms);
                    break;
                case UPDATE_FIELD:
                    Log.i(TAG, "UPDATE FIELD");
                    mAlarmDao.updateField(mId, mValue);
                    break;
                case DELETE:
                    mAlarmDao.deleteAlarm(alarms);
                    //mAlarmList = mAlarmDao.getAlarms();
                    break;
                default:
                    mAlarmList = mAlarmDao.getAlarms();
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            switch (mAction){
                case INSERT:
                    break;
                case DELETE:
                    //mAdapter.setAlarms(mAlarmList);
                    mAdapter.notifyItemRemoved(mPos);
                    break;
                case UPDATE:
                    break;
                case UPDATE_FIELD:
                    //mAdapter.notifyItemChanged(mPos);
                    break;
                default:
                    setupAdapter();
                    break;
            }

        }
    }

}
