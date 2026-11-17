package com.mystic.thither.eclipse.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class RunicStoneBlock extends Block {
    public RunicStoneBlock(int light) {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5F, 7.0F)
                .lightLevel(state -> light));
    }
}
