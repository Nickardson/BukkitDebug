package com.nickardson.bukkitdebug.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;

public class SubHandler extends ContextHandler {
    public SubHandler(String sub, Handler handler) {
        super(sub);
        setHandler(handler);
    }
}
