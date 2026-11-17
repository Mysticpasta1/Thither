package com.mystic.thither.eclipse.worldgen.biomes;

import com.mystic.thither.utils.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

/**
 * Nine eclipse families, blackest to whitest.
 * Light follows whichever biome is actually at the position.
 */
public final class EclipseZones {
    public static final List<String> FAMILIES = List.of(
            "umbral", "nyx", "nocturne", "gloam", "halcyon", "aurelian", "sol", "radiant", "lux");

    private static final int[] BLOCK_LIGHT = {0, 2, 4, 6, 8, 10, 12, 14, 15};

    public static final ResourceKey<Biome> NYX = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "nyx_shadow"));
    public static final ResourceKey<Biome> SOL = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "sol_light"));
    public static final ResourceKey<Biome> LUX = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "lux_eclipse"));

    private static final int PATCH = 150;

    private EclipseZones() {
    }

    public static int index(String family) {
        int found = FAMILIES.indexOf(family);
        return found < 0 ? 1 : found;
    }

    public static int blockLight(String family) {
        return BLOCK_LIGHT[index(family)];
    }

    /**
     * Umbral through nocturne share the dark fluid and fire.
     * Gloam through aurelian share the middle set.
     * Sol through lux share the light set.
     */
    public static String trio(String family) {
        int shade = index(family);
        if (shade <= 2) {
            return "nyx";
        }
        if (shade <= 5) {
            return "lux";
        }
        return "sol";
    }

    public static String family(Holder<Biome> biome) {
        if (biome == null) {
            return "nyx";
        }
        return biome.unwrapKey().map(key -> familyOf(key.location().getPath())).orElse("nyx");
    }

    public static String familyOf(String path) {
        for (String name : FAMILIES) {
            if (path.equals(name) || path.startsWith(name + "_")) {
                return name;
            }
        }
        return "nyx";
    }

    public static int lightLevel(LevelAccessor level, BlockPos pos) {
        return blockLight(family(level.getBiome(pos)));
    }

    public static float cloudShade(LevelAccessor level, BlockPos pos) {
        return index(family(level.getBiome(pos))) / 8.0F;
    }

    public static float sunStrength(LevelAccessor level, BlockPos pos) {
        return 0.18F + 0.82F * (index(family(level.getBiome(pos))) / 8.0F);
    }

    public static boolean isLightSide(int blockX, int blockZ) {
        int warpedX = blockX + edgeShift(blockX, blockZ, 0);
        int warpedZ = blockZ + edgeShift(blockX, blockZ, 1);
        int cellX = Math.floorDiv(warpedX, PATCH);
        int cellZ = Math.floorDiv(warpedZ, PATCH);
        return ((cellX + cellZ) & 1) == 0;
    }

    public static boolean isTwilightOasis(int blockX, int blockZ) {
        return oasisNoise(blockX, blockZ) > 0.80;
    }

    /**
     * Dark patches stay at 0 and light patches stay at 15, at every height.
     * A twilight oasis eases to 7.
     */
    public static int lightLevel(int blockX, int blockZ) {
        double fade = (oasisNoise(blockX, blockZ) - 0.68) / 0.14;
        if (fade < 0.0) {
            fade = 0.0;
        } else if (fade > 1.0) {
            fade = 1.0;
        }
        fade = fade * fade * (3.0 - 2.0 * fade);
        if (isLightSide(blockX, blockZ)) {
            return (int) Math.round(15.0 + (7.0 - 15.0) * fade);
        }
        return (int) Math.round(7.0 * fade);
    }

    private static int edgeShift(int blockX, int blockZ, int salt) {
        double n = smoothValue(blockX + salt * 12000, blockZ - salt * 5000, 160);
        return (int) Math.round((n - 0.5) * 50.0);
    }

    private static double oasisNoise(int blockX, int blockZ) {
        double patch = smoothValue(blockX + 900, blockZ + 400, 64);
        double shape = smoothValue(blockX - 2200, blockZ + 80, 22);
        return patch * 0.7 + shape * 0.3;
    }

    private static double smoothValue(int blockX, int blockZ, int cell) {
        int x0 = Math.floorDiv(blockX, cell);
        int z0 = Math.floorDiv(blockZ, cell);
        double fx = (blockX - (double) x0 * cell) / cell;
        double fz = (blockZ - (double) z0 * cell) / cell;
        fx = fx * fx * (3.0 - 2.0 * fx);
        fz = fz * fz * (3.0 - 2.0 * fz);
        double n00 = hash(x0, z0);
        double n10 = hash(x0 + 1, z0);
        double n01 = hash(x0, z0 + 1);
        double n11 = hash(x0 + 1, z0 + 1);
        double nx0 = n00 + (n10 - n00) * fx;
        double nx1 = n01 + (n11 - n01) * fx;
        return nx0 + (nx1 - nx0) * fz;
    }

    private static double hash(int x, int z) {
        long n = (x * 374761393L) ^ (z * 668265263L);
        n = (n ^ (n >> 13)) * 1274126177L;
        n ^= n >> 16;
        return (n & 0xFFFFFFL) / 16777215.0;
    }
}
