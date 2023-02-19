package cn.nukkit.level.generator;

import cn.nukkit.block.*;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.level.generator.biome.BiomeSelector;
import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.level.generator.noise.vanilla.f.NoiseGeneratorOctavesF;
import cn.nukkit.level.generator.noise.vanilla.f.NoiseGeneratorPerlinF;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.*;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.*;

import static cn.nukkit.block.Block.STILL_WATER;
import static cn.nukkit.block.Block.STONE;

/**
 * This generator was written by Creeperface and Nycuro
 * <p>
 * The following classes are theirs and are intended for NUKKIT USAGE and should not be copied/translated to other software
 * such as BukkitPE, ClearSky, Genisys , Pocketmine-MP
 * <p>
 * Normal.java
 * MushroomPopulator.java
 * DarkOakTreePopulator.java
 * JungleBigTreePopulator.java
 * JungleTreePopulaotr.java
 * SavannaTreePopulator.java
 * SwampTreePopulator.java
 * BasicPopulator.java
 * MesaBiome.java
 * JungleBiome.java
 * SavannaBiome.java
 * RoofedForestBiome.java
 * RoofedForestMBiome.java
 * MushroomIsland.java
 * TreeGenerator.java
 * HugeTreesGenerator.java
 * <p>
 * Created by CreeperFace on 26. 10. 2016.
 */
public class Normal extends Generator {

    /**
     * biome IDs
     */
    public static final int JUNGLE = 21;
    public static final int SAVANNA = 35;
    public static final int ROOFED_FOREST = 29;
    public static final int ROOFED_FOREST_M = 157;
    public static final int MUSHROOM_ISLAND = 14;
    public static final int SWAMP = 6;

    public static final int OCEAN = 0;
    public static final int PLAINS = 1;
    public static final int DESERT = 2;
    public static final int FOREST = 4;
    public static final int TAIGA = 5;
    public static final int RIVER = 7;
    public static final int ICE_PLAINS = 12;
    public static final int BEACH = 16;
    public static final int BIRCH_FOREST = 27;

    public static final int MAX_BIOMES = 256;

    @Override
    public int getId() {
        return TYPE_INFINITE;
    }

    private final List<Populator> populators = new ArrayList<>();

    private ChunkManager level;

    private Random random;
    private NukkitRandom nukkitRandom;

    private long localSeed1;
    private long localSeed2;

    private final List<Populator> generationPopulators = new ArrayList<>();


    private BiomeSelector selector;
    private static final float[] biomeWeights = new float[25];

