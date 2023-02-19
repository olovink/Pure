package cn.nukkit.level.generator.biome.impl.plains;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.level.generator.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.*;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class PlainBiome extends GrassyBiome {

    public PlainBiome() {
        super();
        PopulatorPumpkin pumpkin = new PopulatorPumpkin();
        pumpkin.setBaseAmount(-2000);
        pumpkin.setRandomAmount(2015);
        this.addPopulator(pumpkin);

        PopulatorSugarcane sugarcane = new PopulatorSugarcane();
        sugarcane.setBaseAmount(6);
        PopulatorTallSugarcane tallSugarcane = new PopulatorTallSugarcane();
        tallSugarcane.setBaseAmount(60);
        PopulatorGrass grass = new PopulatorGrass();
        grass.setBaseAmount(40);
        PopulatorTallGrass tallGrass = new PopulatorTallGrass();
        tallGrass.setBaseAmount(7);
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

        this.addPopulator(sugarcane);
        this.addPopulator(tallSugarcane);
        this.addPopulator(grass);
        this.addPopulator(tallGrass);
        this.addPopulator(flower);


        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);

        this.temperature = 0.8;
        this.rainfall = 0.4;
    }

    @Override
    public String getName() {
        return "Plains";
    }
}
