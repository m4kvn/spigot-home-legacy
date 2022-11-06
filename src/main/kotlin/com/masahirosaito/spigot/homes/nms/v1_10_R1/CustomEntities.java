package com.masahirosaito.spigot.homes.nms.v1_10_R1;

import net.minecraft.server.v1_10_R1.*;
import org.bukkit.entity.EntityType;

import java.util.Objects;
import java.util.Set;

import static com.masahirosaito.spigot.homes.UtilsKt.getPrivateStatic;

public enum CustomEntities {

    HOMES_DISPLAY_ARMOR_STAND(
            "Homes_DisplayArmorStand", 30,
            EntityType.ARMOR_STAND,
            EntityArmorStand.class,
            EntityNMSArmorStand.class);

    private final String name;
    private final int id;
    private final EntityType entityType;
    private final Class<? extends Entity> nmsClass;
    private final Class<? extends Entity> customClass;
    private final MinecraftKey key;
    private final MinecraftKey oldKey;

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
                Objects.requireNonNull(getPrivateStatic(EntityTypes.class, "b"))).b(nmsClass);
    }

    public static void registerEntities() {
        for (CustomEntities ce : CustomEntities.values()) ce.register();
    }

    @SuppressWarnings({"unused"})
    public static void unregisterEntities() {
        for (CustomEntities ce : CustomEntities.values()) ce.unregister();
    }

    @SuppressWarnings("unchecked")
    private void register() {
        ((Set<MinecraftKey>) Objects.requireNonNull(getPrivateStatic(EntityTypes.class, "d"))).add(key);
        ((RegistryMaterials<MinecraftKey, Class<?>>)
                Objects.requireNonNull(getPrivateStatic(EntityTypes.class, "b"))).a(id, key, customClass);
    }

    @SuppressWarnings("unchecked")
    private void unregister() {
        ((Set<MinecraftKey>) Objects.requireNonNull(getPrivateStatic(EntityTypes.class, "d"))).remove(key);
        ((RegistryMaterials<MinecraftKey, Class<?>>)
                Objects.requireNonNull(getPrivateStatic(EntityTypes.class, "b"))).a(id, oldKey, nmsClass);
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings({"unused"})
    public int getID() {
        return id;
    }

    @SuppressWarnings({"unused"})
    public EntityType getEntityType() {
        return entityType;
    }

    @SuppressWarnings({"unused"})
    public Class<?> getCustomClass() {
        return customClass;
    }
}

