package de.deeprobin.earny.platform.bukkit;

import de.deeprobin.earny.IPlugin;
import de.deeprobin.earny.PluginFactory;
import de.deeprobin.earny.logging.JavaLoggerImplementation;
import de.deeprobin.earny.platform.bukkit.command.ShortUrlCommand;
import de.deeprobin.earny.platform.bukkit.listener.ChatListener;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class EarnyPlugin extends JavaPlugin implements IPlugin {

    @Getter
    private PluginFactory factory;

    @Override
    public void onEnable() {

        this.factory = new PluginFactory(new JavaLoggerImplementation(this.getLogger()), this);
        this.factory.run();

        this.registerCommands();
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        Metrics metrics = new Metrics(this);
        this.getLogger().info("Initialized bStats metrics.");

        UpdateChecker checker = new UpdateChecker(this);
        checker.startScheduler();

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