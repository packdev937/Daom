package com.example.daom.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeNicknameActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore;

    private EditText mEtChangeNickname;
    private Button mBtnChange;

    private String changedNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname);

        mEtChangeNickname = (EditText) findViewById(R.id.et_change_Nickname);
        mBtnChange = (Button) findViewById(R.id.btn_changeNickname_complete);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String CurrentUserUid = mFirebaseAuth.getCurrentUser().getUid();

        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedNickname = mEtChangeNickname.getText().toString();

                if (!changedNickname.equals("")) {
                    Log.i("daom", "닉네임 변경");
                    DocumentReference docRef = mFirestore.collection("users").document(CurrentUserUid);
                    docRef.update("nickname", changedNickname);
                    Toast.makeText(ChangeNicknameActivity.this, "성공적으로 닉네임을 변경하였습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangeNicknameActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangeNicknameActivity.this, "닉네임 변경을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}