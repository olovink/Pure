package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.level.generator.populator.JungleFloorPopulator;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorFlower;
import cn.nukkit.level.generator.populator.tree.JungleBigTreePopulator;
import cn.nukkit.level.generator.populator.tree.JungleTreePopulator;

public class JungleBiome extends GrassyBiome {

    public JungleBiome() {
        super();
        
        JungleTreePopulator trees = new JungleTreePopulator();
        JungleBigTreePopulator bigTrees = new JungleBigTreePopulator();
        PopulatorGrass grass = new PopulatorGrass();
        PopulatorFlower flower = new PopulatorFlower();
        
        trees.setBaseAmount(10);
        bigTrees.setBaseAmount(6);
        grass.setBaseAmount(20);
        flower.setBaseAmount(10);
        
        flower.addType(Block.DANDELION, 0);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_POPPY);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_AZURE_BLUET);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_RED_TULIP);
        flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_OXEYE_DAISY);

        JungleFloorPopulator floor = new JungleFloorPopulator();
        floor.setBaseAmount(10);
        floor.setRandomAmount(5);
        this.addPopulator(floor);

        
        this.addPopulator(grass);
        this.addPopulator(bigTrees);
        this.addPopulator(trees);
        this.addPopulator(flower);

        this.setBaseHeight(0.45f);
        this.setHeightVariation(0.3f);
        this.temperature = 1.2f;
        this.rainfall = 0.9f;
    }

    @Override
    public String getName() {
        return "Jungle";
    }
}
