package gg.vape.fabric.mixin;

import gg.vape.fabric.VaporFabricClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Renders Vapor world overlays during Minecraft's world render pass. */
@Mixin(value = GameRenderer.class, priority = 1)
public final class GameRendererMixin {
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void vapor421$renderWorldOverlays(DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        VaporFabricClient.renderWorld();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void vapor421$beginRender(DeltaTracker deltaTracker, boolean tick, CallbackInfo callbackInfo) {
        VaporFabricClient.beginRender(deltaTracker);
    }

    /**
     * This is after GuiRenderer and RenderBuffers have completed. HUD mods such
     * as Jade defer their tooltip into those passes, so Vapor must draw here to
     * remain visible above them.
     */
    @Inject(method = "render", at = @At("TAIL"))
    private void vapor421$renderAfterNativeHud(DeltaTracker deltaTracker, boolean tick, CallbackInfo callbackInfo) {
        VaporFabricClient.finishRenderCurrentFrame();
    }
}