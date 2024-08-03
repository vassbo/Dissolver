package net.vassbo.vanillaemc.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class DissolverSlotRemove extends Slot {
    public int id;

    public DissolverSlotRemove(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    // public ItemStack insertStack(ItemStack stack, int count) {
    //     if (stack.isEmpty() || !this.canInsert(stack)) return stack;

    //     String itemId = stack.getItem().toString();
    //     EMCHelper.learnItem(player, itemId);

    //     return stack;
    // }
}
