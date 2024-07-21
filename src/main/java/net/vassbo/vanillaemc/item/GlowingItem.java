package net.vassbo.vanillaemc.item;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.vassbo.vanillaemc.data.EMCValues;

public class GlowingItem extends ExperienceBottleItem {
    private static String TOOLTIP_TEXT = "item_tooltip.vanillaemc.glowing_item";
    private static Formatting TOOLTIP_FORMAT = Formatting.GOLD;

    public GlowingItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.consume(null);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        ExperienceBottleEntity experienceBottleEntity = new ExperienceBottleEntity(world, pos.getX(), pos.getY(), pos.getZ());
        experienceBottleEntity.setItem(stack);
        return experienceBottleEntity;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(TOOLTIP_TEXT).formatted(TOOLTIP_FORMAT));

        // add emc value
        String itemId = stack.getItem().toString(); // vanillaemc:magic_item
        Text formattedText = EMCValues.tooltipValue(itemId);
        if (!"".equals(formattedText.getLiteralString())) tooltip.add(formattedText);
    }

    // @Override
    // public ProjectileItem.Settings getProjectileSettings() {
    //     // return Settings.builder().uncertainty(Settings.DEFAULT.uncertainty() * 0.5F).power(Settings.DEFAULT.power() * 1.25F).build();
    //     return ProjectileItem.getProjectileSettings();
    // }
}
