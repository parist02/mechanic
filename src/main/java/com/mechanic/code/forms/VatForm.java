package com.mechanic.code.forms;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VatForm {
	private Stage stageForm;
	private TextField textFieldVat;
	private Button buttonOk, buttonCancel;
	private Label labelEmpty;
	private float vatBefore;
	private Float vat=null;
	private boolean changed=false;


	public VatForm(Stage primaryStage, float fpa){
		vatBefore=fpa;
		stageForm = new Stage();
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		stageForm.setTitle("Form");
		Font font = Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 15);
		Label labelTitle = new Label("Mechanic details");
		labelTitle.setTextFill(Color.BLACK);
		labelTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		Label labelFpa = new Label("VAT:");
		labelFpa.setFont(font);
		labelFpa.setTextFill(Color.BLACK);
		labelEmpty = new Label("");
		labelEmpty.setFont(font);
		labelEmpty.setTextFill(Color.FIREBRICK);
		textFieldVat=new TextField(String.valueOf(fpa));
		buttonOk = new Button("Ok");
		buttonCancel = new Button("Cancel");
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelFpa, 0, 1);
		grid.add(textFieldVat, 1, 1);
		grid.add(labelEmpty, 0, 2, 2, 1);
		grid.add(buttonOk, 0, 3);
		grid.add(buttonCancel, 1, 3);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setHgap(20);
		grid.setVgap(15);
		Scene sceneForm = new Scene(grid);
		stageForm.setScene(sceneForm);
		buttonCancel.setOnAction(e->stageForm.close());
		buttonOk.setOnAction(actionEvent -> {
			boolean error=false;
			try {
				vat = Float.parseFloat(textFieldVat.getText().replaceAll("[^0-9.]", ""));
			}catch (Exception ex){
				error=true;
			}
			if(error){
				labelEmpty.setText("Please fill all the details");
			}else if(vat!=vatBefore){
				changed=true;
			}
			stageForm.close();
		});
	}

	public boolean isChanged() {
		return changed;
	}

	public Float getVat() {
		return vat;
	}
	public void showForm(){
		stageForm.showAndWait();
	}
}
