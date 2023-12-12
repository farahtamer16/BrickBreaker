package brickGame;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the initialization of game elements such as the ball, bricks, and game board.
 * This is a new class that is modified from the main class of the source code.
 * It contains the Init functions from the source code refactored and encapsulated in one class.
 * @see <a href="https://github.com/kooitt/CourseworkGame/blob/master/src/main/java/brickGame/Main.java">Main.java on GitHub</a>
 */
public class InitiateGame {

    /**
     * Default constructor for the InitiateGame class.
     * This constructor is used to create an instance of the InitiateGame class.
     */
    public InitiateGame() {
    }

    private static final Logger LOGGER = Logger.getLogger(InitiateGame.class.getName());     // Logger for logging messages and exceptions
    private final Object lock = new Object(); // Private field for synchronization
    private static final int CHANCE_OF_SPLITTER = 10; // Chance of creating a splitter block

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
     * Derived from original source code and placed with the remaining Init functions in ths class.
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
     * Derived from original source code and placed with the remaining Init functions in ths class.
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
     * Derived from original source code and placed with the remaining Init functions in ths class.
     * @param level             The current level of the game.
     * @param blocks            The list to store the initialized blocks.
     * @param isExistHeartBlock Indicates whether a heart block already exists on the board.
     */
    public void initBoard(int level, ArrayList<Block> blocks, boolean isExistHeartBlock) {

        synchronized (lock) {
            try {
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
                        } else if (level >= 2 && level <= 15 && r < CHANCE_OF_SPLITTER) {
                            type = Block.BLOCK_SPLITTER;
                        } else {
                            type = Block.BLOCK_NORMAL;
                        }
                        blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                LOGGER.log(Level.WARNING, "Error initializing blocks, size: " + blocks.size(), e);
            }
        }
    }
}
