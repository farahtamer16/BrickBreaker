package brickGame;

public class GameEngine {

    private OnAction onAction;

    // Delay between frames in milliseconds (derived from FPS)
    private int fpsDelayMs = 1000 / 15;

    private Thread updateThread;
    private Thread physicsThread;
    private Thread timeThread;

    public boolean isStopped = true;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps frames per second; converts to milliseconds per frame
     */
    public void setFps(int fps) {
        if (fps <= 0) fps = 15;
        this.fpsDelayMs = 1000 / fps; // integer division is fine here
    }

    private synchronized void startUpdateLoop() {
        updateThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.onUpdate();
                    Thread.sleep(fpsDelayMs);
                }
            } catch (InterruptedException e) {
                // restore interrupt flag and exit
                Thread.currentThread().interrupt();
            }
        }, "GameEngine-Update");
        updateThread.start();
    }

    private synchronized void startPhysicsLoop() {
        physicsThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.onPhysicsUpdate();
                    Thread.sleep(fpsDelayMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "GameEngine-Physics");
        physicsThread.start();
    }

    private void initialize() {
        onAction.onInit();
    }

    private long time = 0;

    private void startTimeLoop() {
        timeThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "GameEngine-Time");
        timeThread.start();
    }

    public void start() {
        if (onAction == null) {
            throw new IllegalStateException("OnAction not set");
        }
        if (!isStopped) return;

        isStopped = false;
        time = 0;

        initialize();
        startUpdateLoop();
        startPhysicsLoop();
        startTimeLoop();
    }

    public void stop() {
        if (isStopped) return;
        isStopped = true;

        // signal threads to stop
        if (updateThread  != null) updateThread.interrupt();
        if (physicsThread != null) physicsThread.interrupt();
        if (timeThread    != null) timeThread.interrupt();

        // (optional) join briefly so they wind down cleanly
        joinQuietly(updateThread);
        joinQuietly(physicsThread);
        joinQuietly(timeThread);

        updateThread = physicsThread = timeThread = null;
    }

    private static void joinQuietly(Thread t) {
        if (t == null) return;
        try {
            t.join(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public interface OnAction {
        void onUpdate();
        void onInit();
        void onPhysicsUpdate();
        void onTime(long time);
    }
}
