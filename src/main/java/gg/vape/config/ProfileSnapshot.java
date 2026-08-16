package gg.vape.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.vape.Vapor;
import gg.vape.api.ApiHttpClient;
import gg.vape.config.ConfigJsonUtils;
import gg.vape.config.Profile;
import gg.vape.config.ProfileModuleSnapshot;
import gg.vape.config.ProfileModuleSnapshotOrderComparator;
import gg.vape.module.Category;
import gg.vape.module.Mod;
import gg.vape.module.SubModule;
import gg.vape.module.render.hud.HudModule;
import gg.vape.ui.click.frame.impl.profile.ProfileSnapshotGuiBuilder;
import gg.vape.utils.NameComparator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ProfileSnapshot {
    private final List<ProfileModuleSnapshot> moduleSnapshots;
    private final ProfileSnapshotGuiBuilder guiBuilder;
    private Profile profile;

    public List<ProfileModuleSnapshot> getSortedModules(boolean includeDefaults) {
        List<ProfileModuleSnapshot> modules = this.getModules(includeDefaults);
        modules.sort(new ProfileModuleSnapshotOrderComparator());
        return modules;
    }

    public JsonObject serializeEnabledModules() {
        JsonObject enabledModules = new JsonObject();
        for (ProfileModuleSnapshot moduleSnapshot : this.getAllModules()) {
            if (moduleSnapshot.getModule() instanceof HudModule || !moduleSnapshot.isEnabled()) continue;
            enabledModules.addProperty(moduleSnapshot.getName(), Boolean.valueOf(moduleSnapshot.isEnabled()));
        }
        return enabledModules;
    }

    public ProfileSnapshotGuiBuilder getGuiBuilder() {
        return this.guiBuilder;
    }

    public void applyToProfile() {
        if (this.profile == null) {
            return;
        }
        JsonObject profileJson = this.profile.getData();
        profileJson.add("modules", this.serializeModules());
        profileJson.add("enabled", this.serializeEnabledModules());
        this.profile.updateData(profileJson);
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public JsonArray serializeModules() {
        JsonArray modulesJson = new JsonArray();
        for (ProfileModuleSnapshot moduleSnapshot : this.moduleSnapshots) {
            JsonObject moduleJson = moduleSnapshot.toJson();
            if (moduleJson == null) continue;
            modulesJson.add(moduleJson);
        }
        return modulesJson;
    }

    public List<ProfileModuleSnapshot> getModules(boolean includeDefaults) {
        ArrayList<ProfileModuleSnapshot> modules = new ArrayList<>();
        for (ProfileModuleSnapshot moduleSnapshot : this.getAllModules()) {
            if (moduleSnapshot.getModule() instanceof SubModule || moduleSnapshot.getModule().getCategory() == Category.NONE || !moduleSnapshot.hasChanges() && !includeDefaults) continue;
            modules.add(moduleSnapshot);
        }
        return modules;
    }

    public ProfileSnapshot(Profile profile, JsonArray modulesJson) {
        this.profile = profile;
        this.moduleSnapshots = new ArrayList<>();
        LinkedHashMap<String, JsonObject> moduleJsonByName = new LinkedHashMap<>();
        if (modulesJson != null) {
            for (JsonElement moduleElement : modulesJson) {
                if (moduleElement.isJsonNull() || !moduleElement.isJsonObject()) continue;
                JsonObject moduleJson = moduleElement.getAsJsonObject();
                String moduleName = ConfigJsonUtils.getString(moduleJson, "name");
                if (moduleName == null) continue;
                moduleJsonByName.put(moduleName, moduleJson);
            }
        }
        for (Mod module : Vapor.INSTANCE.getModManager().getTopLevelModules()) {
            this.moduleSnapshots.add(new ProfileModuleSnapshot(this, module, moduleJsonByName.get(module.getName())));
        }
        this.moduleSnapshots.sort(new NameComparator());
        this.guiBuilder = new ProfileSnapshotGuiBuilder(this);
    }

    public List<ProfileModuleSnapshot> getAllModules() {
        return this.moduleSnapshots;
    }

}

