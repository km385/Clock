package jf.clock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AlarmRepeatBottomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "AlarmRepeatBottomDialog";

    private LinearLayout mStageTwo;
    private CheckBox mCheckBox1;
    private CheckBox mCheckBox2;
    private CheckBox mCheckBox3;
    private CheckBox mCheckBox4;
    private CheckBox mCheckBox5;
    private CheckBox mCheckBox6;
    private CheckBox mCheckBox7;

    private boolean[] mWeekDays = new boolean[7];

    public static AlarmRepeatBottomDialogFragment newInstance() {

        Bundle args = new Bundle();

        AlarmRepeatBottomDialogFragment fragment = new AlarmRepeatBottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_repeat_bottom_sheet_fragment, container, false);

        mStageTwo = view.findViewById(R.id.stage_two);
        
        mCheckBox1 = mStageTwo.findViewById(R.id.layout_monday).findViewById(R.id.checkBox2);
        mCheckBox2 = mStageTwo.findViewById(R.id.layout_tuesday).findViewById(R.id.checkBox3);
        mCheckBox3 = mStageTwo.findViewById(R.id.layout_wednesday).findViewById(R.id.checkBox4);
        mCheckBox4 = mStageTwo.findViewById(R.id.layout_thursday).findViewById(R.id.checkBox5);
        mCheckBox5 = mStageTwo.findViewById(R.id.layout_friday).findViewById(R.id.checkBox6);
        mCheckBox6 = mStageTwo.findViewById(R.id.layout_saturday).findViewById(R.id.checkBox7);
        mCheckBox7 = mStageTwo.findViewById(R.id.layout_sunday).findViewById(R.id.checkBox8);

        mStageTwo.findViewById(R.id.layout_monday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_tuesday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_wednesday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_thursday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_friday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_saturday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_sunday).setOnClickListener(this);

        TextView textView = view.findViewById(R.id.first_stage).findViewById(R.id.custom);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.first_stage).setVisibility(View.GONE);
                view.findViewById(R.id.stage_two).setVisibility(View.VISIBLE);
            }
        });



        // todo returns representation of week days in a form of some sort of array
        // todo make buttons for confirmation and rejection
        return view;
    }

    @Override
    public void onClick(View v) {
        int button = v.getId();
        if (button == R.id.layout_monday){
            mCheckBox1.setChecked(!mCheckBox1.isChecked());
            mWeekDays[0] = mCheckBox1.isChecked();
        }

        if (button == R.id.layout_tuesday){
            mCheckBox2.setChecked(!mCheckBox2.isChecked());
            mWeekDays[1] = mCheckBox2.isChecked();
        }
        if (button == R.id.layout_wednesday){
            mCheckBox3.setChecked(!mCheckBox3.isChecked());
            mWeekDays[2] = mCheckBox3.isChecked();
        }
        if (button == R.id.layout_thursday){
            mCheckBox4.setChecked(!mCheckBox4.isChecked());
            mWeekDays[3] = mCheckBox4.isChecked();
        }
        if (button == R.id.layout_friday){
            mCheckBox5.setChecked(!mCheckBox5.isChecked());
            mWeekDays[4] = mCheckBox5.isChecked();
        }
        if (button == R.id.layout_saturday){
            mCheckBox6.setChecked(!mCheckBox6.isChecked());
            mWeekDays[5] = mCheckBox6.isChecked();
        }
        if (button == R.id.layout_sunday){
            mCheckBox7.setChecked(!mCheckBox7.isChecked());
            mWeekDays[6] = mCheckBox7.isChecked();
        }
        for (int i = 0; i < mWeekDays.length;i++){
            Log.i(TAG, "current state of chosen week days: " + mWeekDays[i]);
        }
    }
}
