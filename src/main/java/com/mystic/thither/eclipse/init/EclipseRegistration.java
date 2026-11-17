package com.mystic.thither.eclipse.init;

import com.mystic.thither.eclipse.creativetab.EclipseGroup;
import com.mystic.thither.eclipse.worldgen.dimension.EclipseDimension;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Registers everything that belongs to the Eclipse dimension.
 * Add a matching registration class for each new dimension.
 */
public final class EclipseRegistration {
    private EclipseRegistration() {
    }

    public static void register(IEventBus bus) {
        SoundInit.SOUNDS.register(bus);
        ParticleInit.PARTICLES.register(bus);
        FluidInit.FLUID_TYPES.register(bus);
        FluidInit.FLUIDS.register(bus);
        FluidInit.FLUID_BLOCKS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ITEMS.register(bus);
        ItemInit.ITEMS.register(bus);
        LootInit.SERIALIZERS.register(bus);
        FluidInit.BUCKETS.register(bus);
        EclipseGroup.TABS.register(bus);
        EclipseDimension.BIOME_SOURCES.register(bus);
    }
}
