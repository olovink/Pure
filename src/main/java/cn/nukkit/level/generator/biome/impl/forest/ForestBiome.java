package cn.nukkit.level.generator.biome.impl.forest;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.generator.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.PopulatorFlower;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorTallGrass;
import cn.nukkit.level.generator.populator.PopulatorTree;

/**
 * author: MagicDroidX
 * Nukkit Project
 */

public class ForestBiome extends GrassyBiome {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_BIRCH = 1;

    public final int type;

    public ForestBiome() {
        this(TYPE_NORMAL);
    }

    public ForestBiome(int type) {
        super();

        this.type = type;

        PopulatorTree trees = new PopulatorTree(type == TYPE_BIRCH ? BlockSapling.BIRCH : BlockSapling.OAK);
        trees.setBaseAmount(5);
        this.addPopulator(trees);

        PopulatorGrass grass = new PopulatorGrass();
        grass.setBaseAmount(30);
        this.addPopulator(grass);

        PopulatorTallGrass tallGrass = new PopulatorTallGrass();
        tallGrass.setBaseAmount(3);

        this.addPopulator(tallGrass);

        this.setBaseHeight(0.45f);
        this.setHeightVariation(0.4f);

        if (type == TYPE_BIRCH) {
            this.temperature = 0.5;
            this.rainfall = 0.5;
        } else {
            this.temperature = 0.7;
            this.temperature = 0.8;
        }

        PopulatorFlower flower = new PopulatorFlower();
        flower.setBaseAmount(10);
        flower.addType(Block.DANDELION, 0);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_POPPY);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_AZURE_BLUET);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_RED_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_ORANGE_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_WHITE_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_PINK_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_OXEYE_DAISY);
        this.addPopulator(flower);
    }

    @Override
    public String getName() {
        return this.type == TYPE_BIRCH ? "Birch Forest" : "Forest";
    }
}
