package brickGame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {


    private int level = 0;

    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;

    private int breakWidth     = 130;
    private int breakHeight    = 30;
    private int halfBreakWidth = breakWidth / 2;

    private int sceneWidth = 500;
    private int sceneHeigt = 700;

    private static int LEFT  = 1;
    private static int RIGHT = 2;

    private Circle ball;
    private double xBall;
    private double yBall;

    private boolean isGoldStauts      = false;
    private boolean isExistHeartBlock = false;

    private Rectangle rect;
    private int       ballRadius = 10;

    private int destroyedBlockCount = 0;

    private double v = 1.000;

    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;
    private long hitTime  = 0;
    private long goldTime = 0;

    private GameEngine engine;
    public static String savePath    = "D:/save/save.mdds";
    public static String savePathDir = "D:/save/";

    private ArrayList<Block> blocks = new ArrayList<>();
    public ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private Color[]          colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;

    private boolean loadFromSave = false;
    private InitiateGame initiateGame;
    private Music music;
    private Music music2;

    Stage  primaryStage;
    Button load    = null;
    Button newGameButton = null;
    Button helpScreen = null;
    Button exitGame = null;
    VBox vbox = null;
    ToggleButton musicButton = new ToggleButton();
    private boolean musicState = true;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        root = new Pane();

        load = new Button("Load Game");
        newGameButton = new Button("Start New Game");
        helpScreen = new Button("How to play");
        exitGame = new Button("Exit game");

        music = new Music();
        music.playMusic(musicState,1);
        musicButton = new ToggleButton();

        musicButton.setOnAction(event -> {
            music.playMusic(!musicButton.isSelected(),1);
            if(musicButton.isSelected()){
                musicState = false;
            }
        });


        vbox = new VBox(newGameButton, load, helpScreen, exitGame, musicButton);
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));
        vbox.setId("vboxy");
        vbox.setFillWidth(true);

        StackPane stackPane = new StackPane(vbox);
        stackPane.setMinSize(sceneWidth, sceneHeigt);
        stackPane.setId("pane");

        if(!loadFromSave){
            level++;
        }

        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 70);


        if (!loadFromSave) {
            if (level >1){
                new Score().showMessage("Level Up!", this);
            }
            //if (level == 18) {
                //new Score().showWin(this);
               // return;
            //}

            ball = new Circle();  // Initialize the 'ball' variable
            rect = new Rectangle();  // Initialize the 'rect' variable
//
            initiateGame = new InitiateGame();

            initiateGame.initBall(ballRadius, ball);

            xBall = initiateGame.setXBall(sceneWidth);
            yBall = initiateGame.setYBall(sceneHeigt, level);

            initiateGame.initBoard(level, blocks, isExistHeartBlock);
            initiateGame.initBreak(xBreak, yBreak, breakWidth, breakHeight, rect);

            root.getChildren().addAll(stackPane,rect, ball, scoreLabel, heartLabel, levelLabel);

            for (Block block : blocks) {
                root.getChildren().add(block.rect);
            }

            setGameObjectsVisible(false);

            Scene scene = new Scene(root, sceneWidth, sceneHeigt);
            scene.getStylesheets().add("style.css");
            scene.setOnKeyPressed(this);

            primaryStage.setTitle("Game");
            primaryStage.setScene(scene);
            primaryStage.show();

            if (level > 1 && level < 18) {
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();

                setButtonsVisible(false);
                setGameObjectsVisible(true);
            }

            load.setOnAction(event -> {
                loadGame();
                setButtonsVisible(false);
                setGameObjectsVisible(true);
            });

            helpScreen.setOnAction(actionEvent -> primaryStage.setScene(new HelpScreenView().view(primaryStage, false, music)));
            exitGame.setOnAction(actionEvent -> System.exit(1));

            newGameButton.setOnAction(event -> {
                engine = new GameEngine();
                engine.setOnAction(Main.this);
                engine.setFps(120);
                engine.start();

                setGameObjectsVisible(true);
                setButtonsVisible(false);
            });

        } else {

            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);

            for (Block block : blocks) {
                root.getChildren().add(block.rect);
            }

            setGameObjectsVisible(false);

            Scene scene = new Scene(root, sceneWidth, sceneHeigt);
            scene.getStylesheets().add("style.css");
            scene.setOnKeyPressed(this);

            primaryStage.setTitle("Game");
            primaryStage.setScene(scene);
            primaryStage.show();

            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;

            setGameObjectsVisible(true);
            setButtonsVisible(false);

        }
    }



    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:
                move(RIGHT);
                break;
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
        }
    }


    private void move(final int direction) {
        AnimationTimer animationTimer = new AnimationTimer() {
            int frames = 0;

            @Override
            public void handle(long now) {
                if (xBreak == (sceneWidth - breakWidth) && direction == RIGHT) {
                    stop();
                } else if (xBreak == 0 && direction == LEFT) {
                    stop();
                } else {
                    if (direction == RIGHT) {
                        xBreak++;
                    } else {
                        xBreak--;
                    }
                    centerBreakX = xBreak + halfBreakWidth;
                }

                frames++;
                if (frames >= 30) {
                    stop();
                }
            }
        };

        animationTimer.start();
    }



    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean colideToBreak               = false;
    private boolean colideToBreakAndMoveToRight = true;
    private boolean colideToRightWall           = false;
    private boolean colideToLeftWall            = false;
    private boolean colideToRightBlock          = false;
    private boolean colideToBottomBlock         = false;
    private boolean colideToLeftBlock           = false;
    private boolean colideToTopBlock            = false;

    private double vX = 1.000;
    private double vY = 1.000;


    private void resetColideFlags() {
        colideToBreak = false;
        colideToBreakAndMoveToRight = false;
        colideToRightWall = false;
        colideToLeftWall = false;

        colideToRightBlock = false;
        colideToBottomBlock = false;
        colideToLeftBlock = false;
        colideToTopBlock = false;
    }

    private void setPhysicsToBall() {
        //v = ((time - hitTime) / 1000.000) + 1.000;

        if (goDownBall) {
            yBall += vY;
        } else {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        } else {
            xBall -= vX;
        }

        if (yBall <= 0) {
            //vX = 1.000;
            resetColideFlags();
            goDownBall = true;
            return;
        }
        if (yBall >= sceneHeigt) {
            goDownBall = false;
            if (!isGoldStauts) {
                heart--;
                new Score().show(sceneWidth / 2, sceneHeigt / 2, -1, this);

                if (heart == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }

            }
            //return;
        }

        if (yBall >= yBreak - ballRadius) {
            //System.out.println("Colide1");
            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                hitTime = time;
                resetColideFlags();
                colideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / (breakWidth / 2);

                if (Math.abs(relation) <= 0.3) {
                    //vX = 0;
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                    //System.out.println("vX " + vX);
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);
                    //System.out.println("vX " + vX);
                }

                if (xBall - centerBreakX > 0) {
                    colideToBreakAndMoveToRight = true;
                } else {
                    colideToBreakAndMoveToRight = false;
                }
                //System.out.println("Colide2");
            }
        }

        if (xBall >= sceneWidth) {
            resetColideFlags();
            //vX = 1.000;
            colideToRightWall = true;
        }

        if (xBall <= 0) {
            resetColideFlags();
            //vX = 1.000;
            colideToLeftWall = true;
        }

        if (colideToBreak) {
            if (colideToBreakAndMoveToRight) {
                goRightBall = true;
            } else {
                goRightBall = false;
            }
        }

        //Wall Colide

        if (colideToRightWall) {
            goRightBall = false;
        }

        if (colideToLeftWall) {
            goRightBall = true;
        }

        //Block Colide

        if (colideToRightBlock) {
            goRightBall = true;
        }

        if (colideToLeftBlock) {
            goRightBall = true;
        }

        if (colideToTopBlock) {
            goDownBall = false;
        }

        if (colideToBottomBlock) {
            goDownBall = true;
        }


    }


