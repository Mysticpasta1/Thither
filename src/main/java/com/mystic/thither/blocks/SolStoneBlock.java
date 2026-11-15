package com.mystic.thither.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SolStoneBlock extends Block {
    public SolStoneBlock(BlockBehaviour.Properties properties) {
        super(properties
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
                .strength(2.0F, 4.0F));
    }
}
