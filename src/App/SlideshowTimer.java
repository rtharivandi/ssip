package App;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SlideshowTimer {
    private ScheduledExecutorService scheduledFuture;
    private long delay = 2500;
    private boolean randomize = false;

    public SlideshowTimer() {

    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setRandomize() {
        randomize = !randomize;
    }

    void startTimer(Runnable command) {
        scheduledFuture.scheduleAtFixedRate(command, delay, delay, TimeUnit.MILLISECONDS);
    }

    void stopTimer() {
        scheduledFuture.shutdown();
    }

    boolean isTimerDown() {
        return (scheduledFuture == null || scheduledFuture.isShutdown());
    }
}
