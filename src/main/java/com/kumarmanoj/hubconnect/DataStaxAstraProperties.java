package com.kumarmanoj.hubconnect;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConfigurationProperties(prefix = "datastax.astra")
public class DataStaxAstraProperties {

    private File secureConnectHubConnectBundle;

    public File getSecureConnectHubConnectBundle() {
        return secureConnectHubConnectBundle;
    }

    public void setSecureConnectHubConnectBundle(File secureConnectHubConnectBundle) {
        this.secureConnectHubConnectBundle = secureConnectHubConnectBundle;
    }
}
