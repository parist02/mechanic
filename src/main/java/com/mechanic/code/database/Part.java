package com.mechanic.code.database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Part {
	private int counter,invoiceID,quantity;
	private String partsID,description;
	private double price;
	private static DecimalFormat decimalFormat=new DecimalFormat("0.00");

	public Part(){
		counter=0;
		invoiceID=0;
		quantity=0;
		partsID="";
		price= 0;
		description="";
	}

	public Part(int count,int arithmosTimologiou,int posotita,String onoma,String kodikos,double timi){
		invoiceID=arithmosTimologiou;
		quantity=posotita;
		partsID=kodikos;
		price=timi;
		description=onoma;
		counter=count;
	}

	public String getDescription() {
		return description;
	}

	public int getCounter() {
		return counter;
	}

	public String getPartsID() {
		return partsID;
	}

	public int getInvoiceID() {
		return invoiceID;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setInvoiceID(int invoiceID) {
		this.invoiceID = invoiceID;
	}

	public void setPartsID(String partsID) {
		this.partsID = partsID;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
}
