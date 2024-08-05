package net.vassbo.vanillaemc.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.vassbo.vanillaemc.config.ModConfig;
import net.vassbo.vanillaemc.helpers.EMCHelper;
import net.vassbo.vanillaemc.screen.DissolverScreenHandler;
import net.vassbo.vanillaemc.screen.ModScreenHandlers;

public class DissolverBlockEntity extends CustomBlockEntity {
    private DefaultedList<ItemStack> inputStacks;

    public DissolverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DISSOLVER_BLOCK_ENTITY, pos, state);
        // redstone input slot
        this.inputStacks = DefaultedList.ofSize(1, ItemStack.EMPTY); 
    }

    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        DissolverScreenHandler handler = new DissolverScreenHandler(syncId, playerInventory);
        ModScreenHandlers.activeHandlers.put(playerInventory.player.getUuid(), handler);
        return handler;
    }

    protected void setHeldStacks(DefaultedList<ItemStack> itemList) {
        if (ModConfig.PRIVATE_EMC) return;
        
        EMCHelper.addItem(itemList.get(0), this.world);
        this.inputStacks = itemList;
    }

    public DefaultedList<ItemStack> getHeldStacks() {
        return this.inputStacks;
    }

    protected Text getContainerName() {
        return Text.translatable("block.vanillaemc.dissolver_block");
    }

    public int size() {
        // redstone input slot size
        return 1;
    }

    public ItemStack getRenderStack() {
        return this.getStack(0);
    }

    // HOPPER/DROPPER INSERT (WIP not working)
    
    @Override
    public int[] getAvailableSlots(Direction side) {
        // VanillaEMC.LOGGER.info("INSERTING........");
        return new int[0];
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return false;
    }

	// public int getComparatorOutput() {
	// 	int i = 0;
    // WIP get 15 * (items learned / all items)
	// 	return i;
	// }
}
