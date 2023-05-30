package com.example.daom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.daom.R;
import com.example.daom.util.GroupModel;

public class InvitationFragment1 extends Fragment {

    private Button mBtnMoveToMap;
    private EditText mEtGroupTitle;
    private String strGroupTitle;

    private GroupModel groupModel;

    private View view;

    public InvitationFragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_invitation_grouptitle, container, false);

        mBtnMoveToMap = (Button) view.findViewById(R.id.btn_invitation_moveToPlace);
        mEtGroupTitle = (EditText) view.findViewById(R.id.et_invitation_groupTitle);

        mBtnMoveToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strGroupTitle = mEtGroupTitle.getText().toString();
                if(!strGroupTitle.equals("")) {

                    Bundle bundle = new Bundle();
                    groupModel = new GroupModel();
                    // 객체 데이터를 보내기 위해 Serializable 사용
                    groupModel.setGroupTitle(strGroupTitle);

                    // InvitationFragment2로 넘어감
                    bundle.putSerializable("group", groupModel);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    InvitationFragment2 frag = new InvitationFragment2();
                    frag.setArguments(bundle);
                    transaction.replace(R.id.frame_layout_invitation, frag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "약속 이름을 정해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
