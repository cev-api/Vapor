package gg.vape.wrapper.impl;

import gg.vape.Vapor;
import gg.vape.wrapper.impl.Packet;

public class ShortStatePacketBridge
extends Packet {
    public short getTransactionId() {
        return Vapor.INSTANCE.getMappingsMapperCompat().F.getTransactionId(this.I);
    }

    public ShortStatePacketBridge(Object handle) {
        super(handle);
    }
}
