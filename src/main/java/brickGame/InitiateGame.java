package brickGame;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class InitiateGame {

    private double xBall;
    private double yBall;

    public void initBall(int ballRadius, Circle ball) {
        Random random = new Random();
//        xBall = random.nextInt(sceneWidth) + 1;
//        yBall = random.nextInt(sceneHeight - 200) + ((level + 1) * Block.getHeight()) + 15;
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new javafx.scene.image.Image("ball.png")));

//        ball.setCenterX(random.nextInt(sceneWidth) + 1);
//        ball.setCenterY(random.nextInt(sceneHeight - 200) + ((level + 1) * Block.getHeight()) + 15);
    }

    public double setXBall(int sceneWidth){
        Random random = new Random();
        xBall = random.nextInt(sceneWidth) + 1;

        return xBall;
    }

    public double setYBall(int sceneHeight, int level){
        Random random = new Random();
        yBall = random.nextInt(sceneHeight - 200) + ((level + 1) * Block.getHeight()) + 15;

        return yBall;
    }


    public void initBreak(double xBreak, double yBreak, int breakWidth, int breakHeight,
                          Rectangle rect) {
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new javafx.scene.image.Image("block.jpg"));

        rect.setFill(pattern);
    }

    public void initBoard(int level, ArrayList<Block> blocks, boolean isExistHeartBlock,
                          Color[] colors) {
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
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
            }
        }
    }
}
