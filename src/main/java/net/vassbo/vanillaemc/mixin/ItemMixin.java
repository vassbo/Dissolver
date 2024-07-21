package net.vassbo.vanillaemc.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.vassbo.vanillaemc.data.EMCValues;

@Mixin(Item.class)
public class ItemMixin {
    // @Unique
    // private static final String customId = "stone";

	// @Inject(at = @At("HEAD"), method = "appendTooltip")
	// private void init(CallbackInfo info) {
	// 	// This code is injected into the start of MinecraftServer.loadWorld()V
	// }

    @Inject(at = @At("RETURN"), method = "appendTooltip", cancellable = true)
    // private void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
        String itemId = stack.getItem().toString();
        // VanillaEMC.LOGGER.info("ITEM ID: " + itemId);

        // add emc value
        Text formattedText = EMCValues.tooltipValue(itemId);
        if (!"".equals(formattedText.getLiteralString())) tooltip.add(formattedText);

        // if (stack.nbt != null) {
        //     VanillaEMC.LOGGER.info("NBT: " + nbt);
        //     if (nbt.contains(customId)) {
        //         String key = "nucleoplasm." + nbt.getString(customId);
        //         String varietyKey = "nucleoplasm.variety";
        //         MutableText variety = Language.getInstance().hasTranslation(varietyKey) ? Text.translatable(varietyKey) : Text.literal("variety");
        //         MutableText text = Language.getInstance().hasTranslation(key) ? Text.translatable(key) : Text.literal(key.replace("nucleoplasm.", ""));
        //         tooltip.add(variety.append(text));
        //     }
        // }
    }
}