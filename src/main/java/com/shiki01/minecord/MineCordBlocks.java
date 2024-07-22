package com.shiki01.minecord;

import com.shiki01.minecord.util.MineCordBlockState;
import com.shiki01.minecord.util.MineCordGUIOpener;
import com.shiki01.minecord.util.MineCordLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;


public class MineCordBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MineCord.MOD_ID);
    public static final RegistryObject<Block> MINE_CORD = BLOCKS.register("mine_cord", MineCordBlock::new);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

class MineCordBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public MineCordBlock() {
        super(BlockBehaviour.Properties.of(Material.EXPLOSIVE)
                .lightLevel(value -> 0)
                .strength(2.0f, 6.0f)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, @NotNull LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        ItemStack item = new ItemStack(MineCordBlocks.MINE_CORD.get());
        drops.add(item);
        return drops;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!world.isClientSide) {
            Map<BlockPos, MineCordBlockState> blockStates;
            try {
                blockStates = MineCordBlockState.loadFromFile("blockStates.dat");
            } catch (IOException | ClassNotFoundException e) {
                blockStates = new HashMap<>();
                MineCordLogger.logger.error("Failed to load block states", e);
            }
            blockStates.remove(pos);
            try {
                MineCordBlockState.saveToFile(blockStates, "blockStates.dat");
            } catch (IOException e) {
                MineCordLogger.logger.error("Failed to save block states", e);
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!world.isClientSide && player instanceof ServerPlayer) {
            MineCordGUIOpener.openCustomGui((ServerPlayer) player, world, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}