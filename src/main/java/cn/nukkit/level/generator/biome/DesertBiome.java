package cn.nukkit.level.generator.biome;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DesertBiome extends SandyBiome {
    public DesertBiome() {
        super();
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
