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

/**
 * The Score class manages the display of scores and game-over/win messages.
 */
public class Score {

    // The duration (in milliseconds) for which the animation thread sleeps between each movement.
    private static final int THREAD_SLEEP_DURATION = 15;
    // The limit for the number of iterations in the animation loop.
    private static final int ANIMATION_LIMIT = 21;

    /**
     * Adds the given label to the root of the main scene in a JavaFX application.
     *
     * @param label The label to be added.
     * @param main The main application instance.
     */

    private void addToRoot(Label label, Main main) {
        Platform.runLater(() -> main.root.getChildren().add(label));
    }

    /**
     * Removes the given label from the root of the main scene in a JavaFX application.
     *
     * @param label The label to be removed.
     * @param main The main application instance.
     */

    private void removeFromRoot(Label label, Main main) {
        Platform.runLater(() -> main.root.getChildren().remove(label));
    }

    /**
     * Displays a score animation at the specified position on the screen.
     * <p>
     * Translation (setTranslateX and setTranslateY) for the restart button in the showGameOver is set, as opposed to,
     * the source code.
     * <p>
     * @param x The x-coordinate of the score display.
     * @param y The y-coordinate of the score display.
     * @param score The score to be displayed.
     * @param main The main application instance.
     */
    public void show(final double x, final double y, int score, final Main main) {
        String sign;
        String colorStyle;      // New variable for score colour

        if (score >= 0) {
            sign = "+";
            colorStyle = "-fx-text-fill: green;";       // Set color style for positive score
        } else {
            sign = "";
            colorStyle = "-fx-text-fill: red;";         // Set color style for negative score
        }

        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.setStyle(colorStyle); // Apply the color style

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

    /**
     * Displays a message with animation on the screen.
     *
     * @param message The message to be displayed.
     * @param main The main application instance.
     */
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

    /**
     * Displays the game-over message with restart and exit buttons.
     *<p>
     *     This method has css styling for labels, which is not in the source code
     *     <p>
     * @param main The main application instance.
     */
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
            vbox.setId("GameOverBox");
            vbox.setFillWidth(true);

            StackPane stackPane = new StackPane(vbox);
            stackPane.setMinSize(500, 700);
            stackPane.setId("pane2");

            main.root.getChildren().addAll(stackPane);
        });
    }

    /**
     * Displays the win message.
     *
     * @param main The main application instance.
     */
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
