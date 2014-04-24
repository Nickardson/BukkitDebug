package com.nickardson.bukkitdebug;

import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.File;

public class BukkitDebug extends JavaPlugin {
    @Override
    public void onEnable() {
        try {
            File htdocs = new File("htdocs");

            if (!htdocs.exists() || !htdocs.isDirectory()) {
                htdocs.mkdir();
                File zip = new File("htdocs.zip");
                FileUtils.copyResourceToFile("/htdocs.zip", zip);
                FileUtils.extractFolder("htdocs.zip");
                zip.delete();
            }

            Server server = new Server(13370);

            ResourceHandler handlerHtdocs = new ResourceHandler();
            handlerHtdocs.setDirectoriesListed(true);
            handlerHtdocs.setResourceBase("htdocs");

            HandlerCollection handlers = new HandlerCollection();
            handlers.addHandler(handlerHtdocs);

            server.setHandler(handlers);

            server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", -1);

            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
