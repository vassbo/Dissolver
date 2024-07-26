package net.vassbo.vanillaemc.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.vassbo.vanillaemc.data.EMCValues;
import net.vassbo.vanillaemc.data.StateSaverAndLoader;
import net.vassbo.vanillaemc.helpers.ItemHelper;
import net.vassbo.vanillaemc.helpers.EMCHelper;
import net.vassbo.vanillaemc.inventory.MagicInventory;
import net.vassbo.vanillaemc.inventory.MagicInventoryInput;
import net.vassbo.vanillaemc.inventory.MagicSlot;

public class MagicScreenHandler extends ScreenHandler {
    private final int WIDTH_SIZE = 5;
    private final int HEIGHT_SIZE = 5;
    private final int PLAYER_INV_SIZE = 36; // player inventory size (9 * 4)
    private final PlayerEntity player;

    private final MagicInventory inventory;
    private final MagicInventoryInput inventoryInput;

    public MagicScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.MAGIC_SCREEN_HANDLER, syncId);

        this.player = playerInventory.player;

        // auto size
        // double length = VanillaEMC.LEARNED_ITEMS.size() / (double)WIDTH_SIZE;
        // int yHeight = (int)Math.ceil(length);
        // this.inventory = new MagicInventory(this, WIDTH_SIZE, yHeight);

        this.inventory = new MagicInventory(this, WIDTH_SIZE, HEIGHT_SIZE);
        this.inventoryInput = new MagicInventoryInput(this, player);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addSlots(inventory);
        addInputSlot(inventoryInput);
    }

    private void addInputSlot(MagicInventoryInput inventory) {
        this.addSlot(inventory.getInputSlot());
    }

    private int DEFAULT_SLOT_SIZE = 18;
    private int START_X_POS = 45;
    private int START_Y_POS = 20;
    private void addSlots(MagicInventory inventory) {
        int MAX_SIZE = WIDTH_SIZE * HEIGHT_SIZE;

        int xIndex = -1;
        int yIndex = -1;
        int index = -1;
        for (int i = 0; i < MAX_SIZE; i++) {
            xIndex++;
            if (xIndex >= WIDTH_SIZE) {
                xIndex = 0;
                yIndex++;
            }
            
            index++;
            Slot customSlot = new MagicSlot(inventory, index, DEFAULT_SLOT_SIZE * xIndex + START_X_POS, DEFAULT_SLOT_SIZE * yIndex + START_Y_POS, this);
            this.addSlot(customSlot);

            // set to air
            ItemStack stack = Items.AIR.getDefaultStack();
            inventory.setStack(index, stack);
        }

        addItems();
    }

    private void addItems() {
        if (player.getServer() == null) return;

        List<Item> ITEMS = filterItems();
        int currentPlayerEMC = EMCHelper.getEMCValue(player);

        this.slots.forEach((Slot slot) -> {
            boolean isCustomSlot = !slot.canInsert(Items.AIR.getDefaultStack());
            if (!isCustomSlot) return;

            int index = slot.getIndex();
            if (index >= ITEMS.size()) return; // no more items in list

            Item item = ITEMS.get(index);
            ItemStack stack = item.getDefaultStack();

            stack = getHighestPossibleStack(currentPlayerEMC, stack);

            inventory.setStack(index, stack);
        });
    }

    private ItemStack getHighestPossibleStack(int playerEMC, ItemStack stack) {
        int emcValue = EMCValues.get(stack.getItem().toString());

        if (emcValue == 0) {
            // WIP set red nbt data
            return stack;
        }

        if (emcValue > playerEMC) {
            // WIP set red nbt data
            return stack;
        }

        int maxItems = playerEMC / emcValue; // auto floored
        stack.setCount(maxItems);
        stack.capCount(stack.getMaxCount());

        return stack;
    }

    private void clearItems() {
        this.slots.forEach((Slot slot) -> {
            int slotIndex = slot.getIndex();
            if (slotIndex <= PLAYER_INV_SIZE) return; // player inv
            if (slotIndex == this.slots.size() - 1) return; // input slot

            slot.setStack(Items.AIR.getDefaultStack());
        });
    }

    private List<Item> filterItems() {
        List<Item> ITEMS = new ArrayList<>();
        List<String> learnedList = StateSaverAndLoader.getPlayerState(player).LEARNED_ITEMS;

        if (learnedList.size() < 1) return new ArrayList<>();

        // WIP filter
        // this.searchValue

        int currentPlayerEMC = EMCHelper.getEMCValue(player);

        // sort by name
        Collections.sort(learnedList.subList(0, learnedList.size()));
        // sort by highest emc values
        learnedList = sortByEMC(learnedList);
        // place all items player can afford first
        learnedList = sortByValid(learnedList, currentPlayerEMC);

        learnedList.forEach((itemId) -> {
            Item item = ItemHelper.getById(itemId);
            ITEMS.add(item);
        });

        return ITEMS;
    }

    private List<String> sortByEMC(List<String> items) {
        Collections.sort(items, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int emc1 = EMCValues.get(o1);
                int emc2 = EMCValues.get(o2);
                return emc1 == emc2 ? -1 : emc1 > emc2 ? -1 : 1;
            }
        });

        return items;
    }

    private List<String> sortByValid(List<String> items, int playerEMC) {
        Collections.sort(items, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                boolean canAfford1 = EMCValues.get(o1) <= playerEMC;
                boolean canAfford2 = EMCValues.get(o2) <= playerEMC;
                return canAfford1 && canAfford2 ? -1 : canAfford1 ? -1 : 1;
            }
        });

        return items;
    }

    // refresh inventory slots when item added/removed!
    public void refresh() {
        clearItems();
        addItems();
    }

    @Override
    // only take items from inv, not add
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot == null || !slot.hasStack() || player.getServer() == null) return newStack;

        if (invSlot < PLAYER_INV_SIZE) {
            if (!EMCHelper.addItem(slot.getStack())) return newStack;
        }

        int inputSlotIndex = this.slots.size() - 1;
        // click in custom inventory
        if (invSlot >= PLAYER_INV_SIZE && invSlot < inputSlotIndex) {
            if (!EMCHelper.getItem(player, slot.getStack(), this, true)) return newStack;
        }

        ItemStack originalStack = slot.getStack();
        newStack = originalStack.copy();

        if (invSlot < PLAYER_INV_SIZE) { // default inventory
            // always quick move to input slot
            int lastSlotIndex = this.slots.size() - 1;
            boolean itemInserted = this.insertItem(originalStack, lastSlotIndex, this.slots.size(), true);
            if (!itemInserted) return ItemStack.EMPTY;
        } else { // input slot or custom inventory
            boolean itemInserted = this.insertItem(originalStack, 0, this.inventory.size() + 1, false);
            if (!itemInserted) return ItemStack.EMPTY;
        }

        if (originalStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        return newStack;
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public MagicInventory getInventory() {
        return this.inventory;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
