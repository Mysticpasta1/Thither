package com.mystic.thither.client;

import com.mystic.thither.client.particle.VoidParticle;
import com.mystic.thither.client.sky.EclipseDimensionSky;
import com.mystic.thither.init.BlockInit;
import com.mystic.thither.init.ParticleInit;
import com.mystic.thither.utils.Reference;
import com.mystic.thither.worldgen.dimension.EclipseDimension;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EclipseClient {
    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(EclipseDimension.ECLIPSE_DIMENSION_TYPE_KEY.location(), new EclipseDimensionSky());
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleInit.VOID.get(), VoidParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, world, pos, tintIndex) -> 0xFFFFFF, BlockInit.NYX_DIRT.get(), BlockInit.NYX_GRASS.get());
        event.register((state, world, pos, tintIndex) -> 0xFFFFFF, BlockInit.SOL_DIRT.get(), BlockInit.SOL_GRASS.get());
        event.register((state, world, pos, tintIndex) -> 0x929292, BlockInit.LUX_DIRT.get(), BlockInit.LUX_GRASS.get());
    }
}
