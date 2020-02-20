package com.example.carhireowner.hiring.models;

import java.io.Serializable;

public class BookedCar implements Serializable {
	private int id;
	private Car car;
	private User user;
	private String date_for_hire;
	private int no_of_days;
	private String date_booked;
	private String status;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setCar(Car car){
		this.car = car;
	}

	public Car getCar(){
		return car;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public String getDate_for_hire() {
		return date_for_hire;
	}

	public void setDate_for_hire(String date_for_hire) {
		this.date_for_hire = date_for_hire;
	}

	public int getNo_of_days() {
		return no_of_days;
	}

	public void setNo_of_days(int no_of_days) {
		this.no_of_days = no_of_days;
	}

	public String getDate_booked() {
		return date_booked;
	}

	public void setDate_booked(String date_booked) {
		this.date_booked = date_booked;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
	public String toString() {
		return "BookedCar{" +
				"id=" + id +
				", car=" + car +
				", user=" + user +
				", date_for_hire='" + date_for_hire + '\'' +
				", no_of_days=" + no_of_days +
				", date_booked='" + date_booked + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}