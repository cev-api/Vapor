package gg.vape.event.impl;

import gg.vape.Vapor;
import gg.vape.event.Event;
import gg.vape.event.EventListeners;
import gg.vape.mapping.MappedClasses;
import gg.vape.module.blatant.BlockIn;
import gg.vape.module.blatant.Scaffold;
import gg.vape.module.none.MouseDelayFix;
import gg.vape.module.utility.Clutch;

public class EventEntityRendererRayTrace
extends Event {
    private static Clutch clutch;
    private static final EventListeners EVENT_LISTENERS;
    private final float partialTicks;
    private static BlockIn blockIn;
    private final Object entityHandle;
    private static MouseDelayFix mouseDelayFix;
    private static Scaffold scaffold;

    public EventEntityRendererRayTrace(Object entityHandle, float partialTicks) {
        this.entityHandle = entityHandle;
        this.partialTicks = partialTicks;
    }

    @Override
    public EventListeners getListeners() {
        return EVENT_LISTENERS;
    }

    public static EventListeners getEventListeners() {
        return EVENT_LISTENERS;
    }

    @Override
    public boolean fire() {
        if (mouseDelayFix == null) {
            mouseDelayFix = Vapor.INSTANCE.getModManager().getMod(MouseDelayFix.class);
            scaffold = Vapor.INSTANCE.getModManager().getMod(Scaffold.class);
            clutch = Vapor.INSTANCE.getModManager().getMod(Clutch.class);
            blockIn = Vapor.INSTANCE.getModManager().getMod(BlockIn.class);
        }
        if (!mouseDelayFix.boolean_r() && !scaffold.boolean_r()) {
            if (!blockIn.boolean_r()) {
                if (!clutch.boolean_r()) {
                    return false;
                }
            }
        }
        return MappedClasses.z5.isInstance(this.entityHandle);
    }

    public Object getVec() {
        return Vapor.INSTANCE.getMappingsMapperCompat().Rr.jL.invokeNativeBridge(this.entityHandle, Float.valueOf(this.partialTicks));
    }


    static {
        EVENT_LISTENERS = new EventListeners();
    }
}

