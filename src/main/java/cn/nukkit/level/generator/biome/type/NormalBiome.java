package cn.nukkit.level.generator.biome.type;

import cn.nukkit.level.generator.biome.Biome;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class NormalBiome extends Biome {
    @Override
    public int getColor() {
        return this.grassColor;
    }
}
