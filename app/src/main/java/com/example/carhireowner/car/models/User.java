package com.example.carhireowner.car.models;

import java.io.Serializable;

public class User implements Serializable {
	private String username;
	private String first_name;
	private String last_name;

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"username = '" + username + '\'' + 
			",first_name = '" + first_name + '\'' +
			",last_name = '" + last_name + '\'' +
			"}";
		}
}