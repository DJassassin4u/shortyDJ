package com.djstuff.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Urlpair {

	@Id
	private int id;
	private String original;
	private String shorter;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String origal) {
		this.original = origal;
	}
	public String getShorter() {
		return shorter;
	}
	public void setShorter(String shorter) {
		this.shorter = "localhost:8080/"+shorter;
	}
	@Override
	public String toString() {
		return "UrlPair [id=" + id + ", original=" + original + ", shorter=" + shorter + "]";
	}
	
}
