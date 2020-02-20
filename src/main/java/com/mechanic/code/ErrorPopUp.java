package com.mechanic.code;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ErrorPopUp {
	public static Insets padding = new Insets(10, 10, 10, 10);

	ErrorPopUp(String errorMessage, Stage primaryStage) {


		Stage stage = new Stage();
		stage.setTitle("Error");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);
//			FileInputStream inputStream = new FileInputStream("@/images/error.png");
//			Image image = new Image(inputStream);
//			ImageView imageView = new ImageView(image);
		Label labelErrorMsg = new Label(errorMessage);

		labelErrorMsg.setStyle("-fx-font-size: 14;"+
				"-fx-font-weight: bold;"+
				"-fx-font-family: Arial");
		Button buttonOk = new Button("Ok");
		buttonOk.setStyle("-fx-font-size: 12;"+
				"-fx-font-family: Arial;");
		buttonOk.setOnAction(e -> {
			stage.close();
		});
		//buttonOk.setAlignment(Pos.CENTER_RIGHT);
		VBox box = new VBox(labelErrorMsg, buttonOk);
		box.setPadding(padding);
		box.setMinHeight(50);
		box.setMinWidth(200);
		box.setSpacing(20);
		Scene scene = new Scene(box);
		stage.setScene(scene);

		stage.show();

	}
}

