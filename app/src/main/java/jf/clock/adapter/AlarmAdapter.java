package jf.clock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

import jf.clock.AlarmSetter;
import jf.clock.ClockFragment;
import jf.clock.ClockFragmentDirections;
import jf.clock.R;
import jf.clock.TimePicker;
import jf.clock.data.Alarm;
import jf.clock.repositories.DatabaseCallback;
import jf.clock.repositories.FindAlarmByIdAsync;
import jf.clock.repositories.UpdateAlarmStatusAsync;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder>{
    private List<Alarm> mAlarms;
    private ClockFragment mCallbacks;
    private OnItemChangedListener mOnItemChangedListener;
    private AlarmSetter mAlarmSetter;
    private Context mContext;

    public interface OnItemChangedListener{
        void handleEvent(Calendar calendar, int pos);
    }

    public void setCallbacks(OnItemChangedListener listener){
        mOnItemChangedListener = listener;
    }

    public AlarmAdapter(List<Alarm> alarms, Context context) {
        mAlarms = alarms;
        mContext = context;
        mAlarmSetter = new AlarmSetter(mContext);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;
        private SwitchCompat mSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView = itemView.findViewById(R.id.alarm_id);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, mAlarms.get(getAdapterPosition()).getHour());
                    calendar.set(Calendar.MINUTE, mAlarms.get(getAdapterPosition()).getMinutes());

                    mOnItemChangedListener.handleEvent(calendar, getAdapterPosition());
                }
            });

            mSwitch = itemView.findViewById(R.id.alarm_switch);
            mSwitch.setOnClickListener(v -> {
                long alarmId = AlarmAdapter.this.getItemId(getAdapterPosition());
                new UpdateAlarmStatusAsync(alarmId, mSwitch.isChecked(), mContext,
                        new DatabaseCallback<List<Alarm>>() {
                            @Override
                            public void handleResponse(List<Alarm> response) {
                                if (mSwitch.isChecked()) {
                                    setAlarm(alarmId);
                                } else {
                                    cancelAlarm(alarmId);
                                }
                                setAlarms(response);
                                notifyItemChanged(getAdapterPosition());
                            }

                            @Override
                            public void handleError(Exception e) {

                            }
                        });

                //Log.i(TAG, "id: " + mAlarms.get(getAdapterPosition()).getId());
            });

            // do things with your elements in each row
        }

        public void bind(Alarm alarm){

            mTextView.setText(String.format("%02d:", alarm.getHour()) + String.format("%02d", alarm.getMinutes()));
            //mTextView.setText(alarm.getId());
            mSwitch.setChecked(alarm.isAlarmSet());
        }



        @Override
        public void onClick(View v) {
            // TODO changing switch right after creating an alarm does not work
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, mAlarms.get(getAdapterPosition()).getHour());
            calendar.set(Calendar.MINUTE, mAlarms.get(getAdapterPosition()).getMinutes());

            //mOnItemChangedListener.handleEvent(calendar, getAdapterPosition());

            long alarmId = AlarmAdapter.this.getItemId(getAdapterPosition());
            new FindAlarmByIdAsync(alarmId, v.getContext(), new DatabaseCallback<Alarm>() {
                @Override
                public void handleResponse(Alarm response) {
                    NavDirections action = jf.clock.ClockFragmentDirections.actionClockFragmentToAlarmDetailsFragment(response);
                    Navigation.findNavController(v).navigate(action);
                }

                @Override
                public void handleError(Exception e) {

                }
            });



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

    private void setAlarm(long id) {
        new FindAlarmByIdAsync(id, mContext, new DatabaseCallback<Alarm>() {
            @Override
            public void handleResponse(Alarm response) {
                mAlarmSetter.setAlarm(response);
            }

            @Override
            public void handleError(Exception e) {

            }
        });
    }

    private void cancelAlarm(long id) {
        new FindAlarmByIdAsync(id, mContext, new DatabaseCallback<Alarm>() {
            @Override
            public void handleResponse(Alarm response) {

                mAlarmSetter.cancelAlarm(response);
            }

            @Override
            public void handleError(Exception e) {

            }
        });
    }
}
