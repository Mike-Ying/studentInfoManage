package com.demo.bean;

import java.sql.Date;

/**
 * Description:  User实体类
 * @author 应文灿
 * @update 2021年5月6日
 */
public class UserBean {
	private String name;
	
	private Date date;
	
	private String sid;
	
	private String phone;
	
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 含参构造器
	 * @author 应文灿
	 * @param name
	 * @param date
	 * @param sid
	 * @param phone
	 * @param type
	 */
	public UserBean(String name, Date date, String sid, String phone, String type) {
		this.name = name;
		this.date = date;
		this.sid = sid;
		this.phone = phone;
		this.type = type;
	}
	
}
