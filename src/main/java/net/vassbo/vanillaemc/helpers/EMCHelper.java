package net.vassbo.vanillaemc.helpers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.EMCValues;
import net.vassbo.vanillaemc.data.PlayerData;
import net.vassbo.vanillaemc.data.StateSaverAndLoader;
import net.vassbo.vanillaemc.packets.PayloadData;
import net.vassbo.vanillaemc.packets.SyncHandler;
import net.vassbo.vanillaemc.packets.SyncHandler.SyncPayload;
import net.vassbo.vanillaemc.screen.MagicScreenHandler;

public class EMCHelper {
    public static void addEMCValue(PlayerEntity player, int addedValue) {
        int currentValue = getEMCValue(player);
        int newValue = currentValue += addedValue;
        
        setEMCValue(player, newValue);
    }

    public static boolean removeEMCValue(PlayerEntity player, int removedValue) {
        int currentValue = getEMCValue(player);
        int newValue = currentValue -= removedValue;

        if (newValue < 0) return false;

        setEMCValue(player, newValue);
        return true;
    }

    public static int getEMCValue(PlayerEntity player) {
        return StateSaverAndLoader.getPlayerState(player).EMC;
    }

    public static void setEMCValue(PlayerEntity player, int value) {
        StateSaverAndLoader.setPlayerEMC(player, value);
    }

    // CHECK

    public enum Action {
        GET, ADD;
    }

    private static boolean checkValidEMC(int emc, String id, Action action) {
        if (emc == 0) {
            VanillaEMC.LOGGER.info("Tried to " + action + " item, but it does not have any EMC value. ID: " + id);
            return false;
        }

        return true;
    }

    // GET

    public static boolean getItem(PlayerEntity player, ItemStack itemStack, MagicScreenHandler handler) {
        return getItem(player, itemStack, handler, false);
    }

    public static boolean getItem(PlayerEntity player, ItemStack itemStack, MagicScreenHandler handler, boolean fullStack) {
        String itemId = itemStack.getItem().toString();
        int stackCount = itemStack.getCount();

        int emcValue = EMCValues.get(itemId) * stackCount;

        if (!checkValidEMC(emcValue, itemId, Action.GET)) return false;

        if (!EMCHelper.removeEMCValue(player, emcValue)) {
            player.sendMessage(Text.translatable("emc.action.not_enough"));
            return false;
        }

        // refresh block inv content
        new Thread(() -> {
            // let content update before updating!
            wait(10);
            handler.refresh();
        }).start();

        return true;
    }

    public static boolean addItem(ItemStack itemStack) {
        String itemId = itemStack.getItem().toString();
        int emcValue = EMCValues.get(itemId);

        return checkValidEMC(emcValue, itemId, Action.ADD);
    }

    // ADD

    public static boolean addItem(ItemStack itemStack, PlayerEntity player, MagicScreenHandler handler) {
        String itemId = itemStack.getItem().toString();
        int emcValue = EMCValues.get(itemId);

        if (!checkValidEMC(emcValue, itemId, Action.ADD)) return false;

        // calculated new EMC (from MagicInventoryInput)
        int itemCount = itemStack.getCount();
        int addedEmcValue = emcValue * itemCount;

        learnItem(player, itemId);

        EMCHelper.addEMCValue(player, addedEmcValue);

        // refresh block inv content
        new Thread(() -> {
            // let content update before updating!
            wait(10);
            handler.refresh();
        }).start();

        return true;
    }

    // LEARN

    public static void learnItem(PlayerEntity player, String itemId) {
        List<String> learnedList = StateSaverAndLoader.getPlayerState(player).LEARNED_ITEMS;
        if (learnedList.contains(itemId)) return;

        // WIP show message in gui about new item!!
        Item item = ItemHelper.getById(itemId);
        player.sendMessage(Text.literal("Learned new item: " + ItemHelper.getName(item)));

        learnedList.add(itemId);
        StateSaverAndLoader.setPlayerLearned(player, learnedList);
    }

    public static void forgetItem(PlayerEntity player, String itemId) {
        List<String> learnedList = StateSaverAndLoader.getPlayerState(player).LEARNED_ITEMS;
        if (!learnedList.contains(itemId)) return;

        learnedList.remove(itemId);
        StateSaverAndLoader.setPlayerLearned(player, learnedList);
    }

    public static void learnAllItems(PlayerEntity player) {
        List<String> learnedList = new ArrayList<>();

        for (String key : EMCValues.getList()) {
            learnedList.add(key);
        }
        
        StateSaverAndLoader.setPlayerLearned(player, learnedList);
    }

    public static void forgetAllItems(PlayerEntity player) {
        StateSaverAndLoader.setPlayerLearned(player, new ArrayList<>());
    }

    // SEND

    public static void sendStateToClient(PlayerEntity player) {
        PlayerData playerState = StateSaverAndLoader.getPlayerState(player);

        MinecraftServer server = player.getServer();
        ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
        if (playerEntity == null || !(player instanceof PlayerEntity)) return;

        List<PayloadData> dataToSend = PayloadData.create(playerState);
        SyncPayload payload = new SyncPayload(dataToSend);

        server.execute(() -> {
            SyncHandler.send(playerEntity, payload);
        });
    }

    // global data
    // StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());

    // HELPERS

    private static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
