package brickGame;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class Score {

    private static final int THREAD_SLEEP_DURATION = 15;
    private static final int ANIMATION_LIMIT = 21;

    private void addToRoot(Label label, Main main) {
        Platform.runLater(() -> main.root.getChildren().add(label));
    }

    private void removeFromRoot(Label label, Main main) {
        Platform.runLater(() -> main.root.getChildren().remove(label));
    }

    public void show(final double x, final double y, int score, final Main main) {
        String sign;
        String colorStyle;

        if (score >= 0) {
            sign = "+";
            colorStyle = "-fx-text-fill: green;";
        } else {
            sign = "";
            colorStyle = "-fx-text-fill: red;";
        }

        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.setStyle(colorStyle);

        addToRoot(label, main);

        new Thread(() -> {
            try {
                for (int i = 0; i < ANIMATION_LIMIT; i++) {
                    label.setScaleX(i);
                    label.setScaleY(i);
                    label.setOpacity((ANIMATION_LIMIT - i) / 20.0);
                    Thread.sleep(THREAD_SLEEP_DURATION);
                }
            } catch (InterruptedException e) {
                // Log the exception instead of printing the stack trace
                Logger.getLogger(Score.class.getName()).log(Level.SEVERE, "Thread interrupted", e);
            } finally {
                removeFromRoot(label, main);
            }
        }).start();
    }

    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        addToRoot(label, main);

        new Thread(() -> {
            try {
                for (int i = 0; i < ANIMATION_LIMIT; i++) {
                    label.setScaleX(Math.abs(i - 10));
                    label.setScaleY(Math.abs(i - 10));
                    label.setOpacity((ANIMATION_LIMIT - i) / 20.0);
                    Thread.sleep(THREAD_SLEEP_DURATION);
                }
            } catch (InterruptedException e) {
                // Log the exception instead of printing the stack trace
                Logger.getLogger(Score.class.getName()).log(Level.SEVERE, "Thread interrupted", e);
            }
        }).start();
    }

    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("Game Over");
            label.getStylesheets().add("style.css");
            label.setId("gameOverLabel");

            Button restart = new Button("Restart Game");
            restart.setOnAction(event -> main.restartGame());

            Button exitGame = new Button("Exit Game");
            exitGame.setOnAction(event -> System.exit(1));

            VBox vbox = new VBox(label, restart, exitGame);
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(20);
            vbox.setPadding(new Insets(20));
            vbox.setId("gameOverVBox");
            vbox.setFillWidth(true);

            StackPane stackPane = new StackPane(vbox);
            stackPane.setMinSize(500, 700);
            stackPane.setId("gameOverPane");

            main.root.getChildren().addAll(stackPane);
        });
    }

    public void showWin(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("You Win :)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            addToRoot(label, main);
        });
    }
}
