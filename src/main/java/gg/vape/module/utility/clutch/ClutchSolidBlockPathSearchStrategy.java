package gg.vape.module.utility.clutch;

import gg.vape.module.utility.BlockIn;
import gg.vape.module.utility.Clutch;
import gg.vape.utils.BlockUtil;
import gg.vape.utils.datas.BlockData;
import gg.vape.wrapper.impl.Block;
import gg.vape.wrapper.impl.World;
import java.util.Vector;

public class ClutchSolidBlockPathSearchStrategy implements BlockPathSearchStrategy<BlockPlacementNode> {
    final World world;
    final Clutch clutch;
    final BlockIn blockIn;

    @Override
    public int scorePath(Vector<BlockPlacementNode> path) {
        return this.clutch != null
                ? this.clutch.computePathCost(this.world, path)
                : this.blockIn.computePathCost(this.world, path);
    }

    @Override
    public boolean isValidBlock(BlockData blockData) {
        Block block = this.world.getBlockByPos(blockData.D(), blockData.B(), blockData.G());
        return BlockUtil.f(block);
    }

    public ClutchSolidBlockPathSearchStrategy(Clutch clutch, World world) {
        this.clutch = clutch;
        this.blockIn = null;
        this.world = world;
    }

    public ClutchSolidBlockPathSearchStrategy(BlockIn blockIn, World world) {
        this.clutch = null;
        this.blockIn = blockIn;
        this.world = world;
    }

    @Override
    public int getMaxDepth() {
        return 4;
    }
}