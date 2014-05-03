package com.nickardson.bukkitdebug.web;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class SubServlet extends ServletContextHandler {
    ServletHolder holder;
    Servlet servlet;

    public SubServlet(String context, Servlet servlet) {
        this.servlet = servlet;

        setContextPath(context);
        holder = new ServletHolder(servlet);
        addServlet(holder, "/");
    }

    public ServletHolder getHolder() {
        return holder;
    }
}
