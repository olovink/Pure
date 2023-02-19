package cn.nukkit.level.generator.biome;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SmallMountainsBiome extends MountainsBiome {

    public SmallMountainsBiome() {
        super();

        this.setBaseHeight(0.1f);
        this.setHeightVariation(0.8f);

    }

    @Override
    public String getName() {
        return "Small Mountains";
    }
}
