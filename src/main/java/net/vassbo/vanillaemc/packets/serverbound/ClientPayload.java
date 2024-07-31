package net.vassbo.vanillaemc.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;

public record ClientPayload(String messageId, String data) implements CustomPayload {
	public static final CustomPayload.Id<ClientPayload> ID = new CustomPayload.Id<>(Identifier.of(VanillaEMC.MOD_ID, "msg_to_server_payload"));
	public static final PacketCodec<RegistryByteBuf, ClientPayload> CODEC = PacketCodec.tuple(
		PacketCodecs.STRING, ClientPayload::messageId,
		PacketCodecs.STRING, ClientPayload::data,
		ClientPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
