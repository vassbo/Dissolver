package net.vassbo.vanillaemc.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.screen.DissolverScreenHandler;

public class DissolverBlockEntity extends CustomBlockEntity {
    private DefaultedList<ItemStack> inputStacks;

    public DissolverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DISSOLVER_BLOCK_ENTITY, pos, state);
        // "dynamic" size
        this.inputStacks = DefaultedList.ofSize(0, ItemStack.EMPTY);
    }

    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        DissolverScreenHandler handler = new DissolverScreenHandler(syncId, playerInventory);
        VanillaEMC.activeHandlers.put(playerInventory.player.getUuid(), handler);
        return handler;
    }

    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inputStacks = inventory;
    }

    public DefaultedList<ItemStack> getHeldStacks() {
        return this.inputStacks;
    }

    protected Text getContainerName() {
        return Text.translatable("block.vanillaemc.dissolver_block");
    }

    public int size() {
        // "dynamic" size
        return 0;
    }

    public ItemStack getRenderStack() {
        return this.getStack(0);
    }

    // test

    public int endCrystalAge = 10;
}
