package net.vassbo.vanillaemc.item;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vassbo.vanillaemc.helpers.EMCHelper;
import net.vassbo.vanillaemc.helpers.WirelessDissolver;

public class CrystalFrameItem extends Item {
    private static String TOOLTIP_TEXT = "item_tooltip.vanillaemc.crystal_frame_item";
    private static Formatting TOOLTIP_FORMAT = Formatting.GOLD;

    public CrystalFrameItem(Settings settings) {
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
    
    @Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
    
    @Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && !WirelessDissolver.open(user, world)) {
            user.sendMessage(Text.translatable("wireless_open.fail", WirelessDissolver.radius), true);
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
	}
}
