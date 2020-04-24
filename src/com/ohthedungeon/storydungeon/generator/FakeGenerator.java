/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohthedungeon.storydungeon.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scheduler.BukkitRunnable;
import com.ohthedungeon.storydungeon.world.ZoneConfig;
import com.ohthedungeon.storydungeon.populator.SmallOakPopulator;
import com.ohthedungeon.storydungeon.populator.VoidPopulator;
import com.ohthedungeon.storydungeon.PerPlayerDungeonInstance;
import com.ohthedungeon.storydungeon.themes.*;
import com.ohthedungeon.storydungeon.util.DungeonSyncTask;
import forge_sandbox.greymerk.roguelike.dungeon.settings.DungeonSettings;
import com.ohthedungeon.storydungeon.generator.me.daddychurchill.Conurbation.ChunkCallback;
import com.ohthedungeon.storydungeon.populator.GiantFlowerPopulator;
import com.ohthedungeon.storydungeon.populator.PromisedTreePopulator;
import com.ohthedungeon.storydungeon.populator.SakuraPopulator;
import com.ohthedungeon.storydungeon.util.AsyncErrorLogger;
import java.util.Collections;
import java.util.LinkedHashMap;
//import java.util.logging.Level;

/**
 *
 * @author shadow_wind
 */
public final class FakeGenerator extends ChunkGenerator {
    private final static Map<String, BaseGenerator> GENERATORS = new LinkedHashMap<>();
    private final static Map<String, BlockPopulator> POPULATORS = new LinkedHashMap<>();
    private final static Map<String, DungeonSettings> DUNGEONS = new LinkedHashMap<>();
    
    private final static Random RANDOM = new Random();
    static {
        GENERATORS.put("Nordic", new Nordic());
//        GENERATORS.put("Empty", new EmptyWorld());
        GENERATORS.put("AlmostFlatlands", new AlmostFlatlands());
        GENERATORS.put("Vanilla", new Vanilla());
        GENERATORS.put("Fjord", new Fjord());
        GENERATORS.put("Hoth", new Hoth());
        GENERATORS.put("Tatooine", new Tatooine());
        GENERATORS.put("Slient", new Slient());
        GENERATORS.put("NorthLand", new NorthLand());
        GENERATORS.put("Rocky", new Rocky());
        GENERATORS.put("City", new ChunkCallback());
        GENERATORS.put("Island", new Island());
        GENERATORS.put("Tropic", new Tropic());
//        GENERATORS.put("Town", new Town());
//        GENERATORS.put("Valley", new Valley());
//        GENERATORS.put("Cyber", new CyberWorldChunkGenerator());
//        GENERATORS.put("GlowsLike", new GlowsLike());
//        GENERATORS.put("FarLand0", new FarLand0());
//        GENERATORS.put("FarLand1", new FarLand1());
//        GENERATORS.put("FarLand2", new FarLand2());
        
        POPULATORS.put("Void", new VoidPopulator());
        POPULATORS.put("Oak", new SmallOakPopulator());
        POPULATORS.put("PromisedTree", new PromisedTreePopulator());
        POPULATORS.put("GiantFlower", new GiantFlowerPopulator());
        POPULATORS.put("Sakura", new SakuraPopulator());
        
        DUNGEONS.put("Desert", new SettingsDesertTheme());
        DUNGEONS.put("Forest", new SettingsForestTheme());
        DUNGEONS.put("Grassland", new SettingsGrasslandTheme());
        DUNGEONS.put("Hole", new SettingsHoleTheme());
        DUNGEONS.put("Ice", new SettingsIceTheme());
        DUNGEONS.put("Jungle", new SettingsJungleTheme());
        DUNGEONS.put("Mountain", new SettingsMountainTheme());
        DUNGEONS.put("Mushroom", new SettingsMushroomTheme());
        DUNGEONS.put("Rare", new SettingsRareTheme());
        DUNGEONS.put("Ruin", new SettingsRuinTheme());
        DUNGEONS.put("Swamp", new SettingsSwampTheme());
        DUNGEONS.put("Tree", new SettingsTreeTheme());
    }
    
