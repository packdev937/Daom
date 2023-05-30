package com.example.daom.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.daom.R;
import com.example.daom.util.GroupModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;

public class WithdrawActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore;

    private Button mBtnWithdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mBtnWithdraw = (Button) findViewById(R.id.btn_withdraw_complete);

        mBtnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentID = mFirebaseAuth.getCurrentUser().getUid();
                mFirestore.collection("users").document(currentID).delete();

                // 내가 방장으로 있는 모든 방들 삭제
                mFirestore.collection("groups")
                        .whereEqualTo("leaderUid", currentID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for(DocumentSnapshot documentSnapshot : task.getResult()) {
                                        DocumentReference documentReference = documentSnapshot.getReference();

                                        documentReference.delete();
                                    }

                                }
                            }
                        });

                // 내가 들어가 있는 모든 방들에서의 내 정보 삭제
                mFirestore.collection("groups")
                        .whereArrayContains("memberList", currentID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        DocumentReference documentReference = documentSnapshot.getReference();

                                        GroupModel groupModel = documentSnapshot.toObject(GroupModel.class);
                                        HashMap<String, Boolean> arrivedInfo = groupModel.getArrivedInfo();
                                        ArrayList<String> list = groupModel.getMemberList();
                                        list.remove(currentID);
                                        arrivedInfo.remove(currentID);

                                        documentReference.update("memberList", list);
                                        documentReference.update("arrivedInfo", arrivedInfo);

                                    }
                                } else {
                                    //empty
                                }
                            }
                        });
                mFirebaseAuth.getCurrentUser().delete();
                Intent intent = new Intent(WithdrawActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}