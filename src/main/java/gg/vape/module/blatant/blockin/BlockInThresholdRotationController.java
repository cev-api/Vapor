package gg.vape.module.blatant.blockin;

import gg.vape.module.blatant.BlockIn;
import gg.vape.rotation.ThresholdFixedRotationController;
import gg.vape.wrapper.impl.EntityPlayerSP;

public class BlockInThresholdRotationController
extends ThresholdFixedRotationController {
    private final BlockIn blockIn;

    @Override
    public void setComplete(boolean complete) {
        super.setComplete(complete);
        if (complete) {
            if (this.blockIn.getRotationClaim().release(this.blockIn)) {
                // empty if block
            }
            this.blockIn.setSavedYaw(-999.0);
        }
    }


    public BlockInThresholdRotationController(BlockIn blockIn, EntityPlayerSP player,
                                              float targetYaw, float targetPitch) {
        super(player, targetYaw, targetPitch);
        this.blockIn = blockIn;
    }
}
