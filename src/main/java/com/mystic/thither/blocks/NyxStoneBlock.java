package com.mystic.thither.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class NyxStoneBlock extends Block {
    public NyxStoneBlock(BlockBehaviour.Properties properties) {
        super(properties
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5F, 5.5F));
    }
}
