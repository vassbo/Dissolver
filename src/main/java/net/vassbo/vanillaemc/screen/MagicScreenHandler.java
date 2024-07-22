package net.vassbo.vanillaemc.screen;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.EMCValues;
import net.vassbo.vanillaemc.data.SetEMC;
import net.vassbo.vanillaemc.inventory.MagicInventory;
import net.vassbo.vanillaemc.inventory.MagicInventoryInput;

public class MagicScreenHandler extends ScreenHandler {
    private final int WIDTH_SIZE = 5;
    private final int HEIGHT_SIZE = 5;
    private final int PLAYER_INV_SIZE = 36; // player inventory size (9 * 4)
    // private final int STARTING_INDEX = 9*4 + 1; // player inventory size
    private final PlayerEntity player;

    private int quickCraftButton;
    private int quickCraftStage;
    private Set<Slot> quickCraftSlots = Sets.newHashSet();

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
        // int listSize = VanillaEMC.LEARNED_ITEMS.size();
        // int maxValue = MAX_SIZE > listSize ? listSize : MAX_SIZE;
        // List<Item> LEARNED_ITEMS = VanillaEMC.LEARNED_ITEMS.subList(0, maxValue);

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
            this.addSlot(new Slot(inventory, index, DEFAULT_SLOT_SIZE * xIndex + START_X_POS, DEFAULT_SLOT_SIZE * yIndex + START_Y_POS));

