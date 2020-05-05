package com.mechanic.code.forms;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BalanceForm {
	private Stage stageForm;
	private float balance,oldbalance;
	private TextField textFieldBalance;
	private Button buttonOk, buttonCancel;
	private boolean changed=false;

	public BalanceForm(Stage primaryStage, float palioBalance){
		oldbalance=palioBalance;
		stageForm=new Stage();
		stageForm.setTitle("Balance");
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		Label labelTitle = new Label("Balance");
		labelTitle.getStyleClass().add("formTitle");
		Label labelBalance=new Label("New Balance: ");
		labelBalance.getStyleClass().add("formLabel");
		Label labelEmpty = new Label("");
		labelEmpty.getStyleClass().add("errorLabel");
		textFieldBalance=new TextField(String.valueOf(oldbalance));
		buttonOk = new Button("Ok");
		buttonOk.getStyleClass().add("buttonMain");
		buttonCancel = new Button("Cancel");
		buttonCancel.getStyleClass().add("buttonCancel");
		GridPane gridPane=new GridPane();
		gridPane.add(labelTitle, 0, 0, 2, 1);
		gridPane.add(labelBalance, 0, 1);
		gridPane.add(textFieldBalance, 2, 1);
		gridPane.add(labelEmpty, 0, 2, 2, 1);
		HBox boxButtons=new HBox(buttonCancel,buttonOk);
		boxButtons.getStyleClass().add("buttonForm");
		gridPane.add(boxButtons, 1, 3,2,1);
		buttonOk.setOnAction(actionEvent -> {
			String balanceStr=textFieldBalance.getText().replaceAll("[^0-9.]", "");
			textFieldBalance.setText(balanceStr);
			if(!balanceStr.equals("")){
				balance=Float.parseFloat(balanceStr);
				changed=true;
				stageForm.close();
			}else{
				labelEmpty.setText("Please fill all the details");
			}
		});
		buttonCancel.setOnAction(actionEvent ->stageForm.close());
		Scene scene=new Scene(gridPane);
		scene.getStylesheets().add("stylesheets.css");
		stageForm.setScene(scene);
	}

	public boolean isChanged() {
		return changed;
	}
	public void showForm(){
		stageForm.showAndWait();
	}

	public float getBalance() {
		return balance;
	}

	public float getDifferenceInBalance(){
		return (oldbalance-balance);
	}
}
