package com.djstuff.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
	
	@Id
	private int id;
	private String accountId;
	private boolean available = true;
	private String password;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public boolean zauzmi() {
		if(this.available) {
			this.available = false;
			return false;
		} else {
			return true;
		}
	}
	
	public boolean oslobodi() {
		if(!this.available) {
			this.available = true;
			return false;
		} else {
			return true;
		}
	}
	
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Account [Id=" + id + ", accountId=" + accountId + ", available=" + available + ", password=" + password
				+ "]";
	}

	
	
	

}
