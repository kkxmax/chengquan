package com.chengxin.bfip.model;

import java.util.Date;

import com.chengxin.common.BaseModel;

public class ClickHistory extends BaseModel{
	
	private int id;
	private int ownerId;
	private int type;
	private int shareKind;
	private int accountId;
	private int productId;
	private int itemId;
	private int serviceId;
	private Date writeTime = new Date();
	
	private int contactAkind;
	private int personal_cnt;
	private int enterprise_cnt;
	private String click_date;
	private int shop_cnt;
	private int item_cnt;
	private int service_cnt;
	private int personal_detail_cnt;
	private int enterprise_detail_cnt;
	private int report_cnt;
	private int buy_cnt;
	private int request_cnt;
	
	
	public int getPersonal_detail_cnt() {
		return personal_detail_cnt;
	}
	public void setPersonal_detail_cnt(int personal_detail_cnt) {
		this.personal_detail_cnt = personal_detail_cnt;
	}
	public int getEnterprise_detail_cnt() {
		return enterprise_detail_cnt;
	}
	public void setEnterprise_detail_cnt(int enterprise_detail_cnt) {
		this.enterprise_detail_cnt = enterprise_detail_cnt;
	}
	public int getReport_cnt() {
		return report_cnt;
	}
	public void setReport_cnt(int report_cnt) {
		this.report_cnt = report_cnt;
	}
	public int getBuy_cnt() {
		return buy_cnt;
	}
	public void setBuy_cnt(int buy_cnt) {
		this.buy_cnt = buy_cnt;
	}
	public int getRequest_cnt() {
		return request_cnt;
	}
	public void setRequest_cnt(int request_cnt) {
		this.request_cnt = request_cnt;
	}
	public int getShop_cnt() {
		return shop_cnt;
	}
	public void setShop_cnt(int shop_cnt) {
		this.shop_cnt = shop_cnt;
	}
	public int getItem_cnt() {
		return item_cnt;
	}
	public void setItem_cnt(int item_cnt) {
		this.item_cnt = item_cnt;
	}
	public int getService_cnt() {
		return service_cnt;
	}
	public void setService_cnt(int service_cnt) {
		this.service_cnt = service_cnt;
	}
	public String getClick_date() {
		return click_date;
	}
	public void setClick_date(String click_date) {
		this.click_date = click_date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getShareKind() {
		return shareKind;
	}
	public void setShareKind(int shareKind) {
		this.shareKind = shareKind;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public Date getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(Date writeTime) {
		this.writeTime = writeTime;
	}
	public int getPersonal_cnt() {
		return personal_cnt;
	}
	public void setPersonal_cnt(int personal_cnt) {
		this.personal_cnt = personal_cnt;
	}
	public int getEnterprise_cnt() {
		return enterprise_cnt;
	}
	public void setEnterprise_cnt(int enterprise_cnt) {
		this.enterprise_cnt = enterprise_cnt;
	}
	public int getContactAkind() {
		return contactAkind;
	}
	public void setContactAkind(int contactAkind) {
		this.contactAkind = contactAkind;
	}
	
	
}
