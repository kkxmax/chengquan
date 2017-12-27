package com.chengxin.bfip.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chengxin.common.BaseModel;

public class Province extends BaseModel {
	
    private int id;
    private String name;
    private String pinyin;
    private String code;
    private List<City> cities = new ArrayList<City>();
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
    
}