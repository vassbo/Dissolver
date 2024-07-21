package net.vassbo.vanillaemc.packets;

import java.util.List;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;

// WIP not in use!!
public final class EMCSyncHandler extends SyncHandler {
	private static String payloadId = "emc_payload";

	public record EMCSyncPayload(List<PayloadData> emcStat) implements CustomPayload {
		public static final CustomPayload.Id<EMCSyncPayload> ID = new CustomPayload.Id<>(Identifier.of(VanillaEMC.MOD_ID, payloadId));
		public static final PacketCodec<PacketByteBuf, EMCSyncPayload> PACKET_CODEC = PacketCodec.tuple(
			PayloadData.PACKET_CODEC.collect(PacketCodecs.toList()),
            EMCSyncPayload::emcStat,
            EMCSyncPayload::new
		);

		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}
}