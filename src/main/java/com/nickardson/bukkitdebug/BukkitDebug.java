package com.nickardson.bukkitdebug;

import com.nickardson.bukkitdebug.script.JavaScriptEngine;
import com.nickardson.bukkitdebug.script.Stringifier;
import com.nickardson.bukkitdebug.web.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.net.BindException;

public class BukkitDebug extends JavaPlugin {
    final String HTDOCS_ROOT = "htdocs";
    final String HTDOCS_DESTINATION = "htdocs";

    public Server server;
    public JavaScriptEngine engine;
    public Stringifier stringifier;
    public Config config;

    File htdocs;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        saveDefaultConfig();

        config = new Config(getConfig());
        if (!config.isEnabled()) {
            getLogger().severe("------------------------------------------------");
            getLogger().severe("BukkitDebug is NOT enabled!");
            getLogger().severe("Configure a password in the config, then enable.");
            getLogger().severe("------------------------------------------------");
            return;
        }

        htdocs = new File(getDataFolder(), HTDOCS_DESTINATION);
        FileUtils.tryExtractFolder(getFile(), HTDOCS_ROOT, htdocs);

        // Disable Jetty logging.
        if (!config.isLogging()) {
            org.eclipse.jetty.util.log.Log.setLog(new DummyLogger());
        }

        startServer();

        engine = new JavaScriptEngine();
    }

    @Override
    public void onDisable() {
        if (server != null) {
            try {
                stopServer();
            } catch (Exception e) {
                getLogger().severe("Unable to stop BukkitDebug server!");
                e.printStackTrace();
            }
        }
    }

    private void startServer() {
        server = new Server(config.getPort());
        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", -1);

        stringifier = new Stringifier();

        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(new RootHandler(htdocs));
        handlers.addHandler(new SubHandler("/proxy", new ProxyHandler()));
        handlers.addHandler(new SubHandler("/eval", new SyncEvalHandler()));

        // LoadPlugin handler.
        SubServlet loadPluginHandler = new SubServlet("/loadplugin", new LoadPluginServlet());
        loadPluginHandler.getHolder().getRegistration().setMultipartConfig(new MultipartConfigElement(""));
        handlers.addHandler(loadPluginHandler);

        // Route all requests through the security handler.
        SecureHandler secureHandler = new SecureHandler(server, handlers);
        secureHandler.addAccount(config.getUsername(), config.getPassword(), "user");
        server.setHandler(secureHandler);

        try {
            server.start();
        } catch (BindException e) {
            getLogger().severe("The BukkitDebug server port is already in use, by either another BukkitDebug instance, or another program!");
        } catch (Exception e) {
            getLogger().severe("Unable to start BukkitDebug server!");
            e.printStackTrace();
        }
    }

    private void stopServer() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.stop();
                } catch (Exception e) {
                    getLogger().severe("Unable to stop BukkitDebug server!");
                    e.printStackTrace();
                }
            }
        }).start();

        server.join();
    }
}
