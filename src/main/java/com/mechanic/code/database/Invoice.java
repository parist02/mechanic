package com.mechanic.code.database;

import java.sql.Date;
import java.time.LocalDate;

public class Invoice {
	private LocalDate date;
	private String fullName, licencePlates, mechanicName, repairType;
	private int phone, customerID,invoiceID;
	private float balance;


	public Invoice() {
		invoiceID=0;
		date = LocalDate.now();
		fullName = "";
		licencePlates = "";
		mechanicName = "";
		repairType = "";
		phone = 0;
		customerID = 0;
		balance = 0;
	}


	public void setInvoiceID(int invoiceID) {
		this.invoiceID = invoiceID;
	}
	public void setDate(LocalDate date) {
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

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getLicencePlates() {
		return licencePlates;
	}

	public LocalDate getDate() {
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

	public int getInvoiceID() {
		return invoiceID;
	}
}