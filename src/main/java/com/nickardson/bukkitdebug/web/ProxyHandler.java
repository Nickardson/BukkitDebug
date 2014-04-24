package com.nickardson.bukkitdebug.web;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ProxyHandler extends AbstractHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = baseRequest.getParameter("url");
        baseRequest.setHandled(true);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));

            response.setStatus(HttpServletResponse.SC_OK);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.getWriter().println(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Malformed URL");
        }
    }
}
