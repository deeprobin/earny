package de.deeprobin.earny;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import de.deeprobin.earny.config.Configuration;
import de.deeprobin.earny.exception.ShorteningException;
import de.deeprobin.earny.logging.EarnyLogger;
import de.deeprobin.earny.manager.ShortenerManager;
import de.deeprobin.earny.shorteners.AdFlyShortener;
import de.deeprobin.earny.shorteners.AdfocusShortener;
import de.deeprobin.earny.shorteners.AdultShortener;
import de.deeprobin.earny.shorteners.IShortener;
import de.deeprobin.earny.util.ErrorReportUtil;
import de.deeprobin.earny.util.UrlUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class PluginFactory implements Runnable {

    @Getter
    private final EarnyLogger logger;

    @Getter
    private final IPlugin plugin;

    private File configFile;

    @Getter
    private Configuration configuration;

    @Getter
    private ShortenerManager shortenerManager;

    @Getter
    private ErrorReportUtil errorReportUtil;

    @Override
    public void run() {
        this.errorReportUtil = new ErrorReportUtil(this.plugin.getGameVersion(), this.plugin.getServerVersion());

        this.configFile = new File(this.plugin.getConfigDir() + "/configuration.toml");
        this.createConfig();
        this.loadConfig();

        this.shortenerManager = new ShortenerManager(this.getLogger());
        this.registerShorteners();

    }

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
                this.getLogger().warn(String.format("Cannot create configuration. Please check if the plugin has access to %s. Stack Trace: %s", this.configFile.getAbsolutePath(), this.errorReportUtil.getErrorReport(e)));
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
    }

    public String replaceChatMessage(String message){
        if(this.getConfiguration().replaceChatLinks) {
            IShortener shortener = this.getShortenerManager().getShortenerByName(this.getConfiguration().replaceChatLinksWith, false);
            if(shortener == null) {
                this.getLogger().warn(String.format("Cannot replace chat links, because we didn't find the shortener %s (Please correct your configuration file and reload).", this.getConfiguration().replaceChatLinksWith.toUpperCase()));
                return message;
            }
            for(String url : UrlUtil.extractUrls(message)) {
                try {
                    message = message.replace(url, shortener.shortUrl(url));
                } catch (ShorteningException e) {
                    this.getLogger().warn(String.format("Cannot short url %s. Please check your api key before you report the stack trace(%s).", url, this.getErrorReportUtil().getErrorReport(e)));
                }
            }
            return message;
        }
        return message;
    }
}
