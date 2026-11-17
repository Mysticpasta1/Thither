package com.mystic.thither.doors.blocks;

import com.mystic.thither.doors.DoorDestination;
import com.mystic.thither.doors.DoorDimension;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

/**
 * One of the twelve doors in the door dimension. The destination and lock state live on
 * the lower half; the upper half copies the same values so either half can be clicked.
 */
public class DimensionDoorBlock extends DoorBlock {
    public static final IntegerProperty DESTINATION = IntegerProperty.create("destination", 0, DoorDestination.values().length - 1);
    public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

    public DimensionDoorBlock() {
        super(BlockBehaviour.Properties.of()
                        .sound(SoundType.METAL)
                        .strength(-1.0F, 3600000.0F)
                        .noOcclusion()
                        .noLootTable()
                        .pushReaction(PushReaction.BLOCK)
                        .isValidSpawn((state, level, pos, type) -> false)
                        .isSuffocating((state, level, pos) -> false)
                        .isViewBlocking((state, level, pos) -> false)
                        .lightLevel(state -> state.getValue(LOCKED) ? 3 : 12),
                BlockSetType.IRON);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DESTINATION, LOCKED);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        BlockPos lowerPos = DoorUtil.lowerPos(state, pos);
        BlockState lowerState = level.getBlockState(lowerPos);
        if (!(lowerState.getBlock() instanceof DimensionDoorBlock)) {
            return InteractionResult.PASS;
        }

        DoorDestination destination = DoorDestination.byIndex(lowerState.getValue(DESTINATION));
        if (lowerState.getValue(LOCKED)) {} /*defeating the boss of the dimension will directly unlock the next door*/

        if (destination.implemented()) {
            if (DoorDimension.enterDestination(serverPlayer, destination)) {
                DoorUtil.setOpen(level, lowerPos, lowerState, true, player);
            }
        } else {
            player.displayClientMessage(Component.translatable("message.thither.door_unwoven",
                    destination.displayName()).withStyle(ChatFormatting.GRAY), true);
        }
        return InteractionResult.CONSUME;
    }

    private static void setLocked(Level level, BlockPos lowerPos, BlockState lowerState, boolean locked) {
        level.setBlock(lowerPos, lowerState.setValue(LOCKED, locked), Block.UPDATE_ALL);
        BlockPos upperPos = lowerPos.above();
        BlockState upperState = level.getBlockState(upperPos);
        if (upperState.is(lowerState.getBlock()) && upperState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            level.setBlock(upperPos, upperState.setValue(LOCKED, locked), Block.UPDATE_ALL);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(HALF) != DoubleBlockHalf.LOWER) {
            return;
        }
        if (state.getValue(LOCKED)) {
            if (random.nextInt(6) == 0) {
                level.addParticle(ParticleTypes.ENCHANT,
                        pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.7D,
                        pos.getY() + 1.0D + random.nextDouble() * 1.4D,
                        pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.7D,
                        0.0D, 0.03D, 0.0D);
            }
            return;
        }
        for (int i = 0; i < 2; i++) {
            level.addParticle(ParticleTypes.END_ROD,
                    pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.8D,
                    pos.getY() + random.nextDouble() * 1.8D,
                    pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.8D,
                    (random.nextDouble() - 0.5D) * 0.02D, 0.01D,
                    (random.nextDouble() - 0.5D) * 0.02D);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }
}
