package com.horizonpvp.astra.config;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginAwareness;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private String name;
    private FileConfiguration newConfig;
    private File configFile;
    private File dataFolder;
    private Logger logger;
    private Plugin plugin;

    public Config(Plugin plugin, String name) {
        this.name = name.toLowerCase();
        this.plugin = plugin;
        dataFolder = plugin.getDataFolder();
        configFile = new File(dataFolder, this.name + ".yml");
        logger = plugin.getLogger();
    }

    public FileConfiguration getConfig() {
        if (newConfig == null) {
            reloadConfig();
        }

        return newConfig;
    }

    public void reloadConfig() {
        newConfig = loadConfiguration(configFile);
        InputStream defConfigStream = getResource(name + ".yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig;
            if (!isStrictlyUTF8() && !FileConfiguration.UTF8_OVERRIDE) {
                defConfig = new YamlConfiguration();

                byte[] contents;
                try {
                    contents = ByteStreams.toByteArray(defConfigStream);
                } catch (IOException var7) {
                    getLogger().log(Level.SEVERE, "Unexpected failure reading " + name + ".yml", var7);
                    return;
                }

                String text = new String(contents, Charset.defaultCharset());
                if (!text.equals(new String(contents, Charsets.UTF_8))) {
                    getLogger().warning("Default system encoding may have misread " + name + ".yml from plugin jar");
                }

                try {
                    defConfig.loadFromString(text);
                } catch (InvalidConfigurationException var6) {
                    getLogger().log(Level.SEVERE, "Cannot load configuration from jar", var6);
                }
            } else {
                defConfig = loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
            }

            newConfig.setDefaults(defConfig);
        }
    }

    private boolean isStrictlyUTF8() {
        return plugin.getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8);
    }

    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException var2) {
            getLogger().log(Level.SEVERE, "Could not save config to " + configFile, var2);
        }

    }

    public boolean exists() {
        return configFile.exists();
    }

    public void saveDefaultConfig(Map<String, Object> def) {
        if (!configFile.exists()) {
            for(Map.Entry<String, Object> entry : def.entrySet()) {
                getConfig().set(entry.getKey(), entry.getValue());
            }

            saveConfig();
        }
    }

    private InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        } else {
            try {
                URL url = getClass().getClassLoader().getResource(filename);
                if (url == null) {
                    return null;
                } else {
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);
                    return connection.getInputStream();
                }
            } catch (IOException var4) {
                return null;
            }
        }
    }

    public Logger getLogger() {
        return logger;
    }

    private YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ignored) {

        } catch (IOException | InvalidConfigurationException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var4);
        }

        return config;
    }

    private YamlConfiguration loadConfiguration(Reader reader) {
        Validate.notNull(reader, "Stream cannot be null");
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(reader);
        } catch (IOException | InvalidConfigurationException var3) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", var3);
        }

        return config;
    }
}

