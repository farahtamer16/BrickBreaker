package brickGame;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Iterator;


public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

    private int level = 0;

    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;

    private final int breakWidth     = 130;
    private final int breakHeight    = 30;
    private final int halfBreakWidth = breakWidth / 2;

    private final int sceneWidth = 500;
    private final int sceneHeight = 700;

    private static final int LEFT  = 1;
    private static final int RIGHT = 2;

    private Circle ball;
    private double xBall;
    private double yBall;
    private boolean goDownSplitterBall = true;
    private boolean goRightSplitterBall = true;

    private boolean isGoldStatus = false;
    private boolean isExistHeartBlock = false;

    private Rectangle rect;
    private final int       ballRadius = 10;

    private int destroyedBlockCount = 0;

    private double v = 1.000;

    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;
    private long hitTime  = 0;
    private long goldTime = 0;
    private final Object chocosLock = new Object();

    private GameEngine engine;
    public static String savePath    = "D:/save/save.mdds";
    public static String savePathDir = "D:/save/";

    private final ArrayList<Block> blocks = new ArrayList<>();
    public ArrayList<Bonus> chocos = new ArrayList<>();
    private final Color[]          colors = new Color[]{
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
    private boolean isSpeedBoostActive = false;
    private long speedBoostStartTime;


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
        stackPane.setMinSize(sceneWidth, sceneHeight);
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
            if (level > 1) {
                new Score().showMessage("Level Up!", this);
            }
            if (level == 18) {
                new Score().showWin(this);
                return;
            }

            ball = new Circle();  // Initialize the 'ball' variable
            rect = new Rectangle();  // Initialize the 'rect' variable

            InitiateGame initiateGame = new InitiateGame();

            initiateGame.initBall(ballRadius, ball);

            xBall = initiateGame.setXBall(sceneWidth);
            yBall = initiateGame.setYBall(sceneHeight); // Pass only sceneHeight here

            initiateGame.initBoard(level, blocks, isExistHeartBlock);
            initiateGame.initBreak(xBreak, yBreak, breakWidth, breakHeight, rect);

            root.getChildren().addAll(stackPane,rect, ball, scoreLabel, heartLabel, levelLabel);

            for (Block block : blocks) {
                root.getChildren().add(block.rect);
            }

            setGameObjectsVisible(false);

            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            scene.getStylesheets().add("style.css");
            scene.setOnKeyPressed(this);

            primaryStage.setTitle("Game");
            primaryStage.setScene(scene);
            primaryStage.show();

            if (level > 1 && level < 18) {
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(240);
                engine.start();

                setButtonsVisible();
                setGameObjectsVisible(true);
            }

            load.setOnAction(event -> {
                loadGame();
                setButtonsVisible();
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
                setButtonsVisible();
            });

        } else {

            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);

            for (Block block : blocks) {
                root.getChildren().add(block.rect);
            }

            setGameObjectsVisible(false);

            Scene scene = new Scene(root, sceneWidth, sceneHeight);
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
            setButtonsVisible();

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
    private boolean collideToBreak = false;
    private boolean collideToBreakAndMoveToRight = true;
    private boolean collideToRightWall = false;
    private boolean collideToLeftWall = false;
    private boolean collideToRightBlock = false;
    private boolean collideToBottomBlock = false;
    private boolean collideToLeftBlock = false;
    private boolean collideToTopBlock = false;

    private double vX = 1.000;
    private double vY = 1.000;


    private void resetCollideFlags() {
        collideToBreak = false;
        collideToBreakAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;

        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

    private void setPhysicsToBall() {
        //v = ((time - hitTime) / 1000.000) + 1.000;

        double vY = 2.000;
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
            resetCollideFlags();
            goDownBall = true;
            return;
        }
        if (yBall >= sceneHeight) {
            goDownBall = false;
            if (!isGoldStatus) {
                heart--;
                new Score().show((double) sceneWidth / 2, (double) sceneHeight / 2, -1, this);

                if (heart == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }

            }
            return;
        }

        if (yBall >= yBreak - ballRadius) {
            //System.out.println("Colide1");
            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                hitTime = time;
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / ((double) breakWidth / 2);

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

                collideToBreakAndMoveToRight = xBall - centerBreakX > 0;
                //System.out.println("Colide2");
            }
        }

        if (xBall >= sceneWidth) {
            resetCollideFlags();
            //vX = 1.000;
            collideToRightWall = true;
        }

        if (xBall <= 0) {
            resetCollideFlags();
            //vX = 1.000;
            collideToLeftWall = true;
        }

        if (collideToBreak) {
            goRightBall = collideToBreakAndMoveToRight;
        }

        //Wall Colide

        if (collideToRightWall) {
            goRightBall = false;
        }

        if (collideToLeftWall) {
            goRightBall = true;
        }

        //Block Colide

        if (collideToRightBlock) {
            goRightBall = true;
        }

        if (collideToLeftBlock) {
            goRightBall = true;
        }

        if (collideToTopBlock) {
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            goDownBall = true;
        }


    }

