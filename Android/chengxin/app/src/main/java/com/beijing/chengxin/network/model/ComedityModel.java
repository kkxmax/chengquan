package com.beijing.chengxin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by star on 11/3/2017.
 */

public class ComedityModel implements Parcelable {
    int id;
    String code;
    int isMain;
    String name;
    float price;
    int pleixingId;
    String comment;
    String weburl;
    String saleAddr;
    int status;
    String statusName;
    String upTime;
    String downTime;
    String cityName;

    int accountId;
    int accountCredit;
    String accountCode;
    String accountLogo;
    String accountMobile;
    int enterKind;
    String enterName;

    String writeTimeString;
    int isFavourite;
    ArrayList<String> imgPaths = new ArrayList<String>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPleixingId() {
        return pleixingId;
    }

    public void setPleixingId(int pleixingId) {
        this.pleixingId = pleixingId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getSaleAddr() {
        return saleAddr;
    }

    public void setSaleAddr(String saleAddr) {
        this.saleAddr = saleAddr;
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

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getDownTime() {
        return downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountCredit() {
        return accountCredit;
    }

    public void setAccountCredit(int accountCredit) {
        this.accountCredit = accountCredit;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountLogo() {
        return accountLogo;
    }

    public void setAccountLogo(String accountLogo) {
        this.accountLogo = accountLogo;
    }

    public String getAccountMobile() {
        return accountMobile;
    }

    public void setAccountMobile(String accountMobile) {
        this.accountMobile = accountMobile;
    }

    public int getEnterKind() {
        return enterKind;
    }

    public void setEnterKind(int enterKind) {
        this.enterKind = enterKind;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public String getWriteTimeString() {
        return writeTimeString;
    }

    public void setWriteTimeString(String writeTimeString) {
        this.writeTimeString = writeTimeString;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public ArrayList<String> getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(ArrayList<String> imgPaths) {
        this.imgPaths = imgPaths;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(code);
        dest.writeInt(isMain);
        dest.writeString(name);
        dest.writeFloat(price);
        dest.writeInt(pleixingId);
        dest.writeString(comment);
        dest.writeString(weburl);
        dest.writeString(saleAddr);
        dest.writeInt(status);
        dest.writeString(statusName);
        dest.writeString(upTime);
        dest.writeString(downTime);
        dest.writeString(cityName);
        dest.writeInt(accountId);
        dest.writeInt(accountCredit);
        dest.writeString(accountCode);
        dest.writeString(accountLogo);
        dest.writeString(accountMobile);
        dest.writeString(enterName);
        dest.writeString(writeTimeString);
        dest.writeInt(isFavourite);
        dest.writeStringList(imgPaths);
    }

    private ComedityModel(Parcel in) {
        id = in.readInt();
        code = in.readString();
        isMain = in.readInt();
        name = in.readString();
        price = in.readFloat();
        pleixingId = in.readInt();
        comment = in.readString();
        weburl = in.readString();
        saleAddr = in.readString();
        status = in.readInt();
        statusName = in.readString();
        upTime = in.readString();
        downTime = in.readString();
        cityName = in.readString();
        accountId = in.readInt();
        accountCredit = in.readInt();
        accountCode = in.readString();
        accountLogo = in.readString();
        accountMobile = in.readString();
        enterName = in.readString();
        writeTimeString = in.readString();
        isFavourite = in.readInt();
        in.readStringList(imgPaths);
    }

    public static final Parcelable.Creator<ComedityModel> CREATOR = new Parcelable.Creator<ComedityModel>() {
        @Override
        public ComedityModel createFromParcel(Parcel in) {
            return new ComedityModel(in);
        }

        @Override
        public ComedityModel[] newArray(int size) {
            return new ComedityModel[size];
        }
    };

}
