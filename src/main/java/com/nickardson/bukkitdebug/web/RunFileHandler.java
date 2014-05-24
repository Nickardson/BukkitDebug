package com.nickardson.bukkitdebug.web;

import com.nickardson.bukkitdebug.BukkitDebug;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.mozilla.javascript.ScriptableObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

public class RunFileHandler extends AbstractHandler {

    Resource htdocs;

    public RunFileHandler(Resource root) {
        htdocs = root;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        baseRequest.setHandled(true);

        Scanner scanner = new Scanner(htdocs.getResource(baseRequest.getParameter("file")).getInputStream());
        scanner.useDelimiter("\\Z");

        String code = scanner.next();
        scanner.close();

        if (code != null && code.length() > 0) {
            BukkitDebug plugin = BukkitDebug.getPlugin(BukkitDebug.class);

            try {
                ScriptableObject scope = plugin.engine.createScope();
                scope.put("output", scope, response.getWriter());
                scope.put("args", scope, baseRequest.getParameterMap());
                String result = plugin.stringifier.stringify(plugin.engine.eval(scope, code, "code"));
                if (!result.equals("undefined")) {
                    response.getWriter().write(result);
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
