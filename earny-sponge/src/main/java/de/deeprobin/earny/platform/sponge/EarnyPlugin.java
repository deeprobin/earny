package de.deeprobin.earny.platform.sponge;

import de.deeprobin.earny.IPlugin;
import de.deeprobin.earny.PluginFactory;
import de.deeprobin.earny.logging.Slf4JLoggerImplementation;
import de.deeprobin.earny.platform.sponge.command.ShortUrlCommand;
import de.deeprobin.earny.platform.sponge.listener.ChatListener;
import lombok.Getter;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import com.google.inject.Inject;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;
@Plugin(id = "earny", name = "Earny", version = "1.0.0", description = "Earn money with earny")
public final class EarnyPlugin implements IPlugin {

    @Getter
    @Inject
    private Logger logger;

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
        Sponge.getEventManager().registerListeners(this, new ChatListener(this));
        this.logger.info("Earny enabled.");
    }


    @Override
    public String getConfigDir() {
        return this.configDirectory.getFileName().toString();
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
