package net.vassbo.vanillaemc.packets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;

public class SyncHandler {
	private static String payloadId = "sync_payload";
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncHandler.class);

	// private SyncHandler() {}

	public static void init() {
        VanillaEMC.LOGGER.info("Registering packet payload.");
		PayloadTypeRegistry.playS2C().register(SyncPayload.ID, SyncPayload.PACKET_CODEC);
	}

	// called on join
	public static void send(ServerPlayerEntity player, SyncPayload payload, ServerPlayNetworkHandler handler) {
		if (ServerPlayNetworking.canSend(player, payload.getId())) {
			ServerPlayNetworking.send(player, payload);
		} else {
			LOGGER.error("Client cannot receive packet. This probably means that VanillaEMC is not installed on the client.");
			handler.disconnect(Text.literal("Please install the VanillaEMC mod to play on this server."));
		}
	}

	public static void send(ServerPlayerEntity player, SyncPayload payload) {
		ServerPlayNetworking.send(player, payload);
	}

	public record SyncPayload(List<PayloadData> value) implements CustomPayload {
		public static final CustomPayload.Id<SyncPayload> ID = new CustomPayload.Id<>(Identifier.of(VanillaEMC.MOD_ID, payloadId));
		public static final PacketCodec<PacketByteBuf, SyncPayload> PACKET_CODEC = PacketCodec.tuple(
			PayloadData.PACKET_CODEC.collect(PacketCodecs.toList()),
            SyncPayload::value,
            SyncPayload::new
		);

		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}
}