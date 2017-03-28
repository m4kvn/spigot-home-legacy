package com.masahirosaito.spigot.homes.nms.v1_11_R1;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Field;
import java.util.Set;

public enum CustomEntities {

    HOMES_DISPLAY_ARMOR_STAND(
            "Homes_DisplayArmorStand", 30,
            EntityType.ARMOR_STAND,
            EntityArmorStand.class,
            EntityNMSArmorStand.class);

    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends Entity> nmsClass;
    private Class<? extends Entity> customClass;
    private MinecraftKey key;
    private MinecraftKey oldKey;

    @SuppressWarnings("unchecked")
    CustomEntities(String name, int id, EntityType entityType,
                   Class<? extends Entity> nmsClass,
                   Class<? extends Entity> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
        this.key = new MinecraftKey(name);
        this.oldKey = ((RegistryMaterials<MinecraftKey, Class<?>>)
                getPrivateStatic(EntityTypes.class, "b")).b(nmsClass);
    }

    public static void registerEntities() {
        for (CustomEntities ce : CustomEntities.values()) ce.register();
    }

    public static void unregisterEntities() {
        for (CustomEntities ce : CustomEntities.values()) ce.unregister();
    }

    @SuppressWarnings("unchecked")
    private void register() {
        ((Set<MinecraftKey>) getPrivateStatic(EntityTypes.class, "d")).add(key);
        ((RegistryMaterials<MinecraftKey, Class<?>>)
                getPrivateStatic(EntityTypes.class, "b")).a(id, key, customClass);
    }

    @SuppressWarnings("unchecked")
    private void unregister() {
        ((Set<MinecraftKey>) getPrivateStatic(EntityTypes.class, "d")).remove(key);
        ((RegistryMaterials<MinecraftKey, Class<?>>)
                getPrivateStatic(EntityTypes.class, "b")).a(id, oldKey, nmsClass);
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Class<?> getCustomClass() {
        return customClass;
    }

    private static Object getPrivateStatic(final Class<?> clazz, final String f) {
        try {
            Field field = clazz.getDeclaredField(f);
            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
