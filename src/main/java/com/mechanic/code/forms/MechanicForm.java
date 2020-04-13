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

public class MechanicForm {
	private Stage stageForm;
	private String name, surname;
	private Integer mechanicID;
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
		Font font = Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 15);
		//Labels
		Label labelName = new Label("Name:");
		labelName.setFont(font);
		labelName.setTextFill(Color.BLACK);
		Label labelSurname = new Label("Surname:");
		labelSurname.setFont(font);
		labelSurname.setTextFill(Color.BLACK);
		labelEmpty = new Label("");
		labelEmpty.setFont(font);
		labelEmpty.setTextFill(Color.FIREBRICK);
		labelEmpty.setPadding(new Insets(10, 10, 10, 10));
		Label labelTitle = new Label("Mechanic details");
		labelTitle.setTextFill(Color.BLACK);
		labelTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		labelTitle.setPadding(new Insets(10, 10, 10, 10));
		//Text fields
		textFieldName = new TextField();
		textFieldSurname = new TextField();
		//
		buttonOk = new Button("Ok");
		buttonCancel = new Button("Cancel");
		//Grid control
		GridPane grid = new GridPane();
		grid.add(labelTitle, 0, 0, 2, 1);
		grid.add(labelName, 0, 1);
		grid.add(textFieldName, 1, 1);
		grid.add(labelSurname, 0, 2);
		grid.add(textFieldSurname, 1, 2);
		grid.add(labelEmpty, 0, 3, 2, 1);
		grid.add(buttonOk, 0, 4);
		grid.add(buttonCancel, 1, 4);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setHgap(20);
		grid.setVgap(15);
		//Scene control
		Scene sceneForm = new Scene(grid);
		//sceneForm.getStylesheets().add("style.css");
		stageForm.setScene(sceneForm);
		buttonOk.getStyleClass().add("button2");
	}

	public Integer getMechanicID() {
		return mechanicID;
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

