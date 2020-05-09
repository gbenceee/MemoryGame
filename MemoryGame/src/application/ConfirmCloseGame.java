package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmCloseGame {
	static boolean choice;

	public boolean display() {
		Stage window = new Stage();
		Image icon = new Image("file:icon.jpg");
		window.getIcons().add(icon);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setResizable(false);
		window.setMinWidth(250);
		window.setMinHeight(220);
		Label message = new Label();
		message.setText("Biztosan ki akarsz lépni a játékból?");
		message.setTranslateY(-80);
		Button yesButton = new Button("Igen");
		Button noButton = new Button("Nem");
		yesButton.setTranslateY(-20);
		yesButton.setPrefSize(60, 30);
		yesButton.setOnAction(e -> {
			choice = true;
			window.close();
		});
		noButton.setTranslateY(50);
		noButton.setPrefSize(60, 30);
		noButton.setOnAction(e -> {
			choice = false;
			window.close();
		});
		VBox layout = new VBox();
		layout.getChildren().addAll(noButton, yesButton, message);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout, 300, 150);
		window.setScene(scene);
		window.showAndWait();
		return choice;
	}
}
