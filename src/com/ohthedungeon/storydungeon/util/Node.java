/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ohthedungeon.storydungeon.util;

import org.bukkit.Material;

public class Node {
    private int[] pos;
    private Material mat;
    
    public Node(int[] pos, Material mat) {
        this.pos = pos;
        this.mat = mat;
    }
    
    public int[] getPos() {
        return this.pos;
    }
    public Material getMaterial() {
        return this.mat;
    }
    
    
}
