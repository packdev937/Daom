package com.example.daom.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.daom.R;
import com.example.daom.activity.MainActivity;
import com.example.daom.util.GroupModel;
import com.example.daom.util.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


public class InvitationFragment5 extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore;

    private ConstraintLayout mClMoveToMain;
    private TextView mTvGroupCode;

    private GroupModel groupModel;

    private View view;
    private String groupCode;

    public InvitationFragment5() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_invitation_groupcode, container, false);

        groupModel = new GroupModel();
        if (getArguments() != null) {
            groupModel = (GroupModel) getArguments().getSerializable("group");
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mClMoveToMain = (ConstraintLayout) view.findViewById(R.id.layout_invitation_groupCode);
        mTvGroupCode = (TextView) view.findViewById(R.id.tv_invitation_groupCode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            groupCode = createCode();
        }
        mTvGroupCode.setText(groupCode);

        mClMoveToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupModel.setGroupCode(groupCode);
                groupModel.setCreateTime(getTime());
                groupModel.setIsFinish(false);

                // 현재 계정의 UserId
                String currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
                groupModel.setLeaderUid(currentUserUid);

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
                                        Log.d("hello", member.getNickname());
                                        groupModel.setLeaderNickname(member.getNickname());
                                    }
                                }
                            }
                        });

                // memberList의 초기 멤버는 방을 만든 사람
                ArrayList<String> memberList = new ArrayList<>();
                memberList.add(currentUserUid);
                groupModel.setMemberList(memberList);

                HashMap<String, Boolean> arrivedInfo = new HashMap<>();
                arrivedInfo.put(currentUserUid, false);
                groupModel.setArrivedInfo(arrivedInfo);

                mFirestore.collection("groups")
                        .whereEqualTo("groupCode", groupCode)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // GroupCode가 중복될 경우
                                    if (!task.getResult().isEmpty()) {
                                        Toast.makeText(getContext().getApplicationContext(), "그룹 코드가 중복되는 그룹이 이미 존재합니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mFirestore.collection("groups")
                                                .add(groupModel)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(getActivity().getApplicationContext(), "그룹생성 완료!", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity().getApplicationContext(), "그룹생성 실패", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });

        return view;
    }

    // minSdkVersion이 지정한 버전보다 낮을 경우 바로 호출 시에는 컴파일 에러 발생
    // 조건문을 통한 분기 처리를 통해 호출해야 에러가 발생하지 않음
    @RequiresApi(api = Build.VERSION_CODES.N)

    // 랜덤한 그룹 코드 여섯 자리를 입력받기
    public static String createCode() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 6;
        Random random = new Random();
        String groupCode = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return groupCode;
    }

    private String getTime() {
        TimeZone tz;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
        tz = TimeZone.getTimeZone("Asia/Seoul");
        sdf.setTimeZone(tz);

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        return sdf.format(date);
    }
}
