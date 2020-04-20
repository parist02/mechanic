package com.mechanic.code.forms;

import com.mechanic.code.main.ErrorPopUp;
import com.mechanic.code.database.Car;
import com.mechanic.code.database.Customer;
import com.mechanic.code.database.Mechanic;
import com.mechanic.code.database.Repair;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InvoiceForm {
	private Stage stageForm;
	private String licensePlates = null;
	private TextField textFieldLicensePlates;
	private Button buttonOK, buttonCancel,buttonCancel2, buttonNext, buttonBack;
	private boolean clickedOK = false;
	private Label labelEmpty;
	private Label labelCustomerIDfound, labelNamefound, labelSurnamefound,labelPhonefound, labelBrandfound, labelModelfound, labelVINfound;
	private ErrorPopUp errorPopUp;
	private ObservableList<Mechanic>allMechanics;
	private ObservableList<Repair>allRepairs;
	private ObservableList<Customer>allCustomers;
	private ObservableList<Car>allCars;
	private ChoiceBox<String> choiceBoxMechanic;
	private ChoiceBox<String>choiceBoxRepair;
	private ChoiceBox<String>choiceBoxCredit;


	public InvoiceForm(Stage primaryStage, Connection connectionMain, ObservableList<Mechanic>mechanics,ObservableList<Repair>repairs,ObservableList<Customer>customers,ObservableList<Car>cars) {
		errorPopUp=new ErrorPopUp(0,primaryStage);
		allMechanics=mechanics;
		allRepairs=repairs;
		allCars=cars;
		allCustomers=customers;
		GridPane gridFirst = invoiceFormFirst();
		GridPane gridSecond = invoiceFormSecond();
		Tab tab1 = new Tab("1st Step", gridFirst);
		Tab tab2 = new Tab("2nd Step", gridSecond);
		tab2.setDisable(true);
		TabPane tabPane = new TabPane();
		tabPane.getTabs().addAll(tab1, tab2);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);
		stageForm = new Stage();
		Scene scene = new Scene(tabPane);
		scene.getStylesheets().add("stylesheets.css");
		stageForm.setScene(scene);
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		buttonCancel.setOnAction(actionEvent -> stageForm.close());
		buttonNext.setOnAction(actionEvent -> {
			licensePlates = textFieldLicensePlates.getText().replaceAll("[^a-zA-Z0-9]", "");
			if (licensePlates.equals("") || choiceBoxMechanic.getSelectionModel().isEmpty() || choiceBoxRepair.getSelectionModel().isEmpty() || choiceBoxCredit.getSelectionModel().isEmpty()) {
				labelEmpty.setText("Please fill all the details!");
			} else {
				try {
					Car car = findCar(licensePlates);
					Customer customer = findCustomer(car.getCustomerId());
					labelCustomerIDfound.setText(String.valueOf(customer.getCounter()));
					labelNamefound.setText(customer.getName());
					labelSurnamefound.setText(customer.getSurname());
					labelPhonefound.setText(String.valueOf(customer.getPhone1()));
					labelBrandfound.setText(car.getBrand());
					labelModelfound.setText(car.getModel());
					labelVINfound.setText(car.getVin());
					tab1.setDisable(true);
					tab2.setDisable(false);
					tabPane.getSelectionModel().select(1);
				}catch (IndexOutOfBoundsException exep){
					errorPopUp.setErrorMessage("Error with license plates. Customer or Car not found");
					errorPopUp.showError();
				}
			}
		});
		buttonBack.setOnAction(actionEvent -> {
			tab1.setDisable(false);
			tab2.setDisable(true);
			tabPane.getSelectionModel().select(0);
		});
		buttonOK.setOnAction(actionEvent -> {
			clickedOK = true;
			stageForm.close();
		});
		stageForm.setOnCloseRequest(windowEvent -> clickedOK = false);

	}

	private GridPane invoiceFormFirst() {
		//Labels
		Label labelTitle = new Label("Invoice details");
		labelTitle.getStyleClass().add("formTitle");
		Label labelLicensePlates = new Label("License Plates:");
		labelLicensePlates.getStyleClass().add("formLabel");
		Label labelMechanic=new Label("Mechanic:");
		labelMechanic.getStyleClass().add("formLabel");
		Label labelRepair=new Label("Repair:");
		labelRepair.getStyleClass().add("formLabel");
		Label labelCredit=new Label("Credit/Cash:");
		labelCredit.getStyleClass().add("formLabel");
		labelEmpty = new Label("");
		labelEmpty.getStyleClass().add("errorLabel");
		//TextFields
		textFieldLicensePlates = new TextField();
		//choiceBox
		choiceBoxMechanic = new ChoiceBox<>();
		for (Mechanic m :allMechanics){
			choiceBoxMechanic.getItems().add(m.getName());
		}
		choiceBoxRepair=new ChoiceBox<>();
		for (Repair r :allRepairs){
			choiceBoxRepair.getItems().add(r.getName());
		}
		choiceBoxCredit=new ChoiceBox<>();
		choiceBoxCredit.getItems().add("Credit");
		choiceBoxCredit.getItems().add("Cash");
		//Buttons
		buttonNext = new Button("Next");
		buttonNext.getStyleClass().add("buttonMain");
		buttonCancel = new Button("Cancel");
		buttonCancel.getStyleClass().add("buttonCancel");
		//Grid control
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelLicensePlates, 0, 1);
		grid.add(textFieldLicensePlates, 1, 1);
		grid.add(labelMechanic, 0, 2);
		grid.add(choiceBoxMechanic, 1, 2);
		grid.add(labelRepair, 0, 3);
		grid.add(choiceBoxRepair, 1, 3);
		grid.add(labelCredit, 0, 4);
		grid.add(choiceBoxCredit, 1, 4);
		grid.add(labelEmpty, 0, 5, 2, 1);
		HBox buttonBox = new HBox(buttonCancel,buttonNext);
		buttonBox.getStyleClass().add("buttonForm");
		grid.add(buttonBox, 0, 6,2,1);
		grid.getStyleClass().add("formGrid");
		//
		return (grid);
	}

	private GridPane invoiceFormSecond() {
		Label labelTitle = new Label("Data found");
		labelTitle.getStyleClass().add("formTitle");
		Label labelCustomerID = new Label("Customer ID:");
		labelCustomerID.getStyleClass().add("formLabel");
		Label labelName = new Label("Name:");
		labelName.getStyleClass().add("formLabel");
		Label labelSurname = new Label("Surname:");
		labelSurname.getStyleClass().add("formLabel");
		Label labelPhone=new Label("Phone:");
		labelPhone.getStyleClass().add("formLabel");
		Label labelBrand = new Label("Brand:");
		labelBrand.getStyleClass().add("formLabel");
		Label labelModel = new Label("Model:");//////
		labelModel.getStyleClass().add("formLabel");
		Label labelVIN = new Label("VIN:");
		labelVIN.getStyleClass().add("formLabel");
		labelCustomerIDfound = new Label("");
		labelCustomerIDfound.getStyleClass().add("formLabel2");
		labelNamefound = new Label("");
		labelNamefound.getStyleClass().add("formLabel2");
		labelSurnamefound = new Label("");
		labelSurnamefound.getStyleClass().add("formLabel2");
		labelPhonefound = new Label("");
		labelPhonefound.getStyleClass().add("formLabel2");
		labelBrandfound = new Label("");
		labelBrandfound.getStyleClass().add("formLabel2");
		labelModelfound = new Label("");
		labelModelfound.getStyleClass().add("formLabel2");
		labelVINfound = new Label("");
		labelVINfound.getStyleClass().add("formLabel2");
		Label labelfound = new Label("Do you want to continue with the creation of the invoice?");
		labelfound.getStyleClass().add("formLabel2");
		buttonOK = new Button("Confirm");
		buttonOK.getStyleClass().add("buttonMain");
		buttonBack = new Button("Back");
		buttonBack.getStyleClass().add("buttonCancel");
		buttonCancel2=new Button("Cancel");
		buttonCancel2.getStyleClass().add("buttonCancel");
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelName, 0, 1);
		grid.add(labelNamefound, 1, 1);
		grid.add(labelSurname, 0, 2);
		grid.add(labelSurnamefound, 1, 2);
		grid.add(labelPhone, 0, 3);
		grid.add(labelPhonefound, 1, 3);
		grid.add(labelCustomerID, 0, 4);
		grid.add(labelCustomerIDfound, 1, 4);
		grid.add(labelBrand, 0, 5);
		grid.add(labelBrandfound, 1, 5);
		grid.add(labelVIN, 0, 6);
		grid.add(labelVINfound, 1, 6);
		grid.add(labelfound, 0, 8, 2, 1);
		HBox buttonBox2 = new HBox(buttonBack, buttonCancel2,buttonOK);
		buttonBox2.getStyleClass().add("buttonForm");
		grid.add(buttonBox2, 0, 9, 2, 1);
		grid.getStyleClass().add("formGrid");
		//
		return (grid);

	}

	private Car findCar(String noumera) throws IndexOutOfBoundsException {
		FilteredList<Car>filteredListCars=new FilteredList<>(allCars.filtered(car -> car.getLicencePlates().equals(noumera)));
		return (filteredListCars.get(0));
	}

	private Customer findCustomer(Integer arithmosPelati) throws IndexOutOfBoundsException{
		FilteredList<Customer>filteredListCustomer=new FilteredList<>(allCustomers.filtered(customer -> customer.getCounter()==arithmosPelati));
		return (filteredListCustomer.get(0));
	}


	public void showForm() {
		stageForm.showAndWait();
	}

	public String getLicensePlates() {
		return licensePlates;
	}

	public Integer getPhone() {
		return (Integer.parseInt(labelPhonefound.getText()));
	}

	public String getFullName(){
		return (labelNamefound.getText()+" "+labelSurnamefound.getText());
	}

	public Integer getCustomerID(){
		return (Integer.parseInt(labelCustomerIDfound.getText()));
	}

	public String getBrandModel(){
		return (labelBrandfound.getText()+" "+labelModelfound.getText());
	}

	public String getVin(){
		return (labelVINfound.getText());
	}

	public Integer getMechanicIndex(){
		return (choiceBoxMechanic.getSelectionModel().getSelectedIndex());
	}

	public Integer getRepairIndex(){
		return (choiceBoxRepair.getSelectionModel().getSelectedIndex());
	}
	public boolean isClickedOK() {
		return clickedOK;
	}

	public String getCreditCash(){
		return (choiceBoxCredit.getSelectionModel().getSelectedItem());
	}
}
