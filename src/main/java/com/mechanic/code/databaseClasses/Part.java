package com.mechanic.code.databaseClasses;

public class Part {
	private int invoiceID,quantity;
	private String partsID;
	private float price;

	public Part(){
		invoiceID=0;
		quantity=0;
		partsID="";
		price= 0;
	}

	public Part(int arithmosTimologiou,int posotita,String kodikos,float timi){
		invoiceID=arithmosTimologiou;
		quantity=posotita;
		partsID=kodikos;
		price=timi;
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
}
