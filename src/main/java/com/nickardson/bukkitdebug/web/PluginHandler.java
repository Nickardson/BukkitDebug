package com.nickardson.bukkitdebug.web;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PluginHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        baseRequest.setHandled(true);

        String action = request.getParameter("action");
        Plugin plugin = Bukkit.getPluginManager().getPlugin(request.getParameter("name"));

        if (action.equals("toggle")) {
            if (plugin.isEnabled()) {
                disable(plugin, response);
            } else {
                enable(plugin, response);
            }
        } else if (action.equals("enable")) {
            enable(plugin, response);
        } else if (action.equals("disable")) {
            disable(plugin, response);
        } else if (action.equals("reload")) {
            reload(plugin, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No such action");
        }
    }

    private void enable(Plugin plugin, HttpServletResponse response) throws IOException {
        Bukkit.getPluginManager().enablePlugin(plugin);
        response.getWriter().write("true");
    }

    private void disable(Plugin plugin, HttpServletResponse response) throws IOException {
        Bukkit.getPluginManager().disablePlugin(plugin);
        response.getWriter().write("false");
    }

    private void reload(Plugin plugin, HttpServletResponse response) throws IOException {
        Bukkit.getPluginManager().disablePlugin(plugin);
        Bukkit.getPluginManager().enablePlugin(plugin);
        response.getWriter().write("true");
    }
}
