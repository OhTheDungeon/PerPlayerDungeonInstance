/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohthedungeon.storydungeon.database;

import com.ohthedungeon.storydungeon.PerPlayerDungeonInstance;
import java.util.logging.Level;

public class Error {
    public static void execute(PerPlayerDungeonInstance plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(PerPlayerDungeonInstance plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
