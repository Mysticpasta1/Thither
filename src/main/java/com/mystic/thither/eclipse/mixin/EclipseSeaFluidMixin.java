package com.mystic.thither.eclipse.mixin;

import com.mystic.thither.eclipse.init.BlockInit;
import com.mystic.thither.eclipse.init.FluidInit;
import com.mystic.thither.eclipse.worldgen.biomes.EclipseZones;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseBasedChunkGenerator.class)
public class EclipseSeaFluidMixin {
    private static final ThreadLocal<ChunkAccess> CHUNK = new ThreadLocal<>();

    @Inject(method = "doFill", at = @At("HEAD"))
    private void eclipse$beginChunk(net.minecraft.world.level.levelgen.blending.Blender blender, net.minecraft.world.level.StructureManager structures, net.minecraft.world.level.levelgen.RandomState random, ChunkAccess chunk, int minY, int cellHeight, CallbackInfoReturnable<ChunkAccess> cir) {
        CHUNK.set(chunk);
    }

    @Inject(method = "doFill", at = @At("RETURN"))
    private void eclipse$endChunk(net.minecraft.world.level.levelgen.blending.Blender blender, net.minecraft.world.level.StructureManager structures, net.minecraft.world.level.levelgen.RandomState random, ChunkAccess chunk, int minY, int cellHeight, CallbackInfoReturnable<ChunkAccess> cir) {
        CHUNK.remove();
    }

    @Inject(method = "createFluidPicker", at = @At("RETURN"), cancellable = true)
    private static void eclipse$biomeFluids(NoiseGeneratorSettings settings, CallbackInfoReturnable<Aquifer.FluidPicker> cir) {
        if (!settings.defaultBlock().is(BlockInit.NYX_STONE.get())) {
            return;
        }
        int sea = settings.seaLevel();
        int lavaLevel = Math.min(-54, sea);
        cir.setReturnValue((x, y, z) -> {
            ChunkAccess chunk = CHUNK.get();
            Holder<Biome> biome = chunk == null ? null : chunk.getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z));
            String trio = EclipseZones.trio(EclipseZones.family(biome));
            if (y < lavaLevel) {
                return new Aquifer.FluidStatus(lavaLevel, lava(trio));
            }
            return new Aquifer.FluidStatus(sea, water(trio));
        });
    }

    private static BlockState water(String trio) {
        if ("sol".equals(trio)) {
            return FluidInit.SOL_WATER_BLOCK.get().defaultBlockState();
        }
        if ("lux".equals(trio)) {
            return FluidInit.LUX_WATER_BLOCK.get().defaultBlockState();
        }
        return FluidInit.NYX_WATER_BLOCK.get().defaultBlockState();
    }

    private static BlockState lava(String trio) {
        if ("sol".equals(trio)) {
            return FluidInit.SOL_LAVA_BLOCK.get().defaultBlockState();
        }
        if ("lux".equals(trio)) {
            return FluidInit.LUX_LAVA_BLOCK.get().defaultBlockState();
        }
        return FluidInit.NYX_LAVA_BLOCK.get().defaultBlockState();
    }
}
