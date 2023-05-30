package com.example.daom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.daom.R;
import com.example.daom.activity.LoginActivity;
import com.example.daom.util.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterFragment4 extends Fragment {

    private View view;
    private EditText mEtPwd, mEtPwdCheck;
    private TextView mTvWrongPwd;
    private Button mBtnComplete;
    private UserModel account;
    private FirebaseAuth mFirebaseAuth;                 // 파이어베이스 인증
    private FirebaseFirestore mFireStore;
    private String strEmail, strPwd, strPwdCheck, strNickname;

    public RegisterFragment4() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_password, container, false);
        account = new UserModel();
        if (getArguments() != null) {
            account = (UserModel) getArguments().getSerializable("user");
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mEtPwd = view.findViewById(R.id.et_register_password);
        mEtPwdCheck = view.findViewById(R.id.et_register_password2);
        mBtnComplete = view.findViewById(R.id.btn_register_complete);
        mTvWrongPwd = view.findViewById(R.id.incorrectPassword);

        mBtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strNickname = account.getNickname();
                strEmail = account.getEmailId();
                strPwd = mEtPwd.getText().toString();
                strPwdCheck = mEtPwdCheck.getText().toString();

                //Firebase Auth 진행
                if (strPwd.equals(strPwdCheck)) {
                    mTvWrongPwd.setVisibility(View.INVISIBLE);
                    if (!strPwd.equals("")) {
                        mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    account = new UserModel(firebaseUser.getUid(), firebaseUser.getEmail(), strPwd, strNickname);

                                    mFireStore.collection("users").document(firebaseUser.getUid()).set(account);
                                    Toast.makeText(getActivity(), "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mTvWrongPwd.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
}