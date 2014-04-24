package com.nickardson.bukkitdebug.web;

import com.nickardson.bukkitdebug.BukkitDebug;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EvalHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        baseRequest.setHandled(true);

        String code = baseRequest.getParameter("code");

        if (code != null && code.length() > 0) {
            response.setStatus(HttpServletResponse.SC_OK);
            BukkitDebug.evals.add(code);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
