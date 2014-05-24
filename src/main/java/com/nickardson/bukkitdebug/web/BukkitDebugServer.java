package com.nickardson.bukkitdebug.web;

import com.nickardson.bukkitdebug.BukkitDebug;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;

import javax.servlet.MultipartConfigElement;
import java.net.BindException;

public class BukkitDebugServer {
    public Server server;

    private BukkitDebug getPlugin() {
        return BukkitDebug.getPlugin(BukkitDebug.class);
    }

    public BukkitDebugServer start() {
        BukkitDebug plugin = getPlugin();

        server = new Server(plugin.config.getPort());
        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", -1);

        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(new RootHandler());
        handlers.addHandler(new SubHandler("/proxy", new ProxyHandler()));
        handlers.addHandler(new SubHandler("/eval", new SyncEvalHandler()));
        handlers.addHandler(new SubHandler("/api/plugin", new PluginHandler()));
        handlers.addHandler(new SubHandler("/api/run", new RunFileHandler(plugin.htdocs)));

        // LoadPlugin handler.
        SubServlet loadPluginHandler = new SubServlet("/loadplugin", new LoadPluginServlet());
        loadPluginHandler.getHolder().getRegistration().setMultipartConfig(new MultipartConfigElement(""));
        handlers.addHandler(loadPluginHandler);

        // Route all requests through the security handler.
        SecureHandler secureHandler = new SecureHandler(server, handlers);
        secureHandler.addAccount(plugin.config.getUsername(), plugin.config.getPassword(), "user");
        server.setHandler(secureHandler);

        try {
            server.start();
        } catch (BindException e) {
            System.err.println("The BukkitDebug server port is already in use, by either another BukkitDebug instance, or another program!");
        } catch (Exception e) {
            System.err.println("Unable to start BukkitDebug server!");
            e.printStackTrace();
        }

        return this;
    }

    public BukkitDebugServer stop() {
        if (server != null) {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            server.stop();
                        } catch (Exception e) {
                            System.err.println("Unable to stop BukkitDebug server!");
                            e.printStackTrace();
                        }
                    }
                }).start();

                server.join();
            } catch (Exception e) {
                System.err.println("Unable to stop BukkitDebug server!");
                e.printStackTrace();
            }
        }

        return this;
    }
}
