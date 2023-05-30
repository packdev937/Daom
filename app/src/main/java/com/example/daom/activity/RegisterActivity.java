package com.example.daom.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.daom.R;
import com.example.daom.fragment.RegisterFragment1;

public class RegisterActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        RegisterFragment1 frag = new RegisterFragment1();
        transaction.replace(R.id.frame_layout_register, frag);
        transaction.commit();
    }
}