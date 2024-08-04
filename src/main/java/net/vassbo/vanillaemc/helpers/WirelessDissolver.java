package net.vassbo.vanillaemc.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.vassbo.vanillaemc.block.ModBlocks;
import net.vassbo.vanillaemc.block.entity.DissolverBlockEntity;

public class WirelessDissolver {
    public static int radius = 40;

    public static boolean open(PlayerEntity player, World world) {
        Vec3d playerPos = player.getPos();
        BlockPos startPos = new BlockPos((int) playerPos.x - radius, (int) playerPos.y - radius, (int) playerPos.z - radius);
        BlockPos endPos = new BlockPos((int) playerPos.x + radius, (int) playerPos.y + radius, (int) playerPos.z + radius);

        BlockPos foundPos = findBlockAtPos(world, startPos, endPos);
        if (foundPos == null)  return false;

        BlockEntity blockEntity = world.getBlockEntity(foundPos);
        if (!(blockEntity instanceof DissolverBlockEntity)) return false;

        player.openHandledScreen((DissolverBlockEntity)blockEntity);
        return true;
    }

    private static BlockPos findBlockAtPos(World world, BlockPos startPos, BlockPos endPos) {
        for (int x = startPos.getX(); x < endPos.getX(); x++) {
        for (int y = startPos.getY(); y < endPos.getY(); y++) {
        for (int z = startPos.getZ(); z < endPos.getZ(); z++) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = world.getBlockState(pos);

            if (state.isOf(ModBlocks.DISSOLVER_BLOCK)) return pos;
        }
        }
        }

        return null;
    }
}
