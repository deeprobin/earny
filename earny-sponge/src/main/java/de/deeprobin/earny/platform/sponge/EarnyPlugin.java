package de.deeprobin.earny.platform.sponge;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import de.deeprobin.earny.IPlugin;
import de.deeprobin.earny.PluginFactory;
import de.deeprobin.earny.config.Configuration;
import de.deeprobin.earny.exception.ShorteningException;
import de.deeprobin.earny.logging.Slf4JLoggerImplementation;
import de.deeprobin.earny.manager.ShortenerManager;
import de.deeprobin.earny.platform.sponge.command.ShortUrlCommand;
import de.deeprobin.earny.shorteners.AdflyShortener;
import de.deeprobin.earny.shorteners.AdfocusShortener;
import de.deeprobin.earny.shorteners.AdultShortener;
import de.deeprobin.earny.shorteners.IShortener;
import de.deeprobin.earny.util.ErrorReportUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.MinecraftVersion;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(id = "earny", name = "Earny", version = "1.0.0", description = "Earn money with earny")
public final class EarnyPlugin implements IPlugin {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}\\.([a-z]+)?$",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    @Getter
    @Inject
    private Logger logger;

    /*
    @Getter
    private Configuration configuration;

    @Getter
    private ShortenerManager shortenerManager;

    @Getter
    private ErrorReportUtil errorReportUtil;

    private File configFile;*/

    @Inject
    private Game game;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDirectory;

    @Getter
    private PluginFactory factory;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        this.factory = new PluginFactory(new Slf4JLoggerImplementation(this.getLogger()), this);
        this.factory.run();

        PluginContainer api = this.game.getPlatform().getContainer(Platform.Component.API);
        PluginContainer impl = this.game.getPlatform().getContainer(Platform.Component.IMPLEMENTATION);
        PluginContainer gameContainer = this.game.getPlatform().getContainer(Platform.Component.GAME);
/*
        this.errorReportUtil = new ErrorReportUtil(gameContainer.getVersion().orElse("Not specified"), String.format("api: %s - impl: %s", api.getVersion().orElse("Not specified"), impl.getVersion().orElse("Not specified")));
        this.configFile = new File(configDirectory + "/configuration.toml");
        this.createConfig();
        this.loadConfig();

        this.shortenerManager = new ShortenerManager(this.getOtherLogger(logger.getName(), java.util.logging.Logger.class));
        this.registerShorteners();*/
        CommandSpec shortUrlCommandSpec = CommandSpec.builder()
                .description(Text.of("Hello World Command"))
                .permission("earny.command.short-url")
                .executor(new ShortUrlCommand(this))
                .arguments(
                        GenericArguments.string(Text.of("type")),
                        GenericArguments.url(Text.of("url"))
                )
                .build();

        Sponge.getCommandManager().register(this, shortUrlCommandSpec, "short-url", "shortUrl", "earny-short");
        this.logger.info("Earny enabled.");
    }

    @Listener
    public void onChat(MessageEvent event){
        if(this.getFactory().getConfiguration().replaceChatLinks) {
            return;
        }

        IShortener shortener = null;

        for(IShortener s : this.getFactory().getShortenerManager().getShorteners()) {
            for(String identifier : s.getIdentifiers()){
                if(identifier.equalsIgnoreCase(this.getFactory().getConfiguration().replaceChatLinksWith)){
                    shortener = s;
                    break;
                }
            }
        }

        if(shortener == null){
            this.getLogger().warn(String.format("Cannot replace chat links, because we didn't find the shortener %s (Please correct your configuration file and reload).", this.getFactory().getConfiguration().replaceChatLinksWith.toUpperCase()));
            return;
        }

        String message = event.getMessage().toPlain();
        Matcher matcher = URL_PATTERN.matcher(message);
        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            urls.add(message.substring(matchStart, matchEnd));
        }

        for(String url : urls){
            try {
                message = message.replace(url, shortener.shortUrl(url));
            } catch (ShorteningException e) {
                this.getLogger().warn(String.format("Cannot short url %s. Please check your api key before you report the stack trace(%s).", url, this.getFactory().getErrorReportUtil().getErrorReport(e)));
            }
        }

        event.setMessage(Text.of(message));
    }
/*
    private void registerShorteners() {
        this.shortenerManager.registerShortener(new AdflyShortener(this.configuration.adFlyUserId, this.configuration.adFlyApiKey));
        this.shortenerManager.registerShortener(new AdultShortener(this.configuration.adultXyzUserId, this.configuration.adultXyzKey));
        this.shortenerManager.registerShortener(new AdfocusShortener(this.configuration.adFocusApiKey));
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
                this.getLogger().warn(String.format("Cannot create configuration. Please check if the plugin has access to %s. Stack Trace: %s", this.configFile.getAbsolutePath(), this.errorReportUtil.getErrorReport(e)));
            }
        }
    }

    private void loadConfig() {
        Toml toml = new Toml().read(this.configFile);
        this.configuration = toml.to(Configuration.class);
        this.getLogger().info("Configuration loaded.");
    }

    public <T> T getOtherLogger(final String loggerName, final Class<T> loggerClass) {
        final org.slf4j.Logger logger = LoggerFactory.getLogger(loggerName);
        try {
            final Class<? extends org.slf4j.Logger> loggerIntrospected = logger.getClass();
            final Field[] fields = loggerIntrospected.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                final String fieldName = fields[i].getName();
                if (fieldName.equals("logger")) {
                    fields[i].setAccessible(true);
                    return loggerClass.cast(fields[i].get(logger));
                }
            }
        } catch (final Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
*/

    @Override
    public String getConfigDir() {
        return null;
    }

    @Override
    public String getServerVersion() {
        PluginContainer api = this.game.getPlatform().getContainer(Platform.Component.API);
        PluginContainer impl = this.game.getPlatform().getContainer(Platform.Component.IMPLEMENTATION);
        return String.format("api: %s - impl: %s", api.getVersion().orElse("Not specified"), impl.getVersion().orElse("Not specified"));
    }

    @Override
    public String getGameVersion() {
        PluginContainer gameContainer = this.game.getPlatform().getContainer(Platform.Component.GAME);
        return gameContainer.getVersion().orElse("Not specified");
    }
}
