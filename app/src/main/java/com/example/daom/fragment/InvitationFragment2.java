package com.example.daom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.daom.R;
import com.example.daom.activity.MapActivity;
import com.example.daom.util.GroupModel;


public class InvitationFragment2 extends Fragment {

    private Button mBtnMoveToDateTime;
    private ImageButton mBtnMoveToMap;
    private EditText mEtPlace;

    private GroupModel groupModel;

    private View view;
    private double latitude, longitude;

    public InvitationFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_invitation_place, container, false);

        groupModel = new GroupModel();
        if (getArguments() != null) {
            groupModel = (GroupModel) getArguments().getSerializable("group");
        }

        Bundle bundle = new Bundle();
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");

        mEtPlace = (EditText) view.findViewById(R.id.et_invitation_groupPlace);
        mBtnMoveToDateTime = (Button) view.findViewById(R.id.btn_register_moveToDateTime);
        mBtnMoveToMap = (ImageButton) view.findViewById(R.id.btn_invitation_groupPlace);

        mBtnMoveToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                launcher.launch(intent);
            }
        });
        mBtnMoveToDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mEtPlace.getText().toString().equals(""))) {
                    Bundle bundle = new Bundle();
                    groupModel.setGroupPlace(mEtPlace.getText().toString());
                    groupModel.setGpsLatitude(latitude);
                    groupModel.setGpsLongitude(longitude);
                    bundle.putSerializable("group", groupModel);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    InvitationFragment3 frag = new InvitationFragment3();
                    frag.setArguments(bundle);
                    transaction.replace(R.id.frame_layout_invitation, frag);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "약속 장소를 정해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();

                        latitude = intent.getDoubleExtra("latitude", 0);
                        longitude = intent.getDoubleExtra("longitude", 0);
                    }
                }
            });
}
