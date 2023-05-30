package com.example.daom.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daom.R;
import com.example.daom.activity.ChangeNicknameActivity;
import com.example.daom.activity.ChangePwdActivity;
import com.example.daom.activity.WithdrawActivity;
import com.example.daom.util.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class InformationFragment extends Fragment {

    private View view;
    private TextView mTvNickname, mTvIdToken;
    private ConstraintLayout mTvChangePwd, mTvWithdraw, mTvChangeNickname;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore;

    private String currentUserUid;

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_information, container, false);

        mTvNickname = (TextView) view.findViewById(R.id.tv_information_nickname);
        mTvIdToken = (TextView) view.findViewById(R.id.tv_information_idToken);
        mTvChangeNickname = (ConstraintLayout) view.findViewById(R.id.tv_information_changeNickname);
        mTvChangePwd = (ConstraintLayout) view.findViewById(R.id.tv_information_changePwd);
        mTvWithdraw = (ConstraintLayout) view.findViewById(R.id.tv_information_withdraw);


        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();

        // 현재 UserId를 통해 Collection에 저장되어 있는 users 문서에 접근하여 nickname을 얻어옴
        mFirestore.collection("users")
                .whereEqualTo("idToken", currentUserUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(MotionEffect.TAG, "InitializeMemberData - 유저 멤버 가져오기 성공.", task.getException());
                            for (DocumentSnapshot document : task.getResult()) {
                                UserModel member = document.toObject(UserModel.class);
                                mTvNickname.setText(member.getNickname());
                                mTvIdToken.setText(member.getIdToken());

                                Log.d("hello", member.getNickname());
                            }
                        }
                    }
                });

        mTvChangeNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeNicknameActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        mTvChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePwdActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        mTvWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WithdrawActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });



        return view;
    }

    public void moveToWithdraw() {
        Intent intent = new Intent(getActivity(), WithdrawActivity.class);
        startActivity(intent);
    }
}