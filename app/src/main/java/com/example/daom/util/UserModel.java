package com.example.daom.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UserModel implements Serializable {

    private String idToken;         //  Firebase Uid(고유 토큰정보)
    private String emailId;         //  이메일 아이디
    private String password;        // 비밀번호
    private String nickname;

    // empty constructor
    public UserModel() {
    }

    public UserModel(String idToken, String emailId, String password, String nickname) {
        this.idToken = idToken;
        this.emailId = emailId;
        this.password = password;
        this.nickname = nickname;
    }

    //getter and setter 세팅
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
