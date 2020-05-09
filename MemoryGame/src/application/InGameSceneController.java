package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InGameSceneController implements Initializable {
	public List<Button> buttons;
	public Label textOfCurrentPlayer;
	public Label scoreOfBluePlayerAsLabel;
	public Label scoreOfRedPlayerAsLabel;
	public FlowPane gameLayout;
	public Button backToMainMenuInGameButton;
	CurrentPlayer currentPlayer = CurrentPlayer.RED;
	EventHandler<MouseEvent> event = MouseEvent::consume;
	int numberOfChosenCards = 0;
	int numberOfPairs = 0;
	int indexOfFirstButtonClicked;
	int indexOfSecondButtonClicked;
	int scoreOfBluePlayer = 0;
	int scoreOfRedPlayer = 0;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<Integer> numbers = fillArrayListWithNumbersNumbers();
		backToMainMenuInGameButton.setOnAction(e -> {
			e.consume();
			if (new ConfirmBackToMainMenuFromGameScene().display()) {
				Main.getWindow().setScene(Main.getMainMenuScene());
			}
		});
		Collections.shuffle(numbers);
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setText(String.valueOf(numbers.get(i)));
			buttons.get(i).setPrefSize(60, 60);
			setButtonStyle(i, "-fx-background-color: white;");
			buttons.get(i).setTextFill(Color.WHITE);
			buttons.get(i).setOnAction(e -> {
				Button clickedButton = (Button) e.getSource();
				numberOfChosenCards++;
				if (numberOfChosenCards <= 1) {
					indexOfFirstButtonClicked = buttons.indexOf(clickedButton);
					setButtonUnavailableToUser(indexOfFirstButtonClicked);
					setButtonStyle(indexOfFirstButtonClicked, "-fx-background-color: blue;");
				} else if (numberOfChosenCards >= 2) {
					indexOfSecondButtonClicked = buttons.indexOf(clickedButton);
					setButtonUnavailableToUser(indexOfSecondButtonClicked);
					setButtonStyle(indexOfSecondButtonClicked, "-fx-background-color: blue;");
				}
				if (isTwoCardsRevealed()) {
					if (!hasSameValues()) {
						Thread setColorToWhite = new Thread(() -> {
							setLayoutUnavailableToUser();
							try {
								Thread.sleep(1300);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							setButtonStyle(indexOfFirstButtonClicked, "-fx-background-color: white;");
							setButtonStyle(indexOfSecondButtonClicked, "-fx-background-color: white;");
							setButtonAvailableToUser(indexOfFirstButtonClicked);
							setButtonAvailableToUser(indexOfSecondButtonClicked);
							setLayoutAvailableToUser();
						});
						setColorToWhite.start();
						changeCurrentPlayer();
						numberOfChosenCards = 0;
					} else {
						setButtonStyle(indexOfFirstButtonClicked, "-fx-background-color: lightgreen;");
						setButtonStyle(indexOfSecondButtonClicked, "-fx-background-color: lightgreen;");
						buttons.get(indexOfFirstButtonClicked).setTextFill(Color.BLACK);
						buttons.get(indexOfSecondButtonClicked).setTextFill(Color.BLACK);
						numberOfPairs++;
						if (currentPlayer == CurrentPlayer.RED) {
							scoreOfRedPlayerAsLabel.setText(String.valueOf(++scoreOfRedPlayer));
						} else {
							scoreOfBluePlayerAsLabel.setText(String.valueOf(++scoreOfBluePlayer));
						}
//						buttons.get(indexOfFirstButtonClicked).setVisible(false);// opcionális
//						buttons.get(indexOfSecondButtonClicked).setVisible(false);// opcionális
						buttons.get(indexOfFirstButtonClicked).setDisable(true);
						buttons.get(indexOfSecondButtonClicked).setDisable(true);
						numberOfChosenCards = 0;
					}
				}
				if (isEveryPairFound()) {
					Image icon = new Image("file:icon.jpg");
					Stage gameOverWindow = initializeGameOverWindow(icon);
					VBox layout = initializeVBoxLayout();
					Label gameIsOver = initializeLabel("Vége a játéknak!", 100, 30, -70);
					Label result = initializeLabel(null, 100, 30, -70);
					Label message = initializeLabel(null, 200, 70, -40);
					Button backToMainMenu = initializeBackToMainMenuButton(gameOverWindow);
					if (isDraw()) {
						result.setText("Az eredmény:");
						Label draw = initializeLabel("Döntetlen.", 150, 30, -60);
						message.setText("Mindkét játékosnak " + String.valueOf(scoreOfBluePlayer) + " pontja lett.");
						layout.getChildren().addAll(gameIsOver, result, draw, message, backToMainMenu);
					} else {
						result.setText("A győztes:");
						Label theWinnerIs = initializeLabel(null, 150, 30, -60);
						if (isBluePlayerWon()) {
							initializeLabelToWinner(theWinnerIs, "Kék", Color.BLUE, scoreOfBluePlayer);
						} else {
							initializeLabelToWinner(theWinnerIs, "Piros", Color.RED, scoreOfRedPlayer);
						}
						message.setText("Gratulálok!");
						message.setStyle("-fx-font-size: 30;");
						layout.getChildren().addAll(gameIsOver, result, theWinnerIs, message, backToMainMenu);
					}
					gameOverWindow.setScene(new Scene(layout));
					gameOverWindow.show();
				}
			});
		}
	}

	private boolean isBluePlayerWon() {
		return scoreOfBluePlayer > scoreOfRedPlayer;
	}

	private boolean hasSameValues() {
		return buttons.get(indexOfFirstButtonClicked).getText()
				.equals(buttons.get(indexOfSecondButtonClicked).getText());
	}

	private boolean isDraw() {
		return scoreOfBluePlayer == scoreOfRedPlayer;
	}

	private boolean isEveryPairFound() {
		return numberOfPairs >= 24;
	}

	private Label initializeLabel(String text, double width, double height, double y) {
		Label label = new Label(text);
		label.setPrefWidth(width);
		label.setPrefHeight(height);
		label.setTranslateY(y);
		label.setAlignment(Pos.CENTER);
		return label;
	}

	private Button initializeBackToMainMenuButton(Stage gameOverWindow) {
		Button backToMainMenu = new Button("Vissza a főmenübe");
		backToMainMenu.setMinSize(150, 40);
		backToMainMenu.setOnAction(ev -> {
			Main.getWindow().setScene(Main.getMainMenuScene());
			gameOverWindow.close();
		});
		return backToMainMenu;
	}

	private Stage initializeGameOverWindow(Image icon) {
		Stage gameOverWindow = new Stage();
		gameOverWindow.getIcons().add(icon);
		gameOverWindow.initModality(Modality.APPLICATION_MODAL);
		gameOverWindow.initStyle(StageStyle.UNDECORATED);
		gameOverWindow.setMinWidth(400);
		gameOverWindow.setMinHeight(400);
		return gameOverWindow;
	}

	private void changeCurrentPlayer() {
		if (currentPlayer == CurrentPlayer.RED) {
			textOfCurrentPlayer.setText("Kék játékos következik.");
			textOfCurrentPlayer.setTextFill(Color.BLUE);
			currentPlayer = CurrentPlayer.BLUE;
		} else {
			textOfCurrentPlayer.setText("Piros játékos következik.");
			currentPlayer = CurrentPlayer.RED;
			textOfCurrentPlayer.setTextFill(Color.RED);
		}
	}

	private VBox initializeVBoxLayout() {
		VBox layout = new VBox();
		layout.setPrefHeight(400);
		layout.setPrefWidth(400);
		layout.setAlignment(Pos.CENTER);
		return layout;
	}

	private void setButtonStyle(int index, String style) {
		buttons.get(index).setStyle(style);
	}

	private boolean isTwoCardsRevealed() {
		return numberOfChosenCards >= 2;
	}

	private void initializeLabelToWinner(Label theWinnerIs, String winner, Color color, int scoreOfWinner) {
		theWinnerIs.setText(winner + " játékos " + String.valueOf(scoreOfWinner) + " ponttal.");
		theWinnerIs.setTextFill(color);
	}

	private void setButtonUnavailableToUser(int index) {
		buttons.get(index).addEventFilter(MouseEvent.ANY, event);
	}

	private void setButtonAvailableToUser(int index) {
		buttons.get(index).removeEventFilter(MouseEvent.ANY, event);
	}

	private void setLayoutUnavailableToUser() {
		gameLayout.addEventFilter(MouseEvent.ANY, event);
	}

	private void setLayoutAvailableToUser() {
		gameLayout.removeEventFilter(MouseEvent.ANY, event);
	}

	private ArrayList<Integer> fillArrayListWithNumbersNumbers() {
		ArrayList<Integer> numbers = new ArrayList<>(buttons.size());
		int number = 1;
		for (int i = 0; i < buttons.size(); i += 2) {
			numbers.add(number);
			numbers.add(number);
			number++;
		}
		return numbers;

	}

}