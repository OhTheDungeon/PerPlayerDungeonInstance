/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohthedungeon.storydungeon.util;

import com.ohthedungeon.storydungeon.PerPlayerDungeonInstance;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.bukkit.Bukkit;

/**
 *
 * @author shadow_wind
 */
public class AsyncErrorLogger {
    private static void logTask(int dx, int dz, String error, PerPlayerDungeonInstance plugin) throws IOException {
        File f = new File(plugin.getDataFolder().toString() + File.separator + "logs");
        if(!f.exists()) f.mkdirs();
        
        FileWriter fileWriter = new FileWriter(f.toString() + File.separator + "log_" + dx + "_" + dz + ".log");
        try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.newLine();
            writer.append(error);
        }
    }
    
    public static void normalLog(int dx, int dz, String error) {
        try {
            logTask(dx, dz, error, PerPlayerDungeonInstance.getInstance());
        } catch(IOException ex) {
            
        }
    }
    
    public static void asyncLog(int dx, int dz, String error) {
        PerPlayerDungeonInstance plugin = PerPlayerDungeonInstance.getInstance();
        Bukkit.getScheduler().runTaskAsynchronously(plugin , () -> {
            try {
                logTask(dx, dz, error, plugin);
            } catch(IOException ex) {
                
            }
        });
    }
    
    public static String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        
        return sw.toString();
    }
}
