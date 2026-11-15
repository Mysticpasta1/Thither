package com.mystic.thither.init;

import com.mystic.thither.blocks.EclipseFireBlock;
import com.mystic.thither.blocks.NyxDirtBlock;
import com.mystic.thither.blocks.EclipseBedrockBlock;
import com.mystic.thither.blocks.NyxGrassBlock;
import com.mystic.thither.blocks.NyxStoneBlock;
import com.mystic.thither.blocks.SolDirtBlock;
import com.mystic.thither.blocks.SolGrassBlock;
import com.mystic.thither.blocks.SolStoneBlock;
import com.mystic.thither.blocks.LuxDirtBlock;
import com.mystic.thither.blocks.LuxGrassBlock;
import com.mystic.thither.blocks.LuxStoneBlock;
import com.mystic.thither.creativetab.EclipseGroup;
import com.mystic.thither.utils.Reference;
import com.mystic.thither.worldgen.trees.EclipseTreeGrower;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    public static final RegistryObject<EclipseBedrockBlock> NYX_BEDROCK = register("nyx_bedrock", () -> new EclipseBedrockBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<EclipseBedrockBlock> SOL_BEDROCK = register("sol_bedrock", () -> new EclipseBedrockBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<EclipseBedrockBlock> LUX_BEDROCK = register("lux_bedrock", () -> new EclipseBedrockBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<NyxStoneBlock> NYX_STONE = register("nyx_stone", () -> new NyxStoneBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<SolStoneBlock> SOL_STONE = register("sol_stone", () -> new SolStoneBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<LuxStoneBlock> LUX_STONE = register("lux_stone", () -> new LuxStoneBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<NyxDirtBlock> NYX_DIRT = register("nyx_dirt", () -> new NyxDirtBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<SolDirtBlock> SOL_DIRT = register("sol_dirt", () -> new SolDirtBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<LuxDirtBlock> LUX_DIRT = register("lux_dirt", () -> new LuxDirtBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<NyxGrassBlock> NYX_GRASS = register("nyx_grass", () -> new NyxGrassBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<SolGrassBlock> SOL_GRASS = register("sol_grass", () -> new SolGrassBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<LuxGrassBlock> LUX_GRASS = register("lux_grass", () -> new LuxGrassBlock(BlockBehaviour.Properties.of()));

    public static final RegistryObject<RotatedPillarBlock> NYX_LOG = register("nyx_log", () -> new RotatedPillarBlock(logProperties()));
    public static final RegistryObject<LeavesBlock> NYX_LEAVES = register("nyx_leaves", () -> new LeavesBlock(leavesProperties()));
    public static final RegistryObject<SaplingBlock> NYX_SAPLING = register("nyx_sapling", () -> new SaplingBlock(EclipseTreeGrower.NYX_SHADOW, saplingProperties()));

    public static final RegistryObject<RotatedPillarBlock> SOL_LOG = register("sol_log", () -> new RotatedPillarBlock(logProperties()));
    public static final RegistryObject<LeavesBlock> SOL_LEAVES = register("sol_leaves", () -> new LeavesBlock(leavesProperties()));
    public static final RegistryObject<SaplingBlock> SOL_SAPLING = register("sol_sapling", () -> new SaplingBlock(EclipseTreeGrower.SOL_LIGHT, saplingProperties()));

    public static final RegistryObject<RotatedPillarBlock> LUX_LOG = register("lux_log", () -> new RotatedPillarBlock(logProperties()));
    public static final RegistryObject<LeavesBlock> LUX_LEAVES = register("lux_leaves", () -> new LeavesBlock(leavesProperties()));
    public static final RegistryObject<SaplingBlock> LUX_SAPLING = register("lux_sapling", () -> new SaplingBlock(EclipseTreeGrower.LUX_ECLIPSE, saplingProperties()));

    public static final RegistryObject<Block> THREAD = registerHidden("thread", () -> new Block(BlockBehaviour.Properties.of().mapColor(net.minecraft.world.level.material.MapColor.COLOR_BLACK).strength(1.5F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<EclipseFireBlock> NYX_FIRE = registerBlock("nyx_fire", () -> new EclipseFireBlock(7));
    public static final RegistryObject<EclipseFireBlock> SOL_FIRE = registerBlock("sol_fire", () -> new EclipseFireBlock(15));
    public static final RegistryObject<EclipseFireBlock> LUX_FIRE = registerBlock("lux_fire", () -> new EclipseFireBlock(11));

    private static BlockBehaviour.Properties logProperties() {
        return BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(2.0F);
    }

    private static BlockBehaviour.Properties leavesProperties() {
        return BlockBehaviour.Properties.of()
                .strength(0.2F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false);
    }

    private static BlockBehaviour.Properties saplingProperties() {
        return BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> registered = registerHidden(name, block);
        EclipseGroup.addToMainTab(registered::get);
        return registered;
    }

    private static <T extends Block> RegistryObject<T> registerHidden(String name, Supplier<T> block) {
        RegistryObject<T> registered = BLOCKS.register(name, block);
        BLOCK_ITEMS.register(name, () -> new BlockItem(registered.get(), new Item.Properties()));
        return registered;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }
}
