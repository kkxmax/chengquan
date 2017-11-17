package com.beijing.chengxin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ErrorModel implements Parcelable {

    String estimateContent;
    int estimateId;
    ArrayList<String> estimateImgPaths = new ArrayList<String>();
    int estimateeAkind;
    String estimateeEnterName;
    int estimateeId;
    String estimateeName;
    String estimateeRealname;
    int estimaterAkind;
    String estimaterEnterName;
    int estimaterId;
    String estimaterLogo;
    String estimaterName;
    String estimaterRealname;
    int id;
    ArrayList<String> imgPaths = new ArrayList<String>();
    int kind;
    String kindName;
    int ownerAkind;
    String ownerEnterName;
    int ownerId;
    String ownerMobile;
    String ownerName;
    String ownerRealname;
    String reason;
    int serial;
    int status;
    String statusName;
    String whyis;
    String writeTimeString;

    public String getEstimateContent() {
        return estimateContent;
    }

    public void setEstimateContent(String estimateContent) {
        this.estimateContent = estimateContent;
    }

    public int getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(int estimateId) {
        this.estimateId = estimateId;
    }

    public ArrayList<String> getEstimateImgPaths() {
        return estimateImgPaths;
    }

    public void setEstimateImgPaths(ArrayList<String> estimateImgPaths) {
        this.estimateImgPaths = estimateImgPaths;
    }

    public int getEstimateeAkind() {
        return estimateeAkind;
    }

    public void setEstimateeAkind(int estimateeAkind) {
        this.estimateeAkind = estimateeAkind;
    }

    public String getEstimateeEnterName() {
        return estimateeEnterName;
    }

    public void setEstimateeEnterName(String estimateeEnterName) {
        this.estimateeEnterName = estimateeEnterName;
    }

    public int getEstimateeId() {
        return estimateeId;
    }

    public void setEstimateeId(int estimateeId) {
        this.estimateeId = estimateeId;
    }

    public String getEstimateeName() {
        return estimateeName;
    }

    public void setEstimateeName(String estimateeName) {
        this.estimateeName = estimateeName;
    }

    public String getEstimateeRealname() {
        return estimateeRealname;
    }

    public void setEstimateeRealname(String estimateeRealname) {
        this.estimateeRealname = estimateeRealname;
    }

    public int getEstimaterAkind() {
        return estimaterAkind;
    }

    public void setEstimaterAkind(int estimaterAkind) {
        this.estimaterAkind = estimaterAkind;
    }

    public String getEstimaterEnterName() {
        return estimaterEnterName;
    }

    public void setEstimaterEnterName(String estimaterEnterName) {
        this.estimaterEnterName = estimaterEnterName;
    }

    public int getEstimaterId() {
        return estimaterId;
    }

    public void setEstimaterId(int estimaterId) {
        this.estimaterId = estimaterId;
    }

    public String getEstimaterLogo() {
        return estimaterLogo;
    }

    public void setEstimaterLogo(String estimaterLogo) {
        this.estimaterLogo = estimaterLogo;
    }

    public String getEstimaterName() {
        return estimaterName;
    }

    public void setEstimaterName(String estimaterName) {
        this.estimaterName = estimaterName;
    }

    public String getEstimaterRealname() {
        return estimaterRealname;
    }

    public void setEstimaterRealname(String estimaterRealname) {
        this.estimaterRealname = estimaterRealname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(ArrayList<String> imgPaths) {
        this.imgPaths = imgPaths;
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

    public int getOwnerAkind() {
        return ownerAkind;
    }

    public void setOwnerAkind(int ownerAkind) {
        this.ownerAkind = ownerAkind;
    }

    public String getOwnerEnterName() {
        return ownerEnterName;
    }

    public void setOwnerEnterName(String ownerEnterName) {
        this.ownerEnterName = ownerEnterName;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerMobile() {
        return ownerMobile;
    }

    public void setOwnerMobile(String ownerMobile) {
        this.ownerMobile = ownerMobile;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerRealname() {
        return ownerRealname;
    }

    public void setOwnerRealname(String ownerRealname) {
        this.ownerRealname = ownerRealname;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getWhyis() {
        return whyis;
    }

    public void setWhyis(String whyis) {
        this.whyis = whyis;
    }

    public String getWriteTimeString() {
        return writeTimeString;
    }

    public void setWriteTimeString(String writeTimeString) {
        this.writeTimeString = writeTimeString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(estimateContent);
        dest.writeInt(estimateId);
        dest.writeStringList(estimateImgPaths);
        dest.writeInt(estimateeAkind);
        dest.writeString(estimateeEnterName);
        dest.writeInt(estimateeId);
        dest.writeString(estimateeName);
        dest.writeString(estimateeRealname);
        dest.writeInt(estimaterAkind);
        dest.writeString(estimaterEnterName);
        dest.writeInt(estimaterId);
        dest.writeString(estimaterLogo);
        dest.writeString(estimaterName);
        dest.writeString(estimaterRealname);
        dest.writeInt(id);
        dest.writeStringList(imgPaths);
        dest.writeInt(kind);
        dest.writeString(kindName);
        dest.writeInt(ownerAkind);
        dest.writeString(ownerEnterName);
        dest.writeInt(ownerId);
        dest.writeString(ownerMobile);
        dest.writeString(ownerName);
        dest.writeString(ownerRealname);
        dest.writeString(reason);
        dest.writeInt(serial);
        dest.writeInt(status);
        dest.writeString(statusName);
        dest.writeString(whyis);
        dest.writeString(writeTimeString);
    }

    private ErrorModel(Parcel in) {
        estimateContent = in.readString();
        estimateId = in.readInt();
        in.readStringList(estimateImgPaths);
        estimateeAkind = in.readInt();
        estimateeEnterName = in.readString();
        estimateeId = in.readInt();
        estimateeName = in.readString();
        estimateeRealname = in.readString();
        estimaterAkind = in.readInt();
        estimaterEnterName = in.readString();
        estimaterId = in.readInt();
        estimaterLogo = in.readString();
        estimaterName = in.readString();
        estimaterRealname = in.readString();
        id = in.readInt();
        in.readStringList(imgPaths);
        kind = in.readInt();
        kindName = in.readString();
        ownerAkind = in.readInt();
        ownerEnterName = in.readString();
        ownerId = in.readInt();
        ownerMobile = in.readString();
        ownerName = in.readString();
        ownerRealname = in.readString();
        reason = in.readString();
        serial = in.readInt();
        status = in.readInt();
        statusName = in.readString();
        whyis = in.readString();
        writeTimeString = in.readString();
    }

    public static final Parcelable.Creator<ErrorModel> CREATOR = new Parcelable.Creator<ErrorModel>() {
        @Override
        public ErrorModel createFromParcel(Parcel in) {
            return new ErrorModel(in);
        }

        @Override
        public ErrorModel[] newArray(int size) {
            return new ErrorModel[size];
        }
    };

}
