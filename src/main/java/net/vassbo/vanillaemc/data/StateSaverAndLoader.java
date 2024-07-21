package net.vassbo.vanillaemc.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.vassbo.vanillaemc.VanillaEMC;

// https://fabricmc.net/wiki/tutorial:persistent_states#player_specific_persistent_state
public class StateSaverAndLoader extends PersistentState {
    // public Integer globalData = 0;
    public HashMap<UUID, PlayerData> players = new HashMap<>();

    private static NbtCompound storeData(NbtCompound playerNbt, PlayerData playerData) {
        playerNbt.putInt("emc", playerData.emc);
        return playerNbt;
    }

    private static PlayerData getData(NbtCompound playerNbt, PlayerData playerData) {
        playerData.emc = playerNbt.getInt("emc");
        return playerData;
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
        StateSaverAndLoader serverState = getServerState(player.getWorld().getServer());

        // Either get the player by the uuid, or we don't have data for him yet, make a new player state
        PlayerData playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());

        return playerState;
    }

    public static PlayerData getFromUuid(MinecraftServer server, UUID uuid) {
        StateSaverAndLoader serverState = getServerState(server);
        PlayerData playerState = serverState.players.getOrDefault(uuid, null);
        
        return playerState;
    }

    public static Collection<PlayerData> getFullList(MinecraftServer server) {
        StateSaverAndLoader serverState = getServerState(server);
        Collection<PlayerData> dataList = serverState.players.values();

        return dataList;
    }
}
