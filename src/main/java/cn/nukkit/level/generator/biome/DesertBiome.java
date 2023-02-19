package cn.nukkit.level.generator.biome;

import cn.nukkit.level.generator.populator.PopulatorDesertWell;


/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DesertBiome extends SandyBiome {
    public DesertBiome() {
        super();

        PopulatorDesertWell dw = new PopulatorDesertWell();
        dw.setBaseAmount(1);
        this.addPopulator(dw);

        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);
        this.temperature = 2;
        this.rainfall = 0;
    }

    @Override
    public String getName() {
        return "Desert";
    }
}
