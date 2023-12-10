package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

/**
 * Represents a block in the brick game.
 */
public class Block implements Serializable {
    // Represents an empty block
    public static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);

    // Position of the block in the game grid
    public int row;
    public int column;

    // Indicates if the block is destroyed
    public boolean isDestroyed = false;

    // Color and type of the block
    private Color color;
    public int type;

    // Coordinates and dimensions of the block
    public int x;
    public int y;
    private final int width = 100;
    private final int height = 30;
    private final int paddingTop = height * 2;
    private final int paddingH = 30;
    public Rectangle rect;

    // Constants representing different hit directions
    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;

    // Constants representing different block types
    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static final int BLOCK_SPEED = 103;
    public static final int BLOCK_SPLITTER = 104;

    /**
     * Constructs a block with the specified parameters.
     *
     * @param row    The row position of the block.
     * @param column The column position of the block.
     * @param color  The color of the block.
     * @param type   The type of the block.
     */
    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.setColor(color);
        this.type = type;

        draw();
    }


    // Draws the block on the screen
    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        // Set block fill based on type
        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_SPEED) {
            Image image = new Image("speed.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_SPLITTER) {
            Image image = new Image("splitter.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(getColor());
        }
    }

    /**
     * Checks if the ball hits the block and returns the hit direction.
     *
     * @param xBall The x-coordinate of the ball.
     * @param yBall The y-coordinate of the ball.
     * @return The hit direction (NO_HIT, HIT_RIGHT, HIT_BOTTOM, HIT_LEFT, HIT_TOP).
     */
    public int checkHitToBlock(double xBall, double yBall, int ballRadius) {
        if (isDestroyed) {
            return NO_HIT;
        }

        double effectiveY = y + height / 2.0;
        double effectiveX = x + width / 2.0;

        if (Math.abs(xBall - effectiveX) <= width / 2.0 + ballRadius && Math.abs(yBall - effectiveY) <= height / 2.0 + ballRadius) {
            // Collision detected
            double dx = Math.min(Math.abs(xBall - x), Math.abs(xBall - (x + width)));
            double dy = Math.min(Math.abs(yBall - y), Math.abs(yBall - (y + height)));

            if (dx < dy) {
                if (xBall < x) {
                    return HIT_LEFT;
                } else {
                    return HIT_RIGHT;
                }
            } else {
                if (yBall < y) {
                    return HIT_TOP;
                } else {
                    return HIT_BOTTOM;
                }
            }
        }

        return NO_HIT;
    }



    /**
     * Gets the padding at the top of the game grid.
     *
     * @return The top padding.
     */
    public static int getPaddingTop() {
        return block.paddingTop;
    }

    /**
     * Gets the horizontal padding of the game grid.
     *
     * @return The horizontal padding.
     */
    public static int getPaddingH() {
        return block.paddingH;
    }

    /**
     * Gets the height of the block.
     *
     * @return The height of the block.
     */
    public static int getHeight() {
        return block.height;
    }

    /**
     * Gets the width of the block.
     *
     * @return The width of the block.
     */
    public static int getWidth() {
        return block.width;
    }

    /**
     * Gets the color of the block.
     *
     * @return The color of the block.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the block.
     *
     * @param color The color to set.
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
