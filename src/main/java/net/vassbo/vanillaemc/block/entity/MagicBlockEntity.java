package net.vassbo.vanillaemc.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.screen.MagicScreenHandler;

public class MagicBlockEntity extends CustomBlockEntity {
    private DefaultedList<ItemStack> inputStacks;

    public MagicBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAGIC_BLOCK_ENTITY, pos, state);
        this.inputStacks = DefaultedList.ofSize(VanillaEMC.LEARNED_ITEMS.size(), ItemStack.EMPTY);
    }

    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new MagicScreenHandler(syncId, playerInventory); // , this
    }

    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inputStacks = inventory;
    }

    public DefaultedList<ItemStack> getHeldStacks() {
        return this.inputStacks;
    }

    protected Text getContainerName() {
        return Text.translatable("block.vanillaemc.magic_block");
    }

    public int size() {
        // "dynamic" size
        return 0;
    }

    public ItemStack getRenderStack() {
        return this.getStack(0);
    }
}
