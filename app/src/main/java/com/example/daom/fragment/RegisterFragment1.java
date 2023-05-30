package com.example.daom.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.daom.R;

public class RegisterFragment1 extends Fragment {

    private View view;
    private Button mBtnStart;

    public RegisterFragment1() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.register_app_announcement, container, false);

        mBtnStart = view.findViewById(R.id.btn_register_start);
        mBtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                RegisterFragment2 frag = new RegisterFragment2();
                transaction.replace(R.id.frame_layout_register, frag);
                transaction.commit();
            }
        });
        return view;
    }
}