package net.vassbo.vanillaemc.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

// , ExtendedScreenHandlerFactory
public class MagicBlockEntity extends BlockEntity implements BlockEntityProvider {
    public MagicBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.MAGIC_BLOCK_ENTITY, pos, state);
	}

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    

//    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
//       if (world.isClient) {
//          return ActionResult.SUCCESS;
//       } else {
//          NamedScreenHandlerFactory namedScreenHandlerFactory = this.createScreenHandlerFactory(state, world, pos);
//          if (namedScreenHandlerFactory != null) {
//             player.openHandledScreen(namedScreenHandlerFactory);
//             player.incrementStat(this.getOpenStat());
//             PiglinBrain.onGuardedBlockInteracted(player, true);
//          }

//          return ActionResult.CONSUME;
//       }
//    }

    // @Override
    // public NamedScreenHandlerFactory.getDisplayName(String string) {
    //     return string;
    // }

    // @Override
    // public ScreenHandlerFactory.createMenu() {

    // }

    // @Override
    // public ExtendedScreenHandlerFactory.getScreenOpeningData() {

    // }


//     The type MagicBlockEntity must implement the inherited abstract method NamedScreenHandlerFactory.getDisplayName()Java(67109264)
// The type MagicBlockEntity must implement the inherited abstract method ScreenHandlerFactory.createMenu(int, PlayerInventory, PlayerEntity)Java(67109264)
// The type MagicBlockEntity must implement the inherited abstract method ExtendedScreenHandlerFactory.getScreenOpeningData(ServerPlayerEntity)
}
