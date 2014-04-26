package com.nickardson.bukkitdebug.web;

import com.nickardson.bukkitdebug.BukkitDebug;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SyncEvalHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        baseRequest.setHandled(true);

        String code = baseRequest.getParameter("code");

        if (code != null && code.length() > 0) {
            BukkitDebug plugin = BukkitDebug.getPlugin(BukkitDebug.class);

            Object result = plugin.engine.eval(plugin.global, code, "remote-sync-code");
            response.getWriter().write(plugin.stringifier.stringify(result));
        }
    }
}
