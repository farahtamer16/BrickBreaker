package brickGame;

import java.io.Serializable;

/**
 * Represents a serializable version of a block in the brick game.
 */
public class BlockSerializable implements Serializable {
    // The row position of the block
    public final int row;

    // The column position of the block
    public final int j;

    // The type of the block
    public final int type;

    /**
     * Constructs a serializable block with the specified parameters.
     *
     * @param row  The row position of the block.
     * @param j    The column position of the block.
     * @param type The type of the block.
     */
    public BlockSerializable(int row, int j, int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}
