package com.mystic.thither.eclipse.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraftforge.registries.RegistryObject;

public class EclipseGrassBlock extends SpreadingSnowyDirtBlock {
    private final RegistryObject<Block> dirt;

    public EclipseGrassBlock(BlockBehaviour.Properties properties, RegistryObject<Block> dirt) {
        super(properties
                .sound(SoundType.GRASS)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 3.5F)
                .randomTicks());
        this.dirt = dirt;
    }

    private static boolean canGrassSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState aboveState = world.getBlockState(above);
        if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        }
        if (aboveState.getFluidState().getAmount() == 8) {
            return false;
        }
        int light = LightEngine.getLightBlockInto(world, state, pos, aboveState, above, Direction.UP, aboveState.getLightBlock(world, above));
        return light < world.getMaxLightLevel();
    }

    private static boolean canSpread(BlockState state, LevelReader world, BlockPos pos) {
        return canGrassSurvive(state, world, pos) && !world.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!canGrassSurvive(state, world, pos)) {
            world.setBlockAndUpdate(pos, this.dirt.get().defaultBlockState());
            return;
        }
        if (world.getMaxLocalRawBrightness(pos.above()) < 0) {
            return;
        }
        BlockState grass = this.defaultBlockState();
        for (int i = 0; i < 4; ++i) {
            BlockPos spread = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
            if (world.getBlockState(spread).is(this.dirt.get()) && canSpread(grass, world, spread)) {
                world.setBlockAndUpdate(spread, grass);
            }
        }
    }
}
