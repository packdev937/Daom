package com.example.daom.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupModel implements Serializable {
    private String groupTitle;
    private String groupCode;
    private String groupPlace;
    private String createTime;
    private String leaderUid;
    private String groupDateTime;
    private String leaderNickname;
    private int groupPenalty;
    private double gpsLatitude;
    private double gpsLongitude;
    private ArrayList<String> memberList; //리더포함
    private HashMap<String, Boolean> arrivedInfo;
    private Boolean isFinish;

    public GroupModel() {
    }

    public String getLeaderNickname() {
        return leaderNickname;
    }

    public void setLeaderNickname(String leaderNickname) {
        this.leaderNickname = leaderNickname;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGroupDateTime() {
        return groupDateTime;
    }

    public void setGroupDateTime(String groupDateTime) {
        this.groupDateTime = groupDateTime;
    }

    public String getLeaderUid() {
        return leaderUid;
    }

    public void setLeaderUid(String leaderUid) {
        this.leaderUid = leaderUid;
    }

    public double getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(double gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public double getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public String getGroupPlace() {
        return groupPlace;
    }

    public void setGroupPlace(String groupPlace) {
        this.groupPlace = groupPlace;
    }

    public ArrayList<String> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<String> memberList) {
        this.memberList = memberList;
    }

    public int getGroupPenalty() {
        return groupPenalty;
    }

    public void setGroupPenalty(int groupPenalty) {
        this.groupPenalty = groupPenalty;
    }

    public HashMap<String, Boolean> getArrivedInfo() {
        return arrivedInfo;
    }

    public void setArrivedInfo(HashMap<String, Boolean> arrivedInfo) {
        this.arrivedInfo = arrivedInfo;
    }

    public Boolean getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Boolean finish) {
        isFinish = finish;
    }
}
