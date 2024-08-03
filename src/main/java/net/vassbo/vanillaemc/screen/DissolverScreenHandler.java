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
import net.vassbo.vanillaemc.data.PlayerData;
import net.vassbo.vanillaemc.data.StateSaverAndLoader;
import net.vassbo.vanillaemc.helpers.EMCHelper;
import net.vassbo.vanillaemc.helpers.ItemHelper;
import net.vassbo.vanillaemc.inventory.DissolverInventory;
import net.vassbo.vanillaemc.inventory.DissolverInventoryInput;
import net.vassbo.vanillaemc.inventory.DissolverSlot;
import net.vassbo.vanillaemc.packets.DataSender;

public class DissolverScreenHandler extends ScreenHandler {
    private final int WIDTH_SIZE = 9;
    private final int HEIGHT_SIZE = 6;
    public final int CUSTOM_INV_SIZE = WIDTH_SIZE * HEIGHT_SIZE;
    private final int PLAYER_INV_SIZE = 36; // player inventory size (9 * 4)

    private final PlayerEntity player;

    private final DissolverInventory inventory;
    private final DissolverInventoryInput inventoryInput;

    public List<Item> itemList = new ArrayList<>();

    public DissolverScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.DISSOLVER_SCREEN_HANDLER_TYPE, syncId);

        this.player = playerInventory.player;

        this.inventory = new DissolverInventory(this, WIDTH_SIZE, HEIGHT_SIZE);
        this.inventoryInput = new DissolverInventoryInput(this, player);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addSlots(inventory);
        this.addSlot(inventoryInput.getRemoverSlot());
        this.addSlot(inventoryInput.getAdderSlot());
        this.addSlot(inventoryInput.getInputSlot());
    }

    // INVENTORIES

    int PLAYER_START_X_POS = 31; // 8
    int PLAYER_START_Y_POS = 140; // 84
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, PLAYER_START_X_POS + l * 18, PLAYER_START_Y_POS + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, PLAYER_START_X_POS + i * 18, PLAYER_START_Y_POS + 58));
        }
    }

    private int DEFAULT_SLOT_SIZE = 18;
    private int START_X_POS = 31;
    private int START_Y_POS = 36;
    private void addSlots(DissolverInventory inventory) {
        int xIndex = -1;
        int yIndex = -1;
        int index = -1;
        for (int i = 0; i < CUSTOM_INV_SIZE; i++) {
            xIndex++;
            if (xIndex >= WIDTH_SIZE) {
                xIndex = 0;
                yIndex++;
            }
            
            index++;
            Slot customSlot = new DissolverSlot(inventory, index, DEFAULT_SLOT_SIZE * xIndex + START_X_POS, DEFAULT_SLOT_SIZE * yIndex + START_Y_POS, this);
            this.addSlot(customSlot);

            // set to air
            ItemStack stack = Items.AIR.getDefaultStack();
            inventory.setStack(index, stack);
        }

        addItems();
    }

    // ITEMS

    private void addItems() {
        List<String> FILTERED = filterItems();
        List<String> SEARCHED = searchFilter(FILTERED);
        convertIdsToItems(SEARCHED);

        scrollItems(this.scrollPosition);
    }

    private List<String> searchFilter(List<String> items) {
        if (searchValue == "") return items;

        List<String> newItems = new ArrayList<>();
        for (String itemId : items) {
            Item item = ItemHelper.getById(itemId);
            String itemName = item.getName().getString().toLowerCase();
            if (itemName.contains(searchValue.toLowerCase())) newItems.add(itemId);
        }
        
        return newItems;
    }

    private List<Item> convertIdsToItems(List<String> itemIds) {
        // send to client (for scroll bar)
        if (player.getServer() != null) {
            // copy so items stored don't get deleted!
            PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
            PlayerData newData = new PlayerData();
            newData.EMC = playerState.EMC;
            newData.LEARNED_ITEMS = itemIds;
            DataSender.sendPlayerData(player, newData);
        }

        List<Item> ITEMS = new ArrayList<>();
        itemIds.forEach((itemId) -> {
            Item item = ItemHelper.getById(itemId);
            ITEMS.add(item);
        });
        
        itemList = new ArrayList<Item>(ITEMS);
        return ITEMS;
    }

    private ItemStack getHighestStack(ItemStack stack) {
        int currentPlayerEMC = EMCHelper.getEMCValue(player);
        return getHighestPossibleStack(currentPlayerEMC, stack);
    }

    private ItemStack getHighestPossibleStack(int playerEMC, ItemStack stack) {
        int emcValue = EMCValues.get(stack.getItem().toString());

        if (emcValue == 0) {
            // WIP set red nbt data
            return stack;
        }

        if (emcValue > playerEMC) {
            // WIP set red nbt data
            // VanillaEMC.LOGGER.info("DATA: " + stack.getItem().getTooltipData(stack));
            // Item item = stack.getItem();
            // TooltipContext context = item.TooltipContext();
            // List<Text> tooltip = new ArrayList<>();
            // tooltip.add(Text.literal("HELLO"));
            // VanillaEMC.LOGGER.info("TT: " + tooltip);
            // item.appendTooltip(stack, context, tooltip, TooltipType.BASIC);
            // VanillaEMC.LOGGER.info("TT22: " + item.getTooltipData(item.getDefaultStack()));
            // return item.getDefaultStack();
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

    // FILTER

    private List<String> filterItems() {
        List<String> learnedList = new ArrayList<>(StateSaverAndLoader.getPlayerState(player).LEARNED_ITEMS);
        if (learnedList.size() < 1) return new ArrayList<>();

        int currentPlayerEMC = EMCHelper.getEMCValue(player);

        // sort by name
        learnedList = sortByName(learnedList);
        // sort by highest emc values
        learnedList = sortByEMC(learnedList);
        // place all items player can afford first
        learnedList = sortByValid(learnedList, currentPlayerEMC);

        return learnedList;
    }

    private List<String> sortByName(List<String> items) {
        Collections.sort(items, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String string1 = o1.substring(o1.indexOf(":") + 1);
                String string2 = o2.substring(o2.indexOf(":") + 1);
                // this is intentionally inverted to the next sorts will turn it back
                return string2.compareTo(string1);
            }
        });

        return items;
    }

    private List<String> sortByEMC(List<String> items) {
        Collections.sort(items, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int emc1 = EMCValues.get(o1);
                int emc2 = EMCValues.get(o2);
                // this is intentionally inverted to the next sort will turn it back
                return emc1 == emc2 ? 0 : emc1 > emc2 ? 1 : -1;
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

    // SEARCH

    private String searchValue = "";
    public void search(String value) {
        searchValue = value;
        refresh();
    }

    // SCROLL

    private float scrollPosition = 0.0F;
    public void scrollItems(float position) {
        scrollPosition = position;
        int j = getRow(position);

        for (int k = 0; k < 6; ++k) {
            for (int l = 0; l < 9; ++l) {
                int m = l + (k + j) * 9;
                inventory.setStack(l + k * 9, m >= 0 && m < this.itemList.size() ? getHighestStack(this.itemList.get(m).getDefaultStack()) : ItemStack.EMPTY);
            }
        }
    }
    
    private int getRow(float position) {
        // temporarily adding an extra row! (seems to fix the issue with the last row not showing up!)
        return (int) Math.max(0, (int)(position * ((this.itemList.size() + 9) / 9F - 6)) + 0.5D);
    }

    // QUICK MOVE

    // only take items from inv, not add
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot == null || !slot.hasStack()) return newStack;

        // getting double stack if server is not checked
        if (player.getServer() == null) return ItemStack.EMPTY;

        if (invSlot < PLAYER_INV_SIZE) {
            if (!EMCHelper.addItem(slot.getStack())) return newStack;
        }

        int inputSlotsStartIndex = this.slots.size() - this.inventoryInput.slots();
        // click in custom inventory
        if (invSlot >= PLAYER_INV_SIZE && invSlot < inputSlotsStartIndex) {
            boolean CANT_GET_ITEM = !EMCHelper.getItem(player, slot.getStack(), this, slot.getStack().getCount());
            if (CANT_GET_ITEM) return newStack;
        }

        ItemStack originalStack = slot.getStack();
        newStack = originalStack.copy();

        if (invSlot < PLAYER_INV_SIZE) { // default inventory
            // always quick move to input slot
            int lastSlotIndex = this.slots.size() - 1;
            boolean itemInserted = this.insertItem(originalStack, lastSlotIndex, this.slots.size(), true);
            if (!itemInserted) return ItemStack.EMPTY;
        } else { // input slots or custom inventory
            boolean itemInserted = this.insertItem(originalStack, 0, PLAYER_INV_SIZE, false);
            if (!itemInserted) return ItemStack.EMPTY;
        }

        if (originalStack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        return newStack;
    }

    // EXTRA

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public DissolverInventory getInventory() {
        return this.inventory;
    }
}
