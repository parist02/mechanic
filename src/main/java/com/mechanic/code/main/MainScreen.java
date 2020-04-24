package com.mechanic.code.main;
import com.mechanic.code.forms.*;
import com.mechanic.code.print.Print;
import com.mechanic.code.database.*;
import com.mechanic.code.print.InvoicePrintPreview;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class MainScreen extends Application {
    private TableView<Customer> tableViewCustomers = new TableView<>();
    private TableView<Car> tableViewCars = new TableView<>();
    private TableView<Invoice> tableViewInvoice = new TableView<>();
    private TableView<Mechanic>tableViewMechanic=new TableView<>();
    private Connection connection;
    private Stage primaryStage;
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private ObservableList<Car> allCars = FXCollections.observableArrayList();
    private ObservableList<Mechanic> allMechanics = FXCollections.observableArrayList();
    private ObservableList<Repair> allRepairs = FXCollections.observableArrayList();
    private ObservableList<Invoice> allInvoices = FXCollections.observableArrayList();
    private ObservableList<InvoiceMetaData> allInvoicesMetaData = FXCollections.observableArrayList();
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ErrorPopUp errorPopUp0;
    private ErrorPopUp errorPopUp1;
    private ErrorPopUp errorPopUp2;
    private Print print;
    private FilteredList<Customer> filteredListCustomers;
    private FilteredList<Car> filteredListCars;
    private FilteredList<Mechanic> filteredMechanics;
    private FilteredList<Repair> filteredRepairs;
    private FilteredList<Invoice> filteredInvoices;
    private FilteredList<Invoice> twiceFilteredInvoices;
    private FilteredList<InvoiceMetaData> filteredInvoicesMetaData;
    private FilteredList<Part> filteredParts;
    private boolean carsFiltered = false;
    private boolean customersFiltered = false;
    private boolean invoiceFiltered = false;
    private boolean invoiceFilteredTwice = false;
    private static Float vat;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    public void init() throws Exception {
        super.init();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mechanic", "root", "");
        } catch (Exception e) {
            System.exit(0);

        }
    }

    @Override
    public void start(Stage stage) {
        allMechanics=importFromMechanics();
        allRepairs=importFromRepairs();
        vat = importVat();
        allCustomers = importFromCustomers();
        allCars = importFromCars();
        allInvoices = importFromInvoices();
        allInvoicesMetaData = importFromInvoiceMetaData();
        allParts = importFromInvoiceParts();


        TabPane tabPane = new TabPane();

        //Tab 1
        TableColumn<Customer, Integer> counterColumn = new TableColumn<>("No.");
        counterColumn.setReorderable(false);
        counterColumn.setCellValueFactory(new PropertyValueFactory<>("counter"));

        TableColumn<Customer, Integer> nameColumn = new TableColumn<>("Name");
        nameColumn.setReorderable(false);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, Integer> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setReorderable(false);
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<Customer, Integer> phone1Column = new TableColumn<>("Phone1");
        phone1Column.setReorderable(false);
        phone1Column.setCellValueFactory(new PropertyValueFactory<>("phone1"));

        TableColumn<Customer, Integer> phone2Column = new TableColumn<>("Phone2");
        phone2Column.setReorderable(false);
        phone2Column.setCellValueFactory(new PropertyValueFactory<>("phone2"));

        TableColumn<Customer, Integer> addressColumn = new TableColumn<>("Address");
        addressColumn.setReorderable(false);
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Customer, Float> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setReorderable(false);
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        tableViewCustomers.getColumns().add(counterColumn);
        tableViewCustomers.getColumns().add(nameColumn);
        tableViewCustomers.getColumns().add(surnameColumn);
        tableViewCustomers.getColumns().add(phone1Column);
        tableViewCustomers.getColumns().add(phone2Column);
        tableViewCustomers.getColumns().add(addressColumn);
        tableViewCustomers.getColumns().add(balanceColumn);
        tableViewCustomers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewCustomers.setEditable(false);
        tableViewCustomers.setItems(allCustomers);
        tableViewCustomers.getStyleClass().add("tableViewCustomers");

        //Clicking table of customers changes cars also
        tableViewCustomers.setOnMouseClicked(mouseEvent -> {
            if (!tableViewCustomers.getSelectionModel().isEmpty()) {
                filteredListCars = new FilteredList<>(allCars.filtered(cars -> cars.getCustomerId() == tableViewCustomers.getSelectionModel().getSelectedItem().getCounter()));
                carsFiltered = true;
                tableViewCars.setItems(filteredListCars);
            }
        });

        Label labelCustomerTitle = new Label("Customers");
        labelCustomerTitle.getStyleClass().add("formTitle");
        Button buttonDeleteCustomers = new Button("Delete");
        buttonDeleteCustomers.getStyleClass().add("buttonCancel");
        //deleting a customer
        buttonDeleteCustomers.setOnAction(del -> {
            if (tableViewCustomers.getSelectionModel().isEmpty()) {
                errorPopUp0.setErrorMessage("Select a customer first.");
                errorPopUp0.showError();
            } else {
                errorPopUp2.setErrorMessage("Are you sure you want to delete the customer?");
                errorPopUp2.showError();
                if (errorPopUp2.isAdded()) {
                    deleteFromCustomers();
                }
            }
        });

        Button buttonBalanceInvoices = new Button("Change Balance");
        buttonBalanceInvoices.getStyleClass().add("buttonCancel");
        //buttonBalance moved down
        Button buttonCustomerRecord = new Button("Show Record");
        buttonCustomerRecord.getStyleClass().add("buttonCancel");
        buttonCustomerRecord.setOnAction(actionEvent -> {
            if (!tableViewCustomers.getSelectionModel().isEmpty()) {
                filterInvoices(5, String.valueOf(tableViewCustomers.getSelectionModel().getSelectedItem().getCounter()), null);
                tabPane.getSelectionModel().select(1);
            }
        });





        Button buttonUpdateCustomers = new Button("Edit");
        buttonUpdateCustomers.getStyleClass().add("buttonCancel");
        buttonUpdateCustomers.setOnAction(upd -> updateFromCustomers());
        Button buttonAddCustomer = new Button("Add");
        buttonAddCustomer.getStyleClass().add("buttonCancel");
        buttonAddCustomer.setOnAction(add -> addToCustomers(null));
        TextField textFieldSearchCustomers = new TextField();
        Button buttonSearchCustomers = new Button("\uD83D\uDD0D Search");
        buttonSearchCustomers.getStyleClass().add("buttonCancel");
        Button buttonSearchClearCustomers = new Button("Clear");
        buttonSearchClearCustomers.getStyleClass().add("buttonCancel");

        TableColumn<Car, String> licencePlatesColumn = new TableColumn<>("License Plates");
        licencePlatesColumn.setReorderable(false);
        licencePlatesColumn.setCellValueFactory(new PropertyValueFactory<>("licencePlates"));

        TableColumn<Car, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setReorderable(false);
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Car, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setReorderable(false);
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Car, String> vinColumn = new TableColumn<>("VIN");
        vinColumn.setReorderable(false);
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));

        TableColumn<Car, Date> dateCarColumn = new TableColumn<>("Date");
        dateCarColumn.setReorderable(false);
        dateCarColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Car, Integer> customerIdColumn = new TableColumn<>("CustomerID");
        customerIdColumn.setReorderable(false);
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));


        tableViewCars.getColumns().add(licencePlatesColumn);
        tableViewCars.getColumns().add(brandColumn);
        tableViewCars.getColumns().add(modelColumn);
        tableViewCars.getColumns().add(vinColumn);
        tableViewCars.getColumns().add(dateCarColumn);
        tableViewCars.getColumns().add(customerIdColumn);
        tableViewCars.setEditable(false);
        tableViewCars.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewCars.setItems(allCars);
        tableViewCars.getStyleClass().add("tableViewCars");

        //Clicking table of cars changes customers also
        tableViewCars.setOnMouseClicked(mouseEvent -> {
            if (!tableViewCars.getSelectionModel().isEmpty()) {
                filteredListCustomers = new FilteredList<>(allCustomers.filtered(customers -> customers.getCounter() == tableViewCars.getSelectionModel().getSelectedItem().getCustomerId()));
                customersFiltered = true;
                tableViewCustomers.setItems(filteredListCustomers);
            }
        });

        Button buttonUpdateCars = new Button("Edit");
        buttonUpdateCars.getStyleClass().add("buttonCancel");
        buttonUpdateCars.setOnAction(upd -> updateFromCars());
        Button buttonAddCars = new Button("Add");
        buttonAddCars.getStyleClass().add("buttonCancel");
        buttonAddCars.setOnAction(add -> addToCars(null));
        Button buttonDeleteCars = new Button("Delete");
        buttonDeleteCars.getStyleClass().add("buttonCancel");
        buttonDeleteCars.setOnAction(del -> {
            if (tableViewCars.getSelectionModel().isEmpty()) {
                errorPopUp0.setErrorMessage("Select a car first.");
                errorPopUp0.showError();
            } else {
                errorPopUp2.setErrorMessage("Are you sure you want to delete the car?");
                errorPopUp2.showError();
                if (errorPopUp2.isAdded()) {
                    deleteFromCars();
                }
            }
        });
        Button buttonCarRecord = new Button("Show Record");
        buttonCarRecord.getStyleClass().add("buttonCancel");
        buttonCarRecord.setOnAction(actionEvent -> {
            if (!tableViewCars.getSelectionModel().isEmpty()) {
                filterInvoices(0, tableViewCars.getSelectionModel().getSelectedItem().getLicencePlates(), null);
                tabPane.getSelectionModel().select(1);
            }
        });

        ChoiceBox<String> choiceBoxSearchTab1 = new ChoiceBox<>();
        choiceBoxSearchTab1.getItems().add("Phone");
        choiceBoxSearchTab1.getItems().add("License Plates");
        choiceBoxSearchTab1.getSelectionModel().selectFirst();
        //searching tables
        buttonSearchCustomers.setOnAction(ser -> {
            if (!textFieldSearchCustomers.getText().equals("")) {
                if (choiceBoxSearchTab1.getValue().equals("Phone")) { //check if the user wants phone or license plates
                    textFieldSearchCustomers.setText(textFieldSearchCustomers.getText().replaceAll("[^0-9]", "")); //deletes all special characters from search bar
                    if (textFieldSearchCustomers.getText().equals("")) { //checks for empty search
                        errorPopUp0.setErrorMessage("Not a valid entry!");
                        errorPopUp0.showError();
                    } else {
                        int searchPhone = Integer.parseInt(textFieldSearchCustomers.getText());
                        filteredListCustomers = new FilteredList<>(allCustomers.filtered(customers -> (customers.getPhone1() == searchPhone || customers.getPhone2() == searchPhone)));
                        if (filteredListCustomers.isEmpty()) { //if nothing found
                            errorPopUp1.setErrorMessage("Customer with phone number " + searchPhone + " is not found. Do you want to add him?");
                            errorPopUp1.showError();
                            if (errorPopUp1.isAdded()) {
                                addToCustomers(String.valueOf(searchPhone));
                            }
                        } else {
                            customersFiltered = true;
                            carsFiltered = true;
                            tableViewCustomers.setItems(filteredListCustomers);
                            int customerIDSearched = tableViewCustomers.getItems().get(0).getCounter();
                            filteredListCars = new FilteredList<>(allCars.filtered(cars -> cars.getCustomerId() == customerIDSearched));
                            tableViewCars.setItems(filteredListCars);
                        }
                    }
                } else if (choiceBoxSearchTab1.getValue().equals("License Plates")) {
                    textFieldSearchCustomers.setText(textFieldSearchCustomers.getText().replaceAll("[^a-zA-Z0-9]", ""));
                    if (textFieldSearchCustomers.getText().equals("")) {
                        errorPopUp0.setErrorMessage("Not a valid entry!");
                        errorPopUp0.showError();
                    } else {
                        String searchLicensePlates = textFieldSearchCustomers.getText();
                        filteredListCars = new FilteredList<>(allCars.filtered(cars -> cars.getLicencePlates().equals(searchLicensePlates)));
                        if (filteredListCars.isEmpty()) {
                            errorPopUp1.setErrorMessage("Car with license plates " + searchLicensePlates + " is not found. Do you want to add it?");
                            errorPopUp1.showError();
                            if (errorPopUp1.isAdded()) {
                                addToCars(textFieldSearchCustomers.getText().replaceAll("[^a-zA-Z0-9]", ""));
                            }
                        } else {
                            customersFiltered = true;
                            carsFiltered = true;
                            tableViewCars.setItems(filteredListCars);
                            int customerIDSearched = tableViewCars.getItems().get(0).getCustomerId();
                            filteredListCustomers = new FilteredList<>(allCustomers.filtered(customers -> customers.getCounter() == customerIDSearched));
                            tableViewCustomers.setItems(filteredListCustomers);
                        }
                    }
                }
            } else {
                errorPopUp0.setErrorMessage("Search bar is empty.");
                errorPopUp0.showError();
            }
        });

        //Clearing selection and searches
        buttonSearchClearCustomers.setOnAction(cle -> {
            customersFiltered = false;
            carsFiltered = false;
            tableViewCustomers.setItems(allCustomers);
            textFieldSearchCustomers.setText("");
            tableViewCars.setItems(allCars);
            tableViewCustomers.getSelectionModel().clearSelection();
            tableViewCars.getSelectionModel().clearSelection();
            tableViewCustomers.refresh();
            tableViewCars.refresh();
        });

        Label labelCarsTitle=new Label("Cars");
        labelCarsTitle.getStyleClass().add("formTitle");
        HBox boxButtonsCustomersTop=new HBox(choiceBoxSearchTab1, textFieldSearchCustomers, buttonSearchCustomers, buttonSearchClearCustomers);
        boxButtonsCustomersTop.getStyleClass().add("buttonForm");
        HBox boxButtonsCustomersBottom = new HBox(buttonAddCustomer, buttonUpdateCustomers, buttonDeleteCustomers,buttonCustomerRecord,buttonBalanceInvoices);
        boxButtonsCustomersBottom.getStyleClass().add("buttonForm");
        HBox boxButtonsCars = new HBox(buttonAddCars, buttonUpdateCars, buttonDeleteCars, buttonCarRecord);
        boxButtonsCars.getStyleClass().add("buttonForm");
        VBox boxCustomers = new VBox(boxButtonsCustomersTop,labelCustomerTitle, tableViewCustomers, boxButtonsCustomersBottom,labelCarsTitle, tableViewCars,boxButtonsCars);
        boxCustomers.getStyleClass().add("boxTabs");

        //Tab2

        TableColumn<Invoice, Integer> invoiceIDColumn = new TableColumn<>("Invoice No.");
        invoiceIDColumn.setReorderable(false);
        invoiceIDColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));

        TableColumn<Invoice, Date> dateInvoiceColumn = new TableColumn<>("Date");
        dateInvoiceColumn.setReorderable(false);
        dateInvoiceColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Invoice, Integer> customerIDInvoiceColumn = new TableColumn<>("CustomerID");
        customerIDInvoiceColumn.setReorderable(false);
        customerIDInvoiceColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));


        TableColumn<Invoice, String> fullNameColumn = new TableColumn<>("Full Name");
        fullNameColumn.setReorderable(false);
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Invoice, Integer> phoneInvoiceColumn = new TableColumn<>("Phone");
        phoneInvoiceColumn.setReorderable(false);
        phoneInvoiceColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));


        TableColumn<Invoice, String> licencePlatesInvoiceColumn = new TableColumn<>("License Plates");
        licencePlatesInvoiceColumn.setReorderable(false);
        licencePlatesInvoiceColumn.setCellValueFactory(new PropertyValueFactory<>("licencePlates"));

        TableColumn<Invoice, String> mechanicNameColumn = new TableColumn<>("Mechanic");
        mechanicNameColumn.setReorderable(false);
        mechanicNameColumn.setCellValueFactory(new PropertyValueFactory<>("mechanicName"));

        TableColumn<Invoice, String> repairTypeColumn = new TableColumn<>("Repair");
        repairTypeColumn.setReorderable(false);
        repairTypeColumn.setCellValueFactory(new PropertyValueFactory<>("repairType"));

        TableColumn<Invoice, Float> balanceInvoiceColumn = new TableColumn<>("Balance");
        balanceInvoiceColumn.setReorderable(false);
        balanceInvoiceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        tableViewInvoice.getColumns().add(invoiceIDColumn);
        tableViewInvoice.getColumns().add(dateInvoiceColumn);
        tableViewInvoice.getColumns().add(customerIDInvoiceColumn);
        tableViewInvoice.getColumns().add(fullNameColumn);
        tableViewInvoice.getColumns().add(phoneInvoiceColumn);
        tableViewInvoice.getColumns().add(licencePlatesInvoiceColumn);
        tableViewInvoice.getColumns().add(mechanicNameColumn);
        tableViewInvoice.getColumns().add(repairTypeColumn);
        tableViewInvoice.getColumns().add(balanceInvoiceColumn);
        tableViewInvoice.setItems(allInvoices);
        tableViewInvoice.setEditable(false);
        tableViewInvoice.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewInvoice.getStyleClass().add("tableViewInvoice");


        Button buttonNewInvoice = new Button("Create invoice");
        buttonNewInvoice.getStyleClass().add("buttonCancel");
        buttonNewInvoice.setOnAction(actionEvent -> {
            InvoiceForm invoiceForm = new InvoiceForm(primaryStage, connection, allMechanics, allRepairs, allCustomers, allCars);
            invoiceForm.showForm();
            if (invoiceForm.isClickedOK()) {
                InvoicePrintPreview invoicePrintPreview = new InvoicePrintPreview(primaryStage, invoiceForm.getCustomerID(), invoiceForm.getFullName(), invoiceForm.getLicensePlates(), invoiceForm.getBrandModel(), invoiceForm.getVin());
                invoicePrintPreview.show();
                if (invoicePrintPreview.isBeingPrinted() && invoicePrintPreview.isBeingSaved()) {
                    //print needs to be implemented
                    Invoice invoice = addToInvoice(invoiceForm, invoicePrintPreview);
                    openInvoice(invoice);
                } else if (invoicePrintPreview.isBeingSaved()) {
                    addToInvoice(invoiceForm, invoicePrintPreview);
                }
            }
        });

        Button buttonOpenInvoice = new Button("Open");
        buttonOpenInvoice.getStyleClass().add("buttonCancel");
        buttonOpenInvoice.setOnAction(actionEvent -> {
            if (!tableViewInvoice.getSelectionModel().isEmpty()) {
                openInvoice(tableViewInvoice.getSelectionModel().getSelectedItem());
            } else {
                errorPopUp0.setErrorMessage("Select an invoice first!");
                errorPopUp0.showError();
            }
        });

        Button buttonEditBalance = new Button("Edit Balance");
        buttonEditBalance.getStyleClass().add("buttonCancel");
        buttonEditBalance.setOnAction(actionEvent -> {
            if (!tableViewInvoice.getSelectionModel().isEmpty()) {
                editBalance();
            } else {
                errorPopUp0.setErrorMessage("Select an invoice first!");
                errorPopUp0.showError();
            }
        });


        ChoiceBox<String> choiceBoxFilterInvoices = new ChoiceBox<>();
        choiceBoxFilterInvoices.getItems().add("No filter");
        for (Repair r : allRepairs) {
            choiceBoxFilterInvoices.getItems().add(r.getName());
        }
        choiceBoxFilterInvoices.getSelectionModel().select(0);
        choiceBoxFilterInvoices.setOnAction(actionEvent -> {
            if (choiceBoxFilterInvoices.getSelectionModel().getSelectedIndex() != 0) {
                if (invoiceFiltered && invoiceFilteredTwice) {
                    filterInvoices(3, null, choiceBoxFilterInvoices.getSelectionModel().getSelectedItem());
                } else if (invoiceFiltered) {
                    filterInvoices(2, null, choiceBoxFilterInvoices.getSelectionModel().getSelectedItem());
                } else {
                    filterInvoices(1, null, choiceBoxFilterInvoices.getSelectionModel().getSelectedItem());
                }
            } else {
                if (!invoiceFiltered && !invoiceFilteredTwice) {
                    tableViewInvoice.setItems(allInvoices);
                }
                if (invoiceFilteredTwice) {
                    tableViewInvoice.setItems(twiceFilteredInvoices);
                }
                tableViewInvoice.refresh();
            }
        });
        Button buttonClearInvoices = new Button("Clear");
        buttonClearInvoices.getStyleClass().add("buttonCancel");
        buttonClearInvoices.setOnAction(actionEvent -> {
            invoiceFiltered = false;
            invoiceFilteredTwice = false;
            tableViewInvoice.setItems(allInvoices);
            tableViewInvoice.getSelectionModel().clearSelection();
            choiceBoxFilterInvoices.getSelectionModel().select(0);
            tableViewInvoice.refresh();
        });
        //button from tab1
        buttonBalanceInvoices.setOnAction(actionEvent -> {
            if (!tableViewCustomers.getSelectionModel().isEmpty()) {
                if(tableViewCustomers.getSelectionModel().getSelectedItem().getBalance()!=0) {
                    filterInvoices(4, String.valueOf(tableViewCustomers.getSelectionModel().getSelectedItem().getCounter()), null);
                    tabPane.getSelectionModel().select(1);
                    if(tableViewInvoice.getItems().size()==1) {
                        tableViewInvoice.getSelectionModel().select(0);
                        buttonEditBalance.fire();
                    }
                }

            }
        });

        Label labelTitleInvoice=new Label("Invoices");
        labelTitleInvoice.getStyleClass().add("formTitle");
        HBox boxButtonsInvoices = new HBox(choiceBoxFilterInvoices, buttonClearInvoices);
        boxButtonsInvoices.getStyleClass().add("buttonForm");
        HBox boxButtonInvoicesBottom=new HBox(buttonNewInvoice,buttonOpenInvoice,buttonEditBalance);
        boxButtonInvoicesBottom.getStyleClass().add("buttonForm");
        VBox boxTab2 = new VBox(boxButtonsInvoices, labelTitleInvoice,tableViewInvoice,boxButtonInvoicesBottom);
        boxTab2.getStyleClass().add("boxTabs");

        //Tab 3
        TableColumn<Mechanic, Integer> mechanicIDColumn = new TableColumn<>("Mechanic ID");
        mechanicIDColumn.setReorderable(false);
        mechanicIDColumn.setCellValueFactory(new PropertyValueFactory<>("mechanicID"));

        TableColumn<Mechanic, Integer> mechanicNameColumn2 = new TableColumn<>("Name");
        mechanicNameColumn2.setReorderable(false);
        mechanicNameColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Mechanic, Integer> mechanicSurname = new TableColumn<>("Surname");
        mechanicSurname.setReorderable(false);
        mechanicSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));

        tableViewMechanic.getColumns().add(mechanicIDColumn);
        tableViewMechanic.getColumns().add(mechanicNameColumn2);
        tableViewMechanic.getColumns().add(mechanicSurname);
        tableViewMechanic.setEditable(false);
        tableViewMechanic.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewMechanic.setItems(allMechanics);
        tableViewMechanic.getStyleClass().add("tableViewMechanics");


        Button buttonUpdateMechanics = new Button("Edit");
        buttonUpdateMechanics.getStyleClass().add("buttonCancel");
        buttonUpdateMechanics.setOnAction(upd -> updateFromMechanics());
        Button buttonAddMechanic = new Button("Add");
        buttonAddMechanic.getStyleClass().add("buttonCancel");
        buttonAddMechanic.setOnAction(add -> addToMechanics());
        Button buttonDeleteMechanic=new Button("Delete");
        buttonDeleteMechanic.getStyleClass().add("buttonCancel");
        buttonDeleteMechanic.setOnAction(actionEvent -> {
            if (tableViewMechanic.getSelectionModel().isEmpty()) {
                errorPopUp0.setErrorMessage("Select a customer first.");
                errorPopUp0.showError();
            } else {
                errorPopUp2.setErrorMessage("Are you sure you want to delete the mechanic?");
                errorPopUp2.showError();
                if (errorPopUp2.isAdded()) {
                    deleteFromMechanics();
                }
            }
        });

        Label labelVAT=new Label("Current VAT: "+vat);
        labelVAT.getStyleClass().add("formLabel");
        Button buttonEditVat=new Button("Edit VAT");
        buttonEditVat.getStyleClass().add("buttonMain");
        buttonEditVat.setOnAction(actionEvent -> {
            VatForm vatForm=new VatForm(primaryStage,vat);
            vatForm.showForm();
            if(vatForm.isChanged()){
                updateVat(vatForm.getVat());
                vat=vatForm.getVat();
                labelVAT.setText("Current VAT: "+(vatForm.getVat()));
            }
        });
        Label labelTitleMechanic=new Label("Mechanics");
        labelTitleMechanic.getStyleClass().add("formTitle");
        HBox boxVat=new HBox(labelVAT,buttonEditVat);
        boxVat.getStyleClass().add("buttonForm2");
        HBox boxButtonsMechanics=new HBox(buttonAddMechanic,buttonUpdateMechanics,buttonDeleteMechanic);
        boxButtonsMechanics.getStyleClass().add("buttonForm");
        VBox boxTab3=new VBox(labelTitleMechanic,tableViewMechanic,boxButtonsMechanics,boxVat);


        //
        Tab tab1 = new Tab("Customers", boxCustomers);
        tab1.getStyleClass().add("tabs");
        Tab tab2 = new Tab("Invoice", boxTab2);
        tab2.getStyleClass().add("tabs");
        Tab tab3 = new Tab("Details", boxTab3);
        tab3.getStyleClass().add("tabs");

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.FIXED);


        Scene scene = new Scene(tabPane);
        scene.getStylesheets().add("stylesheets.css");
        primaryStage = new Stage();
        primaryStage = stage;
        primaryStage.setMaximized(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MainScreen");
        errorPopUp0 = new ErrorPopUp(0, primaryStage);
        errorPopUp1 = new ErrorPopUp(1, primaryStage);
        errorPopUp2 = new ErrorPopUp(2, primaryStage);
        print=new Print(primaryStage);
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        connection.close();

    }


    private ObservableList<Customer> importFromCustomers() {
        ObservableList<Customer> importingCustomers = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customers");
            Customer customer;
            while (rs.next()) {
                customer = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7));
                importingCustomers.add(customer);
            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with getting data from Customers");
            errorPopUp0.showError();
        }
        return importingCustomers;
    }

    private ObservableList<Car> importFromCars() {
        ObservableList<Car> importingCars = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from cars");
            Car cars;
            while (rs.next()) {
                cars = new Car(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                importingCars.add(cars);

            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with getting data from Cars");
            errorPopUp0.showError();
        }
        return importingCars;
    }

    private ObservableList<Mechanic> importFromMechanics() {
        ObservableList<Mechanic>importingMechanics=FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from mechanics");
            Mechanic mechanic;
            while (rs.next()) {
                mechanic = new Mechanic(rs.getInt(1), rs.getString(2), rs.getString(3));
                importingMechanics.add(mechanic);
            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with getting data from Mechanics");
            errorPopUp0.showError();
        }
        return importingMechanics;
    }


    private ObservableList<Repair> importFromRepairs() {
        ObservableList<Repair>importingRepairs=FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from repairs");
            Repair repair;
            while (rs.next()) {
                repair = new Repair(rs.getInt(1), rs.getString(2));
                importingRepairs.add(repair);
            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with getting data from Database");
            errorPopUp0.showError();
        }
        return importingRepairs;
    }

    private ObservableList<Invoice> importFromInvoices() {
        ObservableList<Invoice> importingInvoices = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from invoice");
            Invoice invoice;
            while (rs.next()) {
                invoice = new Invoice();
                invoice.setInvoiceID(rs.getInt(1));
                invoice.setDate(rs.getDate(2).toLocalDate());
                int arithmosPelati = rs.getInt(3);
                invoice.setCustomerID(arithmosPelati);
                filteredListCustomers = new FilteredList<>(allCustomers.filtered(customer -> customer.getCounter() == arithmosPelati));
                invoice.setFullName(filteredListCustomers.get(0).getName() + " " + filteredListCustomers.get(0).getSurname());
                invoice.setPhone(filteredListCustomers.get(0).getPhone1());
                invoice.setLicensePlates(rs.getString(4));
                int arithmosRepair = rs.getInt(5);
                filteredRepairs = new FilteredList<>(allRepairs.filtered(repair -> repair.getRepairID() == arithmosRepair));
                invoice.setRepairType(filteredRepairs.get(0).getName());
                int arithmosMixanikou = rs.getInt(6);
                filteredMechanics = new FilteredList<>(allMechanics.filtered(mechanic -> mechanic.getMechanicID() == arithmosMixanikou));
                invoice.setMechanicName(filteredMechanics.get(0).getName() + " " + filteredMechanics.get(0).getSurname());
                invoice.setBalance(rs.getFloat(8));
                importingInvoices.add(invoice);
            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with getting data from Invoices");
            errorPopUp0.showError();
        }
        return importingInvoices;
    }

    private ObservableList<InvoiceMetaData> importFromInvoiceMetaData() {
        ObservableList<InvoiceMetaData>importingInvoiceMetaData=FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from invoicemetadata");
            InvoiceMetaData invoiceMetaData;
            while (rs.next()) {
                invoiceMetaData = new InvoiceMetaData(rs.getFloat(10));
                invoiceMetaData.setInvoiceId(rs.getInt(1));
                invoiceMetaData.setDateIn(LocalDate.parse(rs.getString(2)));
                invoiceMetaData.setDateOut(LocalDate.parse(rs.getString(3)));
                invoiceMetaData.setMileage(rs.getInt(4));
                invoiceMetaData.setFirstOil(rs.getInt(5));
                invoiceMetaData.setNextOil(rs.getInt(6));
                invoiceMetaData.setNextService(rs.getInt(7));
                invoiceMetaData.setComments(rs.getString(8));
                invoiceMetaData.setDiscount(rs.getFloat(9));
                importingInvoiceMetaData.add(invoiceMetaData);

            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with getting data from InvoiceMetaData");
            errorPopUp0.showError();
        }
        return importingInvoiceMetaData;
    }

    private ObservableList<Part> importFromInvoiceParts() {
        ObservableList<Part>importingParts=FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from invoiceparts");
            Part part;
            while (rs.next()) {
                part = new Part(allParts.size(), rs.getInt(1), rs.getInt(4), rs.getString(3), rs.getString(2), rs.getInt(5));
                importingParts.add(part);
            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with getting data from Parts");
            errorPopUp0.showError();
        }
        return importingParts;
    }

    private float importVat() {
        float fpa=-1;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from company");
            rs.next();
            fpa = rs.getFloat(1);

        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with getting Vat");
            errorPopUp0.showError();
        }
        return fpa;
    }

    private void updateVat(float fpa){
        try{
            final String query = "UPDATE company SET Vat ="+fpa+ ";";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        }catch (SQLException ex){
            errorPopUp0.setErrorMessage("Error with updating VAT");
            errorPopUp0.showError();
        }
    }


    private void deleteFromCustomers() {
        try {
            Customer selectedCustomer = tableViewCustomers.getSelectionModel().getSelectedItem();
            final int index = selectedCustomer.getCounter();
            filteredListCars = new FilteredList<>(allCars.filtered(cars -> cars.getCustomerId() == selectedCustomer.getCounter()));
            if (!filteredListCars.isEmpty()) {
                filteredListCars.get(0).setCustomerId(0);
                tableViewCars.refresh();
            }
            allCustomers.remove(selectedCustomer);
            tableViewCustomers.refresh();
            final String query = "DELETE FROM customers WHERE CustomerID = " + index;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (Exception ex) {
            errorPopUp0.setErrorMessage("Error with deleting Customer");
            errorPopUp0.showError();
        }
    }

    private void deleteFromCars() {
        try {
            Car selectedCar = tableViewCars.getSelectionModel().getSelectedItem();
            final String index = selectedCar.getLicencePlates();
            allCars.remove(selectedCar);
            tableViewCars.refresh();
            final String query = "DELETE FROM cars WHERE LicensePlates = '" + index + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (Exception ex) {
            errorPopUp0.setErrorMessage("Error with deleting Car");
            errorPopUp0.showError();

        }
    }

    private void updateFromCustomers() {
        try {
            final int index2;
            Customer selectedCustomer = tableViewCustomers.getSelectionModel().getSelectedItem();
            if (customersFiltered) {
                index2 = filteredListCustomers.getSourceIndexFor(allCustomers, 0);
            } else {
                index2 = tableViewCustomers.getSelectionModel().getFocusedIndex();
            }
            final int index = selectedCustomer.getCounter();
            CustomersForm customersForm = new CustomersForm(primaryStage, selectedCustomer.getName(), selectedCustomer.getSurname(), selectedCustomer.getPhone1(), selectedCustomer.getPhone2(), selectedCustomer.getAddress(), selectedCustomer.getBalance());
            customersForm.showForm();
            if (customersForm.isChanged()) {
                final String query = customersForm.getQuery() + " WHERE CustomerID = " + index + ";";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                allCustomers.get(index2).setName(customersForm.getName());
                allCustomers.get(index2).setSurname(customersForm.getSurname());
                allCustomers.get(index2).setPhone1(customersForm.getPhone1());
                allCustomers.get(index2).setPhone2(customersForm.getPhone2());
                allCustomers.get(index2).setAddress(customersForm.getAddress());
                allCustomers.get(index2).setBalance(customersForm.getBalance());
                tableViewCustomers.refresh();
            }
        } catch (Exception ex) {
            errorPopUp0.setErrorMessage("Select a customer!");
            errorPopUp0.showError();
        }
    }

    private void updateFromCars() {
        try {
            final int index2;
            Car selectedCar = tableViewCars.getSelectionModel().getSelectedItem();
            if (carsFiltered) {
                index2 = filteredListCars.getSourceIndexFor(allCars, 0);
            } else {
                index2 = tableViewCars.getSelectionModel().getFocusedIndex();
            }
            final String index = selectedCar.getLicencePlates();
            CarsForm carsForm = new CarsForm(primaryStage, selectedCar.getLicencePlates(), selectedCar.getBrand(), selectedCar.getModel(), selectedCar.getVin(), selectedCar.getDate(), selectedCar.getCustomerId());
            carsForm.showForm();
            if (carsForm.isChanged()) {
                final String query = carsForm.getQuery() + " WHERE LicensePlates = '" + index + "';";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                allCars.get(index2).setBrand(carsForm.getBrand());
                allCars.get(index2).setLicencePlates(carsForm.getLicensePlates());
                allCars.get(index2).setModel(carsForm.getModel());
                allCars.get(index2).setVin(carsForm.getVin());
                allCars.get(index2).setDate(carsForm.getDate());
                allCars.get(index2).setCustomerId(carsForm.getCustomerID());
                tableViewCars.refresh();
            }
        } catch (Exception ex) {
            errorPopUp0.setErrorMessage("Select a car!");
            errorPopUp0.showError();
        }
    }

    private void addToCustomers(String til) {
        try {
            CustomersForm customersForm = new CustomersForm(primaryStage);
            customersForm.setTextFieldPhone1(til);
            customersForm.showForm();
            if (customersForm.isChanged()) {
                final String query = customersForm.getQueryAdd();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                //to deftero query ginete gia na piasoume to customerID tou customer to opio exei topothetithei sto database gia ton logo oti den mporoume na gnorizoume sigoura to epomeno
                final String querySearch = "SELECT * FROM customers WHERE Phone_1=" + customersForm.getPhone1() + ";";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(querySearch);
                rs.next();
                Customer customerNew = new Customer(rs.getInt(1), customersForm.getName(), customersForm.getSurname(), customersForm.getPhone1(), ((customersForm.getPhone2() == null) ? 0 : customersForm.getPhone2()), customersForm.getAddress(), customersForm.getBalance());
                allCustomers.add(customerNew);
                tableViewCustomers.refresh();
            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with adding data to database.");
            errorPopUp0.showError();
        }
    }

    private void addToCars(String noumera) {
        try {
            CarsForm carsForm = new CarsForm(primaryStage);
            carsForm.setTextFieldLicensePlates(noumera);
            carsForm.showForm();
            if (carsForm.isChanged()) {
                final String query = carsForm.getQueryAdd();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                Car carNew = new Car(carsForm.getLicensePlates(), carsForm.getBrand(), carsForm.getModel(), carsForm.getVin(), carsForm.getDate(), carsForm.getCustomerID());
                allCars.add(carNew);
                tableViewCars.refresh();
            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with adding data to database.");
            errorPopUp0.showError();
        }
    }

    private Invoice addToInvoice(InvoiceForm invoiceForm, InvoicePrintPreview invoicePrintPreview) {
        int arithmosTimologiou;
        Invoice invoice = null;
        try {
            String queryBegin = "BEGIN;";
            String query = "INSERT INTO invoice (InvoiceID, Date, CustomerID, LicensePlates, RepairID, MechanicID, Cash, Remaining) VALUES (NULL";
            query = query + ", '" + invoicePrintPreview.getDateInvoice() + "', ";
            query = query + invoiceForm.getCustomerID() + ", ";
            query = query + "'" + invoiceForm.getLicensePlates() + "', ";
            query = query + (invoiceForm.getRepairIndex() + 1) + ", ";
            query = query + (invoiceForm.getMechanicIndex() + 1) + ", ";
            query = query + (invoiceForm.getCreditCash().equals("Credit") ? 0 : 1) + ", ";
            query = query + (invoiceForm.getCreditCash().equals("Credit") ? 0 : invoicePrintPreview.getAmount()) + ");";
            final String queryInvoice = query;
            //invoice
            invoice = new Invoice();
            invoice.setBalance(invoiceForm.getCreditCash().equals("Credit") ? 0 : invoicePrintPreview.getAmount());
            invoice.setDate(invoicePrintPreview.getDateInvoice());
            invoice.setFullName(invoiceForm.getFullName());
            invoice.setLicensePlates(invoiceForm.getLicensePlates());
            invoice.setRepairType(allRepairs.get(invoiceForm.getRepairIndex()).getName());
            invoice.setCustomerID(invoiceForm.getCustomerID());
            invoice.setMechanicName(allMechanics.get(invoiceForm.getMechanicIndex()).getName() + allMechanics.get(invoiceForm.getMechanicIndex()).getSurname());
            invoice.setPhone(invoiceForm.getPhone());
            if (invoiceForm.getCreditCash().equals("Cash")) {
                Invoice finalInvoice = invoice;
                filteredListCustomers = new FilteredList<>(allCustomers.filtered(customer -> customer.getCounter() == finalInvoice.getCustomerID()));
                float newAmount = filteredListCustomers.get(0).getBalance() + invoicePrintPreview.getAmount();
                allCustomers.get(filteredListCustomers.getSourceIndexFor(allCustomers, 0)).setBalance(newAmount);
                tableViewCustomers.refresh();
                final String queryCustomerAmount = "Update customers SET Balance = " + newAmount + " WHERE CustomerID = " + invoice.getCustomerID() + ";";
                PreparedStatement preparedStatement = connection.prepareStatement(queryCustomerAmount);
                preparedStatement.execute();
            }
            //invoice metadata
            InvoiceMetaData invoiceMetaData = new InvoiceMetaData(invoicePrintPreview.getVat());
            invoiceMetaData.setVat(invoicePrintPreview.getVat());
            invoiceMetaData.setDateIn(invoicePrintPreview.getDateIN());
            invoiceMetaData.setDateOut(invoicePrintPreview.getDateOUT());
            invoiceMetaData.setMileage(invoicePrintPreview.getMileage());
            invoiceMetaData.setFirstOil(invoicePrintPreview.getFirstOil());
            invoiceMetaData.setNextOil(invoicePrintPreview.getNextOil());
            invoiceMetaData.setNextService(invoicePrintPreview.getNextService());
            invoiceMetaData.setComments(invoicePrintPreview.getComments());
            invoiceMetaData.setDiscount(invoicePrintPreview.getDiscount());
            //execute queries
            query = "INSERT INTO invoicemetadata (InvoiceID,ImportDate,ExportDate,Miles,FirstOil,NextOil,NextService,Comments,Discount, Vat) VALUES (";
            query = query + "LAST_INSERT_ID()" + ", '" + invoiceMetaData.getDateIn() + "', '" + invoiceMetaData.getDateOut() + "', ";
            query = query + invoiceMetaData.getMileage() + ", " + invoiceMetaData.getFirstOil() + ", " + invoiceMetaData.getNextOil() + ", " + invoiceMetaData.getNextService() + ",";
            query = query + "'" + invoiceMetaData.getComments() + "', " + invoiceMetaData.getDiscount() + ", " + invoiceMetaData.getVat() + ");";
            final String queryCommit = "COMMIT;";
            final String queryInvoiceMetaData = query;
            Statement statement = connection.createStatement();
            statement.executeUpdate(queryBegin);
            statement.executeUpdate(queryInvoice);
            statement.executeUpdate(queryInvoiceMetaData);
            statement.executeUpdate(queryCommit);
            //Find invoiceID
            ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            arithmosTimologiou = rs.getInt(1);
            //insert to tables
            invoice.setInvoiceID(arithmosTimologiou);
            invoiceMetaData.setInvoiceId(arithmosTimologiou);
            allInvoices.add(invoice);
            allInvoicesMetaData.add(invoiceMetaData);
            //insert parts to database
            ObservableList<Part> parts;
            parts = invoicePrintPreview.getAllParts();
            Part newPart;
            for (Part p : parts) {
                newPart=new Part(0,arithmosTimologiou,p.getQuantity(),p.getDescription(),p.getPartsID(),p.getPrice());
                allParts.add(newPart);
                query = "INSERT INTO invoiceparts (InvoiceID, PartsId, Description, Quantity, Price) VALUES (";
                query = query + arithmosTimologiou + ", '" + p.getPartsID() + "', '" + p.getDescription() + "', " + p.getQuantity() + ", " + Float.parseFloat(decimalFormat.format(p.getPrice())) + ");";
                final String queryInvoiceParts = query;
                PreparedStatement preparedStatement = connection.prepareStatement(queryInvoiceParts);
                preparedStatement.execute();
            }
        } catch (SQLException ex) {
            errorPopUp0.setErrorMessage("Error with adding data to database.");
            errorPopUp0.showError();
        }
        return invoice;
    }

    private void deleteFromMechanics() {
        try {
            Mechanic selectedMechanic = tableViewMechanic.getSelectionModel().getSelectedItem();
            final int index = selectedMechanic.getMechanicID();
            filteredMechanics = new FilteredList<>(allMechanics.filtered(mechanic -> mechanic.getMechanicID().equals(selectedMechanic.getMechanicID())));
            allMechanics.remove(selectedMechanic);
            tableViewMechanic.refresh();
            final String query = "DELETE FROM mechanics WHERE MechanicID = " + index;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (Exception ex) {
            errorPopUp0.setErrorMessage("Error with deleting Mechanic");
            errorPopUp0.showError();
        }
    }

    private void updateFromMechanics() {
        try {
            Mechanic selectedMechanic = tableViewMechanic.getSelectionModel().getSelectedItem();
            final int index2 = tableViewCustomers.getSelectionModel().getFocusedIndex();
            final int index = selectedMechanic.getMechanicID();
            MechanicForm mechanicForm = new MechanicForm(primaryStage, selectedMechanic.getName(), selectedMechanic.getSurname());
            mechanicForm.showForm();
            if (mechanicForm.isChanged()) {
                final String query = mechanicForm.getQuery() + " WHERE MechanicID = " + index + ";";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                allMechanics.get(index2).setName(mechanicForm.getName());
                allMechanics.get(index2).setSurname(mechanicForm.getSurname());
                tableViewMechanic.refresh();
            }
        } catch (Exception ex) {
            errorPopUp0.setErrorMessage("Select a customer!");
            errorPopUp0.showError();
        }
    }

    private void addToMechanics() {
        try {
            MechanicForm mechanicForm = new MechanicForm(primaryStage);
            mechanicForm.showForm();
            if (mechanicForm.isChanged()) {
                final String query = mechanicForm.getQueryAdd();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                //to deftero query ginete gia na piasoume to customerID tou customer to opio exei topothetithei sto database gia ton logo oti den mporoume na gnorizoume sigoura to epomeno
                final String querySearch = "SELECT LAST_INSERT_ID();";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(querySearch);
                rs.next();
                Mechanic mechanicNew = new Mechanic(rs.getInt(1), mechanicForm.getName(), mechanicForm.getSurname());
                allMechanics.add(mechanicNew);
                tableViewMechanic.refresh();
            }
        } catch (SQLException e) {
            errorPopUp0.setErrorMessage("Error with adding mechanic to database.");
            errorPopUp0.showError();
        }
    }




    private void filterInvoices(int type, String strSearch, String eidosEpiskevis) {
        /*
            0: tha ginei anazitisi meso noumera aftokinitou
            1: tha ginei anazitisi analogos ton episkevon
            2: tha ginei anazitisi analogos ton episkevon apo tin eidi filtrarismeni lista ton timologion
            3: tha ginei anazitisi analogos ton episkevon apo tin dipla filtrarismeni lista ton timologion
            4: tha ginei anazitisi analogos tou arithmou tou pelati kai an to balance einai aniso tou miden
            5: tha ginei anazitisi analogos tou arithmou tou pelati

        */
        switch (type) {
            case 0: {
                filteredInvoices = new FilteredList<>(allInvoices.filtered(invoice -> invoice.getLicencePlates().equals(strSearch)));
                invoiceFiltered = true;
                break;
            }
            case 1: {
                filteredInvoices = new FilteredList<>(allInvoices.filtered(invoice -> invoice.getRepairType().equals(eidosEpiskevis)));
                break;
            }
            case 2: {
                invoiceFilteredTwice = true;
                twiceFilteredInvoices = filteredInvoices;
                filteredInvoices = new FilteredList<>(filteredInvoices.filtered(invoice -> invoice.getRepairType().equals(eidosEpiskevis)));
            }
            case 3: {
                filteredInvoices = new FilteredList<>(twiceFilteredInvoices.filtered(invoice -> invoice.getRepairType().equals(eidosEpiskevis)));
                break;
            }
            case 4:{
                filteredInvoices = new FilteredList<>(allInvoices.filtered(invoice -> (invoice.getCustomerID()==Integer.parseInt(strSearch) && invoice.getBalance()!=0)));
                invoiceFiltered = true;
                break;
            }
            case 5:{
                filteredInvoices = new FilteredList<>(allInvoices.filtered(invoice -> (invoice.getCustomerID()==Integer.parseInt(strSearch))));
                invoiceFiltered = true;
                break;
            }
        }
        tableViewInvoice.setItems(filteredInvoices);
        tableViewInvoice.refresh();
    }

    public static Float getVat() {
        return vat;
    }

    private void openInvoice(Invoice invoice) {
        filteredInvoicesMetaData = new FilteredList<>(allInvoicesMetaData.filtered(invoiceMetaData -> invoiceMetaData.getInvoiceId() == invoice.getInvoiceID()));
        InvoiceMetaData selectedInvoiceMetaData = filteredInvoicesMetaData.get(0);
        filteredParts = new FilteredList<>(allParts.filtered(part -> part.getInvoiceID() == invoice.getInvoiceID()));
        ObservableList<Part> selectedParts;
        selectedParts = filteredParts;
        Car selectedCar = new FilteredList<>(allCars.filtered(car -> car.getLicencePlates().equals(invoice.getLicencePlates()))).get(0);
        InvoicePrintPreview invoicePrintPreview = new InvoicePrintPreview(primaryStage, invoice.getCustomerID(), invoice.getFullName(), invoice.getLicencePlates(), selectedCar.getBrand() + selectedCar.getModel(), selectedCar.getVin());
        invoicePrintPreview.setMileage(selectedInvoiceMetaData.getMileage());
        invoicePrintPreview.setDateInvoice(invoice.getDate());
        invoicePrintPreview.setInvoiceID(invoice.getInvoiceID());
        invoicePrintPreview.setDateOUT(selectedInvoiceMetaData.getDateOut());
        invoicePrintPreview.setDateIN(selectedInvoiceMetaData.getDateIn());
        invoicePrintPreview.setAllParts(selectedParts);
        invoicePrintPreview.setDiscount(selectedInvoiceMetaData.getDiscount());
        invoicePrintPreview.setFirstOil(selectedInvoiceMetaData.getFirstOil());
        invoicePrintPreview.setNextOil(selectedInvoiceMetaData.getNextOil());
        invoicePrintPreview.setComments(selectedInvoiceMetaData.getComments());
        invoicePrintPreview.setNextService(selectedInvoiceMetaData.getNextService());
        invoicePrintPreview.setVat(selectedInvoiceMetaData.getVat());
        invoicePrintPreview.showPreview();
        if (invoicePrintPreview.isBeingPrinted() && invoicePrintPreview.isBeingSaved()) {
            print.setNode(invoicePrintPreview.getNodeForPrint());
            print.print();
        }
    }

    private void editBalance(){
        Invoice selectedInvoice=tableViewInvoice.getSelectionModel().getSelectedItem();
        if(selectedInvoice.getBalance()!=0){
            try {
                int sourceIndex = allInvoices.indexOf(selectedInvoice);

                BalanceForm balanceForm = new BalanceForm(primaryStage, selectedInvoice.getBalance());
                balanceForm.showForm();
                if (balanceForm.isChanged()) {
                    filteredListCustomers = new FilteredList<>(allCustomers.filtered(customer -> customer.getCounter() == selectedInvoice.getCustomerID()));
                    float oldBalance = filteredListCustomers.get(0).getBalance();
                    float newBalance = oldBalance - balanceForm.getDifferenceInBalance();
                    final String queryCustomer = "Update customers SET Balance = " + newBalance + " WHERE CustomerID = " + filteredListCustomers.get(0).getCounter() + ";";
                    PreparedStatement preparedStatement = connection.prepareStatement(queryCustomer);
                    preparedStatement.execute();
                    final String queryInvoice = "Update invoice SET Remaining = " + balanceForm.getBalance() + " WHERE InvoiceID = " + selectedInvoice.getInvoiceID() + ";";
                    preparedStatement = connection.prepareStatement(queryInvoice);
                    preparedStatement.execute();
                    allCustomers.get(filteredListCustomers.getSourceIndexFor(allCustomers,0)).setBalance(newBalance);
                    tableViewCustomers.refresh();
                    allInvoices.get(sourceIndex).setBalance(balanceForm.getBalance());
                    tableViewInvoice.refresh();
                }
            }catch (SQLException exception){
                errorPopUp0.setErrorMessage("Error with updating database");
                errorPopUp0.showError();
            }
        }else{
            errorPopUp0.setErrorMessage("Can not change Balance.");
            errorPopUp0.showError();
        }
    }
}