            // display items
            Item item = VanillaEMC.LEARNED_ITEMS.size() > index ? VanillaEMC.LEARNED_ITEMS.get(index) : Items.AIR;
            ItemStack stack = item.getDefaultStack();
            inventory.setVisualStack(index, stack);
        }
    }

    @Override
    // only take items from inv, not add
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        VanillaEMC.LOGGER.info("SLOT: " + invSlot, " - " + slot);

        if (slot == null || !slot.hasStack()) return newStack;

        if (invSlot < PLAYER_INV_SIZE) {
            String itemId = slot.getStack().getItem().toString();
            int emcValue = EMCValues.get(itemId);
            if (emcValue == 0) {
                VanillaEMC.LOGGER.info("Tried to add item, but it does not have any EMC value. ID: " + itemId);
                return newStack;
            }
        }

        int inputSlotIndex = this.slots.size() - 1;
        // click in custom inventory
        if (invSlot >= PLAYER_INV_SIZE && invSlot < inputSlotIndex) {
            // reduce emc
            String itemId = slot.getStack().getItem().toString();
            int emcValue = EMCValues.get(itemId);
            if (emcValue == 0) {
                VanillaEMC.LOGGER.info("Tried to get item, but it does not have an EMC value. ID: " + itemId);
                return newStack;
            }
            if (!SetEMC.removeEMCValue(player, emcValue)) {
                player.sendMessage(Text.translatable("emc.action.not_enough"));
                return newStack;
            }
        }

        ItemStack originalStack = slot.getStack();
        newStack = originalStack.copy();

        // boolean itemInserted = this.insertItem(originalStack, this.inventory.size() + 1, this.slots.size(), true);
        // if (!itemInserted) return ItemStack.EMPTY;

        if (invSlot < PLAYER_INV_SIZE) { // default inventory
            // always place in input slot
            int lastSlotIndex = this.slots.size() - 1;
            VanillaEMC.LOGGER.info("TO INPUT: " + lastSlotIndex);
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
    
    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        if (player.getWorld().isClient()) return;

        try {
            this.internalOnSlotClick(slotIndex, button, actionType, player);
        } catch (Exception var8) {
            VanillaEMC.LOGGER.error("Error clicking container!", var8);
        }
    }

    private void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        VanillaEMC.LOGGER.info("SLOT CLICKED: " + slotIndex);
        // VanillaEMC.LOGGER.info("BUTTON: " + button);
        VanillaEMC.LOGGER.info("ACTION: " + actionType);

        // // fix inventory issue
        // if (actionType == SlotActionType.QUICK_CRAFT && slotIndex != -999) {
        //     actionType = SlotActionType.PICKUP;
        // }

        // WIP quick craft stopping item from being added if picked up and placed another slot first
        
        if ((actionType == SlotActionType.QUICK_CRAFT)) {
            quickCraft(slotIndex, button, actionType, player);
            return;
        }

        if (this.quickCraftStage != 0) {
            this.endQuickCraft();
            return;
        }
        
        if ((actionType == SlotActionType.PICKUP || actionType == SlotActionType.QUICK_MOVE) && (button == 0 || button == 1)) {
            pickOrQuickMove(slotIndex, button, actionType, player);
            return;
        }
            
        if (actionType == SlotActionType.SWAP && (button >= 0 && button < 9 || button == 40)) {
            swapStacks(slotIndex, button, actionType, player);
            return;
        }

        if (actionType == SlotActionType.CLONE && player.isInCreativeMode() && this.getCursorStack().isEmpty() && slotIndex >= 0) {
            cloneStack(slotIndex, button, actionType, player);
            return;
        }
        
        if (actionType == SlotActionType.THROW && this.getCursorStack().isEmpty() && slotIndex >= 0) {
            throwStack(slotIndex, button, actionType, player);
            return;
        }
        
        if (actionType == SlotActionType.PICKUP_ALL && slotIndex >= 0) {
            pickupStack(slotIndex, button, actionType, player);
            return;
        }
    }

    private void quickCraft(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        Slot slot;
        ItemStack itemStack;
        ItemStack itemStack2;
        int j;
        int k;

        int i = this.quickCraftStage;
        this.quickCraftStage = unpackQuickCraftStage(button);
        if ((i != 1 || this.quickCraftStage != 2) && i != this.quickCraftStage) {
            this.endQuickCraft();
        } else if (this.getCursorStack().isEmpty()) {
            this.endQuickCraft();
        } else if (this.quickCraftStage == 0) {
            this.quickCraftButton = unpackQuickCraftButton(button);
            if (shouldQuickCraftContinue(this.quickCraftButton, player)) {
                this.quickCraftStage = 1;
                this.quickCraftSlots.clear();
            } else {
                this.endQuickCraft();
            }
        } else if (this.quickCraftStage == 1) {
            slot = (Slot)this.slots.get(slotIndex);
            itemStack = this.getCursorStack();
            if (canInsertItemIntoSlot(slot, itemStack, true) && slot.canInsert(itemStack) && (this.quickCraftButton == 2 || itemStack.getCount() > this.quickCraftSlots.size()) && this.canInsertIntoSlot(slot)) {
                this.quickCraftSlots.add(slot);
            }
        } else if (this.quickCraftStage == 2) {
            if (!this.quickCraftSlots.isEmpty()) {
                if (this.quickCraftSlots.size() == 1) {
                    j = ((Slot)this.quickCraftSlots.iterator().next()).id;
                    this.endQuickCraft();
                    this.internalOnSlotClick(j, this.quickCraftButton, SlotActionType.PICKUP, player);
                    return;
                }

                itemStack2 = this.getCursorStack().copy();
                if (itemStack2.isEmpty()) {
                    this.endQuickCraft();
                    return;
                }

                k = this.getCursorStack().getCount();
                Iterator<Slot> var9 = this.quickCraftSlots.iterator();

                label316:
                while(true) {
                    Slot slot2;
                    ItemStack itemStack3;
                    do {
                        do {
                        do {
                            do {
                                if (!var9.hasNext()) {
                                    itemStack2.setCount(k);
                                    this.setCursorStack(itemStack2);
                                    break label316;
                                }

                                slot2 = (Slot)var9.next();
                                itemStack3 = this.getCursorStack();
                            } while(slot2 == null);
                        } while(!canInsertItemIntoSlot(slot2, itemStack3, true));
                        } while(!slot2.canInsert(itemStack3));
                    } while(this.quickCraftButton != 2 && itemStack3.getCount() < this.quickCraftSlots.size());

                    if (this.canInsertIntoSlot(slot2)) {
                        int l = slot2.hasStack() ? slot2.getStack().getCount() : 0;
                        int m = Math.min(itemStack2.getMaxCount(), slot2.getMaxItemCount(itemStack2));
                        int n = Math.min(calculateStackSize(this.quickCraftSlots, this.quickCraftButton, itemStack2) + l, m);
                        k -= n - l;
                        slot2.setStack(itemStack2.copyWithCount(n));
                    }
                }
            }

            this.endQuickCraft();
        } else {
            this.endQuickCraft();
        }
    }

    private void pickOrQuickMove(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        Slot slot;
        ItemStack itemStack;
        int o;

        ClickType clickType = button == 0 ? ClickType.LEFT : ClickType.RIGHT;
        if (slotIndex == -999) {
            if (this.getCursorStack().isEmpty()) return;

            if (clickType == ClickType.LEFT) {
                player.dropItem(this.getCursorStack(), true);
                this.setCursorStack(ItemStack.EMPTY);
            } else {
                player.dropItem(this.getCursorStack().split(1), true);
            }

            return;
        }
        
        if (actionType == SlotActionType.QUICK_MOVE) {
            if (slotIndex < 0) return;

            slot = (Slot)this.slots.get(slotIndex);
            if (!slot.canTakeItems(player)) return;

            for(itemStack = this.quickMove(player, slotIndex); !itemStack.isEmpty() && ItemStack.areItemsEqual(slot.getStack(), itemStack); itemStack = this.quickMove(player, slotIndex)) {
            }
            
            return;
        }

        // click pick stack
        if (slotIndex < 0) return;

        ItemStack itemStack4 = this.getCursorStack();
        boolean holdingItem = itemStack4.getItem() != Items.AIR;

        slot = (Slot)this.slots.get(slotIndex);
        itemStack = slot.getStack();
        
        int inputSlotIndex = this.slots.size() - 1;
        // click in custom inventory
        if (slotIndex >= PLAYER_INV_SIZE && slotIndex < inputSlotIndex) {
            // add to "input" slot
            if (holdingItem) slotIndex = inputSlotIndex;
            else {
                // reduce emc
                String itemId = itemStack.getItem().toString();
                int emcValue = EMCValues.get(itemId);
                if (emcValue == 0) {
                    VanillaEMC.LOGGER.info("Tried to get item, but it does not have an EMC value. ID: " + itemId);
                    return;
                }
                if (!SetEMC.removeEMCValue(player, emcValue)) {
                    player.sendMessage(Text.translatable("emc.action.not_enough"));
                    return;
                }
            }
        }

        // don't add if item don't have emc
        String itemId = itemStack4.getItem().toString();
        int emcValue = EMCValues.get(itemId);
        // item dropped on custom inventory & does not have emc
        if (holdingItem && slotIndex == inputSlotIndex && emcValue == 0) {
            VanillaEMC.LOGGER.info("Tried to add item, but it does not have any EMC value. ID: " + itemId);
            return;
        }

        player.onPickupSlotClick(itemStack4, slot.getStack(), clickType);
        if (this.handleSlotClick(player, clickType, slot, itemStack, itemStack4)) {
            slot.markDirty();
            return;
        }
        
        if (itemStack.isEmpty()) {
            if (!itemStack4.isEmpty()) {
                o = clickType == ClickType.LEFT ? itemStack4.getCount() : 1;
                this.setCursorStack(slot.insertStack(itemStack4, o));
            }
            
            slot.markDirty();
            return;
        }
        
        if (!slot.canTakeItems(player)) {
            slot.markDirty();
            return;
        }

        if (itemStack4.isEmpty()) {
            o = clickType == ClickType.LEFT ? itemStack.getCount() : (itemStack.getCount() + 1) / 2;
            Optional<ItemStack> optional = slot.tryTakeStackRange(o, Integer.MAX_VALUE, player);
            optional.ifPresent((stack) -> {
                this.setCursorStack(stack);
                slot.onTakeItem(player, stack);
            });
            
            slot.markDirty();
            return;
        }
        
        if (slot.canInsert(itemStack4)) {
            if (ItemStack.areItemsAndComponentsEqual(itemStack, itemStack4)) {
                o = clickType == ClickType.LEFT ? itemStack4.getCount() : 1;
                this.setCursorStack(slot.insertStack(itemStack4, o));
            } else if (itemStack4.getCount() <= slot.getMaxItemCount(itemStack4)) {
                this.setCursorStack(itemStack);
                slot.setStack(itemStack4);
            }
            
            slot.markDirty();
            return;
        }
        
        if (ItemStack.areItemsAndComponentsEqual(itemStack, itemStack4)) {
            Optional<ItemStack> optional2 = slot.tryTakeStackRange(itemStack.getCount(), itemStack4.getMaxCount() - itemStack4.getCount(), player);
            optional2.ifPresent((stack) -> {
                itemStack4.increment(stack.getCount());
                slot.onTakeItem(player, stack);
            });
            
            slot.markDirty();
            return;
        }

        slot.markDirty();
    }
    
    private void swapStacks(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        VanillaEMC.LOGGER.info("SLOT CLICKED: " + slotIndex);
        PlayerInventory playerInventory = player.getInventory();
        Slot slot;
        ItemStack itemStack;
        int p;

        ItemStack itemStack5 = playerInventory.getStack(button);
        slot = (Slot)this.slots.get(slotIndex);
        itemStack = slot.getStack();
        if (itemStack5.isEmpty() && itemStack.isEmpty()) return;

        if (itemStack5.isEmpty()) {
            if (slot.canTakeItems(player)) {
                playerInventory.setStack(button, itemStack);
                // slot.onTake(itemStack.getCount());
                slot.setStack(ItemStack.EMPTY);
                slot.onTakeItem(player, itemStack);
            }
        } else if (itemStack.isEmpty()) {
            if (slot.canInsert(itemStack5)) {
                p = slot.getMaxItemCount(itemStack5);
                if (itemStack5.getCount() > p) {
                slot.setStack(itemStack5.split(p));
                } else {
                playerInventory.setStack(button, ItemStack.EMPTY);
                slot.setStack(itemStack5);
                }
            }
        } else if (slot.canTakeItems(player) && slot.canInsert(itemStack5)) {
            p = slot.getMaxItemCount(itemStack5);
            if (itemStack5.getCount() > p) {
                slot.setStack(itemStack5.split(p));
                slot.onTakeItem(player, itemStack);
                if (!playerInventory.insertStack(itemStack)) {
                player.dropItem(itemStack, true);
                }
            } else {
                playerInventory.setStack(button, itemStack);
                slot.setStack(itemStack5);
                slot.onTakeItem(player, itemStack);
            }
        }
    }

    private void cloneStack(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        ItemStack itemStack2;
        Slot slot3;

        slot3 = (Slot)this.slots.get(slotIndex);
        if (slot3.hasStack()) {
            itemStack2 = slot3.getStack();
            this.setCursorStack(itemStack2.copyWithCount(itemStack2.getMaxCount()));
        }
    }

    private void throwStack(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        ItemStack itemStack;
        int j;
        Slot slot3;

        slot3 = (Slot)this.slots.get(slotIndex);
        j = button == 0 ? 1 : slot3.getStack().getCount();
        itemStack = slot3.takeStackRange(j, Integer.MAX_VALUE, player);
        player.dropItem(itemStack, true);
    }

    private void pickupStack(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        ItemStack itemStack2;
        int k;
        int o;
        int p;
        Slot slot3;

        slot3 = (Slot)this.slots.get(slotIndex);
        itemStack2 = this.getCursorStack();
        if (itemStack2.isEmpty() || (slot3.hasStack() && slot3.canTakeItems(player))) return;
        
        k = button == 0 ? 0 : this.slots.size() - 1;
        p = button == 0 ? 1 : -1;

        for(o = 0; o < 2; ++o) {
            for(int q = k; q >= 0 && q < this.slots.size() && itemStack2.getCount() < itemStack2.getMaxCount(); q += p) {
            Slot slot4 = (Slot)this.slots.get(q);
            if (slot4.hasStack() && canInsertItemIntoSlot(slot4, itemStack2, true) && slot4.canTakeItems(player) && this.canInsertIntoSlot(itemStack2, slot4)) {
                ItemStack itemStack6 = slot4.getStack();
                if (o != 0 || itemStack6.getCount() != itemStack6.getMaxCount()) {
                    ItemStack itemStack7 = slot4.takeStackRange(itemStack6.getCount(), itemStack2.getMaxCount() - itemStack2.getCount(), player);
                    itemStack2.increment(itemStack7.getCount());
                }
            }
            }
        }
    }

    private boolean handleSlotClick(PlayerEntity player, ClickType clickType, Slot slot, ItemStack stack, ItemStack cursorStack) {
        FeatureSet featureSet = player.getWorld().getEnabledFeatures();
        if (cursorStack.isItemEnabled(featureSet) && cursorStack.onStackClicked(slot, clickType, player)) {
            return true;
        } else {
            return stack.isItemEnabled(featureSet) && stack.onClicked(cursorStack, slot, clickType, player, this.getCursorStackReference());
        }
    }

    private StackReference getCursorStackReference() {
        return StackReference.EMPTY;
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
