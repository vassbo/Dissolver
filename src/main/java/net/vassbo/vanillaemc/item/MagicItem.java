package net.vassbo.vanillaemc.item;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.vassbo.vanillaemc.helpers.EMCHelper;

public class MagicItem extends Item {
    private static String TOOLTIP_TEXT = "item_tooltip.vanillaemc.magic_item";
    private static Formatting TOOLTIP_FORMAT = Formatting.GOLD;

    public MagicItem(Settings settings) {
		super(settings);
	}

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(TOOLTIP_TEXT).formatted(TOOLTIP_FORMAT));

        // add emc value
        String itemId = stack.getItem().toString(); // vanillaemc:magic_item
        Text formattedText = EMCHelper.tooltipValue(itemId);
        if (!"".equals(formattedText.getLiteralString())) tooltip.add(formattedText);
    }
}
