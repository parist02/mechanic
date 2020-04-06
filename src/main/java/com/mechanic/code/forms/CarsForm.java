package com.mechanic.code.forms;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CarsForm {
	private Stage stageForm;
	private String licensePlates, brand, model,date,vin;
	private Integer customerID;
	private TextField textFieldLicensePlates;
	private TextField textFieldBrand;
	private TextField textFieldModel;
	private TextField textFieldVin;
	private TextField textFieldDate;
	private TextField textFieldCustomerID;
	private Button buttonOk, buttonCancel;
	private Label labelEmpty;
	private boolean changed=false;
	private String licensePlatesBefore,brandBefore,modelBefore,dateBefore,vinBefore;
	private Integer customerIDBefore;
	private String query="UPDATE cars SET ";
	private String queryAdd="INSERT INTO cars (LicensePlates, Brand, Model, VIN, ManufacturingDate, CustomerID) VALUES (";

	public CarsForm(Stage primaryStage) {
		carsFormInitializer();
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		buttonCancel.setOnAction(e ->stageForm.close());
		buttonOk.setOnAction(e -> {
			licensePlates=textFieldLicensePlates.getText().replaceAll("[^a-zA-Z0-9]", "");
			brand=textFieldBrand.getText().replaceAll("[^a-zA-Z0-9]", "");
			model=textFieldModel.getText().replaceAll("[^a-zA-Z0-9]", "");
			vin=textFieldVin.getText().replaceAll("[^a-zA-Z0-9]", "");
			date=textFieldDate.getText().replaceAll("[^a-zA-Z0-9/-]\\s", "");
			if (!textFieldCustomerID.getText().equals("")){
				customerID=Integer.parseInt(textFieldCustomerID.getText().replaceAll("[^0-9]", ""));
			}
			if (licensePlates.equals("") || brand.equals("") || model.equals("") || vin.equals("") || date.equals("")) {
				labelEmpty.setText("Please fill all the details");
			} else {
				changed=true;
				queryAdd = queryAdd + "'" + licensePlates + "' , '" + brand + "', '" + model + "', '" + vin + "', '" + date+"', "+customerID + ");";
				stageForm.close();
			}
		});
	}



	public CarsForm(Stage primaryStage, String noumeraAftokinitou,String marka,String montelo,String chassis,String imerominia,Integer arithmosPelati ) {
		licensePlatesBefore=noumeraAftokinitou;
		brandBefore=marka;
		modelBefore=montelo;
		vinBefore=chassis;
		dateBefore=imerominia;
		customerIDBefore=arithmosPelati;
		carsFormInitializer();
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		textFieldLicensePlates.setText(noumeraAftokinitou);
		textFieldBrand.setText(marka);
		textFieldModel.setText(montelo);
		textFieldVin.setText(chassis);
		textFieldDate.setText(imerominia);
		textFieldCustomerID.setText(arithmosPelati.toString());
		//Event Handler
		buttonCancel.setOnAction(e->stageForm.close());
		buttonOk.setOnAction(e->{
			licensePlates=textFieldLicensePlates.getText().replaceAll("[^a-zA-Z0-9]", "");
			brand=textFieldBrand.getText().replaceAll("[^a-zA-Z0-9]", "");
			model=textFieldModel.getText().replaceAll("[^a-zA-Z0-9]", "");
			vin=textFieldVin.getText().replaceAll("[^a-zA-Z0-9]", "");
			date=textFieldDate.getText().replaceAll("[^a-zA-Z0-9/-]\\s", "");
			if (!textFieldCustomerID.getText().equals("")){
				customerID=Integer.parseInt(textFieldCustomerID.getText().replaceAll("[^0-9]", ""));
			}
			if (licensePlates.equals("") || brand.equals("") || model.equals("") || vin.equals("") || date.equals("")) {
				labelEmpty.setText("Please fill all the details");
			}else {
				if (!licensePlates.equals(licensePlatesBefore)){
					if(changed){
						query=query+", LicensePlates = '"+licensePlates+"'";
					}else{
						changed=true;
						query=query+" LicensePlates = '"+licensePlates+"'";
					}
				}
				if(!brand.equals(brandBefore)){
					if(changed){
						query=query+", Brand = '"+brand+"'";
					}else{
						changed=true;
						query=query+" Brand = '"+brand+"'";
					}
				}
				if(!model.equals(modelBefore)){
					if(changed){
						query=query+", Model = '"+model+"'";
					}else{
						changed=true;
						query=query+" Model = '"+model+"'";
					}
				}
				if(!vin.equals(vinBefore)){
					if(changed){
						query=query+", VIN = '"+vin+"'";
					}else{
						changed=true;
						query=query+" VIN = '"+vin+"'";
					}
				}
				if(!date.equals(dateBefore)){
					if(changed){
						query=query+", ManufacturingDate = '"+date+"'";
					}else{
						changed=true;
						query=query+" ManufacturingDate = '"+date+"'";
					}
				}
				if(!customerID.equals(customerIDBefore)){
					if(changed){
						query=query+", CustomerID = "+customerID.toString();
					}else{
						changed=true;
						query=query+" CustomerID = "+customerID.toString();
					}
				}
				stageForm.close();
			}
		});

	}

	private void carsFormInitializer() {
		stageForm = new Stage();
		stageForm.setTitle("Form");
		Font font = Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 15);
		//Labels
		Label labelLicensePlates = new Label("License Plates:");
		labelLicensePlates.setFont(font);
		labelLicensePlates.setTextFill(Color.BLACK);
		Label labelBrand = new Label("Brand:");
		labelBrand.setFont(font);
		labelBrand.setTextFill(Color.BLACK);
		Label labelModel = new Label("Model:");
		labelModel.setFont(font);
		labelModel.setTextFill(Color.BLACK);
		Label labelVIN = new Label("VIN:");
		labelVIN.setFont(font);
		labelVIN.setTextFill(Color.BLACK);
		Label labelDate = new Label("Date of Manufacture:");
		labelDate.setFont(font);
		labelDate.setTextFill(Color.BLACK);
		Label labelCustomerID = new Label("Customer ID:");
		labelCustomerID.setFont(font);
		labelCustomerID.setTextFill(Color.BLACK);
		labelEmpty = new Label("");
		labelEmpty.setFont(font);
		labelEmpty.setTextFill(Color.FIREBRICK);
		labelEmpty.setPadding(new Insets(10, 10, 10, 10));
		Label labelTitle = new Label("Car details");
		labelTitle.setTextFill(Color.BLACK);
		labelTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		labelTitle.setPadding(new Insets(10, 10, 10, 10));
		//Text fields
		textFieldLicensePlates = new TextField();
		textFieldBrand = new TextField();
		textFieldModel = new TextField();
		textFieldVin = new TextField();
		textFieldDate = new TextField();
		textFieldCustomerID=new TextField();
		//
		buttonOk = new Button("Ok");
		buttonCancel = new Button("Cancel");
		//Grid control
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelLicensePlates, 0, 1);
		grid.add(textFieldLicensePlates, 1, 1);
		grid.add(labelBrand, 0, 2);
		grid.add(textFieldBrand, 1, 2);
		grid.add(labelModel, 0, 3);
		grid.add(textFieldModel, 1, 3);
		grid.add(labelVIN, 0, 4);
		grid.add(textFieldVin, 1, 4);
		grid.add(labelDate, 0, 5);
		grid.add(textFieldDate, 1, 5);
		grid.add(labelCustomerID,0,6);
		grid.add(textFieldCustomerID,1,6);
		grid.add(labelEmpty, 0, 7, 2, 1);
		grid.add(buttonOk, 0, 8);
		grid.add(buttonCancel, 1, 8);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setHgap(20);
		grid.setVgap(15);
		//Scene control
		Scene sceneForm = new Scene(grid);
		//sceneForm.getStylesheets().add("style.css");
		stageForm.setScene(sceneForm);
		buttonOk.getStyleClass().add("button2");
	}

	public String getQuery() {
		return query;
	}

	public String getQueryAdd() {
		return queryAdd;
	}

	public void showForm(){
		stageForm.showAndWait();
	}

	public String getBrand() {
		return brand;
	}

	public String getVin() {
		return vin;
	}

	public String getDate() {
		return date;
	}

	public String getModel() {
		return model;
	}

	public Integer getCustomerID() {
		return customerID;
	}

	public String getLicensePlates() {
		return licensePlates;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setTextFieldLicensePlates(String noumera) {
		textFieldLicensePlates.setText(noumera);
	}
}