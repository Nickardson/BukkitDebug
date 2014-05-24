package com.nickardson.bukkitdebug;

import com.nickardson.bukkitdebug.script.JavaScriptEngine;
import com.nickardson.bukkitdebug.script.Stringifier;
import com.nickardson.bukkitdebug.web.BukkitDebugServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;

public class BukkitDebug extends JavaPlugin {
    public final String HTDOCS_ROOT = "htdocs";
    public final String HTDOCS_DESTINATION = "htdocs";

    public JavaScriptEngine engine;
    public Stringifier stringifier;
    public Config config;
    public BukkitDebugServer server;

    public Resource htdocs;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        config = new Config(getConfig());
        if (!config.isEnabled()) {
            showEnableMessage();
            return;
        }

        // Disable Jetty logging.
        if (!config.isLogging()) {
            org.eclipse.jetty.util.log.Log.setLog(new DummyLogger());
        }

        htdocs = getRootResource();
        stringifier = new Stringifier();
        server = new BukkitDebugServer().start();
        engine = new JavaScriptEngine();
    }

    @Override
    public void onDisable() {
        server.stop();
    }

    /**
     * Writes an red error message
     * @param message The message to write out.
     */
    public static void error(String message) {
        ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();

        if (consoleSender != null) {
            consoleSender.sendMessage(ChatColor.RED + "[BukkitDebug] " + message);
        } else {
            BukkitDebug.getPlugin(BukkitDebug.class).getLogger().severe(message);
        }
    }

    /**
     * Gets the jetty resource for the htdocs root.
     * @return The root resource.
     */
    private Resource getRootResource() {
        if (config.isExternalRoot()) {
            File destination = new File(getDataFolder(), HTDOCS_DESTINATION);
            FileUtils.tryExtractFolder(getJarFile(), HTDOCS_ROOT, destination);
            return Resource.newResource(destination);
        } else {
            return Resource.newClassPathResource("htdocs");
        }
    }

    /**
     * Gets the plugin jar file.
     * @return The plugin jar file.
     */
    public File getJarFile() {
        return getFile();
    }

    /**
     * Shows a message telling the user to enable in the config.
     */
    private void showEnableMessage() {
        getLogger().severe("------------------------------------------------");
        getLogger().severe("BukkitDebug is NOT enabled!");
        getLogger().severe("Configure a password in the config, then enable.");
        getLogger().severe("------------------------------------------------");
    }
}
