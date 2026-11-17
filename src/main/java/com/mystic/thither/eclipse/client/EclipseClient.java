package com.mystic.thither.eclipse.client;

import com.mystic.thither.eclipse.client.particle.VoidParticle;
import com.mystic.thither.eclipse.client.sky.EclipseDimensionSky;
import com.mystic.thither.eclipse.init.BlockInit;
import com.mystic.thither.eclipse.init.ParticleInit;
import com.mystic.thither.utils.Reference;
import com.mystic.thither.eclipse.worldgen.dimension.EclipseDimension;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.registries.RegistryObject;
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
        Block[] tinted = java.util.stream.Stream.concat(BlockInit.DIRT.values().stream(), BlockInit.GRASS.values().stream())
                .map(RegistryObject::get)
                .toArray(Block[]::new);
        event.register((state, world, pos, tintIndex) -> 0xFFFFFF, tinted);
    }
}
