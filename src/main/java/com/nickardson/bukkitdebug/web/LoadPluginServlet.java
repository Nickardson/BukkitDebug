package com.nickardson.bukkitdebug.web;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Collection;

@MultipartConfig(location = "plugins")
public class LoadPluginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            System.out.println("Part: " + part.getName() + ", " + part.getSize());
        }

        Part filePart = request.getPart("userplugin");
        System.out.println(filePart);
        System.out.println(filePart.getContentType());
        System.out.println(filePart.getSubmittedFileName());

        File file = new File("plugins/" + filePart.getSubmittedFileName());

        OutputStream outputStream = new FileOutputStream(file);
        InputStream inputStream = filePart.getInputStream();

        byte[] bytes = new byte[1024];
        int read;
        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        inputStream.close();
        outputStream.close();

        if (file.getName().endsWith(".jar")) {
            try {
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().loadPlugin(file));
            } catch (InvalidPluginException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid plugin!");
            } catch (InvalidDescriptionException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid plugin!");
            }
        }

        response.sendRedirect("/plugins");
    }
}
