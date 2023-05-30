package com.example.daom.fragment;

import android.os.Bundle;
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

public class RegisterFragment2 extends Fragment {

    private View view;
    private Button mBtnMoveToEmail;
    private EditText mEtNickname;
    UserModel account;

    public RegisterFragment2() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_nickname, container, false);

        // Email 정보 입력
        mEtNickname = view.findViewById(R.id.et_register_nickname);
        mBtnMoveToEmail = view.findViewById(R.id.btn_register_moveToEmail);

        mBtnMoveToEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!(mEtNickname.getText().toString().equals(""))) {
                    // 번들을 통해 값 전달
                    Bundle bundle = new Bundle();
                    account = new UserModel();
                    // 객체 데이터를 보내기 위해 Serializable 사용
                    account.setNickname(mEtNickname.getText().toString());
                    bundle.putSerializable("user", account);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    RegisterFragment3 frag = new RegisterFragment3();
                    frag.setArguments(bundle);
                    transaction.replace(R.id.frame_layout_register, frag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "이름이나 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}