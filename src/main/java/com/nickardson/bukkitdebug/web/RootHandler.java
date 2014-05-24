package com.nickardson.bukkitdebug.web;

import com.nickardson.bukkitdebug.BukkitDebug;
import org.eclipse.jetty.server.handler.ResourceHandler;

/**
 * A ResourceHandler which displays sub-files given a root file.
 */
public class RootHandler extends ResourceHandler {
    public RootHandler() {
        super();

        setDirectoriesListed(true);
        setBaseResource(BukkitDebug.getPlugin(BukkitDebug.class).htdocs);
    }
}
