package com.nickardson.bukkitdebug;

import com.nickardson.bukkitdebug.web.RootHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;

import java.io.File;
import java.io.IOException;

public class BukkitDebug extends JavaPlugin {
    final String HTDOCS_ROOT = "htdocs";
    final String HTDOCS_DESTINATION = "htdocs";

    Server server;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        File htdocs = new File(getDataFolder(), HTDOCS_DESTINATION);

        if (!htdocs.exists() || !htdocs.isDirectory()) {
            htdocs.mkdir();

            for (String filename : FileUtils.dir(getFile(), HTDOCS_ROOT)) {
                File destination = new File(htdocs, filename.substring(HTDOCS_ROOT.length() + 1));
                destination.getParentFile().mkdirs();
                try {
                    FileUtils.copyResourceToFile("/" + filename, destination);
                } catch (IOException e) {
                    getLogger().severe("Unable to extract: " + filename);
                    e.printStackTrace();
                }
            }
        }

        server = new Server(13370);
        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", -1);

        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(new RootHandler(htdocs));
        server.setHandler(handlers);

        try {
            server.start();
        } catch (Exception e) {
            getLogger().severe("Unable to start BukkitDebug server!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            server.stop();
        } catch (Exception e) {
            getLogger().severe("Unable to stop BukkitDebug server!");
            e.printStackTrace();
        }
    }
}
