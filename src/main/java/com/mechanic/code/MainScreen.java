package com.mechanic.code;

import com.mechanic.code.databaseClasses.Cars;
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

import java.io.FileNotFoundException;
import java.sql.*;

public class MainScreen extends Application {
    public TableView<Customers> tableViewCustomers;
    public TableView<Cars>tableViewCars;
    public static Insets padding = new Insets(10, 10, 10, 10);
    public static Font font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 18);
    public Connection connection;
    public Stage primaryStage;
    public int customerID;
    public ObservableList<Customers>allCustomers;
    public ObservableList<Cars>allCars;

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("The application is starting");
        System.out.println("Trying to create connection to database...");
        try {

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mechanic", "root", "");
            System.out.println("Established connection...");

        } catch (Exception e) {
            e.printStackTrace();
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
        phone1Column.setMinWidth(75);
        phone1Column.setCellValueFactory(new PropertyValueFactory<>("phone1"));

        TableColumn<Customers, Integer> phone2Column = new TableColumn<>("Phone2");
        phone2Column.setReorderable(false);
        phone2Column.setResizable(false);
        phone2Column.setMinWidth(75);
        phone2Column.setCellValueFactory(new PropertyValueFactory<>("phone2"));

        TableColumn<Customers, Integer> addressColumn = new TableColumn<>("Address");
        addressColumn.setReorderable(false);
        addressColumn.setResizable(false);
        addressColumn.setMinWidth(175);
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        tableViewCustomers = new TableView<>();

        tableViewCustomers.getColumns().add(counterColumn);
        tableViewCustomers.getColumns().add(nameColumn);
        tableViewCustomers.getColumns().add(surnameColumn);
        tableViewCustomers.getColumns().add(phone1Column);
        tableViewCustomers.getColumns().add(phone2Column);
        tableViewCustomers.getColumns().add(addressColumn);
        tableViewCustomers.setEditable(false);

        tableViewCustomers.setItems(importFromCustomers());
        tableViewCustomers.setPadding(padding);
        Label labelCustomerTitle = new Label("Showing all customers");
        Button buttonDeleteCustomers = new Button("Delete");
        buttonDeleteCustomers.setPadding(padding);
        buttonDeleteCustomers.setOnAction(del -> {
            deleteFromCustomers();
        });
        Button buttonUpdateCustomers = new Button("Edit");
        buttonUpdateCustomers.setPadding(padding);
        buttonUpdateCustomers.setOnAction(upd -> {
            updateFromCustomers();
        });
        Button buttonAddCustomer=new Button("Add");
        buttonAddCustomer.setPadding(padding);
        buttonAddCustomer.setOnAction(add->{
            addToCustomers();
        });
        TextField textFieldSearchCustomers = new TextField();
        textFieldSearchCustomers.setPadding(padding);
        Button buttonSearchCustomers=new Button("Search");
        buttonSearchCustomers.setPadding(padding);
        buttonSearchCustomers.setOnAction(ser->tableViewCustomers.setItems(searchFromCustomers(textFieldSearchCustomers.getText())));
        Button buttonSearchClearCustomers=new Button("Clear");
        buttonSearchClearCustomers.setPadding(padding);
        buttonSearchClearCustomers.setOnAction(cle->{
            tableViewCustomers.setItems(allCustomers);
            textFieldSearchCustomers.setText("");

        });

        TableColumn<Cars, String> licencePlatesColumn = new TableColumn<>("Licence Plates");
        licencePlatesColumn.setReorderable(false);
        licencePlatesColumn.setResizable(false);
        licencePlatesColumn.setMinWidth(100);
        licencePlatesColumn.setCellValueFactory(new PropertyValueFactory<>("licencePlates"));

        TableColumn<Cars, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setReorderable(false);
        brandColumn.setResizable(false);
        brandColumn.setMinWidth(100);
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Cars, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setReorderable(false);
        modelColumn.setResizable(false);
        modelColumn.setMinWidth(100);
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Cars, String> vinColumn = new TableColumn<>("VIN");
        vinColumn.setReorderable(false);
        vinColumn.setResizable(false);
        vinColumn.setMinWidth(125);
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

        tableViewCars = new TableView<>();

        tableViewCars.getColumns().add(licencePlatesColumn);
        tableViewCars.getColumns().add(brandColumn);
        tableViewCars.getColumns().add(modelColumn);
        tableViewCars.getColumns().add(vinColumn);
        tableViewCars.getColumns().add(dateColumn);
        tableViewCars.getColumns().add(customerIdColumn);
        tableViewCars.setEditable(false);

        tableViewCars.setItems(importFromCars());
        tableViewCars.setPadding(padding);


        HBox boxButtonsCustomers = new HBox(buttonAddCustomer,buttonUpdateCustomers, buttonDeleteCustomers,textFieldSearchCustomers,buttonSearchCustomers,buttonSearchClearCustomers);
        boxButtonsCustomers.setPadding(padding);
        boxButtonsCustomers.setSpacing(20);
        VBox boxCustomers = new VBox(labelCustomerTitle,boxButtonsCustomers, tableViewCustomers,tableViewCars );
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
                customer = new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6));
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
                cars = new Cars(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getInt(6));
                allCars.add(cars);
            }

        } catch (SQLException e) {
            System.out.println("Error with getting data");
        }
        return allCars;
    }

    public ObservableList<Customers>searchFromCustomers(String searchString) {
        ObservableList<Customers>customersSearched=FXCollections.observableArrayList();
        try {
            final String querySearch = "SELECT * FROM customers WHERE Phone_1=" + searchString + ";";
            System.out.println(querySearch);
            Statement statement=connection.createStatement();
            ResultSet rs = statement.executeQuery(querySearch);
            Customers customerSearched;
            while (rs.next()) {
                customerSearched = new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6));
                customersSearched.add(customerSearched);
            }
        }catch (SQLException ex){
            System.out.println("Error with searching");
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
            ErrorPopUp errorPopUp = new ErrorPopUp("Select an item", primaryStage);
        }
    }

    public void updateFromCustomers() {
        try {
            ObservableList<Customers> selectedCustomer, allCustomers;
            allCustomers = tableViewCustomers.getItems();
            selectedCustomer = tableViewCustomers.getSelectionModel().getSelectedItems();
            final int index2=tableViewCustomers.getSelectionModel().getFocusedIndex();
            final int index = selectedCustomer.get(0).getCounter();
            CustomersForm customersForm = new CustomersForm(primaryStage, selectedCustomer.get(0).getName(), selectedCustomer.get(0).getSurname(), selectedCustomer.get(0).getPhone1(), selectedCustomer.get(0).getPhone2(), selectedCustomer.get(0).getAddress());
            customersForm.showForm();
            if (customersForm.isChanged()) {
                final String query=customersForm.getQuery()+" WHERE CustomerID = "+index+";";
                System.out.println(query);
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.execute();
                Customers customersEdited=new Customers(index,customersForm.getName(), customersForm.getSurname(),customersForm.getPhone1(),customersForm.getPhone2(),customersForm.getAddress());
                allCustomers.set(index2,customersEdited);
            } else {
                System.out.println("Not saving changes");
            }
        } catch (Exception ex) {
            System.out.println("Error with updating from database");
            ErrorPopUp errorPopUp = new ErrorPopUp("Select an item", primaryStage);
        }
    }

    public void addToCustomers(){
        try{
            CustomersForm customersForm=new CustomersForm(primaryStage);
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
                Customers customersNew=new Customers(rs.getInt(1),customersForm.getName(),customersForm.getSurname(),customersForm.getPhone1(),((customersForm.getPhone2()==null)?0:customersForm.getPhone2()),customersForm.getAddress());
                ObservableList<Customers>newCustomer=tableViewCustomers.getItems();
                newCustomer.add(customersNew);
                tableViewCustomers.setItems(newCustomer);
            }else{
                System.out.println("Data not added");
            }
        }catch (SQLException e) {
            System.out.println("Error with adding data to database");
            ErrorPopUp errorPopUp=new ErrorPopUp("Error with adding data to database",primaryStage);
        }
    }
}