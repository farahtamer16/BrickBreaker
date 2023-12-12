package brickGame;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.List;
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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import javafx.util.Duration;

/**
 * The Main class represents the main entry point for the Brick Breaker game and contains the main methods used
 * in controlling the game.
 * It extends the Application class and implements GameEngine.OnAction interfaces.
 * @see <a href="https://github.com/kooitt/CourseworkGame/blob/master/src/main/java/brickGame/Main.java">Main.java on GitHub</a>
 */
public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

    /**
     * Default constructor for the Main class.
     * This constructor is used to create an instance of the Main class.
     */
    public Main() {
    }

    /**
     * Current level of the game.
     */
    private int level = 0;

    /**
     * X-coordinate of the breaker paddle.
     */
    private double xBreak = 0.0f;

    /**
     * Center X-coordinate of the breaker paddle.
     */
    private double centerBreakX;

    /**
     * Y-coordinate of the breaker paddle.
     */
    private double yBreak = 640.0f;
    /**
     * Width of the breaker paddle.
     */
    private final int breakWidth = 130;

    /**
     * Height of the breaker paddle.
     */
    private final int breakHeight = 30;

    /**
     * Half of the width of the breaker paddle.
     */
    private final int halfBreakWidth = breakWidth / 2;

    /**
     * Width of the game scene.
     */
    private final int sceneWidth = 500;
    /**
     * Height of the game scene.
     */
    private final int sceneHeight = 700;

    /**
     * Constant representing left direction.
     */
    private static final int LEFT  = 1;
    /**
     * Constant representing right direction.
     */
    private static final int RIGHT = 2;

    /**
     * Circle representing the game ball.
     */
    private Circle ball;
    /**
     * X-coordinate of the game ball.
     */
    private double xBall;

    /**
     * Y-coordinate of the game ball.
     */
    private double yBall;

    /**
     * Flag indicating whether the splitter ball should move down.
     */
    private boolean goDownSplitterBall = true;

    /**
     * Flag indicating whether the splitter ball should move right.
     */
    private boolean goRightSplitterBall = true;

    /**
     * Flag indicating whether the gold status is active.
     */
    private boolean isGoldStatus = false;

    /**
     * Flag indicating whether a heart block exists.
     */
    private boolean isExistHeartBlock = false;

    /**
     * Rectangle representing the game ball.
     */
    private Rectangle rect;

    /**
     * Radius of the game ball.
     */
    private final int ballRadius = 10;

    /**
     * Number of destroyed blocks.
     */
    private int destroyedBlockCount = 0;

    /**
     * Velocity of the game ball.
     */
    private double v = 1.000;

    /**
     * Number of heart lives.
     */
    private int heart = 3;

    /**
     * Current score in the game.
     */
    private int score = 0;
    /**
     * Current time in the game.
     */
    private long time = 0;

    /**
     * Time when the last block was hit.
     */
    private long hitTime = 0;

    /**
     * Time when gold status was activated.
     */
    private long goldTime = 0;

    /**
     * Lock object for managing chocos.
     */
    private final Object chocosLock = new Object();

    /**
     * Game engine responsible for game actions.
     */
    private GameEngine engine;
    /**
     * File path for saving game data: "D:/save/save.mdds".
     */
    public static String savePath    = "D:/save/save.mdds";
    /**
     * Directory path for saving game data: "D:/save/".
     */
    public static String savePathDir = "D:/save/";
    /**
     * List of blocks in the game.
     */
    private final ArrayList<Block> blocks = new ArrayList<>();

    /**
     * List of bonus chocos in the game.
     */
    public ArrayList<Bonus> chocos = new ArrayList<>();

    /**
     * Color scheme for the blocks.
     */
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
     * Root pane of the game.
     */
    public  Pane root;

    /**
     * Label displaying the current score.
     */
    private Label scoreLabel;

    /**
     * Label displaying the current number of heart lives.
     */
    private Label heartLabel;

    /**
     * Label displaying the current level.
     */
    private Label levelLabel;

    /**
     * Flag indicating whether to load the game state from a saved file.
     */
    private boolean loadFromSave = false;
    /**
     * Music object responsible for playing background music.
     */
    private Music music;
    /**
     * Music object responsible for playing a different background music.
     */
    private Music music2;
    /**
     * Primary stage of the JavaFX application.
     */
    Stage primaryStage;

    /**
     * Button to load a saved game.
     */
    Button load = null;

    /**
     * Button to start a new game.
     */
    Button newGameButton = null;

    /**
     * Button to display the help screen.
     */
    Button helpScreen = null;

    /**
     * Button to exit the game.
     */
    Button exitGame = null;

    /**
     * Vertical box containing buttons.
     */
    VBox vbox = null;

    /**
     * Toggle button to control music playback.
     */
    ToggleButton musicButton = new ToggleButton();

    /**
     * Flag indicating the state of background music (playing or paused).
     */
    private boolean musicState = true;

    /**
     * Flag indicating whether the speed boost is active.
     */
    private boolean isSpeedBoostActive = false;

    /**
     * Time when the speed boost started.
     */
    private long speedBoostStartTime;

    /**
     * The main entry point for the application.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Override of the start method from the Application class.
     * This method is called when the application is launched.
     * This method starts the game and handles UI elements, and button action as opposed to in the source code.
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize UI elements
        root = new Pane();
        load = new Button("Load Game");
        newGameButton = new Button("Start New Game");
        helpScreen = new Button("How to play");
        exitGame = new Button("Exit game");

        // Initialize music elements
        music = new Music();
        music.playMusic(musicState,1);
        musicButton = new ToggleButton();

        // Set action for music toggle button
        musicButton.setOnAction(event -> {
            music.playMusic(!musicButton.isSelected(),1);
            if(musicButton.isSelected()){
                musicState = false;
            }
        });

        // Initialize and configure the vertical box for UI elements
        vbox = new VBox(newGameButton, load, helpScreen, exitGame, musicButton);
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));
        vbox.setId("backgroundBox");
        vbox.setFillWidth(true);

        // Create a stack pane with the vertical box
        StackPane stackPane = new StackPane(vbox);
        stackPane.setMinSize(sceneWidth, sceneHeight);
        stackPane.setId("pane");

        // Increment the level if not loading from a saved game
        if(!loadFromSave){
            level++;
        }

        // Initialize labels for score, level, and heart
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 70);

        // Game initialization logic based on the level and loading status
        if (!loadFromSave) {
            if (level > 1) {
                new Score().showMessage("Level Up!", this);
            }
            if (level == 18) {
                new Score().showWin(this);
                return;
            }

            // Initialize game objects
            ball = new Circle();  // Initialize the 'ball' variable
            rect = new Rectangle();  // Initialize the 'rect' variable
            InitiateGame initiateGame = new InitiateGame();
            initiateGame.initBall(ballRadius, ball);
            xBall = initiateGame.setXBall(sceneWidth);
            yBall = initiateGame.setYBall(sceneHeight); // Pass only sceneHeight here
            initiateGame.initBoard(level, blocks, isExistHeartBlock);
            initiateGame.initBreak(xBreak, yBreak, breakWidth, breakHeight, rect);

            // Add game objects to the root
            root.getChildren().addAll(stackPane,rect, ball, scoreLabel, heartLabel, levelLabel);
            for (Block block : blocks) {
                root.getChildren().add(block.rect);
            }

            // Set visibility of game objects
            setGameObjectsVisible(false);

            // Create and configure the game scene
            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            scene.getStylesheets().add("style.css");
            scene.setOnKeyPressed(this);

            // Set up primary stage
            primaryStage.setTitle("Game");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Start the game engine for levels 2 to 17
            if (level > 1 && level < 18) {
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(240);
                engine.start();

                // Set visibility of buttons and game objects
                setButtonsVisible();
                setGameObjectsVisible(true);
            }

            // Set actions for load, help, and exit buttons
            load.setOnAction(event -> {
                loadGame();
                setButtonsVisible();
                setGameObjectsVisible(true);
            });
            helpScreen.setOnAction(actionEvent -> primaryStage.setScene(new HelpScreenView().view(primaryStage, false, music)));
            exitGame.setOnAction(actionEvent -> System.exit(1));

            // Set actions for the new game button
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

            // Set visibility of game objects
            setGameObjectsVisible(false);

            // Create and configure the game scene
            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            scene.getStylesheets().add("style.css");
            scene.setOnKeyPressed(this);

            // Set up primary stage
            primaryStage.setTitle("Game");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Start the game engine for loaded game
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;

            // Set visibility of game objects and buttons
            setGameObjectsVisible(true);
            setButtonsVisible();

        }
    }

    /**
     * Handles key events, specifically responding to arrow keys and 'S' key.
     *
     * @param event The KeyEvent triggered by user input.
     */
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
                break;
            case S:
                saveGame();
                break;
        }
    }

    /**
     * Moves the break (paddle) in the specified direction using an AnimationTimer.
     * Uses JavaFX's AnimationTimer for handling animation frames instead of creating threads like in the source code.
     * @param direction The direction in which to move the break. Should be either LEFT or RIGHT.
     */
    private void move(final int direction) {
        AnimationTimer animationTimer = new AnimationTimer() {
            int frames = 0;

            /**
             * Handles the animation frames to move the break.
             *
             * @param now The current timestamp in nanoseconds.
             */
            @Override
            public void handle(long now) {
                // Check if the break has reached the right edge and the direction is RIGHT
                if (xBreak == (sceneWidth - breakWidth) && direction == RIGHT) {
                    stop(); // Stop the animation if the break is at the right edge
                } else if (xBreak == 0 && direction == LEFT) {
                    stop(); // Stop the animation if the break is at the left edge
                } else {
                    // Move the break based on the specified direction
                    if (direction == RIGHT) {
                        xBreak++;
                    } else {
                        xBreak--;
                    }
                    // Update the centerBreakX based on the new break position
                    centerBreakX = xBreak + halfBreakWidth;
                }

                // Increment the frame counter
                frames++;

                // Stop the animation after 30 frames to prevent continuous movement
                if (frames >= 30) {
                    stop();
                }
            }
        };

        // Start the animation timer
        animationTimer.start();
    }

    /**
     * Flag indicating whether the ball should move downward.
     */
    private boolean goDownBall = true;

    /**
     * Flag indicating whether the ball should move to the right.
     */
    private boolean goRightBall = true;

    /**
     * Flag indicating whether the ball has collided with the break.
     */
    private boolean collideToBreak = false;

    /**
     * Flag indicating whether the ball has collided with the break and should move to the right.
     */
    private boolean collideToBreakAndMoveToRight = true;

    /**
     * Flag indicating whether the ball has collided with the right wall.
     */
    private boolean collideToRightWall = false;

    /**
     * Flag indicating whether the ball has collided with the left wall.
     */
    private boolean collideToLeftWall = false;

    /**
     * Flag indicating whether the ball has collided with a block on the right side.
     */
    private boolean collideToRightBlock = false;

    /**
     * Flag indicating whether the ball has collided with a block on the bottom side.
     */
    private boolean collideToBottomBlock = false;

    /**
     * Flag indicating whether the ball has collided with a block on the left side.
     */
    private boolean collideToLeftBlock = false;

    /**
     * Flag indicating whether the ball has collided with a block on the top side.
     */
    private boolean collideToTopBlock = false;

    /**
     * The velocity of the ball in the x-direction.
     */
    private double vX = 1.000;

    /**
     * The velocity of the ball in the y-direction.
     */
    private double vY = 1.000;

    /**
     * Resets all collision flags to their initial states. Same as in the source code.
     */
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

    /**
     * Updates the position and physics of the ball based on collisions and game logic.
     *
     * <p>
     * The method adjusts the position of the ball, handles collisions with the break, walls, and blocks,
     * and updates the game state accordingly.
     * </p>
     */
    private void setPhysicsToBall() {

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

        // Handling ball reaching the top and bottom of the scene
        if (yBall <= 0) {
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

        // Handling collisions with the break
        if (yBall >= yBreak - ballRadius) {
            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                hitTime = time;
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / ((double) breakWidth / 2);

                if (Math.abs(relation) <= 0.3) {
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);
                }

                collideToBreakAndMoveToRight = xBall - centerBreakX > 0;
            }
        }

        // Handling collisions with the right and left walls
        if (xBall >= sceneWidth) {
            resetCollideFlags();
            collideToRightWall = true;
        }

        if (xBall <= 0) {
            resetCollideFlags();
            collideToLeftWall = true;
        }

        // Handling various collision scenarios
        if (collideToBreak) {
            goRightBall = collideToBreakAndMoveToRight;
        }

        //Wall Collide
        if (collideToRightWall) {
            goRightBall = false;
        }

        if (collideToLeftWall) {
            goRightBall = true;
        }

        //Block Collide
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

    /**
     * Flag indicating whether the current game level has been successfully completed.
     */
    private boolean isLevelCompleted = false;

    /**
     * Checks the count of destroyed blocks to determine if the current level is completed.
     * If all blocks are destroyed and the level is not already completed, it sets a flag
     * and displays a congratulatory message with options to play the next level or exit the game.
     */
    private void checkDestroyedCount() {
        if (!isLevelCompleted && destroyedBlockCount == blocks.size()) {
            isLevelCompleted = true; // Set the flag to prevent multiple execution

            Platform.runLater(() -> {
                // Check if there is already a StackPane with ID "pane3"
                StackPane existingPane = (StackPane) root.lookup("#pane3");

                if (existingPane == null) {
                    // Create UI elements for the congratulatory message
                    Label label = new Label("Congratulations! Play the next level?");
                    label.getStylesheets().add("style.css");
                    label.setId("nextLevelLabel");

                    Label label1 = new Label("Current level: " + level);
                    label1.getStylesheets().add("style.css");
                    label1.setId("currentLevelLabel");

                    Button nextLevel = new Button("Next Level");
                    nextLevel.setOnAction(event -> nextLevel());

                    Button exitGame = new Button("Exit Game");
                    exitGame.setOnAction(actionEvent -> System.exit(1));

                    // Create and configure the VBox for the congratulatory message
                    VBox vbox = new VBox(label, label1, nextLevel, exitGame);
                    vbox.setAlignment(Pos.CENTER);
                    vbox.setSpacing(20);
                    vbox.setPadding(new Insets(20));
                    vbox.setId("nextLevelBox");
                    vbox.setFillWidth(true);

                    // Create and configure the StackPane for the entire congratulatory message
                    StackPane stackPane = new StackPane(vbox);
                    stackPane.setMinSize(500, 700);
                    stackPane.setId("pane3");

                    // Add the StackPane to the root if it doesn't already exist
                    root.getChildren().addAll(stackPane);
                }
                isLevelCompleted = false; // Reset the flag for the next level
            });
        }
    }

    /**
     * Saves the current game state to a file asynchronously.
     *
     * <p>
     * This method creates a new thread to perform the save operation in the background.
     * It writes various game parameters and states, including the level, score, heart count,
     * block destruction count, ball and break positions, collision flags, and block information,
     * to a file using ObjectOutputStream.
     * </p>
     *
     * @see BlockSerializable
     */
    private void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                // Writing game parameters and states to the file
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

                // Writing block information to the file
                ArrayList<BlockSerializable> blockSerializable = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializable.add(new BlockSerializable(block.row, block.column, block.type));
                }
                outputStream.writeObject(blockSerializable);

                // Show a message indicating successful game save
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

    /**
     * Loads a previously saved game state from a file.
     *
     * <p>
     * This method uses the LoadSave class to read the saved game state from a file.
     * It then sets the game parameters and states accordingly, including the level, score, heart count,
     * block destruction count, ball and break positions, collision flags, and block information.
     * After loading, the game is started or resumed from the loaded state.
     * </p>
     *
     * @see BlockSerializable
     */
    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        try {
            loadSave.read();

            // Setting game parameters and states from the loaded save
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

            // Reconstructing blocks from the loaded block information
            for (BlockSerializable ser : loadSave.blocks) {
                int r = new Random().nextInt(200);
                blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
            }

            // Attempting to start the game from the loaded state
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

    /**
     * Initiates the transition to the next game level.
     *
     * <p>
     * This method is called when the player chooses to proceed to the next level after completing the current one.
     * It performs various tasks, such as stopping the current game engine, resetting collision flags,
     * initializing game parameters, clearing existing blocks and power-ups, and explicitly removing a specific block type.
     * After the necessary setup, it starts the next level and makes the game objects and buttons visible.
     * </p>
     */
    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                Platform.runLater(() -> {
                    removeNodeById(root, "pane3");
                });

                // Play background music for the next level
                music.playMusic(false, 1);

                // Play additional music (music2) for variety
                music2 = new Music();
                music2.playMusic(true, 2);

                // Reset ball velocity for the next level
                vX = 1.000;

                // Stop the current game engine and reset certain game parameters
                engine.stop();
                resetCollideFlags();
                goDownBall = true;

                // Reset special game statuses
                isGoldStatus = false;
                isExistHeartBlock = false;

                // Reset time-related variables
                hitTime = 0;
                time = 0;
                goldTime = 0;

                // Stop the engine and clear existing blocks and power-ups
                engine.stop();
                blocks.clear();
                chocos.clear();
                destroyedBlockCount = 0;

                // Remove a specific block type (BLOCK_SPLITTER) explicitly
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

                // Start the next level
                start(primaryStage);

                // Make game objects and buttons visible
                setGameObjectsVisible(true);
                setButtonsVisible();

            } catch (Exception e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "An error occurred in nextLevel method.", e);
            }
        });
    }

    /**
     * Removes a node with a specific ID from a given Pane.
     *
     * <p>
     * This method removes a node with the specified ID from the provided Pane. It iterates through the children of the
     * Pane, checks if each node has a non-null ID that matches the specified ID, and removes the node if a match is found.
     * After removal, it prints the list of nodes in the Pane before and after the removal for debugging purposes.
     * </p>
     *
     * @param pane The Pane from which to remove the node.
     * @param id   The ID of the node to be removed.
     *            <p>
     *            Example Usage:
     *            </p>
     *            <pre>
     *            {@code
     *            // Remove a node with ID "pane3" from the root Pane
     *            removeNodeById(root, "pane3");
     *            }
     *            </pre>
     */
    private void removeNodeById(Pane pane, String id) {
        System.out.println("Nodes before removal: " + pane.getChildren());

        // Remove nodes with the specified ID
        pane.getChildren().removeIf(node -> node.getId() != null && node.getId().equals(id));

        System.out.println("Nodes after removal: " + pane.getChildren());
    }

    /**
     * Restarts the game to the initial state.
     *
     * <p>
     * This method is called to reset the game to its initial state, including setting the level to 0, restoring the initial
     * number of hearts, resetting the score, velocity, and other game parameters. It also clears existing blocks and power-ups,
     * resets collision flags, and sets special game statuses to their default values. After the reset, it makes game objects
     * and buttons visible and starts the game.
     * </p>
     *
     */
    public void restartGame() {

        try {
            music.playMusic(false,1);

            // Reset game parameters to initial values
            level = 0;
            heart = 3;
            score = 0;
            vX = 1.000;
            destroyedBlockCount = 0;
            resetCollideFlags();
            goDownBall = true;

            // Reset special game statuses and time-related variables
            isGoldStatus = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            // Clear existing blocks and power-ups
            blocks.clear();
            chocos.clear();

            // Make game objects and buttons visible
            setGameObjectsVisible(true);
            setButtonsVisible();

            // Start the game
            start(primaryStage);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "An error occurred in restartGame method.", e);
        }
    }

    private Circle splitterBall;

    /**
     * Handles the physics update for the game, including ball movement, collisions, power-ups, and time-related events.
     *
     * <p>
     * This method is called during each physics update of the game engine. It checks for the count of destroyed blocks,
     * updates the physics for the main ball, and handles additional physics for a special splitter ball if it exists.
     * It also manages speed boost effects, resets the ball appearance after a gold status period, and handles the movement
     * and scoring logic for bonus power-ups (chocos). The method is synchronized to ensure thread safety when dealing with chocos.
     * </p>
     */
    @Override
    public void onPhysicsUpdate() {
        // Check if the level is completed based on destroyed block count
        checkDestroyedCount();

        // Update physics for the main ball
        setPhysicsToBall();

        // If a splitter ball exists, update its physics as well
        if (splitterBall != null) {
            setPhysicsToSplitterBall();
        }

        // Handle speed boost effects and reset after a certain duration
        if (isSpeedBoostActive && time > speedBoostStartTime + 10000) {
            vX /= 2.0;
            vY /= 1.5;
            isSpeedBoostActive = false;
            speedBoostStartTime = 0;
        }

        // Reset ball appearance after a certain duration of gold status
        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
            isGoldStatus = false;
        }

        // Handle physics and scoring logic for bonus power-ups (chocos)
        synchronized (chocosLock) {
            for (Bonus choco : chocos) {
                if (choco.y > sceneHeight || choco.taken) {
                    continue;
                }

                // Check for collision with the breaker and update score if taken
                if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
                    System.out.println("You Got it and +3 score for you");
                    choco.taken = true;
                    choco.choco.setVisible(false);
                    score += 3;
                    new Score().show(choco.x, choco.y, 3, this);
                }

                // Update choco's vertical position based on time
                choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
            }
        }
    }

    /**
     * Updates the physics for the special splitter ball.
     *
     * <p>
     * This method is responsible for updating the position of the special splitter ball based on its current direction
     * (up or down) and (right or left). It uses predefined velocities for vertical and horizontal movements. The method
     * also handles boundary conditions, making the ball change direction when reaching the top or bottom of the scene
     * and reversing direction when reaching the right or left scene boundaries.
     * </p>
     */
    private void setPhysicsToSplitterBall() {
        double splitterVY = 40.000;  // Vertical velocity for the splitter ball
        double splitterVX = 50.000;  // Horizontal velocity for the splitter ball

        // Update the position of the splitter ball based on its current direction
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

        // Handle boundary conditions and change direction if necessary
        if (splitterBall.getCenterY() <= 0 || splitterBall.getCenterY() >= sceneHeight) {
            goDownSplitterBall = !goDownSplitterBall;
        }

        if (splitterBall.getCenterX() >= sceneWidth || splitterBall.getCenterX() <= 0) {
            goRightSplitterBall = !goRightSplitterBall;
        }
    }

    /**
     * Creates a duplicate of the main ball as a special splitter ball.
     *
     * <p>
     * This method creates a duplicate of the main ball, known as the splitter ball, and sets its initial position.
     * It also determines the initial direction of the splitter ball based on the main ball's direction. The method
     * sets a timer for the splitter ball to disappear after 15 seconds, and upon disappearance, it allows the creation
     * of a new splitter ball. The method ensures that only one splitter ball exists at a time.
     * </p>
     */
    private void duplicateBall() {
        if (splitterBall == null) {
            // Create a new splitter ball with the same properties as the main ball
            splitterBall = new Circle(ball.getRadius(), ball.getFill());
            splitterBall.setCenterX(xBall - 3000);
            splitterBall.setCenterY(yBall - 3000);
            root.getChildren().add(splitterBall);

            // Set initial direction of the splitter ball
            goDownSplitterBall = goDownBall;
            goRightSplitterBall = !goRightBall;

            // Set a timer for the ball to disappear after 15 seconds
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> {
                root.getChildren().remove(splitterBall);
                splitterBall = null; // Reset reference to allow creation of a new splitter ball
            }));
            timeline.play();
        }
    }

    /**
     * Updates the visual elements and game state on the UI.
     *
     * <p>
     * This method is responsible for updating various visual elements on the UI, including the score label, heart label,
     * paddle (rect), and the main ball's position. It also iterates through the list of blocks to check for collisions
     * with the ball, update the score, and handle special block types. Additionally, it handles the creation and removal
     * of bonus items (chocos) and updates their positions. The method runs on the JavaFX application thread using
     * Platform.runLater() to ensure proper synchronization with the UI components.
     * </p>
     *
     */
    @Override
    public void onUpdate() {
        try {
            Platform.runLater(() -> {
                // Update score and heart labels, paddle, and main ball position
                scoreLabel.setText("Score: " + score);
                heartLabel.setText("Heart : " + heart);
                rect.setX(xBreak);
                rect.setY(yBreak);
                ball.setCenterX(xBall);
                ball.setCenterY(yBall);

                // Synchronize access to the chocos list and update their positions
                synchronized (chocosLock) {
                    Iterator<Bonus> iterator = chocos.iterator();
                    while (iterator.hasNext()) {
                        Bonus choco = iterator.next();
                        choco.choco.setY(choco.y);

                        // Remove chocos that are out of bounds or already taken
                        if (choco.y > sceneHeight || choco.taken) {
                            iterator.remove();
                        }
                    }
                }

                // Create a copy of the blocks list to avoid Concurrent Modification
                List<Block> blocksCopy = new ArrayList<>(blocks);

                // Check for collisions with blocks and update game state
                if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
                    for (final Block block : blocksCopy) {
                        int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                        if (hitCode != Block.NO_HIT) {
                            // Handle collisions and update score, block visibility, and special block effects
                            score += 1;
                            new Score().show(block.x, block.y, 1, this);
                            block.rect.setVisible(false);
                            block.isDestroyed = true;
                            destroyedBlockCount++;
                            resetCollideFlags();

                            // Handle special block types
                            if (block.type == Block.BLOCK_CHOCO) {
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
                                Platform.runLater(() -> block.rect.setVisible(false)); // Hide the splitter block
                                resetCollideFlags();
                            }

                            // Set collision flags based on hitCode
                            if (hitCode == Block.HIT_RIGHT) {
                                collideToRightBlock = true;
                            } else if (hitCode == Block.HIT_BOTTOM) {
                                collideToBottomBlock = true;
                            } else if (hitCode == Block.HIT_LEFT) {
                                collideToLeftBlock = true;
                            } else if (hitCode == Block.HIT_TOP) {
                                collideToTopBlock = true;
                            }
                        }
                    }
                }
            });

            // Check if the splitter ball exists and update its position
            if (splitterBall != null) {
                Platform.runLater(() -> {
                    splitterBall.setCenterX(xBall);
                    splitterBall.setCenterY(yBall);
                });
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception
        }
    }

    /**
     * Initializes the game state and components.
     */
    @Override
    public void onInit() {

    }

    /**
     * Updates the game state with the given elapsed time.
     *
     * @param time Elapsed time provided by the game engine.
     */
    @Override
    public void onTime(long time) {
        this.time = time;
    }

    /**
     * Sets visibility of game objects.
     *
     * @param visible {@code true} for visible, {@code false} for hidden.
     */
    private void setGameObjectsVisible(boolean visible){
        scoreLabel.setVisible(visible);
        heartLabel.setVisible(visible);
        levelLabel.setVisible(visible);
        rect.setVisible(visible);
        ball.setVisible(visible);
    }

    /**
     * Sets visibility of game interface buttons.
     *
     */
    private void setButtonsVisible(){
        load.setVisible(false);
        newGameButton.setVisible(false);
        helpScreen.setVisible(false);
        exitGame.setVisible(false);
        vbox.setVisible(false);
    }
}
