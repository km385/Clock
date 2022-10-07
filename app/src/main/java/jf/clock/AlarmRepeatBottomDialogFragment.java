package jf.clock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class AlarmRepeatBottomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "AlarmRepeatBottomDialog";

    private LinearLayout mStageTwo;
    private LinearLayout mStageOne;
    private ArrayList<CheckBox> mCheckBoxes = new ArrayList<>();

    private boolean[] mWeekDays = new boolean[7];

    public static AlarmRepeatBottomDialogFragment newInstance(boolean[] alarm) {

        Bundle args = new Bundle();
        args.putBooleanArray("alarm", alarm);
        AlarmRepeatBottomDialogFragment fragment = new AlarmRepeatBottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_repeat_bottom_sheet_fragment, container, false);

        setup();


        mStageTwo = view.findViewById(R.id.stage_two);
        mStageOne = view.findViewById(R.id.first_stage);
        mCheckBoxes.add(mStageTwo.findViewById(R.id.layout_monday).findViewById(R.id.checkBox2));
        mCheckBoxes.add(mStageTwo.findViewById(R.id.layout_tuesday).findViewById(R.id.checkBox3));
        mCheckBoxes.add(mStageTwo.findViewById(R.id.layout_wednesday).findViewById(R.id.checkBox4));
        mCheckBoxes.add(mStageTwo.findViewById(R.id.layout_thursday).findViewById(R.id.checkBox5));
        mCheckBoxes.add(mStageTwo.findViewById(R.id.layout_friday).findViewById(R.id.checkBox6));
        mCheckBoxes.add(mStageTwo.findViewById(R.id.layout_saturday).findViewById(R.id.checkBox7));
        mCheckBoxes.add(mStageTwo.findViewById(R.id.layout_sunday).findViewById(R.id.checkBox8));

        mStageTwo.findViewById(R.id.layout_monday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_tuesday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_wednesday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_thursday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_friday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_saturday).setOnClickListener(this);
        mStageTwo.findViewById(R.id.layout_sunday).setOnClickListener(this);



        TextView textView = mStageOne.findViewById(R.id.custom);
        textView.setOnClickListener(v -> {
            Log.i(TAG, "onClick: " + mCheckBoxes.size());
            for (int i = 0; i < mCheckBoxes.size();i++){
                mCheckBoxes.get(i).setChecked(mWeekDays[i]);
            }

            view.findViewById(R.id.first_stage).setVisibility(View.GONE);
            view.findViewById(R.id.stage_two).setVisibility(View.VISIBLE);
        });

        mStageOne.findViewById(R.id.once).setOnClickListener(v -> Arrays.fill(mWeekDays, false));

        mStageOne.findViewById(R.id.mon_fri).setOnClickListener(v -> {
            Arrays.fill(mWeekDays, true);
            mWeekDays[5] = false;
            mWeekDays[6] = false;

        });

        mStageOne.findViewById(R.id.daily).setOnClickListener(v -> Arrays.fill(mWeekDays, true));

        Button accept = view.findViewById(R.id.accept_button);
        accept.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (mStageTwo.getVisibility() == View.VISIBLE){
                for (int i = 0;i < mCheckBoxes.size();i++){
                    mWeekDays[i] = mCheckBoxes.get(i).isChecked();
                }
            }

            bundle.putBooleanArray("array", mWeekDays);
            getParentFragmentManager().setFragmentResult("weekDays", bundle);
            dismiss();
        });

        Button reject = view.findViewById(R.id.reject_button);
        reject.setOnClickListener(v -> dismiss());

        return view;
    }

    private void setup() {
        mWeekDays = (boolean[]) getArguments().getBooleanArray("alarm");
    }


    @Override
    public void onClick(View v) {
        int button = v.getId();
        if (button == R.id.layout_monday){
            mCheckBoxes.get(0).setChecked(!mCheckBoxes.get(0).isChecked());
            mWeekDays[0] = mCheckBoxes.get(0).isChecked();
        }else if (button == R.id.layout_tuesday){
            mCheckBoxes.get(1).setChecked(!mCheckBoxes.get(1).isChecked());
            mWeekDays[1] = mCheckBoxes.get(1).isChecked();
        }else if (button == R.id.layout_wednesday){
            mCheckBoxes.get(2).setChecked(!mCheckBoxes.get(2).isChecked());
            mWeekDays[2] = mCheckBoxes.get(2).isChecked();
        }else if (button == R.id.layout_thursday){
            mCheckBoxes.get(3).setChecked(!mCheckBoxes.get(3).isChecked());
            mWeekDays[3] = mCheckBoxes.get(3).isChecked();
        }else if (button == R.id.layout_friday){
            mCheckBoxes.get(4).setChecked(!mCheckBoxes.get(4).isChecked());
            mWeekDays[4] = mCheckBoxes.get(4).isChecked();
        }else if (button == R.id.layout_saturday){
            mCheckBoxes.get(5).setChecked(!mCheckBoxes.get(5).isChecked());
            mWeekDays[5] = mCheckBoxes.get(5).isChecked();
        }else if (button == R.id.layout_sunday){
            mCheckBoxes.get(6).setChecked(!mCheckBoxes.get(6).isChecked());
            mWeekDays[6] = mCheckBoxes.get(6).isChecked();
        }
    }
}