//    private void checkDestroyedCount() {
//        new Thread(() -> {
//            if (destroyedBlockCount == blocks.size()) {
//                //TODO win level todo...
////                System.out.println("Next Level");
//                Platform.runLater(() -> {
//                    Label label = new Label("Congratulations! Play the next level?");
//                    label.getStylesheets().add("style.css");
//                    label.setId("nextLevelLabel");
//
//                    Label label1 = new Label("Current level: " + level);
//                    label1.getStylesheets().add("style.css");
//                    label1.setId("currentLevelLabel");
//
//                    Button nextLevel = new Button("Next Level");
//                    nextLevel.setOnAction(event -> nextLevel());
//
//                    Button exitGame = new Button("Exit Game");
//                    exitGame.setOnAction(actionEvent -> System.exit(1));
//
//                    VBox vbox = new VBox(label, label1, nextLevel, exitGame);
//                    vbox.setAlignment(Pos.CENTER);
//                    vbox.setSpacing(20);
//                    vbox.setPadding(new Insets(20));
//                    vbox.setId("vboxy3");
//                    vbox.setFillWidth(true);
//
//                    StackPane stackPane = new StackPane(vbox);
//                    stackPane.setMinSize(500, 700);
//                    stackPane.setId("pane3");
//
//                    root.getChildren().addAll(stackPane);
//
////            nextLevel();
//                });
//            }
//        }).start();
//    }

    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            nextLevel();
        }

    }

    private void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(destroyedBlockCount);


                outputStream.writeDouble(xBall);
                outputStream.writeDouble(yBall);
                outputStream.writeDouble(xBreak);
                outputStream.writeDouble(yBreak);
                outputStream.writeDouble(centerBreakX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);


                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStauts);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(colideToBreak);
                outputStream.writeBoolean(colideToBreakAndMoveToRight);
                outputStream.writeBoolean(colideToRightWall);
                outputStream.writeBoolean(colideToLeftWall);
                outputStream.writeBoolean(colideToRightBlock);
                outputStream.writeBoolean(colideToBottomBlock);
                outputStream.writeBoolean(colideToLeftBlock);
                outputStream.writeBoolean(colideToTopBlock);

                ArrayList<BlockSerializable> blockSerializables = new ArrayList<BlockSerializable>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                }

                outputStream.writeObject(blockSerializables);

                new Score().showMessage("Game Saved", Main.this);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();


        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStauts = loadSave.isGoldStatus;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        colideToBreak = loadSave.collideToBreak;
        colideToBreakAndMoveToRight = loadSave.collideToBreakAndMoveToRight;
        colideToRightWall = loadSave.collideToRightWall;
        colideToLeftWall = loadSave.collideToLeftWall;
        colideToRightBlock = loadSave.collideToRightBlock;
        colideToBottomBlock = loadSave.collideToBottomBlock;
        colideToLeftBlock = loadSave.collideToLeftBlock;
        colideToTopBlock = loadSave.collideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }


        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void nextLevel() {
        Platform.runLater(() -> {
            try {

                music.playMusic(false,1);

                music2 = new Music();
                music2.playMusic(true,2);

                vX = 1.000;

                engine.stop();
                resetColideFlags();
                goDownBall = true;

                isGoldStauts = false;
                isExistHeartBlock = false;


                hitTime = 0;
                time = 0;
                goldTime = 0;

                engine.stop();
                blocks.clear();
                chocos.clear();
                destroyedBlockCount = 0;
                start(primaryStage);

                setGameObjectsVisible(true);
                setButtonsVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void restartGame() {

        try {
            music.playMusic(false,1);

            level = 0;
            heart = 3;
            score = 0;
            vX = 1.000;
            destroyedBlockCount = 0;
            resetColideFlags();
            goDownBall = true;

            isGoldStauts = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();

            setGameObjectsVisible(true);
            setButtonsVisible(false);

            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + score);
            heartLabel.setText("Heart : " + heart);
            rect.setX(xBreak);
            rect.setY(yBreak);
            ball.setCenterX(xBall);
            ball.setCenterY(yBall);

            Iterator<Bonus> iterator = chocos.iterator();
            while (iterator.hasNext()) {
                Bonus choco = iterator.next();
                choco.choco.setY(choco.y);

                if (choco.y > sceneHeigt || choco.taken) {
                    iterator.remove();  // Remove the element using iterator
                }
            }

            if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
                for (final Block block : blocks) {
                    int hitCode = block.checkHitToBlock(xBall, yBall);
                    if (hitCode != Block.NO_HIT) {
                        score += 1;

                        new Score().show(block.x, block.y, 1, this);

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        destroyedBlockCount++;
                        //System.out.println("size is " + blocks.size());
                        resetColideFlags();

                        if (block.type == Block.BLOCK_CHOCO) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = time;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    root.getChildren().add(choco.choco);
                                }
                            });
                            chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) {
                            goldTime = time;
                            ball.setFill(new ImagePattern(new Image("goldball.png")));
                            System.out.println("gold ball");
                            root.getStyleClass().add("goldRoot");
                            isGoldStauts = true;
                        }

                        if (block.type == Block.BLOCK_HEART) {
                            heart++;
                        }

                        if (hitCode == Block.HIT_RIGHT) {
                            colideToRightBlock = true;
                        } else if (hitCode == Block.HIT_BOTTOM) {
                            colideToBottomBlock = true;
                        } else if (hitCode == Block.HIT_LEFT) {
                            colideToLeftBlock = true;
                        } else if (hitCode == Block.HIT_TOP) {
                            colideToTopBlock = true;
                        }

                    }

                    //TODO hit to break and some work here....
                    //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
                }
            }
        });

    }


    @Override
    public void onInit() {

    }

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();


        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
            isGoldStauts = false;
        }

        for (Bonus choco : chocos) {
            if (choco.y > sceneHeigt || choco.taken) {
                continue;
            }
            if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                choco.choco.setVisible(false);
                score += 3;
                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }

        //System.out.println("time is:" + time + " goldTime is " + goldTime);

    }


    @Override
    public void onTime(long time) {
        this.time = time;
    }

    private void setGameObjectsVisible(boolean visible){
        scoreLabel.setVisible(visible);
        heartLabel.setVisible(visible);
        levelLabel.setVisible(visible);
        rect.setVisible(visible);
        ball.setVisible(visible);

    }

    private void setButtonsVisible(boolean visible){

        load.setVisible(visible);
        newGameButton.setVisible(visible);
        helpScreen.setVisible(visible);
        exitGame.setVisible(visible);
        vbox.setVisible(visible);

    }



}
