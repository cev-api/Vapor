package gg.vape.event.listener;

import gg.vape.Vapor;
import gg.vape.event.EventHandler;
import gg.vape.event.EventListener;
import gg.vape.event.impl.EventPreRenderTick;
import gg.vape.event.impl.EventPreTick;
import gg.vape.module.none.ClientSettings;
import gg.vape.utils.render.RenderUtils;

public class ClientSettingsEventForwarder
implements EventListener {
    private ClientSettings clientSettings;


    @EventHandler
    public void onPreRenderTick(EventPreRenderTick eventPreRenderTick) {
        ClientSettings clientSettings = this.getClientSettings();
        if (clientSettings == null) {
            return;
        }
        RenderUtils.C();
        clientSettings.updateGuiScale();
    }

    private ClientSettings getClientSettings() {
        if (this.clientSettings == null) {
            this.clientSettings = Vapor.INSTANCE.getModManager().getMod(ClientSettings.class);
        }
        return this.clientSettings;
    }

    @EventHandler
    public void onTick(EventPreTick eventPreTick) {
        ClientSettings clientSettings = this.getClientSettings();
        if (clientSettings == null) {
            return;
        }
        clientSettings.onTick();
    }
}

