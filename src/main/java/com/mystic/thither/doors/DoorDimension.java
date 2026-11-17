package com.mystic.thither.doors;

import com.mojang.serialization.Codec;
import com.mystic.thither.ThitherMain;
import com.mystic.thither.doors.blocks.DimensionDoorBlock;
import com.mystic.thither.doors.init.DoorBlockInit;
import com.mystic.thither.doors.worldgen.DoorDimensionGenerator;
import com.mystic.thither.doors.worldgen.DoorHubLayout;
import com.mystic.thither.eclipse.init.BlockInit;
import com.mystic.thither.eclipse.worldgen.biomes.EclipseZones;
import com.mystic.thither.eclipse.worldgen.dimension.EclipseDimension;
import com.mystic.thither.utils.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Everything about the door dimension and the teleports between it, the Eclipse
 * dimension, and ordinary worlds.
 */
@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DoorDimension {
    public static final ResourceKey<Level> DOOR_WORLD =
            ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "doors"));
    public static final ResourceKey<net.minecraft.world.level.dimension.DimensionType> DOOR_DIMENSION_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "doors"));

    public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS =
            DeferredRegister.create(Registries.CHUNK_GENERATOR, Reference.MODID);
    public static final RegistryObject<Codec<DoorDimensionGenerator>> DOOR_GENERATOR =
            CHUNK_GENERATORS.register("door_hub", () -> DoorDimensionGenerator.CODEC);

    private static final String HOME_DIMENSION = "thither_door_home_dimension";
    private static final String HOME_X = "thither_door_home_x";
    private static final String HOME_Y = "thither_door_home_y";
    private static final String HOME_Z = "thither_door_home_z";
    private static final String HOME_YAW = "thither_door_home_yaw";
    private static final String HOME_PITCH = "thither_door_home_pitch";

    public static ServerLevel DOOR_DIMENSION;

    private DoorDimension() {
    }

    public static boolean isDoorDimension(Level level) {
        return level != null && level.dimension().equals(DOOR_WORLD);
    }

    public static boolean isModDimension(Level level) {
        return isDoorDimension(level) || EclipseDimension.isEclipseDimension(level);
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        DOOR_DIMENSION = server.getLevel(DOOR_WORLD);
        if (DOOR_DIMENSION != null) {
            DOOR_DIMENSION.getChunk(DoorHubLayout.CHUNK_X, DoorHubLayout.CHUNK_Z);
            int lowerDoors = 0;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    BlockState state = DOOR_DIMENSION.getBlockState(new BlockPos(x, DoorHubLayout.WALK_Y, z));
                    if (state.getBlock() instanceof DimensionDoorBlock
                            && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
                        lowerDoors++;
                    }
                }
            }
            BlockState returnDoor = DOOR_DIMENSION.getBlockState(new BlockPos(15, DoorHubLayout.WALK_Y, 8));
            ThitherMain.LOGGER.info("Door dimension ready; {} dimension doors, return door {}", lowerDoors, returnDoor);
        }
    }

    public static void enterHub(ServerPlayer player, boolean rememberHome) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        ServerLevel hub = server.getLevel(DOOR_WORLD);
        if (hub == null) {
            player.displayClientMessage(Component.translatable("message.thither.door_missing").withStyle(ChatFormatting.RED), true);
            return;
        }
        if (rememberHome) {
            rememberHome(player);
        }
        hub.getChunk(DoorHubLayout.CHUNK_X, DoorHubLayout.CHUNK_Z);
        player.setPortalCooldown(100);
        player.teleportTo(hub, DoorHubLayout.SPAWN_X, DoorHubLayout.SPAWN_Y, DoorHubLayout.SPAWN_Z,
                DoorHubLayout.SPAWN_YAW, 0.0F);
        hub.playSound(null, DoorHubLayout.SPAWN_X, DoorHubLayout.SPAWN_Y, DoorHubLayout.SPAWN_Z,
                SoundEvents.PORTAL_TRAVEL, SoundSource.BLOCKS, 0.6F, 1.1F);
        player.displayClientMessage(Component.translatable("message.thither.enter_hub").withStyle(ChatFormatting.AQUA), true);
    }

    public static void returnHome(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        CompoundTag data = player.getPersistentData();
        ServerLevel target = null;
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        float yaw = 0.0F;
        float pitch = 0.0F;

        ResourceLocation homeId = ResourceLocation.tryParse(data.getString(HOME_DIMENSION));
        if (homeId != null) {
            target = server.getLevel(ResourceKey.create(Registries.DIMENSION, homeId));
            if (target != null && isDoorDimension(target)) {
                target = null;
            }
        }
        if (target != null) {
            x = data.getDouble(HOME_X);
            y = data.getDouble(HOME_Y);
            z = data.getDouble(HOME_Z);
            yaw = data.getFloat(HOME_YAW);
            pitch = data.getFloat(HOME_PITCH);
        } else {
            target = server.overworld();
            BlockPos spawn = target.getSharedSpawnPos();
            x = spawn.getX() + 0.5D;
            y = spawn.getY();
            z = spawn.getZ() + 0.5D;
        }

        player.setPortalCooldown(100);
        player.teleportTo(target, x, y, z, yaw, pitch);
        target.playSound(null, x, y, z, SoundEvents.PORTAL_TRAVEL, SoundSource.BLOCKS, 0.6F, 1.1F);
        player.displayClientMessage(Component.translatable("message.thither.return_home").withStyle(ChatFormatting.AQUA), true);
    }

    public static boolean enterDestination(ServerPlayer player, DoorDestination destination) {
        MinecraftServer server = player.getServer();
        if (server == null || destination == null || !destination.implemented()) {
            return false;
        }
        ServerLevel target = server.getLevel(destination.dimension());
        if (target == null) {
            player.displayClientMessage(Component.translatable("message.thither.door_missing").withStyle(ChatFormatting.RED), true);
            return false;
        }

        Arrival arrival = ensureArrivalShrine(target);
        BlockPos stand = arrival.door().relative(arrival.facing(), 2);
        float yaw = yawFor(arrival.facing().getOpposite());

        player.setPortalCooldown(100);
        player.teleportTo(target, stand.getX() + 0.5D, stand.getY(), stand.getZ() + 0.5D, yaw, 0.0F);
        target.playSound(null, stand, SoundEvents.PORTAL_TRAVEL, SoundSource.BLOCKS, 0.7F, 1.0F);
        target.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL,
                stand.getX() + 0.5D, stand.getY() + 1.0D, stand.getZ() + 0.5D,
                40, 0.6D, 0.9D, 0.6D, 0.2D);
        player.displayClientMessage(Component.translatable("message.thither.enter_door", destination.displayName())
                .withStyle(ChatFormatting.AQUA), true);
        return true;
    }

    /**
     * Builds (or repairs) the small arrival shrine around a return door. The shrine is
     * always at x=0,z=-2 in the destination dimension, with the player arriving at x=0,z=0.
     */
    public static Arrival ensureArrivalShrine(ServerLevel level) {
        level.getChunkAt(new BlockPos(0, 64, -2));
        level.getChunkAt(new BlockPos(0, 64, 0));

        int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 0, 0);
        surface = Mth.clamp(surface, level.getMinBuildHeight() + 8, level.getMaxBuildHeight() - 8);

        int doorY = surface;
        for (int y = surface - 10; y <= surface + 10; y++) {
            BlockState state = level.getBlockState(new BlockPos(0, y, -2));
            if (state.is(DoorBlockInit.RETURN_DOOR.get()) && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
                doorY = y;
                break;
            }
        }

        BlockPos centre = new BlockPos(0, doorY, 0);
        String family = EclipseZones.family(level.getBiome(centre));
        Block stoneBlock = BlockInit.STONE.getOrDefault(family, BlockInit.NYX_STONE).get();
        Block runicBlock = BlockInit.RUNIC.getOrDefault(family, BlockInit.NYX_RUNIC).get();
        BlockState stone = stoneBlock.defaultBlockState();
        BlockState runic = runicBlock.defaultBlockState();
        BlockState light = BlockInit.LUX_RUNIC.get().defaultBlockState();

        // Safe floor, and clear the shrine volume before rebuilding the arch.
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                level.setBlockAndUpdate(new BlockPos(x, doorY - 1, z), stone);
                for (int y = doorY; y <= doorY + 5; y++) {
                    level.setBlockAndUpdate(new BlockPos(x, y, z), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                }
            }
        }

        // Runic arch around the return door.
        for (int y = doorY; y <= doorY + 2; y++) {
            level.setBlockAndUpdate(new BlockPos(-1, y, -2), runic);
            level.setBlockAndUpdate(new BlockPos(1, y, -2), runic);
        }
        for (int x = -1; x <= 1; x++) {
            level.setBlockAndUpdate(new BlockPos(x, doorY + 2, -2), runic);
        }
        level.setBlockAndUpdate(new BlockPos(0, doorY + 3, -2), light);

        BlockState lower = DoorBlockInit.RETURN_DOOR.get().defaultBlockState()
                .setValue(DoorBlock.FACING, Direction.SOUTH)
                .setValue(DoorBlock.HINGE, DoorHingeSide.LEFT)
                .setValue(DoorBlock.OPEN, false)
                .setValue(DoorBlock.POWERED, false)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
        level.setBlockAndUpdate(new BlockPos(0, doorY, -2), lower);
        level.setBlockAndUpdate(new BlockPos(0, doorY + 1, -2), lower.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));

        // A little glow in the floor so the arrival point is easy to find again.
        level.setBlockAndUpdate(new BlockPos(0, doorY - 1, 0), BlockInit.LUX_STONE.get().defaultBlockState());

        return new Arrival(new BlockPos(0, doorY, -2), Direction.SOUTH);
    }

    private static void rememberHome(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.putString(HOME_DIMENSION, player.level().dimension().location().toString());
        data.putDouble(HOME_X, player.getX());
        data.putDouble(HOME_Y, player.getY());
        data.putDouble(HOME_Z, player.getZ());
        data.putFloat(HOME_YAW, player.getYRot());
        data.putFloat(HOME_PITCH, player.getXRot());
    }

    private static float yawFor(Direction direction) {
        return switch (direction) {
            case SOUTH -> 0.0F;
            case WEST -> 90.0F;
            case NORTH -> 180.0F;
            case EAST -> 270.0F;
            default -> 0.0F;
        };
    }

    public record Arrival(BlockPos door, Direction facing) {
    }
}
