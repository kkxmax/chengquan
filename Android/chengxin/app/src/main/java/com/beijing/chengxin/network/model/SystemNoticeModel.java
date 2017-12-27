package com.beijing.chengxin.network.model;

/**
 * Created by start on 2017.11.07.
 */

public class SystemNoticeModel {
    String msgTitle;
    String msgContent;
    int accountId;
    int errorId;
    int estimateId;
    int inviteeId;
    int inviteeAkind;
    int kind;
    String kindName;
    int subKind;
    int testStatus;
    int type;
    String writeTimeString;
    TimeModel writeTime;

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public int getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(int estimateId) {
        this.estimateId = estimateId;
    }

    public int getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(int inviteeId) {
        this.inviteeId = inviteeId;
    }

    public int getInviteeAkind() {
        return inviteeAkind;
    }

    public void setInviteeAkind(int inviteeAkind) {
        this.inviteeAkind = inviteeAkind;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public int getSubKind() {
        return subKind;
    }

    public void setSubKind(int subKind) {
        this.subKind = subKind;
    }

    public int getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(int testStatus) {
        this.testStatus = testStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWriteTimeString() {
        return writeTimeString;
    }

    public void setWriteTimeString(String writeTimeString) {
        this.writeTimeString = writeTimeString;
    }

    public TimeModel getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(TimeModel writeTime) {
        this.writeTime = writeTime;
    }
}
