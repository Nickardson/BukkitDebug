package com.nickardson.bukkitdebug;

import com.nickardson.bukkitdebug.script.JavaScriptEngine;
import com.nickardson.bukkitdebug.script.Stringifier;
import com.nickardson.bukkitdebug.web.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.mozilla.javascript.ScriptableObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BukkitDebug extends JavaPlugin {
    final String HTDOCS_ROOT = "htdocs";
    final String HTDOCS_DESTINATION = "htdocs";

    public Server server;
    public JavaScriptEngine engine;
    public ScriptableObject global;
    public Stringifier stringifier;
    public static ConcurrentLinkedQueue<String> evals;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!getConfigurationEnabled()) {
            getLogger().severe("------------------------------------------------");
            getLogger().severe("BukkitDebug is NOT enabled!");
            getLogger().severe("Configure a password in the config, then enable.");
            getLogger().severe("------------------------------------------------");
            return;
        }

        evals = new ConcurrentLinkedQueue<String>();

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

        server = new Server(getConfigurationPort());
        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", -1);

        stringifier = new Stringifier();

        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(new RootHandler(htdocs));
        handlers.addHandler(new SubHandler("/proxy", new ProxyHandler()));
        handlers.addHandler(new SubHandler("/eval", new EvalHandler()));
        handlers.addHandler(new SubHandler("/synceval", new SyncEvalHandler()));
        handlers.addHandler(new SubHandler("/test", new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                baseRequest.setHandled(true);

                response.getWriter().println("<h1>Hello World</h1>");
            }
        }));

        // Route all requests through the security handler.
        SecureHandler secureHandler = new SecureHandler(server, handlers);
        secureHandler.addAccount(getConfigurationUsername(), getConfigurationPassword(), "user");
        server.setHandler(secureHandler);

        try {
            server.start();
        } catch (Exception e) {
            getLogger().severe("Unable to start BukkitDebug server!");
            e.printStackTrace();
        }

        engine = new JavaScriptEngine();
        global = engine.createScope();

        engine.evalStream(global, getClass().getResourceAsStream("/js/main.js"));

        new BukkitRunnable() {
            @Override
            public void run() {
                String code = evals.poll();

                if (code != null) {
                    try {
                        engine.eval(global, code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        if (server == null || server.isRunning()) {
            return;
        }

        try {
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
        } catch (Exception e) {
            getLogger().severe("Unable to stop BukkitDebug server!");
            e.printStackTrace();
        }
    }

    public boolean getConfigurationEnabled() {
        return getConfig().getBoolean("enabled", false);
    }

    public int getConfigurationPort() {
        return getConfig().getInt("port", 13370);
    }

    public String getConfigurationUsername() {
        return getConfig().getString("username", "admin");
    }

    public String getConfigurationPassword() {
        return getConfig().getString("password", "password");
    }
}
