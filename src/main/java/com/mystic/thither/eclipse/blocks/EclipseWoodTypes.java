package com.mystic.thither.eclipse.blocks;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * Registers the BlockSetType/WoodType pair for every eclipse wood.
 * These are not registry entries; vanilla keeps them in static sets.
 */
public final class EclipseWoodTypes {
    private static final Map<String, BlockSetType> SET_TYPES = new HashMap<>();
    private static final Map<String, WoodType> WOOD_TYPES = new HashMap<>();

    private EclipseWoodTypes() {
    }

    public static BlockSetType setType(String name) {
        return SET_TYPES.computeIfAbsent(name, key -> BlockSetType.register(new BlockSetType(key)));
    }

    public static WoodType woodType(String name) {
        return WOOD_TYPES.computeIfAbsent(name, key -> WoodType.register(new WoodType(key, setType(key))));
    }
}
