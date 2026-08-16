package gg.vape.fabric.mixin;

import gg.vape.fabric.VaporFabricClient;

import gg.vape.event.impl.EventMotion;
import gg.vape.event.impl.EventPostLocalPlayerTick;
import gg.vape.event.impl.EventPostPlayerTick;
import gg.vape.event.impl.EventPostEntityUpdate;
import gg.vape.event.impl.EventPostMotion;
import gg.vape.event.impl.EventPreEntityUpdate;
import gg.vape.event.impl.EventPreLocalPlayerTick;
import gg.vape.event.impl.EventPreLivingTravel;
import gg.vape.event.impl.EventPrePlayerTick;
import gg.vape.event.impl.EventPreMotion;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Reinstates the player lifecycle that the injected build used to create. */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Unique private float vapor421$savedYaw;
    @Unique private float vapor421$savedPitch;
    @Unique private boolean vapor421$savedOnGround;

    @Inject(method = "tick", at = @At("HEAD"))
    private void vapor421$beforeLocalPlayerTick(CallbackInfo callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        new EventPreEntityUpdate(this).fire();
        new EventPrePlayerTick(this).fire();
        new EventPreLocalPlayerTick(this).fire();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void vapor421$afterLocalPlayerTick(CallbackInfo callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        new EventPostLocalPlayerTick(this).fire();
        new EventPostPlayerTick(this).fire();
        new EventPostEntityUpdate(this).fire();
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void vapor421$beforeTravel(CallbackInfo callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        new EventPreLivingTravel(this).fire();
    }

    @Inject(method = "sendPosition", at = @At("HEAD"))
    private void vapor421$beforeSendPosition(CallbackInfo callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        LocalPlayer player = (LocalPlayer) (Object) this;
        vapor421$savedYaw = player.getYRot();
        vapor421$savedPitch = player.getXRot();
        vapor421$savedOnGround = player.onGround();
        new EventPreMotion(this).fire();
        player.setYRot(EventMotion.getRotationYaw());
        player.setXRot(EventMotion.getRotationPitch());
        player.setOnGround(EventMotion.isOnGround());
    }

    @Inject(method = "sendPosition", at = @At("TAIL"))
    private void vapor421$afterSendPosition(CallbackInfo callbackInfo) {
        if (!VaporFabricClient.isReady()) return;
        LocalPlayer player = (LocalPlayer) (Object) this;
        new EventPostMotion(this).fire();
        player.setYRot(vapor421$savedYaw);
        player.setXRot(vapor421$savedPitch);
        player.setOnGround(vapor421$savedOnGround);
    }
}