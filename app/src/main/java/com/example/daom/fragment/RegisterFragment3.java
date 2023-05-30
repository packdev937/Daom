package com.example.daom.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.daom.R;
import com.example.daom.util.UserModel;

import javax.annotation.Nullable;

public class RegisterFragment3 extends Fragment {

    private View view;
    private EditText mEtEmail;
    private Button mBtnMoveToPwd;
    private UserModel account;
    private String strNickname;

    public RegisterFragment3() {
        // Required empty public constructor
    }

    @Nullable // what is this?
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_email, container, false);
        account = new UserModel();
        if (getArguments() != null) {
            account = (UserModel) getArguments().getSerializable("user");
        }
        // nickname 이 잘 넘어오는지 확인
        Log.i("nickname", account.getNickname());
        mEtEmail = view.findViewById(R.id.et_register_email);
        mBtnMoveToPwd = view.findViewById(R.id.btn_register_moveToPassword);

        mBtnMoveToPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mEtEmail.getText().toString().equals(""))) {
                    Bundle bundle = new Bundle();
                    account.setEmailId(mEtEmail.getText().toString());
                    bundle.putSerializable("user", account);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    RegisterFragment4 frag = new RegisterFragment4();
                    frag.setArguments(bundle);
                    transaction.replace(R.id.frame_layout_register, frag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}