//    private void checkDestroyedCount() {
//       if (destroyedBlockCount == blocks.size()) {
//            nextLevel();
//        }

//    }

    private boolean isLevelCompleted = false; // Add this boolean flag

    private void checkDestroyedCount() {
        if (!isLevelCompleted && destroyedBlockCount == blocks.size()) {
            isLevelCompleted = true; // Set the flag to prevent multiple execution

            Platform.runLater(() -> {
                // TODO win level todo...
                // Check if there is already a StackPane with ID "pane3"
                StackPane existingPane = (StackPane) root.lookup("#pane3");

                if (existingPane == null) {
                    Label label = new Label("Congratulations! Play the next level?");
                    label.getStylesheets().add("style.css");
                    label.setId("nextLevelLabel");

                    Label label1 = new Label("Current level: " + level);
                    label1.getStylesheets().add("style.css");
                    label1.setId("currentLevelLabel");

                    Button nextLevel = new Button("Next Level");
                    nextLevel.setOnAction(event -> handleNextLevel());

                    Button exitGame = new Button("Exit Game");
                    exitGame.setOnAction(actionEvent -> System.exit(1));

                    VBox vbox = new VBox(label, label1, nextLevel, exitGame);
                    vbox.setAlignment(Pos.CENTER);
                    vbox.setSpacing(20);
                    vbox.setPadding(new Insets(20));
                    vbox.setId("vboxy3");
                    vbox.setFillWidth(true);

                    StackPane stackPane = new StackPane(vbox);
                    stackPane.setMinSize(500, 700);
                    stackPane.setId("pane3");

                    root.getChildren().addAll(stackPane);
                }

                isLevelCompleted = false; // Reset the flag for the next level
            });
        }
    }

    private void handleNextLevel() {
        // Close the current level completion screen before proceeding to the next level
        StackPane pane3 = (StackPane) root.lookup("#pane3");
        if (pane3 != null) {
            root.getChildren().remove(pane3);
        }

        // Call nextLevel method to proceed to the next level
        nextLevel();
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
                outputStream.writeBoolean(isGoldStatus);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(collideToBreak);
                outputStream.writeBoolean(collideToBreakAndMoveToRight);
                outputStream.writeBoolean(collideToRightWall);
                outputStream.writeBoolean(collideToLeftWall);
                outputStream.writeBoolean(collideToRightBlock);
                outputStream.writeBoolean(collideToBottomBlock);
                outputStream.writeBoolean(collideToLeftBlock);
                outputStream.writeBoolean(collideToTopBlock);

                ArrayList<BlockSerializable> blockSerializable = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializable.add(new BlockSerializable(block.row, block.column, block.type));
                }

                outputStream.writeObject(blockSerializable);

                new Score().showMessage("Game Saved", Main.this);


            } catch (IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Error while saving the game", e);
            } finally {
                try {
                    Objects.requireNonNull(outputStream).flush();
                    outputStream.close();
                } catch (IOException e) {
                    Logger logger = Logger.getLogger(getClass().getName());
                    logger.log(Level.SEVERE, "Error closing output stream", e);
                }
            }
        }).start();
    }

    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        try {
            loadSave.read();

            isExistHeartBlock = loadSave.isExistHeartBlock;
            isGoldStatus = loadSave.isGoldStatus;
            goDownBall = loadSave.goDownBall;
            goRightBall = loadSave.goRightBall;
            collideToBreak = loadSave.collideToBreak;
            collideToBreakAndMoveToRight = loadSave.collideToBreakAndMoveToRight;
            collideToRightWall = loadSave.collideToRightWall;
            collideToLeftWall = loadSave.collideToLeftWall;
            collideToRightBlock = loadSave.collideToRightBlock;
            collideToBottomBlock = loadSave.collideToBottomBlock;
            collideToLeftBlock = loadSave.collideToLeftBlock;
            collideToTopBlock = loadSave.collideToTopBlock;
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
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "An error occurred while starting the game from save.", e);
            }
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "An error occurred while loading the game.", e);
        }
    }

    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                music.playMusic(false, 1);

                music2 = new Music();
                music2.playMusic(true, 2);

                vX = 1.000;

                engine.stop();
                resetCollideFlags();
                goDownBall = true;

                isGoldStatus = false;
                isExistHeartBlock = false;

                hitTime = 0;
                time = 0;
                goldTime = 0;

                engine.stop();
                blocks.clear();
                chocos.clear();
                destroyedBlockCount = 0;

                // Remove BLOCK_SPLITTER block explicitly
                Block splitterBlock = null;
                for (Block block : blocks) {
                    if (block.type == Block.BLOCK_SPLITTER) {
                        splitterBlock = block;
                        break;
                    }
                }
                if (splitterBlock != null) {
                    root.getChildren().remove(splitterBlock.rect);
                }

                start(primaryStage);

                setGameObjectsVisible(true);
                setButtonsVisible();

            } catch (Exception e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "An error occurred in nextLevel method.", e);
            }
        });
    }

    private void removeNodeById(Pane pane, String id) {
        System.out.println("Nodes before removal: " + pane.getChildren());

        pane.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(id));

        System.out.println("Nodes after removal: " + pane.getChildren());
    }

    public void restartGame() {

        try {
            music.playMusic(false,1);

            level = 0;
            heart = 3;
            score = 0;
            vX = 1.000;
            destroyedBlockCount = 0;
            resetCollideFlags();
            goDownBall = true;

            isGoldStatus = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();

            setGameObjectsVisible(true);
            setButtonsVisible();

            start(primaryStage);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "An error occurred in restartGame method.", e);
        }
    }

    private Circle splitterBall;


    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();

        if (splitterBall != null) {
            setPhysicsToSplitterBall();
        }

        if (isSpeedBoostActive && time > speedBoostStartTime + 10000) {
            vX /= 2.0;
            vY /= 1.5;
            isSpeedBoostActive = false;
            speedBoostStartTime = 0;
        }

        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
            isGoldStatus = false;
        }

        synchronized (chocosLock) {
            for (Bonus choco : chocos) {
                if (choco.y > sceneHeight || choco.taken) {
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
        }
    }

    private void setPhysicsToSplitterBall() {
        double splitterVY = 40.000;  // Use different variable names
        double splitterVX = 50.000;

        if (goDownSplitterBall) {
            splitterBall.setCenterY(splitterBall.getCenterY() + splitterVY);
        } else {
            splitterBall.setCenterY(splitterBall.getCenterY() - splitterVY);
        }

        if (goRightSplitterBall) {
            splitterBall.setCenterX(splitterBall.getCenterX() + splitterVX);
        } else {
            splitterBall.setCenterX(splitterBall.getCenterX() - splitterVX);
        }

        if (splitterBall.getCenterY() <= 0 || splitterBall.getCenterY() >= sceneHeight) {
            goDownSplitterBall = !goDownSplitterBall;
        }

        if (splitterBall.getCenterX() >= sceneWidth || splitterBall.getCenterX() <= 0) {
            goRightSplitterBall = !goRightSplitterBall;
        }
    }



    private void duplicateBall() {
        if (splitterBall == null) {
            splitterBall = new Circle(ball.getRadius(), ball.getFill());
            splitterBall.setCenterX(xBall - 3000);
            splitterBall.setCenterY(yBall - 3000);
            root.getChildren().add(splitterBall);
            long splitterBallTimeCreated = time;

            goDownSplitterBall = goDownBall;  // Set initial direction
            goRightSplitterBall = !goRightBall;  // Set initial direction

            // Set a timer for the ball to disappear after 15 seconds
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> {
                root.getChildren().remove(splitterBall);
                splitterBall = null; // Reset reference to allow creation of a new splitter ball
            }));
            timeline.play();
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

            synchronized (chocosLock) {
                Iterator<Bonus> iterator = chocos.iterator();
                while (iterator.hasNext()) {
                    Bonus choco = iterator.next();
                    choco.choco.setY(choco.y);

                    if (choco.y > sceneHeight || choco.taken) {
                        iterator.remove();  // Remove the element using iterator
                    }
                }
            }

            synchronized (blocks) {  // Synchronize access to 'blocks'
                try {
                if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
                    for (final Block block : blocks) {
                        int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                        if (hitCode != Block.NO_HIT) {
                            Platform.runLater(() -> {
                                block.rect.setVisible(false);

                                score += 1;
                                new Score().show(block.x, block.y, 1, this);

                                block.isDestroyed = true;
                                destroyedBlockCount++;
                                resetCollideFlags();

                                if (block.type == Block.BLOCK_CHOCO) {
                                    //duplicateBall();
                                    final Bonus choco = new Bonus(block.row, block.column);
                                    choco.timeCreated = time;
                                    Platform.runLater(() -> root.getChildren().add(choco.choco));
                                    chocos.add(choco);
                                }

                                if (block.type == Block.BLOCK_STAR) {
                                    goldTime = time;
                                    ball.setFill(new ImagePattern(new Image("goldball.png")));
                                    System.out.println("gold ball");
                                    root.getStyleClass().add("goldRoot");
                                    isGoldStatus = true;
                                }

                                if (block.type == Block.BLOCK_HEART) {
                                    heart++;
                                }

                                if (block.type == Block.BLOCK_SPEED) {
                                    isSpeedBoostActive = true;
                                    speedBoostStartTime = time + 25000; // 25 seconds
                                    vX *= 2.0;
                                    vY *= 1.5;
                                }

                                if (block.type == Block.BLOCK_SPLITTER) {
                                    duplicateBall();
                                    // Hide the splitter block instead of removing it immediately
                                    Platform.runLater(() -> {
                                        block.rect.setVisible(false);
                                    });
                                    resetCollideFlags();
                                }

                                if (hitCode == Block.HIT_RIGHT) {
                                    collideToRightBlock = true;
                                } else if (hitCode == Block.HIT_BOTTOM) {
                                    collideToBottomBlock = true;
                                } else if (hitCode == Block.HIT_LEFT) {
                                    collideToLeftBlock = true;
                                } else if (hitCode == Block.HIT_TOP) {
                                    collideToTopBlock = true;
                                }
                            });
                        }
                    }
                }

                // Check if the splitter ball exists and update its position
                if (splitterBall != null) {
                    Platform.runLater(() -> {
                        splitterBall.setCenterX(xBall);
                        splitterBall.setCenterY(yBall);
                    });
                }
            } catch (IndexOutOfBoundsException e) {
                // Print out debug info
                System.out.println("Blocks size: " + blocks.size());
                e.printStackTrace();
            }
            }
        });
    }


    @Override
    public void onInit() {

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

    private void setButtonsVisible(){

        load.setVisible(false);
        newGameButton.setVisible(false);
        helpScreen.setVisible(false);
        exitGame.setVisible(false);
        vbox.setVisible(false);


    }



}
