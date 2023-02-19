package cn.nukkit.level.generator.biome.impl.savanna;

import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.generator.biome.type.GrassyBiome;
import cn.nukkit.level.generator.populator.PopulatorFlower;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorTallGrass;
import cn.nukkit.level.generator.populator.tree.SavannaTreePopulator;

public class SavannaBiome extends GrassyBiome {

    public SavannaBiome() {
        super();
        SavannaTreePopulator tree = new SavannaTreePopulator(BlockSapling.ACACIA);
        tree.setBaseAmount(1);
        PopulatorTallGrass tallGrass = new PopulatorTallGrass();
        tallGrass.setBaseAmount(20);

        PopulatorGrass grass = new PopulatorGrass();
        grass.setBaseAmount(20);

        PopulatorFlower flower = new PopulatorFlower();
        flower.setBaseAmount(4);

        this.addPopulator(tallGrass);
        this.addPopulator(grass);
        this.addPopulator(tree);
        this.addPopulator(flower);

        this.setBaseHeight(0.125f);
        this.setHeightVariation(0.05f);
        this.temperature = 1.2f;
        this.rainfall = 0.0f;
    }

    @Override
    public String getName() {
        return "Savanna";
    }
}
