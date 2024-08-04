package net.vassbo.vanillaemc.block;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.vassbo.vanillaemc.block.entity.DissolverBlockEntity;
import net.vassbo.vanillaemc.entity.CrystalEntity;
import net.vassbo.vanillaemc.entity.ModEntities;

public class DissolverBlock extends BlockWithEntity {
	public static final MapCodec<DissolverBlock> CODEC = createCodec(DissolverBlock::new);

    public DissolverBlock(Settings settings) {
		super(settings);
	}

    @Override
    protected MapCodec<DissolverBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        DissolverBlockEntity blockEntity = new DissolverBlockEntity(pos, state);
        return blockEntity;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        spawnEntity(world, pos);
    }
    
    @Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (world instanceof ServerWorld) {
            List<Entity> list = blockEntityList((World)world, pos);
            if (!list.isEmpty()) list.get(0).remove(Entity.RemovalReason.DISCARDED);
        }
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
            if (blockEntity instanceof DissolverBlockEntity) {
                player.openHandledScreen((DissolverBlockEntity)blockEntity);
            }

            // check for entity
            List<Entity> list = blockEntityList(world, pos);
            if (list.isEmpty()) spawnEntity(world, pos);

            return ActionResult.CONSUME;
        }
    }

    private void spawnEntity(World world, BlockPos pos) {
        if (!(world instanceof ServerWorld)) return;

        CrystalEntity crystalEntity = new CrystalEntity(ModEntities.CRYSTAL_ENTITY, world);
        crystalEntity.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        world.spawnEntity(crystalEntity);
    }

    private List<Entity> blockEntityList(World world, BlockPos pos) {
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        
        return world.getOtherEntities(null, new Box(x, y, z, x + 1.0F, y + 1.0F, z + 1.0F));
    }

    // PARTICLE
    private float offset = 1.1F;
    private float velocity = 0.01F;
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

        if (random.nextInt(12) != 0) return;

        int randomSide = random.nextInt(8);
        boolean northSide = randomSide == 0;
        boolean southSide = randomSide == 1;
        boolean eastSide = randomSide == 2;
        boolean westSide = randomSide == 3;
        boolean topSide = randomSide > 3;

		double x = eastSide ? offset : westSide ? 0 : Math.random();
		double y = topSide ? offset : Math.random();
		double z = southSide ? offset : northSide ? 0 : Math.random();
        
		world.addParticle(ParticleTypes.END_ROD, pos.getX() + x, pos.getY() + y, pos.getZ() + z, eastSide ? velocity : westSide ? -velocity : 0, topSide ? velocity : 0, southSide ? velocity : northSide ? -velocity : 0);
	}
}
