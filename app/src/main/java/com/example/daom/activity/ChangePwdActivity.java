package com.example.daom.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePwdActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirestore;

    private EditText mEtChangePwd, mEtChangePwd2;
    private TextView mTvIncorret;
    private Button mBtnChange;

    private String checkPwd, checkPwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        mEtChangePwd = (EditText) findViewById(R.id.et_change_pwd);
        mEtChangePwd2 = (EditText) findViewById(R.id.et_change_pwd2);
        mBtnChange = (Button) findViewById(R.id.btn_change_complete);
        mTvIncorret = (TextView) findViewById(R.id.incorrectPassword);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String CurrentUserUid = mFirebaseAuth.getCurrentUser().getUid();

        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPwd = mEtChangePwd.getText().toString();
                checkPwd2 = mEtChangePwd2.getText().toString();

                if (checkPwd.equals(checkPwd2)) {
                    Log.i("daom", "비밀번호 변경");
                    mTvIncorret.setVisibility(View.INVISIBLE);
                    mFirebaseAuth.getCurrentUser().updatePassword(checkPwd);
                    DocumentReference docRef = mFirestore.collection("users").document(CurrentUserUid);
                    docRef.update("password", checkPwd);
                    Toast.makeText(ChangePwdActivity.this, "성공적으로 비밀번호를 변경하였습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangePwdActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    mTvIncorret.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}