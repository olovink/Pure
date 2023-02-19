package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.level.generator.populator.PopulatorDisk;
import cn.nukkit.level.generator.populator.PopulatorSugarcane;
import cn.nukkit.level.generator.populator.PopulatorTallSugarcane;

import java.util.Arrays;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class OceanBiome extends WateryBiome {

    public OceanBiome() {
        super();

        PopulatorDisk populatorDiskSand = new PopulatorDisk(1.0, Block.SAND, 2, 4, 2, Arrays.asList(Block.DIRT, Block.GRASS));
        this.addPopulator(populatorDiskSand);

        PopulatorSugarcane sugarcane = new PopulatorSugarcane();
        sugarcane.setBaseAmount(6);
        PopulatorTallSugarcane tallSugarcane = new PopulatorTallSugarcane();
        tallSugarcane.setBaseAmount(60);
        this.addPopulator(sugarcane);
        this.addPopulator(tallSugarcane);
        this.addPopulator(populatorDiskSand);
        this.setBaseHeight(-1f);
        this.setHeightVariation(0.1f);

        this.temperature = 0.5;
        this.rainfall = 0.5;

    }

    @Override
    public Block[] getGroundCover() {
        return super.getGroundCover();
    }

    @Override
    public String getName() {
        return "Ocean";
    }
}
