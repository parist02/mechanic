package com.mechanic.code;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import static java.lang.System.exit;


public class Form{
    public static Insets padding=new Insets(20,20,20,20);
    public static Font font=Font.font("Arial",FontWeight.NORMAL, FontPosture.REGULAR,18);
    public static Stage stageForm;


    public Form(){
          //Labels
          stageForm=new Stage();
          final Label labelName=new Label("Name:");
          labelName.setPadding(padding);
          labelName.setFont(font);
          final Label labelSurname=new Label("Surname:");
          labelSurname.setPadding(padding);
          labelSurname.setFont(font);
          final Label labelCarNumbers=new Label("Car Numbers:");
          labelCarNumbers.setFont(font);
          labelCarNumbers.setPadding(padding);
          final Label labelTitle=new Label("Form");
          labelTitle.setPadding(padding);
          labelTitle.setFont(Font.font("Arial",FontWeight.BOLD,FontPosture.REGULAR,20));

  
          //TextFields
          final TextField textFieldName=new TextField();
          textFieldName.setPadding(padding);
          textFieldName.setFont(font);
          final TextField textFieldSurname=new TextField();
          textFieldSurname.setPadding(padding);
          textFieldSurname.setFont(font);
          final TextField textFieldCarNumbers=new TextField();
          textFieldCarNumbers.setFont(font);
          textFieldCarNumbers.setPadding(padding);
  
          //Button
          final Button buttonPrint =new Button("Print");
          buttonPrint.setPadding(padding);
           
          final Button buttonCancel=new Button("Cancel");
          //buttonCancel.setPadding(padding);
          buttonCancel.setOnAction(e->{
              stageForm.close();
              exit(0);
          });
          buttonPrint.setOnAction(e->{
              final InvoicePrintPreview printPreview=new InvoicePrintPreview(textFieldName.getText(),textFieldSurname.getText(),textFieldCarNumbers.getText());
              printPreview.stagePrint.show();
          });
  
          //Scene control
          final GridPane grid=new GridPane();
          grid.setPadding(padding);
          grid.setHgap(20);
          grid.setVgap(20);
          grid.add(labelTitle,0,0,2,1);
          grid.add(labelName,0,1);
          grid.add(textFieldName,1,1);
          grid.add(labelSurname,0,2);
          grid.add(textFieldSurname,1,2);
          grid.add(labelCarNumbers,0,3);
          grid.add(textFieldCarNumbers,1,3);
          final HBox box=new HBox(buttonPrint,buttonCancel);
          box.setSpacing(20);
          box.setPadding(padding);
          buttonCancel.setAlignment(Pos.BASELINE_RIGHT);
          grid.add(box,0,4,2,1);
          grid.setGridLinesVisible(false);
        final Scene scene=new Scene(grid);
        scene.getStylesheets().add("com.mechanic/style.css");
        stageForm.setScene(scene);
        stageForm.setTitle("Form");
    }

    public void showForm(){
        stageForm.show();
    }

}