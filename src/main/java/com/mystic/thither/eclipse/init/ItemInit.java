package com.mystic.thither.eclipse.init;

import com.mystic.thither.eclipse.creativetab.EclipseGroup;
import com.mystic.thither.eclipse.items.EclipseArmorMaterial;
import com.mystic.thither.eclipse.items.EclipseMusicDiscItem;
import com.mystic.thither.eclipse.items.EclipseTier;
import com.mystic.thither.utils.Reference;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    private static final Item.Properties ECLIPSE_SETTINGS = new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON);

    public static final RegistryObject<Item> WHITEWASH_DISC = register("whitewash", () -> new EclipseMusicDiscItem(15, SoundInit.WHITEWASH, ECLIPSE_SETTINGS, 322));
    public static final RegistryObject<Item> DAWNLIGHT_DISC = register("dawnlight", () -> new EclipseMusicDiscItem(7, SoundInit.DAWNLIGHT, ECLIPSE_SETTINGS, 212));
    public static final RegistryObject<Item> DOWNTIME_DISC = register("downtime", () -> new EclipseMusicDiscItem(0, SoundInit.DOWNTIME, ECLIPSE_SETTINGS, 163));

    public static final RegistryObject<Item> ECLIPSE_INGOT = register("eclipse_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ECLIPSE_KEY = register("eclipse_key", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> ECLIPSE_HELMET = register("eclipse_helmet", () -> new ArmorItem(EclipseArmorMaterial.INSTANCE, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ECLIPSE_CHESTPLATE = register("eclipse_chestplate", () -> new ArmorItem(EclipseArmorMaterial.INSTANCE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ECLIPSE_LEGGINGS = register("eclipse_leggings", () -> new ArmorItem(EclipseArmorMaterial.INSTANCE, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ECLIPSE_BOOTS = register("eclipse_boots", () -> new ArmorItem(EclipseArmorMaterial.INSTANCE, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> ECLIPSE_SWORD = register("eclipse_sword", () -> new SwordItem(EclipseTier.ECLIPSE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> ECLIPSE_PICKAXE = register("eclipse_pickaxe", () -> new PickaxeItem(EclipseTier.ECLIPSE, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> ECLIPSE_AXE = register("eclipse_axe", () -> new AxeItem(EclipseTier.ECLIPSE, 5.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> ECLIPSE_SHOVEL = register("eclipse_shovel", () -> new ShovelItem(EclipseTier.ECLIPSE, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> ECLIPSE_HOE = register("eclipse_hoe", () -> new HoeItem(EclipseTier.ECLIPSE, -4, 0.0F, new Item.Properties()));

    private static RegistryObject<Item> register(String name, java.util.function.Supplier<Item> item) {
        RegistryObject<Item> registered = ITEMS.register(name, item);
        EclipseGroup.addToMainTab(registered::get);
        return registered;
    }
}
