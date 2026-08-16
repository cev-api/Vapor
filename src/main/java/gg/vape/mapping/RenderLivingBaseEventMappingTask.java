package gg.vape.mapping;

import gg.vape.Vapor;
import gg.vape.event.impl.EventBlockRenderColorOverride;
import gg.vape.mapping.expr.EventBlockRenderColorOverrideExprEditor;
import gg.vape.wrapper.impl.ForgeVersion;
import javassist.CtBehavior;

public class RenderLivingBaseEventMappingTask
extends JavassistMappingTask {
    private static Exception a(Exception exception) {
        return exception;
    }

    public RenderLivingBaseEventMappingTask() {
        super(MappedClasses.Fq);
    }

    @Override
    public void transform() {
        MappingMethod mappingMethod;
        if (ForgeVersion.MC_1_8_9.d()) {
            mappingMethod = Vapor.INSTANCE.getMappings().CP.O;
            String string = EventBlockRenderColorOverride.class.getName();
            CtBehavior ctBehavior = this.F(mappingMethod);
            this.O(mappingMethod, EventBlockRenderColorOverride.class, "", "false");
            try {
                ctBehavior.instrument(new EventBlockRenderColorOverrideExprEditor(this, string));
            }
            catch (Exception exception) {
                Vapor.logThrowable(exception);
            }
        }
        if (ForgeVersion.MC_1_7_10.L() && Vapor.INSTANCE.isForgeAbsent()) {
            mappingMethod = Vapor.INSTANCE.getMappings().CP.r;
            this.c(mappingMethod, EventPreRenderEntityForgeCallback.class, "$1, $2, $3, $4");
        }
    }
}
