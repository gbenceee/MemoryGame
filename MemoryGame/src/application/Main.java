package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage window;
	public static Scene mainMenu;

	@Override
	public void start(Stage primaryStage) {
		try {
			window = primaryStage;
			Image icon = new Image("file:icon.jpg");
			initializeWindow(icon);
			Button newGame = initializeNewGameButton();
			Button exit = initializeExitButton();
			VBox layout = new VBox();
			layout.setAlignment(Pos.CENTER);
			layout.getChildren().addAll(newGame, exit);
			mainMenu = new Scene(layout);
			window.setScene(mainMenu);
			window.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getWindow() {
		return window;
	}

	public static Scene getMainMenuScene() {
		return mainMenu;
	}

	public void closeProgram() {
		if (new ConfirmCloseGame().display()) {
			window.close();
		}
	}

	private void initializeWindow(Image icon) {
		window.getIcons().add(icon);
		window.setTitle("Memória");
		window.setMinWidth(1000);
		window.setMinHeight(800);
		window.setWidth(1000);
		window.setHeight(800);
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
	}

	private Button initializeNewGameButton() {
		Button newGame = new Button("Új játék");
		newGame.setPrefSize(150, 40);
		newGame.setOnAction(e -> {
			BorderPane root = null;
			try {
				root = (BorderPane) FXMLLoader.load(getClass().getResource("InGameScene.fxml"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Scene game = new Scene(root, 700, 750);
			window.setScene(game);
		});
		return newGame;
	}

	private Button initializeExitButton() {
		Button exit = new Button("Kilépés");
		exit.setPrefSize(150, 40);
		exit.setTranslateY(10);
		exit.setOnAction(e -> {
			e.consume();
			closeProgram();
		});
		return exit;
	}

}
