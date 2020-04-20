package com.mechanic.code.main;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ErrorPopUp {
	private Stage stage;
	private boolean added=false;
	private Label labelErrorMsg;

	/*
	Types:
			0: simple error massage
			1: search not found found
			2: deleting checking
	*/


	public ErrorPopUp(int type, Stage primaryStage) {
		stage = new Stage();
		labelErrorMsg=new Label("");
		labelErrorMsg.getStyleClass().add("errorLabel2");
		stage.setTitle("Error");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);
		Button buttonOk = new Button("Exit");
		buttonOk.getStyleClass().add("buttonCancel");
		buttonOk.setOnAction(e -> {
			added = false;
			stage.close();
		});
		Button buttonAdd = new Button("Add");
		buttonAdd.getStyleClass().add("buttonMain");
		VBox box;
		switch (type){
			case 0:{
				HBox boxButtons = new HBox( buttonOk);
				boxButtons.getStyleClass().add("buttonForm");
				box = new VBox(labelErrorMsg, boxButtons);
				break;
			}
			case 1:{
				buttonAdd.setOnAction(e -> {
					added = true;
					stage.close();
				});
				HBox boxButtons = new HBox( buttonOk, buttonAdd);
				boxButtons.getStyleClass().add("buttonForm");
				box = new VBox(labelErrorMsg, boxButtons);
				break;
			}
			case 2:{
				buttonAdd.setText("Delete");
				buttonAdd.setOnAction(e -> {
					added = true;
					stage.close();
				});
				HBox boxButtons = new HBox( buttonOk,buttonAdd);
				boxButtons.getStyleClass().add("buttonForm");
				box = new VBox(labelErrorMsg, boxButtons);
				break;
			}
			default:
				throw new IllegalStateException("Unexpected value: " + type);
		}
		box.setMinWidth(250);
		Scene scene = new Scene(box);
		scene.getStylesheets().add("stylesheets.css");
		stage.setScene(scene);
		stage.setOnCloseRequest(e->added=false);
	}

	public void setErrorMessage(String errorMessage) {
		labelErrorMsg.setText(errorMessage);
	}

	public boolean isAdded() {
		return added;
	}

	public void showError(){
		stage.showAndWait();
	}
}

