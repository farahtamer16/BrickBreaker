package brickGame;


public class GameEngine {

    public OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    public boolean isStopped = true;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    private synchronized void Update() {
        updateThread = new Thread(() -> {
            //try catch method to rid of interruption-exception
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.onUpdate();
                    Thread.sleep(fps);
                } }
            catch (InterruptedException e) {
                //restore interrupted status on a thread
                Thread.currentThread().interrupt();
            }

        });
        updateThread.start();
    }

    private void Initialize() {
        onAction.onInit();
    }

    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(() -> {
            //try catch method to get rid on interrupt-exception error
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.onPhysicsUpdate();
                    Thread.sleep(fps);
                }
            } catch (InterruptedException e) {
                // Restore interrupted status on a thread
                Thread.currentThread().interrupt();
            }
        });

        physicsThread.start();
    }

    public void start() {
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();
        }
    }

    private long time = 0;

    private Thread timeThread;

    private void TimeStart() {
        timeThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(fps); // Use the same fps value for consistency
                }
            } catch (InterruptedException e) {
                // Restore interrupted status and exit the thread
                Thread.currentThread().interrupt();
            }
        });
        timeThread.start();
    }


    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}
