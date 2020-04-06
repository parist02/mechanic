package com.mechanic.code.database;

public class Company {
	private int phone;
	private String address,taxNumber;
	private float vat;
	public Company(){
		phone=0;
		taxNumber="";
		address="";
		vat=0;
	}
	public Company(Integer tilefono, String arithmosTax, String diefthinsi,Float fpa){
		phone= tilefono;
		taxNumber=arithmosTax;
		address=diefthinsi;
		vat=fpa;
	}

	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public void setVat(float vat) {
		this.vat = vat;
	}
	public float getVat() {
		return vat;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	public String getTaxNumber() {
		return taxNumber;
	}
}
