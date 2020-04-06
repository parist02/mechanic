package com.mechanic.code.database;



public class Car {
	private String licencePlates,brand,model,date,vin;
	private int customerId;

	public Car(){
		licencePlates="";
		brand="";
		model="";
		vin="";
		date="";
		customerId=0;
	}

	public Car(String noumeraAftokinitou, String marka, String montelo, String chassis, String imerominia, int metritis){
		licencePlates=noumeraAftokinitou;
		brand=marka;
		model=montelo;
		vin=chassis;
		date=imerominia;
		customerId=metritis;
	}

	public String getDate() {
		return date;
	}

	public int getCustomerId() {
		return customerId;
	}

	public String getLicencePlates() {
		return licencePlates;
	}

	public String getModel() {
		return model;
	}

	public String getVin() {
		return vin;
	}

	public String getBrand() {
		return brand;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setLicencePlates(String licencePlates) {
		this.licencePlates = licencePlates;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

}
