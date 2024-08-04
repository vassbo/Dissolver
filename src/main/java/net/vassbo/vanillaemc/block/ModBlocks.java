package net.vassbo.vanillaemc.block;

import java.util.function.ToIntFunction;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.block.entity.DissolverBlockEntity;
import net.vassbo.vanillaemc.item.DissolverBlockItem;

public class ModBlocks {
    // DISSOLVER
    private static final Block _DISSOLVER_BLOCK = new DissolverBlock(AbstractBlock.Settings.copy(Blocks.NETHERITE_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK).luminance(getLuminance(12)).nonOpaque());
    public static final DissolverBlockItem DISSOLVER_BLOCK_ITEM = new DissolverBlockItem(_DISSOLVER_BLOCK, new Item.Settings().rarity(Rarity.RARE));
    public static final Block DISSOLVER_BLOCK = registerBlock("dissolver_block", _DISSOLVER_BLOCK, DISSOLVER_BLOCK_ITEM);
    public static final BlockEntityType<DissolverBlockEntity> DISSOLVER_BLOCK_ENTITY = registerBlockEntity("dissolver_block_entity", DISSOLVER_BLOCK);
    
    // HELPERS
    
    private static Block registerBlock(String id, Block block, BlockItem blockItem) {
        registerBlockItem(id, blockItem);
        return Registry.register(Registries.BLOCK, Identifier.of(VanillaEMC.MOD_ID, id), block);
    }

    private static BlockEntityType<DissolverBlockEntity> registerBlockEntity(String id, Block block) {
        return Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(VanillaEMC.MOD_ID, id),
            BlockEntityType.Builder.create(DissolverBlockEntity::new, block).build()
        );
    }

    private static Item registerBlockItem(String id, BlockItem blockItem) {
        return Registry.register(Registries.ITEM, Identifier.of(VanillaEMC.MOD_ID, id), blockItem);
    }
    
    private static ToIntFunction<BlockState> getLuminance(int luminance) {
        return new ToIntFunction<BlockState>() {
            @Override
            public int applyAsInt(BlockState value) {
                return luminance;
            }
        };
    }

    // INITIALIZE

    public static void init() {
    }
}
