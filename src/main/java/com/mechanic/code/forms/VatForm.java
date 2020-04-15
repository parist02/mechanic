package com.mechanic.code.forms;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
		Label labelTitle = new Label("VAT");
		labelTitle.getStyleClass().add("formTitle");
		Label labelFpa = new Label("New Vat:");
		labelFpa.getStyleClass().add("formLabel");
		labelEmpty = new Label("");
		labelEmpty.getStyleClass().add("errorLabel");
		textFieldVat=new TextField(String.valueOf(fpa));
		buttonOk = new Button("Ok");
		buttonOk.getStyleClass().add("buttonMain");
		buttonCancel = new Button("Cancel");
		buttonCancel.getStyleClass().add("buttonCancel");
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelFpa, 0, 1);
		grid.add(textFieldVat, 1, 1);
		grid.add(labelEmpty, 0, 2, 2, 1);
		HBox buttonBox = new HBox(buttonCancel,buttonOk);
		buttonBox.getStyleClass().add("buttonForm");
		grid.add(buttonBox, 0, 3,2,1);
		grid.getStyleClass().add("formGrid");
		Scene sceneForm = new Scene(grid);
		sceneForm.getStylesheets().add("stylesheets.css");
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
