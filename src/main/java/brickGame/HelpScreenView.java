package brickGame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Creates the help screen through which the user
 * can get an idea of how to play the game.
 * This is a new class that was not in the source code and also acts as the main menu.
 */
public class HelpScreenView{
    /**
     * A string to hold the file path
     * of the image source.
     */
    private final String imageSource= "file:src/main/resources/images/";

    /**
     * Creates and returns a Scene for the main view of the application.
     *
     * @param stage     The primary stage of the application.
     * @param music     A boolean indicating whether to play music.
     * @param musicObj  The Music object for managing music playback.
     * @return The Scene for the main view.
     */
    public Scene view(Stage stage, boolean music, Music musicObj) {
        // Create a VBox to organize UI elements vertically
        VBox vBox = new VBox();

        // Play music based on the provided boolean and music object
        musicObj.playMusic(music,1);

        // Set the background image for the VBox
        BackgroundImage backgroundImage1 = new BackgroundImage(new Image(imageSource + "helpBackground.png",500,600, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        vBox.setBackground(new Background(backgroundImage1));

        // Create a ToggleButton for navigating back
        ToggleButton backButton = new ToggleButton("Back");

        // Add the backButton to the VBox
        vBox.getChildren().add(backButton);

        // Set the action for the backButton
        backButton.setOnAction(event3 -> {
            if (backButton.isSelected()) {
                // Navigate back to the main view when the backButton is selected
                Main main = new Main();
                try {
                    main.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Create a Button for navigating to the points system view
        Button pointsSystem = new Button("Next");

        // Set the action for the pointsSystem button
        pointsSystem.setOnAction(event -> stage.setScene(pointsSystem(stage)));

        // Add the pointsSystem button to the VBox
        vBox.getChildren().add(pointsSystem);

        // Return a new Scene with the VBox as its root and dimensions of 500x600
        return new Scene(vBox, 500, 600);
    }

    /**
     * Creates the scene for the second page of the
     * Help Screen view which contains
     * information about the points system.
     *
     * @param stage stage of the application
     * @return scene with the second page as its root
     */
    private Scene pointsSystem(Stage stage){
        // Create a VBox to organize UI elements vertically
        VBox vBox = new VBox();

        // Set the background image for the VBox
        BackgroundImage backgroundImage1 = new BackgroundImage(new Image(imageSource + "pointsSystem.png",500,600, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        vBox.setBackground(new Background(backgroundImage1));

        // Create a ToggleButton for navigating back
        ToggleButton backButton = new ToggleButton("Back");

        // Add the backButton to the VBox
        vBox.getChildren().add(backButton);

        // Set the action for the backButton
        backButton.setOnAction(event3 -> {
            if(backButton.isSelected()) {
                // Navigate back to the main view when the backButton is selected
                stage.setScene(view(stage, false, new Music()));
            }
        });

        // Return a new Scene with the VBox as its root and dimensions of 500x600
        return new Scene(vBox, 500,600);
    }
}