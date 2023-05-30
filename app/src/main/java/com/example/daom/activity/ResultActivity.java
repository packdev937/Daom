package com.example.daom.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.daom.util.GroupModel;
import com.example.daom.util.UserModel;
import com.example.daom.R;
import com.example.daom.adapter.MemberListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private TextView mTvGroupPlace, mTvGroupDateTime;
    private ImageButton mBtnGroupExit;
    private GroupModel group;

    private ListView listView;
    private ArrayList<String> memberUidArrayList;
    private ArrayList<UserModel> userModelArrayList;
    private MemberListAdapter memberListAdapter;

    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;
    String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        auth = FirebaseAuth.getInstance();
        currentUserUid = auth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();

        group = (GroupModel) getIntent().getSerializableExtra("group");

        mTvGroupPlace = findViewById(R.id.tv_result_place_detail);
        mTvGroupPlace.setText(group.getGroupPlace());

        mTvGroupDateTime = findViewById(R.id.tv_result_date_time_detail);
        mTvGroupDateTime.setText(group.getGroupDateTime());

        mBtnGroupExit = findViewById(R.id.btn_group_result_exitGroup);
        mBtnGroupExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitGroup();
            }
        });

        //멤버 리스트 데이터 초기화
        this.InitializeMemberData();

        //리스트 뷰 초기화
        listView = findViewById(R.id.lv_group_detail_friendsList);
        memberListAdapter = new MemberListAdapter(ResultActivity.this, userModelArrayList, group);

        listView.setAdapter(memberListAdapter);
    }

    public void exitGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);

        builder.setMessage("약속을 종료하시겠습니까?")
                .setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<String> memberList = group.getMemberList();
                        HashMap<String, Boolean> arrivedInfo = group.getArrivedInfo();
                        String currentUserUid = auth.getCurrentUser().getUid();
                        memberList.remove(currentUserUid);
                        arrivedInfo.remove(currentUserUid);

                        mFirestore.collection("groups")
                                .whereEqualTo("groupCode", group.getGroupCode())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                DocumentReference d = document.getReference();
                                                // 만약 내가 그룹에 마지막으로 남은 멤버라면 방을 삭제
                                                if (memberList.size() == 0)
                                                    d.delete();
                                                // 내가 그룹에 마지막으로 남아있는 멤버가 아닐 때
                                                else{
                                                    d.update("memberList", memberList);
                                                    d.update("arrivedInfo", arrivedInfo);
                                                }
                                            }
                                        }
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                });

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // empty
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void InitializeMemberData() {
        memberUidArrayList = group.getMemberList();

        userModelArrayList = new ArrayList<UserModel>();

        //가져온 memberUidArray를 이용하여 유저 목록(UserModel) 가져오기
        //가져온 userModel을 가지고 유저의 프로필사진, 닉네임 등을 세팅해줌.
        if (!memberUidArrayList.isEmpty()) {
            mFirestore.collection("users")
                    .whereIn("idToken", memberUidArrayList)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "InitializeMemberData - 유저 멤버 가져오기 성공.", task.getException());
                                for (DocumentSnapshot document : task.getResult()) {
                                    UserModel member = document.toObject(UserModel.class);
                                    userModelArrayList.add(member);
                                }
                                memberListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }
}