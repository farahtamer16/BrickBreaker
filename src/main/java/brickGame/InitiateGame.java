package brickGame;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Handles the initialization of game elements such as the ball, bricks, and game board.
 */
public class InitiateGame {

    private static final int CHANCE_OF_SPLITTER = 10;

    // Array of colors for block initialization
    private final Color[] colors = new Color[]{
            Color.DARKGREEN,
            Color.GREEN,
            Color.DARKOLIVEGREEN,
            Color.DARKSEAGREEN,
            Color.FORESTGREEN,
            Color.LAWNGREEN,
            Color.GREENYELLOW,
            Color.SPRINGGREEN,
            Color.LIGHTSEAGREEN,
            Color.LIMEGREEN,
            Color.LIGHTGREEN,
            Color.MEDIUMSPRINGGREEN,
            Color.DARKTURQUOISE,
    };

    /**
     * Initializes the ball with the specified radius and appearance.
     *
     * @param ballRadius The radius of the ball.
     * @param ball       The ball object.
     */
    public void initBall(int ballRadius, Circle ball) {
        Random random = new Random();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new javafx.scene.image.Image("ball.png")));
    }

    /**
     * Sets the initial x-coordinate for the ball to the center of the scene.
     *
     * @param sceneWidth The width of the game scene.
     * @return The initial x-coordinate of the ball.
     */
    public double setXBall(int sceneWidth) {
        return sceneWidth / 2.0;
    }

    /**
     * Sets the initial y-coordinate for the ball to the center of the scene.
     *
     * @param sceneHeight The height of the game scene.
     * @return The initial y-coordinate of the ball.
     */
    public double setYBall(int sceneHeight) {
        return sceneHeight / 2.0;
    }

    /**
     * Initializes a brick (breakable block) with the specified parameters.
     *
     * @param xBreak      The x-coordinate of the brick.
     * @param yBreak      The y-coordinate of the brick.
     * @param breakWidth  The width of the brick.
     * @param breakHeight The height of the brick.
     * @param rect        The rectangle representing the brick.
     */
    public void initBreak(double xBreak, double yBreak, int breakWidth, int breakHeight, Rectangle rect) {
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new javafx.scene.image.Image("block.jpg"));

        rect.setFill(pattern);
    }

    /**
     * Initializes the game board by populating it with blocks (or bricks) based on the specified level and certain conditions.
     *
     * @param level            The current level of the game.
     * @param blocks           The list to store the initialized blocks.
     * @param isExistHeartBlock Indicates whether a heart block already exists on the board.
     */
    public void initBoard(int level, ArrayList<Block> blocks, boolean isExistHeartBlock) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else if (r % 10 == 4) {
                    type = Block.BLOCK_SPEED;
                } else if(level >= 2 && level <= 15 && r < CHANCE_OF_SPLITTER) {
                    type = Block.BLOCK_SPLITTER;
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
            }
        }
    }
}
