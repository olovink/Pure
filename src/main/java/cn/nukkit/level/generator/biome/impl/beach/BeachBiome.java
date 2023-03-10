package cn.nukkit.level.generator.biome.impl.beach;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSand;
import cn.nukkit.block.BlockSandstone;
import cn.nukkit.level.generator.biome.type.SandyBiome;

/**
 * Author: PeratX
 * Nukkit Project
 */
public class BeachBiome extends SandyBiome {
    public BeachBiome() {
        //Todo: SugarCane

        this.setBaseHeight(0f);
        this.setHeightVariation(0.025f);
        this.temperature = 2;
        this.rainfall = 0;

        this.setGroundCover(new Block[]{
                new BlockSand(),
                new BlockSand(),
                new BlockSandstone(),
                new BlockSandstone(),
                new BlockSandstone()
        });
    }

    @Override
    public String getName() {
        return "Beach";
    }
}
