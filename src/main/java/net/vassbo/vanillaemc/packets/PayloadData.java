package net.vassbo.vanillaemc.packets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.vassbo.vanillaemc.data.PlayerData;
 
public record PayloadData(int emc) {
    public static final PacketCodec<ByteBuf, PayloadData> PACKET_CODEC = PacketCodec.tuple(
        // Identifier.PACKET_CODEC, PayloadData::identifier,
        PacketCodecs.INTEGER, PayloadData::emc,
        PayloadData::new
    );

    public static List<PayloadData> create(PlayerData playerData) {
        final List<PayloadData> data = listPlayerData(new ArrayList<>(), playerData);
        return Collections.unmodifiableList(data);
    }

    private static List<PayloadData> listPlayerData(List<PayloadData> data, PlayerData playerData) {
        data.add(new PayloadData(playerData.EMC));

        return data;
    }
}