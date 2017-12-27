package com.chengxin.common;

public class File extends BaseModel {
	private int fileNo; 
	private String referenceTableName;
	private String referenceColumnName;
	private String referenceNo;
	private String physicalPath;
	private String physicalName;
	private String originName;
	private String extension;
	private int size;
	private int sequence;
	private String insertedDate;
	private String fileUrl;

	public int getFileNo() {return fileNo;}
	public void setFileNo(int val) {this.fileNo = val;}

	public String getReferenceTableName() {return referenceTableName;}
	public void setReferenceTableName(String val) {this.referenceTableName = val;}

	public String getReferenceColumnName() {return referenceColumnName;}
	public void setReferenceColumnName(String val) {this.referenceColumnName = val;}

	public String getReferenceNo() {return referenceNo;}
	public void setReferenceNo(String val) {this.referenceNo = val;}

	public String getPhysicalPath() {return physicalPath;}
	public void setPhysicalPath(String val) {this.physicalPath = val;}

	public String getPhysicalName() {return physicalName;}
	public void setPhysicalName(String val) {this.physicalName = val;}

	public String getOriginName() {return originName;}
	public void setOriginName(String val) {this.originName = val;}

	public String getExtension() {return extension;}
	public void setExtension(String val) {this.extension = val;}

	public int getSize() {return size;}
	public void setSize(int val) {this.size = val;}

	public int getSequence() {return sequence;}
	public void setSequence(int val) {this.sequence = val;}

	public String getInsertedDate() {return insertedDate;}
	public void setInsertedDate(String val) {this.insertedDate = val;}

	public String getFileUrl() {return fileUrl;}
	public void setFileUrl(String val) {this.fileUrl = val;}

}