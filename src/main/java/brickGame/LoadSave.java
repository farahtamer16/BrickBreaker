package brickGame;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Handles the loading of saved game data from a file.
 */
public class LoadSave {

    // Flags indicating various game states and conditions
    public boolean isExistHeartBlock;
    public boolean isGoldStatus;
    public boolean goDownBall;
    public boolean goRightBall;
    public boolean collideToBreak;
    public boolean collideToBreakAndMoveToRight;
    public boolean collideToRightWall;
    public boolean collideToLeftWall;
    public boolean collideToRightBlock;
    public boolean collideToBottomBlock;
    public boolean collideToLeftBlock;
    public boolean collideToTopBlock;

    // Game variables
    public int level;
    public int score;
    public int heart;
    public int destroyedBlockCount;
    public double xBall;
    public double yBall;
    public double xBreak;
    public double yBreak;
    public double centerBreakX;
    public long time;
    public long goldTime;
    public double vX;
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
