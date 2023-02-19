package cn.nukkit.level.generator;

import cn.nukkit.block.*;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.*;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.*;

public class Nether extends Generator {
    private ChunkManager level;
    /**
     * @var Random
     */
    private NukkitRandom nukkitRandom;
    private Random random;
    private double waterHeight = 32;
    private double emptyHeight = 64;
    private double emptyAmplitude = 1;
    private double density = 0.5;
    private double bedrockDepth = 5;
    private final List<Populator> populators = new ArrayList<>();
    private List<Populator> generationPopulators = new ArrayList<>();

    private long localSeed1;
    private long localSeed2;

    private Simplex noiseBase;

    public Nether() {
        this(new HashMap<>());
    }

    public Nether(Map<String, Object> options) {
        //Nothing here. Just used for future update.
    }

    @Override
    public int getId() {
        return Generator.TYPE_NETHER;
    }

    @Override
    public int getDimension() {
        return Level.DIMENSION_NETHER;
    }

    @Override
    public String getName() {
        return "nether";
    }

    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<>();
    }

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.random = new Random();
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.noiseBase = new Simplex(this.nukkitRandom, 4, 1 / 4f, 1 / 64f);
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();
        PopulatorOre ores = new PopulatorOre(Block.NETHERRACK);
        ores.setOreTypes(new OreType[]{
                new OreType(new BlockOreQuartz(), 20, 16, 0, 128),
                new OreType(new BlockSoulSand(), 5, 64, 0, 128),
                new OreType(new BlockGravel(), 5, 64, 0, 128),
                new OreType(new BlockLava(), 1, 16, 0, (int) this.waterHeight),
        });
        this.populators.add(ores);
        this.populators.add(new PopulatorGlowStone());
        PopulatorGroundFire groundFire = new PopulatorGroundFire();
        groundFire.setBaseAmount(1);
        groundFire.setRandomAmount(1);
        this.populators.add(groundFire);
        PopulatorLava lava = new PopulatorLava();
        lava.setBaseAmount(0);
        lava.setRandomAmount(2);
        this.populators.add(lava);
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {

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

    public Vector3 getSpawn() {
        return new Vector3(0, 64, 0);
    }
}