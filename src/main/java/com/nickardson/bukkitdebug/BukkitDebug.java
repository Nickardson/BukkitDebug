package com.nickardson.bukkitdebug;

import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;
import java.util.List;

public class BukkitDebug extends JavaPlugin {
    final String HTDOCS_ROOT = "htdocs";
    final String HTDOCS_DESTINATION = "htdocs";

    Server server;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        try {
            File htdocs = new File(getDataFolder(), HTDOCS_DESTINATION);

            if (!htdocs.exists() || !htdocs.isDirectory()) {
                htdocs.mkdir();
                List<String> dir = FileUtils.dir(getFile(), HTDOCS_ROOT);
                for (String filename : dir) {
                    File destination = new File(htdocs, filename.substring(HTDOCS_ROOT.length() + 1));
                    destination.getParentFile().mkdirs();
                    FileUtils.copyResourceToFile("/" + filename, destination);
                }
            }

            server = new Server(13370);

            ResourceHandler handlerHtdocs = new ResourceHandler();
            handlerHtdocs.setDirectoriesListed(true);
            handlerHtdocs.setBaseResource(Resource.newResource(htdocs));

            HandlerCollection handlers = new HandlerCollection();
            handlers.addHandler(handlerHtdocs);

            server.setHandler(handlers);

            server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", -1);

            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
