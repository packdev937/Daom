package com.example.daom.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class GroupInfoActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    private double distance;                // 현재 위치와 목적지 사이 거리
    Button mBtnArrived;                      // 도착 버튼

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private TextView mTvGroupTitle, mTvGroupPlace, mTvGroupDateTime, mTvGroupCode;
    private ImageButton mBtnExitGroup;
    private GroupModel group;

    private ListView listView;
    private ArrayList<String> memberUidArrayList;
    private ArrayList<UserModel> userModelArrayList;
    private MemberListAdapter memberListAdapter;
    private HashMap<String, Boolean> arrivedInfo;

    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;
    String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        auth = FirebaseAuth.getInstance();
        currentUserUid = auth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();

        group = (GroupModel) getIntent().getSerializableExtra("group");

        mTvGroupTitle = findViewById(R.id.tv_result_title);
        mTvGroupTitle.setText(group.getGroupTitle());

        mTvGroupPlace = findViewById(R.id.tv_result_place_detail);
        mTvGroupPlace.setText(group.getGroupPlace());

        mTvGroupDateTime = findViewById(R.id.tv_result_date_time_detail);
        mTvGroupDateTime.setText(group.getGroupDateTime());

        mTvGroupCode = findViewById(R.id.tv_group_detail_groupCode);
        mTvGroupCode.setText(group.getGroupCode());

        //멤버 리스트 데이터 초기화
        this.InitializeMemberData();

        //리스트 뷰 초기화
        listView = findViewById(R.id.lv_group_detail_friendsList);
        memberListAdapter = new MemberListAdapter(GroupInfoActivity.this, userModelArrayList, group);

        listView.setAdapter(memberListAdapter);

        mBtnExitGroup = findViewById(R.id.btn_group_detail_exitGroup);
        mBtnExitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitGroup();
            }
        });

        Location locationA = new Location("point A");
        Location locationB = new Location("point B");
        locationA.setLatitude(group.getGpsLatitude());
        locationA.setLongitude(group.getGpsLongitude());

        mBtnArrived = findViewById(R.id.btn_arrive);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationB.setLatitude(location.getLatitude());
                locationB.setLongitude(location.getLongitude());
                distance = locationA.distanceTo(locationB);
                // 100m 이상 떨어져 있을 때
                if (distance > 100) {
                    mBtnArrived.setBackgroundResource(R.drawable.border_round_button_gray);
                    mBtnArrived.setEnabled(false);
                }
                // 100m 이내에 들어왔을 때
                else {
                    mBtnArrived.setBackgroundResource(R.drawable.border_round_button_red);
                    mBtnArrived.setEnabled(true);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        mBtnArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Hello", "도착 버튼 로그 확인");
                arrivedInfo = group.getArrivedInfo();
                arrivedInfo.put(currentUserUid, true);

                // firestore에 도착 버튼을 누르면 해당 key 값을 true로 바꿔주는 함수를 만들어야 할 듯
                mFirestore.collection("groups")
                        .whereEqualTo("groupCode", group.getGroupCode())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        DocumentReference d = document.getReference();
                                        d.update("arrivedInfo", arrivedInfo);
                                    }
                                }
                                Toast.makeText(GroupInfoActivity.this, "도착!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(GroupInfoActivity.this, MainActivity.class));
                            }
                        });
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
        }

    }

    public void exitGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupInfoActivity.this);

        builder.setMessage("약속을 취소하시겠습니까?")
                .setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<String> memberList = group.getMemberList();
                        HashMap<String, Boolean> arrivedInfo = group.getArrivedInfo();
                        String currentUserUid = auth.getCurrentUser().getUid();
                        memberList.remove(currentUserUid);
                        arrivedInfo.remove(currentUserUid);

                        //만약 그룹탈퇴를 요청한 사용자가 리더일 경우, 그룹 자체를 삭제
                        if (currentUserUid.equals(group.getLeaderUid())) {
                            mFirestore.collection("groups")
                                    .whereEqualTo("groupCode", group.getGroupCode())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    DocumentReference d = document.getReference();
                                                    d.delete();
                                                }
                                            }
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            //만약 그룹탈퇴를 요청한 사용자가 그룹원일 경우, 해당 그룹원을 멤버 리스트에서 제외
                        } else {
                            mFirestore.collection("groups")
                                    .whereEqualTo("groupCode", group.getGroupCode())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    DocumentReference d = document.getReference();
                                                    d.update("memberList", memberList);
                                                    d.update("arrivedInfo", arrivedInfo);
                                                }
                                            }
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        }
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