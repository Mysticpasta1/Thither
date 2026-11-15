package com.mystic.thither;

import com.mystic.thither.creativetab.EclipseGroup;
import com.mystic.thither.init.BlockInit;
import com.mystic.thither.init.FluidInit;
import com.mystic.thither.init.ItemInit;
import com.mystic.thither.init.ParticleInit;
import com.mystic.thither.init.SoundInit;
import com.mystic.thither.utils.Reference;
import com.mystic.thither.worldgen.dimension.EclipseDimension;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MODID)
public class ThitherMain {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Reference.MODID, path);
    }

    public ThitherMain(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        SoundInit.SOUNDS.register(bus);
        ParticleInit.PARTICLES.register(bus);
        FluidInit.FLUID_TYPES.register(bus);
        FluidInit.FLUIDS.register(bus);
        FluidInit.FLUID_BLOCKS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ITEMS.register(bus);
        ItemInit.ITEMS.register(bus);
        FluidInit.BUCKETS.register(bus);
        EclipseGroup.TABS.register(bus);
        EclipseDimension.BIOME_SOURCES.register(bus);
    }
}
