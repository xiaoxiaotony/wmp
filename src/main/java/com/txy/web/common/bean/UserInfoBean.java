package com.txy.web.common.bean;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.txy.common.bean.RoleInfoBean;
@SuppressWarnings("serial")
@Table(name="T_SYSTEM_USER")
public class UserInfoBean implements Serializable
{
	@Id
	@Column(name="USERID")
	private String id;
	@Column(name="USERNAME")
	private String name;
	@Column(name="ACCOUNT")
	private String loginName;
	@Column(name="PASSWORD")
	private String loginPassword;
	@Column(name="PHONE")
	private String phone;
	@Column(name="STATUS")
	private int status;
	@Column(name="ADDTIME")
	private Date addTime;
	@Column(name="ADDUSER")
	private String addUser;
	@Column(name="EMAIL")
	private String email;
	@Column(name="ADDRESS")
	private String address;
	@Column(name="ORGID")
	private String orgId;
	@Column(name="GROUPID")
	@ManyToOne
	private RoleInfoBean reInfoBean;
	@Transient
	private String authUrlStr;
	@Transient
	private String authBtnCodeStr;
	@Column(name="USER_TYPE")  //用户类型  0 系统用户  1 演练用户
	private int userType;
	@Column(name="COMPANY")  //用户类型  0 系统用户  1 演练用户
	private String company;
	@Column(name="ERRORCOUNT")
	private long errorCount;
	@Column(name="LOCK_DATE")
	private transient Date lockDate;
	
	@Column(name="ATTENTION_AREA")
	private  String attentionArea;
	
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getAddUser() {
		return addUser;
	}
	public void setAddUser(String addUser) {
		this.addUser = addUser;
	}
	public UserInfoBean() {
	}
	public String getAuthUrlStr() {
		return authUrlStr;
	}
	public void setAuthUrlStr(String authUrlStr) {
		this.authUrlStr = authUrlStr;
	}
	public String getAuthBtnCodeStr() {
		return authBtnCodeStr;
	}
	public void setAuthBtnCodeStr(String authBtnCodeStr) {
		this.authBtnCodeStr = authBtnCodeStr;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	
	public String getCompany()
	{
		return company;
	}
	public void setCompany(String company)
	{
		this.company = company;
	}


	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public RoleInfoBean getReInfoBean()
	{
		return reInfoBean;
	}
	
	public void setReInfoBean(RoleInfoBean reInfoBean)
	{
		this.reInfoBean = reInfoBean;
	}
	
	public int getUserType()
	{
		return userType;
	}
	
	public void setUserType(int userType)
	{
		this.userType = userType;
	}

	
	public long getErrorCount()
	{
		return errorCount;
	}
	
	public void setErrorCount(long errorCount)
	{
		this.errorCount = errorCount;
	}
	
	public Date getLockDate()
	{
		return lockDate;
	}

	public void setLockDate(Date lockDate)
	{
		this.lockDate = lockDate;
	}
	public String getAttentionArea() {
		return attentionArea;
	}
	public void setAttentionArea(String attentionArea) {
		this.attentionArea = attentionArea;
	}
	
	
}
