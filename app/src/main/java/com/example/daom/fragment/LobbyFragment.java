package com.example.daom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.daom.R;
import com.example.daom.activity.GroupInfoActivity;
import com.example.daom.activity.ResultActivity;
import com.example.daom.adapter.LobbyAdapter;
import com.example.daom.util.GroupModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class LobbyFragment extends Fragment {


    private ListView listView;
    private ArrayList<GroupModel> listGroupModel;
    private LobbyAdapter adapter;

    private View view;

    private FirebaseAuth auth;
    private FirebaseFirestore mFirestore;

    public LobbyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lobby, container, false);

        auth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //그룹 리스트 데이터 초기화
        this.InitializeGroupData();

        //리스트 뷰 초기화
        listView = view.findViewById(R.id.tv_lobby_listview);
        adapter = new LobbyAdapter(view.getContext(), listGroupModel);
        adapter.setItemClickListener(position -> {
            GroupModel groupModel = adapter.getItem(position);
            if (groupModel.getIsFinish()) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ResultActivity.class);
                intent.putExtra("group", groupModel);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity().getApplicationContext(), GroupInfoActivity.class);
                intent.putExtra("group", groupModel);
                startActivity(intent);
            }
        });

        //리스트뷰에 Adapter 연결
        listView.setAdapter(adapter);

        return view;
    }

    public void InitializeGroupData() {
        listGroupModel = new ArrayList<GroupModel>();

        mFirestore.collection("groups")
                .whereArrayContains("memberList", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                GroupModel group = document.toObject(GroupModel.class);

                                // 현재 시간 기준으로 초기화
                                String groupTime = group.getGroupDateTime();
                                String currentTime = getTime();
                                if (groupTime.compareTo(currentTime) < 0)
                                    group.setIsFinish(true);
                                int cnt = 0;
                                HashMap<String, Boolean> map = group.getArrivedInfo();
                                for(String keys : map.keySet()){
                                    if(map.get(keys))
                                        cnt++;
                                }
                                if(cnt == map.size())
                                    group.setIsFinish(true);

                                listGroupModel.add(group);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // empty set
                        }
                        Comparator<GroupModel> cmpDate = new Comparator<GroupModel>() {
                            @Override
                            public int compare(GroupModel groupModel1, GroupModel groupModel2) {
                                return groupModel1.getGroupDateTime().compareTo(groupModel2.getGroupDateTime());
                            }
                        };

                        Collections.sort(listGroupModel, cmpDate);
                    }
                });
    }

    private String getTime() {
        TimeZone tz;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd / HH : mm", Locale.KOREAN);
        tz = TimeZone.getTimeZone("Asia/Seoul");
        sdf.setTimeZone(tz);

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        return sdf.format(date);
    }
}