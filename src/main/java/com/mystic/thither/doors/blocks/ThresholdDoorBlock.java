package com.mystic.thither.doors.blocks;

import com.mystic.thither.doors.DoorDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

/**
 * The threshold between the ordinary worlds and the door dimension.
 *
 * <p>Placed in a normal world, it leads into the hub. Used inside the hub it leads back
 * to wherever the player last entered from. The same block is generated next to the
 * arrival shrine in every destination dimension so there is always a door home.</p>
 */
public class ThresholdDoorBlock extends DoorBlock {
    public ThresholdDoorBlock(BlockBehaviour.Properties properties) {
        super(properties
                        .sound(SoundType.METAL)
                        .noOcclusion()
                        .pushReaction(PushReaction.BLOCK)
                        .isValidSpawn((state, level, pos, type) -> false)
                        .isSuffocating((state, level, pos) -> false)
                        .isViewBlocking((state, level, pos) -> false)
                        .lightLevel(state -> 10),
                BlockSetType.IRON);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        BlockPos lowerPos = DoorUtil.lowerPos(state, pos);
        BlockState lowerState = level.getBlockState(lowerPos);
        if (!(lowerState.getBlock() instanceof ThresholdDoorBlock)) {
            return InteractionResult.PASS;
        }

        DoorUtil.setOpen(level, lowerPos, lowerState, true, player);
        level.playSound(null, lowerPos, SoundEvents.PORTAL_TRAVEL, SoundSource.BLOCKS, 0.5F, 1.2F);
        //temp disabled until isModDimension(level) is back
        //if (DoorDimension.isDoorDimension(level)) {
        //    DoorDimension.returnHome(serverPlayer);
        //} else if (DoorDimension.isModDimension(level)) {
        //    DoorDimension.enterHub(serverPlayer, false);
        //} else {
        //    DoorDimension.enterHub(serverPlayer, true);
        //}
        return InteractionResult.CONSUME;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(HALF) != DoubleBlockHalf.LOWER) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            level.addParticle(ParticleTypes.PORTAL,
                    pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.9D,
                    pos.getY() + random.nextDouble() * 1.9D,
                    pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.9D,
                    (random.nextDouble() - 0.5D) * 0.08D,
                    (random.nextDouble() - 0.5D) * 0.08D,
                    (random.nextDouble() - 0.5D) * 0.08D);
        }
    }
}
