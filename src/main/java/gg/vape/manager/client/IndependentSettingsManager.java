package gg.vape.manager.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.vape.Vapor;
import gg.vape.module.none.ClientSettings;
import gg.vape.value.Value;
import java.util.ArrayList;
import java.util.List;

public class IndependentSettingsManager {
    private final List<Value<?, ?>> values = new ArrayList();


    public JsonArray toJson() {
        JsonArray result = new JsonArray();
        for (Value<?, ?> value : this.values) {
            if (value.isDefault()) continue;
            result.add((JsonElement)value.toJson(false));
        }
        JsonObject frames = new JsonObject();
        frames.add("frames", (JsonElement)ClientSettings.INSTANCE.serializeFrameStates());
        result.add((JsonElement)frames);
        JsonObject inventoryManager = new JsonObject();
        inventoryManager.add("inventoryManager", (JsonElement)Vapor.INSTANCE.getInventoryFilterPresetRegistry().toJson());
        result.add((JsonElement)inventoryManager);
        return result;
    }

    public List<Value<?, ?>> values() {
        return this.values;
    }

    public void loadIndependentSettings(JsonArray serializedSettings) {
        if (serializedSettings.size() == 0) {
            return;
        }
        for (int index = 0; index < serializedSettings.size(); ++index) {
            JsonArray nestedArray;
            JsonElement element = serializedSettings.get(index);
            if (!element.isJsonObject() || element.isJsonNull()) continue;
            JsonObject setting = element.getAsJsonObject();
            for (Value<?, ?> value : this.values) {
                if (!value.matchesJsonId(setting)) continue;
                value.loadJson(setting);
            }
            if (setting.get("enemies") != null && !setting.get("enemies").isJsonNull()) {
                nestedArray = setting.get("enemies").getAsJsonArray();
                
            }
            if (setting.get("frames") != null && !setting.get("frames").isJsonNull()) {
                nestedArray = setting.get("frames").getAsJsonArray();
                if (!Vapor.INSTANCE.getPublicProfileSettings().framePositionsPerProfile.getEffectiveValue().booleanValue()) {
                    JsonArray wrappedFrameStates = new JsonArray();
                    wrappedFrameStates.add((JsonElement)nestedArray);
                    ClientSettings.INSTANCE.loadFrameStates(wrappedFrameStates);
                }
            }
            if (setting.get("inventoryManager") == null || setting.get("inventoryManager").isJsonNull()) continue;
            JsonObject inventoryManager = setting.get("inventoryManager").getAsJsonObject();
            Vapor.INSTANCE.getInventoryFilterPresetRegistry().loadJson(inventoryManager);
        }
    }

    public void registerValue(Value<?, ?> value) {
        this.values.add(value);
    }
}
