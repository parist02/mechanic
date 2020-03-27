package com.mechanic.code.databaseClasses;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Invoice {
	private Date date;
	private String fullName, licencePlates, mechanicName, repairType;
	private int phone, customerID,invoiceID;
	private float balance;


	public Invoice() {
		invoiceID=0;
		date = new Date(System.currentTimeMillis());
		fullName = "";
		licencePlates = "";
		mechanicName = "";
		repairType = "";
		phone = 0;
		customerID = 0;
		balance = 0;
	}
	public  Invoice(Integer counter,Date imerominia,String onomateponimo, String noumeraA,String onomaMixanikou, String eidos, Integer tilefono, Integer i, Float ipolipo){
		invoiceID=counter;
		date=imerominia;
		fullName=onomateponimo;
		licencePlates = noumeraA;
		mechanicName = onomaMixanikou;
		repairType = eidos;
		phone = tilefono;
		customerID = i;
		balance = ipolipo;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setLicensePlates(String licencePlates) {
		this.licencePlates = licencePlates;
	}

	public void setMechanicName(String mechanicName) {
		this.mechanicName = mechanicName;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}

	public void setCustomeID(int customerID) {
		this.customerID = customerID;
	}

	public String getLicencePlates() {
		return licencePlates;
	}

	public Date getDate() {
		return date;
	}

	public float getBalance() {
		return balance;
	}

	public int getPhone() {
		return phone;
	}

	public String getFullName() {
		return fullName;
	}

	public String getMechanicName() {
		return mechanicName;
	}

	public String getRepairType() {
		return repairType;
	}

	public int getCustomerID() {
		return customerID;
	}
}