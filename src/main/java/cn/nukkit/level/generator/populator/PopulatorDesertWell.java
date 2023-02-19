package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSlabStone;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

import static cn.nukkit.block.Block.*;

public class PopulatorDesertWell extends Populator
{


    private int randomAmount;
    private int baseAmount;

    public void setRandomAmount(int randomAmount) {
        this.randomAmount = randomAmount;
    }

    public void setBaseAmount(int baseAmount) {
        this.baseAmount = baseAmount;
    }

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        int amount = random.nextBoundedInt(this.randomAmount + 1) + this.baseAmount;

        int rand = (int) rn(1, 1000);

        if (rand == 10) {
            for (int i = 0; i < amount; ++i) {

                int x = (chunkX << 4) + random.nextBoundedInt(16);
                int z = (chunkZ << 4) + random.nextBoundedInt(16);
                int y = this.getHighestWorkableBlock(level, x, z);


                for (int dy = -1; dy <= 0; ++dy) {
                    for (int dx = -2; dx <= 2; ++dx) {
                        for (int dz = -2; dz <= 2; ++dz) {
                            level.setBlockIdAt(x + dx, y + dy, z + dz, SANDSTONE);
                        }
                    }
                }
                level.setBlockIdAt(x, y, z, WATER);
                level.setBlockIdAt(x - 1, y, z, WATER);
                level.setBlockIdAt(x + 1, y, z, WATER);
                level.setBlockIdAt(x, y, z - 1, WATER);
                level.setBlockIdAt(x, y, z + 1, WATER);
                for (int dx = -2; dx <= 2; ++dx) {
                    for (int dz = -2; dz <= 2; ++dz) {
                        if (dx == -2 || dx == 2 || dz == -2 || dz == 2) {
                            level.setBlockIdAt(x + dx, y + 1, z + dz, SANDSTONE);
                        }
                    }
                }
                level.setBlockIdAt(x + 2, y + 1, z, STONE_SLAB);
                level.setBlockDataAt(x + 2, y + 1, z, BlockSlabStone.SANDSTONE);
                level.setBlockIdAt(x - 2, y + 1, z, STONE_SLAB);
                level.setBlockDataAt(x - 2, y + 1, z, BlockSlabStone.SANDSTONE);
                level.setBlockIdAt(x, y + 1, z + 2, STONE_SLAB);
                level.setBlockDataAt(x, y + 1, z + 2, BlockSlabStone.SANDSTONE);
                level.setBlockIdAt(x, y + 1, z - 2, STONE_SLAB);
                level.setBlockDataAt(x, y + 1, z - 2, BlockSlabStone.SANDSTONE);
                for (int dx = -1; dx <= 1; ++dx) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        if (dx == 0 && dz == 0) {
                            level.setBlockIdAt(x + dx, y + 4, z + dz, SANDSTONE);
                        } else {
                            level.setBlockIdAt(x + dx, y + 4, z + dz, STONE_SLAB);
                            level.setBlockDataAt(x + dx, y + 4, z + dz, BlockSlabStone.SANDSTONE);
                        }
                    }
                }
                for (int dy = 1; dy <= 3; ++dy) {
                    level.setBlockIdAt(x - 1, y + dy, z - 1, SANDSTONE);
                    level.setBlockIdAt(x - 1, y + dy, z + 1, SANDSTONE);
                    level.setBlockIdAt(x + 1, y + dy, z - 1, SANDSTONE);
                    level.setBlockIdAt(x + 1, y + dy, z + 1, SANDSTONE);
                }
            }
        }
    }

    private int getHighestWorkableBlock(ChunkManager level, int x, int z) {
        int y;
        for (y = 127; y >= 0; --y) {
            int b = level.getBlockIdAt(x, y, z);
            if (b != Block.AIR && b != Block.LEAVES && b != Block.LEAVES2 && b != Block.SNOW_LAYER) {
                break;
            }
        }

        return y == 0 ? -1 : ++y;
    }

    public static double rn(int min, int max){
        return (Math.random() * ((max - min) + 1)) + min;
    }
}
