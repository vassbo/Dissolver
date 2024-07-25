package net.vassbo.vanillaemc.block;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vassbo.vanillaemc.block.entity.MagicBlockEntity;

public class MagicBlock extends BlockWithEntity {
    public static final MapCodec<MagicBlock> CODEC = createCodec(MagicBlock::new);

    public MagicBlock(Settings settings) {
		super(settings);
	}

    @Override
    protected MapCodec<MagicBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        MagicBlockEntity magicBlockEntity = new MagicBlockEntity(pos, state);
        return magicBlockEntity;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) return;

        // GIVE ADVANCEMENT!
        placer.sendMessage(Text.literal("Well done!"));
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MagicBlockEntity) {
                player.openHandledScreen((MagicBlockEntity)blockEntity);
            }

            return ActionResult.CONSUME;
        }
    }
}
