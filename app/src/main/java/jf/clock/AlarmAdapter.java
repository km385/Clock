package jf.clock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder>{
    private String[] mStrings;
    private Alarm mAlarm;
    private List<Alarm> mAlarms;

    public AlarmAdapter(String[] params) {
        mStrings = params;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,10);
        calendar.set(Calendar.MINUTE, 9);
        mAlarm = new Alarm();
        mAlarm.setAlarmTime(calendar.getTime());
    }

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
            mTextView.setText(alarm.getAlarmTime().toString());
        }

        @Override
        public void onClick(View v) {
            // TODO date is saved as String, change to Date if possible
            Date date = mAlarms.get(getAdapterPosition()).getAlarmTime();
            NavDirections action = ClockFragmentDirections.actionClockFragmentToTimePicker(date);
            Navigation.findNavController(v).navigate(action);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//            View v = layoutInflater.inflate(R.layout.alarm_row, parent, false);
        return new ViewHolder(LayoutInflater
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
