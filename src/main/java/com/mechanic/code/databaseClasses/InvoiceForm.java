package com.mechanic.code.databaseClasses;

import com.mechanic.code.ErrorPopUp;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
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
	private Button buttonOK, buttonCancel, buttonNext, buttonBack;
	private boolean clickedOK = false;
	private Label labelEmpty;
	private Font font = Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 15);
	private Font fontBold = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16);
	private Connection connection;
	private Label labelCustomerIDfound, labelNamefound, labelSurnamefound,labelPhonefound, labelBrandfound, labelModelfound, labelVINfound;
	private ErrorPopUp errorPopUp;
	private ObservableList<Mechanic>allMechanics;
	ChoiceBox<String> choiceBoxMechanic;


	public InvoiceForm(Stage primaryStage, Connection connectionMain, ObservableList<Mechanic>mechanics) {
		errorPopUp=new ErrorPopUp(0,primaryStage);
		allMechanics=mechanics;
		connection = connectionMain;
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
		stageForm.setScene(scene);
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		buttonCancel.setOnAction(actionEvent -> stageForm.close());
		buttonNext.setOnAction(actionEvent -> {
			System.out.println("Next Pressed");
			licensePlates = textFieldLicensePlates.getText().replaceAll("[^a-zA-Z0-9]", "");
			if (licensePlates.equals("") || choiceBoxMechanic.getSelectionModel().isEmpty()) {
				labelEmpty.setText("Please fill all the details!");
			} else {
				try {
					System.out.println("Mechanic Selected is: " + allMechanics.get(choiceBoxMechanic.getSelectionModel().getSelectedIndex()).getMechanicID());
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
				}catch (NullPointerException exep){
					errorPopUp.setErrorMessage("License Plates not found!");
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
			System.out.println("Create Invoice");
			stageForm.close();
		});
		stageForm.setOnCloseRequest(windowEvent -> clickedOK = false);

	}

	private GridPane invoiceFormFirst() {
		//Labels
		Label labelTitle = new Label("Invoice details");
		labelTitle.setFont(fontBold);
		labelTitle.setTextFill(Color.BLACK);
		Label labelLicensePlates = new Label("License Plates:");
		labelLicensePlates.setFont(font);
		labelLicensePlates.setTextFill(Color.BLACK);
		Label labelMechanic=new Label("Mechanic:");
		labelMechanic.setFont(font);
		labelMechanic.setTextFill(Color.BLACK);
		labelEmpty = new Label("");
		labelEmpty.setFont(font);
		labelEmpty.setTextFill(Color.FIREBRICK);
		//TextFields
		textFieldLicensePlates = new TextField();
		//choiceBox
		choiceBoxMechanic = new ChoiceBox<>();
		for (Mechanic m :allMechanics){
			choiceBoxMechanic.getItems().add(m.getName());
		}
		//Buttons
		buttonNext = new Button("Next");
		buttonCancel = new Button("Cancel");
		//Grid control
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelLicensePlates, 0, 1);
		grid.add(textFieldLicensePlates, 1, 1);
		grid.add(labelMechanic, 0, 2);
		grid.add(choiceBoxMechanic, 1, 2);
		grid.add(labelEmpty, 0, 3, 2, 1);
		HBox buttonBox = new HBox(buttonNext, buttonCancel);
		buttonBox.setSpacing(20);
		grid.add(buttonBox, 0, 4,2,1);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setHgap(20);
		grid.setVgap(15);
		//
		return (grid);
	}

	private GridPane invoiceFormSecond() {
		Label labelTitle = new Label("Data found");
		labelTitle.setFont(fontBold);
		labelTitle.setTextFill(Color.BLACK);
		Label labelCustomerID = new Label("Customer ID:");
		labelCustomerID.setFont(font);
		labelCustomerID.setTextFill(Color.BLACK);
		Label labelName = new Label("Name:");
		labelName.setFont(font);
		labelName.setTextFill(Color.BLACK);
		Label labelSurname = new Label("Surname:");
		labelSurname.setFont(font);
		labelSurname.setTextFill(Color.BLACK);
		Label labelPhone=new Label("Phone:");
		labelPhone.setFont(font);
		labelPhone.setTextFill(Color.BLACK);
		Label labelBrand = new Label("Brand:");
		labelBrand.setFont(font);
		labelBrand.setTextFill(Color.BLACK);
		Label labelModel = new Label("Model:");
		labelModel.setFont(font);
		labelModel.setTextFill(Color.BLACK);
		Label labelVIN = new Label("VIN:");
		labelVIN.setFont(font);
		labelVIN.setTextFill(Color.BLACK);
		labelCustomerIDfound = new Label("");
		labelCustomerIDfound.setFont(font);
		labelCustomerIDfound.setTextFill(Color.BLACK);
		labelNamefound = new Label("");
		labelNamefound.setFont(font);
		labelNamefound.setTextFill(Color.BLACK);
		labelSurnamefound = new Label("");
		labelSurnamefound.setFont(font);
		labelSurnamefound.setTextFill(Color.BLACK);
		labelPhonefound = new Label("");
		labelPhonefound.setFont(font);
		labelPhonefound.setTextFill(Color.BLACK);
		labelBrandfound = new Label("");
		labelBrandfound.setFont(font);
		labelBrandfound.setTextFill(Color.BLACK);
		labelModelfound = new Label("");
		labelModelfound.setFont(font);
		labelModelfound.setTextFill(Color.BLACK);
		labelVINfound = new Label("");
		labelVINfound.setFont(font);
		labelVINfound.setTextFill(Color.BLACK);
		Label labelfound = new Label("Do you want to continue with the creation of the invoice?");
		buttonOK = new Button("Confirm");
		buttonBack = new Button("Back");
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
		HBox buttonBox2 = new HBox(buttonBack, buttonOK, buttonCancel);
		buttonBox2.setSpacing(20);
		grid.add(buttonBox2, 0, 9, 2, 1);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setHgap(20);
		grid.setVgap(15);
		//
		return (grid);

	}

	private Car findCar(String noumera) {
		Car carSearched = null;
		try {
			final String querySearch = "SELECT * FROM cars WHERE LicensePlates= '" + noumera + "';";
			System.out.println(querySearch);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(querySearch);

			while (rs.next()) {
				carSearched = new Car(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
			}
		} catch (SQLException ex) {
			System.out.println("Error with searching");
		}
		return carSearched;
	}

	private Customer findCustomer(Integer kinito) {
		Customer customerSearched = null;
		try {
			final String querySearch = "SELECT * FROM customers WHERE CustomerID=" + kinito + ";";
			System.out.println(querySearch);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(querySearch);
			while (rs.next()) {
				customerSearched = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7));
			}
		} catch (SQLException ex) {
			System.out.println("Error with searching");
		}
		return customerSearched;
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
	public boolean isClickedOK() {
		return clickedOK;
	}
}
