package com.nickardson.bukkitdebug.web;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;

public class RelayHandler extends AbstractHandler {
    File root;
    int start;

    public RelayHandler(File dataFolder, int length) {
        root = dataFolder;
        start = length;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = baseRequest.getUri().getPath().substring(start);
        baseRequest.setHandled(true);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(root, url))));
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
