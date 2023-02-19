package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

import java.util.Arrays;
import java.util.List;

public class PopulatorDisk extends Populator{

    private static final int STATE_STILL_WATER = Block.STILL_WATER;

    private final double probability;
    private final int sourceBlock;
    private final int radiusMin;
    private final int radiusMax;
    private final int radiusY;
    private final List<Integer> replaceBlocks;

    public PopulatorDisk(double probability, int sourceBlock, int radiusMin, int radiusMax, int radiusY, List<Integer> replaceBlocks) {
        this.probability = 1.0;
        this.sourceBlock = Block.GRAVEL;
        this.radiusMin = 2;
        this.radiusMax = 3;
        this.radiusY = 2;
        this.replaceBlocks = Arrays.asList(Block.DIRT, Block.GRASS);
    }

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        if (random.nextDouble() >= probability) {
            return;
        }

        FullChunk chunk = level.getChunk(chunkX, chunkZ);

        int sourceX = (chunkX << 4) + random.nextBoundedInt(16);
        int sourceZ = (chunkZ << 4) + random.nextBoundedInt(16);
        int sourceY = getHighestWorkableBlock(chunk, sourceX, sourceZ) - 1;
        if (sourceY < radiusY) {
            return;
        }

        if (level.getBlockIdAt(sourceX, sourceY + 1, sourceZ) != STATE_STILL_WATER) {
            return;
        }

        int radius = NukkitMath.randomRange(random, radiusMin, radiusMax);
        for (int x = sourceX - radius; x <= sourceX + radius; x++) {
            for (int z = sourceZ - radius; z <= sourceZ + radius; z++) {
                if ((x - sourceX) * (x - sourceX) + (z - sourceZ) * (z - sourceZ) <= radius * radius) {
                    for (int y = sourceY - radiusY; y <= sourceY + radiusY; y++) {
                        for (Integer replaceBlockState : replaceBlocks) {
                            if (level.getBlockIdAt(x, y, z) == (replaceBlockState)) {
                                level.setBlockIdAt(x, y, z, sourceBlock);
                            }
                        }
                    }
                }
            }
        }
    }

    private int getHighestWorkableBlock(FullChunk chunk, int x, int z) {
        int y;
        for (y = 127; y >= 0; y--) {
            int b = chunk.getBlockId(x, y, z);
            if (b == Block.AIR) {
                break;
            }
        }
        return y == 0 ? -1 : y;
    }
}
