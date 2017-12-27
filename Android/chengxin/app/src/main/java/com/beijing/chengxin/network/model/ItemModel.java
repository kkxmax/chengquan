package com.beijing.chengxin.network.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by star on 11/3/2017.
 */

public class ItemModel implements Parcelable {
    int id;
    String code;
    int fenleiId;
    String fenleiName;
    String name;
    int cityId;
    String cityName;
    String addr;
    String comment;
    String need;
    String weburl;
    int provinceId;
    String provinceName;
    int isShow;
    int status;
    String upTime;
    String downTime;
    String contactName;
    String contactMobile;
    String contactWeixin;
    String logo;
    int accountId;
    String accountName;
    String accountCode;
    String accountLogo;
    String accountMobile;
    int accountCredit;
    int akind;
    String akindName;
    int enterKind;
    String enterKindName;
    String writeTimeString;
    TimeModel writeTime;

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

    public int getFenleiId() {
        return fenleiId;
    }

    public void setFenleiId(int fenleiId) {
        this.fenleiId = fenleiId;
    }

    public String getFenleiName() {
        return fenleiName;
    }

    public void setFenleiName(String fenleiName) {
        this.fenleiName = fenleiName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactWeixin() {
        return contactWeixin;
    }

    public void setContactWeixin(String contactWeixin) {
        this.contactWeixin = contactWeixin;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public int getAccountCredit() {
        return accountCredit;
    }

    public void setAccountCredit(int accountCredit) {
        this.accountCredit = accountCredit;
    }

    public int getAkind() {
        return akind;
    }

    public void setAkind(int akind) {
        this.akind = akind;
    }

    public String getAkindName() {
        return akindName;
    }

    public void setAkindName(String akindName) {
        this.akindName = akindName;
    }

    public int getEnterKind() {
        return enterKind;
    }

    public void setEnterKind(int enterKind) {
        this.enterKind = enterKind;
    }

    public String getEnterKindName() {
        return enterKindName;
    }

    public void setEnterKindName(String enterKindName) {
        this.enterKindName = enterKindName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(code);
        dest.writeInt(fenleiId);
        dest.writeString(fenleiName);
        dest.writeString(name);
        dest.writeInt(cityId);
        dest.writeString(cityName);
        dest.writeString(addr);
        dest.writeString(comment);
        dest.writeString(need);
        dest.writeString(weburl);
        dest.writeInt(provinceId);
        dest.writeString(provinceName);
        dest.writeInt(isShow);
        dest.writeInt(status);
        dest.writeString(upTime);
        dest.writeString(downTime);
        dest.writeString(contactName);
        dest.writeString(contactMobile);
        dest.writeString(contactWeixin);
        dest.writeString(logo);
        dest.writeInt(accountId);
        dest.writeString(accountName);
        dest.writeString(accountCode);
        dest.writeString(accountLogo);
        dest.writeString(accountMobile);
        dest.writeInt(accountCredit);
        dest.writeInt(akind);
        dest.writeString(akindName);
        dest.writeString(writeTimeString);
    }

    private ItemModel(Parcel in) {
        id = in.readInt();
        code = in.readString();
        fenleiId = in.readInt();
        fenleiName = in.readString();
        name = in.readString();
        cityId = in.readInt();
        cityName = in.readString();
        addr = in.readString();
        comment = in.readString();
        need = in.readString();
        weburl = in.readString();
        provinceId = in.readInt();
        provinceName = in.readString();
        isShow = in.readInt();
        status = in.readInt();
        upTime = in.readString();
        downTime = in.readString();
        contactName = in.readString();
        contactMobile = in.readString();
        contactWeixin = in.readString();
        logo = in.readString();
        accountId = in.readInt();
        accountName = in.readString();
        accountCode = in.readString();
        accountLogo = in.readString();
        accountMobile = in.readString();
        accountCredit = in.readInt();
        akind = in.readInt();
        akindName = in.readString();
        writeTimeString = in.readString();
    }

    public static final Parcelable.Creator<ItemModel> CREATOR = new Parcelable.Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

}
