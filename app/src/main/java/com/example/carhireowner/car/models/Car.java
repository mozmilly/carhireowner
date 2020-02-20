package com.example.carhireowner.car.models;

import java.io.Serializable;

public class Car implements Serializable {
	private int id;
	private String make;
	private String number_plate;
	private String color;
	private int seaters;
	private double price_per_day;
	private String photo;
	private Owner owner;
	private String location;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setMake(String make){
		this.make = make;
	}

	public String getMake(){
		return make;
	}

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	public void setSeaters(int seaters){
		this.seaters = seaters;
	}

	public int getSeaters(){
		return seaters;
	}

	public String getNumber_plate() {
		return number_plate;
	}

	public void setNumber_plate(String number_plate) {
		this.number_plate = number_plate;
	}

	public double getPrice_per_day() {
		return price_per_day;
	}

	public void setPrice_per_day(double price_per_day) {
		this.price_per_day = price_per_day;
	}

	public void setPhoto(String photo){
		this.photo = photo;
	}

	public String getPhoto(){
		return photo;
	}

	public void setOwner(Owner owner){
		this.owner = owner;
	}

	public Owner getOwner(){
		return owner;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	@Override
	public String toString() {
		return "Car{" +
				"id=" + id +
				", make='" + make + '\'' +
				", number_plate='" + number_plate + '\'' +
				", color='" + color + '\'' +
				", seaters=" + seaters +
				", price_per_day=" + price_per_day +
				", photo='" + photo + '\'' +
				", owner=" + owner +
				", location='" + location + '\'' +
				'}';
	}
}