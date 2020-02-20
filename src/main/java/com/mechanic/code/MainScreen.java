package com.mechanic.code;

import com.mechanic.code.databaseClasses.Customers;
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
    public TableView<Customers> tableView;
    public static Insets padding = new Insets(20, 20, 20, 20);
    public static Font font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 18);
    public Connection connection;

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
    public void start(final Stage stage) {

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
        counterColumn.setMinWidth(50);
        counterColumn.setCellValueFactory(new PropertyValueFactory<>("counter"));

        TableColumn<Customers, Integer> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customers, Integer> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setMinWidth(100);
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<Customers, Integer> phone1Column = new TableColumn<>("Phone1");
        phone1Column.setMinWidth(75);
        phone1Column.setCellValueFactory(new PropertyValueFactory<>("phone1"));

        TableColumn<Customers, Integer> phone2Column = new TableColumn<>("Phone2");
        phone2Column.setMinWidth(75);
        phone2Column.setCellValueFactory(new PropertyValueFactory<>("phone2"));

        TableColumn<Customers, Integer> addressColumn = new TableColumn<>("Address");
        addressColumn.setMinWidth(150);
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableView<Customers> tableView = new TableView<>();

        tableView.getColumns().add(counterColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(surnameColumn);
        tableView.getColumns().add(phone1Column);
        tableView.getColumns().add(phone2Column);
        tableView.getColumns().add(addressColumn);
        tableView.setItems(importFromCustomers());
        tableView.setPadding(padding);
        Label labelCustomerTitle= new Label("Showing all customers");
        VBox boxCustomers = new VBox(tableView,labelCustomerTitle);
        boxCustomers.setPadding(padding);
        boxCustomers.setSpacing(20);
        Tab tab1 = new Tab("Customers",boxCustomers);
        Tab tab2 = new Tab("Parts", new Label("Showing all parts"));
        Tab tab3 = new Tab("Invoices", boxInvoice);

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane);
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.setTitle("MainScreen");
        stage.show();

    }

    public ObservableList<Customers>importFromCustomers() {
        ObservableList<Customers>customerData= FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from customers");
            System.out.println("Printing table...\n");
            Customers customer;
            while (rs.next()) {
                customer = new Customers(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getInt(5),rs.getString(6));
                customerData.add(customer);
            }
            connection.close();
        }catch (SQLException e){
            System.out.println("Error with getting data");
        }
        return customerData;
    }


}