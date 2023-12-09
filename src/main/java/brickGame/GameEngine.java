package brickGame;

import javafx.animation.AnimationTimer;

public class GameEngine {

    public OnAction onAction;
    private int fps = 15;
    private Thread timeThread;
    private AnimationTimer updateTimer;
    private AnimationTimer physicsTimer;
    private final Object lock = new Object();
    public boolean isStopped = true;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Set the frames per second (fps) for the game engine.
     *
     * @param fps Frames per second
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    /**
     * Starts the game engine.
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
     * Stops the game engine.
     */
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            stopTimers();
            timeThread.interrupt();
        }
    }

    private long time = 0;

    private void Initialize() {
        onAction.onInit();
    }

    private void startUpdateTimer() {
        updateTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onAction.onUpdate();
            }
        };
        updateTimer.start();
    }

    private void startPhysicsTimer() {
        physicsTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onAction.onPhysicsUpdate();
            }
        };
        physicsTimer.start();
    }

    private void stopTimers() {
        updateTimer.stop();
        physicsTimer.stop();
    }

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
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}
