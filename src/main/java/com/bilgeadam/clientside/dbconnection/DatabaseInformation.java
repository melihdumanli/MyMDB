package com.bilgeadam.clientside.dbconnection;

import com.bilgeadam.resources.Resources;

public class DatabaseInformation {
	private String url;
	private String userName;
	private String password;
	private String forNameData;

	// Constructor
	
	public DatabaseInformation() {
		this.url = Resources.getConfig("dbUrl"); //$NON-NLS-1$
		this.userName = Resources.getConfig("dbUserName"); //$NON-NLS-1$
		this.password = Resources.getConfig("dbPassword"); //$NON-NLS-1$
		this.forNameData = Resources.getConfig("dbDriver"); //$NON-NLS-1$
	}
	
	// Getter and Setter
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getForNameData() {
		return forNameData;
	}
	
	public void setForNameData(String forNameData) {
		this.forNameData = forNameData;
	}
}
