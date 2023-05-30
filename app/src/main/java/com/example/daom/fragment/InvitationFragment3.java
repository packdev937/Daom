package com.example.daom.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.daom.R;
import com.example.daom.util.GroupModel;

import java.util.Calendar;


public class InvitationFragment3 extends Fragment {

    private Button mBtnMoveToPenalty;
    private TextView mTvDate, mTvTime;
    private ImageButton mBtnDate, mBtnTime;

    private GroupModel groupModel;

    private View view;

    public InvitationFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_invitation_datetime, container, false);

        groupModel = new GroupModel();
        if (getArguments() != null) {
            groupModel = (GroupModel) getArguments().getSerializable("group");
        }

        mBtnMoveToPenalty = (Button) view.findViewById(R.id.btn_register_moveToPenalty);
        mTvDate = (TextView) view.findViewById(R.id.tv_invitation_groupDate);
        mTvTime = (TextView) view.findViewById(R.id.tv_invitation_groupTime);
        mBtnDate = (ImageButton) view.findViewById(R.id.btn_invitation_groupDate);
        mBtnTime = (ImageButton) view.findViewById(R.id.btn_invitation_groupTime);

        // 날짜 선택 다이얼로그
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date;
                if (month + 1 < 10) {
                    date = year + ".0" + (month + 1);
                } else {
                    date = year + "." + (month + 1);
                }
                if (dayOfMonth < 10) {
                    date = date + ".0" + dayOfMonth;
                } else {
                    date = date + "." + dayOfMonth;
                }
                mTvDate.setText(date);
            }
        };

        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDialogDate = new DatePickerDialog(
                        getContext(),
                        R.style.customDatePickerStyle,
                        dateSetListener,
                        year,
                        month,
                        day);
                mDialogDate.show();
            }
        });

        // 시간 선택 다이얼로그
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int min) {
                String time;
                if (hour < 10) {
                    time = "0" + hour + " : ";
                } else {
                    time = hour + " : ";
                }
                if (min < 10) {
                    time = time + "0" + min;
                } else {
                    time = time + min;
                }
                mTvTime.setText(time);
            }
        };

        mBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);

                TimePickerDialog mDialogTime = new TimePickerDialog(
                        getContext(),
                        R.style.customTimePickerStyle,
                        timeSetListener,
                        hour,
                        min,
                        DateFormat.is24HourFormat(getContext()));
                mDialogTime.show();
            }
        });


        mBtnMoveToPenalty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = mTvTime.getText().toString();
                String timeCheck = "약속 시간을 정해주세요";
                String date = mTvDate.getText().toString();
                String dateCheck = "약속 날짜를 정해주세요";
                String dateTime = date + " / " + time;

                if (date.compareTo(dateCheck) == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "약속 날짜를 정해주세요.", Toast.LENGTH_SHORT).show();
                } else if (time.compareTo(timeCheck) == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "약속 시간을 정해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    groupModel.setGroupDateTime(dateTime);
                    bundle.putSerializable("group", groupModel);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    InvitationFragment4 frag = new InvitationFragment4();
                    frag.setArguments(bundle);
                    transaction.replace(R.id.frame_layout_invitation, frag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return view;
    }
}
