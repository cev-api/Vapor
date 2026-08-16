package gg.vape.fabric.mixin;

import gg.vape.fabric.VaporFabricClient;

import gg.vape.event.impl.EventPostAttack;
import gg.vape.event.impl.EventPreAttack;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public final class MultiPlayerGameModeMixin {
    @Inject(method = "attack", at = @At("HEAD"))
    private void vapor421$beforeAttack(Player player, Entity target, CallbackInfo callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        new EventPreAttack(target).fire();
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void vapor421$afterAttack(Player player, Entity target, CallbackInfo callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        new EventPostAttack(target).fire();
    }
}