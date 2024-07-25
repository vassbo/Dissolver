package net.vassbo.vanillaemc.inventory;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.vassbo.vanillaemc.helpers.EMCHelper;
import net.vassbo.vanillaemc.screen.MagicScreenHandler;

public class MagicSlot extends Slot {
    public int id;
    public final MagicScreenHandler handler;

    public MagicSlot(Inventory inventory, int index, int x, int y, MagicScreenHandler handler) {
        super(inventory, index, x, y);
        // this.inventory = inventory;
        // this.index = index;
        // this.x = x;
        // this.y = y;
        this.handler = handler;
    }

    // public void onQuickTransfer(ItemStack newItem, ItemStack original) {
    //     int i = original.getCount() - newItem.getCount();
    //     if (i > 0) {
    //         this.onCrafted(original, i);
    //     }
    // }

    // protected void onTake(int amount) {
    // }

    // public void onTakeItem(PlayerEntity player, ItemStack stack) {
    //     this.markDirty();
    // }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    // public void setStack(ItemStack stack) {
    //     this.setStack(stack, this.getStack());
    // }

    // public void setStack(ItemStack stack, ItemStack previousStack) {
    //     this.setStackNoCallbacks(stack);
    // }

    // public boolean canTakeItems(PlayerEntity playerEntity) {
    //     return true;
    // }

    public Optional<ItemStack> tryTakeStackRange(int min, int max, PlayerEntity player) {
        if (!this.canTakeItems(player)) {
            return Optional.empty();
        } else if (!this.canTakePartial(player) && max < this.getStack().getCount()) {
            return Optional.empty();
        } else {
            if (player.getServer() != null && !EMCHelper.getItem(player, this.getStack(), this.handler)) {
                return Optional.empty();
            }

            min = Math.min(min, max);
            ItemStack itemStack = this.takeStack(min);
            if (itemStack.isEmpty()) {
                return Optional.empty();
            } else {
                if (this.getStack().isEmpty()) {
                    this.setStack(ItemStack.EMPTY, itemStack);
                }

                return Optional.of(itemStack);
            }
        }
    }

    // often called by double clicking from another inventory
    public ItemStack takeStackRange(int min, int max, PlayerEntity player) {
        // prevent double clicking to get stack!
        return ItemStack.EMPTY;
    }

    // public ItemStack insertStack(ItemStack stack) {
    //     return this.insertStack(stack, stack.getCount());
    // }

    // public ItemStack insertStack(ItemStack stack, int count) {
    //     if (!stack.isEmpty() && this.canInsert(stack)) {
    //         ItemStack itemStack = this.getStack();
    //         int i = Math.min(Math.min(count, stack.getCount()), this.getMaxItemCount(stack) - itemStack.getCount());
    //         if (itemStack.isEmpty()) {
    //             this.setStack(stack.split(i));
    //         } else if (ItemStack.areItemsAndComponentsEqual(itemStack, stack)) {
    //             stack.decrement(i);
    //             itemStack.increment(i);
    //             this.setStack(itemStack);
    //         }

    //         return stack;
    //     } else {
    //         return stack;
    //     }
    // }

    // public boolean canTakePartial(PlayerEntity player) {
    //     return this.canTakeItems(player) && this.canInsert(this.getStack());
    // }
}
