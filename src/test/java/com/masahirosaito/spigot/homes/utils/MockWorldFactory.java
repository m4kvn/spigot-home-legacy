package com.masahirosaito.spigot.homes.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;

public class MockWorldFactory {
    private static final Map<String, World> createdWorlds = new LinkedHashMap<>();
    private static final Map<UUID, World> worldUIDS = new HashMap<>();

    private static final Map<World, Boolean> pvpStates = new WeakHashMap<>();
    private static final Map<World, Boolean> keepSpawnInMemoryStates = new WeakHashMap<>();
    private static final Map<World, Difficulty> difficultyStates = new WeakHashMap<>();

    private static void registerWorld(World world) {
        createdWorlds.put(world.getName(), world);
        worldUIDS.put(world.getUID(), world);
        new File(TestInstanceCreator.worldsDirectory, world.getName()).mkdir();
    }

    private static World basics(String world, World.Environment env, WorldType type) {
        World mockWorld = mock(World.class);
        when(mockWorld.getName()).thenReturn(world);
        when(mockWorld.getPVP()).thenAnswer(invocation -> {
            World w = (World) invocation.getMock();
            if (!pvpStates.containsKey(w)) pvpStates.put(w, true);
            return pvpStates.get(w);
        });
        doAnswer(invocation -> {
            pvpStates.put((World) invocation.getMock(), invocation.getArgumentAt(0, Boolean.class));
            return null;
        }).when(mockWorld).setPVP(anyBoolean());
        when(mockWorld.getKeepSpawnInMemory()).thenAnswer(invocation -> {
            World w = (World) invocation.getMock();
            if (!keepSpawnInMemoryStates.containsKey(w)) keepSpawnInMemoryStates.put(w, true);
            return keepSpawnInMemoryStates.get(w);
        });
        doAnswer(invocation -> {
            keepSpawnInMemoryStates.put((World) invocation.getMock(), invocation.getArgumentAt(0, Boolean.class));
            return null;
        }).when(mockWorld).setKeepSpawnInMemory(anyBoolean());
        when(mockWorld.getDifficulty()).thenAnswer(invocation -> {
            World w = (World) invocation.getMock();
            if (!difficultyStates.containsKey(w)) difficultyStates.put(w, Difficulty.NORMAL);
            return difficultyStates.get(w);
        });
        doAnswer(invocation -> {
            difficultyStates.put((World) invocation.getMock(), invocation.getArgumentAt(0, Difficulty.class));
            return null;
        }).when(mockWorld).setDifficulty(any(Difficulty.class));
        when(mockWorld.getEnvironment()).thenReturn(env);
        when(mockWorld.getWorldType()).thenReturn(type);
        when(mockWorld.getSpawnLocation()).thenReturn(new Location(mockWorld, 0, 64, 0));
        whenMockWorldGetWorldFolder(mockWorld);
        when(mockWorld.getBlockAt(any(Location.class))).thenAnswer(invocation -> {
            Location loc;
            try {
                loc = invocation.getArgumentAt(0, Location.class);
            } catch (Exception e) {
                return null;
            }
            Material blockType = Material.AIR;
            Block mockBlock = mock(Block.class);
            if (loc.getBlockY() < 64) {
                blockType = Material.DIRT;
            }
            when(mockBlock.getType()).thenReturn(blockType);
            when(mockBlock.getTypeId()).thenReturn(blockType.getId());
            when(mockBlock.getWorld()).thenReturn(loc.getWorld());
            when(mockBlock.getX()).thenReturn(loc.getBlockX());
            when(mockBlock.getY()).thenReturn(loc.getBlockY());
            when(mockBlock.getZ()).thenReturn(loc.getBlockZ());
            when(mockBlock.getLocation()).thenReturn(loc);
            when(mockBlock.isEmpty()).thenReturn(blockType == Material.AIR);
            return mockBlock;
        });
        when(mockWorld.getUID()).thenReturn(UUID.randomUUID());
        return mockWorld;
    }

    private static World nullWorld(String world, World.Environment env, WorldType type) {
        World mockWorld = mock(World.class);
        when(mockWorld.getName()).thenReturn(world);
        when(mockWorld.getEnvironment()).thenReturn(env);
        when(mockWorld.getWorldType()).thenReturn(type);
        when(mockWorld.getSpawnLocation()).thenReturn(new Location(mockWorld, 0, 64, 0));
        whenMockWorldGetWorldFolder(mockWorld);
        when(mockWorld.getBlockAt(any(Location.class))).thenAnswer(invocation -> {
            Location loc;
            try {
                loc = invocation.getArgumentAt(0, Location.class);
            } catch (Exception e) {
                return null;
            }
            Block mockBlock = mock(Block.class);
            Material blockType = Material.AIR;
            when(mockBlock.getType()).thenReturn(blockType);
            when(mockBlock.getTypeId()).thenReturn(blockType.getId());
            when(mockBlock.getWorld()).thenReturn(loc.getWorld());
            when(mockBlock.getX()).thenReturn(loc.getBlockX());
            when(mockBlock.getY()).thenReturn(loc.getBlockY());
            when(mockBlock.getZ()).thenReturn(loc.getBlockZ());
            when(mockBlock.getLocation()).thenReturn(loc);
            when(mockBlock.isEmpty()).thenReturn(true);
            return mockBlock;
        });
        return mockWorld;
    }

    private static void whenMockWorldGetWorldFolder(World mockWorld) {
        when(mockWorld.getWorldFolder()).thenAnswer(invocation -> {
            if (!(invocation.getMock() instanceof World)) return null;
            World thiss = (World) invocation.getMock();
            return new File(TestInstanceCreator.serverDirectory, thiss.getName());
        });
    }

    public static World makeNewMockWorld(String world, World.Environment env, WorldType type) {
        World w = basics(world, env, type);
        registerWorld(w);
        return w;
    }

    public static World makeNewMockWorld(String world, World.Environment env, WorldType type, long seed,
                                         ChunkGenerator generator) {
        World mockWorld = basics(world, env, type);
        when(mockWorld.getGenerator()).thenReturn(generator);
        when(mockWorld.getSeed()).thenReturn(seed);
        registerWorld(mockWorld);
        return mockWorld;
    }

    public static World makeNewNullMockWorld(String world, World.Environment env, WorldType type) {
        World w = nullWorld(world, env, type);
        registerWorld(w);
        return w;
    }

    public static World getWorld(String name) {
        return createdWorlds.get(name);
    }

    public static World getWorld(UUID worldUID) {
        return worldUIDS.get(worldUID);
    }

    public static List<World> getWorlds() {
        return new ArrayList<>(createdWorlds.values());
    }

    public static void clearWorlds() {
        for (String name : createdWorlds.keySet()) {
            new File(TestInstanceCreator.worldsDirectory, name).delete();
        }
        createdWorlds.clear();
        worldUIDS.clear();
    }
}
