package gg.vape.wrapper.impl;

import gg.vape.Vapor;
import gg.vape.wrapper.Wrapper;

public class TooltipFlagBridge
extends Wrapper {

    public TooltipFlagBridge(Object handle) {
        super(handle);
    }

    public static TooltipFlagBridge searchTab() {
        if (ForgeVersion.MC_1_20_6.d()) {
            Vapor.notifyNativeStackTrace();
        }
        return new TooltipFlagBridge(TooltipFlagBridge.vapeInstance.getMappings().creativeTabsSearch.getSearchTab());
    }
}

