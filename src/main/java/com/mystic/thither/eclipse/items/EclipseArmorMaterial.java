package com.mystic.thither.eclipse.items;

import com.mystic.thither.eclipse.init.ItemInit;
import com.mystic.thither.utils.Reference;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Armor material for eclipse gear. Forge accepts a namespaced material name, so the
 * textures live in assets/thither/textures/models/armor/eclipse_layer_1.png and _layer_2.png.
 */
public enum EclipseArmorMaterial implements ArmorMaterial {
    INSTANCE;

    private static final int[] DURABILITY = {13, 15, 16, 11};
    private static final int[] DEFENSE = {3, 6, 8, 3};

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return DURABILITY[type.ordinal()] * 37;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return DEFENSE[type.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return 18;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ItemInit.ECLIPSE_INGOT.get());
    }

    @Override
    public String getName() {
        return Reference.MODID + ":eclipse";
    }

    @Override
    public float getToughness() {
        return 1.5F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.05F;
    }
}
