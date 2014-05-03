package com.nickardson.bukkitdebug;

import org.bukkit.configuration.ConfigurationSection;

public class Config {
    ConfigurationSection config;

    public Config(ConfigurationSection config) {
        this.config = config;
    }

    public boolean isEnabled() {
        return config.getBoolean("enabled", false);
    }

    public boolean isLogging() {
        return config.getBoolean("internal-logging", false);
    }

    public int getPort() {
        return config.getInt("port", 13370);
    }

    public String getUsername() {
        return config.getString("username", "admin");
    }

    public String getPassword() {
        return config.getString("password", "password");
    }
}