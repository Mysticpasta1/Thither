package com.mystic.thither.eclipse.items;

import com.mystic.thither.eclipse.init.ItemInit;
import com.mystic.thither.utils.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

/**
 * Tool material used by every eclipse tool. Registered with Forge's tier sorter so
 * the tools report the correct "needs this tool" behaviour.
 */
public final class EclipseTier {
    public static final Tier ECLIPSE = new ForgeTier(
            4,
            2400,
            9.5F,
            4.0F,
            18,
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ItemInit.ECLIPSE_INGOT.get()));

    static {
        TierSortingRegistry.registerTier(
                ECLIPSE,
                ResourceLocation.fromNamespaceAndPath(Reference.MODID, "eclipse"),
                List.of(Tiers.NETHERITE),
                List.of());
    }

    private EclipseTier() {
    }
}
