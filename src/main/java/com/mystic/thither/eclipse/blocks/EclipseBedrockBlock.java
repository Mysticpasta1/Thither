package com.mystic.thither.eclipse.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class EclipseBedrockBlock extends Block {
    public EclipseBedrockBlock(BlockBehaviour.Properties properties) {
        super(properties.sound(SoundType.STONE).strength(-1.0F, 3600000.0F));
    }
}
