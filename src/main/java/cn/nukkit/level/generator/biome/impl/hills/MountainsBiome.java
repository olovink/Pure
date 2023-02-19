package cn.nukkit.level.generator.biome.impl.hills;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.level.generator.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.PopulatorFlower;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorTallGrass;
import cn.nukkit.level.generator.populator.PopulatorTree;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MountainsBiome extends GrassyBiome {

    public MountainsBiome() {
        super();

        PopulatorTree tree = new PopulatorTree();
        tree.setBaseAmount(1);
        this.addPopulator(tree);

        PopulatorGrass grass = new PopulatorGrass();
        grass.setBaseAmount(30);
        this.addPopulator(grass);

        PopulatorTallGrass tallGrass = new PopulatorTallGrass();
        tallGrass.setBaseAmount(1);
        this.addPopulator(tallGrass);

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


        this.setBaseHeight(0.8f);
        this.setHeightVariation(0.3f);

        this.temperature = 0.4;
        this.rainfall = 0.5;
    }

    @Override
    public String getName() {
        return "Mountains";
    }
}
