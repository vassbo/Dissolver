package net.vassbo.vanillaemc.inventory;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.EMCValues;
import net.vassbo.vanillaemc.data.SetEMC;

public class MagicInventoryInput implements Inventory {
    private final DefaultedList<ItemStack> stacks;
    private final int width;
    private final int height;
    private final ScreenHandler handler;
    private PlayerEntity player;

    private final int xPos = 0;
    private final int yPos = 0;

    public MagicInventoryInput(ScreenHandler handler, PlayerEntity player) {
        this(handler, 1, 1, DefaultedList.ofSize(1, ItemStack.EMPTY));
        this.player = player;
    }

    public MagicInventoryInput(ScreenHandler handler, int width, int height, DefaultedList<ItemStack> stacks) {
        this.stacks = stacks;
        this.handler = handler;
        this.width = width;
        this.height = height;
    }

    public Slot getInputSlot() {
        return new Slot(this, 0, xPos, yPos);
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
        if (player == null || player.getWorld().isClient()) return;

        boolean SLOT_EMPTY = stack.getItem() == Items.AIR;
        if (SLOT_EMPTY) return;

        String itemId = stack.getItem().toString();

        int emcValue = EMCValues.get(itemId);
        if (emcValue == 0) {
            // WIP this will get deleted when closing the block currently!
            // WIP item is invisible!
            this.stacks.set(slot, stack);
            this.handler.onContentChanged(this);
            VanillaEMC.LOGGER.info("Tried to add item, but it does not have any EMC value. ID: " + itemId);
            return;
        }

        int itemCount = stack.getCount();
        int addedEmcValue = emcValue * itemCount;

        VanillaEMC.LOGGER.info("ADDED STACK!!: " + itemId + " New EMC: " + addedEmcValue);
        // add block id to array list

        SetEMC.addEMCValue(player, addedEmcValue);
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
