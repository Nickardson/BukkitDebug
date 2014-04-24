package com.nickardson.bukkitdebug.web;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;

/**
 * A ResourceHandler which displays sub-files given a root file.
 */
public class RootHandler extends ResourceHandler {
    /**
     * @param root The base directory of the server.
     */
    public RootHandler(File root) {
        super();

        setDirectoriesListed(true);
        setBaseResource(Resource.newResource(root));
    }
}
