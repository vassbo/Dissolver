package net.vassbo.vanillaemc.packets.clientbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;

public record PlayerDataPayload(int emc, int learnedItemsSize, String message) implements CustomPayload {
	public static final CustomPayload.Id<PlayerDataPayload> ID = new CustomPayload.Id<>(Identifier.of(VanillaEMC.MOD_ID, "playerdata_to_client_payload"));
	public static final PacketCodec<RegistryByteBuf, PlayerDataPayload> CODEC = PacketCodec.tuple(
		PacketCodecs.INTEGER, PlayerDataPayload::emc,
        PacketCodecs.INTEGER, PlayerDataPayload::learnedItemsSize,
        PacketCodecs.STRING, PlayerDataPayload::message,
		PlayerDataPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
