package de.deeprobin.earny.platform.bukkit;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import de.deeprobin.earny.IPlugin;
import de.deeprobin.earny.PluginFactory;
import de.deeprobin.earny.config.Configuration;
import de.deeprobin.earny.logging.JavaLoggerImplementation;
import de.deeprobin.earny.manager.ShortenerManager;
import de.deeprobin.earny.platform.bukkit.command.ShortUrlCommand;
import de.deeprobin.earny.platform.bukkit.listener.ChatListener;
import de.deeprobin.earny.shorteners.AdflyShortener;
import de.deeprobin.earny.shorteners.AdfocusShortener;
import de.deeprobin.earny.shorteners.AdultShortener;
import de.deeprobin.earny.util.ErrorReportUtil;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class EarnyPlugin extends JavaPlugin implements IPlugin {

    private File configFile;
/*
    @Getter
    private Configuration configuration;

    @Getter
    private ShortenerManager shortenerManager;

    @Getter
    private ErrorReportUtil errorReportUtil;*/

    @Getter
    private PluginFactory factory;

    @Override
    public void onEnable() {

        this.factory = new PluginFactory(new JavaLoggerImplementation(this.getLogger()), this);
        this.factory.run();
        /*
        this.errorReportUtil = new ErrorReportUtil(this.getServer().getBukkitVersion(), this.getServer().getVersion());

        this.configFile = new File(this.getDataFolder().getAbsolutePath() + "/configuration.toml");
        this.createConfig();
        this.loadConfig();

        this.shortenerManager = new ShortenerManager(this.getLogger());
        this.registerShorteners();*/

        this.registerCommands();
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        Metrics metrics = new Metrics(this);
        this.getLogger().info("Initialized bStats metrics.");

        this.getLogger().info("Done.");
    }

    private void registerCommands() {
        PluginCommand shortUrlPluginCommand = this.getCommand("short-url");
        assert shortUrlPluginCommand != null;
        ShortUrlCommand shortUrlCommand = new ShortUrlCommand(this);
        shortUrlPluginCommand.setExecutor(shortUrlCommand);
        shortUrlPluginCommand.setTabCompleter(shortUrlCommand);
    }

    @Override
    public String getConfigDir() {
        return this.getDataFolder().getAbsolutePath();
    }

    @Override
    public String getServerVersion() {
        return this.getServer().getBukkitVersion();
    }

    @Override
    public String getGameVersion() {
        return this.getServer().getVersion();
    }
}