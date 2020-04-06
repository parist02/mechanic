package com.mechanic.code.database;

public class Mechanic {
	private Integer mechanicID;
	private String name,surname;

	public Mechanic(){
		mechanicID=0;
		name="";
		surname="";
	}
	public Mechanic(int id,String onoma,String epitheto){
		mechanicID=id;
		name=onoma;
		surname=epitheto;
	}

	public void setMechanicID(Integer mechanicID) {
		this.mechanicID = mechanicID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Integer getMechanicID() {
		return mechanicID;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}
}
