module brickGame {
    requires transitive javafx.graphics;  // ← needed for Rectangle, Color, etc.
    requires javafx.fxml;
    requires javafx.controls;

    opens brickGame to javafx.fxml;
    exports brickGame;
}
