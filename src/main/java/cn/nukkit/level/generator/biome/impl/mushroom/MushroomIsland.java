package cn.nukkit.level.generator.biome.impl.mushroom;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDirt;
import cn.nukkit.block.BlockMycelium;
import cn.nukkit.level.generator.biome.type.CaveBiome;
import cn.nukkit.level.generator.biome.type.NormalBiome;
import cn.nukkit.level.generator.populator.MushroomPopulator;

public class MushroomIsland extends NormalBiome implements CaveBiome {

    public MushroomIsland() {
        this.setGroundCover(new Block[]{new BlockMycelium(), new BlockDirt(), new BlockDirt(), new BlockDirt(), new BlockDirt()});

        MushroomPopulator mushroomPopulator = new MushroomPopulator();
        mushroomPopulator.setBaseAmount(1);

        addPopulator(mushroomPopulator);
        this.setBaseHeight(0.2f);
        this.setHeightVariation(0.3f);
        temperature = 0.9f;
        rainfall = 1.0f;
    }

    @Override
    public String getName() {
        return "Mushroom Island";
    }

    @Override
    public int getSurfaceBlock() {
        return Block.MYCELIUM;
    }

    @Override
    public int getGroundBlock() {
        return Block.DIRT;
    }

    @Override
    public int getStoneBlock() {
        return Block.STONE;
    }

}
