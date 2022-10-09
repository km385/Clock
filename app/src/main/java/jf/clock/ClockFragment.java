package jf.clock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jf.clock.adapter.AlarmAdapter;
import jf.clock.data.Alarm;
import jf.clock.repositories.DatabaseCallback;
import jf.clock.repositories.DbQueries;

public class ClockFragment extends Fragment {
    private static final String TAG = "ClockFragment";

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private AlarmAdapter mAdapter;
    private FloatingActionButton mFab;
    private DbQueries mDbQueries;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbQueries = new DbQueries(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.clock_fragment, container, false);

        getChildFragmentManager().setFragmentResultListener("updateAlarm", this,
                (requestKey, result) -> {
            int pos = result.getInt("position");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) result.getSerializable("date"));

            mDbQueries.findAlarm(mAdapter.getItemId(pos), new DatabaseCallback<Alarm>() {
                @Override
                public void handleResponse(Alarm response) {
                    response.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                    response.setMinutes(calendar.get(Calendar.MINUTE));

                    mDbQueries.updateAlarm(response, new DatabaseCallback<List<Alarm>>() {
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

                }
            });


        });

        getChildFragmentManager().setFragmentResultListener("addAlarm", this,
                (requestKey, result) -> {
                    // TODO find a good way to set a id manually
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime((Date) result.getSerializable("date"));
                    Alarm alarm = new Alarm();
                    alarm.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                    alarm.setMinutes(calendar.get(Calendar.MINUTE));
                    alarm.setAlarmSet(false);
                    alarm.setWeekdays(new boolean[] {false, false, false, false, false, false, false});

                    mDbQueries.insertAlarm(alarm, new DatabaseCallback<List<Alarm>>() {
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
                long id = mAdapter.getItemId(viewHolder.getAdapterPosition());
                cancelAlarm(id);
                mDbQueries.deleteAlarm(id, new DatabaseCallback<List<Alarm>>() {
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
            mDbQueries.getAlarms(new DatabaseCallback<List<Alarm>>() {
                @Override
                public void handleResponse(List<Alarm> response) {
                    mAdapter = new AlarmAdapter(response ,requireContext());
                    mAdapter.setCallbacks(new AlarmAdapter.OnItemChangedListener() {
                        @Override
                        public void handleEvent(Calendar calendar, int pos) {
                            FragmentManager fm = getChildFragmentManager();
                            TimePicker dialog = TimePicker.newInstance(calendar.getTime(), pos);
                            dialog.show(fm, "dialog");
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void handleError(Exception e) {
                    Log.e(TAG, "handleError: ", e);
                }
            });
        }

    }

    private void cancelAlarm(long id) {
        mDbQueries.findAlarm(id, new DatabaseCallback<Alarm>() {
            @Override
            public void handleResponse(Alarm response) {
                new AlarmSetter(requireContext()).cancelAlarm(response);
            }

            @Override
            public void handleError(Exception e) {

            }
        });
    }



}
