package gg.vape.wrapper.impl;

import gg.vape.Vapor;
import gg.vape.wrapper.Wrapper;

public class GlStateManagerTexGenState
extends Wrapper {
    public static final int F;

    public static boolean p() {
        return Vapor.INSTANCE.getMappings().hs != null;
    }

    static {
        long l2 = -7022238366481794461L;
        F = (int)l2;
    }


    public static void I(int n, int n2) {
        if (Vapor.INSTANCE.getMappings().hs != null) {
            Vapor.INSTANCE.getMappings().hs.K(n, n2);
        }
    }

    public GlStateManagerTexGenState(Object object) {
        super(object);
    }

    public static int J(int n) {
        if (Vapor.INSTANCE.getMappings().hs != null) {
            return Vapor.INSTANCE.getMappings().hs.F(n);
        }
        return 0;
    }
}

