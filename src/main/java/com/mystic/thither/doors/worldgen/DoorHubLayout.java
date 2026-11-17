package com.mystic.thither.doors.worldgen;

import com.mystic.thither.doors.DoorDestination;
import com.mystic.thither.doors.blocks.DimensionDoorBlock;
import com.mystic.thither.doors.init.DoorBlockInit;
import com.mystic.thither.eclipse.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Builds the single-chunk hall that houses the twelve doors.
 *
 * <p>The room is deliberately kept inside chunk (0, 0) so that both halves of every door
 * and all of its frame blocks are generated together. The shell uses unbreakable eclipse
 * bedrock, the floor carries a glowing emblem, and the two long walls hold six doors
 * each. The Eclipse door sits in the middle of the north wall, directly in front of the
 * arrival point.</p>
 */
public final class DoorHubLayout {
    public static final int CHUNK_X = 0;
    public static final int CHUNK_Z = 0;
    public static final int FLOOR_Y = 63;
    public static final int WALK_Y = 64;
    public static final int CEILING_Y = 68;
    public static final double SPAWN_X = 8.5D;
    public static final double SPAWN_Y = 64.0D;
    public static final double SPAWN_Z = 8.5D;
    public static final float SPAWN_YAW = 180.0F;

    /**
     * Left-to-right destination order on the north wall (x = 2, 4, 6, 8, 10, 12).
     * The Eclipse door is the middle one.
     */
    private static final int[] NORTH_ORDER = {5, 3, 1, 0, 2, 4};
    private static final int[] SOUTH_ORDER = {11, 9, 7, 6, 8, 10};

    private DoorHubLayout() {
    }

    public static boolean isHubChunk(ChunkPos pos) {
        return pos.x == CHUNK_X && pos.z == CHUNK_Z;
    }

    public static boolean isInsideHub(int x, int z) {
        return Math.floorDiv(x, 16) == CHUNK_X && Math.floorDiv(z, 16) == CHUNK_Z;
    }

    public static void build(ChunkAccess chunk) {
        if (!isHubChunk(chunk.getPos())) {
            return;
        }

        Heightmap surface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        Heightmap oceanFloor = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);

        BlockState shell = BlockInit.NYX_BEDROCK.get().defaultBlockState();
        BlockState floor = BlockInit.NYX_STONE.get().defaultBlockState();
        BlockState accent = BlockInit.SOL_STONE.get().defaultBlockState();
        BlockState glow = BlockInit.LUX_STONE.get().defaultBlockState();
        BlockState light = BlockInit.LUX_RUNIC.get().defaultBlockState();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                set(chunk, surface, oceanFloor, x, FLOOR_Y - 1, z, shell);
                set(chunk, surface, oceanFloor, x, FLOOR_Y, z, floor);
                set(chunk, surface, oceanFloor, x, CEILING_Y, z, shell);

                if (x == 0 || x == 15 || z == 0 || z == 15) {
                    for (int y = WALK_Y; y < CEILING_Y; y++) {
                        set(chunk, surface, oceanFloor, x, y, z, shell);
                    }
                } else {
                    for (int y = WALK_Y; y < CEILING_Y; y++) {
                        set(chunk, surface, oceanFloor, x, y, z, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }

        // Glowing floor emblem around the arrival point.
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                int distance = Math.abs(dx) + Math.abs(dz);
                if (distance == 0) {
                    set(chunk, surface, oceanFloor, 8, FLOOR_Y, 8, glow);
                } else if (distance <= 2) {
                    set(chunk, surface, oceanFloor, 8 + dx, FLOOR_Y, 8 + dz, accent);
                }
            }
        }

        // Four ceiling lights keep the sealed hall readable without a sky.
        set(chunk, surface, oceanFloor, 4, CEILING_Y, 4, light);
        set(chunk, surface, oceanFloor, 12, CEILING_Y, 4, light);
        set(chunk, surface, oceanFloor, 4, CEILING_Y, 12, light);
        set(chunk, surface, oceanFloor, 12, CEILING_Y, 12, light);

        for (int i = 0; i < 6; i++) {
            int x = 2 + i * 2;
            placeDimensionDoor(chunk, surface, oceanFloor, x, 0, NORTH_ORDER[i], Direction.SOUTH);
            placeDimensionDoor(chunk, surface, oceanFloor, x, 15, SOUTH_ORDER[i], Direction.NORTH);
        }

        placeReturnDoor(chunk, surface, oceanFloor, 15, 8, Direction.WEST);
    }

    private static void placeDimensionDoor(ChunkAccess chunk, Heightmap surface, Heightmap oceanFloor,
                                           int x, int z, int destinationIndex, Direction facing) {
        DoorDestination destination = DoorDestination.byIndex(destinationIndex);
        BlockState lower = DoorBlockInit.DIMENSION_DOOR.get().defaultBlockState()
                .setValue(DimensionDoorBlock.DESTINATION, destination.index())
                .setValue(DimensionDoorBlock.LOCKED, !destination.initiallyUnlocked())
                .setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HINGE, DoorHingeSide.LEFT)
                .setValue(DoorBlock.OPEN, false)
                .setValue(DoorBlock.POWERED, false)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
        set(chunk, surface, oceanFloor, x, WALK_Y, z, lower);
        set(chunk, surface, oceanFloor, x, WALK_Y + 1, z, lower.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
    }

    private static void placeReturnDoor(ChunkAccess chunk, Heightmap surface, Heightmap oceanFloor,
                                        int x, int z, Direction facing) {
        BlockState lower = DoorBlockInit.RETURN_DOOR.get().defaultBlockState()
                .setValue(DoorBlock.FACING, facing)
                .setValue(DoorBlock.HINGE, DoorHingeSide.LEFT)
                .setValue(DoorBlock.OPEN, false)
                .setValue(DoorBlock.POWERED, false)
                .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
        set(chunk, surface, oceanFloor, x, WALK_Y, z, lower);
        set(chunk, surface, oceanFloor, x, WALK_Y + 1, z, lower.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
    }

    private static void set(ChunkAccess chunk, Heightmap surface, Heightmap oceanFloor,
                            int x, int y, int z, BlockState state) {
        chunk.setBlockState(new BlockPos(x, y, z), state, false);
        surface.update(x & 15, y, z & 15, state);
        oceanFloor.update(x & 15, y, z & 15, state);
    }
}
