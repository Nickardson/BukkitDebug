package com.nickardson.bukkitdebug.web;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;

import java.util.Collections;

public class SecureHandler extends ConstraintSecurityHandler {

    HashLoginService loginService;

    /**
     * A handler which routes all requests to a sub-handler through a login.
     * @param server The server running this handler.
     * @param handler The handler which successful requests are routed to.
     */
    public SecureHandler(Server server, Handler handler) {
        // Constraint which requires a certain role.
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__FORM_AUTH);
        constraint.setRoles(new String[]{"user", "admin"});
        constraint.setAuthenticate(true);

        ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setPathSpec("/*");
        constraintMapping.setConstraint(constraint);
        this.setConstraintMappings(Collections.singletonList(constraintMapping));

        // Set up the accounts.
        this.loginService = new HashLoginService();
        this.setLoginService(loginService);
        server.addBean(loginService);

        this.setAuthenticator(new BasicAuthenticator());
        this.setHandler(handler);
    }

    public void addAccount(String username, String password, String... roles) {
        loginService.putUser(username, new Password(password), roles);
    }
}
