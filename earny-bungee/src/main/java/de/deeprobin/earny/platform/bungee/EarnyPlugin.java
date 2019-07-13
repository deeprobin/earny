package de.deeprobin.earny.platform.bungee;

import de.deeprobin.earny.IPlugin;
import de.deeprobin.earny.PluginFactory;
import de.deeprobin.earny.logging.JavaLoggerImplementation;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.io.File;

public class EarnyPlugin extends Plugin implements IPlugin {

    private File configFile;
/*
    @Getter
    private Configuration configuration;

    @Getter
    private ShortenerManager shortenerManager;

    @Getter
    private ErrorReportUtil errorReportUtil;
*/

@Getter
private PluginFactory factory;
    @Override
    public void onEnable() {
        this.factory = new PluginFactory(new JavaLoggerImplementation(this.getLogger()), this);
        this.factory.run();
/*
        this.errorReportUtil = new ErrorReportUtil("Different by player", this.getProxy().getVersion());

        this.configFile = new File(this.getDataFolder().getAbsolutePath() + "/configuration.toml");
        this.createConfig();
        this.loadConfig();

        this.getProxy().getPluginManager().registerListener(this, new ChatListener(this));

        this.shortenerManager = new ShortenerManager(this.getLogger());
        this.registerShorteners();
*/
        Metrics metrics = new Metrics(this);
        this.getLogger().info("Initialized bStats metrics.");

        this.getLogger().info("Done.");
    }
/*
    private void createConfig() {
        if (!this.configFile.exists()) {
            if (this.configFile.getParentFile().mkdirs()) {
                this.getLogger().info("Created plugin directory.");
            }
            TomlWriter writer = new TomlWriter();
            try {
                writer.write(new Configuration(AdFlyShortener.USER_ID, AdFlyShortener.API_KEY, AdultShortener.USER_ID, AdultShortener.API_KEY, AdfocusShortener.API_KEY, true, "adfocus"), this.configFile);
                this.getLogger().info("Created default configuration (please change your api credentials).");
            } catch (IOException e) {
                e.printStackTrace();
                this.getLogger().warning(String.format("Cannot create configuration. Please check if the plugin has access to %s. Stack Trace: %s", this.configFile.getAbsolutePath(), this.errorReportUtil.getErrorReport(e)));
            }
        }
    }

    private void loadConfig() {
        Toml toml = new Toml().read(this.configFile);
        this.configuration = toml.to(Configuration.class);
        this.getLogger().info("Configuration loaded.");
    }

    private void registerShorteners() {
        this.shortenerManager.registerShortener(new AdFlyShortener(this.configuration.adFlyUserId, this.configuration.adFlyApiKey));
        this.shortenerManager.registerShortener(new AdultShortener(this.configuration.adultXyzUserId, this.configuration.adultXyzKey));
        this.shortenerManager.registerShortener(new AdfocusShortener(this.configuration.adFocusApiKey));
    }*/

    @Override
    public String getConfigDir() {
        return this.getDataFolder().getAbsolutePath();
    }

    @Override
    public String getServerVersion() {
        return this.getProxy().getVersion();
    }

    @Override
    public String getGameVersion() {
        return "Different by player";
    }
}
