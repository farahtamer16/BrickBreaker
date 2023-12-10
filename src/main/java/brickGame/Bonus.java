package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a bonus object in the brick game.
 */
public class Bonus implements Serializable {
    // The rectangle representing the bonus
    public Rectangle choco;

    // The x-coordinate of the bonus
    public double x;

    // The y-coordinate of the bonus
    public double y;

    // The time when the bonus was created
    public long timeCreated;

    // Indicates if the bonus has been taken by the player
    public boolean taken = false;

    /**
     * Constructs a bonus at the specified row and column.
     *
     * @param row    The row position of the bonus.
     * @param column The column position of the bonus.
     */
    public Bonus(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;

        draw();
    }

    // Draws the bonus on the screen
    private void draw() {
        choco = new Rectangle();
        choco.setWidth(30);
        choco.setHeight(30);
        choco.setX(x);
        choco.setY(y);

        // Randomly choose a bonus image
        String url;
        if (new Random().nextInt(20) % 2 == 0) {
            url = "bonus1.png";
        } else {
            url = "bonus2.png";
        }

        choco.setFill(new ImagePattern(new Image(url)));
    }
}
