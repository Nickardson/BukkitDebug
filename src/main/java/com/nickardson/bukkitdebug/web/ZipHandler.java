package com.nickardson.bukkitdebug.web;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.util.zip.ZipFile;

/**
 * Reads from a zip file.
 * example:
 * /api/zip/path/to/file.zip/@/path/to/file.txt
 */
public class ZipHandler extends AbstractHandler {

    File file;
    int length;

    public ZipHandler(File dataFolder, int stringLength) {
        file = dataFolder;
        length = stringLength;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] url = baseRequest.getUri().getPath().substring(length).split("/@/");
        baseRequest.setHandled(true);

        ZipFile zipFile = new ZipFile(new File(file, url[0]));

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(zipFile.getInputStream(zipFile.getEntry(url[1]))));
            response.setStatus(HttpServletResponse.SC_OK);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.getWriter().println(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Malformed URL");
        } finally {
            zipFile.close();
        }
    }
}
