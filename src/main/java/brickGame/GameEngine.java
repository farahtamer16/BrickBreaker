package brickGame;

public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    private Thread timeThread;
    private boolean isStopped = true;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Set FPS, converting it to milliseconds.
     *
     * @param fps Frames per second
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    private void updateLoop(Runnable action) {
        updateThread = new Thread(() -> {
            while (!updateThread.isInterrupted()) {
                try {
                    action.run();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Restore interrupted status
                    break;  // Exit the loop on interruption
                }
            }
        });
        updateThread.start();
    }

    private void physicsLoop(Runnable action) {
        physicsThread = new Thread(() -> {
            while (!physicsThread.isInterrupted()) {
                try {
                    action.run();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        physicsThread.start();
    }

    private void timeLoop(Runnable action) {
        timeThread = new Thread(() -> {
            try {
                while (!timeThread.isInterrupted()) {
                    onAction.onTime(time++);
                    action.run();
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        timeThread.start();
    }

    public void start() {
        time = 0;
        onAction.onInit();
        updateLoop(onAction::onUpdate);
        physicsLoop(onAction::onPhysicsUpdate);
        timeLoop(() -> {
        });
        isStopped = false;
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();

            try {
                updateThread.join(); // Wait for the thread to finish
                physicsThread.join();
                timeThread.join();
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for threads to finish: " + e.getMessage());
                Thread.currentThread().interrupt();  // Restore the interrupted status
            }
        }
    }

    private long time = 0;

    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}
