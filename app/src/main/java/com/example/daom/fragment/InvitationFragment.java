package com.example.daom.fragment;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.daom.R;
import com.example.daom.activity.InvitationActivity;

public class InvitationFragment extends Fragment {

    private Button mBtnMoveToTitle;

    private View view;

    public InvitationFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_invitation_test, container, false);

        mBtnMoveToTitle=(Button) view.findViewById(R.id.btn_invitation_moveToTitle);

        mBtnMoveToTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InvitationActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}