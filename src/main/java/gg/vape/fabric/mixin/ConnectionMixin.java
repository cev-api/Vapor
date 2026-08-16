package gg.vape.fabric.mixin;

import gg.vape.event.impl.EventPacketReceive;
import gg.vape.event.impl.EventPacketSend;
import gg.vape.fabric.VaporFabricClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Bridges the old packet event bus to Fabric's normal Connection lifecycle. */
@Mixin(Connection.class)
public abstract class ConnectionMixin {
    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void vapor421$beforeSend(Packet<?> packet, ChannelFutureListener listener,
                                    boolean flush, CallbackInfo callbackInfo) {
        if (!vapor421$shouldDispatch()) {
            return;
        }
        EventPacketSend event = new EventPacketSend(this, packet);
        event.fire();
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void vapor421$beforeReceive(ChannelHandlerContext context, Packet<?> packet,
                                       CallbackInfo callbackInfo) {
        if (!vapor421$shouldDispatch()) {
            return;
        }
        EventPacketReceive event = new EventPacketReceive(this, packet);
        event.fire();
        if (event.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    /** Ignore status pings and the login/configuration handshake. */
    private boolean vapor421$shouldDispatch() {
        return VaporFabricClient.isReady()
                && ((Connection) (Object) this).getPacketListener() instanceof ClientPacketListener;
    }
}