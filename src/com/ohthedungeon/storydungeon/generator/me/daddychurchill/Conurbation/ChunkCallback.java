// 
// Decompiled by Procyon v0.5.36
// 

package com.ohthedungeon.storydungeon.generator.me.daddychurchill.Conurbation;

import com.ohthedungeon.storydungeon.async.AsyncChunk;
import com.ohthedungeon.storydungeon.generator.BaseGenerator;
import com.ohthedungeon.storydungeon.generator.me.daddychurchill.Conurbation.Support.RealChunk;
import org.bukkit.Chunk;
import com.ohthedungeon.storydungeon.generator.me.daddychurchill.Conurbation.Support.ByteChunk;
import org.bukkit.Location;
import java.util.Random;
import java.util.Arrays;
import org.bukkit.generator.BlockPopulator;
import java.util.List;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class ChunkCallback extends BaseGenerator
{
    private WorldConfig config;
    private BlockCallback blockCallback = new BlockCallback(this);;
    private Generator generators;
    
    public ChunkCallback() {
        this.config = new WorldConfig();
    }
    
    @Override
    public AsyncChunk asyncGenerateChunkData(long seed, Random random, int chunkX, int chunkZ) {
        if (this.generators == null) {
            this.generators = new Generator(this.config);
        }
        final ByteChunk byteChunk = new ByteChunk(chunkX, chunkZ);
        this.generators.generate(byteChunk, random, chunkX, chunkZ);
        populate(byteChunk, random, chunkX, chunkZ);
        return byteChunk.blocks;
    }
    
    public void populate(final ByteChunk realChunk, final Random random, final int chunkX, int chunkZ) {
        this.generators.populate(realChunk, random, chunkX, chunkZ);
    }
}
