package gg.vape.sync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import gg.vape.Vapor;
import gg.vape.config.Profile;
import gg.vape.module.none.ClientSettings;
import gg.vape.notification.SettingsSyncStatusNotification;
import gg.vape.runtime.NativeBridge;
import gg.vape.utils.Base64Util;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class SyncThread {
    private static final String LOCAL_CONFIG_FILE_NAME = "vapor.json";
    private SyncStoreRequestWorker storeRequestWorker;
    private final SyncDebounceWorker debounceWorker;
    private final AtomicBoolean pendingSave = new AtomicBoolean();
    private final Vapor vape;
    private long lastSaveTime;
    private boolean onlineSettingsApplied;
    private volatile boolean localPersistenceReady;

    public SyncThread(Vapor vape) {
        this.vape = vape;
        this.debounceWorker = new SyncDebounceWorker();
    }

    public void saveSettings() {
        // Frame visibility is part of the local profile. The Fabric client
        // creates frames on its first render pass, after the config has been
        // read. Do not overwrite a valid saved layout with an empty list
        // during that startup window.
        if (!ClientSettings.framesInitialized) {
            return;
        }
        try {
            this.prepareActiveProfileForSave();
            JsonObject settings = this.buildSettingsPayload(false);
            Path configFile = this.getLocalConfigFile();
            Files.createDirectories(configFile.getParent());
            Path temporaryFile = configFile.resolveSibling(LOCAL_CONFIG_FILE_NAME + ".tmp");
            String serialized = new GsonBuilder().setPrettyPrinting().create().toJson(settings);
            Files.writeString(temporaryFile, serialized, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            try {
                Files.move(temporaryFile, configFile, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(temporaryFile, configFile, StandardCopyOption.REPLACE_EXISTING);
            }
            for (Profile profile : this.vape.getProfilesManager().getProfiles()) {
                profile.setDirty(false);
            }
            this.lastSaveTime = System.currentTimeMillis();
        } catch (Throwable error) {
            Vapor.logThrowable(error);
        } finally {
            this.pendingSave.set(false);
        }
    }

    private Path getLocalConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("vapor").resolve(LOCAL_CONFIG_FILE_NAME);
    }
    private void prepareActiveProfileForSave() {
        try {
            Profile activeProfile = this.vape.getProfilesManager().getActiveProfile();
            if (activeProfile != null) {
                activeProfile.captureCurrentState();
            }
        }
        catch (Throwable throwable) {
            Vapor.logThrowable(throwable);
        }
    }

    public void requestSave() {
        this.lastSaveTime = System.currentTimeMillis();
        this.pendingSave.set(false);
        if (this.storeRequestWorker == null) {
            this.storeRequestWorker = new SyncStoreRequestWorker();
            new Thread(this.storeRequestWorker, "Vapor settings save worker").start();
        }
        this.storeRequestWorker.requestSave();
    }

    public boolean hasPendingSave() {
        return this.pendingSave.get();
    }

    public long getLastSaveTime() {
        return this.lastSaveTime;
    }

    public void loadConfig() {
        Path configFile = this.getLocalConfigFile();
        if (!Files.isRegularFile(configFile)) {
            return;
        }
        try {
            String serialized = Files.readString(configFile, StandardCharsets.UTF_8);
            JsonObject config = new Gson().fromJson(serialized, JsonObject.class);
            if (config != null) {
                this.vape.loadConfigData(config, false);
            }
        } catch (Throwable error) {
            Vapor.debugLog("Could not load local Vapor configuration: " + Vapor.formatThrowable(error));
        }
    }
    private void loadStandaloneConfig() {
        String encodedSettings = NativeBridge.gp("all");
        String decodedSettings = encodedSettings == null
                ? ""
                : new String(Base64Util.decodeBase64(encodedSettings), StandardCharsets.UTF_8).trim();
        JsonReader reader = new JsonReader(new StringReader(decodedSettings));
        reader.setLenient(true);
        JsonObject config = new Gson().fromJson(reader, JsonObject.class);
        if (config == null) {
            return;
        }
        this.vape.loadConfigData(config, false);
        for (Profile profile : this.vape.getProfilesManager().getProfiles()) {
            profile.setDirty(true);
        }
    }

    public void clearPendingSave() {
        this.pendingSave.set(false);
    }

    public void markDirty() {
        this.pendingSave.set(true);
        this.debounceWorker.markChanged();
        if (this.localPersistenceReady) {
            this.requestSave();
        }
    }

    public void start() {
        this.localPersistenceReady = true;
        this.markDirty();
    }

    public JsonObject buildSettingsPayload(boolean splitProfiles) {
        JsonObject payload = new JsonObject();
        
        payload.add(splitProfiles ? "otherData" : "otherdata", this.vape.getSettingsManager().toJson());
        if (!splitProfiles) {
            payload.add("profiles", this.vape.getProfilesManager().toJson(false));
        }
        return payload;
    }
}
