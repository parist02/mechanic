package com.mechanic.code;

import com.mechanic.code.databaseClasses.Cars;
import com.mechanic.code.databaseClasses.CarsForm;
import com.mechanic.code.databaseClasses.Customers;
import com.mechanic.code.databaseClasses.CustomersForm;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;

public class MainScreen extends Application {
    private TableView<Customers> tableViewCustomers=new TableView<>();
    private TableView<Cars>tableViewCars= new TableView<>();
    private static Insets padding = new Insets(10, 10, 10, 10);
    private static Font font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 18);
    private Connection connection;
    private Stage primaryStage;
    private ObservableList<Customers>allCustomers;
    private ObservableList<Cars>allCars;
    private ErrorPopUp errorPopUp0;
    private ErrorPopUp errorPopUp1;
    private ErrorPopUp errorPopUp2;

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("The application is starting");
        System.out.println("Trying to create connection to database...");
        try {

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mechanic", "root", "");
            System.out.println("Established connection...");

        } catch (Exception e) {
            System.out.println("Connection with database has failed...");
            System.exit(0);

        }
    }

    @Override
    public void start(Stage stage) {

        TabPane tabPane = new TabPane();
        Form form = new Form();


        Button buttonForm = new Button("Show form");
        buttonForm.setPadding(padding);
        buttonForm.setFont(font);

        Label labelInvoice = new Label("Press the button to show the print form");
        labelInvoice.setPadding(padding);
        labelInvoice.setFont(font);
        VBox boxInvoice = new VBox(buttonForm, labelInvoice);
        boxInvoice.setPadding(padding);
        buttonForm.setOnAction(e -> form.showForm());


        //Tab 1
        Label labelCustomer = new Label("Customers");
        TableColumn<Customers, Integer> counterColumn = new TableColumn<>("No.");
        counterColumn.setReorderable(false);
        counterColumn.setResizable(false);
        counterColumn.setMinWidth(50);
        counterColumn.setCellValueFactory(new PropertyValueFactory<>("counter"));

        TableColumn<Customers, Integer> nameColumn = new TableColumn<>("Name");
        nameColumn.setReorderable(false);
        nameColumn.setResizable(false);
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customers, Integer> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setReorderable(false);
        surnameColumn.setResizable(false);
        surnameColumn.setMinWidth(100);
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<Customers, Integer> phone1Column = new TableColumn<>("Phone1");
        phone1Column.setReorderable(false);
        phone1Column.setResizable(false);
        phone1Column.setMinWidth(100);
        phone1Column.setCellValueFactory(new PropertyValueFactory<>("phone1"));

        TableColumn<Customers, Integer> phone2Column = new TableColumn<>("Phone2");
        phone2Column.setReorderable(false);
        phone2Column.setResizable(false);
        phone2Column.setMinWidth(100);
        phone2Column.setCellValueFactory(new PropertyValueFactory<>("phone2"));

        TableColumn<Customers, Integer> addressColumn = new TableColumn<>("Address");
        addressColumn.setReorderable(false);
        addressColumn.setResizable(false);
        addressColumn.setMinWidth(200);
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Customers,Float> balanceColumn=new TableColumn<>("Balance");
        balanceColumn.setReorderable(false);
        balanceColumn.setResizable(false);
        balanceColumn.setMinWidth(100);
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));



        tableViewCustomers.getColumns().addAll(counterColumn,nameColumn,surnameColumn,phone1Column,phone2Column,addressColumn,balanceColumn);
        tableViewCustomers.setEditable(false);
        tableViewCustomers.setItems(importFromCustomers());
        tableViewCustomers.setPadding(padding);
        tableViewCustomers.setMaxHeight(500);
        tableViewCustomers.setMaxWidth(800);
        tableViewCustomers.setOnMouseClicked(mouseEvent -> {
            if(tableViewCustomers.getSelectionModel().isEmpty()){
                System.out.println("Nothing is selected");
            }else{
                ObservableList<Customers>selectedCustomer=tableViewCustomers.getSelectionModel().getSelectedItems();
                ObservableList<Cars> carsSearched = FXCollections.observableArrayList();
                try {
                    final String querySearch = "SELECT * FROM cars WHERE CustomerID=" + selectedCustomer.get(0).getCounter() + ";";
                    System.out.println(querySearch);
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(querySearch);
                    Cars carSearched;
                    while (rs.next()) {
                        carSearched =  new Cars(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                        carsSearched.add(carSearched);
                    }
                } catch (SQLException ex) {
                    System.out.println("Error with searching");
                }
                tableViewCars.setItems(carsSearched);
            }
        });
        Label labelCustomerTitle = new Label("Showing all customers");
        Button buttonDeleteCustomers = new Button("Delete");
        buttonDeleteCustomers.setPadding(padding);
        buttonDeleteCustomers.setOnAction(del -> {
            if (tableViewCustomers.getSelectionModel().isEmpty()){
                errorPopUp0.setErrorMessage("Επιλέξτε τον πελάτη που θέλετε");
                errorPopUp0.showError();
            }else {
                errorPopUp2.setErrorMessage("Είστε σίγουρος ότι θέλετε να διαγράψετε τον πελάτη;");
                errorPopUp2.showError();
                if (errorPopUp2.isAdded()) {
                    deleteFromCustomers();
                }
            }
        });
        Button buttonUpdateCustomers = new Button("Edit");
        buttonUpdateCustomers.setPadding(padding);
        buttonUpdateCustomers.setOnAction(upd -> {
            updateFromCustomers();
        });
        Button buttonAddCustomer=new Button("Add");
        buttonAddCustomer.setPadding(padding);
        buttonAddCustomer.setOnAction(add->{
            addToCustomers(null);
        });
        TextField textFieldSearchCustomers = new TextField();
        textFieldSearchCustomers.setPadding(padding);
        Button buttonSearchCustomers=new Button("Search");
        Button buttonSearchClearCustomers=new Button("Clear");



        TableColumn<Cars, String> licencePlatesColumn = new TableColumn<>("License Plates");
        licencePlatesColumn.setReorderable(false);
        licencePlatesColumn.setResizable(false);
        licencePlatesColumn.setMinWidth(100);
        licencePlatesColumn.setCellValueFactory(new PropertyValueFactory<>("licencePlates"));

        TableColumn<Cars, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setReorderable(false);
        brandColumn.setResizable(false);
        brandColumn.setMinWidth(150);
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Cars, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setReorderable(false);
        modelColumn.setResizable(false);
        modelColumn.setMinWidth(150);
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Cars, String> vinColumn = new TableColumn<>("VIN");
        vinColumn.setReorderable(false);
        vinColumn.setResizable(false);
        vinColumn.setMinWidth(200);
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));

        TableColumn<Cars, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setReorderable(false);
        dateColumn.setResizable(false);
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Cars, Integer> customerIdColumn = new TableColumn<>("CustomerID");
        customerIdColumn.setReorderable(false);
        customerIdColumn.setResizable(false);
        customerIdColumn.setMinWidth(50);
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));



        tableViewCars.getColumns().add(licencePlatesColumn);
        tableViewCars.getColumns().add(brandColumn);
        tableViewCars.getColumns().add(modelColumn);
        tableViewCars.getColumns().add(vinColumn);
        tableViewCars.getColumns().add(dateColumn);
        tableViewCars.getColumns().add(customerIdColumn);
        tableViewCars.setEditable(false);
        tableViewCars.setMaxHeight(150);
        tableViewCars.setMaxWidth(800);
        tableViewCars.setItems(importFromCars());
        tableViewCars.setPadding(padding);
        tableViewCars.setOnMouseClicked(mouseEvent -> {
            if(tableViewCars.getSelectionModel().isEmpty()){
                System.out.println("Nothing is selected");
            }else{
                ObservableList<Cars>selectedCar=tableViewCars.getSelectionModel().getSelectedItems();
                ObservableList<Customers> customersSearched = FXCollections.observableArrayList();
                try {
                    final String querySearch = "SELECT * FROM customers WHERE CustomerID=" + selectedCar.get(0).getCustomerId() + ";";
                    System.out.println(querySearch);
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(querySearch);
                    Customers customerSearched;
                    while (rs.next()) {
                        customerSearched =  new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6),rs.getFloat(7));
                        customersSearched.add(customerSearched);
                    }
                } catch (SQLException ex) {
                    System.out.println("Error with searching");
                }
                tableViewCustomers.setItems(customersSearched);
            }
        });






        Button buttonAddCars=new Button("Add");
        buttonAddCars.setPadding(padding);
        buttonAddCars.setOnAction(add->{
            addToCars(null);
        });
        Button buttonDeleteCars = new Button("Delete");
        buttonDeleteCars.setPadding(padding);
        buttonDeleteCars.setOnAction(del -> {
            deleteFromCars();
        });


        ComboBox comboBoxSearchTab1=new ComboBox();
        comboBoxSearchTab1.getItems().add("Αριθμός Τηλεφώνου");
        comboBoxSearchTab1.getItems().add("Νούμερα Αυτοκινήτου");
        comboBoxSearchTab1.getSelectionModel().selectFirst();
        comboBoxSearchTab1.setPadding(padding);
        buttonSearchCustomers.setPadding(padding);
        buttonSearchCustomers.setOnAction(ser->{
            if (!textFieldSearchCustomers.getText().equals("")) {
                if (comboBoxSearchTab1.getValue().equals("Αριθμός Τηλεφώνου")) {
                    if (textFieldSearchCustomers.getText().replaceAll("[^0-9]", "").equals("")) {
                        errorPopUp0.setErrorMessage("Μη έγκυρη είσοδος , παρακαλώ καταχώρησε αριθμό τηλεφώνου");
                        errorPopUp0.showError();
                    } else {
                        ObservableList<Customers> searchedData = searchFromCustomers(0, textFieldSearchCustomers.getText().replaceAll("[^0-9]", ""));
                        if (searchedData.isEmpty()) {
                            errorPopUp1.setErrorMessage("Ο πελάτης με αριθμό τηλεφώνου "+textFieldSearchCustomers.getText().replaceAll("[^0-9]", "")+" που αναζητήσατε δεν βρέθηκε. Θέλετε να τον προσθεσετε;");
                            errorPopUp1.showError();
                            if (errorPopUp1.isAdded()) {
                                addToCustomers(textFieldSearchCustomers.getText().replaceAll("[^0-9]", ""));
                            }
                        } else {
                            tableViewCustomers.setItems(searchedData);
                            int customerIDSearched = tableViewCustomers.getItems().get(0).getCounter();
                            tableViewCars.setItems(searchFromCars(customerIDSearched, ""));
                        }
                    }
                } else if (comboBoxSearchTab1.getValue().equals("Νούμερα Αυτοκινήτου")) {
                    if (textFieldSearchCustomers.getText().replaceAll("[^a-zA-Z0-9]", "").equals("")) {
                        errorPopUp0.setErrorMessage("Μη έγκυρη είσοδος , παρακαλώ καταχώρησε νούμερα αυτοκινήτου");
                        errorPopUp0.showError();
                    } else {
                        ObservableList<Cars> searchedData = searchFromCars(0, textFieldSearchCustomers.getText().replaceAll("[^a-zA-Z0-9]", ""));
                        if (searchedData.isEmpty()) {
                            errorPopUp1.setErrorMessage("To αυτοκίνητο με νούμερα "+textFieldSearchCustomers.getText().replaceAll("[^a-zA-Z0-9]", "")+" που αναζητήσατε δεν βρέθηκε. Θέλετε να το προσθεσετε;");
                            errorPopUp1.showError();
                            if (errorPopUp1.isAdded()) {
                                addToCars(textFieldSearchCustomers.getText().replaceAll("[^a-zA-Z0-9]", ""));
                            }
                        } else {
                            tableViewCars.setItems(searchedData);
                            int customerIDSearched = tableViewCars.getItems().get(0).getCustomerId();
                            tableViewCustomers.setItems(searchFromCustomers(customerIDSearched, ""));
                        }
                    }
                }
            }else {
                errorPopUp0.setErrorMessage("Η μπάρα αναζήτησης είναι κενή");
                errorPopUp0.showError();
            }
        });
        buttonSearchClearCustomers.setPadding(padding);
        buttonSearchClearCustomers.setOnAction(cle->{
            tableViewCustomers.setItems(allCustomers);
            textFieldSearchCustomers.setText("");
            tableViewCars.setItems(allCars);
            tableViewCustomers.getSelectionModel().clearSelection();
            tableViewCars.getSelectionModel().clearSelection();

        });


        HBox boxButtonsCustomers = new HBox(buttonAddCustomer,buttonUpdateCustomers, buttonDeleteCustomers,comboBoxSearchTab1,textFieldSearchCustomers,buttonSearchCustomers,buttonSearchClearCustomers);
        boxButtonsCustomers.setPadding(padding);
        boxButtonsCustomers.setSpacing(20);
        HBox boxButtonsCars=new HBox(buttonAddCars,buttonDeleteCars);
        boxButtonsCars.setPadding(padding);
        boxButtonsCars.setSpacing(20);
        VBox boxCustomers = new VBox(labelCustomerTitle,boxButtonsCustomers, tableViewCustomers,boxButtonsCars,tableViewCars );
        boxCustomers.setPadding(padding);
        boxCustomers.setSpacing(20);




        //
        Tab tab1 = new Tab("Customers", boxCustomers);
        Tab tab2 = new Tab("Parts", new Label("Showing all parts"));
        Tab tab3 = new Tab("Invoices", boxInvoice);

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);


        Scene scene = new Scene(tabPane);
        primaryStage = new Stage();
        primaryStage.setMaximized(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MainScreen");
        stage = primaryStage;
        errorPopUp0=new ErrorPopUp(0,primaryStage);
        errorPopUp1=new ErrorPopUp(1,primaryStage);
        errorPopUp2=new ErrorPopUp(2,primaryStage);
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        System.out.println("Closing connection...");
        connection.close();
        System.out.println("Connection closed...");

    }


    public ObservableList<Customers> importFromCustomers() {
        allCustomers = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customers");
            Customers customer;
            while (rs.next()) {
                customer = new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6),rs.getFloat(7));
                allCustomers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Error with getting data");
        }
        return allCustomers;
    }

    public ObservableList<Cars> importFromCars() {
        allCars = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from cars");
            Cars cars;
            while (rs.next()) {
                cars = new Cars(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                allCars.add(cars);
            }

        } catch (SQLException e) {
            System.out.println("Error with getting data");
        }
        return allCars;
    }

    public ObservableList<Cars>searchFromCars(int searchType,String searchString) {
        ObservableList<Cars> carsSearched = FXCollections.observableArrayList();
        if (searchType == 0) {
            try {
                final String querySearch = "SELECT * FROM cars WHERE LicensePlates= '" + searchString + "';";
                System.out.println(querySearch);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(querySearch);
                Cars carSearched;
                while (rs.next()) {
                    carSearched = new Cars(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                    carsSearched.add(carSearched);
                }
            } catch (SQLException ex) {
                System.out.println("Error with searching");
            }
        }else{
            try {
                final String querySearch = "SELECT * FROM cars WHERE CustomerID=" + searchType + ";";
                System.out.println(querySearch);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(querySearch);
                Cars carSearched;
                while (rs.next()) {
                    carSearched =  new Cars(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6));
                    carsSearched.add(carSearched);
                }
            } catch (SQLException ex) {
                System.out.println("Error with searching");
            }
        }
        return carsSearched;
    }

    public ObservableList<Customers>searchFromCustomers(int searchType,String searchString) {
        ObservableList<Customers> customersSearched = FXCollections.observableArrayList();
        if (searchType == 0) {
            try {
                final String querySearch = "SELECT * FROM customers WHERE Phone_1=" + searchString + ";";
                System.out.println(querySearch);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(querySearch);
                Customers customerSearched;
                while (rs.next()) {
                    customerSearched = new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7));
                    customersSearched.add(customerSearched);
                }
                final String querySearch2 = "SELECT * FROM customers WHERE Phone_2=" + searchString + ";";
                System.out.println(querySearch2);
                rs = statement.executeQuery(querySearch2);
                while (rs.next()) {
                    customerSearched = new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7));
                    customersSearched.add(customerSearched);
                }
            } catch (SQLException ex) {
                System.out.println("Error with searching");
            }
        }else{
            try {
                final String querySearch = "SELECT * FROM customers WHERE CustomerID=" + searchType + ";";
                System.out.println(querySearch);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(querySearch);
                Customers customerSearched;
                while (rs.next()) {
                    customerSearched = new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getFloat(7));
                    customersSearched.add(customerSearched);
                }
            } catch (SQLException ex) {
                System.out.println("Error with searching");
            }
        }
        return customersSearched;
    }





    public void deleteFromCustomers() {
        try {
            ObservableList<Customers> selectedCustomer, allCustomers;
            allCustomers = tableViewCustomers.getItems();
            selectedCustomer = tableViewCustomers.getSelectionModel().getSelectedItems();
            final int index = selectedCustomer.get(0).getCounter();
            selectedCustomer.forEach(allCustomers::remove);
            final String query = "DELETE FROM customers WHERE CustomerID = " + index;
            System.out.println(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (Exception ex) {
            System.out.println("Error with removing from database");
//            ErrorPopUp errorPopUp = new ErrorPopUp("Select an item", primaryStage);
        }
    }

    public void deleteFromCars() {
        try {
            ObservableList<Cars> selectedCars, allCars;
            allCars = tableViewCars.getItems();
            selectedCars = tableViewCars.getSelectionModel().getSelectedItems();
            final String index = selectedCars.get(0).getLicencePlates();
            selectedCars.forEach(allCars::remove);
            final String query = "DELETE FROM cars WHERE LicensePlates = '" + index + "'";
            System.out.println(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (Exception ex) {
            System.out.println("Error with removing from database");
            errorPopUp0.setErrorMessage("Επιλέξτε τον πελάτη που θέλετε");
            errorPopUp0.showError();
//            ErrorPopUp errorPopUp = new ErrorPopUp("Select an item", primaryStage);
        }
    }




    public void updateFromCustomers() {
        try {
            ObservableList<Customers> selectedCustomer, allCustomers;
            allCustomers = tableViewCustomers.getItems();
            selectedCustomer = tableViewCustomers.getSelectionModel().getSelectedItems();
            final int index2=tableViewCustomers.getSelectionModel().getFocusedIndex();
            final int index = selectedCustomer.get(0).getCounter();
            CustomersForm customersForm = new CustomersForm(primaryStage, selectedCustomer.get(0).getName(), selectedCustomer.get(0).getSurname(), selectedCustomer.get(0).getPhone1(), selectedCustomer.get(0).getPhone2(), selectedCustomer.get(0).getAddress(),selectedCustomer.get(0).getBalance());
            customersForm.showForm();
            if (customersForm.isChanged()) {
                final String query=customersForm.getQuery()+" WHERE CustomerID = "+index+";";
                System.out.println(query);
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.execute();
                Customers customersEdited=new Customers(index,customersForm.getName(), customersForm.getSurname(),customersForm.getPhone1(),customersForm.getPhone2(),customersForm.getAddress(),customersForm.getBalance());
                allCustomers.set(index2,customersEdited);
            } else {
                System.out.println("Not saving changes");
            }
        } catch (Exception ex) {
            System.out.println("Error with updating from database");
            errorPopUp0.setErrorMessage("Επιλέξτε τον πελάτη που θέλετε");
            errorPopUp0.showError();
//            ErrorPopUp errorPopUp = new ErrorPopUp("Select an item", primaryStage);
        }
    }

    public void addToCustomers(String til){
        try{
            CustomersForm customersForm=new CustomersForm(primaryStage);
            customersForm.setTextFieldPhone1(til);
            customersForm.showForm();
            if (customersForm.isChanged()) {
                final String query = customersForm.getQueryAdd();
                System.out.println(query);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                final String querySearch = "SELECT * FROM customers WHERE Phone_1=" + customersForm.getPhone1() + ";";
                Statement statement=connection.createStatement();
                ResultSet rs = statement.executeQuery(querySearch);
                rs.next();
                Customers customersNew=new Customers(rs.getInt(1),customersForm.getName(),customersForm.getSurname(),customersForm.getPhone1(),((customersForm.getPhone2()==null)?0:customersForm.getPhone2()),customersForm.getAddress(),customersForm.getBalance());
                ObservableList<Customers>newCustomer=tableViewCustomers.getItems();
                newCustomer.add(customersNew);
                tableViewCustomers.setItems(newCustomer);
            }else{
                System.out.println("Data not added");
            }
        }catch (SQLException e) {
            System.out.println("Error with adding data to database");
            errorPopUp0.setErrorMessage("Πρόβλημα στην εισαγωγή δεδομένων. Ελέξτε αν βάλατε τα σωστά στοιχεία.");
            errorPopUp0.showError();
            //ErrorPopUp errorPopUp=new ErrorPopUp("Error with adding data to database",primaryStage);
        }
    }

    public void addToCars(String noumera){
        try{
            CarsForm carsForm=new CarsForm(primaryStage);
            carsForm.setTextFieldLicensePlates(noumera);
            carsForm.showForm();
            if (carsForm.isChanged()) {
                final String query = carsForm.getQueryAdd();
                System.out.println(query);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                Cars carsNew=new Cars(carsForm.getLicensePlates(),carsForm.getBrand(),carsForm.getModel(),carsForm.getVin(),carsForm.getDate(),carsForm.getCustomerID());
                ObservableList<Cars>newCars=tableViewCars.getItems();
                newCars.add(carsNew);
                tableViewCars.setItems(newCars);
            }else{
                System.out.println("Data not added");
            }
        }catch (SQLException e) {
            System.out.println("Error with adding data to database");
            errorPopUp0.setErrorMessage("Πρόβλημα στην εισαγωγή δεδομένων. Ελέξτε αν βάλατε τα σωστά στοιχεία.");
            errorPopUp0.showError();
            //ErrorPopUp errorPopUp=new ErrorPopUp("Error with adding data to database",primaryStage);
        }
    }

}