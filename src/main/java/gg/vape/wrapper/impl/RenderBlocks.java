package gg.vape.wrapper.impl;

import gg.vape.Vapor;
import gg.vape.wrapper.Wrapper;

public class RenderBlocks
extends Wrapper {
    public void setRenderAllFaces(boolean renderAllFaces) {
        Vapor.INSTANCE.getMappingsMapperCompat().renderBlocks.setRenderAllFaces(this.I, renderAllFaces);
    }

    public RenderBlocks(Object renderBlocksHandle) {
        super(renderBlocksHandle);
    }
}
