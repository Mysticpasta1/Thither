package com.mystic.thither.init;

import com.mystic.thither.ThitherMain;
import com.mystic.thither.creativetab.EclipseGroup;
import com.mystic.thither.fluids.EclipseFluidType;
import com.mystic.thither.fluids.EclipseLavaFluid;
import com.mystic.thither.utils.Reference;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidInit {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Reference.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Reference.MODID);
    public static final DeferredRegister<Block> FLUID_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);
    public static final DeferredRegister<Item> BUCKETS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    public static final RegistryObject<FluidType> NYX_WATER_TYPE = waterType("nyx_water", 0x99323238, 0);
    public static final RegistryObject<FluidType> SOL_WATER_TYPE = waterType("sol_water", 0x99E4E4EA, 0);
    public static final RegistryObject<FluidType> LUX_WATER_TYPE = waterType("lux_water", 0x998A8A96, 0);
    public static final RegistryObject<FluidType> NYX_LAVA_TYPE = lavaType("nyx_lava", 0x993C3C44, 7);
    public static final RegistryObject<FluidType> SOL_LAVA_TYPE = lavaType("sol_lava", 0x99F2F2EC, 15);
    public static final RegistryObject<FluidType> LUX_LAVA_TYPE = lavaType("lux_lava", 0x99A0A0AA, 11);

    public static final RegistryObject<ForgeFlowingFluid> NYX_WATER = FLUIDS.register("nyx_water", () -> new ForgeFlowingFluid.Source(nyxWaterProperties()));
    public static final RegistryObject<ForgeFlowingFluid> NYX_WATER_FLOWING = FLUIDS.register("nyx_water_flowing", () -> new ForgeFlowingFluid.Flowing(nyxWaterProperties()));
    public static final RegistryObject<ForgeFlowingFluid> SOL_WATER = FLUIDS.register("sol_water", () -> new ForgeFlowingFluid.Source(solWaterProperties()));
    public static final RegistryObject<ForgeFlowingFluid> SOL_WATER_FLOWING = FLUIDS.register("sol_water_flowing", () -> new ForgeFlowingFluid.Flowing(solWaterProperties()));
    public static final RegistryObject<ForgeFlowingFluid> LUX_WATER = FLUIDS.register("lux_water", () -> new ForgeFlowingFluid.Source(luxWaterProperties()));
    public static final RegistryObject<ForgeFlowingFluid> LUX_WATER_FLOWING = FLUIDS.register("lux_water_flowing", () -> new ForgeFlowingFluid.Flowing(luxWaterProperties()));

    public static final RegistryObject<ForgeFlowingFluid> NYX_LAVA = FLUIDS.register("nyx_lava", () -> new EclipseLavaFluid.Source(nyxLavaProperties(), BlockInit.NYX_FIRE));
    public static final RegistryObject<ForgeFlowingFluid> NYX_LAVA_FLOWING = FLUIDS.register("nyx_lava_flowing", () -> new EclipseLavaFluid.Flowing(nyxLavaProperties(), BlockInit.NYX_FIRE));
    public static final RegistryObject<ForgeFlowingFluid> SOL_LAVA = FLUIDS.register("sol_lava", () -> new EclipseLavaFluid.Source(solLavaProperties(), BlockInit.SOL_FIRE));
    public static final RegistryObject<ForgeFlowingFluid> SOL_LAVA_FLOWING = FLUIDS.register("sol_lava_flowing", () -> new EclipseLavaFluid.Flowing(solLavaProperties(), BlockInit.SOL_FIRE));
    public static final RegistryObject<ForgeFlowingFluid> LUX_LAVA = FLUIDS.register("lux_lava", () -> new EclipseLavaFluid.Source(luxLavaProperties(), BlockInit.LUX_FIRE));
    public static final RegistryObject<ForgeFlowingFluid> LUX_LAVA_FLOWING = FLUIDS.register("lux_lava_flowing", () -> new EclipseLavaFluid.Flowing(luxLavaProperties(), BlockInit.LUX_FIRE));

    public static final RegistryObject<LiquidBlock> NYX_WATER_BLOCK = fluidBlock("nyx_water", NYX_WATER, MapColor.COLOR_BLACK, 0);
    public static final RegistryObject<LiquidBlock> SOL_WATER_BLOCK = fluidBlock("sol_water", SOL_WATER, MapColor.COLOR_LIGHT_GRAY, 0);
    public static final RegistryObject<LiquidBlock> LUX_WATER_BLOCK = fluidBlock("lux_water", LUX_WATER, MapColor.COLOR_GRAY, 0);
    public static final RegistryObject<LiquidBlock> NYX_LAVA_BLOCK = fluidBlock("nyx_lava", NYX_LAVA, MapColor.COLOR_BLACK, 7);
    public static final RegistryObject<LiquidBlock> SOL_LAVA_BLOCK = fluidBlock("sol_lava", SOL_LAVA, MapColor.SNOW, 15);
    public static final RegistryObject<LiquidBlock> LUX_LAVA_BLOCK = fluidBlock("lux_lava", LUX_LAVA, MapColor.COLOR_GRAY, 11);

    public static final RegistryObject<Item> NYX_WATER_BUCKET = bucket("nyx_water_bucket", NYX_WATER);
    public static final RegistryObject<Item> SOL_WATER_BUCKET = bucket("sol_water_bucket", SOL_WATER);
    public static final RegistryObject<Item> LUX_WATER_BUCKET = bucket("lux_water_bucket", LUX_WATER);
    public static final RegistryObject<Item> NYX_LAVA_BUCKET = bucket("nyx_lava_bucket", NYX_LAVA);
    public static final RegistryObject<Item> SOL_LAVA_BUCKET = bucket("sol_lava_bucket", SOL_LAVA);
    public static final RegistryObject<Item> LUX_LAVA_BUCKET = bucket("lux_lava_bucket", LUX_LAVA);

    private static RegistryObject<FluidType> waterType(String name, int tint, int light) {
        return FLUID_TYPES.register(name, () -> new EclipseFluidType(FluidType.Properties.create()
                .descriptionId("block." + Reference.MODID + "." + name)
                .canSwim(true)
                .canDrown(true)
                .canExtinguish(true)
                .canHydrate(true)
                .canPushEntity(true)
                .supportsBoating(true)
                .canConvertToSource(true)
                .lightLevel(light)
                .density(1000)
                .viscosity(1000)
                .temperature(300)
                .motionScale(0.014)
                .fallDistanceModifier(0.0F)
                .pathType(BlockPathTypes.WATER)
                .adjacentPathType(BlockPathTypes.WATER_BORDER)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY),
                ThitherMain.id("block/" + name + "_still"),
                ThitherMain.id("block/" + name + "_flow"),
                tint));
    }

    private static RegistryObject<FluidType> lavaType(String name, int tint, int light) {
        return FLUID_TYPES.register(name, () -> new EclipseFluidType(FluidType.Properties.create()
                .descriptionId("block." + Reference.MODID + "." + name)
                .canSwim(true)
                .canDrown(false)
                .canExtinguish(false)
                .canPushEntity(true)
                .supportsBoating(false)
                .canConvertToSource(false)
                .lightLevel(light)
                .density(1400)
                .viscosity(1800)
                .temperature(300)
                .motionScale(0.011)
                .fallDistanceModifier(0.0F)
                .pathType(BlockPathTypes.WATER)
                .adjacentPathType(BlockPathTypes.WATER_BORDER)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA),
                ThitherMain.id("block/" + name + "_still"),
                ThitherMain.id("block/" + name + "_flow"),
                tint));
    }

    private static ForgeFlowingFluid.Properties nyxWaterProperties() {
        return water(NYX_WATER_TYPE, NYX_WATER, NYX_WATER_FLOWING, NYX_WATER_BLOCK, NYX_WATER_BUCKET);
    }

    private static ForgeFlowingFluid.Properties solWaterProperties() {
        return water(SOL_WATER_TYPE, SOL_WATER, SOL_WATER_FLOWING, SOL_WATER_BLOCK, SOL_WATER_BUCKET);
    }

    private static ForgeFlowingFluid.Properties luxWaterProperties() {
        return water(LUX_WATER_TYPE, LUX_WATER, LUX_WATER_FLOWING, LUX_WATER_BLOCK, LUX_WATER_BUCKET);
    }

    private static ForgeFlowingFluid.Properties nyxLavaProperties() {
        return lava(NYX_LAVA_TYPE, NYX_LAVA, NYX_LAVA_FLOWING, NYX_LAVA_BLOCK, NYX_LAVA_BUCKET);
    }

    private static ForgeFlowingFluid.Properties solLavaProperties() {
        return lava(SOL_LAVA_TYPE, SOL_LAVA, SOL_LAVA_FLOWING, SOL_LAVA_BLOCK, SOL_LAVA_BUCKET);
    }

    private static ForgeFlowingFluid.Properties luxLavaProperties() {
        return lava(LUX_LAVA_TYPE, LUX_LAVA, LUX_LAVA_FLOWING, LUX_LAVA_BLOCK, LUX_LAVA_BUCKET);
    }

    private static ForgeFlowingFluid.Properties water(RegistryObject<FluidType> type, RegistryObject<ForgeFlowingFluid> source, RegistryObject<ForgeFlowingFluid> flowing, RegistryObject<LiquidBlock> block, RegistryObject<Item> bucket) {
        return new ForgeFlowingFluid.Properties(type, source, flowing).block(block).bucket(bucket).slopeFindDistance(4).levelDecreasePerBlock(1).tickRate(5);
    }

    private static ForgeFlowingFluid.Properties lava(RegistryObject<FluidType> type, RegistryObject<ForgeFlowingFluid> source, RegistryObject<ForgeFlowingFluid> flowing, RegistryObject<LiquidBlock> block, RegistryObject<Item> bucket) {
        return new ForgeFlowingFluid.Properties(type, source, flowing).block(block).bucket(bucket).slopeFindDistance(2).levelDecreasePerBlock(2).tickRate(30);
    }

    private static RegistryObject<LiquidBlock> fluidBlock(String name, RegistryObject<ForgeFlowingFluid> source, MapColor color, int light) {
        return FLUID_BLOCKS.register(name, () -> {
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                    .mapColor(color)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY);
            if (light > 0) {
                properties = properties.lightLevel(state -> light);
            }
            return new LiquidBlock(source, properties);
        });
    }

    private static RegistryObject<Item> bucket(String name, RegistryObject<ForgeFlowingFluid> source) {
        RegistryObject<Item> registered = BUCKETS.register(name, () -> new BucketItem(source.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
        EclipseGroup.addToMainTab(registered::get);
        return registered;
    }
}
