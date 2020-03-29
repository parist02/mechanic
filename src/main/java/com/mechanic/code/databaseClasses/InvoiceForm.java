package com.mechanic.code.databaseClasses;

import com.mechanic.code.MainScreen;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
	private Integer phone = null;
	private TextField textFieldLicensePlates, textFieldPhone;
	private Button buttonOK, buttonCancel, buttonNext, buttonBack;
	private boolean clickedOK = false;
	private Label labelEmpty;
	private Font font = Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 15);
	private Font fontBold = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16);
	private Connection connection;
	private Label labelCustomerIDfound, labelNamefound, labelSurnamefound, labelBrandfound, labelModelfound, labelVINfound;

	public InvoiceForm(Stage primaryStage, Connection connectionMain) {
		connection = connectionMain;
		GridPane gridFirst = invoiceFormFirst();
		GridPane gridSecond = invoiceFormSecond();
		Tab tab1 = new Tab("Βήμα 1ο", gridFirst);
		Tab tab2 = new Tab("Βήμα 2ο", gridSecond);
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
			if (!textFieldPhone.getText().equals("")) {
				phone = Integer.parseInt(textFieldPhone.getText().replaceAll("[^0-9]", ""));
			}
			if (licensePlates.equals("") || phone == null) {
				labelEmpty.setText("Συμπληρώστε όλα τα στοιχεία");
			} else {
				Customers customer = findCustomer(phone);
				Cars car = findCar(licensePlates);
				labelCustomerIDfound.setText(String.valueOf(customer.getCounter()));
				labelNamefound.setText(customer.getName());
				labelSurnamefound.setText(customer.getSurname());
				labelBrandfound.setText(car.getBrand());
				labelModelfound.setText(car.getModel());
				labelVINfound.setText(car.getVin());
				tab1.setDisable(true);
				tab2.setDisable(false);
				tabPane.getSelectionModel().select(1);
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
		Label labelTitle = new Label("Δώστε τα στοιχεία πελάτη και αυτοκινήτου");
		labelTitle.setFont(fontBold);
		labelTitle.setTextFill(Color.BLACK);
		Label labelPhone = new Label("Τηλέφωνο:");
		labelPhone.setFont(font);
		labelPhone.setTextFill(Color.BLACK);
		Label labelLicensePlates = new Label("Νούμερα Αυτοκινήτου:");
		labelLicensePlates.setFont(font);
		labelLicensePlates.setTextFill(Color.BLACK);
		labelEmpty = new Label("");
		labelEmpty.setFont(font);
		labelEmpty.setTextFill(Color.FIREBRICK);
		//TextFields
		textFieldLicensePlates = new TextField();
		textFieldPhone = new TextField();
		//Buttons
		buttonNext = new Button("Επόμενο");
		buttonCancel = new Button("Ακύρωση");
		//Grid control
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelPhone, 0, 1);
		grid.add(textFieldPhone, 1, 1);
		grid.add(labelLicensePlates, 0, 2);
		grid.add(textFieldLicensePlates, 1, 2);
		grid.add(labelEmpty, 0, 3, 2, 1);
		grid.add(buttonNext, 0, 4);
		grid.add(buttonCancel, 1, 4);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setHgap(20);
		grid.setVgap(15);
		//
		return (grid);
	}

	private GridPane invoiceFormSecond() {
		Label labelTitle = new Label("Στοιχεία που βρέθηκαν");
		labelTitle.setFont(fontBold);
		labelTitle.setTextFill(Color.BLACK);
		Label labelCustomerID = new Label("Κωδικός Πελάτη:");
		labelCustomerID.setFont(font);
		labelCustomerID.setTextFill(Color.BLACK);
		Label labelName = new Label("Όνομα:");
		labelName.setFont(font);
		labelName.setTextFill(Color.BLACK);
		Label labelSurname = new Label("Επίθετο:");
		labelSurname.setFont(font);
		labelSurname.setTextFill(Color.BLACK);
		Label labelBrand = new Label("Μάρκα:");
		labelBrand.setFont(font);
		labelBrand.setTextFill(Color.BLACK);
		Label labelModel = new Label("Μοντέλο:");
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
		labelBrandfound = new Label("");
		labelBrandfound.setFont(font);
		labelBrandfound.setTextFill(Color.BLACK);
		labelModelfound = new Label("");
		labelModelfound.setFont(font);
		labelModelfound.setTextFill(Color.BLACK);
		labelVINfound = new Label("");
		labelVINfound.setFont(font);
		labelVINfound.setTextFill(Color.BLACK);
		Label labelfound = new Label("Θέλετε να συνεχίσετε στην δημιουργία τιμολογίου;");
		buttonOK = new Button("Επιβεβαιωση");
		buttonBack = new Button("Προηγούμενο");
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelName, 0, 1);
		grid.add(labelNamefound, 1, 1);
		grid.add(labelSurname, 0, 2);
		grid.add(labelSurnamefound, 1, 2);
		grid.add(labelCustomerID, 0, 3);
		grid.add(labelCustomerIDfound, 1, 3);
		grid.add(labelBrand, 0, 4);
		grid.add(labelBrandfound, 1, 4);
		grid.add(labelVIN, 0, 5);
		grid.add(labelVINfound, 1, 5);
		grid.add(labelfound, 0, 7, 2, 1);
		HBox buttonBox2 = new HBox(buttonBack, buttonOK, buttonCancel);
		buttonBox2.setSpacing(20);
		grid.add(buttonBox2, 0, 8, 2, 1);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setHgap(20);
		grid.setVgap(15);
		//
		return (grid);

	}

	private Cars findCar(String noumera) {
		Cars carSearched = null;
		try {
			final String querySearch = "SELECT * FROM cars WHERE LicensePlates= '" + noumera + "';";
			System.out.println(querySearch);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(querySearch);

			while (rs.next()) {
				carSearched = new Cars(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
			}
		} catch (SQLException ex) {
			System.out.println("Error with searching");
		}
		return carSearched;
	}

	private Customers findCustomer(Integer kinito) {
		Customers customerSearched = null;
		try {
			final String querySearch = "SELECT * FROM customers WHERE Phone_1=" + kinito + ";";
			System.out.println(querySearch);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(querySearch);
			while (rs.next()) {
				customerSearched = new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7));
			}
			if (!customerSearched.equals(null)) {
				return customerSearched;
			}
			final String querySearch2 = "SELECT * FROM customers WHERE Phone_2=" + kinito + ";";
			System.out.println(querySearch2);
			rs = statement.executeQuery(querySearch2);
			while (rs.next()) {
				customerSearched = new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7));
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
		return phone;
	}

	public String getFullName(){
		return (labelNamefound.getText()+labelSurnamefound.getText());
	}

	public Integer getCustomerID(){
		return (Integer.parseInt(labelCustomerIDfound.getText()));
	}

	public String getBrandModel(){
		return (labelBrandfound.getText()+labelModelfound.getText());
	}

	public String getVin(){
		return (labelVINfound.getText());
	}





	public boolean isClickedOK() {
		return clickedOK;
	}
}
