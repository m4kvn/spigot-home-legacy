package com.masahirosaito.spigot.homes.utils;

import com.masahirosaito.spigot.homes.Homes;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.Assert;
import org.mockito.Matchers;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.reflect.Whitebox;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.easymock.EasyMock.mock;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class TestInstanceCreator {
    private Homes homes;
    private Server mockServer;
    private CommandSender commandSender;

    public static final File pluginDirectory = new File("bin/test/server/plugins/homestest");
    public static final File serverDirectory = new File("bin/test/server");
    public static final File worldsDirectory = new File("bin/test/server");

    public boolean setUp() throws Exception {
        try {
            pluginDirectory.mkdirs();
            assertTrue(pluginDirectory.exists());

            MockGateway.MOCK_STANDARD_METHODS = false;

            mockServer = mock(Server.class);
            JavaPluginLoader mockPluginLoader = PowerMock.createMock(JavaPluginLoader.class);
            Whitebox.setInternalState(mockPluginLoader, "server", mockServer);
            when(mockServer.getName()).thenReturn("TestBukkit");
            Logger.getLogger("Minecraft").setParent(Util.logger);
            when(mockServer.getLogger()).thenReturn(Util.logger);

            PluginDescriptionFile pdf = PowerMockito.spy(
                    new PluginDescriptionFile("Homes", "0.6-Test",
                    "com.masahirosaito.spigot.homes.Homes"));
            when(pdf.getAuthors()).thenReturn(new ArrayList<>());

            homes = PowerMockito.spy(
                    new Homes(mockPluginLoader, pdf, pluginDirectory,
                            new File(pluginDirectory, "testPluginFile")));

            PowerMockito.doAnswer(invocation -> null).when(homes, "setupMetrics");

            doReturn(pluginDirectory).when(homes).getDataFolder();

            doReturn(true).when(homes).isEnabled();
            doReturn(Util.logger).when(homes).getLogger();

            JavaPlugin[] plugins = new JavaPlugin[] { homes };

            PluginManager mockPluginManager = PowerMockito.mock(PluginManager.class);
            when(mockPluginManager.getPlugins()).thenReturn(plugins);
            when(mockPluginManager.getPlugin("Homes")).thenReturn(homes);
            when(mockPluginManager.getPermission(anyString())).thenReturn(null);

            when(mockServer.getWorld(anyString())).thenAnswer(invocation -> {
                String arg;
                try {
                    arg = invocation.getArgumentAt(0, String.class);
                } catch (Exception e) {
                    return null;
                }
                return MockWorldFactory.getWorld(arg);
            });

            when(mockServer.getWorld(any(UUID.class))).thenAnswer(invocation -> {
                UUID arg;
                try {
                    arg = invocation.getArgumentAt(0, UUID.class);
                } catch (Exception e) {
                    return null;
                }
                return MockWorldFactory.getWorld(arg);
            });

            when(mockServer.getWorlds()).thenAnswer(invocation -> MockWorldFactory.getWorlds());

            when(mockServer.getPluginManager()).thenReturn(mockPluginManager);

            when(mockServer.createWorld(Matchers.isA(WorldCreator.class))).thenAnswer(invocation -> {
               WorldCreator arg;
               try {
                   arg = invocation.getArgumentAt(0, WorldCreator.class);
               } catch (Exception e) {
                   return null;
               }
               if (arg.name().equalsIgnoreCase("nullworld")) {
                   return MockWorldFactory.makeNewNullMockWorld(arg.name(), arg.environment(), arg.type());
               }
               return MockWorldFactory.makeNewMockWorld(arg.name(), arg.environment(), arg.type());
            });

            when(mockServer.unloadWorld(anyString(), anyBoolean())).thenReturn(true);

            BukkitScheduler mockScheduler = mock(BukkitScheduler.class);
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class), anyLong()))
                    .thenAnswer(invocation -> {
                        Runnable arg;
                        try {
                            arg = invocation.getArgumentAt(1, Runnable.class);
                        } catch (Exception e) {
                            return null;
                        }
                        arg.run();
                        return null;
                    });
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class)))
                    .thenAnswer(invocation -> {
                        Runnable arg;
                        try {
                            arg = invocation.getArgumentAt(1, Runnable.class);
                        } catch (Exception e) {
                            return null;
                        }
                        arg.run();
                        return null;
                    });
            when(mockServer.getScheduler()).thenReturn(mockScheduler);

            // TODO: Set server
            Field serverField = JavaPlugin.class.getDeclaredField("server");
            serverField.setAccessible(true);
            serverField.set(homes, mockServer);

            // TODO: Init our command sender
            final Logger commandSenderLogger = Logger.getLogger("CommandSender");
            commandSenderLogger.setParent(Util.logger);
            commandSender = mock(CommandSender.class);
            doAnswer(invocation -> {
                commandSenderLogger.info(ChatColor.stripColor(invocation.getArgumentAt(0, String.class)));
                return null;
            }).when(commandSender).sendMessage(anyString());
            when(commandSender.getServer()).thenReturn(mockServer);
            when(commandSender.getName()).thenReturn("MockCommandSender");
            when(commandSender.isPermissionSet(anyString())).thenReturn(true);
            when(commandSender.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(commandSender.hasPermission(anyString())).thenReturn(true);
            when(commandSender.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(commandSender.addAttachment(homes)).thenReturn(null);
            when(commandSender.isOp()).thenReturn(true);

            Bukkit.setServer(mockServer);

            homes.onLoad();
            homes.onEnable();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean tearDown() {
        try {
            Field serverField = Bukkit.class.getDeclaredField("server");
            serverField.setAccessible(true);

        } catch (Exception e) {
            Util.log(Level.SEVERE, "Error while trying unregister the server from Bukkit. Has Bukkit changed?");
            e.printStackTrace();
            Assert.fail(e.getMessage());
            return false;
        }

        homes.onDisable();

        MockWorldFactory.clearWorlds();

        return true;
    }

    public Homes getHomes() {
        return this.homes;
    }

    public Server getServer() {
        return this.mockServer;
    }

    public CommandSender getCommandSender() {
        return this.commandSender;
    }
}
