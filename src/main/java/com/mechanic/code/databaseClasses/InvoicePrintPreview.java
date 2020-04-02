package com.mechanic.code.databaseClasses;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.Paper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class InvoicePrintPreview {
	public Stage stagePrint=new Stage();
	private static javafx.geometry.Insets padding = new Insets(10, 10, 10, 10);
	private static javafx.scene.text.Font font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 18);
	private static double height= Paper.A4.getHeight();
	private static double width=Paper.A4.getWidth();
	private ObservableList<Part>allParts= FXCollections.observableArrayList();

	public InvoicePrintPreview(Stage primaryStage, Integer customerID, String fullName, String licensePlates,String brandModel, String vin) {
		//1st Part, import from database except date
		Label labelCompany1=new Label("M.B.A LTD");
		Label labelCompany2=new Label("37 PRODROMOU\nSTR.2062 STROVOLOS");
		Label labelCompany3=new Label("Phone: 22424062");
		Label labelCompany4=new Label("Fax: 22317441");
		Label labelCompany5=new Label("VAT Reg.: 10091591L");
		Label labelCompany6=new Label("TAX REG.: 12091591N");

		VBox boxCompany=new VBox(labelCompany1,labelCompany2,labelCompany3,labelCompany4,labelCompany5,labelCompany6);
		boxCompany.setId("label1");
		boxCompany.setSpacing(5);
		Label labelTitle=new Label("Cash Invoice");
		Label labelCompany7=new Label("Invoice No.:");
		Label labelCompany8=new Label("123456");//get from database
		Label labelCompany9=new Label("Date:");
		//xrisimopoiite gia metatropi imerominies sto format pou theloume
		StringConverter<LocalDate> stringConverter = new StringConverter<>() {
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		};
		DatePicker datePickerInvoice = new DatePicker();
		datePickerInvoice.setConverter(stringConverter);
		datePickerInvoice.setValue(LocalDate.now());

		//Label labelCompany10=new Label("D/MMM/YYYY"); //depends if new  or not
		GridPane gridCompany=new GridPane();
		gridCompany.add(labelCompany7,0,0);
		gridCompany.add(labelCompany8,1,0);
		gridCompany.add(labelCompany9,0,1);
		gridCompany.add(datePickerInvoice,1,1);
		gridCompany.setVgap(5);
		gridCompany.setHgap(5);
		gridCompany.setId("grid");


		//2nd Part, need to implement search when adding customers
		Label labelCustomer1=new Label("Customer Account:");
		Label labelCustomerCustomerID=new Label(String.valueOf(customerID));
		Label labelCustomer2=new Label("Full Name:");
		Label labelCustomerFullName=new Label(fullName);
		Label labelCustomer3=new Label("Auto No.:");
		Label labelCustomerLicencePlates=new Label(licensePlates);
		Label labelCustomer4=new Label("Auto Make.:");
		Label labelCustomerBrandModel=new Label(brandModel);
		Label labelCustomer5=new Label("Chassis No.");
		Label labelCustomerVin=new Label(vin);

		GridPane gridCustomer1=new GridPane();
		gridCustomer1.add(labelCustomer1,0,0);
		gridCustomer1.add(labelCustomerCustomerID,1,0);
		gridCustomer1.add(labelCustomer2,0,1);
		gridCustomer1.add(labelCustomerFullName,1,1);
		gridCustomer1.add(labelCustomer3,0,2);
		gridCustomer1.add(labelCustomerLicencePlates,1,2);
		gridCustomer1.add(labelCustomer4,0,3);
		gridCustomer1.add(labelCustomerBrandModel,1,3);
		gridCustomer1.add(labelCustomer5,0,4);
		gridCustomer1.add(labelCustomerVin,1,4);
		gridCustomer1.setId("grid");

		Label labelCustomer6=new Label("Date In:");
		DatePicker datePickerIN = new DatePicker();
		datePickerIN.setConverter(stringConverter);
		Label labelCustomer7=new Label("Date Out:");
		DatePicker datePickerOUT = new DatePicker();
		datePickerOUT.setConverter(stringConverter);

		GridPane gridCustomer2=new GridPane();
		gridCustomer2.add(labelCustomer6,0,0);
		gridCustomer2.add(datePickerIN,1,0);
		gridCustomer2.add(labelCustomer7,0,1);
		gridCustomer2.add(datePickerOUT,1,1);


		Label labelCustomer8=new Label("Mileage:");
		TextField textFieldMileage = new TextField();
		Label labelCustomer9=new Label("First Oil:");
		TextField textFieldFirstOil = new TextField();
		Label labelCustomer10=new Label("Next Oil:");
		TextField textFieldNextOil = new TextField();
		Label labelCustomer11=new Label("Next Service:");
		TextField textFieldNextService = new TextField();

		GridPane gridCustomer3=new GridPane();
		gridCustomer3.add(labelCustomer8,0,0);
		gridCustomer3.add(textFieldMileage, 1, 0);
		gridCustomer3.add(labelCustomer9,0,1);
		gridCustomer3.add(textFieldFirstOil, 1, 1);
		gridCustomer3.add(labelCustomer10,0,2);
		gridCustomer3.add(textFieldNextOil, 1, 2);
		gridCustomer3.add(labelCustomer11,0,3);
		gridCustomer3.add(textFieldNextService, 1, 3);

		//3rd part
		TableColumn<Part,Integer> counterColumn=new TableColumn<>("No.");
		counterColumn.setReorderable(false);
		counterColumn.setResizable(false);
		counterColumn.setMinWidth(100);
		counterColumn.setCellValueFactory(new PropertyValueFactory<>("counter"));

		TableColumn<Part,String> partsIDColumn=new TableColumn<>("Parts ID");
		partsIDColumn.setReorderable(false);
		partsIDColumn.setResizable(false);
		partsIDColumn.setMinWidth(100);
		partsIDColumn.setCellValueFactory(new PropertyValueFactory<>("partsID"));

		TableColumn<Part,String> descriptionColumn=new TableColumn<>("Description");
		descriptionColumn.setReorderable(false);
		descriptionColumn.setResizable(false);
		descriptionColumn.setMinWidth(300);
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

		TableColumn<Part,Integer> quantityColumn=new TableColumn<>("Quantity");
		quantityColumn.setReorderable(false);
		quantityColumn.setResizable(false);
		quantityColumn.setMinWidth(100);
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<Part,Float> priceColumn=new TableColumn<>("Price");
		priceColumn.setReorderable(false);
		priceColumn.setResizable(false);
		priceColumn.setMinWidth(100);
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


		TableView<Part> tableViewParts = new TableView<>();
		tableViewParts.getColumns().add(counterColumn);
		tableViewParts.getColumns().add(partsIDColumn);
		tableViewParts.getColumns().add(descriptionColumn);
		tableViewParts.getColumns().add(quantityColumn);
		tableViewParts.getColumns().add(priceColumn);
		tableViewParts.setPadding(padding);
		tableViewParts.setMaxHeight(400);
		tableViewParts.setItems(allParts);

		TextField textFieldPartsID=new TextField();
		textFieldPartsID.setPromptText("Parts ID");
		//textFieldPartsID.setMinWidth(100);
		TextField textFieldDescription=new TextField();
		textFieldDescription.setPromptText("Description");
		//textFieldDescription.setMinWidth(300);
		TextField textFieldQuantity=new TextField();
		textFieldQuantity.setPromptText("Quantity");
		//textFieldQuantity.setMinWidth(100);
		TextField textFieldPrice=new TextField();
		textFieldPrice.setPromptText("Price");
		//textFieldPrice.setMinWidth(100);
		Label labelAddPart=new Label("Add new Part:");
		//labelAddPart.setMinWidth(50);
		Button buttonAddPart=new Button("Add");
		//buttonAddPart.setMinWidth(50);
		HBox hBoxAddPart=new HBox(labelAddPart,textFieldPartsID,textFieldDescription,textFieldQuantity,textFieldPrice,buttonAddPart);
		VBox vBoxParts=new VBox(tableViewParts,hBoxAddPart);
		vBoxParts.setPadding(padding);
		buttonAddPart.setOnAction(actionEvent -> {
			String partID=textFieldPartsID.getText().replaceAll("[^a-zA-Z0-9]", "");
			textFieldPartsID.setText("");
			String description=textFieldDescription.getText().replaceAll("[^a-zA-Z0-9]\\s", "");
			textFieldDescription.setText("");
			int quantity=Integer.parseInt(textFieldQuantity.getText().replaceAll("[^0-9]", ""));
			textFieldQuantity.setText("");
			float price=Float.parseFloat(textFieldPrice.getText().replaceAll("[^0-9.]", ""));
			textFieldPrice.setText("");
			int counter=allParts.size()+1;
			Part partNew=new Part(counter,0,quantity,partID,description,price);
			allParts.add(partNew);
			tableViewParts.refresh();

		});

		//4th part
		Label labelComments=new Label("Comments:");
		labelComments.setId("label2");
		TextArea textAreaComments=new  TextArea();
		textAreaComments.setPrefHeight(70);
		//textAreaComments.setPrefWidth(220);
		Label labelSignature=new Label("\tIssued By");
		Label labelDots1=new Label("..........................................");
		VBox boxComments=new VBox(labelComments,textAreaComments,labelSignature,labelDots1);
		boxComments.setId("label2");
		boxComments.setSpacing(5);

		Label labelTotal1=new Label("Amount:");
		Label labelTotal1_1=new Label("€1234.123");
		Label labelTotal2=new Label("Discount:");
		Label labelTotal2_2=new Label("€1234.123");
		Label labelTotal3=new Label("Net:");
		Label labelTotal3_3 = new Label("€1234.123");
		Label labelTotal4=new Label("Vat:");
		Label labelTotal4_4=new Label("€1234.123");
		Label labelTotal5=new Label("GTotal:");
		Label labelTotal5_5=new Label("€1234.123");
		Label labelTotal6=new Label("Received By");
		Label labelDots2=new Label("..........................................");

		GridPane gridTotal=new GridPane();
		gridTotal.add(labelTotal1, 0, 0);
		gridTotal.add(labelTotal1_1, 1, 0);
		gridTotal.add(labelTotal2, 0, 1);
		gridTotal.add(labelTotal2_2, 1, 1);
		gridTotal.add(labelTotal3, 0, 2);
		gridTotal.add(labelTotal3_3, 1, 2);
		gridTotal.add(labelTotal4, 0, 3);
		gridTotal.add(labelTotal4_4, 1, 3);
		gridTotal.add(labelTotal5, 0, 4);
		gridTotal.add(labelTotal5_5, 1, 4);
		gridTotal.add(labelTotal6, 0, 5, 2, 1);
		gridTotal.add(labelDots2, 0, 6, 2, 1);


		//Scene Control
		GridPane grid=new GridPane();
		grid.add(boxCompany,0,0);
		grid.add(labelTitle,1,0);
		grid.add(gridCompany,2,0);
		grid.add(gridCustomer1,0,1);
		grid.add(gridCustomer2,1,1);
		grid.add(gridCustomer3,2,1);
		grid.add(vBoxParts,0,2,3,1);
		grid.add(boxComments,0,3,2,1);
		grid.add(gridTotal,2,3);
		grid.setGridLinesVisible(false);
		grid.setPadding(padding);
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setAlignment(Pos.TOP_CENTER);

		Button buttonPrint=new Button("Print");



		VBox boxMain=new VBox(grid,buttonPrint);
		Scene scene=new Scene(boxMain);
		//
		//scene.getStylesheets().add(getClass().getResource("stylesheets.css").toExternalForm());
		stagePrint.initStyle(StageStyle.UTILITY);
		stagePrint.initOwner(primaryStage);
		stagePrint.setResizable(false);
		stagePrint.setScene(scene);
		buttonPrint.setOnAction(e->System.out.println("Button Print Pressed"));

	}

	public void show(){
		stagePrint.showAndWait();
	}


}