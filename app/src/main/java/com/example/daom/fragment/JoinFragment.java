package com.example.daom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.daom.R;
import com.example.daom.activity.MainActivity;
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

public class JoinFragment extends Fragment {

    private View view;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFireStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_join, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        // 참여하기 버튼
        Button mBtnJoin = view.findViewById(R.id.btn_join_acceptJoin);
        EditText mEtGroupCode = view.findViewById(R.id.et_join_groupCode);

        mBtnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupCode = mEtGroupCode.getText().toString();

                mFireStore.collection("groups")
                        .whereEqualTo("groupCode", groupCode)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        DocumentReference ref = documentSnapshot.getReference();

                                        GroupModel groupModel = documentSnapshot.toObject(GroupModel.class);
                                        ArrayList<String> list = groupModel.getMemberList();
                                        list.add(mFirebaseAuth.getCurrentUser().getUid());

                                        // ArrayList 넣기
                                        HashMap<String, Boolean> arrivedInfo = groupModel.getArrivedInfo();
                                        arrivedInfo.put(mFirebaseAuth.getCurrentUser().getUid(), false);

                                        // ArrayList 넣기
                                        ref.update("memberList", list);
                                        ref.update("arrivedInfo", arrivedInfo);

                                        Toast.makeText(getActivity().getApplicationContext(), "그룹 참여하기 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "해당 그룹이 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "그룹 조회에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return view;
    }
}