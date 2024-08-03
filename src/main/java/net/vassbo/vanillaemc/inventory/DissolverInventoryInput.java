package net.vassbo.vanillaemc.inventory;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.vassbo.vanillaemc.helpers.EMCHelper;
import net.vassbo.vanillaemc.screen.DissolverScreenHandler;

public class DissolverInventoryInput implements Inventory {
    private final DefaultedList<ItemStack> stacks;
    private final int width;
    private final int height;
    private final DissolverScreenHandler handler;
    private PlayerEntity player;

    private int SLOTS = 3;

    public DissolverInventoryInput(DissolverScreenHandler handler, PlayerEntity player) {
        this.stacks = DefaultedList.ofSize(SLOTS, ItemStack.EMPTY);
        this.handler = handler;
        this.player = player;
        this.width = SLOTS;
        this.height = 1;
    }

    public DissolverSlotInput getInputSlot() {
        return new DissolverSlotInput(this, 0, 7, 18);
    }

    public Slot getAdderSlot() {
        return new Slot(this, 1, 7, 54);
    }

    public Slot getRemoverSlot() {
        return new Slot(this, 2, 7, 72);
    }

    public int slots() {
        return this.SLOTS;
    }

    public int size() {
        return this.stacks.size();
    }

    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.stacks.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    public ItemStack getStack(int slot) {
        return slot >= this.size() ? ItemStack.EMPTY : (ItemStack)this.stacks.get(slot);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.stacks, slot);
    }

    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.stacks, slot, amount);
        if (!itemStack.isEmpty()) {
            this.handler.onContentChanged(this);
        }

        return itemStack;
    }

    public void setStack(int slot, ItemStack stack) {
        if (player == null) return;

        boolean NOT_HOLDING_ITEM = stack.getItem() == Items.AIR;
        if (NOT_HOLDING_ITEM) return;

        if (!player.getWorld().isClient()) {
            if (slot == 0) {
                if (EMCHelper.addItem(stack, player, this.handler)) return;
            } else if (slot == 1) {
                // WIP temporarily store items placed here (because they disapear if menu is closed!)
                String itemId = stack.getItem().toString();
                EMCHelper.learnItem(player, itemId);
                this.handler.refresh();
            } else if (slot == 2) {
                // WIP temporarily store items placed here (because they disapear if menu is closed!)
                String itemId = stack.getItem().toString();
                EMCHelper.forgetItem(player, itemId);
                this.handler.refresh();
            }
        }

        if (slot == 0 && player.getWorld().isClient()) return;

        this.stacks.set(slot, stack);
        this.handler.onContentChanged(this);
    }

    public void markDirty() {
    }

    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    public void clear() {
        this.stacks.clear();
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public List<ItemStack> getHeldStacks() {
        return List.copyOf(this.stacks);
    }
}
