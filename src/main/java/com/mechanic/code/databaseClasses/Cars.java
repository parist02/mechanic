package com.mechanic.code.databaseClasses;

import java.sql.Date;

public class Cars {
	public String licencePlates,brand,model,vin;
	public Date date;
	public int customerId;

	public Cars(){
		licencePlates="";
		brand="";
		model="";
		vin="";
		date=new Date(1010-3-30);
		customerId=0;
	}

	public Cars(String noumeraAftokinitou,String marka,String montelo,String chassis,Date imerominia,int metritis){
		licencePlates=noumeraAftokinitou;
		brand=marka;
		model=montelo;
		vin=chassis;
		date=imerominia;
		customerId=metritis;
	}

	public Date getDate() {
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

	public void setDate(Date date) {
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
