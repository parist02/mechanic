package com.mechanic.code.databaseClasses;

public class Repair {
	private int repairID;
	private String name;
	public Repair(){
		repairID=0;
		name="";
	}
	public Repair(int id,String onoma){
		repairID=id;
		name=onoma;
	}

	public void setRepairID(int repairID) {
		this.repairID = repairID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRepairID() {
		return repairID;
	}

	public String getName() {
		return name;
	}
}
