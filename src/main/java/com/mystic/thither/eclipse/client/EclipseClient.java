package com.mystic.thither.eclipse.client;

import com.mystic.thither.doors.DoorDestination;
import com.mystic.thither.doors.blocks.DimensionDoorBlock;
import com.mystic.thither.doors.init.DoorBlockInit;
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

        event.register((state, world, pos, tintIndex) -> {
            if (tintIndex != 0) {
                return -1;
            }
            int destinationColor = DoorDestination.byIndex(state.getValue(DimensionDoorBlock.DESTINATION)).color();
            if (state.getValue(DimensionDoorBlock.LOCKED)) {
                // Keep the door's coloured grain, metal and gem visible while making a
                // sealed door clearly darker than an open one.
                return blend(0x4A4A5A, destinationColor, 0.45F);
            }
            // Only a light wash of the destination colour so the multi-colour texture
            // underneath still reads as teal, gold, cyan and magenta.
            return blend(0xFFFFFF, destinationColor, 0.35F);
        }, DoorBlockInit.DIMENSION_DOOR.get());

        event.register((state, world, pos, tintIndex) -> 0xFFFFFF,
                DoorBlockInit.THRESHOLD_DOOR.get(), DoorBlockInit.RETURN_DOOR.get());
    }

    private static int blend(int base, int tint, float amount) {
        float keep = 1.0F - amount;
        int red = (int) (((base >> 16) & 0xFF) * keep + ((tint >> 16) & 0xFF) * amount);
        int green = (int) (((base >> 8) & 0xFF) * keep + ((tint >> 8) & 0xFF) * amount);
        int blue = (int) ((base & 0xFF) * keep + (tint & 0xFF) * amount);
        return (red << 16) | (green << 8) | blue;
    }
}
