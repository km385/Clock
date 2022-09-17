package jf.clock;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jf.clock.data.Alarm;
import jf.clock.repositories.DatabaseCallback;
import jf.clock.repositories.DeleteAlarmAsync;
import jf.clock.repositories.FindAlarmByIdAsync;
import jf.clock.repositories.GetAlarmsAsync;
import jf.clock.repositories.InsertAlarmAsync;
import jf.clock.repositories.UpdateAlarmAsync;

public class ClockFragment extends Fragment {
    private static final String TAG = "ClockFragment";

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private AlarmAdapter mAdapter;
    private FloatingActionButton mFab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.clock_fragment, container, false);

        getChildFragmentManager().setFragmentResultListener("updateAlarm", this,
                (requestKey, result) -> {
            int pos = result.getInt("position");
            Date date = (Date) result.getSerializable("date");
            // TODO change if id changes to long
            new FindAlarmByIdAsync(Math.toIntExact(mAdapter.getItemId(pos)), requireContext(),
                    new DatabaseCallback<Alarm>() {
                        @Override
                        public void handleResponse(Alarm response) {
                            response.setAlarmTime(date);
                            new UpdateAlarmAsync(response, requireContext(),
                                    new DatabaseCallback<List<Alarm>>() {
                                        @Override
                                        public void handleResponse(List<Alarm> response) {
                                            mAdapter.setAlarms(response);
                                            mAdapter.notifyItemChanged(pos);
                                        }

                                        @Override
                                        public void handleError(Exception e) {
                                            Log.e(TAG, "handleError: ", e);
                                        }
                                    });
                        }

                        @Override
                        public void handleError(Exception e) {
                            Log.e(TAG, "handleError: ", e);
                        }
                    });


        });

        getChildFragmentManager().setFragmentResultListener("addAlarm", this,
                (requestKey, result) -> {
                    // TODO find a good way to set a id manually

                    Alarm alarm = new Alarm();
                    alarm.setAlarmTime((Date) result.getSerializable("date"));
                    alarm.setAlarmSet(true);

                    new InsertAlarmAsync(alarm, requireContext(), new DatabaseCallback<List<Alarm>>() {
                        @Override
                        public void handleResponse(List<Alarm> response) {
                            mAdapter.setAlarms(response);
                            mAdapter.notifyItemInserted(response.size()-1);
                        }

                        @Override
                        public void handleError(Exception e) {

                        }
                        });
                });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mFab = view.findViewById(R.id.fab);
        mFab.setOnClickListener(v -> {
            Date date = Calendar.getInstance().getTime();

            FragmentManager fm = getChildFragmentManager();
            TimePicker dialog = TimePicker.newInstance(date, -1);
            dialog.show(fm, "dialog");
        });

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        setupAdapter();

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
                new DeleteAlarmAsync(
                        Math.toIntExact(mAdapter.getItemId(viewHolder.getAdapterPosition())),
                        requireContext(),
                        new DatabaseCallback<List<Alarm>>() {
                            @Override
                            public void handleResponse(List<Alarm> response) {

                                mAdapter.setAlarms(response);
                                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            }

                            @Override
                            public void handleError(Exception e) {

                            }
                        });
            }
        });
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    public void setupAdapter(){
        Log.i(TAG, "setupAdapter");
        if (isAdded()){
            new GetAlarmsAsync(requireContext(), new DatabaseCallback<List<Alarm>>() {
                @Override
                public void handleResponse(List<Alarm> response) {
                    mAdapter = new AlarmAdapter(response);
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void handleError(Exception e) {
                    Log.e(TAG, "handleError: ", e);
                }
            });
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
                mSwitch.setOnClickListener(v -> {

                    new FindAlarmByIdAsync(
                            Math.toIntExact(AlarmAdapter.this.getItemId(getAdapterPosition())),
                            requireContext(),
                            new DatabaseCallback<Alarm>() {

                                @Override
                                public void handleResponse(Alarm response) {
                                    response.setAlarmSet(mSwitch.isChecked());
                                    if (mSwitch.isChecked()){
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(response.getAlarmTime());
                                        new AlarmSetter(requireContext()).setAlarm(cal);
                                    }
                                    new UpdateAlarmAsync(
                                            response,
                                            requireContext(),
                                            new DatabaseCallback<List<Alarm>>() {

                                                @Override
                                                public void handleResponse(List<Alarm> response) {
                                                    mAdapter.setAlarms(response);
                                                    mAdapter.notifyItemChanged(getAdapterPosition());
                                                }

                                                @Override
                                                public void handleError(Exception e) {

                                                }
                                            }
                                    );
                                }

                                @Override
                                public void handleError(Exception e) {

                                }
                            });

                    Log.i(TAG, "id: " + mAlarms.get(getAdapterPosition()).getId());
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
                Log.i(TAG, "alarms' id: " + mAlarms.get(getAdapterPosition()).getId());

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
        public long getItemId(int position) {
            return mAlarms.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return mAlarms.size();
        }

        public void setAlarms(List<Alarm> alarms){
            mAlarms = alarms;
        }
    }

}
