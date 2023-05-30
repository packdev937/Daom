package com.example.daom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.daom.R;
import com.example.daom.util.GroupModel;


public class InvitationFragment4 extends Fragment {

    private Button mBtnMoveToCode;
    private EditText mEtPenalty;

    private GroupModel groupModel;

    private View view;

    public InvitationFragment4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_invitation_penalty, container, false);

        groupModel = new GroupModel();
        if (getArguments() != null) {
            groupModel = (GroupModel) getArguments().getSerializable("group");
        }
        mBtnMoveToCode = (Button) view.findViewById(R.id.btn_invitation_moveToCode);
        mEtPenalty = (EditText) view.findViewById(R.id.et_invitation_penalty);

        mBtnMoveToCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int penalty = Integer.parseInt(mEtPenalty.getText().toString());
                Bundle bundle = new Bundle();
                groupModel.setGroupPenalty(penalty);
                bundle.putSerializable("group", groupModel);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                InvitationFragment5 frag = new InvitationFragment5();
                frag.setArguments(bundle);
                transaction.add(R.id.frame_layout_invitation, frag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
