package com.mechanic.code;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class PrintPreview {
    public Stage stagePrint=new Stage();
    public static javafx.geometry.Insets padding = new Insets(20, 20, 20, 20);
    public static javafx.scene.text.Font font = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 18);
    private static double height= Paper.A4.getHeight();
    private static double width=Paper.A4.getWidth();


    public PrintPreview(String name, String surname, String carNumbers) {

        //1st Part
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
        labelTitle.setId("title");

        Label labelCompany7=new Label("Invoice No.:");
    
        Label labelCompany8=new Label("123456");
        Label labelCompany9=new Label("Date:");
        Label labelCompany10=new Label("D/MMM/YYYY");
//        VBox boxCompany2=new VBox(labelCompany7,labelCompany8);
//        boxCompany2.setId("label1");
//        boxCompany2.setSpacing(5);
        GridPane gridCompany=new GridPane();
        gridCompany.add(labelCompany7,0,0);
        gridCompany.add(labelCompany8,1,0);
        gridCompany.add(labelCompany9,0,1);
        gridCompany.add(labelCompany10,1,1);
        gridCompany.setVgap(5);
        gridCompany.setHgap(5);
        gridCompany.setId("grid");


        //2nd Part
        Label labelCustomer1=new Label("Customer Account:");
        Label labelCustomer111=new Label("12345");
        Label labelCustomer2=new Label("Name/Comp:");
        Label labelCustomer222=new Label(name+" "+surname);
        Label labelCustomer3=new Label("Auto No.:");
        Label labelCustomer333=new Label(carNumbers);
        Label labelCustomer4=new Label("Auto Make.:");
        Label labelCustomer444=new Label("TEST TEST TEST");
        Label labelCustomer5=new Label("Chassis No.");
        Label labelCustomer555=new Label("123456789");

        GridPane gridCustomer1=new GridPane();
        gridCustomer1.add(labelCustomer1,0,0);
        gridCustomer1.add(labelCustomer111,1,0);
        gridCustomer1.add(labelCustomer2,0,1);
        gridCustomer1.add(labelCustomer222,1,1);
        gridCustomer1.add(labelCustomer3,0,2);
        gridCustomer1.add(labelCustomer333,1,2);
        gridCustomer1.add(labelCustomer4,0,3);
        gridCustomer1.add(labelCustomer444,1,3);
        gridCustomer1.add(labelCustomer5,0,4);
        gridCustomer1.add(labelCustomer555,1,4);
        gridCustomer1.setId("grid");

//        VBox boxCustomer1=new VBox(labelCustomer1,labelCustomer2,labelCustomer3,labelCustomer4,labelCustomer5);
//        boxCustomer1.setSpacing(5);
//        boxCustomer1.setId("label1");

        Label labelCustomer6=new Label("Date In:");
        Label labelCustomer66=new Label("DD/MMM/YYY");
        Label labelCustomer7=new Label("Date Out:");
        Label labelCustomer77=new Label("DD/MMM/YYY");

        GridPane gridCustomer2=new GridPane();
        gridCustomer2.add(labelCustomer6,0,0);
        gridCustomer2.add(labelCustomer66,1,0);
        gridCustomer2.add(labelCustomer7,0,1);
        gridCustomer2.add(labelCustomer77,1,1);
        gridCustomer2.setId("grid");

//        VBox boxCustomer2=new VBox(labelCustomer6,labelCustomer7);
//        boxCustomer2.setSpacing(5);
//        boxCustomer2.setId("label1");

        Label labelCustomer8=new Label("Mileage:");
        Label labelCustomer8_1=new Label("123456KM");
        Label labelCustomer9=new Label("First Oil:");
        Label labelCustomer9_1=new Label("123456KM");
        Label labelCustomer10=new Label("Next Oil:");
        Label labelCustomer10_1=new Label("123456KM");
        Label labelCustomer11=new Label("Next Service:");
        Label labelCustomer11_1=new Label("123456KM");

        GridPane gridCustomer3=new GridPane();
        gridCustomer3.add(labelCustomer8,0,0);
        gridCustomer3.add(labelCustomer8_1, 1, 0);
        gridCustomer3.add(labelCustomer9,0,1);
        gridCustomer3.add(labelCustomer9_1, 1, 1);
        gridCustomer3.add(labelCustomer10,0,2);
        gridCustomer3.add(labelCustomer10_1, 1, 2);
        gridCustomer3.add(labelCustomer11,0,3);
        gridCustomer3.add(labelCustomer11_1, 1, 3);
        gridCustomer3.setId("grid");


        // VBox boxCustomer3=new VBox(labelCustomer8,labelCustomer9,labelCustomer10,labelCustomer11);
        // boxCustomer3.setSpacing(5);
        // boxCustomer3.setId("label1");

        //3rd part


        //4th part
        Label labelComments=new Label("Comments:");
        labelComments.setId("label2");
        TextArea textAreaComments=new  TextArea();
        textAreaComments.setPrefHeight(70);
        textAreaComments.setPrefWidth(220);
        Label labelSignature=new Label("\tIssued By");
        Label labelDots1=new Label("..........................................");
        VBox boxComments=new VBox(labelComments,textAreaComments,labelSignature,labelDots1);
        boxComments.setId("label2");
        boxComments.setSpacing(5);

        Label labelTotal1=new Label("Amount:   €1234.123");
        Label labelTotal2=new Label("Discount: €1234.123");
        Label labelTotal3=new Label("Net:      €1234.123");
        Label labelTotal4=new Label("Vat:      €1234.123");
        Label labelTotal5=new Label("GTotal:   €1234.123");
        Label labelTotal6=new Label("\tReceived By");
        Label labelDots2=new Label("..........................................");
        VBox boxTotal=new VBox(labelTotal1,labelTotal2,labelTotal3,labelTotal4,labelTotal5,labelTotal6,labelDots2);
        boxTotal.setSpacing(5);
        boxTotal.setId("label2");


        //Scene Control
        GridPane grid=new GridPane();
        grid.add(boxCompany,0,0);
        grid.add(labelTitle,1,0);
        grid.add(gridCompany,2,0);
        grid.add(gridCustomer1,0,1);
        grid.add(gridCustomer2,1,1);
        grid.add(gridCustomer3,2,1);
        grid.add(boxComments,0,2,2,1);
        grid.add(boxTotal,2,2);
        grid.setGridLinesVisible(false);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPrefWidth(width);
        grid.setPrefHeight(height);
        grid.setAlignment(Pos.TOP_CENTER);
        Print print=new Print(grid);
        Button buttonPrint=new Button("Print");
        buttonPrint.setOnAction(e->{
            print.print();
        });

        VBox boxMain=new VBox(grid,buttonPrint);
        Scene scene=new Scene(boxMain);
        scene.getStylesheets().add("stylesheets.css");
        stagePrint.setScene(scene);

    }
}