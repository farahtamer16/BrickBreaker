package brickGame;

import javafx.animation.AnimationTimer;

/**
 * Represents the game engine responsible for managing game updates and time.
 * The GameEngine class provides functionality for starting, stopping, and
 * managing the game loop, as well as handling game initialization, updates,
 * physics updates and time updates.
 * <p>
 * Uses JavaFX's AnimationTimer for efficiency and proper integration with the JavaFX framework instead of
 * traditional thread handling mechanisms used in the source code.
 */
public class GameEngine {

    // Callback interface for defining actions in the game engine
    public OnAction onAction;

    // Frames per second (fps) for the game engine
    private int fps = 25;

    // Thread for managing time
    private Thread timeThread;

    // Timers for game update and physics update
    private AnimationTimer updateTimer;
    private AnimationTimer physicsTimer;

    // Object for synchronization
    private final Object lock = new Object();

    // Flag indicating whether the game engine is stopped
    public boolean isStopped = true;

    /**
     * Sets the callback interface for defining actions in the game engine.
     *
     * @param onAction The callback interface.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Set the frames per second (fps) for the game engine.
     *
     * @param fps Frames per second.
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    /**
     * Starts the game engine.
     * Initializes game components and starts the update and physics timers using AnimationTimer,
     * instead of the sleep mechanisms used in the source code.
     */
    public void start() {
        time = 0;
        Initialize();
        startUpdateTimer();
        startPhysicsTimer();
        TimeStart();
        isStopped = false;
    }

    /**
     * Notifies the waiting thread to wake up.
     */
    public void notifyTimeThread() {
        synchronized (lock) {
            lock.notify();
        }
    }

    /**
     * Stops the game engine by interrupting threads gracefully using AnimationTimer's stop method instead of directly
     * like in the source code.
     */
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            stopTimers();
            timeThread.interrupt();
        }
    }

    // Current time in the game
    private long time = 0;

    // Initializes the game engine
    private void Initialize() {
        onAction.onInit();
    }

    // Starts the update timer for game updates
    private void startUpdateTimer() {
        updateTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onAction.onUpdate();
            }
        };
        updateTimer.start();
    }

    // Starts the physics timer for physics updates
    private void startPhysicsTimer() {
        physicsTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onAction.onPhysicsUpdate();
            }
        };
        physicsTimer.start();
    }

    // Stops the update and physics timers
    private void stopTimers() {
        updateTimer.stop();
        physicsTimer.stop();
    }

    // Starts the time thread for managing game time
    private void TimeStart() {
        timeThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    time++;
                    onAction.onTime(time);

                    synchronized (lock) {
                        lock.wait(fps); // Use wait() instead of Thread.sleep()
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        timeThread.start();
    }

    /**
     * Interface for defining actions in the game engine.
     */
    public interface OnAction {
        /**
         * Called for game update.
         */
        void onUpdate();

        /**
         * Called for game initialization.
         */
        void onInit();

        /**
         * Called for physics update.
         */
        void onPhysicsUpdate();

        /**
         * Called for updating game time.
         *
         * @param time The current time in the game.
         */
        void onTime(long time);
    }
}
