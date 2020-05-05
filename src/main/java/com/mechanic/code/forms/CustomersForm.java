package com.mechanic.code.forms;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomersForm {
	private Stage stageForm;
	private String name, surname, address=null;
	private Integer phone1=null, phone2=null;
	private float balance=0;
	private TextField textFieldName;
	private TextField textFieldSurname;
	private TextField textFieldPhone1;
	private TextField textFieldPhone2;
	private TextField textFieldAddress;
	private Button buttonOk, buttonCancel;
	private Label labelEmpty,labelBalance2;
	private boolean changed=false;
	private String nameBefore,surnameBefore,addressBefore;
	private Integer phone1Before,phone2Before;
	private String query="UPDATE customers SET ";
	private String queryAdd="INSERT INTO customers (CustomerId, Name, Surname, Phone_1, Phone_2, Address, Balance) VALUES (NULL";

	public CustomersForm(Stage primaryStage) {
		customersFormInitializer();
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		buttonCancel.setOnAction(e ->stageForm.close());
		buttonOk.setOnAction(e -> {
			boolean error=false;
			name=textFieldName.getText();
			name=name.replaceAll("[^a-zA-Z0-9]", "");
			surname=textFieldSurname.getText();
			surname=surname.replaceAll("[^a-zA-Z0-9]", "");
			try {
				phone1 = Integer.parseInt(textFieldPhone1.getText().replaceAll("[^0-9]", ""));
			}catch (Exception ex){
				textFieldPhone1.setText("");
				error=true;
			}
			try {
				phone2 = Integer.parseInt(textFieldPhone2.getText().replaceAll("[^0-9]", ""));
			}catch (Exception ex){
				phone2=0;
				textFieldPhone2.setText("");
			}
			address=textFieldAddress.getText();
			address=address.replaceAll("[^a-zA-Z0-9]\\s", "");
			if (name.equals("") || surname.equals("") || phone1 == null || error) { //elegxos an iparxoun xaraktires sta tilefono
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
		customersFormInitializer();
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		textFieldName.setText(onoma);
		textFieldSurname.setText(epitheto);
		textFieldPhone1.setText(til1.toString());
		textFieldPhone2.setText(til2.toString());
		textFieldAddress.setText(diefthinsi);
		labelBalance2.setText(ipolipo.toString());
		//Event Handler
		buttonCancel.setOnAction(e->stageForm.close());
		buttonOk.setOnAction(e->{
			boolean error=false;
			name=textFieldName.getText().replaceAll("[^a-zA-Z0-9]", "");
			surname=textFieldSurname.getText().replaceAll("[^a-zA-Z0-9]", "");
			try {
				phone1 = Integer.parseInt(textFieldPhone1.getText().replaceAll("[^0-9]", ""));
			}catch (Exception ex){
				textFieldPhone1.setText("");
				error=true;
			}
			try {
				phone2 = Integer.parseInt(textFieldPhone2.getText().replaceAll("[^0-9]", ""));
			}catch (Exception ex){
				phone2=0;
				textFieldPhone2.setText("");
			}
			address=textFieldAddress.getText().replaceAll("[^a-zA-Z0-9]\\s", "");
			if (name.equals("")||surname.equals("")|| phone1 == null || error){
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

				stageForm.close();
			}
		});

	}

	private void customersFormInitializer() {
		stageForm = new Stage();
		stageForm.setTitle("Form");
		//Labels
		Label labelName = new Label("Name:");
		labelName.getStyleClass().add("formLabel");
		Label  labelSurname= new Label("Surname:");
		labelSurname.getStyleClass().add("formLabel");
		Label labelPhone1 = new Label("Phone1:");
		labelPhone1.getStyleClass().add("formLabel");
		Label labelPhone2 = new Label("Phone2:");
		labelPhone2.getStyleClass().add("formLabel");
		Label labelAddress = new Label("Address:");
		labelAddress.getStyleClass().add("formLabel");
		Label labelBalance=new Label("Balance");
		labelBalance.getStyleClass().add("formLabel");
		labelBalance2=new Label("0");
		labelEmpty = new Label("");
		labelEmpty.getStyleClass().add("errorLabel");
		Label labelTitle = new Label("Customer details");
		labelTitle.getStyleClass().add("formTitle");
		//Text fields
		textFieldName = new TextField();
		textFieldSurname = new TextField();
		textFieldPhone1 = new TextField();
		textFieldPhone2 = new TextField();
		textFieldAddress = new TextField();
		//
		buttonOk = new Button("OK");
		buttonOk.getStyleClass().add("buttonMain");
		buttonCancel = new Button("Cancel");
		buttonCancel.getStyleClass().add("buttonCancel");
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
		grid.add(labelBalance2, 1, 6);
		grid.add(labelEmpty, 0, 7, 2, 1);
		HBox boxButtons=new HBox(buttonCancel,buttonOk);
		boxButtons.getStyleClass().add("buttonForm");
		grid.add(boxButtons, 0, 8,2,1);
		grid.getStyleClass().add("formGrid");
		//Scene control
		Scene sceneForm = new Scene(grid);
		sceneForm.getStylesheets().add("stylesheets.css");
		stageForm.setScene(sceneForm);
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
	public void setTextFieldPhone1(String til) {
		this.textFieldPhone1.setText(til);
	}
}
