/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohthedungeon.storydungeon.async;

import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.Material;

/**
 *
 * @author shadow_wind
 */
public class AsyncChunk {
    private final Map<Integer, Material> map = new LinkedHashMap<>();
    
    public Map<Integer, Material> getMap() {
        return this.map;
    }
    
    public int getSize() {
        return map.size();
    }
    
    public static int xyzToPos(int x, int y, int z) {
        int res = (x << 20) | (y << 10) | z;
        return res;
    }
    
    public AsyncChunk setRegion(int x1, int y1, int z1, int x2, int y2, int z2, Material material) {
        for(int x3 = x1; x3 < x2; x3++)
            for(int y3 = y1; y3 < y2; y3++)
                for(int z3 = z1; z3 < z2; z3++)
                    setBlock(x3, y3, z3, material);
        return this;
    }
    
    public static int[] posToXYZ(int key) {
        
        int x = (key >>> 20) & 0xF;
        int y = (key >>> 10) & 0xFF;
        int z = key & 0xF;
        return new int[] { x,y,z };
    }
    
    public Material getType(int x, int y, int z) {
        int key = xyzToPos(x, y, z);
        if(!map.containsKey(key)) return Material.AIR;
        return map.get(key);
    }
    
    public AsyncChunk setBlock(int x, int y, int z, Material material) {
        int key = xyzToPos(x, y, z);
        map.put(key, material);
        return this;
    }
    
    public AsyncChunk remove(int key) {
        map.remove(key);
        return this;
    }
}
