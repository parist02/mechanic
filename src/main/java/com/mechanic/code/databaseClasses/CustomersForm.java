package com.mechanic.code.databaseClasses;

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

public class CustomersForm {
	private Stage stageForm;
	private String name, surname, address=null;
	private Integer phone1=null, phone2=null;
	private Float balance=null;
	private TextField textFieldName;
	private TextField textFieldSurname;
	private TextField textFieldPhone1;
	private TextField textFieldPhone2;
	private TextField textFieldAddress;
	private TextField textFieldBalance;
	private Button buttonOk, buttonCancel;
	private Label labelEmpty;
	private boolean changed=false;
	private String nameBefore,surnameBefore,addressBefore;
	private Integer phone1Before,phone2Before;
	private Float balanceBefore;
	private String query="UPDATE customers SET ";
	private String queryAdd="INSERT INTO customers (CustomerId, Name, Surname, Phone_1, Phone_2, Address, Balance) VALUES (NULL";

	public CustomersForm(Stage primaryStage) {
		customersFormInitializer();
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		buttonCancel.setOnAction(e -> {
			stageForm.close();
		});
		buttonOk.setOnAction(e -> {
			name=textFieldName.getText();
			surname=textFieldSurname.getText();
			if (!textFieldPhone1.getText().equals("")){
				phone1=Integer.parseInt(textFieldPhone1.getText());
			}
			if (!textFieldPhone2.getText().equals("")){
				phone2=Integer.parseInt(textFieldPhone2.getText());
			}
			address=textFieldAddress.getText();
			balance=Float.parseFloat(textFieldBalance.getText());
			if (name.equals("") || surname.equals("") || phone1 == null) { //elegxos an iparxoun xaraktires sta tilefono
				labelEmpty.setText("Please fill all the details");
			} else {
				changed=true;
				queryAdd = queryAdd + ", '" + name + "' , '" + surname + "', " + phone1 + ", " + phone2 + ", '" + address + "'," + balance +");";
				stageForm.close();
			}
		});
	}



	public CustomersForm(Stage primaryStage, String onoma,String epitheto,Integer til1,Integer til2,String diefthinsi,Float ipolipo ) {
		nameBefore=onoma;
		surnameBefore=epitheto;
		phone1Before=til1;
		phone2Before=til2;
		addressBefore=diefthinsi;
		balanceBefore=ipolipo;
		customersFormInitializer();
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		textFieldName.setText(onoma);
		textFieldSurname.setText(epitheto);
		textFieldPhone1.setText(til1.toString());
		textFieldPhone2.setText(til2.toString());
		textFieldAddress.setText(diefthinsi);
		textFieldBalance.setText(ipolipo.toString());
		//Event Handler
		buttonCancel.setOnAction(e->{
			stageForm.close();
		});
		buttonOk.setOnAction(e->{
			name=textFieldName.getText();
			surname=textFieldSurname.getText();
			phone1=Integer.parseInt(textFieldPhone1.getText());
			phone2=Integer.parseInt(textFieldPhone2.getText());
			address=textFieldAddress.getText();
			balance=Float.parseFloat(textFieldBalance.getText());
			if (name.equals("")||surname.equals("")|| phone1 == null){
				labelEmpty.setText("Please fill all the details");
			}else {
				if (!name.equals(nameBefore)){
					if(changed){
						query=query+", Name = '"+name+"'";
					}else{
						changed=true;
						query=query+" Name = '"+name+"'";
					}
				}
				if(!surname.equals(surnameBefore)){
					if(changed){
						query=query+", Surname = '"+surname+"'";
					}else{
						changed=true;
						query=query+" Surname = '"+surname+"'";
					}
				}
				if(!phone1.equals(phone1Before)){
					if(changed){
						query=query+", Phone_1 = "+phone1.toString();
					}else{
						changed=true;
						query=query+" Phone_1 = "+phone1.toString();
					}
				}
				if(!phone2.equals(phone2Before)){
					if(changed){
						query=query+", Phone_2 = "+phone2.toString();
					}else{
						changed=true;
						query=query+" Phone_2 = "+phone2.toString();
					}
				}
				if(!address.equals(addressBefore)){
					if(changed){
						query=query+", Address = '"+address+"'";
					}else{
						changed=true;
						query=query+" Address = '"+address+"'";
					}
				}
				if(!balance.equals(balanceBefore)){
					if(changed){
						query=query+", Balance = "+balance.toString();
					}else{
						changed=true;
						query=query+" Balance = "+balance.toString();
					}
				}
				stageForm.close();
			}
		});

	}

	public void customersFormInitializer() {
		stageForm = new Stage();
		stageForm.setTitle("Form");
		Font font = Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 15);
		//Labels
		Label labelName = new Label("Name:");
		labelName.setFont(font);
		labelName.setTextFill(Color.BLACK);
		Label labelSurname = new Label("Surname:");
		labelSurname.setFont(font);
		labelSurname.setTextFill(Color.BLACK);
		Label labelPhone1 = new Label("Phone1:");
		labelPhone1.setFont(font);
		labelPhone1.setTextFill(Color.BLACK);
		Label labelPhone2 = new Label("Phone2:");
		labelPhone2.setFont(font);
		labelPhone2.setTextFill(Color.BLACK);
		Label labelAddress = new Label("Address:");
		labelAddress.setFont(font);
		labelAddress.setTextFill(Color.BLACK);
		Label labelBalance=new Label("Balance");
		labelBalance.setFont(font);
		labelBalance.setTextFill(Color.BLACK);
		labelEmpty = new Label("");
		labelEmpty.setFont(font);
		labelEmpty.setTextFill(Color.FIREBRICK);
		labelEmpty.setPadding(new Insets(10, 10, 10, 10));
		Label labelTitle = new Label("Στοιχεία Πελάτη");
		labelTitle.setTextFill(Color.BLACK);
		labelTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		labelTitle.setPadding(new Insets(10, 10, 10, 10));
		//Text fields
		textFieldName = new TextField();
		textFieldSurname = new TextField();
		textFieldPhone1 = new TextField();
		textFieldPhone2 = new TextField();
		textFieldAddress = new TextField();
		textFieldBalance=new TextField();
		//
		buttonOk = new Button("Ok");
		buttonCancel = new Button("Cancel");
		//Grid control
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelName, 0, 1);
		grid.add(textFieldName, 1, 1);
		grid.add(labelSurname, 0, 2);
		grid.add(textFieldSurname, 1, 2);
		grid.add(labelPhone1, 0, 3);
		grid.add(textFieldPhone1, 1, 3);
		grid.add(labelPhone2, 0, 4);
		grid.add(textFieldPhone2, 1, 4);
		grid.add(labelAddress, 0, 5);
		grid.add(textFieldAddress, 1, 5);
		grid.add(labelBalance, 0, 6);
		grid.add(textFieldBalance, 1, 6);
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
	public String getName(){
		return name;
	}
	public String getSurname(){
		return surname;
	}
	public String getAddress(){
		return address;
	}
	public Integer getPhone1(){
		return phone1;
	}
	public Integer getPhone2() {
		return phone2;
	}
	public Float getBalance() {
		return balance;
	}

	public boolean isChanged() {
		return changed;
	}
}
