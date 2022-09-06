package jf.clock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder>{
    private String[] mStrings;

    public AlarmAdapter(String[] params) {
        mStrings = params;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.alarm_id);

            // do things with your elements in each row
        }

        public void bind(String text){
            mTextView.setText(text);
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
        holder.bind(mStrings[position]);
    }

    @Override
    public int getItemCount() {
        return mStrings.length;
    }
}
