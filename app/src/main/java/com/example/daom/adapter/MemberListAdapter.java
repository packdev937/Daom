package com.example.daom.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.daom.R;
import com.example.daom.util.GroupModel;
import com.example.daom.util.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class MemberListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private List<UserModel> memberList;
    private FirebaseFirestore mFirestore;
    private GroupModel group;
    private Boolean isArrived = false;
    private int arrivedMemberNum;


    public MemberListAdapter(Context context, List<UserModel> list, GroupModel group) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        memberList = list;
        this.group = group;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public UserModel getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_member, null);
        TextView mTvArrivedInfo = view.findViewById(R.id.tv_list_arrivedInfo);
        TextView mTvPenaltyInfo = view.findViewById(R.id.tv_list_penaltyInfo);

        TextView memberName = view.findViewById(R.id.tv_list_memberNickname);
        memberName.setText(memberList.get(position).getNickname());

        String UserIdToken = memberList.get(position).getIdToken();
        HashMap<String, Boolean> arrivedInfo = group.getArrivedInfo();

        for (String key : arrivedInfo.keySet()) {
            if (arrivedInfo.get(key))
                arrivedMemberNum++;
        }

        if (arrivedInfo.containsKey(UserIdToken)) {
            if (arrivedInfo.get(UserIdToken) == true)
                isArrived = true;
            else
                isArrived = false;
        }

        int unArrivedMemberNum = arrivedInfo.size() - arrivedMemberNum;
        Log.i("helloworld", String.valueOf(unArrivedMemberNum));
        //  Log.i("hello", String.valueOf(arrivedInfo.size()));
        // Log.i("hello", String.valueOf(unArrivedMemberNum));
        if (isArrived) {
            if (group.getIsFinish()) {
                mTvPenaltyInfo.setVisibility(View.VISIBLE);
                mTvArrivedInfo.setVisibility(View.INVISIBLE);

                if (arrivedInfo.size() == arrivedMemberNum) {
                    mTvPenaltyInfo.setText("+ 0");
                    mTvPenaltyInfo.setTextColor(ColorStateList.valueOf(Color.parseColor("#6C6C6C")));
                } else {
                    mTvPenaltyInfo.setText("+" + (group.getGroupPenalty() * unArrivedMemberNum) / arrivedMemberNum);
                    mTvPenaltyInfo.setTextColor(ColorStateList.valueOf(Color.parseColor("#6C6C6C")));
                }
            } else {
                mTvPenaltyInfo.setVisibility(View.INVISIBLE);
                mTvArrivedInfo.setVisibility(View.VISIBLE);
                mTvArrivedInfo.setText("도착");
                mTvArrivedInfo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BC3333")));
            }
        } else {
            if (group.getIsFinish()) {
                mTvPenaltyInfo.setVisibility(View.VISIBLE);
                mTvArrivedInfo.setVisibility(View.INVISIBLE);
                if (unArrivedMemberNum == arrivedInfo.size())
                    mTvPenaltyInfo.setText("- " + 0);
                else
                    mTvPenaltyInfo.setText("- " + group.getGroupPenalty());
                mTvPenaltyInfo.setTextColor(ColorStateList.valueOf(Color.parseColor("#6C6C6C")));
            } else {
                mTvPenaltyInfo.setVisibility(View.INVISIBLE);
                mTvArrivedInfo.setVisibility(View.VISIBLE);
                mTvArrivedInfo.setText("미도착");
                mTvArrivedInfo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6C6C6C")));
            }
        }
        arrivedMemberNum = 0;
        unArrivedMemberNum = 0;
        return view;
    }
}
