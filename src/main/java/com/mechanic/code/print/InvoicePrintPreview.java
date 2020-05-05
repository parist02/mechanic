package com.mechanic.code.print;
import com.mechanic.code.main.ErrorPopUp;
import com.mechanic.code.database.Part;
import com.mechanic.code.main.MainScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.print.Paper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class InvoicePrintPreview {
	private Stage stagePrint = new Stage();
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
	private VBox boxMain;

	public InvoicePrintPreview(Stage primaryStage, Integer customerID, String fullName, String licensePlates, String brandModel, String vin) {
		vat=MainScreen.getVat();
		//1st Part, import from database except date
		Label labelCompany1 = new Label("M.B.A LTD");
		labelCompany1.getStyleClass().add("printLabelBold");
		Label labelCompany2 = new Label("37 PRODROMOU\nSTR.2062 STROVOLOS");
		labelCompany2.getStyleClass().add("printLabelBold");
		Label labelCompany3 = new Label("Phone: 22424062");
		labelCompany3.getStyleClass().add("printLabelBold");
		Label labelCompany4 = new Label("Fax: 22317441");
		labelCompany4.getStyleClass().add("printLabelBold");
		Label labelCompany5 = new Label("VAT Reg.: 10091591L");
		labelCompany5.getStyleClass().add("printLabelBold");
		Label labelCompany6 = new Label("TAX REG.: 12091591N");
		labelCompany6.getStyleClass().add("printLabelBold");

		VBox boxCompany = new VBox(labelCompany1, labelCompany2, labelCompany3, labelCompany4, labelCompany5, labelCompany6);
		boxCompany.getStyleClass().add("printVBox");
		Label labelTitle = new Label("Cash Invoice");
		labelTitle.getStyleClass().add("printTitle");
		Label labelCompany7 = new Label("Invoice No.:");
		labelCompany7.getStyleClass().add("printLabelBold");
		labelInvoiceID = new Label("");//get from database
		labelInvoiceID.getStyleClass().add("printLabel2");
		Label labelCompany9 = new Label("Date:");
		labelCompany9.getStyleClass().add("printLabelBold");
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
		gridCompany = new GridPane();
		gridCompany.add(labelCompany7, 0, 0);
		gridCompany.add(labelInvoiceID, 1, 0);
		gridCompany.add(labelCompany9, 0, 1);
		gridCompany.add(datePickerInvoice, 1, 1);
		gridCompany.getStyleClass().add("printGrid");
		labelInvoiceDate=new Label("");
		labelInvoiceDate.getStyleClass().add("printLabel2");
		//2nd Part
		Label labelCustomer1 = new Label("Customer Account:");
		labelCustomer1.getStyleClass().add("printLabelBold");
		Label labelCustomerCustomerID = new Label(String.valueOf(customerID));
		labelCustomerCustomerID.getStyleClass().add("printLabel");
		Label labelCustomer2 = new Label("Full Name:");
		labelCustomer2.getStyleClass().add("printLabelBold");
		Label labelCustomerFullName = new Label(fullName);
		labelCustomerFullName.getStyleClass().add("printLabel");
		Label labelCustomer3 = new Label("Auto No.:");
		labelCustomer3.getStyleClass().add("printLabelBold");
		Label labelCustomerLicencePlates = new Label(licensePlates);
		labelCustomerLicencePlates.getStyleClass().add("printLabel");
		Label labelCustomer4 = new Label("Auto Make.:");
		labelCustomer4.getStyleClass().add("printLabelBold");
		Label labelCustomerBrandModel = new Label(brandModel);
		labelCustomerBrandModel.getStyleClass().add("printLabel");
		Label labelCustomer5 = new Label("Chassis No.");
		labelCustomer5.getStyleClass().add("printLabelBold");
		Label labelCustomerVin = new Label(vin);
		labelCustomerVin.getStyleClass().add("printLabel");

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
		gridCustomer1.getStyleClass().add("printGrid");

		Label labelCustomer6 = new Label("Date In:");
		labelCustomer6.getStyleClass().add("printLabelBold");
		datePickerIN = new DatePicker();
		datePickerIN.setConverter(stringConverter);
		Label labelCustomer7 = new Label("Date Out:");
		labelCustomer7.getStyleClass().add("printLabelBold");
		datePickerOUT = new DatePicker();
		datePickerOUT.setConverter(stringConverter);

		gridCustomer2 = new GridPane();
		gridCustomer2.add(labelCustomer6, 0, 0);
		gridCustomer2.add(datePickerIN, 1, 0);
		gridCustomer2.add(labelCustomer7, 0, 1);
		gridCustomer2.add(datePickerOUT, 1, 1);
		gridCustomer2.getStyleClass().add("printGrid");

		labelInvoiceDateIN=new Label("");
		labelInvoiceDateIN.getStyleClass().add("printLabel2");
		labelInvoiceDateOUT=new Label("");
		labelInvoiceDateOUT.getStyleClass().add("printLabel2");

		Label labelCustomer8 = new Label("Mileage:");
		labelCustomer8.getStyleClass().add("printLabelBold");
		textFieldMileage = new TextField();
		Label labelCustomer9 = new Label("First Oil:");
		labelCustomer9.getStyleClass().add("printLabelBold");
		textFieldFirstOil = new TextField();
		Label labelCustomer10 = new Label("Next Oil:");
		labelCustomer10.getStyleClass().add("printLabelBold");
		textFieldNextOil = new TextField();
		Label labelCustomer11 = new Label("Next Service:");
		labelCustomer11.getStyleClass().add("printLabelBold");
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
		gridCustomer3.getStyleClass().add("printGrid");

		labelMileage=new Label("");
		labelMileage.getStyleClass().add("printLabel2");
		labelFirstOil=new Label("");
		labelFirstOil.getStyleClass().add("printLabel2");
		labelNextOil=new Label("");
		labelNextOil.getStyleClass().add("printLabel2");
		labelNextService=new Label("");
		labelNextService.getStyleClass().add("printLabel2");


		//3rd part
		TableColumn<Part, Integer> counterColumn = new TableColumn<>("No.");
		counterColumn.setReorderable(false);
		counterColumn.setCellValueFactory(new PropertyValueFactory<>("counter"));

		TableColumn<Part, String> partsIDColumn = new TableColumn<>("Parts ID");
		partsIDColumn.setReorderable(false);
		partsIDColumn.setCellValueFactory(new PropertyValueFactory<>("partsID"));

		TableColumn<Part, String> descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setReorderable(false);

		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

		TableColumn<Part, Integer> quantityColumn = new TableColumn<>("Quantity");
		quantityColumn.setReorderable(false);
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<Part, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setReorderable(false);
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
		tableViewParts.setMaxHeight(385);
		tableViewParts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableViewParts.setEditable(false);
		tableViewParts.setItems(allParts);
		tableViewParts.setPadding(new Insets(0));
		

		TextField textFieldPartsID = new TextField();
		textFieldPartsID.setPromptText("Parts ID");
		TextField textFieldDescription = new TextField();
		textFieldDescription.setPromptText("Description");
		TextField textFieldQuantity = new TextField();
		textFieldQuantity.setPromptText("Quantity");
		TextField textFieldPrice = new TextField();
		textFieldPrice.setPromptText("Price");
		Label labelAddPart = new Label("Add new Part:");
		Button buttonAddPart = new Button("Add");
		buttonAddPart.getStyleClass().add("buttonMain");
		HBox hBoxAddPart = new HBox(labelAddPart, textFieldPartsID, textFieldDescription, textFieldQuantity, textFieldPrice, buttonAddPart);
		vBoxParts = new VBox(tableViewParts, hBoxAddPart);

		//4th part
		Label labelComments = new Label("Comments:");
		labelComments.getStyleClass().add("printLabelBold");
		textAreaComments = new TextArea();
		textAreaComments.setPrefHeight(75);
		textAreaComments.setPrefWidth(2*width/3-10);
		Label labelSignature = new Label("\tIssued By");
		labelSignature.getStyleClass().add("printLabelBold");
		Label labelDots1 = new Label("...............................................");
		labelDots1.getStyleClass().add("printLabelBold");
		VBox boxComments = new VBox(labelComments, textAreaComments);

		VBox boxSignature=new VBox(labelSignature,labelDots1);
		boxSignature.setSpacing(8);

		Label labelTotal1 = new Label("Amount:");
		labelTotal1.getStyleClass().add("printLabelBold");
		labelAmount = new Label("");
		labelAmount.getStyleClass().add("printLabel2");
		Label labelTotal2 = new Label("Discount:");
		labelTotal2.getStyleClass().add("printLabelBold");
		labelDiscount = new Label("");
		labelDiscount.getStyleClass().add("printLabel2");
		textFieldDiscount=new TextField("0");
		Label labelTotal3 = new Label("Net:");
		labelTotal3.getStyleClass().add("printLabelBold");
		labelNet = new Label("");
		labelNet.getStyleClass().add("printLabel2");
		Label labelTotal4 = new Label("Vat:");
		labelTotal4.getStyleClass().add("printLabelBold");
		labelVat = new Label("");
		labelVat.getStyleClass().add("printLabel2");
		Label labelTotal5 = new Label("GTotal:");
		labelTotal5.getStyleClass().add("printLabelBold");
		labelTotal = new Label("");
		labelTotal.getStyleClass().add("printLabel2");
		Label labelTotal6 = new Label("\tReceived By");
		labelTotal6.getStyleClass().add("printLabelBold");
		Label labelDots2 = new Label("...............................................");
		labelDots2.getStyleClass().add("printLabelBold");


		VBox boxSignature2=new VBox(labelTotal6,labelDots2);
		boxSignature2.setSpacing(4);

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
		gridTotal.getStyleClass().add("printGrid");


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
		grid.add(boxSignature,0,4,2,1);
		grid.add(boxSignature2,2,4,1,1);
		grid.getStyleClass().add("formGrid");

		buttonPrint = new Button("Save and Show Print Preview");
		buttonPrint.getStyleClass().add("buttonMain");
		buttonSave = new Button("Save and Close");
		buttonSave.getStyleClass().add("buttonCancel");

		boxButtons = new HBox(buttonSave,buttonPrint);
		boxButtons.getStyleClass().add("buttonForm");
		grid.setMinWidth(width);
		grid.setMinHeight(height);
		boxMain = new VBox(grid, boxButtons);
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
			String partID = textFieldPartsID.getText();
			partID=partID.replace(";","");
			partID=partID.replace("`","");
			partID=partID.replace("'","");
			if (partID.equals("")) {
				error = true;
			}
			String description = textFieldDescription.getText();
			description=description.replace(";","");
			description=description.replace("`","");
			description=description.replace("'","");
			if (description.equals("")) {
				textFieldDescription.setText(description);
				error = true;
			}
			try {
				quantity = Integer.parseInt(textFieldQuantity.getText().replaceAll("[^0-9]", ""));
				price = Float.parseFloat(textFieldPrice.getText().replaceAll("[^0-9.]", ""));
				discount=Float.parseFloat(textFieldDiscount.getText().replaceAll("[^0-9 .]", ""));
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
				comments=comments.replace(";","");
				comments=comments.replace("`","");
				comments=comments.replace("'","");
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
				comments=comments.replace(";","");
				comments=comments.replace("`","");
				comments=comments.replace("'","");
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
		return total;
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

	public GridPane getNodeForPrint(){
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
		labelVat.setText(String.valueOf(vatTotal));
		labelTotal.setText(String.valueOf(total));
		labelDiscount.setText(String.valueOf(discount));
		tableViewParts.setItems(allParts);
		tableViewParts.refresh();
	}

	private void initializePreview(){
		calculateAmount();
		readyForPrint();
		buttonPrint.setText("Print");
		boxButtons.getChildren().remove(buttonSave);
		boxMain.getChildren().remove(boxButtons); //needs to be removed and added again because it was causing problems at the table(vertical scrolling bar)
		boxMain.getChildren().add(boxButtons);
		grid.setPrefWidth(width);
		grid.setPrefHeight(height);

	}

	public void showPreview(){
		initializePreview();
		stagePrint.showAndWait();
	}




}
