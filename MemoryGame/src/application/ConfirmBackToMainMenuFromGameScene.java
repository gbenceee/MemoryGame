package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBackToMainMenuFromGameScene {
	static boolean choice;

	public boolean display() {
		Stage window = new Stage();
		Image icon = new Image("file:icon.jpg");
		window.getIcons().add(icon);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setResizable(false);
		window.setMinWidth(250);
		window.setMinHeight(220);
		Label warning = new Label();
		warning.setText("Ha visszalépsz a főmenübe, a jelenlegi játékállás elvész.");
		warning.setTranslateY(-120);
		Label message = new Label();
		message.setText("Biztosan vissza akarsz lépni a főmenübe?");
		message.setTranslateY(-70);
		Button yesButton = new Button("Igen");
		Button noButton = new Button("Nem");
		yesButton.setTranslateY(0);
		yesButton.setPrefSize(70, 35);
		yesButton.setOnAction(e -> {
			choice = true;
			window.close();
		});
		noButton.setTranslateY(80);
		noButton.setPrefSize(70, 35);
		noButton.setOnAction(e -> {
			choice = false;
			window.close();
		});
		VBox layout = new VBox();
		layout.getChildren().addAll(noButton, yesButton, message, warning);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout, 400, 250);
		window.setScene(scene);
		window.showAndWait();
		return choice;
	}
}
