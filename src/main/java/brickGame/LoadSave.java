package brickGame;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Handles the loading of saved game data from a file.
 * @see <a href="https://github.com/kooitt/CourseworkGame/blob/master/src/main/java/brickGame/LoadSave.java">LoadSave.java on GitHub</a>
 */
public class LoadSave {

    /**
     * Default constructor for the LoadSave class.
     * This constructor is used to create an instance of the LoadSave class.
     */
    public LoadSave() {
    }

    /**
     * Flags indicating the presence of a heart block in the game.
     */
    public boolean isExistHeartBlock;

    /**
     * Flags indicating the gold status in the game.
     */
    public boolean isGoldStatus;

    /**
     * Flag indicating the downward movement of the ball.
     */
    public boolean goDownBall;

    /**
     * Flag indicating the rightward movement of the ball.
     */
    public boolean goRightBall;

    /**
     * Flag indicating a collision with a break.
     */
    public boolean collideToBreak;

    /**
     * Flag indicating a collision with a break and movement to the right.
     */
    public boolean collideToBreakAndMoveToRight;

    /**
     * Flag indicating a collision with the right wall.
     */
    public boolean collideToRightWall;

    /**
     * Flag indicating a collision with the left wall.
     */
    public boolean collideToLeftWall;

    /**
     * Flag indicating a collision with a block to the right.
     */
    public boolean collideToRightBlock;

    /**
     * Flag indicating a collision with a block at the bottom.
     */
    public boolean collideToBottomBlock;

    /**
     * Flag indicating a collision with a block to the left.
     */
    public boolean collideToLeftBlock;

    /**
     * Flag indicating a collision with a block at the top.
     */
    public boolean collideToTopBlock;

    /**
     * The level of the game.
     */
    public int level;

    /**
     * The current score in the game.
     */
    public int score;

    /**
     * The number of heart blocks in the game.
     */
    public int heart;

    /**
     * The count of destroyed blocks in the game.
     */
    public int destroyedBlockCount;

    /**
     * The x-coordinate of the ball.
     */
    public double xBall;

    /**
     * The y-coordinate of the ball.
     */
    public double yBall;

    /**
     * The x-coordinate of the break.
     */
    public double xBreak;

    /**
     * The y-coordinate of the break.
     */
    public double yBreak;

    /**
     * The center x-coordinate of the break.
     */
    public double centerBreakX;

    /**
     * The current time in the game.
     */
    public long time;

    /**
     * The time when the gold status is active.
     */
    public long goldTime;

    /**
     * The velocity of the ball in the x-direction.
     */
    public double vX;

    /**
     * List of serialized blocks in the game.
     */
    public ArrayList<BlockSerializable> blocks = new ArrayList<>();

    /**
     * Reads saved game data from a file.
     */
    public void read() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Main.savePath));

            // Read game variables
            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = inputStream.readInt();
            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xBreak = inputStream.readDouble();
            yBreak = inputStream.readDouble();
            centerBreakX = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();

            // Read game state flags
            isExistHeartBlock = inputStream.readBoolean();
            isGoldStatus = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            collideToBreak = inputStream.readBoolean();
            collideToBreakAndMoveToRight = inputStream.readBoolean();
            collideToRightWall = inputStream.readBoolean();
            collideToLeftWall = inputStream.readBoolean();
            collideToRightBlock = inputStream.readBoolean();
            collideToBottomBlock = inputStream.readBoolean();
            collideToLeftBlock = inputStream.readBoolean();
            collideToTopBlock = inputStream.readBoolean();

            // Read ArrayList of BlockSerializable objects
            try {
                blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
