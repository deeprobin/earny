package de.deeprobin.earny.platform.bungee;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import de.deeprobin.earny.config.Configuration;
import de.deeprobin.earny.manager.ShortenerManager;
import de.deeprobin.earny.platform.bungee.listener.ChatListener;
import de.deeprobin.earny.shorteners.AdflyShortener;
import de.deeprobin.earny.shorteners.AdfocusShortener;
import de.deeprobin.earny.shorteners.AdultShortener;
import de.deeprobin.earny.util.ErrorReportUtil;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.io.IOException;

public class EarnyPlugin extends Plugin {

    private File configFile;

    @Getter
    private Configuration configuration;

    @Getter
    private ShortenerManager shortenerManager;

    @Getter
    private ErrorReportUtil errorReportUtil;

    @Override
    public void onEnable() {

        this.errorReportUtil = new ErrorReportUtil("Different by player", this.getProxy().getVersion());

        this.configFile = new File(this.getDataFolder().getAbsolutePath() + "/configuration.toml");
        this.createConfig();
        this.loadConfig();

        this.getProxy().getPluginManager().registerListener(this, new ChatListener(this));

        this.shortenerManager = new ShortenerManager(this.getLogger());
        this.registerShorteners();

        Metrics metrics = new Metrics(this);
        this.getLogger().info("Initialized bStats metrics.");

        this.getLogger().info("Done.");
    }

    private void createConfig() {
        if (!this.configFile.exists()) {
            if (this.configFile.getParentFile().mkdirs()) {
                this.getLogger().info("Created plugin directory.");
            }
            TomlWriter writer = new TomlWriter();
            try {
                writer.write(new Configuration(AdflyShortener.USER_ID, AdflyShortener.API_KEY, AdultShortener.USER_ID, AdultShortener.API_KEY, AdfocusShortener.API_KEY, true, "adfocus"), this.configFile);
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
        this.shortenerManager.registerShortener(new AdflyShortener(this.configuration.adFlyUserId, this.configuration.adFlyApiKey));
        this.shortenerManager.registerShortener(new AdultShortener(this.configuration.adultXyzUserId, this.configuration.adultXyzKey));
        this.shortenerManager.registerShortener(new AdfocusShortener(this.configuration.adFocusApiKey));
    }
}
