package com.example.daom.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.daom.R;
import com.example.daom.util.GroupModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LobbyAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<GroupModel> groupList;
    private ItemClickListener mItemClickListener;

    public LobbyAdapter(Context context, ArrayList<GroupModel> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        groupList = list;
    }

    public interface ItemClickListener {
        void onItemClick(int a_imageResId);
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public GroupModel getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;
        final Context context = viewGroup.getContext();

        if (view == null) {
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mLayoutInflater.inflate(R.layout.list_group, viewGroup, false);
        }
       // Log.i("hello", groupList.get(position).getLeaderNickname());

        TextView groupName = view.findViewById(R.id.tv_list_groupTitle);
        TextView groupPlace = view.findViewById(R.id.tv_list_groupPlace);
        TextView groupDateTime = view.findViewById(R.id.tv_list_groupDateTime);
        TextView groupLeader = view.findViewById(R.id.tv_list_groupLeader);

        groupLeader.setText(groupList.get(position).getLeaderNickname());
        groupName.setText(groupList.get(position).getGroupTitle());
        groupDateTime.setText(groupList.get(position).getGroupDateTime());
        groupPlace.setText(groupList.get(position).getGroupPlace());

        Boolean isFinish = groupList.get(position).getIsFinish();

        ConstraintLayout groupListLayout = view.findViewById(R.id.item_group);
        // 약속의 종료 여부에 따라 그룹 객체 색 변경
        if (isFinish) {
            groupListLayout.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        } else {
            groupListLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#67ABEF")));
        }

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
        return view;
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

    public void setItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }
}
