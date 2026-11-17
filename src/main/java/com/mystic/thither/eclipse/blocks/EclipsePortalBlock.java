package com.mystic.thither.eclipse.blocks;

import com.mystic.thither.eclipse.worldgen.dimension.EclipseDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Fills the 3x3x3 interior of the conduit-shaped frame once the heart activates.
 * Entering any of the 26 portal blocks moves the entity to the other dimension.
 */
public class EclipsePortalBlock extends Block {
    private static final String COOLDOWN = "thither_eclipse_portal_cooldown";

    public EclipsePortalBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(-1.0F)
                .randomTicks()
                .lightLevel(state -> 15)
                .sound(SoundType.GLASS)
                .noLootTable());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !entity.canChangeDimensions()) {
            return;
        }
        if (!EclipsePortalCoreBlock.validatePortal(level, pos)) {
            return;
        }
        long now = level.getGameTime();
        if (entity.getPersistentData().getLong(COOLDOWN) > now) {
            return;
        }
        ServerLevel destination = level.getServer().getLevel(EclipseDimension.isEclipseDimension(level)
                ? Level.OVERWORLD
                : EclipseDimension.ECLIPSE_WORLD);
        if (destination == null) {
            return;
        }
        entity.getPersistentData().putLong(COOLDOWN, now + 100);
        entity.setPortalCooldown(100);
        BlockPos target = safeSpot(destination, pos);
        level.playSound(null, pos, SoundEvents.PORTAL_TRAVEL, SoundSource.BLOCKS, 0.5F, 1.0F);
        if (entity instanceof ServerPlayer player) {
            player.teleportTo(destination, target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, player.getYRot(), player.getXRot());
        } else {
            entity.changeDimension(destination);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!EclipsePortalCoreBlock.validatePortal(level, pos)) {
            level.removeBlock(pos, false);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, block, fromPos, movedByPiston);
        if (!level.isClientSide && !EclipsePortalCoreBlock.validatePortal(level, pos)) {
            level.removeBlock(pos, false);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.PORTAL, x, y, z,
                    (random.nextDouble() - 0.5D) * 0.3D,
                    (random.nextDouble() - 0.5D) * 0.3D,
                    (random.nextDouble() - 0.5D) * 0.3D);
        }
    }

    private static BlockPos safeSpot(ServerLevel level, BlockPos origin) {
        int x = origin.getX();
        int z = origin.getZ();
        level.getChunk(x >> 4, z >> 4);
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        y = Math.max(y, level.getMinBuildHeight() + 2);
        y = Math.min(y, level.getMaxBuildHeight() - 3);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, y, z);
        for (int i = 0; i < 8 && !level.isEmptyBlock(cursor); i++) {
            cursor.move(0, 1, 0);
        }
        return cursor.immutable();
    }
}
