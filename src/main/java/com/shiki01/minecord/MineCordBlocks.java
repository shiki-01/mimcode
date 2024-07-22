package com.shiki01.minecord;

import com.shiki01.minecord.util.MineCordBlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import net.minecraft.world.phys.AABB;

import static net.minecraftforge.network.NetworkHooks.*;

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
        super(BlockBehaviour.Properties.of(Material.EXPLOSIVE).lightLevel(value -> 0));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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
    public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        ItemStack itemStack = new ItemStack(Blocks.DIRT.asItem());
        Block.popResource(world, pos, itemStack);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        // Your custom logic here, for example:
        if (!world.isClientSide) {
            // Example: Retrieve and apply block state from ItemStack if it carries custom NBT data
            if (itemStack.hasTag() && itemStack.getTag().contains("BlockUUID")) {
                UUID blockUUID = itemStack.getTag().getUUID("BlockUUID");
                MineCordBlockState blockState = MineCordBlockState.getBlockState(blockUUID);
                blockState.setPos(pos);
                blockState.saveBlockState();
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!world.isClientSide && player instanceof ServerPlayer) {
            openScreen((ServerPlayer) player, new SimpleMenuProvider(
                            (windowId, inv, playerEntity) -> new MineCordContainer(windowId, world, pos, inv, playerEntity),
                            Component.literal("Mine Cord GUI")),
                    (buffer) -> buffer.writeBlockPos(pos)); // Ensure you're sending the necessary data to the client, like the block position.
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}