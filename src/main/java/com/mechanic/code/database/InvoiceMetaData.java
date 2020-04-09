package com.mechanic.code.database;

import java.time.LocalDate;

public class InvoiceMetaData {
	private LocalDate dateIn,dateOut;
	private int invoiceId,mileage,firstOil,nextOil,nextService;
	private String comments;
	private float vat,discount;


	//den tha dimiourgithei defteros constructor tha ekteleite i entoli set gia efkolia afou einai arketes oi metavlites
	public InvoiceMetaData(Float fpa){
		invoiceId=0;
		dateIn=null;
		dateOut=null;
		mileage=0;
		firstOil=0;
		nextOil=0;
		nextService=0;
		comments="";
		vat=fpa;
		discount=0;
	}



	public int getInvoiceId() {
		return invoiceId;
	}

	public String getComments() {
		return comments;
	}

	public float getDiscount() {
		return discount;
	}

	public float getVat() {
		return vat;
	}

	public int getFirstOil() {
		return firstOil;
	}

	public int getMileage() {
		return mileage;
	}

	public int getNextOil() {
		return nextOil;
	}

	public int getNextService() {
		return nextService;
	}

	public LocalDate getDateIn() {
		return dateIn;
	}

	public LocalDate getDateOut() {
		return dateOut;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setDateOut(LocalDate dateOut) {
		this.dateOut = dateOut;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public void setFirstOil(int firstOil) {
		this.firstOil = firstOil;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public void setNextOil(int nextOil) {
		this.nextOil = nextOil;
	}

	public void setNextService(int nextService) {
		this.nextService = nextService;
	}

	public void setVat(float vat) {
		this.vat = vat;
	}

	public void setDateIn(LocalDate dateIn) {
		this.dateIn = dateIn;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}
}


