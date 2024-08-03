package net.vassbo.vanillaemc.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.vassbo.vanillaemc.helpers.EMCHelper;

public class DissolverBlockItem extends BlockItem {
    private static String TOOLTIP_TEXT = "item_tooltip.vanillaemc.dissolver_block_item";
    private static Formatting TOOLTIP_FORMAT = Formatting.GOLD;

    public DissolverBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(TOOLTIP_TEXT).formatted(TOOLTIP_FORMAT));

        // add emc value
        String itemId = stack.getItem().toString(); // vanillaemc:magic_item
        Text formattedText = EMCHelper.tooltipValue(itemId);
        if (!"".equals(formattedText.getLiteralString())) tooltip.add(formattedText);
    }
    
    @Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
}
