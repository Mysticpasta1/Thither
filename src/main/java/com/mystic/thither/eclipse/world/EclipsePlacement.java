package com.mystic.thither.eclipse.world;

import com.mystic.thither.eclipse.blocks.EclipseFireBlock;
import com.mystic.thither.eclipse.init.BlockInit;
import com.mystic.thither.eclipse.init.FluidInit;
import com.mystic.thither.eclipse.init.ParticleInit;
import com.mystic.thither.eclipse.worldgen.biomes.EclipseZones;
import com.mystic.thither.eclipse.worldgen.dimension.EclipseDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public final class EclipsePlacement {
    private EclipsePlacement() {
    }

    public static boolean isEclipseFluid(Fluid fluid) {
        if (fluid == null || fluid == Fluids.EMPTY) {
            return false;
        }
        return fluid.isSame(FluidInit.NYX_WATER.get())
                || fluid.isSame(FluidInit.SOL_WATER.get())
                || fluid.isSame(FluidInit.LUX_WATER.get())
                || fluid.isSame(FluidInit.NYX_LAVA.get())
                || fluid.isSame(FluidInit.SOL_LAVA.get())
                || fluid.isSame(FluidInit.LUX_LAVA.get());
    }

    /**
     * @return the state to place, or null when the placement is refused
     */
    public static BlockState rewrite(Level level, BlockPos pos, BlockState state) {
        if (state == null) {
            return null;
        }
        if (!EclipseDimension.isEclipseDimension(level)) {
            return confine(state);
        }
        Block block = state.getBlock();
        if (isForeignFire(block)) {
            return fire(level, pos).shaped(level, pos);
        }
        FluidState fluid = state.getFluidState();
        if (!fluid.isEmpty() && !isEclipseFluid(fluid.getType())) {
            if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
                voidParticles(level, pos);
                return state.setValue(BlockStateProperties.WATERLOGGED, false);
            }
            if (isForeignFluidBlock(block, state)) {
                voidParticles(level, pos);
                return foreignFluidResult(level, pos);
            }
        }
        return state;
    }

    /**
     * Water, Lava, and Fire exist only in the Eclipse. Outside it, the placement is refused.
     */
    private static BlockState confine(BlockState state) {
        if (state.getBlock() instanceof EclipseFireBlock) {
            return null;
        }
        FluidState fluid = state.getFluidState();
        if (fluid.isEmpty() || !isEclipseFluid(fluid.getType())) {
            return state;
        }
        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
            return state.setValue(BlockStateProperties.WATERLOGGED, false);
        }
        return null;
    }

    public static void voidParticles(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel server)) {
            return;
        }
        server.sendParticles(ParticleInit.VOID.get(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 20, 0.32D, 0.32D, 0.32D, 0.01D);
    }

    /**
     * A solid turning into a foreign fluid (ice melt, modded conversion) becomes air.
     * A fluid placed into air or into an existing fluid is refused, so buckets stay full.
     */
    private static BlockState foreignFluidResult(Level level, BlockPos pos) {
        BlockState current = level.getBlockState(pos);
        if (current.isAir() || isForeignFluidBlock(current.getBlock(), current) || isEclipseFluid(current.getFluidState().getType())) {
            return null;
        }
        if (!current.getFluidState().isEmpty()
                && !(current.hasProperty(BlockStateProperties.WATERLOGGED) && current.getValue(BlockStateProperties.WATERLOGGED))) {
            return null;
        }
        return Blocks.AIR.defaultBlockState();
    }

    private static boolean isForeignFluidBlock(Block block, BlockState state) {
        if (block instanceof LiquidBlock || block instanceof BubbleColumnBlock) {
            return true;
        }
        return state.getFluidState().isSource() && !state.hasProperty(BlockStateProperties.WATERLOGGED);
    }

    private static boolean isForeignFire(Block block) {
        if (block instanceof EclipseFireBlock) {
            return false;
        }
        if (block instanceof BaseFireBlock) {
            return true;
        }
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
        String path = id == null ? "" : id.getPath();
        if (rejectsFireName(path) || !namesFire(path, block)) {
            return false;
        }
        return !block.defaultBlockState().blocksMotion();
    }

    private static boolean namesFire(String path, Block block) {
        if (path.contains("fire") || path.contains("flame")) {
            return true;
        }
        Class<?> type = block.getClass();
        while (type != null && type != Object.class) {
            String name = type.getSimpleName().toLowerCase(Locale.ROOT);
            if (rejectsFireName(name)) {
                return false;
            }
            if (name.contains("fire") || name.contains("flame")) {
                return true;
            }
            type = type.getSuperclass();
        }
        return false;
    }

    private static boolean rejectsFireName(String name) {
        return name.contains("coral")
                || name.contains("campfire")
                || name.contains("firework")
                || name.contains("brick")
                || name.contains("charge")
                || name.contains("resistance")
                || name.contains("proof");
    }

    private static EclipseFireBlock fire(Level level, BlockPos pos) {
        return switch (EclipseZones.trio(EclipseZones.family(level.getBiome(pos)))) {
            case "sol" -> BlockInit.SOL_FIRE.get();
            case "lux" -> BlockInit.LUX_FIRE.get();
            default -> BlockInit.NYX_FIRE.get();
        };
    }

}
