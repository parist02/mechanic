package com.mechanic.code.forms;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MechanicForm {
	private Stage stageForm;
	private String name, surname;
	private TextField textFieldName;
	private TextField textFieldSurname;
	private Button buttonOk, buttonCancel;
	private Label labelEmpty;
	private boolean changed=false;
	private String nameBefore,surnameBefore;
	private String query="UPDATE mechanics SET ";
	private String queryAdd="INSERT INTO mechanics (MechanicID, Name, Surname) VALUES (NULL";

	public MechanicForm(Stage primaryStage) {
		mechanicFormInitializer();
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		buttonCancel.setOnAction(e ->stageForm.close());
		buttonOk.setOnAction(e -> {
			name=textFieldName.getText();
			name=name.replaceAll("[^a-zA-Z0-9]", "");
			surname=textFieldSurname.getText();
			surname=surname.replaceAll("[^a-zA-Z0-9]", "");
			if (name.equals("") || surname.equals("")) {
				labelEmpty.setText("Please fill all the details");
			} else {
				changed=true;
				queryAdd = queryAdd + ", '" + name + "' , '" + surname + "');";
				stageForm.close();
			}
		});
	}



	public MechanicForm(Stage primaryStage, String onoma,String epitheto) {
		nameBefore=onoma;
		surnameBefore=epitheto;
		mechanicFormInitializer();
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		textFieldName.setText(onoma);
		textFieldSurname.setText(epitheto);
		//Event Handler
		buttonCancel.setOnAction(e->stageForm.close());
		buttonOk.setOnAction(e->{
			name=textFieldName.getText().replaceAll("[^a-zA-Z0-9]", "");
			surname=textFieldSurname.getText().replaceAll("[^a-zA-Z0-9]", "");
			if (name.equals("")||surname.equals("")){
				labelEmpty.setText("Please fill all the details");
			}else {
				if (!name.equals(nameBefore)){
					if(changed){
						query=query+", Name = '"+name+"'";
					}else{
						changed=true;
						query=query+" Name = '"+name+"'";
					}
				}
				if(!surname.equals(surnameBefore)){
					if(changed){
						query=query+", Surname = '"+surname+"'";
					}else{
						changed=true;
						query=query+" Surname = '"+surname+"'";
					}
				}
				stageForm.close();
			}
		});

	}

	private void mechanicFormInitializer() {
		stageForm = new Stage();
		stageForm.setTitle("Form");
		//Labels
		Label labelName = new Label("Name:");
		labelName.getStyleClass().add("formLabel");
		Label labelSurname = new Label("Surname:");
		labelSurname.getStyleClass().add("formLabel");
		labelEmpty = new Label("");
		labelEmpty.getStyleClass().add("errorLabel");
		Label labelTitle = new Label("Mechanic details");
		labelTitle.getStyleClass().add("formTitle");
		//Text fields
		textFieldName = new TextField();
		textFieldSurname = new TextField();
		//
		buttonOk = new Button("Ok");
		buttonOk.getStyleClass().add("buttonMain");
		buttonCancel = new Button("Cancel");
		buttonCancel.getStyleClass().add("buttonCancel");
		//Grid control
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelName, 0, 1);
		grid.add(textFieldName, 1, 1);
		grid.add(labelSurname, 0, 2);
		grid.add(textFieldSurname, 1, 2);
		grid.add(labelEmpty, 0, 3, 2, 1);
		HBox buttonBox = new HBox(buttonCancel,buttonOk);
		buttonBox.getStyleClass().add("buttonForm");
		grid.add(buttonBox, 0, 4,2,1);
		grid.getStyleClass().add("formGrid");
		//Scene control
		Scene sceneForm = new Scene(grid);
		sceneForm.getStylesheets().add("stylesheets.css");
		stageForm.setScene(sceneForm);
		buttonOk.getStyleClass().add("button2");
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public boolean isChanged() {
		return changed;
	}

	public String getQuery() {
		return query;
	}

	public String getQueryAdd() {
		return queryAdd;
	}

	public void showForm(){
		stageForm.showAndWait();
	}
}

