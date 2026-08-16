package gg.vape.module.blatant.blockin;

import gg.vape.module.blatant.BlockIn;
import gg.vape.module.utility.clutch.BlockPathSearchStrategy;
import gg.vape.module.utility.clutch.ClutchPlacementPathUtils;
import gg.vape.module.utility.clutch.PlacementTarget;
import gg.vape.utils.BlockUtil;
import gg.vape.utils.Vec3d;
import gg.vape.utils.datas.BlockData;
import gg.vape.wrapper.impl.Block;
import gg.vape.wrapper.impl.EntityPlayerSP;
import gg.vape.wrapper.impl.World;
import java.util.ArrayList;
import java.util.Vector;

public class BlockInPlacementSearchStrategy implements BlockPathSearchStrategy<PlacementTarget> {
    private final int extraDepth;
    private final World world;
    private final EntityPlayerSP player;
    private final ArrayList<Vec3d> candidatePositions;
    private final BlockIn blockIn;
    private final int verticalDepth;
    private final int baseDepth;

    public BlockInPlacementSearchStrategy(BlockIn blockIn, int baseDepth, int verticalDepth, int extraDepth,
                                          World world, EntityPlayerSP player,
                                          ArrayList<Vec3d> candidatePositions) {
        this.blockIn = blockIn;
        this.baseDepth = baseDepth;
        this.verticalDepth = verticalDepth;
        this.extraDepth = extraDepth;
        this.world = world;
        this.player = player;
        this.candidatePositions = candidatePositions;
    }

    @Override
    public boolean isValidBlock(BlockData blockData) {
        Block block = this.world.getBlockByPos(blockData.D(), blockData.B(), blockData.G());
        return BlockUtil.u(block);
    }

    @Override
    public int scorePath(Vector<PlacementTarget> path, int depth) {
        return this.blockIn.scorePlacementPath(this.candidatePositions, this.world, path);
    }

    @Override
    public int getMaxDepth() {
        return this.baseDepth + this.verticalDepth + this.extraDepth;
    }

    @Override
    public boolean canVisit(BlockData blockData) {
        return ClutchPlacementPathUtils.isPlacementSpaceClear(this.world, this.player, blockData)
                && !this.blockIn.getPlacedBlocks().Y(blockData);
    }
}
