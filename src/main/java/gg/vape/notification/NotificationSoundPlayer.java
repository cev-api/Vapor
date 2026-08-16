package gg.vape.notification;

import gg.vape.Vapor;
import gg.vape.notification.SoundClip;
import java.util.concurrent.atomic.AtomicReference;

public class NotificationSoundPlayer {
    private static int[] controlFlowMarker;
    private final AtomicReference<SoundClip> pendingSound = new AtomicReference<SoundClip>();

    public NotificationSoundPlayer() {
        this.startSoundThread();
    }

    static {
        if (NotificationSoundPlayer.getControlFlowMarker() == null) {
            NotificationSoundPlayer.setControlFlowMarker(new int[3]);
        }
    }

    public void playPendingSound() {
        if (this.pendingSound.get() != null) {
            SoundClip sound = this.pendingSound.get();
            this.pendingSound.set(null);
            if (!this.isMuted()) {
                sound.play(this.getVolumePercent());
            }
        }
    }

    public boolean isMuted() {
        return Vapor.INSTANCE.getPublicProfileSettings().muted.getEffectiveValue();
    }

    public static int[] getControlFlowMarker() {
        return controlFlowMarker;
    }

    public static void setControlFlowMarker(int[] marker) {
        controlFlowMarker = marker;
    }


    public float getVolumePercent() {
        return ((Double)Vapor.INSTANCE.getPublicProfileSettings().volume.getValue()).floatValue();
    }

    public void queue(SoundClip sound) {
        this.pendingSound.set(sound);
    }

    public void startSoundThread() {
        new Thread(this::runSoundLoop, "Vapor notification sound player").start();
    }

    private void runSoundLoop() {
        while (!Vapor.INSTANCE.enabled) {
            try {
                Thread.sleep(100L);
                this.playPendingSound();
            }
            catch (Exception exception) {
                Vapor.logThrowable(exception);
            }
        }
    }
}
