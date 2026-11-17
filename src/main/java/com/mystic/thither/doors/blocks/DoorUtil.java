package com.mystic.thither.doors.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Small helpers shared by the door blocks so both halves always agree.
 */
public final class DoorUtil {
    private DoorUtil() {
    }

    public static BlockPos lowerPos(BlockState state, BlockPos pos) {
        return state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
    }

    public static void setOpen(Level level, BlockPos lowerPos, BlockState lowerState, boolean open, Player player) {
        BlockState opened = lowerState.setValue(DoorBlock.OPEN, open);
        level.setBlock(lowerPos, opened, Block.UPDATE_ALL);

        BlockPos upperPos = lowerPos.above();
        BlockState upperState = level.getBlockState(upperPos);
        if (upperState.is(opened.getBlock()) && upperState.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
            level.setBlock(upperPos, upperState.setValue(DoorBlock.OPEN, open), Block.UPDATE_ALL);
        }

        level.playSound(player, lowerPos, open ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE,
                SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, lowerPos);
    }
}
