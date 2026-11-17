package com.mystic.thither.doors;

import com.mystic.thither.eclipse.init.ItemInit;
import com.mystic.thither.eclipse.worldgen.dimension.EclipseDimension;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * The twelve doors that stand in the door dimension.
 *
 * <p>The first door (index 0) opens onto the Eclipse dimension and is unlocked from the
 * start. Every other door is sealed until a key is crafted from materials gathered in the
 * dimension behind the previous door. Only the first link of that chain exists today, so
 * the Eclipse key opens door II and the remaining realms are left as sealed placeholders.</p>
 */
public enum DoorDestination {
    ECLIPSE(0, "eclipse", true, null, 0x67E8F9),
    SEALED_2(1, "sealed_2", false, null, 0x9B7BFF),
    SEALED_3(2, "sealed_3", false, null, 0xF07BD8),
    SEALED_4(3, "sealed_4", false, null, 0xF77B7B),
    SEALED_5(4, "sealed_5", false, null, 0xF7B86B),
    SEALED_6(5, "sealed_6", false, null, 0xF7E86B),
    SEALED_7(6, "sealed_7", false, null, 0xB6F76B),
    SEALED_8(7, "sealed_8", false, null, 0x6BF7B0),
    SEALED_9(8, "sealed_9", false, null, 0x6BF7E3),
    SEALED_10(9, "sealed_10", false, null, 0x6BB8F7),
    SEALED_11(10, "sealed_11", false, null, 0x8B8BF7),
    SEALED_12(11, "sealed_12", false, null, 0xD07BF7);

    private static final DoorDestination[] BY_INDEX = values();

    private final int index;
    private final String name;
    private final boolean initiallyUnlocked;
    private final ResourceKey<Level> dimension;
    private final int color;

    DoorDestination(int index, String name, boolean initiallyUnlocked, ResourceKey<Level> dimension, int color) {
        this.index = index;
        this.name = name;
        this.initiallyUnlocked = initiallyUnlocked;
        this.dimension = dimension;
        this.color = color;
    }

    public int index() {
        return this.index;
    }

    public boolean initiallyUnlocked() {
        return this.initiallyUnlocked;
    }

    public boolean implemented() {
        return this.dimension != null;
    }

    public ResourceKey<Level> dimension() {
        return this.dimension;
    }

    public int color() {
        return this.color;
    }

    public Component displayName() {
        return Component.translatable("door.thither." + this.name);
    }

    public static DoorDestination byIndex(int index) {
        if (index < 0 || index >= BY_INDEX.length) {
            return ECLIPSE;
        }
        return BY_INDEX[index];
    }
}
