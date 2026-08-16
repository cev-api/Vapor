package gg.vape.mapping;

import gg.vape.Vapor;
import gg.vape.event.impl.EventPostRenderHand;
import gg.vape.event.impl.EventPreRenderHand;
import gg.vape.mapping.JavassistMappingTask;
import gg.vape.mapping.MappedClasses;

public class RenderHandEventMappingTask
extends JavassistMappingTask {
    @Override
    public void transform() {
        if (Vapor.INSTANCE.getMappings().RY.x != null && !Vapor.INSTANCE.getMappings().RY.x.hasResolutionFailed()) {
            this.c(Vapor.INSTANCE.getMappings().RY.x, EventPreRenderHand.class, "");
            this.k(Vapor.INSTANCE.getMappings().RY.x, EventPostRenderHand.class, "");
        }
    }

    public RenderHandEventMappingTask() {
        super(MappedClasses.lt);
    }

}

