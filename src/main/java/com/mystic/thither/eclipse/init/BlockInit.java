package com.mystic.thither.eclipse.init;

import com.mystic.thither.eclipse.blocks.EclipseBedrockBlock;
import com.mystic.thither.eclipse.blocks.EclipseFireBlock;
import com.mystic.thither.eclipse.blocks.EclipseGrassBlock;
import com.mystic.thither.eclipse.blocks.EclipseWoodTypes;
import com.mystic.thither.eclipse.blocks.RunicStoneBlock;
import com.mystic.thither.eclipse.creativetab.EclipseGroup;
import com.mystic.thither.eclipse.worldgen.biomes.EclipseZones;
import com.mystic.thither.eclipse.worldgen.trees.EclipseTreeGrower;
import com.mystic.thither.utils.Reference;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    // ---------------------------------------------------------------------
    // Stone, dirt, and grass for every eclipse family
    // ---------------------------------------------------------------------
    public static final Map<String, RegistryObject<Block>> DIRT = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<EclipseGrassBlock>> GRASS = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Block>> STONE = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Block>> RUNIC = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Block>> ECLIPSE_ORE = new LinkedHashMap<>();
    public static final Map<String, RegistryObject<Block>> BEDROCK = new LinkedHashMap<>();

    public static final RegistryObject<Block> NYX_BEDROCK;
    public static final RegistryObject<Block> NYX_STONE;
    public static final RegistryObject<Block> SOL_STONE;
    public static final RegistryObject<Block> LUX_STONE;
    public static final RegistryObject<Block> NYX_RUNIC;
    public static final RegistryObject<Block> SOL_RUNIC;
    public static final RegistryObject<Block> LUX_RUNIC;
    public static final RegistryObject<Block> NYX_DIRT;
    public static final RegistryObject<Block> SOL_DIRT;
    public static final RegistryObject<Block> LUX_DIRT;
    public static final RegistryObject<EclipseGrassBlock> NYX_GRASS;
    public static final RegistryObject<EclipseGrassBlock> SOL_GRASS;
    public static final RegistryObject<EclipseGrassBlock> LUX_GRASS;

    static {
        for (String family : EclipseZones.FAMILIES) {
            terrain(family);
        }
        NYX_BEDROCK = BEDROCK.get("nyx");
        NYX_STONE = STONE.get("nyx");
        SOL_STONE = STONE.get("sol");
        LUX_STONE = STONE.get("lux");
        NYX_RUNIC = RUNIC.get("nyx");
        SOL_RUNIC = RUNIC.get("sol");
        LUX_RUNIC = RUNIC.get("lux");
        NYX_DIRT = DIRT.get("nyx");
        SOL_DIRT = DIRT.get("sol");
        LUX_DIRT = DIRT.get("lux");
        NYX_GRASS = GRASS.get("nyx");
        SOL_GRASS = GRASS.get("sol");
        LUX_GRASS = GRASS.get("lux");
    }

    // ---------------------------------------------------------------------
    // Wood sets
    // ---------------------------------------------------------------------
    static {
        woodSet("nyx");
        woodSet("sol");
        woodSet("lux");
        woodSet("umbral");
        woodSet("nocturne");
        woodSet("gloam");
        woodSet("radiant");
        woodSet("aurelian");
        woodSet("halcyon");
    }

    // ---------------------------------------------------------------------
    // Plants
    // ---------------------------------------------------------------------
    public static final RegistryObject<TallGrassBlock> ECLIPSE_GRASS = register("eclipse_grass", () -> new TallGrassBlock(plantProperties()));
    public static final RegistryObject<TallGrassBlock> ECLIPSE_FERN = register("eclipse_fern", () -> new TallGrassBlock(plantProperties()));
    public static final RegistryObject<FlowerBlock> NYX_SPROUT = flower("nyx_sprout", MobEffects.DARKNESS);
    public static final RegistryObject<FlowerBlock> SOL_BLOSSOM = flower("sol_blossom", MobEffects.GLOWING);
    public static final RegistryObject<FlowerBlock> LUX_BLOOM = flower("lux_bloom", MobEffects.NIGHT_VISION);

    static {
        flower("umbral_thorn", MobEffects.DARKNESS);
        flower("nocturne_bell", MobEffects.DARKNESS);
        flower("gloam_frond", MobEffects.SLOW_FALLING);
        flower("halcyon_petal", MobEffects.REGENERATION);
        flower("aurelian_crown", MobEffects.LUCK);
        flower("radiant_lance", MobEffects.GLOWING);
        tuft("umbral_scrub");
        tuft("nyx_wisp");
        tuft("nocturne_reed");
        tuft("gloam_shag");
        tuft("halcyon_sward");
        tuft("aurelian_awn");
        tuft("sol_ray");
        tuft("radiant_whisk");
        tuft("lux_silk");
    }

    // ---------------------------------------------------------------------
    // Misc
    // ---------------------------------------------------------------------
    public static final RegistryObject<Block> THREAD = registerHidden("thread", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(1.5F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<EclipseFireBlock> NYX_FIRE = registerBlock("nyx_fire", () -> new EclipseFireBlock(7));
    public static final RegistryObject<EclipseFireBlock> SOL_FIRE = registerBlock("sol_fire", () -> new EclipseFireBlock(15));
    public static final RegistryObject<EclipseFireBlock> LUX_FIRE = registerBlock("lux_fire", () -> new EclipseFireBlock(11));

    private static void terrain(String family) {
        BEDROCK.put(family, register(family + "_bedrock", () -> new EclipseBedrockBlock(BlockBehaviour.Properties.of().mapColor(mapColor(family)))));
        RegistryObject<Block> stoneBlock = register(family + "_stone", () -> new Block(stoneProperties(family)));
        STONE.put(family, stoneBlock);
        RegistryObject<Block> dirtBlock = register(family + "_dirt", () -> new Block(dirtProperties(family)));
        DIRT.put(family, dirtBlock);
        GRASS.put(family, register(family + "_grass", () -> new EclipseGrassBlock(BlockBehaviour.Properties.of().mapColor(mapColor(family)), dirtBlock)));
        RegistryObject<Block> cobble = register(family + "_cobblestone", () -> new Block(stoneProperties(family)));
        register(family + "_cobblestone_stairs", () -> new StairBlock(() -> cobble.get().defaultBlockState(), stoneProperties(family)));
        register(family + "_cobblestone_slab", () -> new SlabBlock(stoneProperties(family)));
        register(family + "_cobblestone_wall", () -> new WallBlock(stoneProperties(family)));
        RUNIC.put(family, register(family + "_runic_stone", () -> new RunicStoneBlock(EclipseZones.blockLight(family))));
        ECLIPSE_ORE.put(family, register(family + "_eclipse_ore", () -> new Block(oreProperties(family))));
    }

    private static RegistryObject<FlowerBlock> flower(String name, net.minecraft.world.effect.MobEffect effect) {
        return register(name, () -> new FlowerBlock(() -> effect, 6, plantProperties()));
    }

    private static void tuft(String name) {
        register(name, () -> new TallGrassBlock(plantProperties()));
    }

    private static void woodSet(String name) {
        BlockSetType setType = EclipseWoodTypes.setType(name);
        WoodType woodType = EclipseWoodTypes.woodType(name);

        register(name + "_log", () -> new RotatedPillarBlock(logProperties()));
        register(name + "_wood", () -> new RotatedPillarBlock(logProperties()));
        register(name + "_stripped_log", () -> new RotatedPillarBlock(logProperties()));
        register(name + "_stripped_wood", () -> new RotatedPillarBlock(logProperties()));

        RegistryObject<Block> planks = register(name + "_planks", () -> new Block(plankProperties()));
        register(name + "_stairs", () -> new StairBlock(() -> planks.get().defaultBlockState(), plankProperties()));
        register(name + "_slab", () -> new SlabBlock(plankProperties()));
        register(name + "_fence", () -> new FenceBlock(plankProperties()));
        register(name + "_fence_gate", () -> new FenceGateBlock(plankProperties(), woodType));
        register(name + "_door", () -> new DoorBlock(doorProperties(), setType));
        register(name + "_trapdoor", () -> new TrapDoorBlock(trapdoorProperties(), setType));
        register(name + "_button", () -> new ButtonBlock(buttonProperties(), setType, 30, true));
        register(name + "_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, buttonProperties(), setType));
        register(name + "_leaves", () -> new LeavesBlock(leavesProperties()));
        register(name + "_sapling", () -> new SaplingBlock(EclipseTreeGrower.of(name), saplingProperties()));
    }

    private static MapColor mapColor(String family) {
        return switch (EclipseZones.index(family)) {
            case 0, 1 -> MapColor.COLOR_BLACK;
            case 2, 3 -> MapColor.COLOR_GRAY;
            case 4, 5 -> MapColor.COLOR_LIGHT_GRAY;
            case 6 -> MapColor.QUARTZ;
            default -> MapColor.SNOW;
        };
    }

    private static BlockBehaviour.Properties stoneProperties(String family) {
        return BlockBehaviour.Properties.of()
                .mapColor(mapColor(family))
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5F, 5.5F);
    }

    private static BlockBehaviour.Properties dirtProperties(String family) {
        return BlockBehaviour.Properties.of()
                .mapColor(mapColor(family))
                .sound(SoundType.ROOTED_DIRT)
                .requiresCorrectToolForDrops()
                .strength(2.0F, 3.5F);
    }

    private static BlockBehaviour.Properties oreProperties(String family) {
        return BlockBehaviour.Properties.of()
                .mapColor(mapColor(family))
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 3.0F);
    }

    private static BlockBehaviour.Properties logProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F);
    }

    private static BlockBehaviour.Properties plankProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F);
    }

    private static BlockBehaviour.Properties doorProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(3.0F).noOcclusion().pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties trapdoorProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.WOOD)
                .strength(3.0F)
                .noOcclusion()
                .isValidSpawn((state, level, pos, type) -> false)
                .isSuffocating((state, level, pos) -> false)
                .isViewBlocking((state, level, pos) -> false);
    }

    private static BlockBehaviour.Properties buttonProperties() {
        return BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOD);
    }

    private static BlockBehaviour.Properties leavesProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
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

    private static BlockBehaviour.Properties plantProperties() {
        return BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ);
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