    public static Map<String, BaseGenerator> getGenerators() {
        return Collections.unmodifiableMap(GENERATORS);
    }
    
    public static Map<String, BlockPopulator> getPopulators() {
        return Collections.unmodifiableMap(POPULATORS);
    }
    
    public static Map<String, DungeonSettings> getTowers() {
        return Collections.unmodifiableMap(DUNGEONS);
    }
    
    private final List<DungeonSyncTask> task_pool = new ArrayList<>();
    private final PerPlayerDungeonInstance plugin;
    
    public FakeGenerator(PerPlayerDungeonInstance plugin) {
        this.plugin = plugin;
    }
    
    public World fake_world;
    public void setDefaultWorld(World w) {
        fake_world = w;
    }
    
    public static DungeonSettings getDungeonTheme(String name) {
//        Bukkit.getLogger().log(Level.SEVERE, name);
        return DUNGEONS.get(name);
    }

    
    public World getDungeonWorld() {
        return this.fake_world;
    }
    
    public int getPoolSize() {
        int res;
        synchronized(this.task_pool) {
            res = this.task_pool.size();
        }
        return res;
    }
    
    public FakeGenerator addTaskToPool(DungeonSyncTask... tasks) {
        synchronized(this.task_pool) {
            for(DungeonSyncTask task : tasks) {
                task_pool.add(task);
            }
        }
        return this;
    }
    
    public static Random getRandom() {
        return RANDOM;
    }
    
    public static BaseGenerator getGenerator(String generator) {
        return GENERATORS.get(generator);
    }
    
    public static BlockPopulator getBlockPopulator(String populator) {
        return POPULATORS.get(populator);
    }
    
    
    public void generateZone(World world, int chunkX, int chunkZ, ZoneConfig zc) {
//        TaskHolder t = new TaskHolder(this, world, chunkX, chunkZ, zc);
        this.plugin.getAsyncTaskPool().addTask(this, world, chunkX, chunkZ, zc);
//        BukkitRunnable t_run = new BukkitRunnable() {
//            @Override
//            public void run() {
//                try {
//                    while(!t.run()) {
//                        try {
//                            Thread.sleep(1000L);
//                        } catch(InterruptedException ex) {
//
//                        }
//                    }
//                } catch(Exception ex) {
//                    AsyncErrorLogger.normalLog(chunkX, chunkZ, AsyncErrorLogger.exceptionToString(ex));
//                }
//            }
//        };
//        t_run.runTaskLaterAsynchronously(this.plugin, 1L);
    }
    
    ChunkData empty = null;
    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        if(empty == null) empty = Bukkit.createChunkData(world);
        for(int i = 0; i < 16; i++)
            for(int j = 0; j < 16; j++)
                biome.setBiome(i, j, Biome.THE_VOID);
        return empty;
    }
    
    private static BukkitRunnable run = null;
        
    public void registerSyncChunkTask() {
        if(run != null) return;
        run = new BukkitRunnable() {
            @Override
            public void run() {
                DungeonSyncTask task = null;
                synchronized(task_pool) {
                    if(task_pool.isEmpty()) return;
                    for(DungeonSyncTask t : task_pool) {
                        task = t;
                        break;
                    }
                }
                if(task == null) return;
                boolean r;
                try {
                    r = task.getTask().doTask(fake_world, RANDOM);
                } catch(Exception ex) {
                    r = true;
                    AsyncErrorLogger.normalLog(task.getX(), task.getZ(), AsyncErrorLogger.exceptionToString(ex));
                }
                if(r) {
                    synchronized(task_pool) {
                        task_pool.remove(task);
                    }
                }
            }
        };
        run.runTaskTimer(this.plugin, 1L, 5L);
    }
}
