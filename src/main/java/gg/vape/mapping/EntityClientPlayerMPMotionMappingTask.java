package gg.vape.mapping;

import gg.vape.Vapor;
import gg.vape.event.impl.EventMotion;
import gg.vape.event.impl.EventPostMotion;
import gg.vape.event.impl.EventPreMotion;
import gg.vape.runtime.NativeBridge;
import gg.vape.wrapper.impl.ForgeVersion;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;

public class EntityClientPlayerMPMotionMappingTask
extends JavassistMappingTask {
    private String y;
    private String q;
    private String u;
    private CtClass p;
    private String M;
    private String b;
    private MappingMethod J = null;

    public static String z(EntityClientPlayerMPMotionMappingTask entityClientPlayerMPMotionMappingTask) {
        return entityClientPlayerMPMotionMappingTask.u;
    }

    public static String Q(EntityClientPlayerMPMotionMappingTask entityClientPlayerMPMotionMappingTask) {
        return entityClientPlayerMPMotionMappingTask.b;
    }

    private static CannotCompileException a(CannotCompileException cannotCompileException) {
        return cannotCompileException;
    }

    private Object lambda$create$0() throws CannotCompileException, NotFoundException {
        this.s();
        return null;
    }

    public EntityClientPlayerMPMotionMappingTask(Class clazz) {
        super(clazz);
    }

    @Override
    public void transform() {
        this.J(this::lambda$create$0);
    }

    private void s() throws CannotCompileException, NotFoundException {
        String string;
        this.J = Vapor.INSTANCE.getMappings().q_.M;
        this.q = Vapor.INSTANCE.getMappings().RQ.n.getResolvedName();
        this.M = Vapor.INSTANCE.getMappings().RQ.n.getDescriptor();
        String string2 = ForgeVersion.MC_1_16_5.d() ? Vapor.INSTANCE.getMappings().Rr.jz.getResolvedName() : null;
        this.y = Vapor.INSTANCE.getMappings().Rr.jv.getResolvedName();
        this.u = Vapor.INSTANCE.getMappings().Rr.jT.getResolvedName();
        this.b = Vapor.INSTANCE.getMappings().Rr.U.getResolvedName();
        this.p = this.i(MappedClasses.uk);
        String string3 = EventMotion.class.getName();
        CtBehavior ctBehavior = this.F(this.J);
        this.c(this.J, EventPreMotion.class, "$0");
        this.k(this.J, EventPostMotion.class, "$0");
        boolean bl = Vapor.INSTANCE.isForgeAbsent();
        if (bl && NativeBridge.gc(string = "aaa") == null || ForgeVersion.MC_1_7_10.L() && Vapor.INSTANCE.isVanillaMinecraftPresent()) {
            bl = false;
        }
        boolean bl2 = bl;
        CtClass ctClass = this.i(MappedClasses.zc);
        ctBehavior.instrument(new EntityClientPlayerMPMotionExprEditor(this, string2, string3, bl2, ctClass));
    }

    public static String j(EntityClientPlayerMPMotionMappingTask entityClientPlayerMPMotionMappingTask) {
        return entityClientPlayerMPMotionMappingTask.y;
    }
}
