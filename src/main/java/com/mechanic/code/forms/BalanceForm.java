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

public class BalanceForm {
	private Stage stageForm;
	private float balance,oldbalance;
	private TextField textFieldBalance;
	private Button buttonOk, buttonCancel;
	private final Font font = Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 15);
	private boolean changed=false;

	private static final Insets padding = new Insets(10, 10, 10, 10);

	public BalanceForm(Stage primaryStage, float palioBalance){
		oldbalance=palioBalance;
		stageForm=new Stage();
		stageForm.setTitle("Balance");
		stageForm.initModality(Modality.WINDOW_MODAL);
		stageForm.initOwner(primaryStage);
		Label labelBalance=new Label("New Balance: ");
		labelBalance.setFont(font);
		labelBalance.setTextFill(Color.BLACK);
		Label labelEmpty = new Label("");
		labelEmpty.setFont(font);
		labelEmpty.setTextFill(Color.FIREBRICK);
		textFieldBalance=new TextField(String.valueOf(oldbalance));
		buttonOk = new Button("Ok");
		buttonOk.setPadding(padding);
		buttonCancel = new Button("Cancel");
		buttonCancel.setPadding(padding);
		GridPane gridPane=new GridPane();
		gridPane.add(labelBalance, 0, 0);
		gridPane.add(textFieldBalance, 1, 0);
		gridPane.add(labelEmpty, 0, 1, 2, 1);
		gridPane.add(buttonOk, 0, 2);
		gridPane.add(buttonCancel, 1, 2);
		gridPane.setPadding(padding);
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
