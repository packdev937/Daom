package com.example.daom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.daom.R;
import com.example.daom.fragment.InvitationFragment1;

public class InvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        InvitationFragment1 frag = new InvitationFragment1();
        transaction.replace(R.id.frame_layout_invitation, frag);
        transaction.commit();
    }
}