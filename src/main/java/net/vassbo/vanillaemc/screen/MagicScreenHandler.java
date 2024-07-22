package net.vassbo.vanillaemc.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.vassbo.vanillaemc.inventory.MagicInventory;

public class MagicScreenHandler extends ScreenHandler {
    private final MagicInventory inventory;

    // , BlockEntity blockEntity
    public MagicScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.MAGIC_SCREEN_HANDLER, syncId);
        // this.player = playerInventory.player;
        // this.inventory = new CraftingInventory(this, 3, 3);
        // this.addSlots(playerInventory);

        // checkSize(((Inventory) blockEntity), 2);
        // this.inventory = ((Inventory) blockEntity);
        // inventory.onOpen(playerInventory.player);
        // this.blockEntity = ((MagicBlockEntity) blockEntity);

        // this.addSlot(new Slot(inventory, 0, 80, 11));
        // this.addSlot(new Slot(inventory, 1, 80, 59));

        this.inventory = new MagicInventory(this, 3, 3);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        // addProperties(arrayPropertyDelegate);
        addSlots(inventory);
    }

    private void addSlots(MagicInventory inventory) {
        this.addSlot(new Slot(inventory, 0, 80, 11));
        this.addSlot(new Slot(inventory, 1, 80, 59));

        // int i;
        // int j;

        // for(i = 0; i < 3; ++i) {
        //     for(j = 0; j < 8; ++j) {
        //         this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        //     }
        // }

        // for(i = 0; i < 9; ++i) {
        //     this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        // }

        // this.addSlot(new CrafterOutputSlot(this.resultInventory, 0, 134, 35));
        // this.updateResult();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
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
