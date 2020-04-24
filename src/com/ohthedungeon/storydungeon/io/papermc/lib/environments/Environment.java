package com.ohthedungeon.storydungeon.io.papermc.lib.environments;

import com.ohthedungeon.storydungeon.io.papermc.lib.features.asyncchunks.AsyncChunks;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.asyncchunks.AsyncChunksSync;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.asyncteleport.AsyncTeleport;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.asyncteleport.AsyncTeleportSync;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshot;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshotBeforeSnapshots;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshotNoOption;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshotResult;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.chunkisgenerated.ChunkIsGenerated;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.chunkisgenerated.ChunkIsGeneratedApiExists;
import com.ohthedungeon.storydungeon.io.papermc.lib.features.chunkisgenerated.ChunkIsGeneratedUnknown;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.concurrent.CompletableFuture;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
public abstract class Environment {

    private int minecraftVersion;
    private int minecraftPatchVersion;

    protected AsyncChunks asyncChunksHandler = new AsyncChunksSync();
    protected AsyncTeleport asyncTeleportHandler = new AsyncTeleportSync();
    protected ChunkIsGenerated isGeneratedHandler = new ChunkIsGeneratedUnknown();
    protected BlockStateSnapshot blockStateSnapshotHandler;

    public Environment() {
        Pattern versionPattern = Pattern.compile("\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?\\)");
        Matcher matcher = versionPattern.matcher(Bukkit.getVersion());
        int version = 0;
        int patchVersion = 0;
        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            try {
                version = Integer.parseInt(matchResult.group(2), 10);
            } catch (Exception ignored) {
            }
            if (matchResult.groupCount() >= 3) {
                try {
                    patchVersion = Integer.parseInt(matchResult.group(3), 10);
                } catch (Exception ignored) {
                }
            }
        }
        this.minecraftVersion = version;
        this.minecraftPatchVersion = patchVersion;

        // Common to all environments
        if (isVersion(13, 1)) {
            isGeneratedHandler = new ChunkIsGeneratedApiExists();
        } else {
            // TODO: Reflection based?
        }
        if (!isVersion(12)) {
            blockStateSnapshotHandler = new BlockStateSnapshotBeforeSnapshots();
        } else {
            blockStateSnapshotHandler = new BlockStateSnapshotNoOption();
        }
    }

    public abstract String getName();

    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen) {
        return asyncChunksHandler.getChunkAtAsync(world, x, z, gen);
    }

    public CompletableFuture<Boolean> teleport(Entity entity, Location location, TeleportCause cause) {
        return asyncTeleportHandler.teleportAsync(entity, location, cause);
    }

    public boolean isChunkGenerated(World world, int x, int z) {
        return isGeneratedHandler.isChunkGenerated(world, x, z);
    }

    public BlockStateSnapshotResult getBlockState(Block block, boolean useSnapshot) {
        return blockStateSnapshotHandler.getBlockState(block, useSnapshot);
    }

    public boolean isVersion(int minor) {
        return isVersion(minor, 0);
    }

    public boolean isVersion(int minor, int patch) {
        return minecraftVersion > minor || (minecraftVersion >= minor && minecraftPatchVersion >= patch);
    }

    public int getMinecraftVersion() {
        return minecraftVersion;
    }

    public int getMinecraftPatchVersion() {
        return minecraftPatchVersion;
    }

    public boolean isSpigot() {
        return false;
    }

    public boolean isPaper() {
        return false;
    }
}
