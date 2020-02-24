package com.example.carhireowner.car.models;

import java.io.Serializable;

public class CarReview implements Serializable {
	private int id;
	private String comment;
	private User user;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"CarReview{" + 
			"id = '" + id + '\'' + 
			",comment = '" + comment + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}
}