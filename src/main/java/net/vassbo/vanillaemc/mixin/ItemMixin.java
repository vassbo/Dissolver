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
import net.vassbo.vanillaemc.helpers.EMCHelper;
import net.vassbo.vanillaemc.helpers.ItemHelper;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(at = @At("RETURN"), method = "appendTooltip", cancellable = true)
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
        String itemId = stack.getItem().toString();

        // add emc value
        Text formattedText = EMCHelper.tooltipValue(itemId, ItemHelper.getDurabilityPercentage(stack));
        if (!"".equals(formattedText.getLiteralString())) tooltip.add(formattedText);
    }
}