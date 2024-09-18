/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.wodt.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public final class ConfigProperties {

    private static final String CONFIG_PROPERTIES_FILE = "config.properties";

    private static ConfigProperties instance;

    private final String namespace;
    private final String endpoint;

    private final String username;
    private final String password;

    private final String clientId;
    private final String clientSecret;
    private final String tokenEndpoint;

    private final String proxyHost;
    private final String proxyPort;
    private final String proxyPrincipal;
    private final String proxyPassword;

    private ConfigProperties(final String namespace,
            final String endpoint,
            final String username,
            final String password,
            final String clientId,
            final String clientSecret,
            final String tokenEndpoint,
            final String proxyHost,
            final String proxyPort,
            final String proxyPrincipal,
            final String proxyPassword) {
        this.namespace = namespace;
        this.endpoint = endpoint;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenEndpoint = tokenEndpoint;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyPrincipal = proxyPrincipal;
        this.proxyPassword = proxyPassword;
    }

    public static ConfigProperties getInstance() {
        if (null == instance) {
            final Properties props = readPropertiesFromFile(CONFIG_PROPERTIES_FILE);

            final String namespace = props.getProperty("namespace");
            final String endpoint = props.getProperty("endpoint");

            final String username = props.getProperty("username");
            final String password = props.getProperty("password");
            final String clientId = props.getProperty("clientId");
            final String clientSecret = props.getProperty("clientSecret");
            final String tokenEndpoint = props.getProperty("tokenEndpoint");

            final String proxyHost = props.getProperty("proxyHost");
            final String proxyPort = props.getProperty("proxyPort");
            final String proxyPrincipal = props.getProperty("proxyPrincipal");
            final String proxyPassword = props.getProperty("proxyPassword");

            instance = new ConfigProperties(namespace, endpoint, username, password, clientId, clientSecret,
                    tokenEndpoint, proxyHost, proxyPort, proxyPrincipal, proxyPassword);
        }

        return instance;
    }

    public static Properties readPropertiesFromFile(String file) {
        final Properties props = new Properties(System.getProperties());
        try (final InputStream in = ConfigProperties.class.getClassLoader()
                .getResourceAsStream(file)) {
            props.load(in);
        } catch (final IOException ioe) {
            throw new IllegalStateException(
                    "File " + file + " could not be opened but is required for this example: "
                            + ioe.getMessage());
        }
        return props;
    }

    Optional<String> getEndpoint() {
        return Optional.ofNullable(endpoint);
    }

    String getEndpointOrThrow() {
        return getEndpoint().orElseThrow(IllegalStateException::new);
    }

    Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    String getUsernameOrThrow() {
        return getUsername().orElseThrow(IllegalStateException::new);
    }

    Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    String getPasswordOrThrow() {
        return getPassword().orElseThrow(IllegalStateException::new);
    }

    Optional<String> getClientId() {
        return Optional.ofNullable(clientId);
    }

    String getClientIdOrThrow() {
        return getClientId().orElseThrow(IllegalStateException::new);
    }

    Optional<String> getClientSecret() {
        return Optional.ofNullable(clientSecret);
    }

    String getClientSecretOrThrow() {
        return getClientSecret().orElseThrow(IllegalStateException::new);
    }

    Optional<String> getTokenEndpoint() {
        return Optional.ofNullable(tokenEndpoint);
    }

    String getTokenEndpointOrThrow() {
        return getTokenEndpoint().orElseThrow(IllegalStateException::new);
    }

    Optional<String> getProxyHost() {
        return Optional.ofNullable(proxyHost);
    }

    Optional<String> getProxyPort() {
        return Optional.ofNullable(proxyPort);
    }

    Optional<String> getProxyPrincipal() {
        return Optional.ofNullable(proxyPrincipal);
    }

    Optional<String> getProxyPassword() {
        return Optional.ofNullable(proxyPassword);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConfigProperties that = (ConfigProperties) o;
        return Objects.equals(namespace, that.namespace) &&
                Objects.equals(endpoint, that.endpoint) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(clientSecret, that.clientSecret) &&
                Objects.equals(tokenEndpoint, that.tokenEndpoint) &&
                Objects.equals(proxyHost, that.proxyHost) &&
                Objects.equals(proxyPort, that.proxyPort) &&
                Objects.equals(proxyPrincipal, that.proxyPrincipal) &&
                Objects.equals(proxyPassword, that.proxyPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, endpoint, username, password, clientId, clientSecret, tokenEndpoint,
                proxyHost, proxyPort, proxyPrincipal, proxyPassword);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" +
                ", namespace=" + namespace +
                ", endpoint=" + endpoint +
                ", username=" + username +
                ", password=" + password +
                ", clientId=" + clientId +
                ", clientSecret=" + clientSecret +
                ", tokenEndpoint=" + tokenEndpoint +
                ", proxyHost=" + proxyHost +
                ", proxyPort=" + proxyPort +
                ", proxyPrincipal=" + proxyPrincipal +
                ", proxyPassword=" + proxyPassword +
                "]";
    }

}
