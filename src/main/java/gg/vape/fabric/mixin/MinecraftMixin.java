package gg.vape.fabric.mixin;

import gg.vape.fabric.VaporFabricClient;

import gg.vape.event.impl.EventClickMouse;
import gg.vape.event.impl.EventMouseOverUpdate;
import gg.vape.event.impl.EventWorldChange;
import gg.vape.wrapper.impl.WorldClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow public ClientLevel level;
    @Unique private ClientLevel vapor421$previousLevel;

    @Inject(method = "startAttack", at = @At("HEAD"))
    private void vapor421$beforeClickAttack(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        new EventClickMouse().fire();
    }

    /**
     * Vapor's Reach and rotation controller replace the current mouse-over
     * result after vanilla has calculated it. The injected build appended
     * this exact callback to Minecraft.pick(float).
     */
    @Inject(method = "pick", at = @At("TAIL"))
    private void vapor421$afterPick(float partialTick, CallbackInfo callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        new EventMouseOverUpdate(partialTick).fire();
    }

    @Inject(method = "setLevel", at = @At("HEAD"))
    private void vapor421$capturePreviousLevel(ClientLevel level, CallbackInfo callbackInfo) {
        vapor421$previousLevel = this.level;
    }

    @Inject(method = "setLevel", at = @At("TAIL"))
    private void vapor421$afterWorldChange(ClientLevel level, CallbackInfo callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        new EventWorldChange(vapor421$previousLevel == null ? null : new WorldClient(vapor421$previousLevel),
                level == null ? null : new WorldClient(level)).fire();
    }
}