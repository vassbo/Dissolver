package net.vassbo.vanillaemc.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.vassbo.vanillaemc.helpers.EMCHelper;

public class DissolverSlotInput extends Slot {
    public int id;

    public DissolverSlotInput(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    // @Override
    // public boolean canInsert(ItemStack stack) {
    //     return false;
    // }

    // public void setStack(ItemStack stack) {
    //     this.setStack(stack, this.getStack());
    // }

    // public void setStack(ItemStack stack, ItemStack previousStack) {
    //     this.setStackNoCallbacks(stack);
    // }

    // public ItemStack insertStack(ItemStack stack) {
    //     return this.insertStack(stack, stack.getCount());
    // }

    public ItemStack insertStack(ItemStack stack, int count) {
        if (stack.isEmpty() || !this.canInsert(stack)) return stack;

        // check that item has emc and is valid
        if (!EMCHelper.addItem(stack)) return stack;

        ItemStack itemStack = this.getStack();
        int i = Math.min(Math.min(count, stack.getCount()), this.getMaxItemCount(stack) - itemStack.getCount());

        if (itemStack.isEmpty()) {
            this.setStack(stack.split(i));
        } else if (ItemStack.areItemsAndComponentsEqual(itemStack, stack)) {
            stack.decrement(i);
            itemStack.increment(i);
            this.setStack(itemStack);
        }

        return stack;
    }
}
