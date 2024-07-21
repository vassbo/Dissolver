package net.vassbo.vanillaemc.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vassbo.vanillaemc.VanillaEMC;

public class MagicBlock extends Block {
    // private final ViewerCountManager stateManager = new 1(this);

    public MagicBlock(Settings settings) {
		super(settings);
	}

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        // GIVE ADVANCEMENT!
        // WIP this gived double messages!!
        VanillaEMC.LOGGER.info("PLACED!");
        placer.sendMessage(Text.literal("Well done!"));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        VanillaEMC.LOGGER.info("CLICKED!");
        player.sendMessage(Text.literal("Hello!"));
        
        // if (!this.removed && !player.isSpectator()) {
        //     this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        // }
        
        //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
        //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
        NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
        // PlayerInventory playerInventory = "test";
        // screenHandlerFactory.createMenu(0, playerInventory, player);

        player.openHandledScreen(screenHandlerFactory);

        return ActionResult.SUCCESS;
        // if (!player.getAbilities().allowModifyWorld) {
        //     // Skip if the player isn't allowed to modify the world.
        //     return ActionResult.PASS;
        // } else {
        //     // Get the current value of the "activated" property
        //     boolean activated = state.get(ACTIVATED);

        //     // Flip the value of activated and save the new blockstate.
        //     world.setBlockState(pos, state.with(ACTIVATED, !activated));

        //     // Play a click sound to emphasise the interaction.
        //     world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);

        //     return ActionResult.SUCCESS;
        // }
    }
}
