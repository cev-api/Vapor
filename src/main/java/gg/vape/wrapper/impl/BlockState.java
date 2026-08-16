package gg.vape.wrapper.impl;

import com.google.common.collect.ImmutableMap;
import gg.vape.mapping.mappings.MIBlockState;
import gg.vape.wrapper.Wrapper;

public class BlockState
extends Wrapper {
    public boolean u() {
        return BlockState.vapeInstance.getMappings().DE.d(this.I);
    }

    public boolean Y() {
        return BlockState.vapeInstance.getMappings().DE.W(this.I);
    }

    public Object I(BlockProperty blockProperty) {
        if (ForgeVersion.MC_1_20_6.d()) {
            return BlockState.vapeInstance.getMappings().DE.o(this.I, blockProperty.getObject());
        }
        ImmutableMap immutableMap = BlockState.vapeInstance.getMappings().DE.w(this.I);
        for (Object e : immutableMap.keySet()) {
            if (!e.getClass().equals(blockProperty.getObject().getClass())) continue;
            Object object = immutableMap.get(e);
            return object;
        }
        if (immutableMap.containsKey(blockProperty.getObject())) {
            Object object = immutableMap.get(blockProperty.getObject());
            return object;
        }
        return null;
    }

    public boolean x() {
        return BlockState.vapeInstance.getMappings().DE.e(this.I);
    }

    public Block getBlock() {
        return new Block(BlockState.vapeInstance.getMappings().DE.v(this.I));
    }

    public float getPlayerRelativeDestroyProgress(EntityPlayer player, World world, BlockPos blockPos) {
        Block block = this.getBlock();
        float hardness = block.c();
        if (hardness <= 0.0f) {
            return 1.0f;
        }
        ItemStack heldItem = player.getHeldItemHand();
        if (heldItem == null || heldItem.isNull()) {
            return 0.0f;
        }
        float destroySpeed = heldItem.V(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (destroySpeed <= 0.0f) {
            destroySpeed = 1.0f;
        }
        return destroySpeed / hardness / 30.0f;
    }
    public BlockState(Object object) {
        super(object);
    }

    public boolean g() {
        return BlockState.vapeInstance.getMappings().DE.I(this.I);
    }

    public BlockStateWorldBridge j() {
        return new BlockStateWorldBridge(MIBlockState.j(BlockState.vapeInstance.getMappings().DE, this.I));
    }

}

