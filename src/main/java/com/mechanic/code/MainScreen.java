package com.mechanic.code;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import static java.lang.System.exit;

public class MainScreen extends Application{

    public static Insets padding=new Insets(20,20,20,20);
    public static Font font=Font.font("Arial",FontWeight.NORMAL, FontPosture.REGULAR,18);

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("The application is starting");
    }

    @Override
    public void start(final Stage stage) {
        TabPane tabPane = new TabPane();
        Form form=new Form();
    
        
        Button buttonForm=new Button("Show form");
        buttonForm.setPadding(padding);
        buttonForm.setFont(font);
        
        Label labelInvoice=new Label("Press the button to show the print form");
        labelInvoice.setPadding(padding);
        labelInvoice.setFont(font);
        VBox boxInvoice=new VBox(buttonForm,labelInvoice);
        boxInvoice.setPadding(padding);
        buttonForm.setOnAction(e->{
            form.showForm();
        });

        Tab tab1=new Tab("Customers",new Label("Showing all customers"));
        Tab tab2=new Tab("Parts",new Label("Showing all parts"));
        Tab tab3=new Tab("Invoices", boxInvoice);

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        Scene scene=new Scene(tabPane);
        stage.setScene(scene);
        stage.setTitle("MainScreen");
        stage.show();

    }


}