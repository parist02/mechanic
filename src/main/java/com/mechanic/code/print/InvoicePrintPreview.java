package com.mechanic.code.print;
import com.mechanic.code.database.Invoice;
import com.mechanic.code.main.ErrorPopUp;
import com.mechanic.code.database.Part;
import com.mechanic.code.main.MainScreen;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.Paper;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;


public class InvoicePrintPreview {
	private Stage stagePrint = new Stage();
	private static final javafx.geometry.Insets padding = new Insets(10, 10, 10, 10);
	private static final javafx.scene.text.Font font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 18);
	private static double height = Paper.A4.getHeight();
	private static double width = Paper.A4.getWidth();
	private LocalDate dateInvoice, dateIN, dateOUT;
	private Integer mileage, firstOil, nextOil, nextService,invoiceID;
	private String comments;
	private ObservableList<Part> allParts = FXCollections.observableArrayList();
	private ErrorPopUp errorPopUp = new ErrorPopUp(0, stagePrint);
	private boolean beingPrinted = false, beingSaved = false;
	private float amount = 0, net = 0, vat, total = 0, vatTotal,discount=0;
	private Label labelInvoiceID,labelInvoiceDate, labelInvoiceDateIN,labelInvoiceDateOUT;
	private Label labelMileage,labelFirstOil,labelNextOil,labelNextService,labelDiscount;
	private Label labelAmount,labelNet,labelVat,labelTotal;
	private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
	private GridPane gridForPrint,gridCompany,gridCustomer2,gridCustomer3,grid,gridTotal;
	private DatePicker datePickerInvoice,datePickerIN,datePickerOUT;
	private TextField textFieldMileage,textFieldFirstOil,textFieldNextOil,textFieldNextService,textFieldDiscount;
	private  VBox vBoxParts;
	private TableView<Part>tableViewParts;
	private TextArea textAreaComments;
	private Button buttonSave,buttonPrint;
	private HBox boxButtons;

	public InvoicePrintPreview(Stage primaryStage, Integer customerID, String fullName, String licensePlates, String brandModel, String vin) {
		vat=MainScreen.getVat();
		//1st Part, import from database except date
		Label labelCompany1 = new Label("M.B.A LTD");
		Label labelCompany2 = new Label("37 PRODROMOU\nSTR.2062 STROVOLOS");
		Label labelCompany3 = new Label("Phone: 22424062");
		Label labelCompany4 = new Label("Fax: 22317441");
		Label labelCompany5 = new Label("VAT Reg.: 10091591L");
		Label labelCompany6 = new Label("TAX REG.: 12091591N");

		VBox boxCompany = new VBox(labelCompany1, labelCompany2, labelCompany3, labelCompany4, labelCompany5, labelCompany6);
		boxCompany.setId("label2");
		boxCompany.setSpacing(5);
		Label labelTitle = new Label("Cash Invoice");
		labelTitle.setId("title");
		Label labelCompany7 = new Label("Invoice No.:");
		labelInvoiceID = new Label("");//get from database
		Label labelCompany9 = new Label("Date:");
		//xrisimopoiite gia metatropi imerominies sto format pou theloume
		StringConverter<LocalDate> stringConverter = new StringConverter<>() {
			private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
		datePickerInvoice = new DatePicker();
		datePickerInvoice.setConverter(stringConverter);
		datePickerInvoice.setValue(LocalDate.now());

		//Label labelCompany10=new Label("D/MMM/YYYY"); //depends if new  or not
		gridCompany = new GridPane();
		gridCompany.add(labelCompany7, 0, 0);
		gridCompany.add(labelInvoiceID, 1, 0);
		gridCompany.add(labelCompany9, 0, 1);
		gridCompany.add(datePickerInvoice, 1, 1);
		gridCompany.setVgap(5);
		gridCompany.setHgap(5);
		gridCompany.setId("grid");
		labelInvoiceDate=new Label("");

		//2nd Part, need to implement search when adding customers
		Label labelCustomer1 = new Label("Customer Account:");
		Label labelCustomerCustomerID = new Label(String.valueOf(customerID));
		Label labelCustomer2 = new Label("Full Name:");
		Label labelCustomerFullName = new Label(fullName);
		Label labelCustomer3 = new Label("Auto No.:");
		Label labelCustomerLicencePlates = new Label(licensePlates);
		Label labelCustomer4 = new Label("Auto Make.:");
		Label labelCustomerBrandModel = new Label(brandModel);
		Label labelCustomer5 = new Label("Chassis No.");
		Label labelCustomerVin = new Label(vin);

		GridPane gridCustomer1 = new GridPane();
		gridCustomer1.add(labelCustomer1, 0, 0);
		gridCustomer1.add(labelCustomerCustomerID, 1, 0);
		gridCustomer1.add(labelCustomer2, 0, 1);
		gridCustomer1.add(labelCustomerFullName, 1, 1);
		gridCustomer1.add(labelCustomer3, 0, 2);
		gridCustomer1.add(labelCustomerLicencePlates, 1, 2);
		gridCustomer1.add(labelCustomer4, 0, 3);
		gridCustomer1.add(labelCustomerBrandModel, 1, 3);
		gridCustomer1.add(labelCustomer5, 0, 4);
		gridCustomer1.add(labelCustomerVin, 1, 4);
		gridCustomer1.setId("grid");

		Label labelCustomer6 = new Label("Date In:");
		datePickerIN = new DatePicker();
		datePickerIN.setConverter(stringConverter);
		Label labelCustomer7 = new Label("Date Out:");
		datePickerOUT = new DatePicker();
		datePickerOUT.setConverter(stringConverter);

		gridCustomer2 = new GridPane();
		gridCustomer2.add(labelCustomer6, 0, 0);
		gridCustomer2.add(datePickerIN, 1, 0);
		gridCustomer2.add(labelCustomer7, 0, 1);
		gridCustomer2.add(datePickerOUT, 1, 1);

		labelInvoiceDateIN=new Label("");
		labelInvoiceDateOUT=new Label("");

		Label labelCustomer8 = new Label("Mileage:");
		textFieldMileage = new TextField();
		Label labelCustomer9 = new Label("First Oil:");
		textFieldFirstOil = new TextField();
		Label labelCustomer10 = new Label("Next Oil:");
		textFieldNextOil = new TextField();
		Label labelCustomer11 = new Label("Next Service:");
		textFieldNextService = new TextField();

		gridCustomer3 = new GridPane();
		gridCustomer3.add(labelCustomer8, 0, 0);
		gridCustomer3.add(textFieldMileage, 1, 0);
		gridCustomer3.add(labelCustomer9, 0, 1);
		gridCustomer3.add(textFieldFirstOil, 1, 1);
		gridCustomer3.add(labelCustomer10, 0, 2);
		gridCustomer3.add(textFieldNextOil, 1, 2);
		gridCustomer3.add(labelCustomer11, 0, 3);
		gridCustomer3.add(textFieldNextService, 1, 3);

		labelMileage=new Label("");
		labelFirstOil=new Label("");
		labelNextOil=new Label("");
		labelNextService=new Label("");


		//3rd part
		TableColumn<Part, Integer> counterColumn = new TableColumn<>("No.");
		counterColumn.setReorderable(false);
		counterColumn.setResizable(false);
		counterColumn.setMinWidth(100);
		counterColumn.setCellValueFactory(new PropertyValueFactory<>("counter"));

		TableColumn<Part, String> partsIDColumn = new TableColumn<>("Parts ID");
		partsIDColumn.setReorderable(false);
		partsIDColumn.setResizable(false);
		partsIDColumn.setMinWidth(100);
		partsIDColumn.setCellValueFactory(new PropertyValueFactory<>("partsID"));

		TableColumn<Part, String> descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setReorderable(false);
		descriptionColumn.setResizable(false);
		descriptionColumn.setMinWidth(300);
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

		TableColumn<Part, Integer> quantityColumn = new TableColumn<>("Quantity");
		quantityColumn.setReorderable(false);
		quantityColumn.setResizable(false);
		quantityColumn.setMinWidth(100);
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<Part, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setReorderable(false);
		priceColumn.setResizable(false);
		priceColumn.setMinWidth(100);
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceColumn.setCellFactory(partFloatTableColumn -> new TableCell<>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					setText(decimalFormat.format(item));
				}
			}
		});

		tableViewParts = new TableView<>();
		tableViewParts.getColumns().add(counterColumn);
		tableViewParts.getColumns().add(partsIDColumn);
		tableViewParts.getColumns().add(descriptionColumn);
		tableViewParts.getColumns().add(quantityColumn);
		tableViewParts.getColumns().add(priceColumn);
		tableViewParts.setPadding(padding);
		tableViewParts.setMaxHeight(400);
		tableViewParts.setEditable(false);
		tableViewParts.setItems(allParts);
		

		TextField textFieldPartsID = new TextField();
		textFieldPartsID.setPromptText("Parts ID");
		//textFieldPartsID.setMinWidth(100);
		TextField textFieldDescription = new TextField();
		textFieldDescription.setPromptText("Description");
		//textFieldDescription.setMinWidth(300);
		TextField textFieldQuantity = new TextField();
		textFieldQuantity.setPromptText("Quantity");
		//textFieldQuantity.setMinWidth(100);
		TextField textFieldPrice = new TextField();
		textFieldPrice.setPromptText("Price");
		//textFieldPrice.setMinWidth(100);
		Label labelAddPart = new Label("Add new Part:");
		//labelAddPart.setMinWidth(50);
		Button buttonAddPart = new Button("Add");
		//buttonAddPart.setMinWidth(50);
		HBox hBoxAddPart = new HBox(labelAddPart, textFieldPartsID, textFieldDescription, textFieldQuantity, textFieldPrice, buttonAddPart);
		vBoxParts = new VBox(tableViewParts, hBoxAddPart);
		vBoxParts.setPadding(padding);

		//4th part
		Label labelComments = new Label("Comments:");
		labelComments.setId("label2");
		textAreaComments = new TextArea();
		textAreaComments.setPrefHeight(70);
		//textAreaComments.setPrefWidth(220);
		Label labelSignature = new Label("\tIssued By");
		Label labelDots1 = new Label("..........................................");
		VBox boxComments = new VBox(labelComments, textAreaComments, labelSignature, labelDots1);
		boxComments.setId("label2");
		boxComments.setSpacing(5);

		Label labelTotal1 = new Label("Amount:");
		labelAmount = new Label("");
		Label labelTotal2 = new Label("Discount:");
		labelDiscount = new Label("");
		textFieldDiscount=new TextField("0");
		Label labelTotal3 = new Label("Net:");
		labelNet = new Label("");
		Label labelTotal4 = new Label("Vat:");
		labelVat = new Label("");
		Label labelTotal5 = new Label("GTotal:");
		labelTotal = new Label("");
		Label labelTotal6 = new Label("Received By");
		Label labelDots2 = new Label("..........................................");

		gridTotal = new GridPane();
		gridTotal.add(labelTotal1, 0, 0);
		gridTotal.add(labelAmount, 1, 0);
		gridTotal.add(labelTotal2, 0, 1);
		gridTotal.add(textFieldDiscount, 1, 1);
		gridTotal.add(labelTotal3, 0, 2);
		gridTotal.add(labelNet, 1, 2);
		gridTotal.add(labelTotal4, 0, 3);
		gridTotal.add(labelVat, 1, 3);
		gridTotal.add(labelTotal5, 0, 4);
		gridTotal.add(labelTotal, 1, 4);
		gridTotal.add(labelTotal6, 0, 5, 2, 1);
		gridTotal.add(labelDots2, 0, 6, 2, 1);


		//Scene Control
		grid = new GridPane();
		grid.add(boxCompany, 0, 0);
		grid.add(labelTitle, 1, 0);
		grid.add(gridCompany, 2, 0);
		grid.add(gridCustomer1, 0, 1);
		grid.add(gridCustomer2, 1, 1);
		grid.add(gridCustomer3, 2, 1);
		grid.add(vBoxParts, 0, 2, 3, 1);
		grid.add(boxComments, 0, 3, 2, 1);
		grid.add(gridTotal, 2, 3);
		grid.setGridLinesVisible(false);
		grid.setPadding(padding);
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setAlignment(Pos.TOP_CENTER);

		buttonPrint = new Button("Save and Show Print Preview");
		Tooltip tooltipPrint = new Tooltip("Save Invoice and Print");
		buttonPrint.setTooltip(tooltipPrint);
		buttonSave = new Button("Save and Close");
		Tooltip tooltipSave = new Tooltip("Save Invoice without Printing");
		buttonSave.setTooltip(tooltipSave);

		boxButtons = new HBox(buttonPrint, buttonSave);
		boxButtons.setPadding(padding);
		boxButtons.setSpacing(10);
		VBox boxMain = new VBox(grid, boxButtons);
		boxMain.getStylesheets().add("stylesheets.css");
		Scene scene = new Scene(boxMain);
		stagePrint.initStyle(StageStyle.UTILITY);
		stagePrint.initOwner(primaryStage);
		stagePrint.setResizable(false);
		stagePrint.setScene(scene);
		buttonAddPart.setOnAction(actionEvent -> {
			boolean error = false;
			int quantity = 0;
			float price = 0;
			String partID = textFieldPartsID.getText().replaceAll("[^a-zA-Z0-9]", "");
			if (partID.equals("")) {
				error = true;
			}
			String description = textFieldDescription.getText().replaceAll("[^a-zA-Z0-9/.-]\\s", "");
			if (description.equals("")) {
				textFieldDescription.setText(description);
				error = true;
			}
			try {
				quantity = Integer.parseInt(textFieldQuantity.getText().replaceAll("[^0-9]", ""));
				price = Float.parseFloat(textFieldPrice.getText().replaceAll("[^0-9.]", ""));
				discount=Float.parseFloat(textFieldDiscount.getText().replaceAll("[^0-9.]", ""));
			} catch (Exception ex) {
				error = true;
			}
			textFieldPrice.setText("");
			textFieldQuantity.setText("");
			textFieldDiscount.setText(String.valueOf(discount));
			if (!error) {
				textFieldPartsID.setText("");
				textFieldDescription.setText("");
				int counter = allParts.size() + 1;
				Part partNew = new Part(counter, 0, quantity, description, partID, price);
				amount = amount + quantity * price;
				amount = Float.parseFloat(decimalFormat.format(amount));
				net = Float.parseFloat(decimalFormat.format(amount - discount));
				vatTotal = Float.parseFloat(decimalFormat.format(net * vat));
				total = Float.parseFloat(decimalFormat.format(net + vatTotal));
				labelAmount.setText(String.valueOf(amount));
				labelNet.setText(String.valueOf(net));
				labelVat.setText(String.valueOf(vatTotal));
				labelTotal.setText(String.valueOf(total));
				labelDiscount.setText(String.valueOf(discount));
				allParts.add(partNew);
				tableViewParts.refresh();
			}
		});
		buttonPrint.setOnAction(actionEvent -> {
			dateInvoice = datePickerInvoice.getValue();
			dateIN = datePickerIN.getValue();
			dateOUT = datePickerOUT.getValue();
			if (!textFieldMileage.getText().equals("")) {
				mileage = Integer.parseInt(textFieldMileage.getText().replaceAll("[^0-9]", ""));
				textFieldMileage.setText(String.valueOf(mileage));
			}
			if (!textFieldFirstOil.getText().equals("")) {
				firstOil = Integer.parseInt(textFieldFirstOil.getText().replaceAll("[^0-9]", ""));
				textFieldFirstOil.setText(String.valueOf(firstOil));
			}
			if (!textFieldNextOil.getText().equals("")) {
				nextOil = Integer.parseInt(textFieldNextOil.getText().replaceAll("[^0-9]", ""));
				textFieldNextOil.setText(String.valueOf(nextOil));
			}
			if (!textFieldNextService.getText().equals("")) {
				nextService = Integer.parseInt(textFieldNextService.getText().replaceAll("[^0-9]", ""));
				textFieldNextService.setText(String.valueOf(nextService));
			}
			if (allParts.isEmpty() || mileage == null || firstOil == null || nextOil == null || nextService == null) {
				errorPopUp.setErrorMessage("Please fill all the details.");
				errorPopUp.showError();
			} else {
				comments = textAreaComments.getText();
				beingSaved = true;
				beingPrinted = true;
				stagePrint.close();
			}
		});
		buttonSave.setOnAction(actionEvent -> {
			dateInvoice = datePickerInvoice.getValue();
			dateIN = datePickerIN.getValue();
			dateOUT = datePickerOUT.getValue();
			if (!textFieldMileage.getText().equals("")) {
				mileage = Integer.parseInt(textFieldMileage.getText().replaceAll("[^0-9]", ""));
				textFieldMileage.setText(String.valueOf(mileage));
			}
			if (!textFieldFirstOil.getText().equals("")) {
				firstOil = Integer.parseInt(textFieldFirstOil.getText().replaceAll("[^0-9]", ""));
				textFieldFirstOil.setText(String.valueOf(firstOil));
			}
			if (!textFieldNextOil.getText().equals("")) {
				nextOil = Integer.parseInt(textFieldNextOil.getText().replaceAll("[^0-9]", ""));
				textFieldNextOil.setText(String.valueOf(nextOil));
			}
			if (!textFieldNextService.getText().equals("")) {
				nextService = Integer.parseInt(textFieldNextService.getText().replaceAll("[^0-9]", ""));
				textFieldNextService.setText(String.valueOf(nextService));
			}
			if (allParts.isEmpty() || mileage == null || firstOil == null || nextOil == null || nextService == null) {
				errorPopUp.setErrorMessage("Please fill all the details.");
				errorPopUp.showError();
			} else {
				comments = textAreaComments.getText();
				beingSaved = true;
				beingPrinted = false;
				stagePrint.close();
			}
		});


	}

	public void show() {
		stagePrint.showAndWait();
	}

	public ObservableList<Part> getAllParts() {
		return allParts;
	}

	public LocalDate getDateInvoice() {
		return dateInvoice;
	}

	public LocalDate getDateIN() {
		return dateIN;
	}

	public LocalDate getDateOUT() {
		return dateOUT;
	}

	public String getComments() {
		return comments;
	}

	public Integer getMileage() {
		return mileage;
	}

	public Integer getFirstOil() {
		return firstOil;
	}

	public Integer getNextOil() {
		return nextOil;
	}

	public Integer getNextService() {
		return nextService;
	}

	public float getVat() {
		return vat;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setVat(float vat) {
		this.vat = vat;
	}

	public Integer getInvoiceID() {
		return invoiceID;
	}

	public void setInvoiceID(Integer invoiceID) {
		this.invoiceID = invoiceID;
		labelInvoiceID.setText(String.valueOf(invoiceID));
	}

	public void setNextService(Integer nextService) {
		this.nextService = nextService;
		labelNextService.setText(String.valueOf(nextService));
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
		labelMileage.setText(String.valueOf(mileage));
	}

	public void setFirstOil(Integer firstOil) {
		this.firstOil = firstOil;
		labelFirstOil.setText(String.valueOf(firstOil));
	}

	public void setNextOil(Integer nextOil) {
		this.nextOil = nextOil;
		labelNextOil.setText(String.valueOf(nextOil));
	}

	public void setAllParts(ObservableList<Part> allParts) {
		this.allParts = allParts;
		for (int i=0;i<allParts.size();i++){
			this.allParts.get(i).setCounter(i+1);
		}
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public void setDateInvoice(LocalDate dateInvoice) {
		this.dateInvoice = dateInvoice;
	}

	public void setDateIN(LocalDate dateIN) {
		this.dateIN = dateIN;
	}

	public void setDateOUT(LocalDate dateOUT) {
		this.dateOUT = dateOUT;
	}

	public void setComments(String comments) {
		this.comments = comments;
		textAreaComments.setText(comments);
	}

	public boolean isBeingPrinted() {
		return beingPrinted;
	}

	public boolean isBeingSaved() {
		return beingSaved;
	}

	public float getAmount() {
		return amount;
	}


	private void readyForPrint(){
		labelInvoiceDate.setText(String.valueOf(dateInvoice));
		labelInvoiceDateIN.setText(String.valueOf(dateIN));
		labelInvoiceDateOUT.setText(String.valueOf(dateOUT));
		labelMileage.setText(String.valueOf(mileage));
		labelFirstOil.setText(String.valueOf(firstOil));
		labelNextOil.setText(String.valueOf(nextOil));
		labelNextService.setText(String.valueOf(nextService));
		gridCompany.getChildren().remove(datePickerInvoice);
		gridCompany.add(labelInvoiceDate,1,1);
		gridCustomer2.getChildren().remove(datePickerIN);
		gridCustomer2.getChildren().remove(datePickerOUT);
		gridCustomer2.add(labelInvoiceDateIN, 1, 0);
		gridCustomer2.add(labelInvoiceDateOUT, 1, 1);
		gridCustomer3.getChildren().remove(textFieldMileage);
		gridCustomer3.getChildren().remove(textFieldFirstOil);
		gridCustomer3.getChildren().remove(textFieldNextOil);
		gridCustomer3.getChildren().remove(textFieldNextService);
		gridCustomer3.add(labelMileage, 1, 0);
		gridCustomer3.add(labelFirstOil, 1, 1);
		gridCustomer3.add(labelNextOil, 1, 2);
		gridCustomer3.add(labelNextService, 1, 3);
		gridTotal.getChildren().remove(textFieldDiscount);
		gridTotal.add(labelDiscount, 1, 1);
		grid.getChildren().remove(vBoxParts);
		grid.add(tableViewParts, 0, 2, 3, 1);
	}

	public GridPane getNodeForPrint(boolean isOpen){
		if(!isOpen){
			readyForPrint();
		}
		textAreaComments.setDisable(false);
		return (grid);
	}

	private void calculateAmount(){
		for(Part p : allParts) {
			amount = Float.parseFloat(String.valueOf(amount + p.getQuantity() * p.getPrice()));
			amount = Float.parseFloat(decimalFormat.format(amount));
			net = Float.parseFloat(decimalFormat.format(amount - discount));
			vatTotal = Float.parseFloat(decimalFormat.format(net * vat));
			total = Float.parseFloat(decimalFormat.format(net + vatTotal));
		}
		labelAmount.setText(String.valueOf(amount));
		labelNet.setText(String.valueOf(net));
		labelVat.setText(String.valueOf(vat));
		labelTotal.setText(String.valueOf(total));
		labelDiscount.setText(String.valueOf(discount));
		tableViewParts.setItems(allParts);
		tableViewParts.refresh();
	}

	public void initializePreview(){
		calculateAmount();
		readyForPrint();
		textAreaComments.setDisable(true);
		boxButtons.getChildren().remove(buttonSave);
	}

	public void showPreview(){
		initializePreview();
		buttonPrint.setText("Print");
		stagePrint.showAndWait();
	}




}
