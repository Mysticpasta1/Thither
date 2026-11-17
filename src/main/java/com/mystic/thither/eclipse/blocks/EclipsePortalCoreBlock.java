package com.mystic.thither.eclipse.blocks;

import com.mystic.thither.eclipse.init.BlockInit;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * The conduit-like heart placed in the centre of the 42-block portal frame.
 */
public class EclipsePortalCoreBlock extends Block {
    private static final List<BlockPos> FRAME = new ArrayList<>();
    private static final List<BlockPos> INTERIOR = new ArrayList<>();

    static {
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    boolean onXyRing = z == 0 && (Math.abs(x) == 2 || Math.abs(y) == 2);
                    boolean onXzRing = y == 0 && (Math.abs(x) == 2 || Math.abs(z) == 2);
                    boolean onYzRing = x == 0 && (Math.abs(y) == 2 || Math.abs(z) == 2);
                    if (onXyRing || onXzRing || onYzRing) {
                        FRAME.add(new BlockPos(x, y, z));
                    } else if (Math.abs(x) <= 1 && Math.abs(y) <= 1 && Math.abs(z) <= 1 && !(x == 0 && y == 0 && z == 0)) {
                        INTERIOR.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
    }

    public EclipsePortalCoreBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(5.0F, 12.0F)
                .lightLevel(state -> 15));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide) {
            tryActivate(level, pos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, block, fromPos, movedByPiston);
        if (!level.isClientSide) {
            tryActivate(level, pos);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            boolean active = tryActivate(level, pos);
            if (!active) {
                level.playSound(null, pos, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.BLOCKS, 0.7F, 1.2F);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && !state.is(newState.getBlock()) && !newState.is(BlockInit.ECLIPSE_HEART.get())) {
            clearInterior(level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static void activateNear(Level level, BlockPos framePos) {
        BlockPos heart = findHeart(level, framePos);
        if (heart != null) {
            tryActivate(level, heart);
        }
    }

    public static void deactivateNear(Level level, BlockPos framePos) {
        BlockPos heart = findHeart(level, framePos);
        if (heart != null && !isFrameComplete(level, heart)) {
            clearInterior(level, heart);
            level.playSound(null, heart, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.BLOCKS, 0.7F, 0.9F);
        }
    }

    public static boolean tryActivate(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).is(BlockInit.ECLIPSE_HEART.get()) || !isFrameComplete(level, pos) || !isInteriorClear(level, pos)) {
            return false;
        }
        boolean changed = false;
        for (BlockPos offset : INTERIOR) {
            BlockPos target = pos.offset(offset);
            if (!level.getBlockState(target).is(BlockInit.ECLIPSE_PORTAL.get())) {
                level.setBlock(target, BlockInit.ECLIPSE_PORTAL.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                changed = true;
            }
        }
        if (changed) {
            level.playSound(null, pos, SoundEvents.CONDUIT_ACTIVATE, SoundSource.BLOCKS, 0.9F, 1.0F);
            if (level instanceof net.minecraft.server.level.ServerLevel server) {
                server.sendParticles(ParticleTypes.PORTAL, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 40, 1.2D, 1.2D, 1.2D, 0.12D);
            }
        }
        return true;
    }

    public static boolean validatePortal(Level level, BlockPos portalPos) {
        BlockPos heart = null;
        for (int x = -1; x <= 1 && heart == null; x++) {
            for (int y = -1; y <= 1 && heart == null; y++) {
                for (int z = -1; z <= 1 && heart == null; z++) {
                    BlockPos candidate = portalPos.offset(x, y, z);
                    if (level.getBlockState(candidate).is(BlockInit.ECLIPSE_HEART.get())) {
                        heart = candidate;
                    }
                }
            }
        }
        if (heart == null || !isFrameComplete(level, heart)) {
            if (heart != null) {
                clearInterior(level, heart);
            }
            return false;
        }
        if (!isInteriorClear(level, heart)) {
            clearInterior(level, heart);
            return false;
        }
        return true;
    }

    public static boolean isFrameComplete(Level level, BlockPos heart) {
        for (BlockPos offset : FRAME) {
            if (!level.getBlockState(heart.offset(offset)).is(BlockInit.ECLIPSE_PORTAL_FRAME.get())) {
                return false;
            }
        }
        return true;
    }

    private static boolean isInteriorClear(Level level, BlockPos heart) {
        for (BlockPos offset : INTERIOR) {
            BlockState state = level.getBlockState(heart.offset(offset));
            if (!state.isAir() && !state.is(BlockInit.ECLIPSE_PORTAL.get())) {
                return false;
            }
        }
        return true;
    }

    private static void clearInterior(Level level, BlockPos heart) {
        for (BlockPos offset : INTERIOR) {
            BlockPos target = heart.offset(offset);
            if (level.getBlockState(target).is(BlockInit.ECLIPSE_PORTAL.get())) {
                level.setBlock(target, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
        }
    }

    private static BlockPos findHeart(Level level, BlockPos around) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos candidate = around.offset(x, y, z);
                    if (level.getBlockState(candidate).is(BlockInit.ECLIPSE_HEART.get())) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }
}
