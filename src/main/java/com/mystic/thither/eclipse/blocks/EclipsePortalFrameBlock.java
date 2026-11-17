package com.mystic.thither.eclipse.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * One of the 42 blocks that make up a fully active conduit frame:
 * three intersecting 5x5 rings centred on the portal heart.
 */
public class EclipsePortalFrameBlock extends Block {
    public EclipsePortalFrameBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 12.0F)
                .lightLevel(state -> 3));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide) {
            EclipsePortalCoreBlock.activateNear(level, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            EclipsePortalCoreBlock.deactivateNear(level, pos);
        }
    }
}
