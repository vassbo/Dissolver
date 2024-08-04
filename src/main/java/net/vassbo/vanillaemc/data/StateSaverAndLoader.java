package net.vassbo.vanillaemc.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.helpers.EMCHelper;

// https://fabricmc.net/wiki/tutorial:persistent_states#player_specific_persistent_state
public class StateSaverAndLoader extends PersistentState {
    // public Integer globalData = 0;
    public HashMap<UUID, PlayerData> players = new HashMap<>();

    private static NbtCompound storeData(NbtCompound playerNbt, PlayerData playerData) {
        if (playerData.NAME != "") playerNbt.putString("NAME", playerData.NAME);
        playerNbt.putInt("EMC", playerData.EMC);
        playerNbt = storeList(playerNbt, "LEARNED_ITEMS", playerData.LEARNED_ITEMS);

        return playerNbt;
    }

    private static PlayerData getData(NbtCompound playerNbt, PlayerData playerData) {
        playerData.NAME = playerNbt.getString("NAME");
        playerData.EMC = playerNbt.getInt("EMC");
        playerData.LEARNED_ITEMS = getList(playerNbt, "LEARNED_ITEMS");

        return playerData;
    }

    // STRING LISTS

    private static NbtCompound storeList(NbtCompound playerNbt, String key, List<String> list) {
        int listLength = list.size();
        playerNbt.putInt(key + "_SIZE", listLength);

        int index = -1;
        for (String value : list) {
            index++;
            playerNbt.putString(key + ":" + index, value);
        };

        return playerNbt;
    }

    private static List<String> getList(NbtCompound playerNbt, String key) {
        int listLength = playerNbt.getInt(key + "_SIZE");
        List<String> list = new ArrayList<>();

        for (int i = 0; i < listLength; i++) {
            list.add(playerNbt.getString(key + ":" + i));
        }

        return list;
    }

    // STORE DATA

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        // nbt.putInt("globalData", globalData);

        nbt = storePlayersData(nbt, StateSaverAndLoader::storeData);

        return nbt;
    }

    private interface StoreDataInterface {
        NbtCompound store(NbtCompound playerNbt, PlayerData playerData);
    }

    private NbtCompound storePlayersData(NbtCompound nbt, StoreDataInterface func) {
        NbtCompound playersNbt = new NbtCompound();

        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
            playerNbt = func.store(playerNbt, playerData);
            playersNbt.put(uuid.toString(), playerNbt);
        });

        nbt.put("players", playersNbt);
        return nbt;
    }

    // GET DATA

    public static StateSaverAndLoader createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        // state.globalData = nbt.getInt("globalData");

        state = getPlayersData(nbt, state, StateSaverAndLoader::getData);

        return state;
    }

    private interface GetDataInterface {
        PlayerData get(NbtCompound playerNbt, PlayerData playerData);
    }

    private static StateSaverAndLoader getPlayersData(NbtCompound nbt, StateSaverAndLoader state, GetDataInterface func) {
        NbtCompound playersNbt = nbt.getCompound("players");

        playersNbt.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();
            NbtCompound playerNbt = playersNbt.getCompound(key);

            playerData = func.get(playerNbt, playerData);

            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });

        return state;
    }

    // PLAYER MANAGER

    private static Type<StateSaverAndLoader> type = new Type<>(
            StateSaverAndLoader::new, // If there's no 'StateSaverAndLoader' yet create one
            StateSaverAndLoader::createFromNbt, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // The first time the following 'getOrCreate' function is called, it creates a brand new 'StateSaverAndLoader' and
        // stores it inside the 'PersistentStateManager'. The subsequent calls to 'getOrCreate' pass in the saved
        // 'StateSaverAndLoader' NBT on disk to our function 'StateSaverAndLoader::createFromNbt'.
        StateSaverAndLoader state = persistentStateManager.getOrCreate(type, VanillaEMC.MOD_ID);

        // If state is not marked dirty, when Minecraft closes, 'writeNbt' won't be called and therefore nothing will be saved.
        state.markDirty();

        return state;
    }

    public static PlayerData getPlayerState(LivingEntity player) {
        if (player.getServer() == null) return new PlayerData();

        // Either get the player by the uuid, or we don't have data for him yet, make a new player state
        PlayerData playerState = getPlayerState(player, getSaver(player));

        return playerState;
    }

    private static StateSaverAndLoader getSaver(LivingEntity player) {
        MinecraftServer server = player.getServer();
        return getServerState(server);
    }

    private static PlayerData getPlayerState(LivingEntity player, StateSaverAndLoader serverState) {
        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());
    }

    public static void setPlayerEMC(LivingEntity player, int emc) {
        if (player.getServer() == null) return;

        StateSaverAndLoader serverState = getSaver(player);
        PlayerData playerState = getPlayerState(player, serverState);
        
        // store player name
        if (playerState.NAME == "") {
            String playerName = player.getDisplayName().getString();
            playerState.NAME = playerName;
        }

        playerState.EMC = emc;
        
        serverState.players.put(player.getUuid(), playerState);
        EMCHelper.sendStateToClient((PlayerEntity)player);
    }

    public static void setPlayerLearned(LivingEntity player, List<String> learnedList) {
        if (player.getServer() == null) return;

        StateSaverAndLoader serverState = getSaver(player);
        PlayerData playerState = getPlayerState(player, serverState);

        playerState.LEARNED_ITEMS = learnedList;

        serverState.players.put(player.getUuid(), playerState);
        EMCHelper.sendStateToClient((PlayerEntity)player);
    }

    public static PlayerData getFromUuid(MinecraftServer server, UUID uuid) {
        StateSaverAndLoader serverState = getServerState(server);
        PlayerData playerState = serverState.players.getOrDefault(uuid, null);
        
        return playerState;
    }

    public static HashMap<String, PlayerData> getFullList(MinecraftServer server) {
        StateSaverAndLoader serverState = getServerState(server);
        HashMap<String, PlayerData> playersData = new HashMap<>();

        serverState.players.forEach((uuid, data) -> {
            playersData.put(data.NAME == "" ? uuid.toString() : data.NAME, data);
        });

        return playersData;
    }
}
