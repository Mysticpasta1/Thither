package com.mystic.thither.doors.init;

import com.mystic.thither.Thither;
import com.mystic.thither.doors.blocks.DimensionDoorBlock;
import com.mystic.thither.doors.blocks.ThresholdDoorBlock;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class DoorBlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Thither.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Thither.MODID);

    /** Hub-only door. It has no item because it is placed by the door dimension generator. */
    public static final RegistryObject<DimensionDoorBlock> DIMENSION_DOOR =
            BLOCKS.register("dimension_door", DimensionDoorBlock::new);

    /** The craftable door that takes a player into the door dimension. */
    public static final RegistryObject<ThresholdDoorBlock> THRESHOLD_DOOR =
            registerWithItem("threshold_door", () -> new ThresholdDoorBlock(BlockBehaviour.Properties.of()
                    .strength(3.0F, 6.0F)));

    /** Generated at the hub and at every arrival shrine. It cannot be broken or collected. */
    public static final RegistryObject<ThresholdDoorBlock> RETURN_DOOR =
            BLOCKS.register("return_door", () -> new ThresholdDoorBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()));

    private DoorBlockInit() {
    }

    private static <T extends Block> RegistryObject<T> registerWithItem(String name, Supplier<T> block) {
        RegistryObject<T> registered = BLOCKS.register(name, block);
        BLOCK_ITEMS.register(name, () -> new BlockItem(registered.get(), new Item.Properties()));
        //Make door dimension creative tab later
        return registered;
    }
}
