package com.mechanic.code.main;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ErrorPopUp {
	private static Insets padding = new Insets(10, 10, 10, 10);
	private Stage stage;
	private boolean added=false;
	private Label labelErrorMsg = new Label();

	/*
	Types:
			0: simple error massage
			1: search not found found
			2: deleting checking
	*/


	public ErrorPopUp(int type, Stage primaryStage) {
		stage = new Stage();
		stage.setTitle("Error");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);
		labelErrorMsg.setStyle("-fx-font-size: 14;" +
				"-fx-font-weight: bold;" +
				"-fx-font-family: Arial");
		Button buttonOk = new Button("Exit");
		buttonOk.setStyle("-fx-font-size: 12;" +
				"-fx-font-family: Arial;");
		buttonOk.setOnAction(e -> {
			added = false;
			stage.close();
		});
		Button buttonAdd = new Button("Add");
		buttonAdd.setStyle("-fx-font-size: 12;" +
				"-fx-font-family: Arial;");
		VBox box;
		switch (type){
			case 0:{
				box = new VBox(labelErrorMsg, buttonOk);
				break;
			}
			case 1:{
				buttonAdd.setOnAction(e -> {
					added = true;
					stage.close();
				});
				HBox boxButtons = new HBox(buttonAdd, buttonOk);
				boxButtons.setSpacing(50);
				box = new VBox(labelErrorMsg, boxButtons);
				break;
			}
			case 2:{
				buttonAdd.setText("Delete");
				buttonAdd.setOnAction(e -> {
					added = true;
					stage.close();
				});
				HBox boxButtons = new HBox(buttonAdd, buttonOk);
				boxButtons.setSpacing(50);
				box = new VBox(labelErrorMsg, boxButtons);
				break;
			}
			default:
				throw new IllegalStateException("Unexpected value: " + type);
		}
		box.setPadding(padding);
		box.setMinHeight(50);
		box.setMinWidth(200);
		box.setSpacing(20);
		Scene scene = new Scene(box);
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

