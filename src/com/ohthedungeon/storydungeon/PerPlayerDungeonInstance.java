/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohthedungeon.storydungeon;

import com.ohthedungeon.storydungeon.async.Async_Task_Pool;
import com.ohthedungeon.storydungeon.commands.CommandBack;
import com.ohthedungeon.storydungeon.commands.CommandDebug;
import com.ohthedungeon.storydungeon.commands.CommandMenu;
import com.ohthedungeon.storydungeon.config.DungeonConfig;
import com.ohthedungeon.storydungeon.database.Database;
import com.ohthedungeon.storydungeon.database.SQLite;
import com.ohthedungeon.storydungeon.event.EventManager;
import com.ohthedungeon.storydungeon.generator.FakeGenerator;
import com.ohthedungeon.storydungeon.gui.Menu;
import com.ohthedungeon.storydungeon.io.papermc.lib.PaperLib;
import com.ohthedungeon.storydungeon.util.I18n;
import com.ohthedungeon.storydungeon.util.ZoneUtils;
import com.ohthedungeon.storydungeon.vault.VaultManager;
import com.ohthedungeon.storydungeon.world.WorldManager;
import java.lang.reflect.Field;
import java.util.logging.Level;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author shadow_wind
 */
public class PerPlayerDungeonInstance extends JavaPlugin {
    private static PerPlayerDungeonInstance instance;
    private FakeGenerator fakeGenerator = null;
    private World world;
    private VaultManager vault;
    private Database db;
    private Async_Task_Pool async_task_pool;
    private boolean api_ready;
    private EventManager eventManager;
    
    public static class UnsupportedVersion extends Exception {
        
    }
    
    @Override
    public void onEnable() {
        instance = this;
        
        try {
            Class otd = Class.forName("otd.Main");
            Field api = otd.getDeclaredField("api_version");
            api.setAccessible(true);
            Integer api_version = (Integer) api.get(null);
            if(api_version >= 6) this.api_ready = true;
        } catch(ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            this.getLogger().log(Level.SEVERE, "{0}Reflection failed... Are you using the latest version of [Oh The Dungeons You''ll Go]?", ChatColor.RED);
            this.api_ready = false;
        }
        
        if(!api_ready) {
            this.getLogger().log(Level.SEVERE, "{0}You need upgrade to the latest version of [Oh The Dungeons You''ll Go] in order to use this plugin", ChatColor.RED);
            return;
        }
        
        async_task_pool = new Async_Task_Pool(this);
        vault = new VaultManager();
        this.getDataFolder().mkdir();
        DungeonConfig.load();
        if(!vault.setupEconomy()) {
            Bukkit.getLogger().log(Level.INFO, "{0}Vault is not installed. Economy function is disabled", ChatColor.RED);
            DungeonConfig.setEnableMoneyPayment(false);
        }
        this.getCommand("otd_pi").setExecutor(new CommandMenu(this));
        this.getCommand("otd_pi_back").setExecutor(new CommandBack(this));
        this.getCommand("otd_pi_debug").setExecutor(new CommandDebug(this));
        initFakeGenerator();
        
        this.db = new SQLite(this);
        this.db.load();
        
        I18n.init();
        ZoneUtils.loadZonePos();
        
        eventManager = new EventManager();
        getServer().getPluginManager().registerEvents(eventManager, this);
        getServer().getPluginManager().registerEvents(new Menu(), this);
        
        PaperLib.suggestPaper(this);
    }
    
    public Async_Task_Pool getAsyncTaskPool() {
        return this.async_task_pool;
    }
    
    public Database getDatabase() {
        return this.db;
    }
    
    public static PerPlayerDungeonInstance getInstance() {
        return instance;
    }
    
    public VaultManager getVaule() {
        return this.vault;
    }
    
    private void initFakeGenerator() {
        fakeGenerator = new FakeGenerator(this);
        world = WorldManager.createDungeonWorld(fakeGenerator);
        fakeGenerator.setDefaultWorld(world);
        fakeGenerator.registerSyncChunkTask();
    }
    
    public FakeGenerator getFakeGenerator() {
        return this.fakeGenerator;
    }
    
    public World getWorld() {
        return this.world;
    }
}
