package App;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import App.App;

public class SlideshowTimer extends Menu {
    private final ScheduledExecutorService scheduledFuture;
    private long delay = 2500;
    private boolean randomize = false;

    public SlideshowTimer() {
        scheduledFuture = Executors.newScheduledThreadPool(1);
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
