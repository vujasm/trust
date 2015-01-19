package com.inn.trusthings.rest.app;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfiguration extends ResourceConfig {
    public ApplicationConfiguration() {
        packages("com.inn.trusthings.rest.service");
    }
}
