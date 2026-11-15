package com.mystic.thither.fluids;

import com.mystic.thither.blocks.EclipseFireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Supplier;

public final class EclipseLavaFluid {
    private EclipseLavaFluid() {
    }

    public static class Source extends ForgeFlowingFluid.Source {
        private final Supplier<? extends Block> fire;

        public Source(Properties properties, Supplier<? extends Block> fire) {
            super(properties);
            this.fire = fire;
        }

        @Override
        public boolean isRandomlyTicking() {
            return true;
        }

        @Override
        public void randomTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
            ignite(level, pos, random, this.fire);
        }
    }

    public static class Flowing extends ForgeFlowingFluid.Flowing {
        private final Supplier<? extends Block> fire;

        public Flowing(Properties properties, Supplier<? extends Block> fire) {
            super(properties);
            this.fire = fire;
        }

        @Override
        public boolean isRandomlyTicking() {
            return true;
        }

        @Override
        public void randomTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
            ignite(level, pos, random, this.fire);
        }
    }

    private static void ignite(Level level, BlockPos pos, RandomSource random, Supplier<? extends Block> fire) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            return;
        }
        Block block = fire.get();
        if (!(block instanceof EclipseFireBlock fireBlock)) {
            return;
        }
        for (Direction direction : Direction.values()) {
            if (random.nextInt(12) != 0) {
                continue;
            }
            BlockPos adjacent = pos.relative(direction);
            if (!level.getBlockState(adjacent).isAir()) {
                continue;
            }
            if (!fireBlock.canSurvive(fireBlock.defaultBlockState(), level, adjacent)) {
                continue;
            }
            level.setBlock(adjacent, fireBlock.shaped(level, adjacent), 3);
        }
    }
}
