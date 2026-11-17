package com.mystic.thither.eclipse.creativetab;

import com.mystic.thither.eclipse.init.BlockInit;
import com.mystic.thither.utils.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EclipseGroup {
    public static final List<Supplier<? extends ItemLike>> MAIN_BLOCKS = new ArrayList<>();
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MODID);

    public static final RegistryObject<CreativeModeTab> ECLIPSE = TABS.register("thither", () -> CreativeModeTab.builder()
            .title(Component.literal("Thither"))
            .icon(() -> new ItemStack(BlockInit.LUX_STONE.get()))
            .displayItems((displayContext, entries) -> MAIN_BLOCKS.forEach(itemLike -> entries.accept(itemLike.get())))
            .build());

    public static void addToMainTab(Supplier<? extends ItemLike> itemLike) {
        MAIN_BLOCKS.add(itemLike);
    }
}
