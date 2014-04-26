package com.nickardson.bukkitdebug.web;

import com.nickardson.bukkitdebug.BukkitDebug;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.mozilla.javascript.ScriptableObject;

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

            try {
                ScriptableObject scope = plugin.engine.createScope();
                scope.put("output", scope, response.getWriter());
                Object result = plugin.engine.eval(scope, code, "code");
                response.getWriter().write(plugin.stringifier.stringify(result));
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
