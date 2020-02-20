package com.example.carhireowner.hiring.models;

import java.io.Serializable;

public class Owner implements Serializable {
	private User user;
	private String image;

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	@Override
 	public String toString(){
		return 
			"Owner{" + 
			"user = '" + user + '\'' + 
			",image = '" + image + '\'' + 
			"}";
		}
}