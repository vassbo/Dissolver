package net.vassbo.vanillaemc.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;

public class ModBlocks {
    public static final Block MAGIC_BLOCK = registerBlock("magic_block", new MagicBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final BlockEntityType<MagicBlockEntity> MAGIC_BLOCK_ENTITY = registerBlockEntity("magic_block_entity", MAGIC_BLOCK);

    // HELPERS

    private static Block registerBlock(String id, Block block) {
        registerBlockItem(id, block);
        return Registry.register(Registries.BLOCK, Identifier.of(VanillaEMC.MOD_ID, id), block);
    }

    private static BlockEntityType<MagicBlockEntity> registerBlockEntity(String id, Block block) {
        return Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(VanillaEMC.MOD_ID, id),
            BlockEntityType.Builder.create(MagicBlockEntity::new, block).build()
        );
    }

    private static Item registerBlockItem(String id, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(VanillaEMC.MOD_ID, id), new BlockItem(block, new Item.Settings()));
    }

    // INITIALIZE

    public static void registerBlocks() {
        // 
    }
}
