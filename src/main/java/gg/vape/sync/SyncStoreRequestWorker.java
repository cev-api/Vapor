package gg.vape.sync;

import gg.vape.Vapor;
import java.util.concurrent.atomic.AtomicBoolean;

public class SyncStoreRequestWorker
implements Runnable {
    private final AtomicBoolean saveRequested = new AtomicBoolean();

    public void requestSave() {
        this.saveRequested.set(true);
    }

    @Override
    public void run() {
        while (!Vapor.INSTANCE.isEnabled()) {
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if (!this.saveRequested.get()) continue;
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            Vapor.INSTANCE.getSyncThread().saveSettings();
            this.saveRequested.set(false);
        }
    }
}