    static {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                biomeWeights[i + 2 + (j + 2) * 5] = (float) (10.0F / Math.sqrt((float) (i * i + j * j) + 0.2F));
            }
        }
    }

    private final ThreadLocal<Biome[]> biomes = ThreadLocal.withInitial(() -> new Biome[10 * 10]);
    private final ThreadLocal<float[]> depthRegion = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<float[]> mainNoiseRegion = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<float[]> minLimitRegion = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<float[]> maxLimitRegion = ThreadLocal.withInitial(() -> null);
    private final ThreadLocal<float[]> heightMap = ThreadLocal.withInitial(() -> new float[825]);
    private NoiseGeneratorOctavesF minLimitPerlinNoise;
    private NoiseGeneratorOctavesF maxLimitPerlinNoise;
    private NoiseGeneratorOctavesF mainPerlinNoise;
    private NoiseGeneratorPerlinF surfaceNoise;
    public NoiseGeneratorOctavesF scaleNoise;
    public NoiseGeneratorOctavesF depthNoise;


    private final int seaHeight = 64;

    protected float rainfall = 0.5F;
    protected float temperature = 0.5F;
    public Normal() {
        this(new HashMap<>());
    }

    public Normal(Map<String, Object> options) {
        //Nothing here. Just used for future update.
    }

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public String getName() {
        return "normal";
    }

    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<>();
    }

    public Biome pickBiome(int x, int z) {
        long hash = x * 2345803 ^ z * 9236449 ^ this.level.getSeed();
        hash *= hash + 223;

        long xNoise = hash >> 20 & 3;
        long zNoise = hash >> 22 & 3;

        if (xNoise == 3) {
            xNoise = 1;
        }
        if (zNoise == 3) {
            zNoise = 1;
        }

        return this.selector.pickBiome(x + xNoise - 1, z + zNoise - 1);
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.random = new Random();
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.selector = new BiomeSelector(this.nukkitRandom, Biome.getBiome(Biome.FOREST));
        this.minLimitPerlinNoise = new NoiseGeneratorOctavesF(random, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctavesF(random, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctavesF(random, 8);
        this.surfaceNoise = new NoiseGeneratorPerlinF(random, 4);
        this.scaleNoise = new NoiseGeneratorOctavesF(random, 10);
        this.depthNoise = new NoiseGeneratorOctavesF(random, 16);
        this.selector.addBiome(Biome.getBiome(OCEAN));
        this.selector.addBiome(Biome.getBiome(PLAINS));
        this.selector.addBiome(Biome.getBiome(DESERT));
        this.selector.addBiome(Biome.getBiome(FOREST));
        this.selector.addBiome(Biome.getBiome(TAIGA));
        this.selector.addBiome(Biome.getBiome(RIVER));
        this.selector.addBiome(Biome.getBiome(ICE_PLAINS));
        this.selector.addBiome(Biome.getBiome(BIRCH_FOREST));

        this.selector.addBiome(Biome.getBiome(JUNGLE));
        this.selector.addBiome(Biome.getBiome(SAVANNA));
        this.selector.addBiome(Biome.getBiome(ROOFED_FOREST));
        this.selector.addBiome(Biome.getBiome(ROOFED_FOREST_M));
        this.selector.addBiome(Biome.getBiome(MUSHROOM_ISLAND));
        this.selector.addBiome(Biome.getBiome(SWAMP));

        this.selector.recalculate();


        PopulatorCaves caves = new PopulatorCaves();
        this.populators.add(caves);

        PopulatorRavines ravines = new PopulatorRavines();
        this.populators.add(ravines);

//        PopulatorDungeon dungeons = new PopulatorDungeon();
//        this.populators.add(dungeons);

        PopulatorGroundCover cover = new PopulatorGroundCover();
        this.generationPopulators.add(cover);

        PopulatorOre ores = new PopulatorOre();
        ores.setOreTypes(new OreType[]{
                new OreType(new BlockOreCoal(), 20, 17, 0, 128),
                new OreType(new BlockOreIron(), 20, 9, 0, 64),
                new OreType(new BlockOreRedstone(), 8, 8, 0, 16),
                new OreType(new BlockOreLapis(), 1, 7, 0, 16),
                new OreType(new BlockOreGold(), 2, 9, 0, 32),
                new OreType(new BlockOreDiamond(), 1, 8, 0, 16),
                new OreType(new BlockDirt(), 10, 33, 0, 128),
                new OreType(new BlockGravel(), 8, 33, 0, 128),
                new OreType(new BlockStone(BlockStone.GRANITE), 10, 33, 0, 80),
                new OreType(new BlockStone(BlockStone.DIORITE), 10, 33, 0, 80),
                new OreType(new BlockStone(BlockStone.ANDESITE), 10, 33, 0, 80)
        });
        this.populators.add(ores);
    }

    @Override
    public void generateChunk(final int chunkX, final int chunkZ) {
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;
        this.nukkitRandom.setSeed(chunkX * this.localSeed1 ^ chunkZ * this.localSeed2 ^ this.level.getSeed());

        BaseFullChunk chunk = this.level.getChunk(chunkX, chunkZ);

        //generate base noise values
        float[] depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion.get(), chunkX * 4, chunkZ * 4, 5, 5, 200f, 200f, 0.5f);
        this.depthRegion.set(depthRegion);
        float[] mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion.get(), chunkX * 4, 0, chunkZ * 4, 5, 33, 5, 684.412f / 60f, 684.412f / 160f, 684.412f / 60f);
        this.mainNoiseRegion.set(mainNoiseRegion);
        float[] minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion.get(), chunkX * 4, 0, chunkZ * 4, 5, 33, 5, 684.412f, 684.412f, 684.412f);
        this.minLimitRegion.set(minLimitRegion);
        float[] maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion.get(), chunkX * 4, 0, chunkZ * 4, 5, 33, 5, 684.412f, 684.412f, 684.412f);
        this.maxLimitRegion.set(maxLimitRegion);
        float[] heightMap = this.heightMap.get();

        //generate heightmap and smooth biome heights
        int horizCounter = 0;
        int vertCounter = 0;
        for (int xSeg = 0; xSeg < 5; ++xSeg) {
            for (int zSeg = 0; zSeg < 5; ++zSeg) {
                float heightVariationSum = 0.0F;
                float baseHeightSum = 0.0F;
                float biomeWeightSum = 0.0F;
                Biome biome = this.pickBiome(baseX + (xSeg * 4), baseZ + (zSeg * 4));

                for (int xSmooth = -2; xSmooth <= 2; ++xSmooth) {
                    for (int zSmooth = -2; zSmooth <= 2; ++zSmooth) {
                        Biome biome1 = this.pickBiome(baseX + (xSeg * 4) + xSmooth, baseZ + (zSeg * 4) + zSmooth);
                        float baseHeight = biome1.getBaseHeight();
                        float heightVariation = biome1.getHeightVariation();

                        float scaledWeight = biomeWeights[xSmooth + 2 + (zSmooth + 2) * 5] / (baseHeight + 2.0F);

                        if (biome1.getBaseHeight() > biome.getBaseHeight()) {
                            scaledWeight /= 2.0F;
                        }

                        heightVariationSum += heightVariation * scaledWeight;
                        baseHeightSum += baseHeight * scaledWeight;
                        biomeWeightSum += scaledWeight;
                    }
                }

                heightVariationSum = heightVariationSum / biomeWeightSum;
                baseHeightSum = baseHeightSum / biomeWeightSum;
                heightVariationSum = heightVariationSum * 0.9F + 0.1F;
                baseHeightSum = (baseHeightSum * 4.0F - 1.0F) / 8.0F;
                float depthNoise = depthRegion[vertCounter] / 8000.0f;

                if (depthNoise < 0.0f) {
                    depthNoise = -depthNoise * 0.3f;
                }

                depthNoise = depthNoise * 3.0f - 2.0f;

                if (depthNoise < 0.0f) {
                    depthNoise = depthNoise / 2.0f;

                    if (depthNoise < -1.0f) {
                        depthNoise = -1.0f;
                    }

                    depthNoise = depthNoise / 1.4f;
                    depthNoise = depthNoise / 2.0f;
                } else {
                    if (depthNoise > 1.0f) {
                        depthNoise = 1.0f;
                    }

                    depthNoise = depthNoise / 8.0f;
                }

                ++vertCounter;
                float baseHeightClone = baseHeightSum;
                float heightVariationClone = heightVariationSum;
                baseHeightClone = baseHeightClone + depthNoise * 0.2f;
                baseHeightClone = baseHeightClone * 8.5f / 8.0f;
                float baseHeightFactor = 8.5f + baseHeightClone * 4.0f;

                for (int ySeg = 0; ySeg < 33; ++ySeg) {
                    float baseScale = ((float) ySeg - baseHeightFactor) * 12f * 128.0f / 256.0f / heightVariationClone;

                    if (baseScale < 0.0f) {
                        baseScale *= 4.0f;
                    }

                    float minScaled = minLimitRegion[horizCounter] / 512f;
                    float maxScaled = maxLimitRegion[horizCounter] / 512f;
                    float noiseScaled = (mainNoiseRegion[horizCounter] / 10.0f + 1.0f) / 2.0f;
                    float clamp = MathHelper.denormalizeClamp(minScaled, maxScaled, noiseScaled) - baseScale;

                    if (ySeg > 29) {
                        float yScaled = ((float) (ySeg - 29) / 3.0F);
                        clamp = clamp * (1.0f - yScaled) + -10.0f * yScaled;
                    }

                    heightMap[horizCounter] = clamp;
                    ++horizCounter;
                }
            }
        }

        //place blocks
        for (int xSeg = 0; xSeg < 4; ++xSeg) {
            int xScale = xSeg * 5;
            int xScaleEnd = (xSeg + 1) * 5;

            for (int zSeg = 0; zSeg < 4; ++zSeg) {
                int zScale1 = (xScale + zSeg) * 33;
                int zScaleEnd1 = (xScale + zSeg + 1) * 33;
                int zScale2 = (xScaleEnd + zSeg) * 33;
                int zScaleEnd2 = (xScaleEnd + zSeg + 1) * 33;

                for (int ySeg = 0; ySeg < 32; ++ySeg) {
                    double height1 = heightMap[zScale1 + ySeg];
                    double height2 = heightMap[zScaleEnd1 + ySeg];
                    double height3 = heightMap[zScale2 + ySeg];
                    double height4 = heightMap[zScaleEnd2 + ySeg];
                    double height5 = (heightMap[zScale1 + ySeg + 1] - height1) * 0.125f;
                    double height6 = (heightMap[zScaleEnd1 + ySeg + 1] - height2) * 0.125f;
                    double height7 = (heightMap[zScale2 + ySeg + 1] - height3) * 0.125f;
                    double height8 = (heightMap[zScaleEnd2 + ySeg + 1] - height4) * 0.125f;

                    for (int yIn = 0; yIn < 8; ++yIn) {
                        double baseIncr = height1;
                        double baseIncr2 = height2;
                        double scaleY = (height3 - height1) * 0.25f;
                        double scaleY2 = (height4 - height2) * 0.25f;

                        for (int zIn = 0; zIn < 4; ++zIn) {
                            double scaleZ = (baseIncr2 - baseIncr) * 0.25f;
                            double scaleZ2 = baseIncr - scaleZ;

                            for (int xIn = 0; xIn < 4; ++xIn) {
                                if ((scaleZ2 += scaleZ) > 0.0f) {
                                    chunk.setBlockId(xSeg * 4 + zIn, ySeg * 8 + yIn, zSeg * 4 + xIn, STONE);
                                } else if (ySeg * 8 + yIn <= seaHeight) {
                                    chunk.setBlockId(xSeg * 4 + zIn, ySeg * 8 + yIn, zSeg * 4 + xIn, STILL_WATER);
                                }
                            }

                            baseIncr += scaleY;
                            baseIncr2 += scaleY2;
                        }

                        height1 += height5;
                        height2 += height6;
                        height3 += height7;
                        height4 += height8;
                    }
                }
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                Biome biome = this.selector.pickBiome(baseX | x, baseZ | z);

                chunk.setBiomeId(x, z, biome.getId());
            }
        }

        //populate chunk
        for (Populator populator : this.generationPopulators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom);
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        this.nukkitRandom.setSeed(0xdeadbeef ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());
        for (Populator populator : this.populators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom);
        }

        FullChunk chunk = this.level.getChunk(chunkX, chunkZ);
        Biome biome = Biome.getBiome(chunk.getBiomeId(7, 7));
        biome.populateChunk(this.level, chunkX, chunkZ, this.nukkitRandom);
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(127.5, 256, 127.5);
    }
}