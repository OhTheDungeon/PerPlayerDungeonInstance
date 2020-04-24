package com.ohthedungeon.storydungeon.generator.b173gen.oldgen;

import com.ohthedungeon.storydungeon.generator.b173gen.biome.BetaBiome;
import com.ohthedungeon.storydungeon.generator.b173gen.biome.BiomeOld;
import com.ohthedungeon.storydungeon.generator.b173gen.oldnoisegen.NoiseGeneratorOctaves2D;
import java.util.Random;

public class WorldChunkManagerOld {
    public double temperatures[];
    public double rain[];
    public double c[];
    public BetaBiome dx[];

    private final NoiseGeneratorOctaves2D noise1;
    private final NoiseGeneratorOctaves2D noise2;
    private final NoiseGeneratorOctaves2D noise3;

    public WorldChunkManagerOld(long seed) {
        noise1 = new NoiseGeneratorOctaves2D(new Random(seed * 9871L), 4);
        noise2 = new NoiseGeneratorOctaves2D(new Random(seed * 39811L), 4);
        noise3 = new NoiseGeneratorOctaves2D(new Random(seed * 0x84a59L), 2);
    }

    public BetaBiome getBiome(int i, int j) {
        return getBiomeData(i, j, 1, 1)[0];
    }

    public double[] getTemperatures(double[] ad, int i, int j, int k, int l) {
        if(ad == null || ad.length < k * l) {
            ad = new double[k * l];
        }

        ad = noise1.generateNoiseArray(ad, i, j, k, l, 0.025D, 0.025D, 0.25D);
        c = noise3.generateNoiseArray(c, i, j, k, l, 0.25D, 0.25D, 0.58823529411764708D);
        int i1 = 0;
        for(int j1 = 0; j1 < k; j1++) {
            for(int k1 = 0; k1 < l; k1++) {
                double d = c[i1] * 1.1000000000000001D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;
                double d3 = (ad[i1] * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;
                d3 = 1.0D - (1.0D - d3) * (1.0D - d3);
                if(d3 < 0.0D) {
                    d3 = 0.0001D;
                }
                if(d3 > 1.0D) {
                    d3 = 1.0D;
                }
                ad[i1] = d3;
                i1++;
            }

        }
        this.temperatures = ad;
        return ad;
    }

    public BetaBiome[] getBiomeBlock(BetaBiome biomes[], int x, int z, int xSize, int zSize) {
        if(biomes == null || biomes.length < xSize * zSize) {
            biomes = new BetaBiome[xSize * zSize];
        }
        temperatures = noise1.generateNoiseArray(temperatures, x, z, xSize, xSize, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        rain = noise2.generateNoiseArray(rain, x, z, xSize, xSize, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        c = noise3.generateNoiseArray(c, x, z, xSize, xSize, 0.25D, 0.25D, 0.58823529411764708D);
        int index = 0;
        for(int blockX = 0; blockX < xSize; blockX++) {
            for(int blockZ = 0; blockZ < zSize; blockZ++) {
                double d = c[index] * 1.1D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;
                double temp = (temperatures[index] * 0.15D + 0.7D) * d2 + d * d1;
                d1 = 0.002D;
                d2 = 1.0D - d1;
                double humid = (rain[index] * 0.15D + 0.5D) * d2 + d * d1;
                temp = 1.0D - (1.0D - temp) * (1.0D - temp);
                if(temp < 0.0D) {
                    temp = 0.0D;
                }
                if(humid < 0.0D) {
                    humid = 0.0D;
                }
                if(temp > 1.0D) {
                    temp = 1.0D;
                }
                if(humid > 1.0D) {
                    humid = 1.0D;
                }
                temperatures[index] = temp;
                rain[index] = humid;
                biomes[index++] = BiomeOld.getBiomeFromLookup(temp, humid);
            }

        }

        return biomes;
    }

    public BetaBiome[] getBiomes(BetaBiome biomes[], int x, int z, int xSize, int zSize) {
        if(biomes == null || biomes.length < xSize * zSize) {
            biomes = new BetaBiome[xSize * zSize];
        }
        temperatures = noise1.generateNoiseArray(temperatures, x, z, xSize, xSize, 0.025D, 0.025D, 0.25D);
        rain = noise2.generateNoiseArray(rain, x, z, xSize, xSize, 0.05D, 0.05D, 1D / 3D);
        c = noise3.generateNoiseArray(c, x, z, xSize, xSize, 0.25D, 0.25D, 0.58823529411764708D);
        int index = 0;
        for(int blockX = 0; blockX < xSize; blockX++) {
            for(int blockZ = 0; blockZ < zSize; blockZ++) {
                double d = c[index] * 1.1D + 0.5D;
                double d1 = 0.01D;
                double d2 = 1.0D - d1;
                double temp = (temperatures[index] * 0.15D + 0.7D) * d2 + d * d1;
                d1 = 0.002D;
                d2 = 1.0D - d1;
                double humid = (rain[index] * 0.15D + 0.5D) * d2 + d * d1;
                temp = 1.0D - (1.0D - temp) * (1.0D - temp);
                if(temp < 0.0D) {
                    temp = 0.0D;
                }
                if(humid < 0.0D) {
                    humid = 0.0D;
                }
                if(temp > 1.0D) {
                    temp = 1.0D;
                }
                if(humid > 1.0D) {
                    humid = 1.0D;
                }
                temperatures[index] = temp;
                rain[index] = humid;
                biomes[index++] = BiomeOld.getBiomeFromLookup(temp, humid);
            }

        }

        return biomes;
    }

    private BetaBiome[] getBiomeData(int i, int j, int k, int l) {
        this.dx = getBiomes(this.dx, i, j, k, l);
        return this.dx;
    }
}
