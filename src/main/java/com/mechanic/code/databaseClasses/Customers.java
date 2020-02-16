package com.mechanic.code.databaseClasses;

public class Customers {
	String name,surname,address;
	int phone1,phone2,counter;

	public Customers(){
		name="";
		surname="";
		address="";
		phone1=0;
		phone2=0;
		counter=0;
	}
	public Customers(String onoma,String epitheto,String diefthinsi,int til1,int til2, int i){
		name=onoma;
		surname=epitheto;
		address=diefthinsi;
		phone1=til1;
		phone2=til2;
		counter=i;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setPhone1(int phone1) {
		this.phone1 = phone1;
	}

	public void setPhone2(int phone2) {
		this.phone2 = phone2;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public String getName(){
		return name;
	}
	public String getSurname(){
		return surname;
	}
	public String getAddress(){
		return  address;
	}
	public int getPhone1(){
		return phone1;
	}
	public int getPhone2(){
		return phone2;
	}
	public int getCounter() {
		return counter;
	}
}
